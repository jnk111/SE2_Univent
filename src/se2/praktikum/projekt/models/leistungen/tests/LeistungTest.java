package se2.praktikum.projekt.models.leistungen.tests;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.junit.BeforeClass;
import org.junit.Test;

import se2.praktikum.projekt.models.leistungen.Leistung;
import se2.praktikum.projekt.models.leistungen.NoteBKP;
import se2.praktikum.projekt.models.person.Professor;
import se2.praktikum.projekt.models.person.Student;
import se2.praktikum.projekt.models.person.fachwerte.MAID;
import se2.praktikum.projekt.models.person.fachwerte.MatrikelNr;

public class LeistungTest {

	private final static int NUM_OF_TESTS = 10;
	
	private static List<Leistung> LeistListe1;
	private static List<Leistung> LeistListe2;
	
	private static NoteBKP [] noten;
	private static boolean [] pvl;
	private static Student [] studenten;
	private static Professor [] professoren;
	

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		setUpData();
	}
	
	
	@Test
	public void testEqualsCollection(){
		
		assertTrue(LeistListe1.equals(LeistListe2));
		assertTrue(LeistListe1.equals(LeistListe1));
		assertTrue(LeistListe2.equals(LeistListe2));
		
		Set<Leistung> leistSet1 = new HashSet<Leistung>();
		Set<Leistung> leistSet2 = new HashSet<Leistung>();
		
		leistSet1.addAll(LeistListe1);
		leistSet2.addAll(LeistListe2);
		
		assertTrue(leistSet1.equals(leistSet2));
		assertTrue(leistSet1.equals(leistSet1));
		assertTrue(leistSet2.equals(leistSet2));
		
		leistSet1 = null;
		
		assertFalse(leistSet2.equals(leistSet1));
	
	}
	
	
	@Test
	public void testEqualsLeistung(){
		
		// AssertTrue
		for(int i = 0; i < LeistListe1.size(); i++){
			assertTrue(LeistListe1.get(i).equals(LeistListe2.get(i)));
		}
		
		//AssertFalse
		for(int i = 0; i < LeistListe1.size()-1; i++){
			for(int j = i+1; j < LeistListe1.size(); j++){
			assertFalse(LeistListe1.get(i).equals(LeistListe2.get(j)));
			}
		}
	}

	private static void setUpData() {
		
		LeistListe1 = new ArrayList<>();
		LeistListe2 = new ArrayList<>();
		noten = new NoteBKP[NUM_OF_TESTS];
		professoren = new Professor[NUM_OF_TESTS];
		studenten = new Student[NUM_OF_TESTS];
		String testVorname = "Vorname";
		String testNachname = "Nachname";
		//String testFach = "Fach";
		
		for(int i = 0; i < NUM_OF_TESTS; i++){
			
			noten[i] = new NoteBKP(i + 1);
			MAID maid = MAID.getMAID(i + 1);
			MatrikelNr matrNr = MatrikelNr.getMatrikelNr((i+1) * 7);
			String vn = testVorname + i;
			String nn = testNachname + i;
			professoren[i] = new Professor(maid, vn, nn);
			studenten[i] = new Student(matrNr, vn, nn);
			pvl = new boolean[NUM_OF_TESTS];
		}
		
		for(int i = 0; i < NUM_OF_TESTS; i++){
			
			if(i % 2 == 0){
				pvl[i] = true;
			}else{
				pvl[i] = false;
			}
		}
		
		
//		for(int i = 0; i < NUM_OF_TESTS; i++){
//			
//			Leistung l = new Leistung(new Fach(testFach + i), studenten[i], professoren[i], pvl[i], noten[i]);
//			LeistListe1.add(l);
//		}
//		
//		for(int i = 0; i < (NUM_OF_TESTS); i++){
//			
//			Leistung l = new Leistung(new Fach(testFach + i), studenten[i], professoren[i], pvl[i], noten[i]);
//			LeistListe2.add(l);
//		}
	}

}
