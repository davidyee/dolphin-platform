package com.canoo.dolphin.impl;

import com.canoo.dolphin.event.Subscription;
import com.canoo.dolphin.event.ValueChangeEvent;
import com.canoo.dolphin.event.ValueChangeListener;
import com.canoo.dolphin.internal.info.PropertyInfo;
import com.canoo.dolphin.mapping.BooleanProperty;
import com.canoo.dolphin.mapping.Property;
import org.opendolphin.core.Attribute;

public class BooleanPropertyImpl extends PropertyImpl<Boolean> implements BooleanProperty {

    public BooleanPropertyImpl(Attribute attribute, PropertyInfo propertyInfo) {
        super(attribute, propertyInfo);
        set(false);
    }

    @Override
    public void set(Boolean value) {
        if(value == null) {
            super.set(false);
        }
        super.set(value);
    }

    @Override
    public Boolean get() {
        Boolean ret = super.get();
        if(ret == null) {
            return false;
        }
        return ret;
    }

    @Override
    public BooleanProperty negate() {
        return new BooleanProperty(){

            @Override
            public void set(Boolean value) {
                if(value == null) {
                    BooleanPropertyImpl.this.set(true);
                }
                BooleanPropertyImpl.this.set(!value);

            }

            @Override
            public Boolean get() {
                return !BooleanPropertyImpl.this.get();
            }

            @Override
            public Subscription onChanged(final ValueChangeListener<? super Boolean> listener) {
                final BooleanProperty thisProperty = this;
                return BooleanPropertyImpl.this.onChanged(new ValueChangeListener<Boolean>() {
                    @Override
                    public void valueChanged(final ValueChangeEvent<? extends Boolean> evt) {
                        ValueChangeEvent<? extends Boolean> event = new ValueChangeEvent<Boolean>() {
                            @Override
                            public Property<Boolean> getSource() {
                                return thisProperty;
                            }

                            @Override
                            public Boolean getOldValue() {
                                return !evt.getOldValue();
                            }

                            @Override
                            public Boolean getNewValue() {
                                return !evt.getNewValue();
                            }
                        };
                        listener.valueChanged(event);
                    }
                });
            }

            @Override
            public BooleanProperty negate() {
                return BooleanPropertyImpl.this;
            }

            @Override
            public boolean getPrimitive() {
                return get();
            }
        };
    }

    @Override
    public boolean getPrimitive() {
        return get();
    }
}
