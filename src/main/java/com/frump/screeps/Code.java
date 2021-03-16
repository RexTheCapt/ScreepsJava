package com.frump.screeps;

import java.util.ArrayList;
import java.util.List;

public class Code {
    private static String[] rawCodes = { "OK=0", "ERR_NOT_OWNER=-1", "ERR_NO_PATH=-2", "ERR_NAME_EXISTS=-3", "ERR_BUSY=-4", "ERR_NOT_FOUND=-5", "ERR_NOT_ENOUGH_ENERGY=-6", "ERR_NOT_ENOUGH_RESOURCES=-6", "ERR_INVALID_TARGET=-7", "ERR_FULL=-8", "ERR_NOT_IN_RANGE=-9", "ERR_INVALID_ARGS=-10", "ERR_TIRED=-11", "ERR_NO_BODYPART=-12", "ERR_NOT_ENOUGH_EXTENSIONS=-6", "ERR_RCL_NOT_ENOUGH=-14", "ERR_GCL_NOT_ENOUGH=-15", "FIND_EXIT_TOP=1", "FIND_EXIT_RIGHT=3", "FIND_EXIT_BOTTOM=5", "FIND_EXIT_LEFT=7", "FIND_EXIT=10", "FIND_CREEPS=101", "FIND_MY_CREEPS=102", "FIND_HOSTILE_CREEPS=103", "FIND_SOURCES_ACTIVE=104", "FIND_SOURCES=105", "FIND_DROPPED_RESOURCES=106", "FIND_STRUCTURES=107", "FIND_MY_STRUCTURES=108", "FIND_HOSTILE_STRUCTURES=109", "FIND_FLAGS=110", "FIND_CONSTRUCTION_SITES=111", "FIND_MY_SPAWNS=112", "FIND_HOSTILE_SPAWNS=113", "FIND_MY_CONSTRUCTION_SITES=114", "FIND_HOSTILE_CONSTRUCTION_SITES=115", "FIND_MINERALS=116", "FIND_NUKES=117", "FIND_TOMBSTONES=118", "FIND_POWER_CREEPS=119", "FIND_MY_POWER_CREEPS=120", "FIND_HOSTILE_POWER_CREEPS=121", "FIND_DEPOSITS=122", "FIND_RUINS=123", "TOP=1", "TOP_RIGHT=2", "RIGHT=3", "BOTTOM_RIGHT=4", "BOTTOM=5", "BOTTOM_LEFT=6", "LEFT=7", "TOP_LEFT=8", "COLOR_RED=1", "COLOR_PURPLE=2", "COLOR_BLUE=3", "COLOR_CYAN=4", "COLOR_GREEN=5", "COLOR_YELLOW=6", "COLOR_ORANGE=7", "COLOR_BROWN=8", "COLOR_GREY=9", "COLOR_WHITE=10" };
    private static Code[] codes = null;
    public final String name;
    public final double value;

    public static void init() {
        List<Code> codeList = new ArrayList<>();

        for (String rc : rawCodes) {
            String[] split = rc.split("=");
            Code code = new Code(split[0], Double.parseDouble(split[1]));
            codeList.add(code);
        }

        codes = (Code[]) codeList.toArray();
    }

    public Code(String name, double value) {
        this.name = name;
        this.value = value;
    }

    public static Code getResponse(double response) {
        for (Code c : codes) {
            if (c.value == response)
                return c;
        }

        return new Code("UnknownCode", 0);
    }

    @Override
    public String toString() {
        return name;
    }
}
