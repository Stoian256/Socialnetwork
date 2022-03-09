package com.lab6.map_socialnetwork_gui.exceptions;


/**
 * O clasa de exceptie pentru exceptiile din service ce extinde clasa Exception
 */
public class ServiceException extends Exception {
    public ServiceException(String errors) {
        super(errors);
    }
}
