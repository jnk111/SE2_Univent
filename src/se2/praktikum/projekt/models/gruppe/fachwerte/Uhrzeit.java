package se2.praktikum.projekt.models.gruppe.fachwerte;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Repräsentiert den Fachwert Uhrzeit
 * @author Jan
 *
 */
public class Uhrzeit {
	
	// Felder
	private int stunden;	// Stunden
	private int minuten;	// Minuten
	
	/**
	 * Initialisiert eine neue Uhrzeit mit den übergebenen Parametern
	 * @param stunden : Die Stunden
	 * @param minuten : Die Minuten
	 */
	@JsonCreator
	public Uhrzeit(@JsonProperty("stunden") int stunden, 
					@JsonProperty("minuten") int minuten){
		
		this.stunden = stunden;
		this.minuten = minuten;
	}
	
	/**
	 * Referenziert eine Uhrzeit zu den übergebenen Parametern
	 * @param stunden : Die Stunden
	 * @param minuten : Die Minuten
	 * @return
	 */
	public static Uhrzeit getUhrzeit(int stunden, int minuten){
		
		if(istGueltigeUhrzeit(stunden, minuten)){
			return new Uhrzeit(stunden, minuten);
		}
		throw new IllegalArgumentException("Ungültige Uhrzeit: "+stunden +":"+minuten);
	}

	/**
	 * Prüft ob gültige Stunden und Minuten übergeben wurden
	 * @param stunden : Die Stunden
	 * @param minuten : Die Minuten
	 * @return
	 */
	private static boolean istGueltigeUhrzeit(int stunden, int minuten) {
		
		return istGueltigeStunde(stunden) && istGueltigeMinute(minuten);
	}

	/**
	 * Prüft ob die übergebenen Minuten gültig sind
	 * @param minuten : Die Minuten
	 * @return boolean
	 */
	private static boolean istGueltigeMinute(int minuten) {
		
		return minuten > -1 && minuten < 60;
	}
	
	public static Uhrzeit getUhrzeit(String uhrzeit){
		
		String [] uz = uhrzeit.split(":");
		int stunde = Integer.parseInt(uz[0]);
		int minute = Integer.parseInt(uz[1]);
		
		return getUhrzeit(stunde, minute);
		
	}

	/**
	 * Prüft ob die übergebenen Stunden gültig sind
	 * @param stunden : Die Stunden
	 * @return boolean
	 */
	private static boolean istGueltigeStunde(int stunden) {
		
		return stunden > -1 && stunden < 24;
	}

	/**
	 * Gibt den Wert der Stunden zurück
	 * @return Stunden
	 */
	public int getStunden() {
		return stunden;
	}

	/**
	 * Setzt den Wert der Stunden
	 * @param stunden
	 */
	public void setStunden(int stunden) {
		
		if(istGueltigeStunde(stunden)){
			this.stunden = stunden;
		}else{
			throw new IllegalArgumentException("Ungültige Stunden!");
		}
	}

	/**
	 * Gibt den Wert der Minuten zurück
	 * @return Den Wert der Minuten
	 */
	public int getMinuten() {
		return minuten;
	}

	/**
	 * Setzt den Wert der Minuten
	 * @param minuten
	 */
	public void setMinuten(int minuten) {
		
		if(istGueltigeMinute(minuten)){
			this.minuten = minuten;
		}else{
			throw new IllegalArgumentException("Ungültige Minuten!");
		}
	}

	@Override
	public int hashCode() {
		
		return (this.getStunden() + this.getMinuten()) * 12;
	}

	@Override
	public boolean equals(Object obj) {
		
		if(obj == null){
			return false;
		}
		
		if(obj instanceof Uhrzeit){
			
			Uhrzeit u = (Uhrzeit) obj;
			// Hier später noch Datum-Vergleich mit Comparator
			return this.getStunden() == u.getStunden()
					 && this.getMinuten() == u.getMinuten();
			
		}else{
			return false;
		}
	}

	@Override
	public String toString() {
		
		String stunden = null;
		String minuten = null;
		
		if(this.stunden < 10){
			stunden = "0" + this.stunden;
		}else{
			stunden = "" + this.stunden;
		}
		
		if(this.minuten < 10){
			minuten = "0" + this.minuten;
		}else{
			minuten = ""+this.minuten;
		}
		
		return stunden + ":" + minuten;
	}
	
	
	
	
	
}