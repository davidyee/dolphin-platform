package com.canoo.dolphin.server.event.impl2;

import com.canoo.dolphin.server.DolphinAction;
import com.canoo.dolphin.server.DolphinController;

@DolphinController
public class EventBusController {

    DolphinSessionEventMonitor myEventMonitor;

    @DolphinAction
    public void longPoll() {
        myEventMonitor.waitForEvents();
    }

    @DolphinAction
    public void release() {
        myEventMonitor.release();
    }

}
