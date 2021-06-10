/**
 * Copyright (C) 2015-2021 Philip Helger (www.helger.com)
 * philip[at]helger[dot]com
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.as2flow.backend.as2;

import com.as2flow.backend.service.AS2MessageService;
import com.as2flow.backend.service.PartnershipService;
import com.helger.as2lib.cert.CertificateFactory;
import com.helger.as2lib.crypto.ECryptoAlgorithmCrypt;
import com.helger.as2lib.crypto.ECryptoAlgorithmSign;
import com.helger.as2lib.exception.AS2Exception;
import com.helger.as2lib.partner.Partnership;
import com.helger.as2lib.processor.DefaultMessageProcessor;
import com.helger.as2lib.processor.resender.DirectoryResenderModule;
import com.helger.as2lib.processor.sender.AS2SenderModule;
import com.helger.as2lib.processor.sender.AsynchMDNSenderModule;
import com.helger.as2lib.processor.storage.MessageFileModule;
import com.helger.as2lib.session.AS2Session;
import com.helger.as2servlet.AbstractAS2ReceiveXServletHandler;
import com.helger.as2servlet.util.AS2ServletReceiverModule;
import com.helger.commons.collection.impl.ICommonsMap;
import com.helger.security.keystore.EKeyStoreType;

import javax.annotation.Nonnull;
import javax.servlet.ServletException;

import static com.helger.as2lib.processor.receiver.AbstractDirectoryPollingModule.*;
import static com.helger.as2lib.processor.resender.AbstractActiveResenderModule.ATTR_RESEND_DELAY_SECONDS;
import static com.helger.as2lib.processor.resender.DirectoryResenderModule.ATTR_RESEND_DIRECTORY;

public class AS2ReceiveXServletHandlerCodeConfig extends AbstractAS2ReceiveXServletHandler
{
    final PartnershipService partnershipService;
    final AS2MessageService as2MessageService;

    public AS2ReceiveXServletHandlerCodeConfig(PartnershipService partnershipService, AS2MessageService as2MessageService)
    {
        this.partnershipService = partnershipService;
        this.as2MessageService = as2MessageService;
    }

    @Override
    @Nonnull
    protected AS2Session createAS2Session(@Nonnull final ICommonsMap<String, String> aInitParams) throws AS2Exception, ServletException
    {
        final AS2Session aSession = new AS2Session();
        {
            final CertificateFactory aCertFactory = new CertificateFactory();
            aCertFactory.setKeyStoreType(EKeyStoreType.PKCS12);
            aCertFactory.setFilename("config/certs.p12");
            aCertFactory.setPassword("test");
            aCertFactory.setSaveChangesToFile(false);
            aCertFactory.initDynamicComponent(aSession, null);
            aSession.setCertificateFactory(aCertFactory);
        }

        {
            final H2PartnershipFactory h2PartnershipFactory = new H2PartnershipFactory(partnershipService);

            Partnership pReceive = new Partnership("OpenAS2A-OpenAS2B");
            pReceive.setSenderAS2ID("OpenAS2A");
            pReceive.setReceiverAS2ID("OpenAS2B");
            pReceive.setProtocol("as2");
            pReceive.setSubject("From OpenAS2A to OpenAS2B");
            //pReceive.setAS2URL("http://localhost:8081");
            //pReceive.setAS2MDNTo("http://localhost:8081");
            //pReceive.setAS2MDNOptions("signed-receipt-protocol=optional, pkcs7-signature; signed-receipt-micalg=optional, md5");
            pReceive.setEncryptAlgorithm(ECryptoAlgorithmCrypt.CRYPT_3DES);
            pReceive.setSigningAlgorithm(ECryptoAlgorithmSign.DIGEST_MD5);
            pReceive.setReceiverX509Alias("OpenAS2B_alias");
            pReceive.setSenderX509Alias("OpenAS2A_alias");
            //pReceive.setSenderEmail("sender@sender.com");
            //pReceive.setReceiverEmail("receiver@receiver.com");

            Partnership pSend = new Partnership("OpenAS2B-OpenAS2A");
            pSend.setSenderAS2ID("OpenAS2B");
            pSend.setReceiverAS2ID("OpenAS2A");
            pSend.setProtocol("as2");
            pSend.setSubject("From OpenAS2B to OpenAS2A");
            pSend.setAS2URL("http://localhost:8081");
            //pSend.setAS2MDNTo("http://localhost:8081");
            //pSend.setAS2MDNOptions("signed-receipt-protocol=optional, pkcs7-signature; signed-receipt-micalg=optional, md5");
            pSend.setEncryptAlgorithm(ECryptoAlgorithmCrypt.CRYPT_3DES);
            pSend.setSigningAlgorithm(ECryptoAlgorithmSign.DIGEST_MD5);
            pSend.setReceiverX509Alias("OpenAS2A_alias");
            pSend.setSenderX509Alias("OpenAS2B_alias");
            pSend.setSenderEmail("sender@sender.com");
            //pSend.setReceiverEmail("receiver@receiver.com");

            h2PartnershipFactory.addPartnership(pReceive);
            h2PartnershipFactory.addPartnership(pSend);
            h2PartnershipFactory.initDynamicComponent(aSession, null);
            aSession.setPartnershipFactory(h2PartnershipFactory);
        }

        {
            final DefaultMessageProcessor aMessageProcessor = new DefaultMessageProcessor();
            aMessageProcessor.setPendingMDNFolder("data/pendingMDN");
            aMessageProcessor.setPendingMDNInfoFolder("data/pendinginfoMDN");
            aMessageProcessor.initDynamicComponent(aSession, null);
            aSession.setMessageProcessor(aMessageProcessor);

            /** Below module is used to send async mdn */
            {
                final AsynchMDNSenderModule aModule = new AsynchMDNSenderModule();
                aModule.initDynamicComponent(aSession, null);
                aMessageProcessor.addModule(aModule);
            }

            /** A module storing the message. */
            {
                final MyHandlerModule aModule = new MyHandlerModule(partnershipService, as2MessageService);
                aModule.initDynamicComponent(aSession, null);
                aMessageProcessor.addModule(aModule);
            }

            /** A module storing the message. */
            {
                final MessageFileModule aModule = new MessageFileModule();
                aModule.setFilename("data/inbox/$msg.sender.as2_id$-$msg.receiver.as2_id$-$msg.headers.message-id$");
                aModule.setHeaderFilename("data/inbox/msgheaders/$date.uuuu$/$date.MM$/$msg.sender.as2_id$-$msg.receiver.as2_id$-$msg.headers.message-id$");
                aModule.setProtocol("as2");
                aModule.setTempDir("data/temp");
                aModule.setCharsetName("utf-8");
                aModule.initDynamicComponent(aSession, null);
                aMessageProcessor.addModule(aModule);
            }

            {
                // TODO: polling dirs to db
                final AS2DirectoryPollingModule aModule = new AS2DirectoryPollingModule(partnershipService);
                aModule.attrs().putIn(ATTR_OUTBOX_DIRECTORY, "data/toOpenAS2A");
                aModule.attrs().putIn(ATTR_ERROR_DIRECTORY, "data/toOpenAS2A/error");
                aModule.attrs().putIn(ATTR_DEFAULTS, "sender.as2_id=OpenAS2B, receiver.as2_id=OpenAS2A");
                aModule.initDynamicComponent(aSession, null);
                aMessageProcessor.addModule(aModule);
            }

            {
                final DirectoryResenderModule aModule = new DirectoryResenderModule();
                aModule.attrs().putIn(ATTR_RESEND_DIRECTORY, "data/resend");
                aModule.attrs().putIn(ATTR_ERROR_DIRECTORY, "data/resend/error");
                aModule.attrs().putIn(ATTR_RESEND_DELAY_SECONDS, 5);
                aModule.initDynamicComponent(aSession, null);
                aMessageProcessor.addModule(aModule);
            }

            // start polling
            aMessageProcessor.startActiveModules();

            /**
             * Required to receive messages port is required internally - simply
             * ignore it for servlets
             */
            {
                final AS2ServletReceiverModule aModule = new AS2ServletReceiverModule();
                //aModule.setPort(8080);
                aModule.setErrorDirectory("data/inbox/error");
                aModule.setErrorFormat("$msg.sender.as2_id$, $msg.receiver.as2_id$, $msg.headers.message-id$");
                aModule.initDynamicComponent(aSession, null);
                aMessageProcessor.addModule(aModule);
            }

            /** Only needed to receive asynchronous MDNs *//*
            {
                final AS2ServletMDNReceiverModule aModule = new AS2ServletMDNReceiverModule ();
                aModule.initDynamicComponent (aSession, null);
                aMessageProcessor.addModule (aModule);
            }*/

            {
                final AS2SenderModule aModule = new AS2SenderModule();
                aModule.initDynamicComponent(aSession, null);
                aMessageProcessor.addModule(aModule);
            }
        }

        return aSession;
    }
}