package se2.praktikum.projekt.datenimexport.backuptables;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class DBDepartment {
	
	private String dKuerzel;
	private String dBezeichnung;
	
	@JsonCreator
	public DBDepartment(@JsonProperty("dkuerzel") String dkuerzel,
						@JsonProperty("dbezeichnung") String dbezeichung){
		
		this.dKuerzel = dkuerzel;
		this.dBezeichnung = dbezeichung;
		
	}

	@Override
	public int hashCode() {
		
		return this.dBezeichnung.hashCode() + this.dKuerzel.hashCode();
	}

	@Override
	public boolean equals(Object obj) {
		
		if(obj == null){
			return false;
		}
		
		if(obj == this){
			return true;
		}
		
		if(obj instanceof DBDepartment){
			
			DBDepartment dbp = (DBDepartment) obj;
			
			return this.dBezeichnung.equals(dbp.getdBezeichnung())
					&& this.dKuerzel.equals(dbp.getdKuerzel());
		}
		
		return false;
	}

	@Override
	public String toString() {
		
		return this.dKuerzel + ": " + this.dBezeichnung;
	}

	public String getdKuerzel() {
		return dKuerzel;
	}

	public void setdKuerzel(String dKuerzel) {
		this.dKuerzel = dKuerzel;
	}

	public String getdBezeichnung() {
		return dBezeichnung;
	}

	public void setdBezeichnung(String dBezeichnung) {
		this.dBezeichnung = dBezeichnung;
	}
	
	
	
	

}
