package me.Visionexe.ZombieArena.Utils;

public class ValueFormat {
    public static String format(long value) {
        // Trillions
        if (value >= 1000000000000.0) {
            return (((Math.round(value / 100000000.0)) / 10000.0) + "T");
        } else if (value >= 1000000000.0) { // Billions
            return (((Math.round(value / 1000000.0)) / 1000.0) + "B");
        } else if (value >= 1000000.0) { // Millions
            return (((Math.round(value / 10000.0)) / 100.0) + "M");
        } else if (value >= 1000) { // Thousands
            return (((Math.round(value / 100.0)) / 10.0) + "K");
        }
        return String.valueOf(value);
    }
}
