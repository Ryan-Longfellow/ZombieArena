package me.Visionexe.ZombieArena.Utils;

public class ValueFormat {
    public static String format(long value) {
        // Trillions
        if (value >= 1000000000000.0) {
            return (((Math.round(value / 100000000.0)) / 10000.0) + "T").replace(".0", "");
        } else if (value >= 1000000000.0) { // Billions
            return (((Math.round(value / 1000000.0)) / 1000.0) + "B").replace(".0", "");
        } else if (value >= 1000000.0) { // Millions
            return (((Math.round(value / 10000.0)) / 100.0) + "M").replace(".0", "");
        } else if (value >= 1000) { // Thousands
            return (((Math.round(value / 100.0)) / 10.0) + "K").replace(".0", "");
        }
        return String.valueOf(value);
    }
}
