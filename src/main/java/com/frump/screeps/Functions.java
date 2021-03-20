package com.frump.screeps;

import def.screeps.Creep;
import def.screeps.Structure;

import static com.frump.screeps.CustomLogger.log;
import static def.screeps.Globals.ERR_BUSY;
import static def.screeps.Globals.ERR_FULL;
import static def.screeps.Globals.ERR_INVALID_ARGS;
import static def.screeps.Globals.ERR_INVALID_TARGET;
import static def.screeps.Globals.ERR_NOT_ENOUGH_RESOURCES;
import static def.screeps.Globals.ERR_NOT_IN_RANGE;
import static def.screeps.Globals.ERR_NOT_OWNER;
import static def.screeps.Globals.OK;

public class Functions {
    /**
     * Transfer inventory from creep to target structure.
     * Will move to target automatically if returns ERR_NOT_IN_RANGE.
     * @param creep target creep.
     * @param storage target storage.
     * @param resource target resource.
     * @return status code.
     * @throws Exception when something goes wrong.
     */
    public static double transfer(Creep creep, Structure storage, String resource) throws Exception {
         double res = creep.transfer(storage, resource);

         if (res == OK) {
             log(creep, "transferred resource");
             return OK;
         } else if (res == ERR_NOT_OWNER) {
             throw GameError.newNotOwner(creep);
         } else if (res == ERR_BUSY) {
             log(creep, "still spawning");
             return ERR_BUSY;
         } else if (res == ERR_NOT_ENOUGH_RESOURCES) {
             log(creep, "not enough resources");
             return ERR_NOT_ENOUGH_RESOURCES;
         } else if (res == ERR_INVALID_TARGET) {
             log(creep, "invalid target");
             return ERR_INVALID_TARGET;
         } else if (res == ERR_FULL) {
             log(creep, "inventory full");
             return ERR_FULL;
         } else if (res == ERR_NOT_IN_RANGE) {
             log(creep, "moving to storage");
             creep.moveTo(storage.pos);
             return ERR_NOT_IN_RANGE;
         } else if (res == ERR_INVALID_ARGS) {
             throw GameError.newInvalidArgs(creep);
         } else {
             throw GameError.newUnhandledCode(creep, res);
         }
    }
}
