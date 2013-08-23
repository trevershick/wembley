package com.railinc.r2dq.support;

import static org.apache.commons.lang.StringUtils.isNotBlank;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.MissingResourceException;
import java.util.ResourceBundle;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Application {
        private static final String BUNDLE_NAME = "r2dq"; //$NON-NLS-1$

        private static final ResourceBundle RESOURCE_BUNDLE = ResourceBundle.getBundle(BUNDLE_NAME);
        private static final Logger log = LoggerFactory.getLogger(Application.class);
        
        public static String getVersion(){
                String x = getString("application.version");
                if (isNotBlank(System.getProperty("wtp.deploy"))) {
                        try { 
                        	x = x.replaceAll("-RELEASE", "");
                        	x = x.concat("-").concat(InetAddress.getLocalHost().getHostName());
                        } catch (UnknownHostException e) {
                               log.debug("Can't get host name", e);
                        }
                }
                return x;
        }
        
        public static String getString(String key) {
                try {
                        return RESOURCE_BUNDLE.getString(key);
                } catch (MissingResourceException e) {
                        return '!' + key + '!';
                }
        }

}
