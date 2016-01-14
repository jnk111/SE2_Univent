package se2.praktikum.projekt.models.person.fachwerte;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * repräsentiert den Fachwert Matrikelnr
 * @author Jan
 *
 */
public class MatrikelNr extends UserID{
	
	// Felder
	private int matrNr; // Der Wert der Matrikelnummer
	
	
	/**
	 * Initialisiert eine Matrikelnummer
	 * @param matrNr : Die Matrikelnummer
	 */
	private MatrikelNr(int matrNr){
		
		this.matrNr = matrNr;
	}
	
	/**
	 * Referenziert eine Matrikelnumer
	 * @param matrNr : Die Matrikelnummer 
	 * @return MatrikelNummer
	 */
	@JsonCreator
	public static MatrikelNr getMatrikelNr(@JsonProperty("matrNr") int matrNr){
		
		//TODO: Preconditions
		return new MatrikelNr(matrNr);
	}

	/**
	 * Gibt den Integerwert einer Matrikelnummer zurück
	 * @return Wert der Matrikelnummer
	 */
	public int getMatrNr() {
		return matrNr;
	}

	/**
	 * Setzt den Wert einer Matrikelnummer
	 * @param matrNr : Wert der Matrikelnummer
	 */
	public void setMatrNr(int matrNr) {
		
		// TODO: Preconditions
		this.matrNr = matrNr;
	}

	@Override
	public int hashCode() {

		return this.getMatrNr()*21;
	}

	@Override
	public boolean equals(Object obj) {
		
		if(obj == null){
			return false;
		}
		
		if(obj == this){
			return true;
		}
		
		if(obj instanceof MatrikelNr){
			
			MatrikelNr id = (MatrikelNr) obj;
			
			return this.getMatrNr() == id.getMatrNr();
			
		}else{
			return false;
		}
	}
	
	
	
	
	
	

}
