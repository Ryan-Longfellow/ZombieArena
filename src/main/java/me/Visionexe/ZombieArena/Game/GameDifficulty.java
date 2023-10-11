package me.Visionexe.ZombieArena.Game;

import me.Visionexe.ZombieArena.ZombieArena;

public enum GameDifficulty {
    EASY,
    NORMAL,
    HARD,
    INSANE;

    @Override
    public String toString() {
        return switch (this) {
            case EASY -> "easy";
            case NORMAL -> "normal";
            case HARD -> "hard";
            case INSANE -> "insane";
        };
    }

    public int getMultiplier() {
        return switch (this) {
            case EASY -> ZombieArena.getInstance().getConfigFile().getInt("EASY");
            case NORMAL -> ZombieArena.getInstance().getConfigFile().getInt("NORMAL");
            case HARD -> ZombieArena.getInstance().getConfigFile().getInt("HARD");
            case INSANE -> ZombieArena.getInstance().getConfigFile().getInt("INSANE");
        };
    }
}
