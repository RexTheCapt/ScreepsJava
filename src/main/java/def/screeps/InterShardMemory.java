package def.screeps;

import jsweet.lang.Interface;

@Interface
public class InterShardMemory {
    public static native String getLocal();
    public static native void setLocal(String value);
    public static native String getRemote(String shard);
}
