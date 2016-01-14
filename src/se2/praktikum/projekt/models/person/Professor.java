package se2.praktikum.projekt.models.person;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import se2.praktikum.projekt.models.person.fachwerte.Adresse;
import se2.praktikum.projekt.models.person.fachwerte.MAID;

/**
 * Repräsentiert einen Professor
 * @author Jan
 *
 */
public class Professor extends Angestellter {
	

	private String titel;
	private int count;
	/**
	 * Default-Konstruktor
	 * Felder können separat über Setter initialsiert werden
	 */
	public Professor(){
		
		this(null, null, null);
	}
	
	/**
	 * Initialisiert einen Professor mit den wichtigsten Parametern
	 * Restliche Felder können über Setter gesetzt werden
	 * @param maID : Die Mitarbeiter-ID
	 * @param vorname : Der Vorname
	 * @param nachname : Der Nachname
	 */
	@JsonCreator
	public Professor(@JsonProperty("maID") MAID maID, 
					 @JsonProperty("vorname")String vorname,
					 @JsonProperty("nachname")String nachname)				 {
		
		this(maID, null, vorname, nachname, 
			 null, null, null, null,null, null, 
			 null);
	}
	
	
	/**
	 * Initialisiert einen Professor mit den übergebenen Parametern
	 * @param maID : Mitarbeiter-ID
	 * @param vorname : Vorname
	 * @param nachname : Nachname
	 * @param benutzername : Benutzername
	 * @param gebDatum : Geburtsdatum
	 * @param gebOrt : Geburtsort 
	 * @param adresse : Adresse
	 * @param department : Department
	 * @param fachbereich : Fachbereich 
	 * @param email : EMail-Adresse
	 */
	public Professor(MAID maID, String titel, String vorname,
				   String nachname, String benutzername, String passwort,
				   Date gebDatum, String gebOrt,
				   Adresse adresse, String department,
				   EMail email)		{
		
		this.maID = maID;
		this.setTitel(titel);
		this.vorname = vorname;
		this.nachname = nachname;
		this.benutzername = benutzername;
		this.gebDatum = gebDatum;
		this.gebOrt = gebOrt;
		this.adresse = adresse;
		this.department = department;
		this.email = email;
		this.passwort = passwort;
	}
	
	@Override
	public boolean equals(Object obj) {
		
		if(obj == null){
			
			return false;
		}
		
		
		if(obj instanceof Professor){
			
			Professor a = (Professor) obj;
			
			return this.getMaID().equals(a.getMaID())
					&& this.getVorname().equals(a.getVorname())
					&& this.getNachname().equals(a.getNachname());
		}else{
			return false;
		}
	}

	public String getTitel() {
		return titel;
	}

	public void setTitel(String titel) {
		this.titel = titel;
	}




}
