package se2.praktikum.projekt.datenimexport.backuptables;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class DBPruefungsordnung {
	
	private String poKuerzel;
	private String poBezeichnung;
	
	@JsonCreator
	public DBPruefungsordnung(@JsonProperty("poKuerzel") String poKuerzel,
							  @JsonProperty("poBezeichnung") String poBezeichnung){
		
		this.poKuerzel = poKuerzel;
		this.poBezeichnung = poBezeichnung;
	}

	@Override
	public int hashCode() {
		
		return this.poKuerzel.hashCode() + this.poBezeichnung.hashCode();
	}

	@Override
	public String toString() {
		
		return this.poKuerzel + ", " + this.poBezeichnung;
	}

	
	@Override
	public boolean equals(Object obj) {
		
		if(obj == null){
			return false;
		}
		if(obj == this){
			return true;
		}
		
		if(obj instanceof DBPruefungsordnung){
			
			DBPruefungsordnung po = (DBPruefungsordnung) obj;
			
			return this.poKuerzel.equals(po.getPoKuerzel())
					&& this.poBezeichnung.equals(po.getPoBezeichnung());
		}
		
		return false;
	}

	public String getPoKuerzel() {
		return poKuerzel;
	}

	public void setPoKuerzel(String poKuerzel) {
		this.poKuerzel = poKuerzel;
	}

	public String getPoBezeichnung() {
		return poBezeichnung;
	}

	public void setPoBezeichnung(String poBezeichnung) {
		this.poBezeichnung = poBezeichnung;
	}
	
	
	
	

}
