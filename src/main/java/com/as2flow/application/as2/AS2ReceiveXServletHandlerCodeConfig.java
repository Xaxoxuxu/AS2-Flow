/**
 * Copyright (C) 2015-2021 Philip Helger (www.helger.com)
 * philip[at]helger[dot]com
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *         http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.as2flow.application.as2;

import com.as2flow.application.backend.service.PartnershipService;
import com.helger.as2lib.cert.CertificateFactory;
import com.helger.as2lib.crypto.ECryptoAlgorithmCrypt;
import com.helger.as2lib.crypto.ECryptoAlgorithmSign;
import com.helger.as2lib.exception.AS2Exception;
import com.helger.as2lib.partner.Partnership;
import com.helger.as2lib.processor.DefaultMessageProcessor;
import com.helger.as2lib.processor.sender.AsynchMDNSenderModule;
import com.helger.as2lib.processor.storage.MessageFileModule;
import com.helger.as2lib.session.AS2Session;
import com.helger.as2servlet.AbstractAS2ReceiveBaseXServletHandler;
import com.helger.as2servlet.AbstractAS2ReceiveXServletHandler;
import com.helger.as2servlet.util.AS2ServletMDNReceiverModule;
import com.helger.as2servlet.util.AS2ServletReceiverModule;
import com.helger.commons.collection.impl.ICommonsMap;
import com.helger.security.keystore.EKeyStoreType;

import javax.annotation.Nonnull;
import javax.servlet.ServletException;

/**
 * A special {@link AbstractAS2ReceiveBaseXServletHandler} with a code based
 * configuration. This is contained as an example only.
 *
 * @author Philip Helger
 */
public class AS2ReceiveXServletHandlerCodeConfig extends AbstractAS2ReceiveXServletHandler
{
    final PartnershipService partnershipService;

    public AS2ReceiveXServletHandlerCodeConfig(PartnershipService partnershipService)
    {
        this.partnershipService = partnershipService;
    }

    @Override
    @Nonnull
    protected AS2Session createAS2Session (@Nonnull final ICommonsMap <String, String> aInitParams) throws AS2Exception, ServletException
    {
        final AS2Session aSession = new AS2Session ();
        {
            // Create CertificateFactory
            final CertificateFactory aCertFactory = new CertificateFactory ();
            aCertFactory.setKeyStoreType (EKeyStoreType.PKCS12);
            aCertFactory.setFilename ("config/certs.p12");
            aCertFactory.setPassword ("test");
            aCertFactory.setSaveChangesToFile (false);
            aCertFactory.initDynamicComponent (aSession, null);
            aSession.setCertificateFactory (aCertFactory);
        }

        {
            // Create PartnershipFactory
            final H2PartnershipFactory h2PartnershipFactory = new H2PartnershipFactory(partnershipService);

            Partnership p = new Partnership("OpenAS2A-OpenAS2B");
            p.setSenderAS2ID("OpenAS2A");
            p.setReceiverAS2ID("OpenAS2B");
            p.setProtocol("as2");
            p.setSubject("From OpenAS2A to OpenAS2B");
            p.setAS2URL("http://localhost:8080");
            p.setAS2MDNTo("http://localhost:8080");
            p.setAS2MDNOptions("signed-receipt-protocol=optional, pkcs7-signature; signed-receipt-micalg=optional, md5");
            p.setEncryptAlgorithm(ECryptoAlgorithmCrypt.CRYPT_3DES);
            p.setSigningAlgorithm(ECryptoAlgorithmSign.DIGEST_MD5);
            p.setReceiverX509Alias("OpenAS2B_alias");
            p.setSenderX509Alias("OpenAS2A_alias");

            h2PartnershipFactory.addPartnership(p);
            h2PartnershipFactory.initDynamicComponent (aSession, null);
            aSession.setPartnershipFactory (h2PartnershipFactory);
        }

        {
            // Create MessageProcessor
            final DefaultMessageProcessor aMessageProcessor = new DefaultMessageProcessor ();
            aMessageProcessor.setPendingMDNFolder ("data/pendingMDN");
            aMessageProcessor.setPendingMDNInfoFolder ("data/pendinginfoMDN");
            aMessageProcessor.initDynamicComponent (aSession, null);
            aSession.setMessageProcessor (aMessageProcessor);

            /**
             * Required to receive messages port is required internally - simply
             * ignore it for servlets
             */
            {
                final AS2ServletReceiverModule aModule = new AS2ServletReceiverModule ();
                //aModule.setPort(8080);
                aModule.setErrorDirectory ("data/inbox/error");
                aModule.setErrorFormat ("$msg.sender.as2_id$, $msg.receiver.as2_id$, $msg.headers.message-id$");
                aModule.initDynamicComponent (aSession, null);
                aMessageProcessor.addModule (aModule);
            }

            /** Only needed to receive asynchronous MDNs */
            {
                final AS2ServletMDNReceiverModule aModule = new AS2ServletMDNReceiverModule ();
                aModule.initDynamicComponent (aSession, null);
                aMessageProcessor.addModule (aModule);
            }

            /** Below module is used to send async mdn */
            {
                final AsynchMDNSenderModule aModule = new AsynchMDNSenderModule ();
                aModule.initDynamicComponent (aSession, null);
                aMessageProcessor.addModule (aModule);
            }

            /** A module storing the message. */
            {
                final MyHandlerModule aModule = new MyHandlerModule ();
                aModule.initDynamicComponent (aSession, null);
                aMessageProcessor.addModule (aModule);
            }

            /** A module storing the message. */
            {
                final MessageFileModule aModule = new MessageFileModule ();
                aModule.setFilename("data/inbox/$msg.sender.as2_id$-$msg.receiver.as2_id$-$msg.headers.message-id$");
                aModule.setHeaderFilename("data/inbox/msgheaders/$date.uuuu$/$date.MM$/$msg.sender.as2_id$-$msg.receiver.as2_id$-$msg.headers.message-id$");
                aModule.setProtocol("as2");
                aModule.setTempDir("data/temp");
                aModule.setCharsetName("utf-8");
                aModule.initDynamicComponent (aSession, null);
                aMessageProcessor.addModule (aModule);
            }
            // Add further modules if you need them
        }

        return aSession;
    }
}