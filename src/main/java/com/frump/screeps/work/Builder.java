package com.frump.screeps.work;

import com.frump.screeps.Code;
import com.frump.screeps.DeathStroll;
import com.frump.screeps.GameError;
import com.frump.screeps.Helper;
import def.screeps.ConstructionSite;
import def.screeps.Creep;
import def.screeps.Flag;
import def.screeps.Game;
import def.screeps.Structure;
import def.screeps.StructureContainer;
import def.screeps.StructureSpawn;

import static com.frump.screeps.CustomLogger.log;
import static def.screeps.Globals.*;

public class Builder {
    public static void run(Creep creep) throws Exception {
        Flag flag = Game.flags.$get("deconstruct");

        if (flag != null && flag.color == COLOR_RED && flag.secondaryColor == COLOR_RED) {
            log(creep, "deconstruction flag found");
            Structure[] structures = flag.pos.lookFor(LOOK_STRUCTURES);

            if (structures == null || structures.length == 0) {
                log(creep, "no structures found, removing flag");
                flag.remove();
            } else {
                log(creep, "dismantling " + structures[0].structureType);
                Code response = Code.getResponse(creep.dismantle(structures[0]));
                double res = response.value;

                if (res == OK) {
                    log(creep, structures[0].hits + " hits left");
                } else if (res == ERR_NOT_IN_RANGE) {
                    log(creep, "moving to " + structures[0].structureType);
                    creep.moveTo(structures[0].pos);
                } else {
                    log(creep, "Builder.buildSite");
                    creep.say("C: " + res);
                    throw GameError.newUnhandledCode(creep, response, "Builder.run");
                }
            }

            return;
        }

        if (!creep.memory.refill)
            buildSite(creep);
        else
            refill(creep);
    }

    private static void refill(Creep creep) {
        log(creep, "refill");
        StructureContainer container = getContainer(creep);

        if (container != null)
        {
            log(creep, "found container");

            log(creep, "taking energy");
            double res = creep.withdraw(container, RESOURCE_ENERGY);

            if (res != OK) {
                if (res == ERR_NOT_IN_RANGE) {
                    creep.moveTo(container.pos);
                    log(creep, "failed, moving to energy");
                }
                else if (res == ERR_FULL)
                {
                    log(creep, "full: " + creep.store.getCapacity(RESOURCE_ENERGY));
                    creep.memory.refill = false;
                }
                else
                    log(creep, res, "Builder.refill");
            }
            else
                log(creep, "Energy taken, current: " + creep.store.energy);
        }
    }

    private static StructureContainer getContainer(Creep creep) {
        log(creep, "searching for container...");
        return creep.pos.findClosestByPath(
                FIND_STRUCTURES,
                Helper.findFilter((Structure s) ->
                        s.structureType.equals(STRUCTURE_CONTAINER) &&
                                (((StructureContainer) s).store.energy >= creep.store.getCapacity(RESOURCE_ENERGY))
                )
        );
    }

    private static void buildSite(Creep creep) {
        ConstructionSite constructionSite = getConstructionSite(creep);

        if (creep.memory.destinationId == null || creep.memory.destinationId.isEmpty()) {
            if (constructionSite == null) {
                log(creep, "construction site not found, upgrading controller");
                creep.memory.upgradingController = true;
                Upgrade.run(creep);
                return;
            } else {
                creep.memory.upgradingController = false;
                log(creep, "assigned construction site");
                creep.memory.destinationId = constructionSite.id;
            }
        }

        log(creep, "building");
        double res = creep.build(constructionSite);

        if (res != OK) {
            if (res == ERR_NOT_ENOUGH_RESOURCES) {
                log(creep, "Need to refill");
                creep.memory.destinationId = null;
                creep.memory.refill = true;
            }
            else if (res == ERR_NOT_IN_RANGE) {
                log(creep, "going to construction site");
                creep.moveTo(constructionSite.pos);
            } else if (res == ERR_INVALID_TARGET) {
                log(creep, "invalid construction target, clearing");
                creep.memory.destinationId = null;
            } else {
                creep.say("C: " + res);
                log(creep, res, "Builder.buildSite");
            }
        }
    }

    private static ConstructionSite getConstructionSite(Creep creep) {
        if (creep.memory.destinationId == null || creep.memory.destinationId.isEmpty()) {
            log(creep, "finding construction");

            Flag priorityBuild = Game.flags.$get("priorityBuild");

            if (priorityBuild != null && priorityBuild.room.name.equals(creep.room.name)) {
                log(creep, "priority found");
                return priorityBuild.pos.findClosestByRange(FIND_MY_CONSTRUCTION_SITES);
            } else
                return creep.pos.findClosestByPath(FIND_MY_CONSTRUCTION_SITES);
        } else {
            log(creep, "getting construction site from id");
            return Game.getObjectById(creep.memory.destinationId);
        }
    }
}
