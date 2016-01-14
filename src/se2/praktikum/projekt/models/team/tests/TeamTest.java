package se2.praktikum.projekt.models.team.tests;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.junit.BeforeClass;
import org.junit.Test;

import se2.praktikum.projekt.models.team.Team;
import se2.praktikum.projekt.models.team.fachwerte.TeamID;

public class TeamTest {

	private final static int NUM_OF_TESTS = 10;
	
	private static List<Team> teamListe1;
	private static List<Team> teamListe2;
	

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		setUpData();
	}
	
	
	@Test
	public void testEqualsCollection(){
		
		assertTrue(teamListe1.equals(teamListe2));
		assertTrue(teamListe1.equals(teamListe1));
		assertTrue(teamListe2.equals(teamListe2));
		
		Set<Team> teamSet1 = new HashSet<Team>();
		Set<Team> teamSet2 = new HashSet<Team>();
		
		teamSet1.addAll(teamListe1);
		teamSet2.addAll(teamListe2);
		
		assertTrue(teamSet1.equals(teamSet2));
		assertTrue(teamSet1.equals(teamSet1));
		assertTrue(teamSet2.equals(teamSet2));
		
		teamSet1 = null;
		
		assertFalse(teamSet2.equals(teamSet1));
	
	}
	
	
	@Test
	public void testEqualsTeam(){
		
		// AssertTrue
		for(int i = 0; i < teamListe1.size(); i++){
			assertTrue(teamListe1.get(i).equals(teamListe2.get(i)));
		}
		
		//AssertFalse
		for(int i = 0; i < teamListe1.size()-1; i++){
			for(int j = i+1; j < teamListe1.size(); j++){
			assertFalse(teamListe1.get(i).equals(teamListe2.get(j)));
			}
		}
	}

	private static void setUpData() {
		
		teamListe1 = new ArrayList<>();
		teamListe2 = new ArrayList<>();
		
		
		// Studenten
		for(int i = 0; i < (NUM_OF_TESTS); i++){
			
			
			TeamID teamId = TeamID.getTeamID(""+(i + 2));
			Team team = new Team();
			team.setTeamID(teamId);
			
			teamListe1.add(team);
			teamListe2.add(team);
		}
	}
		
		
}
