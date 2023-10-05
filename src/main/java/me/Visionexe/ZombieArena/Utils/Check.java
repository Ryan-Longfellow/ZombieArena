package me.Visionexe.ZombieArena.Utils;

import me.Visionexe.ZombieArena.Game.GameDifficulty;

/*
This classes sole purpose is to check whether values are what they are supposed to be
Example: If an argument passed into a command is actually an Integer
 */
public class Check {
    public static boolean isInteger(String string) {
        if (string == null) {
            return false;
        }
        try {
            Integer.parseInt(string);
        } catch (NumberFormatException exception){
            return false;
        }
        return true;
    }

    public static GameDifficulty getDifficulty(String difficulty) {
        switch (difficulty) {
            case "MEDIUM" -> {
                return GameDifficulty.MEDIUM;
            }
            case "HARD" -> {
                return GameDifficulty.HARD;
            }
            case "INSANE" -> {
                return GameDifficulty.INSANE;
            }
            default -> {
                return GameDifficulty.EASY;
            }
        }
    }
}
