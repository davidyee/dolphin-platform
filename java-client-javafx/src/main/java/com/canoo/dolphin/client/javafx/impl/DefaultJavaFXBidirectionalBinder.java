package com.canoo.dolphin.client.javafx.impl;

import com.canoo.dolphin.client.javafx.BidirectionalConverter;
import com.canoo.dolphin.client.javafx.Binding;
import com.canoo.dolphin.client.javafx.JavaFXBidirectionalBinder;
import com.canoo.dolphin.mapping.Property;
import javafx.beans.value.ChangeListener;

/**
 * Created by hendrikebbers on 28.09.15.
 */
public class DefaultJavaFXBidirectionalBinder<S> extends DefaultJavaFXBinder<S> implements JavaFXBidirectionalBinder<S> {

    private final javafx.beans.property.Property<S> javaFxProperty;

    public DefaultJavaFXBidirectionalBinder(final javafx.beans.property.Property<S> javaFxProperty) {
        super(javaFxProperty);
        this.javaFxProperty = javaFxProperty;
    }

    @Override
    public <T> Binding bidirectionalTo(final Property<T> property, BidirectionalConverter<T, S> converter) {
        final Binding unidirectionalBinding = to(property, converter);

        final ChangeListener<S> listener = (obs, oldVal, newVal) -> property.set(converter.convertBack(newVal));
        javaFxProperty.addListener(listener);
        return () -> {
            javaFxProperty.removeListener(listener);
            unidirectionalBinding.unbind();
        };
    }

}
