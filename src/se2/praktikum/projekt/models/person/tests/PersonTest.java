package se2.praktikum.projekt.models.person.tests;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.junit.BeforeClass;
import org.junit.Test;

import se2.praktikum.projekt.models.person.Assistent;
import se2.praktikum.projekt.models.person.IPerson;
import se2.praktikum.projekt.models.person.Professor;
import se2.praktikum.projekt.models.person.Student;
import se2.praktikum.projekt.models.person.fachwerte.MAID;
import se2.praktikum.projekt.models.person.fachwerte.MatrikelNr;

public class PersonTest {
	
	private final static int NUM_OF_TESTS = 10;
	
	private static List<IPerson> persListe1;
	private static List<IPerson> persListe2;
	

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		setUpData();
	}
	
	
	@Test
	public void testEqualsCollection(){
		
		assertTrue(persListe1.equals(persListe2));
		assertTrue(persListe1.equals(persListe1));
		assertTrue(persListe2.equals(persListe2));
		
		Set<IPerson> leistSet1 = new HashSet<IPerson>();
		Set<IPerson> leistSet2 = new HashSet<IPerson>();
		
		leistSet1.addAll(persListe1);
		leistSet2.addAll(persListe2);
		
		assertTrue(leistSet1.equals(leistSet2));
		assertTrue(leistSet1.equals(leistSet1));
		assertTrue(leistSet2.equals(leistSet2));
		
		leistSet1 = null;
		
		assertFalse(leistSet2.equals(leistSet1));
	
	}
	
	
	@Test
	public void testEqualsPerson(){
		
		// AssertTrue
		for(int i = 0; i < persListe1.size(); i++){
			assertTrue(persListe1.get(i).equals(persListe2.get(i)));
		}
		
		//AssertFalse
		for(int i = 0; i < persListe1.size()-1; i++){
			for(int j = i+1; j < persListe1.size(); j++){
			assertFalse(persListe1.get(i).equals(persListe2.get(j)));
			}
		}
	}

	private static void setUpData() {
		
		persListe1 = new ArrayList<>();
		persListe2 = new ArrayList<>();
		String testVorname = "Vorname";
		String testNachname = "Nachname";
		
		
		// Studenten
		for(int i = 0; i < (NUM_OF_TESTS - 4); i++){
			
			MatrikelNr absMatr = MatrikelNr.getMatrikelNr(i+1);
			//MatrikelNr empfMatr = MatrikelNr.getMatrikelNr(i+2);
			Student stud = new Student(absMatr, testVorname + i, testNachname + i);
			
			persListe1.add(stud);
			persListe2.add(stud);
		}
		
		
		// Assistenten
		for(int i = 0; i < (NUM_OF_TESTS - 4); i++){
			
			MAID maid = MAID.getMAID(i + 2);
			Assistent assis = new Assistent(maid, testVorname + i, testNachname + i);

			persListe1.add(assis);
			persListe2.add(assis);
		}
		
		// Professoren
		for(int i = 0; i < NUM_OF_TESTS - 8; i++){
			
			MAID maid = MAID.getMAID(i + 4);
			Professor prof = new Professor(maid, testVorname + i, testNachname + i);

			persListe1.add(prof);
			persListe2.add(prof);
		}
	}

}
