package com.canoo.dolphin.server.context;

import java.lang.ref.WeakReference;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by hendrikebbers on 03.03.16.
 */
public class WeakDolphinContextCollection {

    private Map<String, WeakReference<DolphinContext>> contextMap;

    public WeakDolphinContextCollection() {
        this.contextMap = new HashMap<>();
    }

    public synchronized void add(DolphinContext dolphinContext) {
        contextMap.put(dolphinContext.getId(), new WeakReference<DolphinContext>(dolphinContext));
    }

    public synchronized void remove(DolphinContext dolphinContext) {
        contextMap.remove(dolphinContext.getId());
    }

    public synchronized DolphinContext get(String id) {
        if (contextMap.containsKey(id)) {
            WeakReference<DolphinContext> ref = contextMap.get(id);
            if(ref.get() == null) {
                //Maybe throw exception?
                contextMap.remove(id);
                return null;
            }
            return ref.get();
        }
        //Maybe throw exception?
        return null;
    }

}
