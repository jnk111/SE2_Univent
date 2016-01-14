package se2.praktikum.projekt.models.meldungen;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import se2.praktikum.projekt.models.person.Student;
import se2.praktikum.projekt.models.team.Team;

/**
 * Repräsentiert eine Meldung zur Teameinladung
 * @author jan
 *
 */
public class Teameinladung extends Teammeldung{
	
	private boolean bestaetigt;
	private Date bestaetDatum;
	
	
	@JsonCreator
	public Teameinladung(@JsonProperty("absender") Student absender, 
						 @JsonProperty("empfaenger") Student empfaenger,
						 @JsonProperty("versandDatum") Date versandDatum, 
						 @JsonProperty("team") Team team, 
						 @JsonProperty("bestaetigt") boolean bestaetigt, 
						 @JsonProperty("bestaetDatum") Date bestaetDatum,
						 @JsonProperty("typ") String typ){

		this.absender = absender;
		this.empfaenger = empfaenger;
		this.versandDatum = versandDatum;
		this.team = team;
		this.bestaetDatum = bestaetDatum;
		this.bestaetigt = bestaetigt;
		this.typ = typ;
	}
	
	
	public Teameinladung(){
		
		
	}


	public boolean isBestaetigt() {
		return bestaetigt;
	}

	public void setBestaetigt(boolean bestaetigt) {
		this.bestaetigt = bestaetigt;
	}

	public Date getBestaetDatum() {
		return bestaetDatum;
	}

	public void setBestaetDatum(Date bestaetDatum) {
		this.bestaetDatum = bestaetDatum;
	}


	@Override
	public int hashCode() {
		
		return super.hashCode() + this.getTyp().hashCode();
	}


	@Override
	public boolean equals(Object obj) {
		
		if(obj == null){
			return false;
		}
		
		
		if(obj instanceof Teameinladung){
			
			Teameinladung te = (Teameinladung) obj;
			
			return te.getAbsender().equals(this.getAbsender())
					&& te.getEmpfaenger().equals(this.getEmpfaenger())
					  && te.isBestaetigt() == this.isBestaetigt();
		}else{
			
			return false;
		}
	}
	
	 @Override
	 public String toString(){
		 
		 
		 return this.absender.getVorname() + ", " + this.absender.getNachname() + " -> " + this.empfaenger.getVorname()+ ", " + this.empfaenger.getNachname()
		 			+ " Team: " + this.team.getTeamID().getId() + ", " + this.bestaetigt;
	 }


}
