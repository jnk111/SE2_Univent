package se2.praktikum.projekt.models.meldungen;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonProperty;

import se2.praktikum.projekt.models.person.IPerson;
import se2.praktikum.projekt.models.team.Team;

/**
 * Repräsentiert eine Meldung über abgelehnte Teameinladungen
 * @author jan
 *
 */
public class TEAblehnung extends Teammeldung{
	
	
	public TEAblehnung(@JsonProperty("absender") IPerson absender, 
					   @JsonProperty("empfaenger") IPerson empfaenger,
					   @JsonProperty("versandDatum") Date versandDatum,
					   @JsonProperty("team") Team team,
					   @JsonProperty("typ") String typ){
		
		this.absender = absender;
		this.empfaenger = empfaenger;
		this.versandDatum = versandDatum;
		this.team = team;
		this.typ = typ;
	}
	
	
	public TEAblehnung(){
		
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
		
		if(obj instanceof TEAblehnung){
			
			TEAblehnung te = (TEAblehnung) obj;
			
			return te.getAbsender().equals(this.getAbsender())
					&& te.getEmpfaenger().equals(this.getEmpfaenger())
					 && te.getTeam().equals(this.getTeam());
		}else{
			
			return false;
		}
	}
	

}
