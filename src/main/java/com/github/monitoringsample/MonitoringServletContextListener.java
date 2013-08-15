package com.github.monitoringsample;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;

import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import com.codahale.metrics.MetricRegistry;
import com.codahale.metrics.health.HealthCheckRegistry;
import com.codahale.metrics.servlets.AdminServletContextListener;

public class MonitoringServletContextListener extends AdminServletContextListener {
    private MetricRegistry metricRegistry;
    private HealthCheckRegistry healthCheckRegistry;

    /**
     * (non-Javadoc)
     *
     * @see com.codahale.metrics.servlets.AdminServletContextListener#getMetricRegistry()
     */
    @Override
    protected MetricRegistry getMetricRegistry() {
        return metricRegistry;
    }

    /**
     * (non-Javadoc)
     *
     * @see com.codahale.metrics.servlets.AdminServletContextListener#getHealthCheckRegistry()
     */
    @Override
    protected HealthCheckRegistry getHealthCheckRegistry() {
        return healthCheckRegistry;
    }
    
    /**
     * (non-Javadoc)
     *
     * @see com.codahale.metrics.servlets.AdminServletContextListener#contextInitialized(javax.servlet.ServletContextEvent)
     */
    @Override
    public void contextInitialized(ServletContextEvent aServletContextEvent) {
        super.contextInitialized(aServletContextEvent);
        final ServletContext ctx = aServletContextEvent.getServletContext();
        final WebApplicationContext springContext = WebApplicationContextUtils.getWebApplicationContext(ctx);
        this.healthCheckRegistry = springContext.getBean(HealthCheckRegistry.class);
        this.metricRegistry = springContext.getBean(MetricRegistry.class);
        aServletContextEvent.getServletContext().setAttribute("com.codahale.metrics.servlets.HealthCheckServlet.registry", healthCheckRegistry);
        aServletContextEvent.getServletContext().setAttribute("com.codahale.metrics.servlets.MetricsServlet.registry", metricRegistry);
    }
}
