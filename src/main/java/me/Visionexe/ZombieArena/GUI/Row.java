package me.Visionexe.ZombieArena.GUI;

/**
 * Represents a row of a GUI.
 */
public enum Row {
	ONE(0),
	TWO(1),
	THREE(2),
	FOUR(3),
	FIVE(4),
	SIX(5);
	
	private int row;
	
	Row(int row) {
		this.row = row;
	}
	
	public int get() {
		return this.row;
	}
	
	public static Row of(int row) {
		switch (row) {
			case 0:
				return ONE;
			case 1:
				return TWO;
			case 2:
				return THREE;
			case 3:
				return FOUR;
			case 4:
				return FIVE;
			default:
			case 5:
				return SIX;
		}
	}
}
