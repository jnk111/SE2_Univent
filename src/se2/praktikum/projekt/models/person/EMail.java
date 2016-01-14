package se2.praktikum.projekt.models.person;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class EMail {
	
	private String email; // Vorerst einfach nur String
	
	@JsonCreator
	public EMail(@JsonProperty("email") String email){
		this.email = email;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	@Override
	public int hashCode() {

		return this.getEmail().hashCode();
	}

	@Override
	public boolean equals(Object obj) {

		if(obj == null){
			
			return false;
		}
		
		
		if(obj instanceof EMail){
			
			EMail a = (EMail) obj;
			
			return this.getEmail().equals(a.getEmail());
		}else{
			return false;
		}
		
	}
	
	
	
	

}
