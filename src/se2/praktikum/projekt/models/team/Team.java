package se2.praktikum.projekt.models.team;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import se2.praktikum.projekt.models.person.Student;
import se2.praktikum.projekt.models.team.fachwerte.TeamID;

/**
 * Repräsentiert ein Team
 * @author Jan
 *
 */
public class Team {
	
	// Felder
	private TeamID teamID;				// Die TeamID
	private int grpNr;					// Die zugehörige Gruppennummer
	private int vorgGrpNr;				// Vorgemrkte Gruppenummer
	private List<Student> mitglieder;	// Die Mitglieder des Teams
	private int minTeiln;				// min. Anzahl Teilnehmer
	private int maxTeiln;				// max. Anzahl Teilnehmer
	private String fachkuerzel;
	
	
	/**
	 * Default-Konstruktor
	 * Initialisert ein neues Teams
	 * Felder können separat über Setter definiert werden
	 */
	public Team(){
		
		this(null, 0, 0, null, 0, 0, null);
	}
	
	
	
	/**
	 * Initialisiert ein neues Team mit übergebenen Parametern
	 * @param teamID : Die TeamID
	 * @param minTeiln : minimale Anzahl Mitglieder
	 * @param maxTeiln : maximale Anzahl Mitglieder
	 */
	@JsonCreator
	public Team(@JsonProperty("teamID") TeamID teamId,
				@JsonProperty("minTeiln") int minTeiln, 
				@JsonProperty("maxTeiln")int maxTeiln,
				@JsonProperty("mitglieder") List<Student> mitglieder,
				@JsonProperty("grpNr") int grpNr,
				@JsonProperty("vorgGrpNr") int vorgGrpNr,
				@JsonProperty("fachkuerzel") String fachkuerzel){
		
		
		this.minTeiln = minTeiln;
		this.maxTeiln = maxTeiln;
		this.mitglieder = mitglieder;
		this.teamID = teamId;
		this.fachkuerzel = fachkuerzel;
		this.grpNr = grpNr;
		this.vorgGrpNr = vorgGrpNr;
	}

	/**
	 * Gibt die TeamID zurück
	 * @return die TeamID
	 */
	public TeamID getTeamID() {
		return teamID;
	}
	
	/**
	 * Gibt eine Liste aller Mitglieder zurück
	 * @return Liste aller Mitglieder
	 */
	public List<Student> getMitglieder() {
		return mitglieder;
	}

	/**
	 * Setzt die Mitglieder eines Teams
	 * @param Liste Mitglieder
	 */
	public void setMitglieder(List<Student> mitglieder) {
		this.mitglieder = mitglieder;
	}

	/**
	 * Gibt die minimale Anzahl Mitglieder zurück
	 * @return min. Anzahl Mitglieder
	 */
	public int getMinTeiln() {
		return minTeiln;
	}

	/**
	 * Setzt die minimale anzahl Mitglieder
	 * @param minTeiln
	 */
	public void setMinTeiln(int minTeiln) {
		this.minTeiln = minTeiln;
	}

	/**
	 * Gibt die max. Anzahl Mitglieder zurück
	 * @return max. Anzahl Mitglieder
	 */
	public int getMaxTeiln() {
		return maxTeiln;
	}

	/**
	 * Setzt max. Anzahl Mitglieder
	 * @param max. Anzahl Mitglieder
	 */
	public void setMaxTeiln(int maxTeiln) {
		this.maxTeiln = maxTeiln;
	}

	public int getGrpNr() {
		return grpNr;
	}

	public void setGrpNr(int grpNr) {
		this.grpNr = grpNr;
	}



	public void setTeamID(TeamID teamID) {
		this.teamID = teamID;
	}



	@Override
	public int hashCode() {
		
		return this.getTeamID().hashCode();
	}



	@Override
	public boolean equals(Object obj) {
		
		if(obj == null){
			return false;
		}
		
		if(obj == this){
			return true;
		}
		
		if(obj instanceof Team){
			
			Team t = (Team) obj;
			
			return this.getTeamID().equals(t.getTeamID())
					&& this.getMitglieder().containsAll(t.getMitglieder())
					&& this.getMitglieder().size() == t.getMitglieder().size()
					&& this.getFachkuerzel().equals(t.getFachkuerzel())
					&& this.getGrpNr() == t.getGrpNr();
		}else{
			return false;
		}
	}



	@Override
	public String toString() {
		
		return this.teamID.getId() + ", " + this.vorgGrpNr + ", " + this.grpNr;
	}



	public void setFachkuerzel(String fachkuerzel) {
		this.fachkuerzel = fachkuerzel;
		
	}



	public String getFachkuerzel() {
		return fachkuerzel;
	}



	public int getVorgGrpNr() {
		return vorgGrpNr;
	}



	public void setVorgGrpNr(int vorgGrpNr) {
		this.vorgGrpNr = vorgGrpNr;
	}
	
	
	
	
	
	
	
	
}