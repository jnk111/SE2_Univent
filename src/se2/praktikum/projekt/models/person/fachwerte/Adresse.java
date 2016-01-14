package se2.praktikum.projekt.models.person.fachwerte;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Repräsentiert den Fachwert Adresse einer Person
 * @author Jan
 *
 */
public class Adresse {
	
	private String strasse;
	private int hausNr;
	private PLZ postleitzahl;
	private String stadt;
	
	
	/**
	 * Initialsiert eine neue Adresse mit übergebenen Parametern
	 * @param strasse	: Straße
	 * @param hausNr	: HausNr.
	 * @param plz		: PLZ
	 * @param stadt		: Stadt
	 * @param land		: Land
	 */
	private Adresse(String strasse, int hausNr, PLZ plz, String stadt)				{
		
		this.strasse = strasse;
		this.hausNr = hausNr;
		this.postleitzahl = plz;
		this.stadt = stadt;
	}
	
	
	/**
	 * Ruft eine neue Adresse ab mit den übergebenen Parametern
	 * @param strasse	: st
	 * @param strasse	: Straße
	 * @param hausNr	: HausNr.
	 * @param plz		: PLZ
	 * @param stadt		: Stadt
	 * @param land		: Land
	 * @return eine Adresse
	 */
	@JsonCreator
	public static Adresse getAdresse(@JsonProperty("strasse") String strasse, 
									 @JsonProperty("hausNr") int hausNr,
									 @JsonProperty("postleitzahl") PLZ plz, 
									 @JsonProperty("stadt") String stadt)				{
		
		return new Adresse(strasse, hausNr, plz, stadt);
	}


	/**
	 * Gibt die Straße einer Adresse zurück
	 * @return Straße
	 */
	public String getStrasse() {
		return strasse;
	}

	/**
	 * Setzt die Straße einer Adresse
	 * @param strasse
	 */
	public void setStrasse(String strasse) {
		this.strasse = strasse;
	}

	/**
	 * Gibt die HausNr einer Straße zurück
	 * @return hausNr
	 */
	public int getHausNr() {
		return hausNr;
	}


	/**
	 * Setzt die HausNr einer Straße
	 * @param hausNr
	 */
	public void setHausNr(int hausNr) {
		this.hausNr = hausNr;
	}

	/**
	 * Gibt die Postleitzahl einer Adresse zurück
	 * @return Postleitzahl
	 */
	public PLZ getPostleitzahl() {
		return postleitzahl;
	}

	/**
	 * Setzt die Postleitahl einer Adresse
	 * @param postleitzahl
	 */
	public void setPostleitzahl(PLZ postleitzahl) {
		this.postleitzahl = postleitzahl;
	}

	/**
	 * Gibt die Stadt einer Adressse zurück
	 * @return Stadt
	 */
	public String getStadt() {
		return stadt;
	}

	/**
	 * Setzt die Stadt einer Adresse
	 * @param stadt
	 */
	public void setStadt(String stadt) {
		this.stadt = stadt;
	}


	@Override
	public int hashCode() {
		
		return this.getStrasse().hashCode()
				+ this.getHausNr()
				+ this.getStadt().hashCode()
				+ this.getPostleitzahl().hashCode();
	}


	@Override
	public boolean equals(Object obj) {
		
		if(obj == null){
			return false;
		}
		
		if(obj == this){
			return true;
		}
		
		if(obj instanceof Adresse){
			
			Adresse a = (Adresse) obj;
			
			return a.getStrasse().equals(this.getStrasse())
					&& a.getHausNr() == this.getHausNr()
					 && a.getPostleitzahl().equals(this.getPostleitzahl())
					 && a.getStadt().equals(this.getStadt());
		}else{
			return false;
		}
	}
	
	
}
