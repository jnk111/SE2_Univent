package se2.praktikum.projekt.models.leistungen;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Vorläufige Notenklasse die einfach nur einen String nimmt
 * @author jan
 *
 */
public class Note {
	
	
	public String note;
	
	@JsonCreator
	public Note(@JsonProperty("note") String note){
		
		this.note = note;
	}

	public String getNote() {
		
		return note;
	}

	public void setNote(String note) {
		
		this.note = note;
	}
	
	@Override
	public boolean equals(Object obj) {

		if(obj == null){
			return false;
		}
		
		if(obj instanceof Note){
			
			Note n = (Note) obj;
			return (n.getNote() == null && this.getNote() == null) 
					|| (n.getNote().equals(getNote()));
			
		}else{
			return false;
		}
	}

	@Override
	public String toString() {
		
		return this.note;
	}
	
	
	
	
	
	

}
