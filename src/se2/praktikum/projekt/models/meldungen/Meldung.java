package se2.praktikum.projekt.models.meldungen;

import java.util.Date;

import se2.praktikum.projekt.models.person.IPerson;

/**
 * Repräsentiert eine Meldung
 * @author jan
 *
 */
public abstract class Meldung {
	
	protected IPerson absender;
	protected IPerson empfaenger;
	protected Date versandDatum;
	protected String typ;
	
	public IPerson getAbsender() {
		return absender;
	}
	
	public void setAbsender(IPerson absender) {
		this.absender = absender;
	}
	
	public IPerson getEmpfaenger() {
		return empfaenger;
	}
	
	public void setEmpfaenger(IPerson empfaenger) {
		this.empfaenger = empfaenger;
	}
	
	public Date getVersandDatum() {
		return versandDatum;
	}
	
	public void setVersandDatum(Date versandDatum) {
		this.versandDatum = versandDatum;
	}

	@Override
	public int hashCode(){
		
		return this.getAbsender().hashCode() 
				+ this.getEmpfaenger().hashCode() 
				+ this.getVersandDatum().hashCode();
		
	}

	@Override
	public abstract boolean equals(Object obj);

	public String getTyp() {
		return typ;
	}

	public void setTyp(String typ) {
		this.typ = typ;
	}

	
	
	
	
	
	
	

}
