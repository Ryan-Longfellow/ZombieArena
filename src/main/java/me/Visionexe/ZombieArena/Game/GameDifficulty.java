package me.Visionexe.ZombieArena.Game;

public enum GameDifficulty {
    EASY,
    NORMAL,
    HARD,
    INSANE;

    @Override
    public String toString() {
        return switch (this) {
            case EASY -> "easy";
            case NORMAL -> "medium";
            case HARD -> "hard";
            case INSANE -> "insane";
        };
    }
}
