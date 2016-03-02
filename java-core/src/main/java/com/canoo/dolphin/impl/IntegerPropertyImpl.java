package com.canoo.dolphin.impl;

import com.canoo.dolphin.internal.info.PropertyInfo;
import com.canoo.dolphin.mapping.IntegerProperty;
import com.canoo.dolphin.mapping.NumberProperty;
import org.opendolphin.core.Attribute;

/**
 * Created by hendrikebbers on 02.03.16.
 */
public class IntegerPropertyImpl extends PropertyImpl<Integer> implements IntegerProperty {

    public IntegerPropertyImpl(Attribute attribute, PropertyInfo propertyInfo) {
        super(attribute, propertyInfo);
    }

    @Override
    public void set(Integer value) {
        if (value == null) {
            super.set(0);
        }
        super.set(value);
    }

    @Override
    public Integer get() {
        Integer ret = super.get();
        if(ret == null) {
            return 0;
        }
        return ret;
    }

    @Override
    public int getPrimitive() {
        return get();
    }

    @Override
    public NumberProperty toNumber() {
        return NumberPropertyImpl.convertIntegerToNumber(this);
    }
}
