package com.as2flow.application.as2;

import com.helger.as2lib.exception.AS2Exception;
import com.helger.as2lib.message.IMessage;
import com.helger.as2lib.processor.module.AbstractProcessorModule;
import com.helger.as2lib.processor.storage.IProcessorStorageModule;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;

public class MyHandlerModule extends AbstractProcessorModule implements IProcessorStorageModule
{
  private static final Logger LOGGER = LoggerFactory.getLogger (MyHandlerModule.class);

  public boolean canHandle (final String sAction, final IMessage aMsg, final Map <String, Object> aOptions)
  {
    return sAction.equals (DO_STORE);
  }

  public void handle (final String sAction, final IMessage aMsg, final Map <String, Object> aOptions) throws AS2Exception
  {
    // TODO e.g. save to DB
    LOGGER.info ("Received AS2 message");
    LOGGER.info(aMsg.getAsString());
    if (aOptions != null)
      aOptions.forEach((k,v) -> LOGGER.info(k + ":" + v));
    LOGGER.info(sAction);
    LOGGER.info(aMsg.getHeader("content-disposition"));
  }
}
