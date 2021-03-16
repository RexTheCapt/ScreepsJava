package com.frump.screeps;

import def.screeps.Creep;
import jsweet.lang.Interface;

@Interface
public class GameError extends Exception {
    public String stack;
    public String name;
    public String message;
    public double lineNumber;
    public double columnNumber;

    public static Exception newUnhandledCode(Creep creep, Code responseCode, String location) {
        return new Exception(creep.name + " : unhandled code " + responseCode + " in " + location);
    }

    public static Exception newError(Creep creep, String message) {
        return new Exception(creep.name + " : " + message);
    }

    public static Exception newInvalidArgs(Creep creep) {
        return new Exception(creep.name + " : got invalid args");
    }
}
