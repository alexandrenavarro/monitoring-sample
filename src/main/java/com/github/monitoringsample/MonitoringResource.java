package com.github.monitoringsample;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Named;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.codahale.metrics.Counter;
import com.codahale.metrics.Histogram;


@Named(value="monitoringResource")
@Path("/monitoring/")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class MonitoringResource {
    
    
    private static final Logger LOGGER = LoggerFactory.getLogger(MonitoringResource.class);

    
    
    @Inject
    private Counter counter;
    
    @Inject
    private Histogram histogram;
    
    @GET
    public String get() {
        final long startTime = System.nanoTime();
        this.counter.inc();
        final long stopTime = System.nanoTime();
        histogram.update(stopTime - startTime);
        return "" + this.counter.getCount();
        
        
    }
    
    @GET
    @Path("/mem")
    public long getPctMemory() {
        LOGGER.info("totalMemory=" + Runtime.getRuntime().totalMemory());
        LOGGER.info("freeMemory=" + Runtime.getRuntime().freeMemory());
        return (Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory()) * 100 / Runtime.getRuntime().totalMemory();
    }
    

}
