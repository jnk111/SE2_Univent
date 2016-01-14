package se2.praktikum.projekt.datenimexport.backuptables;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import se2.praktikum.projekt.models.person.fachwerte.MatrikelNr;
import se2.praktikum.projekt.models.team.fachwerte.TeamID;

public class DBTeammitglied {
	
	private TeamID teamID;
	private MatrikelNr matrNr;
	private String bestaetigt;
	private String einzel;
	
	@JsonCreator
	public DBTeammitglied(@JsonProperty("teamID") TeamID teamID,
						  @JsonProperty("matrNr") MatrikelNr matrNr,
						  @JsonProperty("bestaetigt") String bestaetigt, 
						  @JsonProperty("einzel") String einzel){
		
		this.teamID = teamID;
		this.matrNr = matrNr;
		this.bestaetigt = bestaetigt;
	}

	@Override
	public int hashCode() {
		
		return this.teamID.hashCode() + this.matrNr.hashCode() + this.bestaetigt.hashCode();
	}

	@Override
	public boolean equals(Object obj) {
		
		if(obj == null){
			return false;
		}
		
		if(obj == this){
			
			return true;
		}
		
		if(obj instanceof DBTeammitglied){
			
			DBTeammitglied tm = (DBTeammitglied) obj;
			
			return this.teamID.equals(tm.getTeamID())
					&& this.matrNr.equals(tm.getMatrNr())
					&& this.bestaetigt.equals(tm.getBestaetigt());
		}
		
		return false;
	}

	@Override
	public String toString() {
		
		return this.getTeamID().getId() + ", " + this.getMatrNr().getMatrNr() + ", " + this.getBestaetigt();
	}

	public TeamID getTeamID() {
		return teamID;
	}

	public void setTeamID(TeamID teamID) {
		this.teamID = teamID;
	}

	public MatrikelNr getMatrNr() {
		return matrNr;
	}

	public void setMatrNr(MatrikelNr matrNr) {
		this.matrNr = matrNr;
	}

	public String getBestaetigt() {
		return bestaetigt;
	}

	public void setBestaetigt(String bestaetigt) {
		this.bestaetigt = bestaetigt;
	}

	public String getEinzel() {
		return einzel;
	}

	public void setEinzel(String einzel) {
		this.einzel = einzel;
	}
	
	

}
