package se2.praktikum.projekt.models.person.fachwerte;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;


public class MAID extends UserID{
	
	// Felder
	private int id; // Der Wert der Mitarbeiter-ID
	
	
	/**
	 * Initialisiert eine Mitarbeiter-ID
	 * @param maID : Die Mitarbeiter-ID
	 */
	@JsonCreator
	public MAID(@JsonProperty("id") int maID){
		
		
		this.id = maID;
	}
	
	
	/**
	 * Referenziert eine neue Mitarbeiter ID
	 */
	public static MAID getMAID( int id){
		
		// TODO: Preconditions
		return new MAID(id);
	}

	/**
	 * Gibt den Wert einer Mitarbeiter-ID zurück
	 * @return MAID-Wert
	 */
	public int getId() {
		return id;
	}

	/**
	 * Setzt den Wert einer MAID
	 * @param maID : Wert der MAID
	 */
	public void setId(int id) {
		this.id = id;
	}


	@Override
	public int hashCode() {
		
		return this.getId()*22;
	}


	@Override
	public boolean equals(Object obj) {

		if(obj == null){
			return false;
		}
		
		if(obj == this){
			return true;
		}
		
		if(obj instanceof MAID){
			
			MAID id = (MAID) obj;
			
			return this.getId() == id.getId();
		}else{
			return false;
		}
	}

	




	
	


}
