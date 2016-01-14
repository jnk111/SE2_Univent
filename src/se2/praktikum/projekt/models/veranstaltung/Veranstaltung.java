package se2.praktikum.projekt.models.veranstaltung;

import se2.praktikum.projekt.models.person.IAngestellter;

//@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.PROPERTY, property = "type")
//@JsonSubTypes(value = {
//    @JsonSubTypes.Type(name = "praktikum", value = Praktikum.class),
//    @JsonSubTypes.Type(name = "wp", value = WP.class),
//    @JsonSubTypes.Type(name = "projekt", value = Projekt.class)
//}) 
public interface Veranstaltung {
 
	public Fach getFach();

	public void setFach(Fach fach);

	public IAngestellter getProfessor();

	public int getAnzTm();

	public void setAnzTm(int anzTm);


	public int getAnzGr();

	public void setAnzGr(int anzGr);


	public void setProfessor(IAngestellter professor);

	int getMinTeilnTeam();

	void setMinTeilnTeam(int minTeilnTeam);

	int getMaxTeilnTeam();

	void setMaxTeilnTeam(int maxTeilnTeam);
	
	public String getTyp();
	
	public void setTyp(String typ);

	int getSemester();

	void setSemester(int semester);
	

}
