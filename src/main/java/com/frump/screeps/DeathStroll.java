package com.frump.screeps;

import def.screeps.Creep;
import def.screeps.Game;
import def.screeps.RoomPosition;
import def.screeps.Structure;
import def.screeps.StructureStorage;

import static com.frump.screeps.CustomLogger.log;
import static com.frump.screeps.Main.enable_death_stroll;
import static def.screeps.Globals.ERR_BUSY;
import static def.screeps.Globals.ERR_FULL;
import static def.screeps.Globals.ERR_INVALID_ARGS;
import static def.screeps.Globals.ERR_INVALID_TARGET;
import static def.screeps.Globals.ERR_NOT_ENOUGH_RESOURCES;
import static def.screeps.Globals.ERR_NOT_IN_RANGE;
import static def.screeps.Globals.ERR_NOT_OWNER;
import static def.screeps.Globals.FIND_STRUCTURES;
import static def.screeps.Globals.OK;
import static def.screeps.Globals.RESOURCE_ENERGY;
import static def.screeps.Globals.STRUCTURE_STORAGE;

public class DeathStroll {
    /*
     * Returns true when the death stroll is completed.
     */
    public static boolean run(Creep creep) throws Exception {
        if (!enable_death_stroll)
            return false;

        log(creep, "im dying");
        StructureStorage storage = null;

        if (creep.memory.destinationId != null) {
            log(creep, "getting storage from id");
            storage = Game.getObjectById(creep.memory.destinationId);

            if (storage.structureType.equals(STRUCTURE_STORAGE)) {
                log(creep, "got storage from id");
                creep.memory.destinationId = storage.id;
            } else {
                log(creep, "id was not an storage");
                creep.memory.destinationId = null;
            }
        }

        if (storage == null) {
            log(creep, "searching for closest storage");
            storage = creep.pos.findClosestByPath(FIND_STRUCTURES,
                    Helper.findFilter((Structure s) -> s.structureType.equals(STRUCTURE_STORAGE)));
        }

        RoomPosition spawnRoom = new RoomPosition(25, 25, creep.memory.roomSpawnedIn);

        if (storage == null) {
            log(creep, "no storage found, walking to room " + creep.memory.roomSpawnedIn);
            creep.moveTo(spawnRoom);
            return false;
        }

        double res = creep.transfer(storage, RESOURCE_ENERGY);

        if (res == OK) {
            log(creep, "transfer complete, " + creep.store.energy + " energy left");
            return false;
        } else if (res == ERR_NOT_OWNER) {
            log(creep, "storage is not mine! moving to room " + creep.memory.roomSpawnedIn);
            creep.moveTo(spawnRoom);
            return false;
        } else if (res == ERR_BUSY) {
            throw GameError.newError(creep, "i am still spawning, but already dying?!?!");
        } else if (res == ERR_NOT_ENOUGH_RESOURCES) {
            log(creep, "no more resources");
            return true;
        } else if (res == ERR_INVALID_TARGET) {
            log(creep, "cant use this target");
            creep.memory.destinationId = null;
            return false;
        } else if (res == ERR_FULL) {
            log(creep, "storage is already full, i guess this is the end then");
            return true;
        } else if (res == ERR_NOT_IN_RANGE) {
            log(creep, "moving to storage");
            creep.moveTo(storage.pos);
            return false;
        } else if (res == ERR_INVALID_ARGS) {
            throw GameError.newInvalidArgs(creep);
        } else {
            throw GameError.newUnhandledCode(creep, res);
        }
    }
}
