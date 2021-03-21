package com.frump.screeps.work;

import com.frump.screeps.GameError;
import def.screeps.ConstructionSite;
import def.screeps.Creep;
import def.screeps.Flag;
import def.screeps.Game;
import def.screeps.Structure;

import static com.frump.screeps.CustomLogger.log;
import static def.screeps.Globals.*;

public class Builder {
    public static void run(Creep creep) throws Exception {
        if (creep.store.energy == 0)
            creep.memory.refill = true;

        if (creep.memory.refill) {
            log(creep, "refilling");
            Structure refillStructure = Runner.getRefillStructure(creep);

            if (Runner.handleRefill(creep, refillStructure) && creep.store.getFreeCapacity(RESOURCE_ENERGY) == 0)
                creep.memory.refill = false;
        } else {
            // TODO: Implement deconstruction flag

            log(creep, "building");
            Flag priorityBuildFlag = Game.flags.$get("priorityBuild");
            ConstructionSite constructSite = null;

            // TODO: use destination id
            if (creep.memory.destinationId != null) {
                log(creep, "getting site by destination id");
                constructSite = Game.getObjectById(creep.memory.destinationId);

                if (constructSite != null) {
                    if (constructSite.progress > 0) {
                        log(creep, "got site");
                    } else {
                        log(creep, "failed");
                        constructSite = null;
                        creep.memory.destinationId = null;
                    }
                } else {
                    log(creep, "failed");
                    constructSite = null;
                    creep.memory.destinationId = null;
                }
            }

            if (constructSite == null) {
                if (priorityBuildFlag != null) {
                    log(creep, "priority flag found, getting closest by range for flag");
                    constructSite = priorityBuildFlag.pos.findClosestByRange(FIND_MY_CONSTRUCTION_SITES);
                } else {
                    log(creep, "finding closest by path");
                    constructSite = creep.pos.findClosestByPath(FIND_MY_CONSTRUCTION_SITES);
                }

                if (constructSite != null) {
                    log(creep, "construction site found");
                    creep.memory.destinationId = constructSite.id;
                } else {
                    log(creep, "no construction site found");
                    creep.memory.destinationId = null;
                }
            }

            if (constructSite != null) {
                buildStructure(creep, constructSite);
            } else {
                if (!Repair.run(creep))
                    Upgrade.run(creep);
            }
        }
    }

    private static void buildStructure(Creep creep, ConstructionSite constructSite) throws Exception {
        double res = creep.build(constructSite);

        if (res == OK) {
            log(creep, constructSite.structureType + " built");
        } else if (res == ERR_NOT_OWNER) {
            log(creep, "this construction site is not mine!");
            // TODO: do something if im not the owner.
        } else if (res == ERR_BUSY) {
            log(creep, "still spawning");
        } else if (res == ERR_NOT_ENOUGH_RESOURCES) {
            log(creep, "refill needed");
            creep.memory.refill = true;
            run(creep);
        } else if (res == ERR_INVALID_TARGET) {
            log(creep, "this is an invalid target");
            creep.memory.destinationId = null;
        } else if (res == ERR_NOT_IN_RANGE) {
            log(creep, "moving to construction site");
            creep.moveTo(constructSite.pos);
        } else if (res == ERR_NO_BODYPART) {
            log(creep, "I have no body part for this, time to die");
            throw GameError.newNoBodyPart(creep);
        }
    }
}
