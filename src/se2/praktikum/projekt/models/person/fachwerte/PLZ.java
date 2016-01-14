package se2.praktikum.projekt.models.person.fachwerte;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Repräsentiert den Fachwert PLZ einer Adresse
 * @author Jan
 *
 */
public class PLZ {
	
	
	// Felder
	private String plz; // Die Postleitzahl
	
	
	/**
	 * Initialisiert eine Postleitzahl
	 * @param string : Die Postleitzahl
	 */
	private PLZ(String string){
		
		// TODO: Preconditions
		this.plz = string;
	}
	
	
	/**
	 * Referenziert einer Postleitzahl
	 * @param string : Postleitzahl
	 * @return eine Postleitzahl
	 */
	@JsonCreator
	public static PLZ getPostLeitzahl(@JsonProperty("plz") String string){
		
		return new PLZ(string);
	}


	/**
	 * Gibt den Integer-Wert einer PLZ zurück
	 * @return PLZ als int
	 */
	public String getPlz() {
		return plz;
	}

	/**
	 * Setzt den Wert einer PLZ
	 * @param plz : Wert der PLZ
	 */
	public void setPlz(String plz) {
		this.plz = plz;
	}


	@Override
	public int hashCode() {
		
		return this.getPlz().hashCode()*25;
	}


	@Override
	public boolean equals(Object obj) {
		
		if(obj == null){
			return false;
		}
		
		if(obj == this){
			return true;
		}
		
		if(obj instanceof PLZ){
			
			PLZ id = (PLZ) obj;
			
			return this.getPlz().equals(id.getPlz());
		}else{
			return false;
		}
	}
	
	
}
