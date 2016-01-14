package se2.praktikum.projekt.models.team.fachwerte;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Die Klasse repräsentiert den Fachwert TeamID 
 * -> Dienst als Primary Key des Tables "TEAMS" in der Datenbank 
 * @author jan
 *
 */
public class TeamID {
	
	
	private String id;
	/**
	 * Referenziert eine neue TeamID
	 * @param string
	 */
	private TeamID(String string) {
		// TODO: Preconditions
		
		this.id = string;
	}

	@JsonCreator
	public static TeamID getTeamID(@JsonProperty("id") String id){
		
		
		return new TeamID(id); // Beispiel
	}

	/**
	 * Gibt den Integerwert einer TeamID zurück
	 * @return Integerwert der TeamID
	 */
	public String getId() {
		return id;
	}

	@Override
	public int hashCode() {
		
		return this.getId().hashCode() * 26;
	}

	@Override
	public boolean equals(Object obj) {
		
		if(obj == null){
			return false;
		}
		
		if(obj == this){
			return true;
		}
		
		if(obj instanceof TeamID){
			
			TeamID id = (TeamID) obj;
			
			return this.getId().equals(id.getId());
		}else{
			return false;
		}
	}
	
	
	

}