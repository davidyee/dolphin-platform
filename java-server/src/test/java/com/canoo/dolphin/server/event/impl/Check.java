package com.canoo.dolphin.server.event.impl;

/**
 * Created by hendrikebbers on 04.03.16.
 */
public class Check {

    private int checked = 0;

    public synchronized void check() {
        checked++;
    }

    public synchronized int getCheckCount() {
        return checked;
    }
}
