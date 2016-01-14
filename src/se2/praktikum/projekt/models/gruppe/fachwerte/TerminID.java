package se2.praktikum.projekt.models.gruppe.fachwerte;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class TerminID 
{
	
	private String id;
	
	private TerminID(String id)
	{
		this.setId(id);
	}
	
	@JsonCreator
	public static TerminID getTerminID(@JsonProperty("id") String id)
	{
		return new TerminID(id);
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	@Override
	public boolean equals(Object obj) {
		
		if(obj == null){
			return false;
		}
		
		
		if(obj instanceof TerminID){
			
			TerminID t = (TerminID) obj;
			// Hier später noch Datum-Vergleich mit Comparator
			return this.getId().equals(t.getId());
			
		}else{
			return false;
		}
	}

	@Override
	public int hashCode() {
		
		return this.getId().hashCode()*12;
	}

	@Override
	public String toString() {
		
		return this.getId();
	}
	
	
	
}