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
import com.helger.as2servlet.AS2ReceiveServlet;
import com.helger.commons.http.EHttpMethod;
import com.helger.xservlet.AbstractXServlet;

import javax.annotation.OverridingMethodsMustInvokeSuper;
import javax.servlet.ServletException;

/**
 * This is the main servlet that takes AS2 messages and processes them. This
 * servlet is configured to accept only POST requests. The logic for receiving
 * is contained in {@link AS2ReceiveXServletHandlerCodeConfig}.<br>
 * This is an alternative implementation to {@link AS2ReceiveServlet} which uses
 * a file to set the configuration.
 *
 * @author Philip Helger
 */
public class AS2ReceiveServletCodeConfig extends AbstractXServlet
{
    final PartnershipService partnershipService;
    final AS2MessageService as2MessageService;

    public AS2ReceiveServletCodeConfig(PartnershipService partnershipService, AS2MessageService as2MessageService)
    {
        this.partnershipService = partnershipService;
        this.as2MessageService = as2MessageService;
    }

    @Override
    @OverridingMethodsMustInvokeSuper
    public void init() throws ServletException
    {
        // Multipart is handled specifically inside
        settings().setMultipartEnabled(false);
        handlerRegistry().registerHandler(EHttpMethod.POST, new AS2ReceiveXServletHandlerCodeConfig(partnershipService, as2MessageService), false);
    }
}