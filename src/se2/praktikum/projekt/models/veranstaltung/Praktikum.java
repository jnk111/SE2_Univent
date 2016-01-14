package se2.praktikum.projekt.models.veranstaltung;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import se2.praktikum.projekt.models.person.Professor;

/**
 * Repräsentiert ein Pflichtpraktikum
 * @author Jan
 *
 */
public class Praktikum extends AbstrVeranstaltung {
	
	/**
	 * Default-Konstruktor
	 * Initialisiert ein neues Praktikum ohne Parameter
	 * Felder können seperat über Setter festgelegt werden
	 */
	public Praktikum() {
		
		this(null, null);
		
	}
	
	
	/**
	 * Initialisiert ein neues Praktikum mit den wichtigsten Parametern
	 * Restliche Felder können separat über Setter festgelegt werden.
	 * @param fach
	 * @param prof
	 * @param assist
	 */
	public Praktikum(Fach fach, Professor prof)			{
		
		this(fach, 0, prof,0, 0, 0, 0);
		
	}
	
	
	public Praktikum(Fach fach, int semester, Professor prof){
		
		this(fach, semester, prof, 0, 0, 0, 0);
		
	}
	
	
	/**
	 * Initialsiert ein neues Praktikum mit übergebenen Parametern
	 * @param fach : Das Fach
	 * @param prof : Der Verantwortliche Professor
	 * @param assist : Assistent des Professors
	 * @param anzTm : aktuelle Anzahl Teilnehmer
	 * @param anzGr : aktuelle Anzahl Gruppen
	 * @param maxGr : aktuelle max. möglche Gruppenzahl.
	 */
	@JsonCreator
	public Praktikum(@JsonProperty("fach") Fach fach, 
					 @JsonProperty("semester") int semester,
					 @JsonProperty("professor")	Professor prof, 
					 @JsonProperty("anzTm") int anzTm, 
					 @JsonProperty("anzGr") int anzGr,
					 @JsonProperty("minTeilnTeam") int minTeilnTeam, 
					 @JsonProperty("maxTeilnTeam") int maxTeilnTeam)					{
		
		this.fach = fach;
		this.semester = semester;
		this.professor = prof;
		this.anzTm = anzTm;
		this.anzGr = anzGr;
		this.minTeilnTeam = minTeilnTeam;
		this.maxTeilnTeam = maxTeilnTeam;
	}
	
	@Override
	public boolean equals(Object obj) {
		
		
		if(obj == null){
			return false;
		}
		
		if(obj == this){
			return true;
		}
		
		if(obj instanceof Praktikum){
			
			Praktikum va = (Praktikum) obj;
			
			return this.getFach().equals(va.getFach());
		}else{
			return false;
		}
	}


	@Override
	public int hashCode() {
		
		return super.hashCode() * 6;
	}

}
