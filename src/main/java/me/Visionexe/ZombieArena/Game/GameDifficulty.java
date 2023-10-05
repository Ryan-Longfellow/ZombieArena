package me.Visionexe.ZombieArena.Game;

public enum GameDifficulty {
    EASY,
    MEDIUM,
    HARD,
    INSANE;

    @Override
    public String toString() {
        return switch (this) {
            case EASY -> "easy";
            case MEDIUM -> "medium";
            case HARD -> "hard";
            case INSANE -> "insane";
        };
    }
}
