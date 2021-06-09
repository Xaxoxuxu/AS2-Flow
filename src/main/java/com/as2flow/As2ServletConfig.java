/**
 * Copyright (C) 2018-2021 Philip Helger (www.helger.com)
 * philip[at]helger[dot]com
 * Idea by: Sergey Yaskov
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
package com.as2flow;

import com.as2flow.backend.as2.AS2ReceiveServletCodeConfig;
import com.as2flow.backend.service.AS2MessageService;
import com.as2flow.backend.service.PartnershipService;
import com.helger.as2servlet.AS2WebAppListener;
import com.helger.as2servlet.AbstractAS2ReceiveXServletHandler;
import com.helger.as2servlet.mdn.AS2MDNReceiveServlet;
import com.helger.web.scope.mgr.WebScopeManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.servlet.ServletContext;
import java.util.HashMap;
import java.util.Map;

@Configuration
public class As2ServletConfig
{
    /** The ServletContext to be used */
    @Autowired
    private ServletContext m_aSC;

    private void _initScope ()
    {
        // Required to be called before the servlet is initialized
        if (!WebScopeManager.isGlobalScopePresent ())
        {
            AS2WebAppListener.staticInit (m_aSC);
        }
    }

    @Bean
    public ServletRegistrationBean <AS2ReceiveServletCodeConfig> servletRegistrationBeanAS2 (final PartnershipService partnershipService, final AS2MessageService as2MessageService)
    {
        _initScope ();

        final ServletRegistrationBean <AS2ReceiveServletCodeConfig> bean = new ServletRegistrationBean <> (new AS2ReceiveServletCodeConfig(partnershipService, as2MessageService), "/as2");
        final Map <String, String> aInitParams = new HashMap <> ();
        //aInitParams.put (AbstractAS2ReceiveXServletHandler.SERVLET_INIT_PARAM_AS2_SERVLET_CONFIG_FILENAME, "config/config.xml");
        bean.setInitParameters (aInitParams);
        bean.setLoadOnStartup(1);
        return bean;
    }

    //@Bean
    public ServletRegistrationBean <AS2MDNReceiveServlet> servletRegistrationBeanMDN ()
    {
        _initScope ();

        final ServletRegistrationBean <AS2MDNReceiveServlet> bean = new ServletRegistrationBean <> (new AS2MDNReceiveServlet (), "/as2mdn");
        final Map <String, String> aInitParams = new HashMap <> ();
        aInitParams.put (AbstractAS2ReceiveXServletHandler.SERVLET_INIT_PARAM_AS2_SERVLET_CONFIG_FILENAME, "config/config.xml");
        bean.setInitParameters (aInitParams);
        bean.setLoadOnStartup(1);
        return bean;
    }
}
