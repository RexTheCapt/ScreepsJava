package com.frump.screeps.work;

import com.frump.screeps.GameError;
import com.frump.screeps.Helper;
import com.frump.screeps.memoryDef.ExtractorInfo;
import com.frump.screeps.memoryDef.SourceInfo;
import def.screeps.ConstructionSite;
import def.screeps.Creep;
import def.screeps.Game;
import def.screeps.Source;
import def.screeps.Structure;
import def.screeps.StructureContainer;
import def.screeps.StructureExtension;
import def.screeps.StructureLink;
import def.screeps.StructureSpawn;
import def.screeps.StructureStorage;
import def.screeps.StructureTower;
import jsweet.lang.Array;

import static com.frump.screeps.CustomLogger.log;
import static def.screeps.Globals.*;

public class Miner {
    public static void run(Creep creep) throws Exception {
        if (creep.memory.destinationId == null) {
            log(creep, "looking for mining spot");

            if (!assignEnergySource(creep))
                    throw GameError.newError(creep, "mining spot not found");
        } else {
            if (creep.memory.miningType.equals("energy")) {
                if (creep.store.energy > creep.store.getCapacity(RESOURCE_ENERGY) / 2) {
                    if (!buildFixCloseBy(creep))
                        if (!depositEnergy(creep))
                            runLink(creep);
                } else {
                    mineSource(creep);
                }
            } else if (creep.memory.miningType.equals(("mineral"))) {
                // TODO: Mine minerals.
            }
        }
    }

    private static void mineSource(Creep creep) throws Exception {
        Source source = Game.getObjectById(creep.memory.destinationId);

        if (source == null) {
            log(creep, "invalid source");
            throw GameError.newError(creep, "source not found");
        }

        double res = creep.harvest(source);

        if (res == OK) {
            log(creep, "harvested energy");
        } else if (res == ERR_NOT_IN_RANGE) {
            log(creep, "moving to source");
            creep.moveTo(source.pos);
        } else {
            creep.say("C: " + res);
            log(creep, "Miner.mineSource");
            throw GameError.newUnhandledCode(creep, res);
        }
    }

    private static void runLink(Creep creep) throws Exception {
        log(creep, "looking for link");
        StructureLink structureLink = creep.pos.findClosestByRange(FIND_STRUCTURES,
                Helper.findFilter((Structure s) -> s.structureType.equals(STRUCTURE_LINK) && s.pos.getRangeTo(creep.pos) <= 1 && ((StructureLink)s).cooldown == 0));

        if (structureLink == null) {
            log(creep, "no links to use");
            return;
        }

        StructureLink targetLink = getStoreLink(creep);

        double res = structureLink.transferEnergy(targetLink);

        if (res == OK) {
            log(creep, "transferred energy");
        } else {
            creep.say("C: " + res);
            log(creep, "Miner.runLink");
        }
    }

    private static StructureLink getStoreLink(Creep creep) throws Exception {
        log(creep, "looking for destination");

        if (creep.room.memory.storageLink != null) {
            log(creep, "using room storage link id");
            StructureLink link = Game.getObjectById(creep.room.memory.storageLink);

            if (link != null) {
                log(creep, "link found, checking storage");
                StructureStorage storage = link.pos.findClosestByRange(FIND_STRUCTURES,
                        Helper.findFilter((Structure s) -> s.structureType.equals(STRUCTURE_STORAGE) && s.pos.getRangeTo(link.pos) <= 1));

                if (storage == null) {
                    log(creep, "no storage");
                    creep.room.memory.storageLink = null;
                } else {
                    log(creep, "storage found");
                    return link;
                }
            } else {
                log(creep, "not an link");
                creep.room.memory.storageLink = null;
            }
        }

        StructureStorage[] storages = creep.room.find(FIND_STRUCTURES,
                Helper.findFilter((Structure s) -> s.structureType.equals(STRUCTURE_STORAGE)));

        if (storages.length == 1) {
            log(creep, "storage found");
            StructureStorage storage = storages[0];
            StructureLink link = storage.pos.findClosestByRange(FIND_STRUCTURES,
                    Helper.findFilter((Structure s) -> s.structureType.equals(STRUCTURE_LINK) &&
                            s.pos.getRangeTo(storage.pos) <= 1));

            creep.room.memory.storageLink = link.id;

            return link;
        } else {
            throw GameError.newError(creep, "too many storages");
        }
    }

    private static boolean depositEnergy(Creep creep) throws Exception {
        log(creep, "looking for a place to deposit");
        Structure structure = creep.pos.findClosestByRange(FIND_STRUCTURES,
                Helper.findFilter((Structure s) -> {
                    if (s.pos.getRangeTo(creep.pos) > 1)
                        return false;

                    if (s.structureType.equals(STRUCTURE_TOWER))
                        return ((StructureTower) s).store.getFreeCapacity(RESOURCE_ENERGY) > 0;
                    if (s.structureType.equals(STRUCTURE_SPAWN))
                        return ((StructureSpawn)s).store.getFreeCapacity(RESOURCE_ENERGY) > 0;
                    if (s.structureType.equals(STRUCTURE_STORAGE))
                        return ((StructureStorage) s).store.getFreeCapacity(RESOURCE_ENERGY) > 0;
                    if (s.structureType.equals(STRUCTURE_LINK))
                        return ((StructureLink)s).store.getFreeCapacity(RESOURCE_ENERGY) > 0;
                    if (s.structureType.equals(STRUCTURE_CONTAINER))
                        return ((StructureContainer) s).store.getFreeCapacity(RESOURCE_ENERGY) > 0;
                    if (s.structureType.equals(STRUCTURE_EXTENSION))
                        return ((StructureExtension) s).store.getFreeCapacity(RESOURCE_ENERGY) > 0;
                    return false;
                }));

        if (structure == null) {
            log(creep, "no structure to deposit to");
            return false;
        }

        double res = creep.transfer(structure, RESOURCE_ENERGY);

        if (res == OK) {
            log(creep, "transferred energy to " + structure.structureType);
            return true;
        } else {
            creep.say("C: " + res);
            log(creep, "Miner.depositEnergy");
            throw GameError.newUnhandledCode(creep, res);
        }
    }

    private static boolean buildFixCloseBy(Creep creep) throws Exception {
        log(creep, "looking for construction sites");
        ConstructionSite constructionSite = creep.pos.findClosestByRange(FIND_CONSTRUCTION_SITES,
                Helper.findFilter((ConstructionSite cs) -> cs.pos.getRangeTo(creep.pos) <= 1));

        if (constructionSite == null) {
            log(creep, "looking for repairs");
            Structure structure = creep.pos.findClosestByRange(FIND_STRUCTURES,
                    Helper.findFilter((Structure s) -> s.hits != s.hitsMax && creep.pos.getRangeTo(s.pos) <= 1));

            if (structure == null) {
                log(creep, "no repairs found");
                return false;
            } else {
                log(creep, "repairing " + structure.structureType);

                double res = creep.repair(structure);

                if (res == OK) {
                    return true;
                } else if (res == ERR_NOT_IN_RANGE) {
                    log(creep, structure.structureType + " not in range, wont fix");
                    return false;
                } else {
                    creep.say("C: " + res);
                    log(creep, "Miner.buildFixCloseBy");
                    throw GameError.newUnhandledCode(creep, res);
                }
            }
        } else {
            log(creep, "building");

            double res = creep.build(constructionSite);

            if (res == OK) {
                return true;
            } else {
                creep.say("C: " + res);
                log(creep, "Miner.buildFixCloseBy");
                throw GameError.newUnhandledCode(creep, res);
            }
        }
    }

    private static boolean assignEnergySource(Creep creep) {
        Array<SourceInfo> sources = creep.room.memory.sources;

        boolean assigned = false;

        // Assign energy source.
        for (SourceInfo source : sources) {
            if (source.miner == null || source.miner.isEmpty()) {
                log(creep, "taking source");
                source.miner = creep.name;
                assigned = true;
            } else if (Game.creeps.$get(source.miner) == null) {
                log(creep, "replacing " + source.miner);
                source.miner = creep.name;
                assigned = true;
            }

            if (assigned) {
                log(creep, "writing energy source to memory");
                creep.memory.miningType = "energy";
                creep.memory.destinationId = source.id;
                break;
            }
        }

        // Assign extractor.
        if (!assigned) {
            Array<ExtractorInfo> extractors = creep.room.memory.extractors;

            for (ExtractorInfo ei : extractors) {
                if (ei.miner == null) {
                    log(creep, "taking mineral");
                    ei.miner = creep.name;
                    assigned = true;
                } else if (Game.creeps.$get(ei.miner) == null) {
                    log(creep, "replacing " + ei.miner);
                    ei.miner = creep.name;
                    assigned = true;
                }

                if (assigned) {
                    log(creep, "writing mineral to memory");
                    creep.memory.miningType = "mineral";
                    creep.memory.destinationId = ei.mineralId;
                    break;
                }
            }
        }

        if (!assigned) {
            log(creep, "was not able to get source");
        }

        return assigned;
    }
}
