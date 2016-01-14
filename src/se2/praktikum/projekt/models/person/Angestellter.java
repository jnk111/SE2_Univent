package se2.praktikum.projekt.models.person;
import se2.praktikum.projekt.models.person.fachwerte.MAID;

/**
 * Repräsentiert einen Angestellten als Abstrakte Klasse
 * @author Jan
 *
 */
public abstract class Angestellter extends Person implements IAngestellter{
	
	// Felder
	protected MAID maID; // Die Mitarbeiter-ID
	

	/**
	 * Gibt die Mitarbeiter-ID eines Mitarbeiters zurück
	 * @return Mitarbeiter-ID
	 */
	@Override
	public MAID getMaID() {
		return maID;
	}

	/**
	 * Setzt die Mitarbeiter-ID
	 * @param mitarbeiterID
	 */
	@Override
	public void setMaID(MAID maID) {
		this.maID = maID;
	}

	@Override
	public int hashCode() {
		
		return super.hashCode() + this.getMaID().hashCode();
	}
	
	@Override
	public String toString(){
		
		return this.getMaID().getId() + ", "+this.getVorname() + ", " + this.getNachname();
	}

	@Override
	public abstract boolean equals(Object obj);
	
	
	
	

	
	

}
