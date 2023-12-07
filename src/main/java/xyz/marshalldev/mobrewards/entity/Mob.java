package xyz.marshalldev.mobrewards.entity;

import java.util.List;

public class Mob {
    private final List<String> commands;
    private final List<String> mobNames;
    private final List<String> worlds;
    private final List<String> regions;
    private double chance;
    private String message;

    public Mob(List<String> commands, List<String> mobNames, List<String> worlds, List<String> regions, double chance, String message) {
        this.commands = commands;
        this.mobNames = mobNames;
        this.worlds = worlds;
        this.regions = regions;
        this.chance = chance;
        this.message = message;
    }

    public List<String> getCommands() {
        return commands;
    }

    public List<String> getMobNames() {
        return mobNames;
    }

    public List<String> getWorlds() {
        return worlds;
    }

    public List<String> getRegions() {
        return regions;
    }

    public double getChance() {
        return chance;
    }

    public void setChance(double chance) {
        this.chance = chance;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}