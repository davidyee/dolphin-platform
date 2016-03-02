package com.canoo.dolphin.impl;

import com.canoo.dolphin.event.Subscription;
import com.canoo.dolphin.event.ValueChangeEvent;
import com.canoo.dolphin.event.ValueChangeListener;
import com.canoo.dolphin.internal.info.PropertyInfo;
import com.canoo.dolphin.mapping.DoubleProperty;
import com.canoo.dolphin.mapping.IntegerProperty;
import com.canoo.dolphin.mapping.NumberProperty;
import com.canoo.dolphin.mapping.Property;
import org.opendolphin.core.Attribute;

/**
 * Created by hendrikebbers on 02.03.16.
 */
public class NumberPropertyImpl extends PropertyImpl<Number> implements NumberProperty {

    public NumberPropertyImpl(Attribute attribute, PropertyInfo propertyInfo) {
        super(attribute, propertyInfo);
        set(0);
    }

    @Override
    public void set(Number value) {
        if(value == null) {
            super.set(0);
        }
        super.set(value);
    }

    @Override
    public Number get() {
        Number ret = super.get();
        if(ret == null) {
            return 0;
        }
        return ret;
    }

    @Override
    public DoubleProperty toDouble() {
        return convertNumberToDouble(this);
    }

    @Override
    public IntegerProperty toInteger() {
        return convertNumberToInteger(this);
    }

    @Override
    public int intValue() {
        return get().intValue();
    }

    @Override
    public long longValue() {
        return get().longValue();
    }

    @Override
    public float floatValue() {
        return get().floatValue();
    }

    @Override
    public double doubleValue() {
        return get().doubleValue();
    }

    @Override
    public byte byteValue() {
        return get().byteValue();
    }

    @Override
    public short shortValue() {
        return get().shortValue();
    }

    public static IntegerProperty convertNumberToInteger(final NumberProperty numberProperty) {
        return new IntegerProperty() {

            @Override
            public int getPrimitive() {
                return get();
            }

            @Override
            public NumberProperty toNumber() {
                return numberProperty;
            }

            @Override
            public void set(Integer value) {
                numberProperty.set(value);
            }

            @Override
            public Integer get() {
                return numberProperty.get().intValue();
            }

            @Override
            public Subscription onChanged(final ValueChangeListener<? super Integer> listener) {
                final Property<Integer> thisProperty = this;
                return numberProperty.onChanged(new ValueChangeListener<Number>() {
                    @Override
                    public void valueChanged(final ValueChangeEvent<? extends Number> evt) {
                        listener.valueChanged(new ValueChangeEvent<Integer>() {
                            @Override
                            public Property<Integer> getSource() {
                                return thisProperty;
                            }

                            @Override
                            public Integer getOldValue() {
                                return evt.getOldValue().intValue();
                            }

                            @Override
                            public Integer getNewValue() {
                                return evt.getNewValue().intValue();
                            }
                        });
                    }
                });
            }
        };
    }

    public static NumberProperty convertIntegerToNumber(final IntegerProperty integerProperty) {
        return new NumberProperty() {

            @Override
            public DoubleProperty toDouble() {
                return convertNumberToDouble(this);
            }

            @Override
            public IntegerProperty toInteger() {
                return integerProperty;
            }

            @Override
            public int intValue() {
                return get().intValue();
            }

            @Override
            public long longValue() {
                return get().longValue();
            }

            @Override
            public float floatValue() {
                return get().floatValue();
            }

            @Override
            public double doubleValue() {
                return get().doubleValue();
            }

            @Override
            public byte byteValue() {
                return get().byteValue();
            }

            @Override
            public short shortValue() {
                return get().shortValue();
            }

            @Override
            public void set(Number value) {
                integerProperty.set(value.intValue());
            }

            @Override
            public Number get() {
                return integerProperty.get();
            }

            @Override
            public Subscription onChanged(final ValueChangeListener<? super Number> listener) {
                final NumberProperty thisProperty = this;
                return integerProperty.onChanged(new ValueChangeListener<Integer>() {
                    @Override
                    public void valueChanged(final ValueChangeEvent<? extends Integer> evt) {
                        listener.valueChanged(new ValueChangeEvent<Number>() {
                            @Override
                            public Property<Number> getSource() {
                                return thisProperty;
                            }

                            @Override
                            public Number getOldValue() {
                                return evt.getOldValue();
                            }

                            @Override
                            public Number getNewValue() {
                                return evt.getNewValue();
                            }
                        });
                    }
                });
            }
        };
    }

    public static DoubleProperty convertNumberToDouble(final NumberProperty numberProperty) {
        return new DoubleProperty() {

            @Override
            public NumberProperty toNumber() {
                return numberProperty;
            }

            @Override
            public double getPrimitive() {
                return get();
            }

            @Override
            public void set(Double value) {
                numberProperty.set(value);
            }

            @Override
            public Double get() {
                return numberProperty.get().doubleValue();
            }

            @Override
            public Subscription onChanged(final ValueChangeListener<? super Double> listener) {
                final Property<Double> thisProperty = this;
                return numberProperty.onChanged(new ValueChangeListener<Number>() {
                    @Override
                    public void valueChanged(final ValueChangeEvent<? extends Number> evt) {
                        listener.valueChanged(new ValueChangeEvent<Double>() {
                            @Override
                            public Property<Double> getSource() {
                                return thisProperty;
                            }

                            @Override
                            public Double getOldValue() {
                                return evt.getOldValue().doubleValue();
                            }

                            @Override
                            public Double getNewValue() {
                                return evt.getNewValue().doubleValue();
                            }
                        });
                    }
                });
            }
        };
    }

    public static NumberProperty convertDoubleToNumber(final DoubleProperty doubleProperty) {
        return new NumberProperty() {

            @Override
            public DoubleProperty toDouble() {
                return doubleProperty;
            }

            @Override
            public IntegerProperty toInteger() {
                return convertNumberToInteger(this);
            }

            @Override
            public int intValue() {
                return get().intValue();
            }

            @Override
            public long longValue() {
                return get().longValue();
            }

            @Override
            public float floatValue() {
                return get().floatValue();
            }

            @Override
            public double doubleValue() {
                return get().doubleValue();
            }

            @Override
            public byte byteValue() {
                return get().byteValue();
            }

            @Override
            public short shortValue() {
                return get().shortValue();
            }

            @Override
            public void set(Number value) {
                doubleProperty.set(value.doubleValue());
            }

            @Override
            public Number get() {
                return doubleProperty.get();
            }

            @Override
            public Subscription onChanged(final ValueChangeListener<? super Number> listener) {
                final NumberProperty thisProperty = this;
                return doubleProperty.onChanged(new ValueChangeListener<Double>() {
                    @Override
                    public void valueChanged(final ValueChangeEvent<? extends Double> evt) {
                        listener.valueChanged(new ValueChangeEvent<Number>() {
                            @Override
                            public Property<Number> getSource() {
                                return thisProperty;
                            }

                            @Override
                            public Number getOldValue() {
                                return evt.getOldValue();
                            }

                            @Override
                            public Number getNewValue() {
                                return evt.getNewValue();
                            }
                        });
                    }
                });
            }
        };
    }

}
