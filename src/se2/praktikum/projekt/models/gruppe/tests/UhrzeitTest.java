package se2.praktikum.projekt.models.gruppe.tests;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.junit.BeforeClass;
import org.junit.Test;

import se2.praktikum.projekt.models.gruppe.fachwerte.Uhrzeit;

public class UhrzeitTest {

	private final static int NUM_OF_TESTS = 10;
	
	private static List<Uhrzeit> uzListe1;
	private static List<Uhrzeit> uzListe2;
	private static String [] uhrzeiten = {"8:15", "11:45", "12:30","15:45", "16:30"};

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		setUpData();
	}
	
	
	@Test
	public void testEqualsCollection(){
		
		assertTrue(uzListe1.equals(uzListe2));
		assertTrue(uzListe1.equals(uzListe1));
		assertTrue(uzListe2.equals(uzListe2));
		
		Set<Uhrzeit> grSet1 = new HashSet<Uhrzeit>();
		Set<Uhrzeit> grSet2 = new HashSet<Uhrzeit>();
		
		grSet1.addAll(uzListe1);
		grSet2.addAll(uzListe2);
		
		assertTrue(grSet1.equals(grSet2));
		assertTrue(grSet1.equals(grSet1));
		assertTrue(grSet2.equals(grSet2));
		
		grSet1 = null;
		
		assertFalse(grSet2.equals(grSet1));
	
	}
	
	
	@Test
	public void testEqualsGruppe(){
		
		// AssertTrue
		for(int i = 0; i < uzListe1.size(); i++){
			assertTrue(uzListe1.get(i).equals(uzListe2.get(i)));
		}
		
		//AssertFalse
		for(int i = 0; i < uzListe1.size()-1; i++){
			for(int j = i+1; j < uzListe1.size(); j++){
			assertFalse(uzListe1.get(i).equals(uzListe2.get(j)));
			}
		}
	}

	private static void setUpData() {
		
		uzListe1 = new ArrayList<>();
		uzListe2 = new ArrayList<>();
		Uhrzeit.getUhrzeit("16:30");
		for(int i = 0; i < (NUM_OF_TESTS/2); i++){
			
			Uhrzeit uz = Uhrzeit.getUhrzeit(uhrzeiten[i]);
			uzListe1.add(uz);
		}
		
		for(int i = 0; i < (NUM_OF_TESTS/2); i++){
			
			Uhrzeit uz = Uhrzeit.getUhrzeit(uhrzeiten[i]);
			uzListe2.add(uz);
		}
	}

}
