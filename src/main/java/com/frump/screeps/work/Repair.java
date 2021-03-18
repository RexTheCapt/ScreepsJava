package com.frump.screeps.work;

import com.frump.screeps.CustomLogger;
import com.frump.screeps.Helper;
import def.screeps.Creep;
import def.screeps.Game;
import def.screeps.Structure;
import def.screeps.StructureContainer;
import def.screeps.StructureRampart;
import def.screeps.StructureWall;

import static com.frump.screeps.Main.minWallHealth;
import static def.screeps.Globals.*;

public class Repair {
    /**
     * Returns true if structure is repaired.
     * @param creep creep.
     * @return true on repair.
     * @throws Exception when something goes wrong.
     */
    public static boolean run(Creep creep) throws Exception {
        if (creep.store.energy == 0) {
            creep.memory.refill = true;
            creep.memory.destinationId = null;
        } else if (creep.store.getFreeCapacity(RESOURCE_ENERGY) == 0 && creep.memory.refill) {
            log(creep, "back to work");
            creep.memory.refill = false;
            creep.memory.destinationId = null;
        }

        if (creep.memory.refill) {
            refill(creep);
        } else {
            repairStructure(creep);
        }
    }

    private static void repairStructure(Creep creep) {
        Structure structure = getStructure(creep);

        if (structure == null || structure.hits == structure.hitsMax) {
            log(creep, "structure invalid or repaired");
            creep.memory.destinationId = null;
            return;
        }

        double res = creep.repair(structure);

        if (res == OK) {
            log(creep, "repairing, hits left: " + (structure.hitsMax - structure.hits));
        } else {
            if (res == ERR_NOT_IN_RANGE) {
                log(creep, "moving to structure");
                creep.moveTo(structure.pos);
            } else {
                log(creep, res, "Repair.repairStructure");
            }
        }
    }

    private static Structure getStructure(Creep creep) {
        Structure structure;

        if (creep.memory.destinationId == null || creep.memory.destinationId.isEmpty()) {
            log(creep, "finding closest structure to repair");
            structure = creep.pos.findClosestByPath(
                    FIND_STRUCTURES,
                    Helper.findFilter((Structure s) -> {
                                if (s.structureType.equals(STRUCTURE_WALL)) {
                                    StructureWall sw = (StructureWall) s;

                                    return sw.hits < minWallHealth;
                                }

                                if (s.structureType.equals(STRUCTURE_RAMPART)) {
                                    StructureRampart sr = (StructureRampart) s;
                                    return sr.hits < minWallHealth;
                                }

                                return s.hits != s.hitsMax && !s.structureType.equals(STRUCTURE_CONTAINER);
                            }
                    )
            );
        } else {
            log(creep, "getting structure by id");
            structure = Game.getObjectById(creep.memory.destinationId);
        }

        return structure;
    }

    private static void refill(Creep creep) {
        StructureContainer container = getEnergyContainer(creep);

        if (container == null) {
            log(creep, "resetting refill destination");
            creep.memory.destinationId = null;
            return;
        }

        double res = creep.withdraw(container, RESOURCE_ENERGY);

        if (res == OK) {
            log(creep, "refilled energy, " + creep.store.getFreeCapacity(RESOURCE_ENERGY) + " left");

            if (creep.store.getFreeCapacity(RESOURCE_ENERGY) == 0) {
                log(creep, "refill complete");
                creep.memory.refill = false;
            }
        } else {
            if (res == ERR_NOT_IN_RANGE) {
                log(creep, "moving to container");
                creep.moveTo(container.pos);
            } else if (res == ERR_NOT_ENOUGH_ENERGY) {
                log(creep, "not enough energy");
                creep.memory.destinationId = null;
            } else {
                log(creep, res, "Repair.refill");
            }
        }
    }

    private static void log(Creep creep, double result, String text) {
        CustomLogger.log(creep, result, text);
    }

    private static StructureContainer getEnergyContainer(Creep creep) {
        StructureContainer container;

        if (creep.memory.destinationId == null || creep.memory.destinationId.isEmpty()) {
            log(creep, "finding closest container");
            container = creep.pos.findClosestByPath(
                    FIND_STRUCTURES,
                    Helper.findFilter((Structure s) ->
                            s.structureType.equals(STRUCTURE_CONTAINER) &&
                                    ((StructureContainer) s).store.energy >= creep.store.getFreeCapacity(RESOURCE_ENERGY)
                    )
            );

            if (container != null) {
                creep.memory.destinationId = container.id;
            }
        } else {
            log(creep, "finding container by id");
            container = Game.getObjectById(creep.memory.destinationId);
        }

        return container;
    }

    private static void log(Creep creep, String text) {
        CustomLogger.log(creep, text);
    }
}
