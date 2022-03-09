package com.lab6.map_socialnetwork_gui.exceptions;


/**
 * O clasa de exceptie pentru exceptiile de validare ce extinde clasa Exception
 */
public class ValidException extends Exception{
    public ValidException(String errors) {
        super(errors);
    }
}

