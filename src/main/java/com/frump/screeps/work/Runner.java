package com.frump.screeps.work;

import com.frump.screeps.GameError;
import com.frump.screeps.Helper;
import def.screeps.Creep;
import def.screeps.Game;
import def.screeps.Room;
import def.screeps.Structure;
import def.screeps.StructureContainer;
import def.screeps.StructureExtension;
import def.screeps.StructureLink;
import def.screeps.StructureSpawn;
import def.screeps.StructureStorage;
import def.screeps.StructureTower;

import static com.frump.screeps.CustomLogger.log;
import static com.frump.screeps.Main.role_upgrade;
import static def.screeps.Globals.*;

public class Runner {
    public static void run(Creep creep) throws Exception {
        if (creep.ticksToLive < 100) {
            creep.say("dying");
            log(creep, "im dying");

            if (creep.store.energy == 0)
                creep.suicide();

            StructureStorage storage = creep.pos.findClosestByRange(FIND_STRUCTURES,
                    Helper.findFilter((Structure s) -> s.structureType.equals(STRUCTURE_STORAGE)));

            double transfer = creep.transfer(storage, RESOURCE_ENERGY);

            if (transfer == ERR_NOT_IN_RANGE) {
                log(creep, "moving to my last location");
                creep.moveTo(storage.pos);
            }

            return;
        }

        if (creep.memory.refill == null || creep.memory.refill) {
            log(creep, "refilling");
            Structure refillStructure = getRefillStructure(creep);

            if (handleRefill(creep, refillStructure) && creep.store.getFreeCapacity(RESOURCE_ENERGY) == 0) {
                creep.memory.refill = false;
            }
        } else {
            log(creep, "depositing");

            if (creep.room.controller.ticksToDowngrade < 40000) {
                if (giveUpgrade(creep))
                    return;
            }

            // TODO: Make all structure searches use stored target id
            if (!emptyStoreLink(creep))
                if (!giveSpawn(creep))
                    if (!giveExtension(creep))
                        if (!giveTower(creep))
                            giveUpgrade(creep);

            // TODO: create normal procedures
        }
    }

    private static boolean giveTower(Creep creep) throws Exception {
        log(creep, "looking for towers to fill");

        // TODO: make this use stored structure id
        StructureTower tower = creep.pos.findClosestByPath(FIND_STRUCTURES,
                Helper.findFilter((Structure s) -> {
                    if (s.structureType.equals(STRUCTURE_TOWER)) {
                        StructureTower st = (StructureTower) s;
                        return st.store.getFreeCapacity(RESOURCE_ENERGY) > 0;
                    }

                    return false;
                }));

        if (tower == null) {
            log(creep, "no towers found");
            return false;
        }

        double res = creep.transfer(tower, RESOURCE_ENERGY);

        if (res == OK) {
            log(creep, "transferred energy to tower");
            return true;
        } else if (res == ERR_NOT_ENOUGH_ENERGY) {
            log(creep, "need refill");
            creep.memory.refill = true;
            run(creep);
            return true;
        } else if (res == ERR_NOT_IN_RANGE) {
            log(creep, "moving to tower");
            creep.moveTo(tower.pos);
            return true;
        } else {
            throw GameError.newUnhandledCode(creep, res, "Runner.giveTower");
        }
    }

    private static boolean giveExtension(Creep creep) throws Exception {
        log(creep, "searching for extension");
        StructureExtension extension = creep.pos.findClosestByPath(FIND_STRUCTURES,
                Helper.findFilter((Structure s) -> {
                    if (s.structureType.equals(STRUCTURE_EXTENSION)) {
                        StructureExtension se = (StructureExtension) s;
                        return se.store.getFreeCapacity(RESOURCE_ENERGY) > 0;
                    }

                    return false;
                }));

        if (extension == null) {
            log(creep, "no extensions to fill");
            return false;
        }

        double res = creep.transfer(extension, RESOURCE_ENERGY);

        if (res == OK) {
            log(creep, "transferred energy to extension");
            return true;
        } else if (res == ERR_NOT_ENOUGH_ENERGY) {
            log(creep, "not enough energy, going for refill");
            creep.memory.refill = true;
            run(creep);
            return true;
        } else if (res == ERR_NOT_IN_RANGE) {
            log(creep, "moving to extension");
            creep.moveTo(extension.pos);
            return true;
        } else {
            throw GameError.newUnhandledCode(creep, res, "Runner.giveExtension");
        }
    }

    private static boolean giveSpawn(Creep creep) throws Exception {
        StructureSpawn spawn = null;

        if (creep.memory.destinationId != null) {
            log(creep, "getting spawn by id");
            Structure structure = Game.getObjectById(creep.memory.destinationId);

            //noinspection ConstantConditions
            if (spawn == null) {
                log(creep, "id was not an spawn");
                creep.memory.destinationId = null;
            } else if (structure.structureType.equals(STRUCTURE_SPAWN)) {
                spawn = (StructureSpawn) structure;
            } else {
                log(creep, "id was not an spawn");
                creep.memory.destinationId = null;
            }
        }

        //noinspection ConstantConditions
        if (spawn == null) {
            log(creep, "finding spawn by distance");
            spawn = creep.pos.findClosestByPath(FIND_MY_STRUCTURES,
                    Helper.findFilter((Structure s) -> {
                        if (s.structureType.equals(STRUCTURE_SPAWN)) {
                            StructureSpawn ss = (StructureSpawn) s;
                            return ss.store.getFreeCapacity(RESOURCE_ENERGY) > 0;
                        }

                        return false;
                    }));
        }

        if (spawn == null) {
            log(creep, "no spawn found");
            creep.memory.destinationId = null;
            return false;
        }

        double res = creep.transfer(spawn, RESOURCE_ENERGY);

        if (res == OK) {
            log(creep, "transferred energy to spawn");
            return true;
        } else if (res == ERR_NOT_IN_RANGE) {
            log(creep, "moving to spawn");
            creep.moveTo(spawn.pos);
            return true;
        } else if (res == ERR_NOT_ENOUGH_RESOURCES) {
            log(creep, "need refill");
            creep.memory.refill = true;
            run(creep);
            return true;
        } else {
            throw GameError.newUnhandledCode(creep, res, "Runner.giveSpawn");
        }
    }

    private static boolean emptyStoreLink(Creep creep) throws Exception {
        if (creep.room.memory.storageLink == null) {
            log(creep, "no storage link defined");
            return false;
        }

        log(creep, "checking store link");
        StructureLink link = Game.getObjectById(creep.room.memory.storageLink);

        if (link.store.energy == 0) {
            log(creep, "link has no energy");
            return false;
        }

        StructureStorage storage = link.pos.findClosestByRange(FIND_STRUCTURES,
                Helper.findFilter((Structure s) -> s.structureType.equals(STRUCTURE_STORAGE) && s.pos.getRangeTo(link.pos) <= 1));

        if (storage == null) {
            log(creep, "no storage near link");
            creep.room.memory.storageLink = null;
            return false;
        }

        if (creep.room.memory.storingCreep != null && !creep.room.memory.storingCreep.equals(creep.name)) {
            Creep storingCreep = Game.creeps.$get(creep.room.memory.storingCreep);

            if (storingCreep != null) {
                log(creep, "someone else is already storing, skipping");
                return false;
            }
        }

        creep.room.memory.storingCreep = creep.name;

        double res = creep.transfer(storage, RESOURCE_ENERGY);

        if (res == OK) {
            log(creep, "stored energy");
        } else if (res == ERR_NOT_IN_RANGE) {
            log(creep, "moving to storage");
            creep.moveTo(storage.pos);
        } else if (res == ERR_NOT_ENOUGH_ENERGY) {
            log(creep, "already empty");
        } else {
            throw GameError.newUnhandledCode(creep, res, "Runner.emptyStoreLink 1");
        }

        res = creep.withdraw(link, RESOURCE_ENERGY);

        if (res == OK) {
            log(creep, "took energy from link, " + link.store.energy + " left");
        } else if (res == ERR_NOT_IN_RANGE) {
            log(creep, "moving to link");
            creep.moveTo(link.pos);
        } else if (res == ERR_FULL) {
            log(creep, "already full");
        } else {
            throw GameError.newUnhandledCode(creep, res, "Runner.emptyStoreLink 2");
        }

        return true;
    }

    private static boolean giveUpgrade(Creep creep) throws Exception {
        Creep upgradeCreep = findUpgradeCreep(creep);

        if (upgradeCreep == null) {
            if (creep.store.getFreeCapacity(RESOURCE_ENERGY) > 0) {
                log(creep, "going for refill instead");
                creep.memory.refill = true;
                run(creep);
            } else {
                log(creep, "upgrade creep not found, resuming normal duties");
            }
            return false;
        }

        double res = creep.transfer(upgradeCreep, RESOURCE_ENERGY);

        if (res == OK) {
            log(creep, "transferred energy to " + upgradeCreep.name);
            return true;
        } else if (res == ERR_NOT_IN_RANGE) {
            log(creep, "moving to " + upgradeCreep.name);
            creep.moveTo(upgradeCreep.pos);
            return false;
        } else if (res == ERR_NOT_ENOUGH_RESOURCES) {
            log(creep, "going for refill");
            creep.memory.refill = true;
            run(creep);
            return true;
        } else {
            throw GameError.newUnhandledCode(creep, res, "Runner.giveUpgrade");
        }
    }

    private static Creep findUpgradeCreep(Creep creep) {
        Room room = Game.rooms.$get(creep.memory.roomSpawnedIn);

        if (creep.memory.destinationId != null) {
            log(creep, "checking destination id for upgrade creep");
            Creep c = Game.getObjectById(creep.memory.destinationId);

            if (c != null) {
                if (c.store.energy < c.store.getFreeCapacity(RESOURCE_ENERGY) / 2 && c.memory.role.equals(role_upgrade)) {
                    log(creep, c.name + " was in memory and valid target");
                    return c;
                } else {
                    log(creep, c.name + " was in memory and not an valid target");
                    creep.memory.destinationId = null;
                }
            }
        }

        log(creep, "searching for upgrade creep");
        Creep[] creeps = room.find(FIND_MY_CREEPS,
                Helper.findFilter((Creep c) -> {
                            double energyLimit = c.store.getFreeCapacity(RESOURCE_ENERGY) / 2;
                            return c.memory.role.equals(role_upgrade) && creep.store.energy < energyLimit;
                        }
                )
        );

        if (creeps.length > 0) {
            log(creep, "found " + creeps[0].name);
            creep.memory.destinationId = creeps[0].id;
            return creeps[0];
        } else {
            log(creep, "no creeps found");
            creep.memory.destinationId = null;
            return null;
        }
    }

    public static boolean handleRefill(Creep creep, Structure refillStructure) throws Exception {
        double res = creep.withdraw(refillStructure, RESOURCE_ENERGY);

        if (res == OK) {
            if (creep.store.getFreeCapacity(RESOURCE_ENERGY) > 0) {
                log(creep, "space left to fill: " + creep.store.getFreeCapacity(RESOURCE_ENERGY));
                return false;
            } else {
                log(creep, "refilled to full capacity");
                return true;
            }
        } else if (res == ERR_NOT_OWNER) {
            log(creep, "This structure is not mine!");
            creep.memory.destinationId = null;
            throw GameError.newError(creep, "not my structure!");
        } else if (res == ERR_BUSY) {
            log(creep, "still spawning");
            return false;
        } else if (res == ERR_NOT_ENOUGH_RESOURCES) {
            log(creep, "going for refill");
            creep.memory.refill = true;
            run(creep);
            return false;
        } else if (res == ERR_INVALID_TARGET) {
            log(creep, "invalid target");
            creep.memory.destinationId = null;
            return false;
        } else if (res == ERR_FULL) {
            log(creep, "storage is full, resuming work");
            return true;
        } else if (res == ERR_NOT_IN_RANGE) {
            log(creep, "moving to refill location");
            creep.moveTo(refillStructure.pos);
            return false;
        } else if (res == ERR_INVALID_ARGS) {
            throw GameError.newInvalidArgs(creep);
        } else {
            throw GameError.newUnhandledCode(creep, res, "Runner.handleRefill");
        }
    }

    public static Structure getRefillStructure(Creep creep) {
        Structure structure;
        double minCapacity = creep.store.getFreeCapacity(RESOURCE_ENERGY) / 4;

        if (creep.memory.destinationId != null) {
            log(creep, "checking destination id for refill");
            structure = Game.getObjectById(creep.memory.destinationId);

            if (STRUCTURE_STORAGE.equals(structure.structureType) || STRUCTURE_CONTAINER.equals(structure.structureType) || STRUCTURE_LINK.equals(structure.structureType)) {
                log(creep, "got " + structure.structureType);
                return structure;
            } else {
                log(creep, "id was not an energy store");
                creep.memory.destinationId = null;
            }
        }

        log(creep, "searching for closest structure");
        structure = creep.pos.findClosestByPath(FIND_STRUCTURES,
                Helper.findFilter((Structure s) -> {
                            if (s == null)
                                return false;
                            if (s.structureType == null)
                                return false;

                            if (s.structureType.equals(STRUCTURE_STORAGE)) {
                                StructureStorage ss = (StructureStorage) s;
                                return ss.store.energy >= minCapacity;
                            }
                            if (s.structureType.equals(STRUCTURE_CONTAINER)) {
                                StructureContainer sc = (StructureContainer) s;
                                return sc.store.energy >= minCapacity;
                            }
                            if (s.structureType.equals(STRUCTURE_LINK)) {
                                StructureLink sl = (StructureLink) s;
                                return sl.store.energy >= minCapacity;
                            }

                            return false;
                        }
                )
        );

        return structure;
    }
}
