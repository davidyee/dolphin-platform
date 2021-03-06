package com.canoo.dolphin.test.impl;

import com.canoo.dolphin.impl.PlatformConstants;
import com.canoo.dolphin.server.context.DolphinContext;
import org.opendolphin.core.client.ClientDolphin;
import org.opendolphin.core.client.comm.AbstractClientConnector;
import org.opendolphin.core.client.comm.CommandAndHandler;
import org.opendolphin.core.client.comm.OnFinishedHandler;
import org.opendolphin.core.comm.Command;
import org.opendolphin.core.comm.NamedCommand;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by hendrikebbers on 01.11.16.
 */
public class DolphinTestClientConnector extends AbstractClientConnector{

    private final DolphinContext dolphinContext;

    public DolphinTestClientConnector(ClientDolphin clientDolphin, DolphinContext dolphinContext) {
        super(clientDolphin);
        this.dolphinContext = dolphinContext;
    }

    protected void startCommandProcessing() {
        /* do nothing! */
    }

    public void send(Command command, OnFinishedHandler callback) {
        List<Command> answer = transmit(new ArrayList<>(Arrays.asList(command)));
        CommandAndHandler handler = new CommandAndHandler();
        handler.setCommand(command);
        handler.setHandler(callback);
        processResults(answer, new ArrayList<>(Arrays.asList(handler)));
    }

    public void send(Command command) {
        send(command, null);
    }

    @Override
    public void listen() {

    }

    @Override
    public List<Command> transmit(List<Command> commands) {
        ArrayList<Command> realCommands = new ArrayList<>(commands);
        realCommands.add(new NamedCommand(PlatformConstants.POLL_EVENT_BUS_COMMAND_NAME));
        return dolphinContext.handle(commands);
    }

}
