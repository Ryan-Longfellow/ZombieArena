package me.Visionexe.ZombieArena.GUI;

/**
 * Represents a column of a GUI.
 */
public enum Column {
	ONE(0),
	TWO(1),
	THREE(2),
	FOUR(3),
	FIVE(4),
	SIX(5),
	SEVEN(6),
	EIGHT(7),
	NINE(8);
	
	private int column;
	
	Column(int column) {
		this.column = column;
	}
	
	public int get() {
		return this.column;
	}
	
	public static Column of(int column) {
		switch (column) {
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
			case 5:
				return SIX;
			case 6:
				return SEVEN;
			case 7:
				return EIGHT;
			default:
			case 8:
				return NINE;
		}
	}
}