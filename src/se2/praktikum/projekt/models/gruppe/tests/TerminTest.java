package se2.praktikum.projekt.models.gruppe.tests;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.junit.BeforeClass;
import org.junit.Test;

import se2.praktikum.projekt.models.gruppe.Termin;
import se2.praktikum.projekt.models.gruppe.fachwerte.TerminID;
import se2.praktikum.projekt.models.gruppe.fachwerte.Uhrzeit;

public class TerminTest {

	private final static int NUM_OF_TESTS = 10;
	
	private static List<Termin> terminListe1;
	private static List<Termin> terminListe2;

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		setUpData();
	}
	
	
	@Test
	public void testEqualsCollection(){
		
		assertTrue(terminListe1.equals(terminListe2));
		assertTrue(terminListe1.equals(terminListe1));
		assertTrue(terminListe2.equals(terminListe2));
		
		Set<Termin> grSet1 = new HashSet<Termin>();
		Set<Termin> grSet2 = new HashSet<Termin>();
		
		grSet1.addAll(terminListe1);
		grSet2.addAll(terminListe2);
		
		assertTrue(grSet1.equals(grSet2));
		assertTrue(grSet1.equals(grSet1));
		assertTrue(grSet2.equals(grSet2));
		
		grSet1 = null;
		
		assertFalse(grSet2.equals(grSet1));
	
	}
	
	
	@Test
	public void testEqualsGruppe(){
		
		// AssertTrue
		for(int i = 0; i < terminListe1.size(); i++){
			assertTrue(terminListe1.get(i).equals(terminListe2.get(i)));
		}
		
		//AssertFalse
		for(int i = 0; i < terminListe1.size()-1; i++){
			for(int j = i+1; j < terminListe1.size(); j++){
			assertFalse(terminListe1.get(i).equals(terminListe2.get(j)));
			}
		}
	}

	private static void setUpData() {
		
		terminListe1 = new ArrayList<>();
		terminListe2 = new ArrayList<>();
		Date date = new Date();
		Uhrzeit uzStart = Uhrzeit.getUhrzeit("08:15");
		Uhrzeit uzEnde = Uhrzeit.getUhrzeit("11:30");
		for(int i = 0; i < (NUM_OF_TESTS/2); i++){
			
			TerminID id = TerminID.getTerminID(""+(i+1));
			Termin t = new Termin();
			t.setTerminID(id);
			t.setDatum(date);
			t.setStart(uzStart);
			t.setEnde(uzEnde);
			terminListe1.add(t);
		}
		
		for(int i = 0; i < (NUM_OF_TESTS/2); i++){
			
			TerminID id = TerminID.getTerminID(""+(i+1));
			Termin t = new Termin();
			t.setTerminID(id);
			t.setDatum(date);
			t.setStart(uzStart);
			t.setEnde(uzEnde);
			
			terminListe2.add(t);
		}
	}
}
