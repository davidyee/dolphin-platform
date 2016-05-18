package com.canoo.dolphin.server.context;

import java.util.concurrent.Callable;
import java.util.concurrent.Future;

/**
 * Created by hendrikebbers on 18.05.16.
 */
public interface DolphinSessionRunner {

    <V> Future<V> runLater(final Callable<V> callable);
}
