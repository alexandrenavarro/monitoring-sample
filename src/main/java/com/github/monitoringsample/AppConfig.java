package com.github.monitoringsample;

import javax.inject.Inject;

import org.apache.cxf.bus.spring.SpringBus;
import org.apache.cxf.jaxrs.JAXRSServerFactoryBean;
import org.codehaus.jackson.jaxrs.JacksonJsonProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;

import com.codahale.metrics.Counter;
import com.codahale.metrics.Histogram;
import com.codahale.metrics.JmxReporter;
import com.codahale.metrics.MetricRegistry;
import com.codahale.metrics.health.HealthCheck;
import com.codahale.metrics.health.HealthCheckRegistry;

/**
 * <p>AppConfig. </p>
 * 
 * @author anavarro - Aug 15, 2013
 * 
 */
@Configuration
@ComponentScan(basePackageClasses = { AppConfig.class })
public class AppConfig {

    /**
     * LOGGER
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(AppConfig.class);

    @Inject
    private MonitoringResource monitoringResource;

    @Bean
    public HealthCheckRegistry healthCheckRegistry() {
        final HealthCheckRegistry healthCheckRegistry = new HealthCheckRegistry();
        return healthCheckRegistry;
    }


    @Bean
    public MetricRegistry metricRegistry() {
        final MetricRegistry metricRegistry = new MetricRegistry();
        return metricRegistry;
    }
    
    @Bean
    public JmxReporter jmxReporter() {
        final JmxReporter reporter = JmxReporter.forRegistry(metricRegistry()). build();
        reporter.start();
        return reporter;
        }

    @Bean
    public Counter counter() {
        return metricRegistry().counter("counter");
    }

    @Bean
    public Histogram histogram() {
        return metricRegistry().histogram("histogram");
    }
    
    @Bean
    public HealthCheck healthCheck() {
        final HealthCheck healthCheck = new MemoryHealthCheck();
        healthCheckRegistry().register("mem", healthCheck);
        return healthCheck;
    }
    

    /**
     * cxf.
     * 
     * @return
     */
    @Bean(destroyMethod = "shutdown")
    public SpringBus cxf() {
        final SpringBus springBus = new SpringBus();
        return springBus;
    }

    /**
     * restContainer.
     * 
     * @return
     */
    @Bean
    @DependsOn("cxf")
    public JAXRSServerFactoryBean restContainer() {
        final JAXRSServerFactoryBean jaxRSServerFactoryBean = new JAXRSServerFactoryBean();
        jaxRSServerFactoryBean.setServiceBeanObjects(this.monitoringResource);
        jaxRSServerFactoryBean.setAddress("/rest/");
        jaxRSServerFactoryBean.setProvider(new JacksonJsonProvider());
        jaxRSServerFactoryBean.create();
        return jaxRSServerFactoryBean;

    }

}
