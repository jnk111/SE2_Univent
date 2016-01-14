package se2.praktikum.projekt.datenimexport.backuptables;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class DBFachbereich {
	
	
	private String fbKuerzel;
	private String fbBezeichnung;
	private String dKuerzel;
	
	@JsonCreator
	public DBFachbereich(@JsonProperty("fbKuerzel") String fbKuerzel,
						 @JsonProperty("fbBezeichnung") String fbBezeichnung,
						 @JsonProperty("dKuerzel") String dKuerzel){
		
		
		this.fbKuerzel = fbKuerzel;
		this.fbBezeichnung = fbBezeichnung;
		this.dKuerzel = dKuerzel;
		
	}

	@Override
	public int hashCode() {
		
		return this.dKuerzel.hashCode() + this.fbBezeichnung.hashCode() + this.fbKuerzel.hashCode();
	}

	@Override
	public boolean equals(Object obj) {
		
		if(obj == null){
			return false;
		}
		
		if(obj == this){
			return true;
		}
		
		if(obj instanceof DBFachbereich){
			
			DBFachbereich dbf = (DBFachbereich) obj;
			
			return this.dKuerzel.equals(dbf.getdKuerzel())
					&& this.fbBezeichnung.equals(dbf.getFbBezeichnung())
					&& this.fbKuerzel.equals(dbf.getFbKuerzel());
		}
		
		return false;
	}

	@Override
	public String toString() {
		
		return this.dKuerzel + ", " + this.fbKuerzel + ", " + this.fbBezeichnung;
	}

	public String getFbKuerzel() {
		return fbKuerzel;
	}

	public void setFbKuerzel(String fbKuerzel) {
		this.fbKuerzel = fbKuerzel;
	}

	public String getFbBezeichnung() {
		return fbBezeichnung;
	}

	public void setFbBezeichnung(String fbBezeichnung) {
		this.fbBezeichnung = fbBezeichnung;
	}

	public String getdKuerzel() {
		return dKuerzel;
	}

	public void setdKuerzel(String dKuerzel) {
		this.dKuerzel = dKuerzel;
	}
	
	
	
	
	

}
