package com.canoo.dolphin.test.eventbus;

import com.canoo.dolphin.server.DolphinAction;
import com.canoo.dolphin.server.DolphinController;
import com.canoo.dolphin.server.DolphinModel;
import com.canoo.dolphin.server.event.DolphinEventBus;

import javax.inject.Inject;

@DolphinController(EventBusTestConstants.EVENT_BUS_PUBLISHER_CONTROLLER_NAME)
public class EventBusPublisherController {

    @DolphinModel
    private EventBusTestModel model;

    @Inject
    private DolphinEventBus eventBus;

    @DolphinAction(EventBusTestConstants.CALL_ACTION)
    public void call() {
        eventBus.publish(EventBusTestConstants.TEST_TOPIC, model.valueProperty().get());
    }
}
