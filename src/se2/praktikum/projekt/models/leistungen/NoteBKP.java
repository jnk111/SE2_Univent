package se2.praktikum.projekt.models.leistungen;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Repräsentiert eine vergebene Note
 * @author Jan
 *
 */
public class NoteBKP {
	
	// Konstanten
	private final int MAX_NOTE = 15;
	private final int MIN_NOTE = -1;
	private final String ERROR = "Ungültige Note, muss im Bereich "+this.MIN_NOTE+" und "+this.MAX_NOTE+" liegen!";
	
	// Felder
	private int note; 	// Die vergebene Note
	
	/**
	 * Default-Konstruktor
	 * Initialisert eine Note mit dem Status "nicht bewertet"
	 * Felder könnten separat über Setter gesetzt werden
	 */
	public NoteBKP(){
		
		this(-1);
	}
	
	/**
	 * Initialsiert eine Note mit den gegebenen Paramtern
	 * @param note : Die Note
	 */
	@JsonCreator
	public NoteBKP(@JsonProperty("note") int note){
		
		if(istGueltigeNote(note)){		// -1 = nicht bewertet
			this.setNote(note);
		}else{
			throw new IllegalArgumentException(this.ERROR);
		}
	}

	/**
	 * Prüft ob die vergebene Note um Gültigkeitsbereich liegt
	 * @param note
	 * @return
	 */
	private boolean istGueltigeNote(int note) {
		
		return (note >= this.MIN_NOTE && note <= this.MAX_NOTE);
	}

	/**
	 * Gibt die vergebene Note zurück
	 * @return Die note
	 */
	public int getNote() {
		return note;
	}

	/**
	 * Setzt die zu vergebene Note
	 * @param note
	 */
	public void setNote(int note) {
		
		if(istGueltigeNote(note)){		// -1 = nicht bewertet
			this.note = note;
		}else{
			throw new IllegalArgumentException(this.ERROR);
		}
	}

	/**
	 * Gibt den möglichen Minimumwert einer Note zurück
	 * @return Minimum Note
	 */
	public int getMIN_NOTE() {
		return MIN_NOTE;
	}

	/**
	 * Gibt den möglichen Maxiumwert einer Note zurück
	 * @return Maximum Note
	 */
	public int getMAX_NOTE() {
		return MAX_NOTE;
	}

	/**
	 * Gibt die derzeitige Fehlermeldung bei ungültiger Note zurück
	 * @return Fehlermeldung
	 */
	public String getERROR() {
		return ERROR;
	}

	@Override
	public int hashCode() {

		return this.getNote() * 12;
	}

	@Override
	public boolean equals(Object obj) {

		if(obj == null){
			return false;
		}
		
		if(obj instanceof NoteBKP){
			
			NoteBKP n = (NoteBKP) obj;
			return n.getNote() == this.getNote();
			
		}else{
			return false;
		}
	}
	
	
	
}
