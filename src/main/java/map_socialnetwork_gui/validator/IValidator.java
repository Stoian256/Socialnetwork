package com.lab6.map_socialnetwork_gui.validator;

import com.lab6.map_socialnetwork_gui.exceptions.ValidException;


public abstract class IValidator<T> {
    public abstract void validate(T t) throws ValidException;
}
