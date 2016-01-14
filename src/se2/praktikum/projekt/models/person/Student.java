package se2.praktikum.projekt.models.person;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import se2.praktikum.projekt.models.person.fachwerte.Adresse;
import se2.praktikum.projekt.models.person.fachwerte.MatrikelNr;

/**
 * Repräsentiert einen Studenten
 * @author Jan
 *
 */

public class Student extends Person{
	
	// Felder
	private MatrikelNr matrNr;		// Die Matrikelnummer -> VORERST INT damit DBBeispiel weiterhin
							//					     funktioniert
							//					  -> Später fachlicher Datentyp "MatrikelNr"
	private String po;		// Die Prüfungsordnung
	private String fachbereich;
	private boolean bestaetigt;
	private boolean einzel;
	
	/**
	 * Default-Konstruktor
	 * Felder können separat über Setter initialsiert werden
	 */
	public Student(){
		
		this(null, null, null);
	}
	
	/**
	 * Initialisiert einen Studenten mit den wichtigsten Parametern
	 * Restliche Felder können über Setter gesetzt werden
	 * @param matrNr : Die MatrikelNr.
	 * @param vorname : Der Vorname
	 * @param nachname : Der Nachname
	 */
	public Student(MatrikelNr matrNr, 
				   String vorname,
				   String nachname)				 {
		
		this(matrNr, vorname, nachname, 
			 null, null, null, null,null, null, 
			 null, null);
	}
	
	
	/**
	 * Initialisiert einen Studenten mit den übergebenen Parametern
	 * @param matrNr : MatrikelNr
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
	@JsonCreator
	public Student(@JsonProperty("matrNr") MatrikelNr matrNr, 
				   @JsonProperty("vorname") String vorname,
				   @JsonProperty("nachname") String nachname, 
				   @JsonProperty("benutzername")String benutzername,
				   @JsonProperty("passwort") String passwort,
				   @JsonProperty("gebDatum") Date gebDatum, 
				   @JsonProperty("gebOrt") String gebOrt,
				   @JsonProperty("adresse") Adresse adresse, 
				   @JsonProperty("department") String department,
				   @JsonProperty("fachbereich") String fachbereich, 
				   @JsonProperty("email") EMail email)		{
		
		this.matrNr = matrNr;
		this.vorname = vorname;
		this.nachname = nachname;
		this.benutzername = benutzername;
		this.passwort = passwort;
		this.gebDatum = gebDatum;
		this.gebOrt = gebOrt;
		this.adresse = adresse;
		this.department = department;
		this.fachbereich = fachbereich;
		this.email = email;
		
	}
	
	/**
	 * Gibt die Matrikelnummer eines Studenten zurück
	 * @return Matrikelnummer
	 */
	public MatrikelNr getMatrNr() {
		return matrNr;
	}
	
	/**
	 * Setzt die Matrikelnummer eines Studenten
	 * @param matrikelNr : Die Matrikelnummer
	 */
	public void setMatrNr(MatrikelNr matrikelNr) {
		this.matrNr = matrikelNr;
	}

	/**
	 * holt die Prüfungsordnung des Studenten
	 * @return
	 */
	public String getPo() {
		return po;
	}

	/**
	 * Setzt die Prüfungsordnung des Studenten
	 * @param po
	 */
	public void setPo(String po) {
		this.po = po;
	}

	
	@Override
	public int hashCode() {
		
		return super.hashCode() + this.getMatrNr().hashCode();
	}
	
	@Override
	public boolean equals(Object obj) {
		
		if(obj == null){
			
			return false;
		}
		
		
		if(obj instanceof Student){
			
			Student s = (Student) obj;
			
			return this.getMatrNr().equals(s.getMatrNr())
					&& this.getVorname().equals(s.getVorname())
					&& this.getNachname().equals(s.getNachname());
		}else{
			return false;
		}
	}

	@Override
	public String toString() {
		
		return this.matrNr.getMatrNr() + ", " + this.getVorname() + ", " + this.getNachname();
	}

	public String getFachbereich() {
		return fachbereich;
	}

	public void setFachbereich(String fachbereich) {
		this.fachbereich = fachbereich;
	}

	public boolean isBestaetigt() {
		return bestaetigt;
	}

	public void setBestaetigt(boolean bestaetigt) {
		this.bestaetigt = bestaetigt;
	}

	public void setEinzel(boolean parseBoolean) {
		this.einzel = parseBoolean;
		
	}
	
	
	
	public boolean isEinzel(){
		
		return this.einzel;
	}

	
	
	
	
}