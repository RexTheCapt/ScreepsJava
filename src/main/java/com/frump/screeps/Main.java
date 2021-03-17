package com.frump.screeps;

import com.frump.screeps.memoryDef.CreepMemory;
import com.frump.screeps.memoryDef.CreepSpawnInfo;
import com.frump.screeps.memoryDef.RoomMemory;
import com.frump.screeps.memoryDef.SpawnMemory;
import com.frump.screeps.spawnCreeps.Spawn;
import com.frump.screeps.work.Miner;
import com.frump.screeps.work.Builder;
import com.frump.screeps.work.Repair;
import com.frump.screeps.work.Runner;
import com.frump.screeps.work.Upgrade;
import def.screeps.Creep;
import def.screeps.Game;
import def.screeps.Memory;
import def.screeps.Room;
import def.screeps.RoomVisual;
import def.screeps.Structure;
import def.screeps.StructureController;
import def.screeps.StructureLink;
import def.screeps.StructureRampart;
import def.screeps.StructureSpawn;
import def.screeps.TextStyle;
import jsweet.lang.Array;
import jsweet.lang.Object;

import java.util.HashMap;

import static def.screeps.Globals.FIND_MY_CREEPS;
import static def.screeps.Globals.FIND_STRUCTURES;
import static def.screeps.Globals.STRUCTURE_RAMPART;
import static def.screeps.Globals.STRUCTURE_SPAWN;
import static jsweet.util.Globals.$export;


public class Main {
    private static Main instance;
    public static final String role_miner = "miner";
    public static final double role_miner_max = 2;
    public static final boolean role_miner_enabled = true;

    public static final String role_runner = "runner";
    public static final double role_runner_max = 3;
    public static final boolean role_runner_enabled = true;

    public static final String role_upgrade = "upgrader";
    public static final double role_upgrade_max = 2;
    public static final boolean role_upgrade_enabled = true;

    public static final String role_builder = "builder";
    public static final double role_builder_max = 2;
    public static final boolean role_builder_enabled = true;

    public static final String role_repair = "repairer";
    public static final double role_repair_max = 1;
    public static final boolean role_repair_enabled = true;

    public static final boolean enable_death_stroll = false;

    public static final boolean spawn_only_when_max_energy = false;
    public static final double minWallHealth = 21000;

    public static final String[] allies = { "SirFrump"};
    @SuppressWarnings("SpellCheckingInspection")
    public static final String[] dontAttack = { "likeafox" };

    private static final Array<Double> rollingAverage = new Array<>();

    // TODO: Get this number to automatically increment
    private static final int version_number = 5;

    public void init(){
        if(Memory.constructionSites == null){
            Memory.$setStatic("constructionSites",new Memory.ConstructionSites());
        }
        if(Memory.creeps == null){
            Memory.$setStatic("creeps",new Memory.Creeps());
        }
        if(Memory.rooms == null){
            Memory.$setStatic("rooms",new Memory.Rooms());
        }
        if(Memory.spawns == null){
            Memory.$setStatic("spawns",new Memory.Spawns());
        }
        if(Memory.structures == null){
            Memory.$setStatic("structures",new Memory.Structures());
        }

        Game.notify("Initialized version " + version_number);

        System.out.println("init");
    }

    public void loop() {
        // Start stuff
        System.out.println("----- NEW LOOP -----\r\r");

        cleanup();

        System.out.println("Runs since update: " + rollingAverage.length);

        // Loop through rooms
        if (Memory.rooms == null) {
            Memory.rooms = new Memory.Rooms();
        }

        Mapper<Room> rooms = new Mapper<>(Game.rooms);
        for (String roomName : rooms.getKeys()) {
            // Initialize room
            if (Memory.rooms.$get(roomName) == null) {
                Memory.rooms.$set(roomName, new Object());
            }

            RoomMemory rm = (RoomMemory) Memory.rooms.$get(roomName);
            Room room = Game.rooms.$get(roomName);
            if (room.controller != null) {
                if (room.controller.my && room.controller.level > 0) {
                    //this room is claimed, setup memory
                    if (rm.storingCreep != null) {
                        StructureLink link = Game.getObjectById(rm.storageLink);

                        if (link != null && link.store.energy == 0) {
                            rm.storingCreep = null;
                        }
                    }

                    StructureRampart[] ramparts = room.find(FIND_STRUCTURES,
                            Helper.findFilter((Structure s) -> s.structureType.equals(STRUCTURE_RAMPART)));

                    for (StructureRampart r : ramparts)
                        r.setPublic(TowerControl.getHostiles(room).length == 0);

                    RoomScanner.run(room, rm);
                    TowerControl.run(room);
                    spawnCreeps(room);
                }
            }
        }

        // Do tasks
        Mapper<Creep> creeps = new Mapper<>(Game.creeps);
        for (String creepName : creeps.getKeys()) {
            Creep creep = Game.creeps.$get(creepName);
            try {

                if (creep.spawning)
                    continue;

                if (creep.fatigue > 0)
                    continue;
                runTasksForCreep(creep);
            } catch (Exception e) {
                GameError ge = (GameError) e;

                String nl = "\r\n";

                Array<String> log = creep.memory.log;
                double offset = 5;
                StringBuilder msg = new StringBuilder("Version: " + version_number + nl + "Last " + offset + " actions:" + nl);

                for (double i = log.length - offset; i < log.length; i++) {
                    msg.append(log.$get(i)).append(nl);
                }

                msg.append(nl);

                msg.append("Message: ").append(ge.message).append(nl).append("Trace: ").append(ge.stack);

                System.out.println(msg);
                Game.notify(msg.toString());
            }
        }

        // End stuff before CPU count
        //noinspection ConstantConditions
        TextStyle ts = (TextStyle) new jsweet.lang.Object();
        RoomVisual visual = Game.spawns.$get("Spawn1").room.visual;
        ts.align = "left";
        visual.text("BUCKET: " + Game.cpu.bucket, 0, 2, ts);
        visual.text("REMAINING RCL: " + remainingRclProgress(Game.spawns.$get("Spawn1").room.controller), 0, 3, ts);

        double avgAll = 0;
        double avgLast50 = 0;
        double avgLast10 = 0;
        double avgLength = rollingAverage.length;

        // Calculate average cpu time
        for (int x = 0; x < avgLength; x++) {
            avgAll += rollingAverage.$get(x);

            if (avgLength > 10) {
                if (x >= avgLength - 10)
                    avgLast10 += rollingAverage.$get(x);

                if (avgLength > 50) {
                    if (x >= avgLength - 50)
                        avgLast50 += rollingAverage.$get(x);
                }
            }
        }

        if (avgAll > 0)
            avgAll = round(avgAll / avgLength, 2);
        if (avgLast10 > 0)
            avgLast10 = round(avgLast10 / 10, 2);
        if (avgLast50 > 0)
            avgLast50 = round(avgLast50 / 50, 2);

        final double timeFinish = Game.cpu.getUsed();

        // After CPU count
        visual.text("CPU: " + avgAll + " / " + avgLast50 + " / " + avgLast10, 0, 1, ts);
        rollingAverage.push(timeFinish);
    }

    @SuppressWarnings("SameParameterValue")
    private double round(double toRound, int places) {
        return Math.floor(toRound * (10^places)) / (10^places);
    }

    private double remainingRclProgress(StructureController controller) {
        return controller.progressTotal - controller.progress;
    }

    private void runTasksForCreep(Creep creep) throws Exception {
        String role = creep.memory.role;

        if (creep.ticksToLive < 100)
            if (DeathStroll.run(creep))
                return;

        switch (role) {
            case role_miner:
                Miner.run(creep);
                break;
            case role_upgrade:
                Upgrade.run(creep);
                break;
            case role_runner:
                Runner.run(creep);
                break;
            case role_builder:
                Builder.run(creep);
                break;
            case role_repair:
                Repair.run(creep);
                break;
            default:
                CustomLogger.log(creep, null, "No task for role");
                break;
        }
    }

    private void spawnCreeps(Room room) {
        // Spawn only when max energy toggle
        if (spawn_only_when_max_energy)
            if (room.energyAvailable != room.energyCapacityAvailable)
                return;

        // Get stuff
        StructureSpawn[] spawns = room.find(FIND_STRUCTURES, Helper.findFilter((Structure s) -> s.structureType.equals(STRUCTURE_SPAWN)));
        Array<CreepSpawnInfo> queue = new Array<>();

        HashMap<String, Double> roleCount = countCreepRoles(queue, room);

        if (role_miner_enabled)
            Spawn.miner(roleCount, room, queue);

        if (roomHasMiners(room)) {
            if (role_runner_enabled)
                Spawn.runner(roleCount, room, queue);

            if (role_upgrade_enabled)
                Spawn.upgrader(roleCount, room, queue);

            if (role_builder_enabled)
                Spawn.builder(roleCount, room, queue);

            if (role_repair_enabled)
                Spawn.repair(roleCount, room, queue);
        }

        Spawn.handleQueue(spawns, queue, room);
    }

    private boolean roomHasMiners(Room room) {
        Creep[] creeps = room.find(FIND_MY_CREEPS,
                Helper.findFilter((Creep c) -> c.memory.role.equals(role_miner)
                )
        );

        return creeps.length > 1;
    }

    private HashMap<String, Double> countCreepRoles(Array<CreepSpawnInfo> queue, Room room) {
        HashMap<String, Double> roleCount = new HashMap<>();

        for (int i = 0; i < queue.length; i++) {
            if (roleCount.containsKey(queue.$get(i).role))
                roleCount.put(queue.$get(i).role, roleCount.get(queue.$get(i).role) + 1);
            else
                roleCount.put(queue.$get(i).role, 1.0);
        }

        Mapper<Creep> creeps = new Mapper<>(Game.creeps);
        for (Creep creep : creeps.getValues()){
            if (!room.name.equals(creep.memory.roomSpawnedIn)) continue;

            if (creep.memory.role == null)
                creep.suicide();
            else if (roleCount.containsKey(creep.memory.role))
                roleCount.put(creep.memory.role, roleCount.get(creep.memory.role) + 1);
            else
                roleCount.put(creep.memory.role, 1.0);
        }

        return roleCount;
    }

    private void cleanup() {
        //clear dead creeps
        Mapper<CreepMemory> creeps = new Mapper<>(Memory.creeps);
        String[] keys = creeps.getKeys();
        for(String s : keys){
            if(Game.creeps.$get(s) == null) {
                System.out.println("Say goodbye to " + s);
                Memory.creeps.$delete(s);
            }
        }

        // Clean up spawns from memory
        Mapper<SpawnMemory> spawns = new Mapper<>(Memory.spawns);
        keys = spawns.getKeys();
        for(String s : keys){
            if(Game.spawns.$get(s) == null){
                Memory.spawns.$delete(s);
            }
        }
    }

    public static Main getInstance() {
        return instance;
    }

    public static void main(String[] args) {
        instance = new Main();
        $export("main", instance);
    }

}