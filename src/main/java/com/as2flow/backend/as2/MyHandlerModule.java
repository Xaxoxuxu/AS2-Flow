package com.as2flow.backend.as2;

import com.as2flow.backend.entity.AS2Message;
import com.as2flow.backend.entity.Partnership;
import com.as2flow.backend.service.AS2MessageService;
import com.as2flow.backend.service.PartnershipService;
import com.helger.as2lib.exception.AS2Exception;
import com.helger.as2lib.message.IMessage;
import com.helger.as2lib.processor.module.AbstractProcessorModule;
import com.helger.as2lib.processor.storage.IProcessorStorageModule;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;

public class MyHandlerModule extends AbstractProcessorModule implements IProcessorStorageModule
{
    private static final Logger LOGGER = LoggerFactory.getLogger(MyHandlerModule.class);

    final PartnershipService partnershipService;
    final AS2MessageService as2MessageService;

    public MyHandlerModule(PartnershipService partnershipService, AS2MessageService as2MessageService)
    {
        this.partnershipService = partnershipService;
        this.as2MessageService = as2MessageService;
    }

    public boolean canHandle(final String sAction, final IMessage aMsg, final Map<String, Object> aOptions)
    {
        return sAction.equals(DO_STORE);
    }

    public void handle(final String sAction, final IMessage aMsg, final Map<String, Object> aOptions) throws AS2Exception
    {
        // TODO: do this BEFORE DO_STORE
        //if (!aMsg.partnership().getSubject().equals(aMsg.getSubject()))
        //{
       //     throw new AS2Exception("No partnership with subject: '" + aMsg.getSubject() + "' found!");
        //}

        // TODO e.g. save to DB
        LOGGER.info("Received AS2 message");

        Partnership matchingPartnershipFromDb = partnershipService.findAll(aMsg.partnership().getName()).get(0);
        AS2Message messageStoreEntity = new AS2Message(matchingPartnershipFromDb);
        as2MessageService.save(messageStoreEntity);
        LOGGER.info("Stored AS2 message to DB");
    }
}
