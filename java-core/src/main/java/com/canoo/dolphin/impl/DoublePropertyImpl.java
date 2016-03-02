package com.canoo.dolphin.impl;

import com.canoo.dolphin.internal.info.PropertyInfo;
import com.canoo.dolphin.mapping.DoubleProperty;
import com.canoo.dolphin.mapping.NumberProperty;
import org.opendolphin.core.Attribute;

/**
 * Created by hendrikebbers on 02.03.16.
 */
public class DoublePropertyImpl extends PropertyImpl<Double> implements DoubleProperty {

    public DoublePropertyImpl(Attribute attribute, PropertyInfo propertyInfo) {
        super(attribute, propertyInfo);
        set(0.0);
    }

    @Override
    public Double get() {
        Double ret = super.get();
        if(ret == 0) {
            return 0.0;
        }
        return ret;
    }

    @Override
    public void set(Double value) {
        if(value == null) {
            super.set(0.0);
        }
        super.set(value);
    }

    @Override
    public double getPrimitive() {
        return get();
    }

    @Override
    public NumberProperty toNumber() {
        return NumberPropertyImpl.convertDoubleToNumber(this);
    }
}
