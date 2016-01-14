package se2.praktikum.projekt.models.gruppe;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import se2.praktikum.projekt.models.person.Assistent;
import se2.praktikum.projekt.models.person.Professor;

/**
 * Repräsentiert eine Gruppe für eine gegebene Veranstaltung
 * @author Jan
 *
 */
public class Gruppe {
	
	// Felder
	private String fachkuerzel;
	private List<Termin> termine;		// Die Abgabetermine
	private int grpNr;					// Die Gruppennummer
	private Professor professor;	// Der betreuende Professor
	private Assistent assistent;	// Der betreuende Assistent
	private int maxTeams;				// Maximal Anzahl Teams in der Gruppe
	private int anzTeams;				// aktuelle Anzahl Teams
	
	
	/**
	 * Default-Konstruktor
	 * Initialisiert ein neue Gruppe ohne Parameter
	 * Felder können seperat über Setter festgelegt werden
	 */
	public Gruppe(){
		
		this(null, 0, null, null);
		
	}
	
	public Gruppe(String fachkuerzel, int grpNr){
		
		this(fachkuerzel, grpNr, null, null);
		
	}
	
	
	/**
	 * Initialisiert eine neue Gruppe mit den wichtigsten Parametern
	 * @param grpNr : Gruppenummer
	 * @param prof : Der Professor
	 * @param assist : Der Assistent
	 */
	public Gruppe(String fachkuerzel, int grpNr, Professor prof, 
				Assistent assist) {
		
		this(fachkuerzel, grpNr, null, prof, assist, 0, 0);

	}
	/**
	 * Initialisiert eine neue Gruppe mit den übergebenen Parametern
	 * @param grpNr : Gruppennummer
	 * @param prof : Der Professor
	 * @param assist : der Assistent
	 * @param raum : Der Raum
	 * @param maxTeams : max. Teams
	 * @param resTeams : reserve Teams
	 * @param minTeams : min. Teams
	 * @param anzTeams : aktuelle Anzahl Teams
	 */
	@JsonCreator
	public Gruppe(@JsonProperty("fachkuerzel") String fachkuerzel, 
				  @JsonProperty("grpNr") int grpNr, 
				  @JsonProperty("termine") List<Termin> termine,
				  @JsonProperty("dozent") Professor prof, 
				  @JsonProperty("assistent") Assistent assist,
				  @JsonProperty("maxTeams") int maxTeams, 
				  @JsonProperty("anzTeams") int anzTeams)				{
		
		this.fachkuerzel = fachkuerzel;
		this.grpNr = grpNr;
		this.professor = prof;
		this.assistent = assist;
		this.maxTeams = maxTeams;
		this.anzTeams = anzTeams;
		this.termine = termine;
	}


	/**
	 * Gibt alle Termnine zurück
	 * @return alle Termine
	 */
	public List<Termin> getTermine() {
		return termine;
	}

	/**
	 * Setzt neue Termine
	 * @param termine
	 */
	public void setTermine(List<Termin> termine) {
		this.termine = termine;
	}


	/**
	 * Gibt die Gruppennummer zurück
	 * @return Gruppennummer
	 */
	public int getGrpNr() {
		return grpNr;
	}

	
	/**
	 * Setzt die Gruppennummer dieser Gruppe
	 * @param grpNr : Gruppennummer
	 */
	public void setGrpNr(int grpNr) {
		this.grpNr = grpNr;
	}

	/**
	 * Gibt den betreuenden Professor zurück
	 * @return betreuender Professor
	 */
	public Professor getProfessor() {
		return professor;
	}

	/**
	 * Setzt den betreuenden Professor
	 * @param professor
	 */
	public void setProfessor(Professor professor) {
		this.professor = professor;
	}

	/**
	 * Gibt den betreuenden Assistenten zurück
	 * @return Assistent
	 */
	public Assistent getAssistent() {
		return assistent;
	}

	/**
	 * Setzt den betreuenden Assistenten
	 * @param assistent
	 */
	public void setAssistent(Assistent assistent) {
		this.assistent = assistent;
	}


	/**
	 * Gibt die max. mögliche Anzahl Teams zurück
	 * @return max. Anzahl Teams
	 */
	public int getMaxTeams() {
		return maxTeams;
	}

	
	/**
	 * Setzt die max. Anzahl der Teams dieser Gruppe
	 * @param max. anzahl Teams
	 */
	public void setMaxTeams(int maxTeams) {
		this.maxTeams = maxTeams;
	}

	
	/**
	 * Gibt die aktuelle Anzahl Teams zurück (inkl. belegte Reserve)
	 * @return aktuelle Anzahl Teams
	 */
	public int getAnzTeams() {
		return anzTeams;
	}

	/**
	 * Setzt aktuelle Anzahl Teams
	 * @param anzTeams
	 */
	public void setAnzTeams(int anzTeams) {
		this.anzTeams = anzTeams;
	}

	/**
	 * Gibt das zugehörige Fachkürzel einer Gruppe zurück
	 * @return fachkuerzel
	 */
	public String getFachkuerzel() {
		return fachkuerzel;
	}

	/**
	 * Setzt das zugehörige Fachkürzel
	 * @param fachkuerzel
	 */
	public void setFachkuerzel(String fachkuerzel) {
		this.fachkuerzel = fachkuerzel;
	}


	@Override
	public boolean equals(Object obj) {

		if(obj == null){
			return false;
		}
		
		
		if(obj instanceof Gruppe){
			
			Gruppe gr = (Gruppe) obj;
			
			return this.fachkuerzel.equals(gr.getFachkuerzel())
					&& this.grpNr == gr.getGrpNr();
			
		}else{
			return false;
		}
	}

	@Override
	public int hashCode() {
		
		return this.fachkuerzel.hashCode() * this.grpNr;
	}

	@Override
	public String toString() {
		
		return this.getFachkuerzel() 
				+ ", GrNr: " 
				+ this.grpNr 
				+ ", Prof: " 
				+ this.getProfessor().toString() 
				+ ", Assist: " 
				+ this.getAssistent().toString() 
				+ ", AnzTeams: " + this.getAnzTeams() 
				+ ", MaxTeams: " + this.getMaxTeams() 
				+ ", Termine: " + this.getTermine().toString();
	}
	
	
	
	
	
	
	
}