package com.as2flow.backend.as2;

import com.as2flow.backend.service.PartnershipService;
import com.helger.as2lib.message.AS2Message;
import com.helger.as2lib.message.IMessage;
import com.helger.as2lib.processor.receiver.AbstractDirectoryPollingModule;

import javax.annotation.Nonnull;

public class AS2DirectoryPollingModule extends AbstractDirectoryPollingModule
{
    private final PartnershipService partnershipService;

    // TODO: polling dirs to db
    public AS2DirectoryPollingModule(PartnershipService partnershipService)
    {
        this.partnershipService = partnershipService;

        attrs().putIn(ATTR_MIMETYPE, "application/EDI-X12");
        attrs().putIn(ATTR_SENDFILENAME, "true");
        setInterval(1);
    }

    @Override
    @Nonnull
    protected IMessage createMessage()
    {
        return new AS2Message();
    }
}