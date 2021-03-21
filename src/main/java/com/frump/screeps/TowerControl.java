package com.frump.screeps;

import def.screeps.Creep;
import def.screeps.Room;
import def.screeps.Structure;
import def.screeps.StructureRampart;
import def.screeps.StructureTower;
import def.screeps.StructureWall;

import static com.frump.screeps.CustomLogger.log;
import static com.frump.screeps.Main.minWallHealth;
import static def.screeps.Globals.*;

public class TowerControl {
    public static void run(Room room) {
        StructureTower[] towers = getTowersInRoom(room);

        Creep[] hostiles = getHostiles(room);

        if(hostiles.length > 0) {
            for (StructureTower tower : towers) {
                tower.attack(hostiles[0]);
            }
        } else {
//            for (StructureTower tower : towers) {
//                if (tower.store.energy >= 800) {
//                    log(tower, "no enemy found, repairing");
//                    repairStructure(tower);
//                }
//            }
        }
    }

    private static void repairStructure(StructureTower tower) {
        Structure structure = getStructure(tower);

        if (structure == null || structure.hits == structure.hitsMax) {
            log(tower, "Structure fixed or invalid");
            return;
        }

        double res = tower.repair(structure);

        if (res == OK) {
            log(tower, "repaired structure, hits remaining: " + (structure.hitsMax - structure.hits));
        } else {
            log(tower, res, "TowerControl.repairStructure");
        }
    }

    private static Structure getStructure(StructureTower tower) {
        return tower.pos.findClosestByRange(FIND_STRUCTURES,
                Helper.findFilter((Structure s) -> {
                            if (s.structureType.equals(STRUCTURE_WALL)) {
                                StructureWall sw = (StructureWall) s;

                                return sw.hits < minWallHealth;
                            }

                            if (s.structureType.equals(STRUCTURE_RAMPART)) {
                                StructureRampart sr = (StructureRampart) s;

                                return sr.hits < minWallHealth;
                            }

                            return s.hits != s.hitsMax;
                        }
                )
        );
    }

    public static Creep[] getHostiles(Room room) {
        return room.find(FIND_HOSTILE_CREEPS, Helper.findFilter((Creep c) -> {
            for (int i = 0; i < Main.allies.length; i++)
                if (c.owner.username.equalsIgnoreCase(Main.allies[i]))
                    return false;
            for (int i = 0; i < Main.dontAttack.length; i++)
                if (c.owner.username.equalsIgnoreCase(Main.dontAttack[i]))
                    return false;
            return true;
        }));
    }

    private static StructureTower[] getTowersInRoom(Room room) {
        return room.find(
                FIND_MY_STRUCTURES,
                Helper.findFilter((Structure s) ->
                        s.structureType.equals(STRUCTURE_TOWER)
                )
        );
    }
}
