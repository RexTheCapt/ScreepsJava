package com.frump.screeps;

import jsweet.lang.Math;

import java.util.ArrayList;

public class BodyBuilder {
    private int MAX_PARTS_FOR_ROADS = 33;//17 moves(must round up) (Math.ceil(33.0 / 2) = 17)
    private int MAX_PARTS_FOR_NO_ROADS = 25;//25 moves

    private int maxEnergy;
    private int currentCost;
    private ArrayList<String> body;
    private boolean roads;

    private BodyBuilder(){}//NO!

    public BodyBuilder(int maxEnergy,boolean roads){
        this.maxEnergy = maxEnergy;
        this.body = new ArrayList<>();
        this.currentCost = 0;
        this.roads = roads;
    }

    public static int getTotalCost(String[] body) {
        int total = 0;

        for (String s : body) {
            total += getPartCost(s);
        }

        return total;
    }

    public int getMaxEnergy() {
        return maxEnergy;
    }

    public void repeatablyAddPartsX(String[] sequence,int x){
        int y = 0;
        while(addParts(sequence) && y<x){
            y++;
        }
    }

    public void repeatablyAddParts(String[] sequence){
        while(addParts(sequence)){
            //do nothing
        }
    }

    public boolean addParts(String[] sequence){
        if(sequence.length + this.body.size() > (roads ? MAX_PARTS_FOR_ROADS : MAX_PARTS_FOR_NO_ROADS)){
            return false;
        }
        int movesNeeded = roads ? (int)Math.ceil((sequence.length + this.body.size())/2) : (sequence.length + this.body.size());
        int seqCost = 0;
        for(String s : sequence){
            seqCost += getPartCost(s);
        }
        if(currentCost+seqCost+(movesNeeded*50) > maxEnergy){
            return false;
        }
        for(String s : sequence){
            currentCost += getPartCost(s);
            body.add(s);
        }
        return true;
    }

    public String[] getBody(){
        int movesNeeded = roads ? (int) Math.ceil(this.body.size()/2) + 1 : this.body.size();
        String[] bodi = new String[this.body.size()+movesNeeded];
        int z = 0;
        for(int x = 0; x < bodi.length;x++){
            if(x < movesNeeded) {
                bodi[x] = "move";
            }
            else{
                bodi[x] = this.body.get(z);
                z++;
            }
        }
        return bodi;
    }

    public String[] getBodyNoMove() {
        return this.body.toArray(new String[this.body.size()]);
    }

    public static int getPartCost(String part){
        switch(part){
            case "carry":
            case "move":
                return 50;
            case "claim":
                return 600;
            case "tough":
                return 10;
            case "attack":
                return 80;
            case "work":
                return 100;
            case "ranged_attack":
                return 150;
            case "heal":
                return 250;
        }
        return 99999;
    }


}
