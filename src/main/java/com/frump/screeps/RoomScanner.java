package com.frump.screeps;

import com.frump.screeps.memoryDef.ExtensionInfo;
import com.frump.screeps.memoryDef.ExtractorInfo;
import com.frump.screeps.memoryDef.RoomMemory;
import com.frump.screeps.memoryDef.SourceInfo;
import def.screeps.Mineral;
import def.screeps.Room;
import def.screeps.Source;
import def.screeps.Structure;
import def.screeps.StructureExtension;
import def.screeps.StructureExtractor;
import jsweet.lang.Array;

import static com.frump.screeps.CustomLogger.log;
import static def.screeps.Globals.FIND_MINERALS;
import static def.screeps.Globals.FIND_SOURCES;
import static def.screeps.Globals.FIND_STRUCTURES;
import static def.screeps.Globals.STRUCTURE_EXTENSION;
import static def.screeps.Globals.STRUCTURE_EXTRACTOR;

@SuppressWarnings("ConstantConditions")
public class RoomScanner {
    public static void run(Room room, RoomMemory roomMemory) {
        log(room, "scanning");
        scanSources(room, roomMemory);
        scanExtensions(room, roomMemory);
        scanExtractors(room, roomMemory);
    }

    private static void scanExtractors(Room room, RoomMemory roomMemory) {
        if (roomMemory.extractors == null) {
            log(room, "initializing extractors");
            roomMemory.extractors = new Array<>(new ExtractorInfo[0]);
        }

        Array<ExtractorInfo> extractors = roomMemory.extractors;
        StructureExtractor[] found = room.find(FIND_STRUCTURES, Helper.findFilter((Structure s) -> s.structureType.equals(STRUCTURE_EXTRACTOR)));

        if (extractors.length != found.length) {
            log(room, "extractors not eq number");
            extractors = new Array<>();

            for (StructureExtractor se : found) {
                ExtractorInfo ei = (ExtractorInfo) new jsweet.lang.Object();
                ei.id = se.id;
                ei.x = se.pos.x;
                ei.y = se.pos.y;
                ei.mineralId = ((Mineral)se.pos.findClosestByRange(FIND_MINERALS, Helper.findFilter((Mineral m) -> m.pos.getRangeTo(se.pos) == 0))).id;
                extractors.push(ei);
            }

            roomMemory.extractors = extractors;
        }
    }

    private static void scanExtensions(Room room, RoomMemory roomMemory) {
        if (roomMemory.extensions == null) {
            log(room, "initializing extensions");
            roomMemory.extensions = new Array<>(new ExtensionInfo[0]);
        }

        Array<ExtensionInfo> extensions = roomMemory.extensions;
        StructureExtension[] foundExt = room.find(FIND_STRUCTURES,
                Helper.findFilter((Structure s) -> s.structureType.equals(STRUCTURE_EXTENSION)
                )
        );

        if (extensions.length != foundExt.length) {
            log(room, "extensions not eq number");
            extensions = new Array<>();

            for (StructureExtension se : foundExt) {
                ExtensionInfo ei = (ExtensionInfo) new jsweet.lang.Object();
                ei.id = se.id;
                ei.owner = se.owner.username;
                ei.x = se.pos.x;
                ei.y = se.pos.y;
                extensions.push(ei);
            }

            roomMemory.extensions = extensions;
        }
    }

    private static void scanSources(Room room, RoomMemory roomMemory) {
        if (roomMemory.sources == null) {
            log(room, "initializing sources");
            roomMemory.sources = new Array<>(new SourceInfo[0]);
        }

        Array<SourceInfo> sources = roomMemory.sources;
        Source[] foundSources = room.find(FIND_SOURCES);

        if (sources.length != foundSources.length) {
            log(room, "sources not eq number");
            sources = new Array<>();

            for (Source source : foundSources) {
                SourceInfo si = (SourceInfo) new jsweet.lang.Object();
                si.id = source.id;
                si.x = source.pos.x;
                si.y = source.pos.y;
                sources.push(si);
            }

            roomMemory.sources = sources;
        }
    }
}
