package com.canoo.dolphin.mapping;

/**
 * Created by hendrikebbers on 02.03.16.
 */
public interface BooleanProperty extends Property<Boolean> {

    BooleanProperty negate();

    boolean getPrimitive();
}
