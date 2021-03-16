package com.frump.screeps;

import def.screeps.Creep;
import def.screeps.Game;
import def.screeps.RoomPosition;
import def.screeps.Structure;
import def.screeps.StructureSpawn;

import static com.frump.screeps.CustomLogger.log;
import static com.frump.screeps.Main.enable_death_stroll;
import static def.screeps.Globals.*;

public class DeathStroll {
    public static boolean run(Creep creep) throws Exception {
        if (!enable_death_stroll)
            return false;

        log(creep, "im dying");
        StructureSpawn spawn = null;

        if (creep.memory.destinationId != null) {
            log(creep, "getting spawn by id");
            spawn = Game.getObjectById(creep.memory.destinationId);

            if (spawn == null)
                log(creep, "failed");
        }

        if (spawn == null) {
            log(creep, "looking for closest spawn");
            spawn = creep.pos.findClosestByPath(FIND_MY_STRUCTURES,
                    Helper.findFilter((Structure s) -> {
                        if (s.structureType.equals(STRUCTURE_SPAWN)) {
                            StructureSpawn ss = (StructureSpawn) s;
                            return ss.spawning == null;
                        } else return false;
                    }));
        }

        if (spawn == null) {
            log(creep, "cant find spawn");
            return false;
        }

        creep.memory.destinationId = creep.id;

        double res = spawn.renewCreep(creep);

        if (res == OK) {
            log(creep, "lifespan elongated");
            if (creep.ticksToLive > 1000) {
                log(creep, "back to work");
                creep.memory.renew = false;
                return true;
            }

            return false;
        } else if (res == ERR_NOT_OWNER) {
            log(creep, "not my spawn!");
            creep.moveTo(new RoomPosition(25,25, creep.memory.roomSpawnedIn));
            return false;
        } else if (res == ERR_BUSY) {
            log(creep, "spawner is spawning, trying to find a new one");
            creep.memory.destinationId = null;
            return false;
        } else if (res == ERR_NOT_ENOUGH_ENERGY) {
            log(creep, "spawner need more energy");
            return false;
        } else if (res == ERR_INVALID_TARGET) {
            // TODO: make sure inventory is empty first.
            log(creep, "spawner cant extend my life, going for suicide");
            return false;
        } else if (res == ERR_FULL) {
            log(creep, "already at full lifespan");
            creep.memory.renew = false;
            return true;
        } else if (res == ERR_NOT_IN_RANGE) {
            log(creep, "not in range, moving closer");
            creep.moveTo(spawn.pos);
            return false;
        } else if (res == ERR_RCL_NOT_ENOUGH) {
            throw GameError.newError(creep, "RCL is not high enough");
        } else
            throw GameError.newUnhandledCode(creep, res, "DeathStroll.run");
    }
}
