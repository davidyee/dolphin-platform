package com.canoo.dolphin.server.event.impl2;

/**
 * Implementations must return the {@link DolphinSessionEventMonitor} of the current Dolphin Platform context.
 */
public interface EventMonitorSupplier {

    /**
     * Returns the {@link DolphinSessionEventMonitor} for the current Dolphin Platform context. If the method is called
     * outside of a Dolphin Platform context null is returned
     * @return the current Dolphin Platform context or null
     */
    DolphinSessionEventMonitor getCurrentMonitor();

}
