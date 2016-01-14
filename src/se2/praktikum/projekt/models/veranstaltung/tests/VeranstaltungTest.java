package se2.praktikum.projekt.models.veranstaltung.tests;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.junit.BeforeClass;
import org.junit.Test;

import se2.praktikum.projekt.models.person.Professor;
import se2.praktikum.projekt.models.person.fachwerte.MAID;
import se2.praktikum.projekt.models.veranstaltung.Fach;
import se2.praktikum.projekt.models.veranstaltung.Praktikum;
import se2.praktikum.projekt.models.veranstaltung.Projekt;
import se2.praktikum.projekt.models.veranstaltung.Veranstaltung;
import se2.praktikum.projekt.models.veranstaltung.WP;

public class VeranstaltungTest {

	private final static int NUM_OF_TESTS = 10;
	
	private static List<Veranstaltung> veranListe1;
	private static List<Veranstaltung> veranListe2;
	

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		setUpData();
	}
	
	
	@Test
	public void testEqualsCollection(){
		
		assertTrue(veranListe1.equals(veranListe2));
		assertTrue(veranListe1.equals(veranListe1));
		assertTrue(veranListe2.equals(veranListe2));
		
		Set<Veranstaltung> veranSet1 = new HashSet<Veranstaltung>();
		Set<Veranstaltung> veranSet2 = new HashSet<Veranstaltung>();
		
		veranSet1.addAll(veranListe1);
		veranSet2.addAll(veranListe2);
		
		assertTrue(veranSet1.equals(veranSet2));
		assertTrue(veranSet1.equals(veranSet1));
		assertTrue(veranSet2.equals(veranSet2));
		
		veranSet1 = null;
		
		assertFalse(veranSet2.equals(veranSet1));
	
	}
	
	
	@Test
	public void testEqualsVeranstaltung(){
		
		// AssertTrue
		for(int i = 0; i < veranListe1.size(); i++){
			assertTrue(veranListe1.get(i).equals(veranListe2.get(i)));
		}
		
		//AssertFalse
		for(int i = 0; i < veranListe1.size()-1; i++){
			for(int j = i+1; j < veranListe1.size(); j++){
				assertFalse(veranListe1.get(i).equals(veranListe2.get(j)));
			}
		}
	}

	private static void setUpData() {
		
		veranListe1 = new ArrayList<>();
		veranListe2 = new ArrayList<>();
		String testFach = "Fach";
		String testVorname = "Vorname";
		String testNachname = "Nachname";
		
		
		// Pflichpraktika
		for(int i = 0; i < (NUM_OF_TESTS - 4); i++){
			
			
			MAID maid = MAID.getMAID(i + 2);
			Professor prof = new Professor(maid, testVorname + i, testNachname + i);
			Veranstaltung prak = new Praktikum(new Fach(testFach + i), prof);
			veranListe1.add(prak);
			veranListe2.add(prak);
		}
		
		
		// WP
		for(int i = 0; i < (NUM_OF_TESTS - 4); i++){
			
			MAID maid = MAID.getMAID(i + 4);
			Professor prof = new Professor(maid, testVorname + i, testNachname + i);
			Veranstaltung wp = new WP(new Fach(testFach + i), prof);
			veranListe1.add(wp);
			veranListe2.add(wp);
		}
		
		// PO
		for(int i = 0; i < NUM_OF_TESTS - 8; i++){
			
			MAID maid = MAID.getMAID(i + 6);
			Professor prof = new Professor(maid, testVorname + i, testNachname + i);
			Veranstaltung po = new Projekt(new Fach(testFach + i), prof);
			veranListe1.add(po);
			veranListe2.add(po);
		}
		
	}

}
