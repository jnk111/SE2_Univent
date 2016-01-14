package se2.praktikum.projekt.datenimexport.backuptables;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import se2.praktikum.projekt.models.gruppe.fachwerte.Uhrzeit;

public class DBAnmeldetermin {
	
	private String vTyp;
	private String aTyp;
	private Date dStart;
	private Date dEnde;
	private Uhrzeit uzStart;
	private Uhrzeit uzEnde;
	
	@JsonCreator
	public DBAnmeldetermin(@JsonProperty("vTyp") String vTyp,
						  @JsonProperty("aTyp") String aTyp,
						 @JsonProperty("dStart") Date dStart,
						 @JsonProperty("dEnde") Date dEnde,
						 @JsonProperty("uzStart") Uhrzeit uzStart,
						 @JsonProperty("uzEnde") Uhrzeit uzEnde){
		
		this.vTyp = vTyp;
		this.setaTyp(aTyp);
		this.dStart = dStart;
		this.setdEnde(dEnde);
		this.uzStart = uzStart;
		this.uzEnde = uzEnde;
	}

	@Override
	public int hashCode() {
		return this.vTyp.hashCode() 
				+ this.dStart.hashCode() 
				+ this.uzEnde.hashCode() 
				+ this.uzStart.hashCode();
	}

	@SuppressWarnings("deprecation")
	@Override
	public boolean equals(Object obj) {
		
		if(obj == null){
			return false;
		}
		
		if(this == obj){
			return true;
		}
		
		if(obj instanceof DBAnmeldetermin) {
			
			DBAnmeldetermin at = (DBAnmeldetermin) obj;
			
			return this.dStart.getMonth() == at.getdStart().getMonth()
					&& this.dStart.getDate() == at.getdStart().getDate()
					&& this.dStart.getYear() == at.getdStart().getYear()
					&& this.vTyp.equals(at.getvTyp())
					&& this.aTyp.equals(at.getaTyp())
					&& this.uzStart.equals(at.getUzStart())
					&& this.uzEnde.equals(at.getUzEnde());
		}
		
		return false;
	}

	@SuppressWarnings("deprecation")
	@Override
	public String toString() {
		
		return this.vTyp + ", " + this.dStart.toGMTString() + ", " + this.uzStart.toString() + ", " + this.uzEnde.toString();
	}

	public String getvTyp() {
		return vTyp;
	}

	public void setvTyp(String vTyp) {
		this.vTyp = vTyp;
	}

	public String getaTyp() {
		return aTyp;
	}

	public void setaTyp(String aTyp) {
		this.aTyp = aTyp;
	}

	public Date getdStart() {
		return dStart;
	}

	public void setdStart(Date dStart) {
		this.dStart = dStart;
	}

	public Date getdEnde() {
		return dEnde;
	}

	public void setdEnde(Date dEnde) {
		this.dEnde = dEnde;
	}

	public Uhrzeit getUzStart() {
		return uzStart;
	}

	public void setUzStart(Uhrzeit uzStart) {
		this.uzStart = uzStart;
	}

	public Uhrzeit getUzEnde() {
		return uzEnde;
	}

	public void setUzEnde(Uhrzeit uzEnde) {
		this.uzEnde = uzEnde;
	}
	
	


	
	

}
