package com.frump.screeps;

import def.screeps.Creep;
import def.screeps.Game;
import def.screeps.MarketOrder;
import def.screeps.MarketOrderFilter;
import def.screeps.Room;
import def.screeps.Source;
import def.screeps.Structure;
import def.screeps.StructureExtractor;
import def.screeps.StructureStorage;
import def.screeps.StructureTerminal;
import def.screeps.TextStyle;

import static com.frump.screeps.CustomLogger.log;
import static com.frump.screeps.Main.role_mineral_miner_enabled;
import static def.screeps.Globals.ERR_BUSY;
import static def.screeps.Globals.ERR_FULL;
import static def.screeps.Globals.ERR_INVALID_ARGS;
import static def.screeps.Globals.ERR_INVALID_TARGET;
import static def.screeps.Globals.ERR_NOT_ENOUGH_RESOURCES;
import static def.screeps.Globals.ERR_NOT_IN_RANGE;
import static def.screeps.Globals.ERR_NOT_OWNER;
import static def.screeps.Globals.FIND_SOURCES;
import static def.screeps.Globals.FIND_STRUCTURES;
import static def.screeps.Globals.OK;
import static def.screeps.Globals.ORDER_BUY;
import static def.screeps.Globals.RESOURCE_ENERGY;
import static def.screeps.Globals.STRUCTURE_EXTRACTOR;
import static def.screeps.Globals.STRUCTURE_STORAGE;

public class Functions {
    /**
     * Transfer inventory from creep to target structure.
     * Will move to target automatically if returns ERR_NOT_IN_RANGE.
     * @param creep target creep.
     * @param storage target storage.
     * @param resource target resource.
     * @return status code.
     * @throws Exception when something goes wrong.
     */
    public static double transfer(Creep creep, Structure storage, String resource) throws Exception {
         double res = creep.transfer(storage, resource);

         if (res == OK) {
             log(creep, "transferred resource");
             return OK;
         } else if (res == ERR_NOT_OWNER) {
             throw GameError.newNotOwner(creep);
         } else if (res == ERR_BUSY) {
             log(creep, "still spawning");
             return ERR_BUSY;
         } else if (res == ERR_NOT_ENOUGH_RESOURCES) {
             log(creep, "not enough resources");
             return ERR_NOT_ENOUGH_RESOURCES;
         } else if (res == ERR_INVALID_TARGET) {
             log(creep, "invalid target");
             return ERR_INVALID_TARGET;
         } else if (res == ERR_FULL) {
             log(creep, "inventory full");
             return ERR_FULL;
         } else if (res == ERR_NOT_IN_RANGE) {
             log(creep, "moving to storage");
             creep.moveTo(storage.pos);
             return ERR_NOT_IN_RANGE;
         } else if (res == ERR_INVALID_ARGS) {
             throw GameError.newInvalidArgs(creep);
         } else {
             throw GameError.newUnhandledCode(creep, res);
         }
    }

    public static double getMiningSpots(Room room) {
        StructureExtractor[] extractors = room.find(FIND_STRUCTURES, Helper.findFilter((Structure s) -> s.structureType.equals(STRUCTURE_EXTRACTOR)));
        Source[] source = room.find(FIND_SOURCES);

        log(room, "found " + source.length + " sources and " + extractors.length + " extractors");

        if (role_mineral_miner_enabled)
            return source.length + extractors.length;
        else
            return source.length;
    }

    public static StructureStorage getStorage(Creep creep) {
        return creep.pos.findClosestByRange(FIND_STRUCTURES, Helper.findFilter((Structure s) -> s.structureType.equals(STRUCTURE_STORAGE)));
    }

    public static StructureTerminal getTerminal(Creep creep) {
        return creep.room.terminal;
    }

    public static void sellExcessEnergy(Room room) {
        StructureTerminal terminal = room.terminal;

        if (terminal.cooldown != 0)
            return;

        MarketOrderFilter marketOrderFilter = (MarketOrderFilter) new jsweet.lang.Object();
        marketOrderFilter.resourceType = RESOURCE_ENERGY;
        marketOrderFilter.type = ORDER_BUY;

        MarketOrder[] allOrders = Game.market.getAllOrders(marketOrderFilter);
        double highestBid = 0;

        // Get highest bid.
        for (MarketOrder mo : allOrders) {
            if (highestBid < mo.price) {
                highestBid = mo.price;
            }
        }

        // Sell to highest bid.
        for (MarketOrder mo : allOrders) {
            if (highestBid == mo.price) {
                double distance = Game.map.getRoomLinearDistance(room.name, mo.roomName);
                double amount = 1000;

                double res = Game.market.deal(mo.id, amount, terminal.room.name);

                TextStyle ts = (TextStyle) new jsweet.lang.Object();
                ts.align = "left";
                room.visual.text("MARKET: " + (Math.ceil( amount * ( 1 - Math.exp(-distance/30) ) )) + ", " + res, 0, 4, ts);
                break;
            }
        }
    }
}
