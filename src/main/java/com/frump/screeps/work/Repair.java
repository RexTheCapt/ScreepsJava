package com.frump.screeps.work;

import com.frump.screeps.CustomLogger;
import com.frump.screeps.GameError;
import com.frump.screeps.Helper;
import def.screeps.Creep;
import def.screeps.Game;
import def.screeps.Structure;
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
        }

        if (creep.memory.refill) {
            if (Runner.handleRefill(creep))
                creep.memory.refill = false;
            return false;
        } else {
            return repairStructure(creep);
        }
    }

    private static boolean repairStructure(Creep creep) throws Exception {
        Structure structure = getStructure(creep);

        if (structure == null || structure.hits == structure.hitsMax) {
            log(creep, "structure invalid or repaired");
            creep.memory.destinationId = null;
            return false;
        }

        double res = creep.repair(structure);

        if (res == OK) {
            log(creep, "repairing, hits left: " + (structure.hitsMax - structure.hits));
            return true;
        } else if (res == ERR_NOT_OWNER) {
            log(creep, "not my structure");
            // TODO: Do something if not mine.
            return false;
        } else if (res == ERR_BUSY) {
            log(creep, "still spawning");
            return false;
        } else if (res == ERR_NOT_ENOUGH_RESOURCES) {
            log(creep, "need refill");
            creep.memory.refill = true;
            return false;
        } else if (res == ERR_INVALID_TARGET) {
            log(creep, "invalid target");
            throw GameError.newInvalidTarget(creep);
        } else if (res == ERR_NOT_IN_RANGE) {
            log(creep, "moving to target");
            creep.moveTo(structure.pos);
            return false;
        } else if (res == ERR_NO_BODYPART) {
            log(creep, "missing body part");
            throw GameError.newNoBodyPart(creep);
        } else {
            throw GameError.newUnhandledCode(creep, res);
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

    private static void log(Creep creep, String text) {
        CustomLogger.log(creep, text);
    }
}
