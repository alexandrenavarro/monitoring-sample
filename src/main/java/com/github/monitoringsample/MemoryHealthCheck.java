package com.github.monitoringsample;

import com.codahale.metrics.health.HealthCheck;

/**
 * <p>MemoryHealthCheck. </p>
 *
 * @author anavarro - Aug 15, 2013
 *
 */
public class MemoryHealthCheck extends HealthCheck {

    /**
     * (non-Javadoc)
     *
     * @see com.codahale.metrics.health.HealthCheck#check()
     */
    @Override
    protected Result check() throws Exception {
        final long pctMem = (Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory()) * 100 / Runtime.getRuntime().totalMemory();
        return pctMem < 50 ? Result.healthy() : Result.unhealthy("Low Memory");
    }

}
