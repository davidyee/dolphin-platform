package com.canoo.dolphin.mapping;

/**
 * Created by hendrikebbers on 02.03.16.
 */
public interface DoubleProperty extends Property<Double> {

    double getPrimitive();

    NumberProperty toNumber();

}
