package com.frump.screeps.memoryDef;

import jsweet.lang.Ambient;
import jsweet.lang.Array;
import jsweet.lang.Object;

@Ambient
public class RoomMemory extends Object {
    public Array<SourceInfo> sources;
    public Array<ExtensionInfo> extensions;
    public String storageLink;
    public String storingCreep;
}

