package se2.praktikum.projekt.models.veranstaltung;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeName;

import se2.praktikum.projekt.models.person.Professor;


/**
 * Repräsentiert ein Wahlpflichtpraktikum
 * @author Jan
 *
 */
@JsonTypeName("wp")
public class WP extends AbstrVeranstaltung {
	
	/**
	 * Default-Konstruktor
	 * Initialisiert ein neues WP ohne Parameter
	 * Felder können seperat über Setter festgelegt werden
	 */
	public WP(){
		
		this(null, null);
		
	}
	
	
	/**
	 * Initialisiert ein neues WP mit den wichtigsten Parametern
	 * Restliche Felder können separat über Setter festgelegt werden.
	 * @param fach
	 * @param prof
	 * @param assist
	 */
	public WP(Fach fach, Professor prof)			{
		
		this(fach, 0, prof, 0, 0, 0, 0);
	}
	
	public WP(Fach fach, int semester, Professor prof)			{
		
		this(fach, semester, prof, 0, 0, 0, 0);
	}
	
	
	/**
	 * Initialsiert ein neues WP mit übergebenen Parametern
	 * @param fach : Das Fach
	 * @param prof : Der Verantwortliche Professor
	 * @param assist : Assistent des Professors
	 * @param anzTm : aktuelle Anzahl Teilnehmer
	 * @param anzGr : aktuelle Anzahl Gruppen
	 * @param maxGr : aktuelle max. möglche Gruppenzahl.
	 */
	@JsonCreator
	public WP( @JsonProperty("fach") Fach fach, 
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
		
		if(obj instanceof WP){
			
			WP va = (WP) obj;
			
			return this.getFach().equals(va.getFach());
		}else{
			return false;
		}
	}


	@Override
	public int hashCode() {
		
		return super.hashCode() * 18;
	}

}
