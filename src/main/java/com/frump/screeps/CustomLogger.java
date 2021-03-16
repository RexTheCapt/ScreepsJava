package com.frump.screeps;

import def.screeps.Creep;
import def.screeps.Game;
import def.screeps.Room;
import def.screeps.StructureTower;
import jsweet.lang.Array;

public class CustomLogger {
    public static void log(Creep creep, Double result, String text)
    {
        if (creep.memory.log == null)
            creep.memory.log = new Array<>();

        while (creep.memory.log.length >= 100)
            creep.memory.log.shift();

        String prefix;

        if (Game.time % 2 == 0)
            prefix = "-";
        else
            prefix = "|";

        if (result != null) {
            System.out.println(creep.name + " : [" + result + "] " + text);
            creep.memory.log.push(prefix + " [" + result + "] " + text);
        } else {
            System.out.println(creep.name + " : " + text);
            creep.memory.log.push(prefix + " " + text);
        }
    }

    public static void log(Creep creep, String text) {
        log(creep, null, text);
    }

    public static void log(StructureTower tower, Double result, String text)
    {
        if (result != null)
            System.out.println("T-" + tower.pos.x + "," + tower.pos.y + "-" + tower.room.name + " : [" + result + "] " + text);
        else
            System.out.println("T-" + tower.pos.x + "," + tower.pos.y + "-" + tower.room.name + " : " + text);
    }

    public static void log(StructureTower tower, String text) {
        log(tower, null, text);
    }

    public static void log(Room room, String text) {
        System.out.println("R-" + room.name + " : " + text);
    }
}
