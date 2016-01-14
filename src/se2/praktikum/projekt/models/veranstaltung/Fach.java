package se2.praktikum.projekt.models.veranstaltung;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Repräsentiert ein Fach einer Veranstaltung
 * @author Jan
 *
 */
public class Fach {
	
	// Felder
	private String fachKuerzel;			// Das Fachkürzel
	private String fachBezeichnung;		// Die Fachbezeichnung (ausgeschrieben)
	private String fBKuerzel;			// Der Fachbereich
	private String fachbereich;		//
	private int semester;				// Das zugehörige Semester
	
	
	
	/**
	 * Default-Konstruktor
	 * Initialisiert ein neues Fach ohne Parameter
	 * Felder können separat über Setter gesetzt werden
	 */
	public Fach(){
		
		this(null, null, null, 0, null);
	}
	
	public Fach(String fkuerzel) {
		
		this(fkuerzel, null, null, 0, null);
	}
	
	public Fach(String fkuerzel, int semester) {
		
		this(fkuerzel, null, null, semester, null);
	}
	/**
	 * Erzeugt ein neues Fach für eine Veranstaltung
	 * @param fachKuerzel	: Das Fachkuerzel
	 * @param fachBezeichnung : Die fachBezeichnung
	 * @param fachbereich : Den Fachbereich
	 * @param semester : Das Semester
	 */
	@JsonCreator
	public Fach(@JsonProperty("fachKuerzel") String fachKuerzel, 
				@JsonProperty("fachBezeichnung") String fachBezeichnung, 
				@JsonProperty("fachbereich") String fachbereich, 
				@JsonProperty("semester") int semester,
				@JsonProperty("fBKuerzel") String fBKuerzel)			{
		
		this.fachKuerzel = fachKuerzel;
		this.fachBezeichnung = fachBezeichnung;
		this.fBKuerzel = fachbereich;
		this.semester = semester;
		this.fBKuerzel = fBKuerzel;
	}
	

	/**
	 * Gibt das Fachkuerzel eines Fachs zurück
	 * @return Fachkuerzel
	 */
	public String getFachKuerzel() {
		return fachKuerzel;
	}
	
	/**
	 * Setzt das Fachkuerzel eines Fachs
	 * @param fachKuerzel
	 */
	public void setFachKuerzel(String fachKuerzel) {
		this.fachKuerzel = fachKuerzel;
	}
	
	
	/**
	 * Gibt die Fachbezeichnung eines Fachs zurück
	 * @return Fachbezeichnung
	 */
	public String getFachBezeichnung() {
		return fachBezeichnung;
	}
	
	/**
	 * Setzt die Fachbezeichnung eines Fachs
	 * @param fachBezeichnung
	 */
	public void setFachBezeichnung(String fachBezeichnung) {
		this.fachBezeichnung = fachBezeichnung;
	}
	
	
	
	/**
	 * Gibt das zugehörige Semester eines Fachs zurück
	 * @return Semester
	 */
	public int getSemester() {
		return semester;
	}
	
	/**
	 * Setzt das zugehörige Semester eines Fachs
	 * @param semester
	 */
	public void setSemester(int semester) {
		this.semester = semester;
	}
	
	/**
	 * Gibt das Fachbereichkuerzel zurück
	 * @return
	 */
	public String getfBKuerzel() {
		return fBKuerzel;
	}
	
	/**
	 * Setzt das Fachbereichkuerzel
	 * @param fBKuerzel
	 */
	public void setfBKuerzel(String fBKuerzel) {
		this.fBKuerzel = fBKuerzel;
	}
	public String getFachbereich() {
		return fachbereich;
	}
	public void setFachbereich(String fachbereich) {
		this.fachbereich = fachbereich;
	}

	@Override
	public int hashCode() {
		
		return getFachKuerzel().hashCode() * getSemester();
	}

	@Override
	public boolean equals(Object obj) {

		if(obj == null){
			return false;
		}
		
		if(obj == this){
			return true;
		}
		
		if(obj instanceof Fach){
			
			Fach f = (Fach) obj;
			
			return f.getFachKuerzel().equals(this.getFachKuerzel());
		}else{
			
			return false;
		}
	}
	
	
	
	

}
