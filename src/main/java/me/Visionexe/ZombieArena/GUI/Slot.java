package me.Visionexe.ZombieArena.GUI;

/**
 * Represents a slot in a GUI.
 */
public class Slot {
	
	private Column column;
	private Row row;
	
	private Slot(Column column, Row row) {
		this.column = column;
		this.row = row;
	}
	
	public static Slot of (Column column, Row row) {
		return new Slot(column, row);
	}
	
	public Column getColumn() {
		return column;
	}
	
	public Row getRow() {
		return row;
	}
	
	/**
	 * Checks whether two objects are equal based on their data.
	 */
	@Override
	public boolean equals(Object obj) {
		if (obj == null)
			return false;
		
		if (!(obj instanceof Slot))
			return false;
		
		if (obj == this)
			return true;
		
		Slot other = (Slot) obj;
		
		return (this.row.equals(other.row) && this.column.equals(other.column));
	}
	
	/**
	 * Returns a unique hashcode calculated based on the object'Message data values.
	 */
	@Override
	public int hashCode() {
		int result = 17;
		
		result = 37 * result + ((column == null) ? 0 : column.hashCode());
		result = 37 * result + ((row == null)? 0 : row.hashCode());
		
		return result;
	}
	
}
