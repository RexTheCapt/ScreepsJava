package com.frump.screeps.memoryDef;

import jsweet.lang.Ambient;
import jsweet.lang.Object;

@Ambient
public class CreepSpawnInfo extends Object {
    public String[] body;
    public String role;
    public String roomName;
    public String name;
    /**
     * The name of the assigned claim flag
     */
    public String claimFlag;
}
