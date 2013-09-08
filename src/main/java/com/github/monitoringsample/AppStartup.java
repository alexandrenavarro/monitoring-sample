package com.github.monitoringsample;


import java.io.File;

import javax.servlet.ServletException;

import org.apache.catalina.Context;
import org.apache.catalina.LifecycleException;
import org.apache.catalina.mbeans.JmxRemoteLifecycleListener;
import org.apache.catalina.startup.Tomcat;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * <p>AppStartup.</p>
 * 
 * @author anavarro - Jun 1, 2013
 * 
 */
public final class AppStartup {

    private static final Logger LOGGER = LoggerFactory.getLogger(AppStartup.class);

    /**
     * APPLICATION_NAME
     */
    static final String APP_NAME = "monitoring-sample";

    /**
     * APP_HOME
     */
    static final String APP_HOME = "app.home";

    /**
     * PROPERTIES_EXT
     */
    static final String PROPERTIES_EXT = ".properties";
    
    /**
     * HTTP_PORT
     */
    static final String HTTP_PORT = "httpPort";
    
    /**
     * SHUTDOWN_CONFIG
     */
    static final String SHUTDOWN_CONFIG = "shutdown-listener.configuration";
    
    /**
     * SLASH
     */ 
    static final String SLASH = "/";

    
    /**
     * Constructor.
     *
     */
    private AppStartup() {
        super();
    }


    /**
     * main.
     * 
     * @param args
     */
    public static void main(final String... args) {
        LOGGER.info("Application is starting ...");
        final long startTime = System.currentTimeMillis();

        // Hack to start Tomcat >= 7.0.20 quicker
        //System.setProperty("java.security.egd", "file:/dev/./urandom");

        try {
            // Start Tomcat
            final String webappLocation = new File("./src/main/webapp").getAbsolutePath(); //"src" + File.separator + "main" + File.separator + "webapp").getAbsolutePath();

            final Tomcat tomcat = new Tomcat();
            tomcat.setBaseDir("target");
            tomcat.setPort(8080);
            Context context = tomcat.addWebapp(SLASH + APP_NAME, webappLocation);
 
            
            tomcat.start();
            final long stopTime = System.currentTimeMillis();
            LOGGER.info("Application is started in " + (stopTime - startTime) + " ms.");
            tomcat.getServer().await();
            

        } catch (LifecycleException exception) {
            LOGGER.error("Application can not be started because of a error when Tomcat tried to start (maybe port already used problem).", exception);
        } catch (ServletException exception) {
            LOGGER.error("Application can not be started because of a error when Tomcat tried to start (maybe port already used problem).", exception);
        } catch (Exception exception) {
            LOGGER.error("Application can not be started because of an error when we tried to set Shutdown hook (maybe port already used problem).", exception);
        }
    }
}
