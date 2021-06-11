package com.as2flow.backend.as2;

import com.helger.as2lib.message.AS2Message;
import com.helger.as2lib.message.IMessage;
import com.helger.as2lib.processor.receiver.AbstractDirectoryPollingModule;

import javax.annotation.Nonnull;

public class AS2DirectoryPollingModule extends AbstractDirectoryPollingModule
{
    @Override
    @Nonnull
    protected IMessage createMessage()
    {
        return new AS2Message();
    }
}