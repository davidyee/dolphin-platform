package com.canoo.dolphin.mapping;

/**
 * Created by hendrikebbers on 02.03.16.
 */
public interface IntegerProperty extends Property<Integer> {

    int getPrimitive();

    NumberProperty toNumber();
}
