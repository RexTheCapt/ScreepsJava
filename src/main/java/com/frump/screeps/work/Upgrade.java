package com.frump.screeps.work;

import com.frump.screeps.Main;
import def.screeps.Creep;
import def.screeps.StructureController;

import static com.frump.screeps.CustomLogger.log;
import static def.screeps.Globals.*;

public class Upgrade {
    public static void run(Creep creep) {
        StructureController controller = creep.room.controller;

        double res = creep.upgradeController(controller);

        if (res == OK)
            return;

        if (res == ERR_NOT_ENOUGH_ENERGY)
        {
            if (creep.memory.role.equals(Main.role_builder)) {
                log(creep, "cant upgrade controller, need refill");
                creep.memory.refill = true;
            } else
                creep.say("energy");
        }
        else if (res == ERR_NOT_IN_RANGE)
        {
            log(creep, "moving to room controller");
            creep.moveTo(controller.pos);
        }
        else
            creep.say("C: " + res);
    }
}
