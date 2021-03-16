package com.frump.screeps.spawnCreeps;

import def.screeps.Memory;

@SuppressWarnings("SpellCheckingInspection")
public class NameList {
    public static String GetName() {
        String[] names = { "Alexander", "Alf", "Anders", "Andreas", "Arild", "Arne", "Arvid", "Asbjørn", "Bjarne", "Bjørn", "Christian", "Dag", "Daniel", "Egil", "Einar", "Eirik", "Eivind", "Erik", "Erling", "Espen", "Finn", "Frank", "Fredrik", "Frode", "Geir", "Gunnar", "Hans", "Harald", "Helge", "Henrik", "Håkon", "Håvard", "Ivar", "Jan", "Jarle", "Jens", "Johan", "Johannes", "John", "Jon", "Jonas", "Jostein", "Jørgen", "Jørn", "Karl", "Kenneth", "Kim", "Kjell", "Kjetil", "Knut", "Kristian", "Kristoffer", "Kåre", "Lars", "Leif", "Magne", "Magnus", "Marius", "Martin", "Morten", "Nils", "Odd", "Oddvar", "Ola", "Olav", "Ole", "Ove", "Per", "Petter", "Pål", "Reidar", "Roar", "Robert", "Roger", "Rolf", "Roy", "Rune", "Sigurd", "Simen", "Sindre", "Stein", "Steinar", "Stian", "StigSvein", "Sverre", "Terje", "Thomas", "Thor", "Tom", "Tommy", "Tor", "Torbjørn", "Tore", "Trond", "Trygve", "Vegard", "Vidar", "Øystein", "Øyvind" };
        return names[(int) (Memory.creepIndex++ % names.length)];
    }
}
