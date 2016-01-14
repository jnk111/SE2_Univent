package se2.praktikum.projekt.models.gruppe.tests;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.junit.BeforeClass;
import org.junit.Test;

import se2.praktikum.projekt.models.gruppe.Gruppe;

public class GruppeTest {
	
	private final static int NUM_OF_TESTS = 10;
	
	private static List<Gruppe> grListe1;
	private static List<Gruppe> grListe2;
	private static String [] fachkuerzel = {"PRP1", "PRP2", "BSP", "ADP", "BWP"};
	private static int [] grpNummern = {1, 2, 3, 4, 5};

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		setUpData();
	}
	
	
	@Test
	public void testEqualsCollection(){
		
		assertTrue(grListe1.equals(grListe2));
		assertTrue(grListe1.equals(grListe1));
		assertTrue(grListe2.equals(grListe2));
		
		Set<Gruppe> grSet1 = new HashSet<Gruppe>();
		Set<Gruppe> grSet2 = new HashSet<Gruppe>();
		
		grSet1.addAll(grListe1);
		grSet2.addAll(grListe2);
		
		assertTrue(grSet1.equals(grSet2));
		assertTrue(grSet1.equals(grSet1));
		assertTrue(grSet2.equals(grSet2));
		
		grSet1 = null;
		
		assertFalse(grSet2.equals(grSet1));
	
	}
	
	
	@Test
	public void testEqualsGruppe(){
		
		// AssertTrue
		for(int i = 0; i < grListe1.size(); i++){
			assertTrue(grListe1.get(i).equals(grListe2.get(i)));
		}
		
		//AssertFalse
		for(int i = 0; i < grListe1.size()-1; i++){
			for(int j = i+1; j < grListe1.size(); j++){
			assertFalse(grListe1.get(i).equals(grListe2.get(j)));
			}
		}
	}

	private static void setUpData() {
		
		grListe1 = new ArrayList<>();
		grListe2 = new ArrayList<>();
		
		for(int i = 0; i < (NUM_OF_TESTS/2); i++){
			
			Gruppe gr = new Gruppe(fachkuerzel[i], grpNummern[i]);
			grListe1.add(gr);
		}
		
		for(int i = 0; i < (NUM_OF_TESTS/2); i++){
			Gruppe gr = new Gruppe(fachkuerzel[i], grpNummern[i]);
			grListe2.add(gr);
		}
	}



}
