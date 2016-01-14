package se2.praktikum.projekt.models.meldungen;

import se2.praktikum.projekt.models.team.Team;

public abstract class Teammeldung extends Meldung{
	
	protected Team team;

	
	public Team getTeam() {
		return team;
	}

	public void setTeam(Team team) {
		this.team = team;
	}
	
	@Override
	public int hashCode(){

		return super.hashCode() + team.hashCode();
	}


	@Override
	public abstract boolean equals(Object obj);

}
