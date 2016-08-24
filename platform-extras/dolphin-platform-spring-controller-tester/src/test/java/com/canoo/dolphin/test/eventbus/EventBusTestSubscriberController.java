package com.canoo.dolphin.test.eventbus;

import com.canoo.dolphin.server.DolphinController;
import com.canoo.dolphin.server.DolphinModel;
import com.canoo.dolphin.server.event.DolphinEventBus;

import javax.annotation.PostConstruct;
import javax.inject.Inject;

@DolphinController(EventBusTestConstants.EVENT_BUS_SUBSCIBER_CONTROLLER_NAME)
public class EventBusTestSubscriberController {

    @DolphinModel
    private  EventBusTestModel model;

    @Inject
    private DolphinEventBus eventBus;

    @PostConstruct
    public void init() {
        eventBus.subscribe(EventBusTestConstants.TEST_TOPIC, message -> model.valueProperty().set(message.getData()));
    }
}
