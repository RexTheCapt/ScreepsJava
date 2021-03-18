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

    public static Exception newUnhandledCode(Creep creep, double responseCode, String location) {
        return new Exception(creep.name + " : unhandled code " + responseCode + " in " + location);
    }

    public static Exception newError(Creep creep, String message) {
        return new Exception(creep.name + " : " + message);
    }

    public static Exception newInvalidArgs(Creep creep) {
        return new Exception(creep.name + " : got invalid args");
    }

    public static Exception newNoBodyPart(Creep creep) {
        String msg = creep.name + " did not have the required body part to finish he's task";
        creep.suicide();
        return new Exception(msg);
    }
}
