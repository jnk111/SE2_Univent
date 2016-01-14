package se2.praktikum.projekt.models.veranstaltung;

import se2.praktikum.projekt.models.person.IAngestellter;

/**
 * Repräsentiert eine Veranstaltung als abstrakte Klasse
 * @author Jan
 *
 */
public abstract class AbstrVeranstaltung implements Veranstaltung{
	
	
	// Felder
	protected Fach fach;					// Das Fach dieser Veranstaltung
	protected IAngestellter professor;			// Modulverantwortlicher Professor
	protected int anzTm;			// aktuelle Anzahl der Teilnehmer
	protected int anzGr;				// aktuelle Anzahl der Gruppen
	protected int minTeilnTeam;
	protected int maxTeilnTeam;
	protected int semester;
	protected String typ;			// Info für Json Datenexport
	
	@Override
	public Fach getFach() {
		return fach;
	}
	
	@Override
	public void setFach(Fach fach) {
		this.fach = fach;
	}
	
	@Override
	public IAngestellter getProfessor() {
		return professor;
	}
	
	
	
	@Override
	public int getAnzTm() {
		return anzTm;
	}
	
	@Override
	public void setAnzTm(int anzTm) {
		this.anzTm = anzTm;
	}
	
	
	@Override
	public int getAnzGr() {
		return anzGr;
	}
	
	@Override
	public void setAnzGr(int anzGr) {
		this.anzGr = anzGr;
	}
	

	@Override
	public void setProfessor(IAngestellter professor) {
		this.professor = professor;
		
	}

	@Override
	public int getMinTeilnTeam() {
		return minTeilnTeam;
	}

	@Override
	public void setMinTeilnTeam(int minTeilnTeam) {
		this.minTeilnTeam = minTeilnTeam;
	}

	@Override
	public int getMaxTeilnTeam() {
		return maxTeilnTeam;
	}

	@Override
	public void setMaxTeilnTeam(int maxTeilnTeam) {
		this.maxTeilnTeam = maxTeilnTeam;
	}
	

	@Override
	public int hashCode() {
		
		return this.getFach().hashCode();
	}

	
	
	public String getTyp() {
		return typ;
	}

	public void setTyp(String typ) {
		this.typ = typ;
	}

	@Override
	public int getSemester() {
		return semester;
	}

	@Override
	public void setSemester(int semester) {
		this.semester = semester;
	}

	@Override
	public abstract boolean equals(Object obj);
	
	@Override
	public String toString(){
		
		return this.getFach().getFachKuerzel() 
				+ ", AnzTm: "+this.getAnzTm() + ", AnzGr: "
				+ this.getAnzGr() + ", MinTeam: " 
				+ this.getMinTeilnTeam() 
				+ ", MaxTeam: " + this.getMaxTeilnTeam() 
				+ ", Sem: " + this.getSemester();
	}
	
	

	
	
	
	
	
}
