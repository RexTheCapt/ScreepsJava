package com.frump.screeps.work;

import com.frump.screeps.GameError;
import def.screeps.Creep;
import def.screeps.Flag;
import def.screeps.Game;
import def.screeps.Room;

import static com.frump.screeps.CustomLogger.log;
import static def.screeps.Globals.ERR_BUSY;
import static def.screeps.Globals.ERR_FULL;
import static def.screeps.Globals.ERR_GCL_NOT_ENOUGH;
import static def.screeps.Globals.ERR_INVALID_TARGET;
import static def.screeps.Globals.ERR_NOT_IN_RANGE;
import static def.screeps.Globals.ERR_NOT_OWNER;
import static def.screeps.Globals.ERR_NO_BODYPART;
import static def.screeps.Globals.OK;

public class Claim {
    /**
     * Claim an room by placing an flag and add the flag name to ClaimRoom.
     *
     * @param creep claim creep.
     * @return true on claim.
     * @throws Exception when something goes wrong.
     */
    public static boolean run(Creep creep) throws Exception {
        log(creep, "getting flag");
        Flag flag = Game.flags.$get("claim");

        if (flag == null) {
            log(creep, "flag not found");
            String msg = "Claim flag \"claim\" does not exist";
            Game.notify(msg);
            log(creep, msg);
            return false;
        }

        log(creep, "claim flag found");
        creep.say("Claiming");

        Room claimRoom = flag.room;

        if (claimRoom == null || !creep.room.name.equals(claimRoom.name)) {
            log(creep, "moving to room");
            creep.moveTo(flag.pos);
            return false;
        } else {
            log(creep, "claiming");
            return claimController(creep, flag);
        }
    }

    private static boolean claimController(Creep creep, Flag flag) throws Exception {
        double res = creep.claimController(flag.room.controller);

        if (res == OK) {
            log(creep, "claimed");
            return true;
        } else if (res == ERR_NOT_OWNER) {
            throw GameError.newError(creep, "Not owning creep");
        } else if (res == ERR_BUSY) {
            log(creep, "spawning");
            return false;
        } else if (res == ERR_INVALID_TARGET) {
            throw GameError.newError(creep, "target is not neutral");
        } else if (res == ERR_FULL) {
            throw GameError.newError(creep, "cant claim more");
        } else if (res == ERR_NOT_IN_RANGE) {
            log(creep, "moving to target");
            creep.moveTo(flag.room.controller.pos);
            return false;
        } else if (res == ERR_NO_BODYPART) {
            throw GameError.newNoBodyPart(creep);
        } else if (res == ERR_GCL_NOT_ENOUGH) {
            throw GameError.newError(creep, "GCL is not high enough for claiming");
        } else {
            throw GameError.newUnhandledCode(creep, res);
        }
    }
}