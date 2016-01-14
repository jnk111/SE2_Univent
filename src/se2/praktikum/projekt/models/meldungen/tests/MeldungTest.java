package se2.praktikum.projekt.models.meldungen.tests;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.junit.BeforeClass;
import org.junit.Test;

import se2.praktikum.projekt.models.meldungen.Meldung;
import se2.praktikum.projekt.models.meldungen.MeldungTyp;
import se2.praktikum.projekt.models.meldungen.TEAblehnung;
import se2.praktikum.projekt.models.meldungen.TeamAustrittsmeldung;
import se2.praktikum.projekt.models.meldungen.Teameinladung;
import se2.praktikum.projekt.models.person.Student;
import se2.praktikum.projekt.models.person.fachwerte.MatrikelNr;
import se2.praktikum.projekt.models.team.Team;
import se2.praktikum.projekt.models.team.fachwerte.TeamID;

public class MeldungTest {

	private final static int NUM_OF_TESTS = 10;
	
	private static List<Meldung> meldListe1;
	private static List<Meldung> meldListe2;
	

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		setUpData();
	}
	
	
	@Test
	public void testEqualsCollection(){
		
		assertTrue(meldListe1.equals(meldListe2));
		assertTrue(meldListe1.equals(meldListe1));
		assertTrue(meldListe2.equals(meldListe2));
		
		Set<Meldung> leistSet1 = new HashSet<Meldung>();
		Set<Meldung> leistSet2 = new HashSet<Meldung>();
		
		leistSet1.addAll(meldListe1);
		leistSet2.addAll(meldListe2);
		
		assertTrue(leistSet1.equals(leistSet2));
		assertTrue(leistSet1.equals(leistSet1));
		assertTrue(leistSet2.equals(leistSet2));
		
		leistSet1 = null;
		
		assertFalse(leistSet2.equals(leistSet1));
	
	}
	
	
	@Test
	public void testEqualsMeldungen(){
		
		// AssertTrue
		for(int i = 0; i < meldListe1.size(); i++){
			assertTrue(meldListe1.get(i).equals(meldListe2.get(i)));
		}
		
		//AssertFalse
		for(int i = 0; i < meldListe1.size()-1; i++){
			for(int j = i+1; j < meldListe1.size(); j++){
			assertFalse(meldListe1.get(i).equals(meldListe2.get(j)));
			}
		}
	}

	private static void setUpData() {
		
		meldListe1 = new ArrayList<>();
		meldListe2 = new ArrayList<>();
		String testAbsVorname = "AbsVorname";
		String testAbsNachname = "AbsNachname";
		String testEmpfVorname = "AbsVorname";
		String testEmpfNachname = "AbsNachname";
		// String testNachrText = "Nachrichtentext";
		
		
		// Teameinladungen
		for(int i = 0; i < (NUM_OF_TESTS - 4); i++){
			
			MatrikelNr absMatr = MatrikelNr.getMatrikelNr(i+1);
			MatrikelNr empfMatr = MatrikelNr.getMatrikelNr(i+2);
			Student abs = new Student(absMatr, testAbsVorname + i, testAbsNachname + i);
			Student empf = new Student(empfMatr, testEmpfVorname + i, testEmpfNachname + i);
			
			TeamID teamId = TeamID.getTeamID(""+(i+4));
			Team team = new Team();
			team.setTeamID(teamId);
			boolean bestaetigt = false;
			
			if(i % 2 == 0){
				bestaetigt = true;
			}
			Meldung meld = new Teameinladung(abs, empf, new Date(), team, bestaetigt, null, MeldungTyp.TEAMEINL);
			
			meldListe1.add(meld);
			meldListe2.add(meld);
		}
		
		
		// Teamaustrittsmeldungen
		for(int i = 0; i < (NUM_OF_TESTS - 4); i++){
			
			MatrikelNr absMatr = MatrikelNr.getMatrikelNr(i+1);
			MatrikelNr empfMatr = MatrikelNr.getMatrikelNr(i+2);
			Student abs = new Student(absMatr, testAbsVorname + i, testAbsNachname + i);
			Student empf = new Student(empfMatr, testEmpfVorname + i, testEmpfNachname + i);
			
			TeamID teamId = TeamID.getTeamID(""+(i+4));
			Team team = new Team();
			team.setTeamID(teamId);

			Meldung meld = new TeamAustrittsmeldung(abs, empf, new Date(), team, MeldungTyp.TEAMAUSTR);
			meldListe1.add(meld);
			meldListe2.add(meld);
		}
		
		// Teameinladung-Ablehnungsmeldungen
		for(int i = 0; i < NUM_OF_TESTS - 8; i++){
			
			MatrikelNr absMatr = MatrikelNr.getMatrikelNr(i+1);
			MatrikelNr empfMatr = MatrikelNr.getMatrikelNr(i+2);
			Student abs = new Student(absMatr, testAbsVorname + i, testAbsNachname + i);
			Student empf = new Student(empfMatr, testEmpfVorname + i, testEmpfNachname + i);
			
			TeamID teamId = TeamID.getTeamID(""+(i+4));
			Team team = new Team();
			team.setTeamID(teamId);
			Meldung meld = new TEAblehnung(abs, empf, new Date(), team, "TEAnlehnung");
			meldListe1.add(meld);
			meldListe2.add(meld);
		}
	}

}
