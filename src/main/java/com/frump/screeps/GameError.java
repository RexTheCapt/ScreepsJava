package com.frump.screeps;

import def.screeps.Creep;
import jsweet.lang.Interface;

import static com.frump.screeps.CustomLogger.log;

@Interface
public class GameError extends Exception {
    public String stack;
    public String name;
    public String message;
    public double lineNumber;
    public double columnNumber;

    public static Exception newUnhandledCode(Creep creep, double responseCode) {
        log(creep, "unhandled code: " + responseCode);
        return new Exception(creep.name + " : unhandled code " + responseCode);
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

    public static Exception newInvalidTarget(Creep creep) {
        return new Exception(creep.name + " invalid target");
    }

    public static Exception newNotOwner(Creep creep) {
        return new Exception(creep.name + " is not one of my creeps");
    }
}
