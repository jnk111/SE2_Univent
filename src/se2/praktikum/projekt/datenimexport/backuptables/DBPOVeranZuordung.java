package se2.praktikum.projekt.datenimexport.backuptables;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class DBPOVeranZuordung {
	
	private String poKuerzel;
	private String fKuerzel;
	
	@JsonCreator
	public DBPOVeranZuordung(@JsonProperty("poKuerzel") String poKuerzel,
							 @JsonProperty("fKuerzel") String fKuerzel){
		
		
		this.poKuerzel = poKuerzel;
		this.fKuerzel = fKuerzel;
	}

	@Override
	public int hashCode() {
		
		return this.poKuerzel.hashCode() + this.fKuerzel.hashCode();
	}

	@Override
	public boolean equals(Object obj) {
		
		if(obj == null){
			return false;
		}
		
		if(obj == this){
			return true;
		}
		
		if(obj instanceof DBPOVeranZuordung){
			
			DBPOVeranZuordung poVeran = (DBPOVeranZuordung) obj;
			
			return this.fKuerzel.equals(poVeran.getfKuerzel())
					&& this.poKuerzel.equals(poVeran.getPoKuerzel());
		}
		
		return false;
	}

	@Override
	public String toString() {
		
		return this.fKuerzel + " <-> " +this.poKuerzel;
	}

	public String getPoKuerzel() {
		return poKuerzel;
	}

	public void setPoKuerzel(String poKuerzel) {
		this.poKuerzel = poKuerzel;
	}

	public String getfKuerzel() {
		return fKuerzel;
	}

	public void setfKuerzel(String fKuerzel) {
		this.fKuerzel = fKuerzel;
	}
	
	
	
	
	

}
