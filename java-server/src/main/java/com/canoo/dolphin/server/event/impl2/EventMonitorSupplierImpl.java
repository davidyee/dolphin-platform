package com.canoo.dolphin.server.event.impl2;

import com.canoo.dolphin.server.context.DolphinContext;
import com.canoo.dolphin.server.context.DolphinContextHandler;

/**
 * Current default implementation for {@link EventMonitorSupplier}.
 */
public class EventMonitorSupplierImpl implements EventMonitorSupplier {

    private DolphinContextHandler dolphinContextHandler;

    public EventMonitorSupplierImpl(DolphinContextHandler dolphinContextHandler) {
        this.dolphinContextHandler = dolphinContextHandler;
    }

    /**
     * Returns the {@link DolphinSessionEventMonitor} for the current Dolphin Platform context. If the method is called
     * outside of a Dolphin Platform context null is returned
     * @return the current Dolphin Platform context or null
     */
    public DolphinSessionEventMonitor getCurrentMonitor() {
        DolphinContext currentContext = dolphinContextHandler.getCurrentContext();
        if(currentContext == null) {
            return null;
        }
        return currentContext.getEventMonitor();
    }
}
