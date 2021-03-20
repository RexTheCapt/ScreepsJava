package com.frump.screeps.work;

import com.frump.screeps.GameError;
import def.screeps.Creep;
import def.screeps.StructureController;

import static com.frump.screeps.CustomLogger.log;
import static def.screeps.Globals.*;

public class Upgrade {
    /**
     * Returns true when upgrading the controller was successful.
     * @param creep creep
     * @return true on upgrade success.
     * @throws Exception thrown when something goes wrong.
     */
    public static boolean run(Creep creep) throws Exception {
        StructureController controller = creep.room.controller;

        if (controller.ticksToDowngrade < 20000) {
            if (creep.store.energy == 0) {
                creep.memory.refill = true;
            }

            if (creep.memory.refill && Runner.handleRefill(creep)) {
                creep.memory.refill = false;
            }
        }

        double res = creep.upgradeController(controller);

        if (res == OK) {
            log(creep, "controller is upgraded");
            return true;
        } else if (res == ERR_NOT_OWNER) {
            log(creep, "not owner");
            // TODO: to something if not mine?
            return false;
        } else if (res == ERR_BUSY) {
            log(creep, "still spawning");
            return false;
        } else if (res == ERR_NOT_ENOUGH_RESOURCES) {
            log(creep, "need refill");
            creep.memory.refill = true;
            Runner.handleRefill(creep);
            return false;
        } else if (res == ERR_INVALID_TARGET) {
            log(creep, "invalid target");
            throw GameError.newInvalidTarget(creep);
        } else if (res == ERR_NOT_IN_RANGE) {
            log(creep, "moving to controller");
            creep.moveTo(controller.pos);
            return false;
        } else if (res == ERR_NO_BODYPART) {
            log(creep, "no body part");
            throw GameError.newNoBodyPart(creep);
        } else {
            throw GameError.newUnhandledCode(creep, res);
        }
    }
}
