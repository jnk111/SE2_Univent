package se2.praktikum.projekt.datenimexport.backuptables;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import se2.praktikum.projekt.models.person.fachwerte.MatrikelNr;

public class DBEinzelanmeldung {
	
	private MatrikelNr matrNr;
	private int grpNr;
	private String fachkuerzel;
	
	@JsonCreator
	public DBEinzelanmeldung(@JsonProperty("matrNr") MatrikelNr matrNr,
						     @JsonProperty("grpNr") int grpNr,
						     @JsonProperty("fachkuerzel") String fachkuerzel){
		
		
		this.matrNr = matrNr;
		this.grpNr = grpNr;
		this.fachkuerzel = fachkuerzel;
	}

	@Override
	public int hashCode() {
		
		return this.matrNr.hashCode() + this.fachkuerzel.hashCode() + this.grpNr * 22;
	}

	@Override
	public String toString() {
		
		return this.getMatrNr().getMatrNr() + " -> " + this.fachkuerzel + ", " + this.grpNr;
	}

	@Override
	public boolean equals(Object obj) {
		
		if(obj == null){
			return false;
		}
		
		if(obj == this){
			return true;
		}
		
		if(obj instanceof DBEinzelanmeldung){
			
			DBEinzelanmeldung e = (DBEinzelanmeldung) obj;
			
			return this.matrNr.equals(e.getMatrNr())
					&& this.fachkuerzel.equals(e.getFachkuerzel())
					&& this.grpNr == e.getGrpNr();
		}
		
		return false;
	}

	public MatrikelNr getMatrNr() {
		return matrNr;
	}

	public void setMatrNr(MatrikelNr matrNr) {
		this.matrNr = matrNr;
	}

	public int getGrpNr() {
		return grpNr;
	}

	public void setGrpNr(int grpNr) {
		this.grpNr = grpNr;
	}

	public String getFachkuerzel() {
		return fachkuerzel;
	}

	public void setFachkuerzel(String fachkuerzel) {
		this.fachkuerzel = fachkuerzel;
	}
	
	
	
	
	
	
	
	

}
