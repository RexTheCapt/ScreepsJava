package def.screeps;

import com.frump.screeps.memoryDef.ConstructionMemory;

import java.util.List;

@jsweet.lang.Interface
public abstract class Memory extends jsweet.lang.Object {
    public static int nameCount;
    public static double creepIndex;
    public static String[] towers;


    native public static jsweet.lang.Object $getStatic(String name);
    native public static jsweet.lang.Object $setStatic(String name,jsweet.lang.Object obj);
    public int spawnCount;
    public static Creeps creeps;
    public static Flags flags;
    public static Rooms rooms;
    public static Spawns spawns;
    public static Structures structures;
    public static ConstructionSites constructionSites;
    /** This is an automatically generated object type (see the source definition). */
    @jsweet.lang.ObjectType
    public static class Creeps extends jsweet.lang.Object {
        native public java.lang.Object $get(String name);
    }
    /** This is an automatically generated object type (see the source definition). */
    @jsweet.lang.ObjectType
    public static class Flags extends jsweet.lang.Object {
        native public java.lang.Object $get(String name);
    }
    /** This is an automatically generated object type (see the source definition). */
    @jsweet.lang.ObjectType
    public static class Rooms extends jsweet.lang.Object {
        native public java.lang.Object $get(String name);
    }
    /** This is an automatically generated object type (see the source definition). */
    @jsweet.lang.ObjectType
    public static class Spawns extends jsweet.lang.Object {
        native public java.lang.Object $get(String name);
    }

    /** This is NOT an automatically generated object type  */
    @jsweet.lang.ObjectType
    public static class Structures extends jsweet.lang.Object {
        native public java.lang.Object $get(String name);
    }

    @jsweet.lang.ObjectType
    public static class ConstructionSites extends jsweet.lang.Object {
        native public java.lang.Object $get(String name);
    }
}

