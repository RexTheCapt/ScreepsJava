package com.frump.screeps.spawnCreeps;

import com.frump.screeps.BodyBuilder;
import com.frump.screeps.memoryDef.CreepSpawnInfo;
import def.screeps.Memory;
import def.screeps.Room;
import def.screeps.RoomVisual;
import def.screeps.StructureSpawn;
import def.screeps.TextStyle;
import jsweet.lang.Array;
import jsweet.lang.Object;

import java.util.HashMap;

import static com.frump.screeps.Main.role_builder;
import static com.frump.screeps.Main.role_builder_max;
import static com.frump.screeps.Main.role_miner;
import static com.frump.screeps.Main.role_miner_max;
import static com.frump.screeps.Main.role_repair;
import static com.frump.screeps.Main.role_repair_max;
import static com.frump.screeps.Main.role_runner;
import static com.frump.screeps.Main.role_runner_max;
import static com.frump.screeps.Main.role_upgrade;
import static com.frump.screeps.Main.role_upgrade_max;
import static def.screeps.Globals.CARRY;
import static def.screeps.Globals.WORK;

public class Spawn {
    public static void upgrader(HashMap<String, Double> roleCount, Room room, Array<CreepSpawnInfo> queue) {
        if (roleCount.get(role_upgrade) == null || roleCount.get(role_upgrade) < role_upgrade_max)
        {
            //noinspection ConstantConditions
            CreepSpawnInfo csi = (CreepSpawnInfo) new jsweet.lang.Object();
            csi.role = role_upgrade;
            csi.roomName = room.name;
//            csi.name = csi.role + "-" + (Memory.creepIndex) + "-" + csi.roomName;
            csi.name = csi.role + " " + (NameList.GetName());

            BodyBuilder bb = new BodyBuilder((int) room.energyAvailable, false);
            Boolean canCarry = bb.addParts(new String[]{CARRY});
            Boolean canWork = bb.addParts(new String[]{WORK});
            bb.repeatablyAddPartsX(new String[]{CARRY, WORK}, 2);
            bb.repeatablyAddPartsX(new String[]{WORK}, 5);

            if (canCarry && canWork) {
                csi.body = bb.getBody();
                queue.push(csi);
                Memory.creepIndex++;
            }
        }
    }

    public static void runner(HashMap<String, Double> roleCount, Room room, Array<CreepSpawnInfo> queue) {
        if (roleCount.get(role_runner) == null || roleCount.get(role_runner) < role_runner_max)
        {
            //noinspection ConstantConditions
            CreepSpawnInfo csi = (CreepSpawnInfo) new jsweet.lang.Object();
            csi.role = role_runner;
            csi.roomName = room.name;
//            csi.name = csi.role + "-" + (Memory.creepIndex) + "-" + csi.roomName;
            csi.name = csi.role + " " + (NameList.GetName());

            BodyBuilder bb = new BodyBuilder((int) room.energyAvailable, false);
            boolean canCarry = bb.addParts(new String[]{CARRY});
            bb.repeatablyAddPartsX(new String[]{CARRY}, 3);

            if (canCarry) {
                csi.body = bb.getBody();
                queue.push(csi);
                Memory.creepIndex++;
            }
        }
    }

    public static void miner(HashMap<String, Double> roleCount, Room room, Array<CreepSpawnInfo> queue) {
        if (roleCount.get(role_miner) == null || roleCount.get(role_miner) < role_miner_max)
        {
            //noinspection ConstantConditions
            CreepSpawnInfo csi = (CreepSpawnInfo) new jsweet.lang.Object();
            csi.role = role_miner;
            csi.roomName = room.name;
//            csi.name = csi.role + "-" + (Memory.creepIndex) + "-" + csi.roomName;
            csi.name = csi.role + " " + (NameList.GetName());

            BodyBuilder bb;
            boolean canCarry;
            boolean canWork;

            if (roleCount.get(role_miner) == 0) {
                bb = new BodyBuilder((int) room.energyAvailable, false);
                canCarry = bb.addParts(new String[]{CARRY});
                canWork = bb.addParts(new String[]{WORK});
            } else {
                bb = new BodyBuilder((int) room.energyAvailable, false);
                canCarry = bb.addParts(new String[]{CARRY});
                canWork = bb.addParts(new String[]{WORK});
                bb.repeatablyAddPartsX(new String[]{WORK}, 4);
            }

            if (canCarry && canWork) {
                csi.body = bb.getBody();
                queue.push(csi);
                Memory.creepIndex++;
            }
        }
    }

    public static void builder(HashMap<String, Double> roleCount, Room room, Array<CreepSpawnInfo> queue) {
        if (roleCount.get(role_builder) == null || roleCount.get(role_builder) < role_builder_max)
        {
            //noinspection ConstantConditions
            CreepSpawnInfo csi = (CreepSpawnInfo) new jsweet.lang.Object();
            csi.role = role_builder;
            csi.roomName = room.name;
//            csi.name = csi.role + "-" + (Memory.creepIndex) + "-" + csi.roomName;
            csi.name = csi.role + " " + (NameList.GetName());

            BodyBuilder bb = new BodyBuilder((int) room.energyAvailable, false);
            Boolean canCarry = bb.addParts(new String[]{CARRY});
            Boolean canWork = bb.addParts(new String[]{WORK});
            bb.repeatablyAddPartsX(new String[]{CARRY, WORK}, 3);

            if (canCarry && canWork) {
                csi.body = bb.getBody();
                queue.push(csi);
                Memory.creepIndex++;
            }
        }
    }

    public static void repair(HashMap<String, Double> roleCount, Room room, Array<CreepSpawnInfo> queue) {
        if (roleCount.get(role_repair) == null || roleCount.get(role_repair) < role_repair_max)
        {
            //noinspection ConstantConditions
            CreepSpawnInfo csi = (CreepSpawnInfo) new jsweet.lang.Object();
            csi.role = role_repair;
            csi.roomName = room.name;
//            csi.name = csi.role + "-" + (Memory.creepIndex) + "-" + csi.roomName;
            csi.name = csi.role + " " + (NameList.GetName());

            BodyBuilder bb = new BodyBuilder((int) room.energyAvailable, false);
            Boolean canCarry = bb.addParts(new String[]{CARRY});
            Boolean camWork = bb.addParts(new String[]{WORK});
            bb.repeatablyAddPartsX(new String[]{CARRY, WORK}, 2);

            if (canCarry && camWork) {
                csi.body = bb.getBody();
                queue.push(csi);
                Memory.creepIndex++;
            }
        }
    }

    public static void handleQueue(StructureSpawn[] spawns, Array<CreepSpawnInfo> queue, Room room) {
        RoomVisual visual = room.visual;
        TextStyle textStyle = new TextStyle();
        textStyle.align = "right";
        visual.text("Spawn queue:", 49, 0, textStyle);

        double row = 1;

        for (CreepSpawnInfo csi : queue) {
            visual.text(csi.name, 49, row, textStyle);
            row++;
        }

        textStyle.align = "left";
        textStyle.opacity = .5;

        for (StructureSpawn ss : spawns) {
            CreepSpawnInfo csi = queue.$get(0);

            if (ss.spawning != null || csi == null) {
                if (ss.spawning != null)
                    visual.text(ss.spawning.remainingTime + " " + ss.spawning.name, ss.pos.x + 1, ss.pos.y, textStyle);
                continue;
            }

            StructureSpawn.SpawnMemory ops = new StructureSpawn.SpawnMemory();
            ops.memory = new Object();
            ops.memory.$set("roomSpawnedIn", room.name);
            ops.memory.$set("role", csi.role);

            ss.spawnCreep(csi.body, csi.name, ops);
        }
    }
}
