package com.canoo.dolphin.mapping;

/**
 * Created by hendrikebbers on 02.03.16.
 */
public interface NumberProperty extends Property<Number> {

    DoubleProperty toDouble();

    IntegerProperty toInteger();

    int intValue();

    long longValue();

    float floatValue();

    double doubleValue();

    byte byteValue();

    short shortValue();

}
