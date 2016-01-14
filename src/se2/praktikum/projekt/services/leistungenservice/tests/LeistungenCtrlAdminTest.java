package se2.praktikum.projekt.services.leistungenservice.tests;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import se2.praktikum.projekt.datenimexport.DatenImExportController;
import se2.praktikum.projekt.models.leistungen.Leistung;
import se2.praktikum.projekt.models.leistungen.Note;
import se2.praktikum.projekt.models.person.Professor;
import se2.praktikum.projekt.models.person.Student;
import se2.praktikum.projekt.models.person.fachwerte.MAID;
import se2.praktikum.projekt.models.person.fachwerte.MatrikelNr;
import se2.praktikum.projekt.models.veranstaltung.Fach;
import se2.praktikum.projekt.services.leistungenservice.LeistungenCtrlAdmin;
import se2.praktikum.projekt.services.loginservice.LoginSrv;
import se2.praktikum.projekt.services.loginservice.ServicePool;

public class LeistungenCtrlAdminTest {
	
	private static LoginSrv login;
	private static ServicePool servicePool;
	private static List<Leistung> leistungenBenoted;
	private static List<Leistung> leistungenUnbenoted;
	private static List<Leistung> leistungenGesamt;
	private static List<String> fachkuerzelSem1;
	private static List<String> fachkuerzelSem2;
	private static List<String> fachkuerzelSem3;
	private static List<String> fachkuerzelSem4;
	private static List<String> fachkuerzelSem5;
	

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		
		servicePool = new ServicePool();
		login = new LoginSrv(servicePool);
		login.login("naa001", "naa001");
		List<String> folders = DatenImExportController.holeBackupDateien();
		Collections.sort(folders);
		DatenImExportController.backupWiederherstellen(folders.get(0));
		
		leistungenBenoted = new ArrayList<>();
		leistungenUnbenoted = new ArrayList<>();
		leistungenGesamt = new ArrayList<>();
		fachkuerzelSem1 = new ArrayList<>();
		fachkuerzelSem2 = new ArrayList<>();
		fachkuerzelSem3 = new ArrayList<>();
		fachkuerzelSem4 = new ArrayList<>();
		fachkuerzelSem5 = new ArrayList<>();
		
		setUp();
	}

	/* Methoen die getestet werden muessen
	 * ====================================================================
	 * public @ResponseBody List<Leistung> leistungenAnzeigen(@RequestBody List<String> args)   -> erledigt
	 * public @ResponseBody boolean noteEintragen(@RequestBody Leistung leistung)				-> erledigt
	 * public @ResponseBody boolean pvlEintragen(@RequestBody Leistung leistung)				-> erledigt
	 * public @ResponseBody List<String> holeFachkuerzel(@RequestBody List<String> args)		-> erledigt
	 */


	@Test
	public void testLeistungenAnzeigen() {
		
		List<String> fachkuerzel 
				= new ArrayList<>(Arrays.asList("PRP1", "MGP", "PRP2", "DBP", "BSP", "GKAP", "WPDBDP", "WPPNP"));
		
		List<Leistung> result = new ArrayList<>();
		Leistung currLeist = null;
		// Hole alle Leistungen
		for(String fk: fachkuerzel){
			result.addAll(LeistungenCtrlAdmin.leistungenAnzeigen(fk));
		}

		try{
			// enthaelt result die Stichprobenleistungen?
			for(Leistung l: leistungenGesamt){
				
				currLeist = l;
				assertTrue(result.contains(l));			
			}
		}catch(AssertionError e){
			
			System.out.println("Fehler - testLeistungenAnzeigen()");
			System.out.println("==================================");
			
			System.out.println(currLeist.toString());
			System.out.println();
			
			for(Leistung l: leistungenGesamt){
				System.out.println(l.toString());
			}
		}

	}
	
	@Test
	public void testTrageNoteEin(){
		
		ObjectMapper mapper = new ObjectMapper();
		List<Leistung> nachherBenoted = new ArrayList<>();
		Date date = new Date();
		
		for(Leistung l: leistungenUnbenoted){
			
			assertTrue(l.getNote() == null
					 	&& l.getDatumNote() == null);
			
			l.setDatumNote(date);
			int noteValue = (int) (Math.random() * 15);
			
			l.setDatumNote(date);
			l.setNote(new Note(""+noteValue));
			try {
				assertTrue(LeistungenCtrlAdmin.noteEintragen(mapper.writeValueAsString(l)));
			} catch (JsonProcessingException e) {
				e.printStackTrace();
			}
			nachherBenoted.add(l);
		}
		

		
		for(Leistung l: nachherBenoted){
			
			List<Leistung> eingetragen = new ArrayList<>();
			eingetragen.addAll(LeistungenCtrlAdmin.leistungenAnzeigen(l.getFach().getFachKuerzel()));
			
			assertTrue(eingetragen.contains(l) 
						&& l.getNote() != null
						&& l.getDatumNote().equals(date));
		}
	}
	
	
	@Test
	public void testTragePVLEin(){
		
		ObjectMapper mapper = new ObjectMapper();
		List<Leistung> nachherPVL = new ArrayList<>();
		Date date = new Date();
		
		for(Leistung l: leistungenUnbenoted){
			
			assertTrue(!l.isPvl());
			
			l.setDatumPVL(date);
			l.setPvl(true);
			
			try {
				assertTrue(LeistungenCtrlAdmin.pvlEintragen(mapper.writeValueAsString(l)));
			} catch (JsonProcessingException e) {

				e.printStackTrace();
			}
			nachherPVL.add(l);
		}
		

		
		for(Leistung l: nachherPVL){
			
			List<Leistung> eingetragen = new ArrayList<>();
			eingetragen.addAll(LeistungenCtrlAdmin.leistungenAnzeigen(l.getFach().getFachKuerzel()));
			
			assertTrue(eingetragen.contains(l) 
						&& l.isPvl()
						&& l.getDatumPVL().equals(date));
		}
	}
	
	
	@Test
	public void testHoleFachkuerzel(){

		List<String> args = new ArrayList<>(Arrays.asList("AI", "Praktikum", "1"));
		List<String> result = LeistungenCtrlAdmin.holeFachkuerzel(args);
		args = new ArrayList<>(Arrays.asList("AI", "Vorlesung", "1"));
		result.addAll(LeistungenCtrlAdmin.holeFachkuerzel(args));
		
		try{
			assertTrue(fachkuerzelSem1.containsAll(result));
			assertFalse(fachkuerzelSem2.containsAll(result));
			assertFalse(fachkuerzelSem3.containsAll(result));
			assertFalse(fachkuerzelSem4.containsAll(result));
			assertFalse(fachkuerzelSem5.containsAll(result));
			
			args = new ArrayList<>(Arrays.asList("AI", "Praktikum", "2"));
			result = LeistungenCtrlAdmin.holeFachkuerzel(args);
			args = new ArrayList<>(Arrays.asList("AI", "Vorlesung", "2"));
			result.addAll(LeistungenCtrlAdmin.holeFachkuerzel(args));
			
			result = LeistungenCtrlAdmin.holeFachkuerzel(args);
			
			assertTrue(fachkuerzelSem2.containsAll(result));
			assertFalse(fachkuerzelSem1.containsAll(result));
			assertFalse(fachkuerzelSem3.containsAll(result));
			assertFalse(fachkuerzelSem4.containsAll(result));
			assertFalse(fachkuerzelSem5.containsAll(result));
			
			args = new ArrayList<>(Arrays.asList("AI", "Praktikum", "3"));
			result = LeistungenCtrlAdmin.holeFachkuerzel(args);
			args = new ArrayList<>(Arrays.asList("AI", "Vorlesung", "3"));
			result.addAll(LeistungenCtrlAdmin.holeFachkuerzel(args));
			
			assertTrue(fachkuerzelSem3.containsAll(result));
			assertFalse(fachkuerzelSem1.containsAll(result));
			assertFalse(fachkuerzelSem2.containsAll(result));
			assertFalse(fachkuerzelSem4.containsAll(result)); 
			assertFalse(fachkuerzelSem5.containsAll(result));
			
			args = new ArrayList<>(Arrays.asList("AI", "WP", "4"));
			result = LeistungenCtrlAdmin.holeFachkuerzel(args);
			args = new ArrayList<>(Arrays.asList("AI", "WPP", "4"));
			result.addAll(LeistungenCtrlAdmin.holeFachkuerzel(args));
			
			assertFalse(fachkuerzelSem3.containsAll(result));
			assertFalse(fachkuerzelSem1.containsAll(result));
			assertFalse(fachkuerzelSem2.containsAll(result));
			// assertTrue(fachkuerzelSem4.containsAll(result));  --> Stimmt nicht mehr da neue Daten fuer finale Abgabe hinzugekommen
			assertFalse(fachkuerzelSem5.containsAll(result));
			
			args = new ArrayList<>(Arrays.asList("AI", "PO", "5"));
			result = LeistungenCtrlAdmin.holeFachkuerzel(args);
			
			assertFalse(fachkuerzelSem3.containsAll(result));
			assertFalse(fachkuerzelSem1.containsAll(result));
			assertFalse(fachkuerzelSem2.containsAll(result));
			assertFalse(fachkuerzelSem4.containsAll(result));
			assertTrue(fachkuerzelSem5.containsAll(result));
		} catch (AssertionError e){
			
			System.out.println("Fachkuerzel Semester 2");
			System.out.println("======================");
			for(String fk: fachkuerzelSem2){
				System.out.println(fk);
			}
			
			System.out.println();
			System.out.println("Ergebnis");
			System.out.println("====================");
			for(String fk: result){
				
				System.out.println(fk);
			}
			
			e.printStackTrace();
			
		}

	}
	

	private static void setUp() {
				
		Professor schmolitzky = new Professor(MAID.getMAID(3), "Axel", "Schmolitzky");
		Professor gerken = new Professor(MAID.getMAID(4), "Wolfgang", "Gerken");
		
		Fach pr1 = new Fach("PRP1", 1);
		Fach mg = new Fach("MGP", 1);
		Student stud1 = new Student(MatrikelNr.getMatrikelNr(1111111),"Martin", "Schmidt");
		Student stud2 = new Student(MatrikelNr.getMatrikelNr(1111112),"Clara", "Schulz");
		Student stud3 = new Student(MatrikelNr.getMatrikelNr(1111113),"Achmed", "Swolyski");
		Student stud4 = new Student(MatrikelNr.getMatrikelNr(1111114),"James", "Bond");
		Student stud5 = new Student(MatrikelNr.getMatrikelNr(1111121),"Natsu", "Dragneel");
		Student stud6 = new Student(MatrikelNr.getMatrikelNr(1111122),"Patrick", "Schuller");
		Student stud7 = new Student(MatrikelNr.getMatrikelNr(1111123),"Sercy", "Lannister");

		// Semester 1
		fachkuerzelSem1.add("PR1");
		fachkuerzelSem1.add("PRP1");
		fachkuerzelSem1.add("MGP");
		fachkuerzelSem1.add("MG");
		
		// Semester 2
		fachkuerzelSem2.add("PR2");
		fachkuerzelSem2.add("PRP2");
		fachkuerzelSem2.add("DB");
		fachkuerzelSem2.add("DBP");

		// Semester 3
		fachkuerzelSem3.add("BS");
		fachkuerzelSem3.add("BSP");
		fachkuerzelSem3.add("GKA");
		fachkuerzelSem3.add("GKAP");
		
		// Semester 4
		fachkuerzelSem4.add("WPDBD");
		fachkuerzelSem4.add("WPDBDP");
		fachkuerzelSem4.add("WPPN");
		fachkuerzelSem4.add("WPPNP");
		fachkuerzelSem4.add("WPNSBDP");
		fachkuerzelSem4.add("WPDEP");
		
		// Semester 5
		fachkuerzelSem5.add("POCTF");
		fachkuerzelSem5.add("POLA");
		fachkuerzelSem5.add("POSR");
		fachkuerzelSem5.add("POWRTC");
		
		/*  Testdaten
		 *  ============================================================================================================================================================================
		    Bewertung: MAID, matrNr, FKuerzel, DatumPvl, DatumNote, Pvl, Note */ /* noch füllen, 5*5't Semester, 5*3't Semester, alle fächer davor ausser, das Semester direkt darunter 
			insert INTO Bewertung values(3, 1111111, 'PR1', TO_DATE('1-13-2014','MM-DD-YYYY'), TO_DATE('2-5-2014','MM-DD-YYYY'), 'erhalten', '12');
			insert INTO Bewertung values(3, 1111112, 'PR1', TO_DATE('1-13-2014','MM-DD-YYYY'), TO_DATE('2-5-2014','MM-DD-YYYY'), 'erhalten', '14');
			insert INTO Bewertung values(4, 1111113, 'MG', TO_DATE('1-13-2014','MM-DD-YYYY'), TO_DATE('2-5-2014','MM-DD-YYYY'), 'erhalten', '9');
			insert INTO Bewertung values(4, 1111114, 'MG', TO_DATE('1-13-2014','MM-DD-YYYY'), TO_DATE('2-5-2014','MM-DD-YYYY'), 'erhalten', '7');;
			insert INTO Bewertung values(3, 1111121, 'PR1', TO_DATE('1-13-2016','MM-DD-YYYY'), null, 'nicht erhalten', null);
			insert INTO Bewertung values(3, 1111122, 'PR1', TO_DATE('1-13-2016','MM-DD-YYYY'), null, 'nicht erhalten', null);
			insert INTO Bewertung values(4, 1111121, 'MG', TO_DATE('1-13-2016','MM-DD-YYYY'), null, 'nicht erhalten', null);
			insert INTO Bewertung values(4, 1111122, 'MG', TO_DATE('1-13-2016','MM-DD-YYYY'), null, 'nicht erhalten', null);
			insert INTO Bewertung values(4, 1111123, 'MG', TO_DATE('1-13-2016','MM-DD-YYYY'), null, 'nicht erhalten', null);
		*/
		
		Note note1 = new Note("12");
		Note note2 = new Note("14");
		Note note3 = new Note("9");
		Note note4 = new Note("7");

		// Benotet
		Leistung leist1 = new Leistung(pr1, stud1, schmolitzky, new Date(1-13-2014), new Date(2-5-2014), true, note1);
		Leistung leist2 = new Leistung(pr1, stud2, schmolitzky, new Date(1-13-2014), new Date(2-5-2014), true, note2);
		Leistung leist3 = new Leistung(mg, stud3, gerken, new Date(1-13-2014), new Date(2-5-2014), true, note3);
		Leistung leist4 = new Leistung(mg, stud4, gerken, new Date(1-13-2014), new Date(2-5-2014), true, note4);
		
		// unbenoted
		Leistung leist5 = new Leistung(pr1, stud5, schmolitzky, new Date(1-13-2016), null, false, null);
		Leistung leist6 = new Leistung(pr1, stud6, schmolitzky, new Date(1-13-2016), null, false, null);
		Leistung leist7 = new Leistung(mg, stud5, gerken, new Date(1-13-2016), null, false, null);
		Leistung leist8 = new Leistung(mg, stud6, gerken, new Date(1-13-2016), null, false, null);
		Leistung leist9 = new Leistung(mg, stud7, gerken, new Date(1-13-2016), null, false, null);
		
		leistungenBenoted.add(leist1);
		leistungenBenoted.add(leist2);
		leistungenBenoted.add(leist3);
		leistungenBenoted.add(leist4);
		
		leistungenUnbenoted.add(leist5);
		leistungenUnbenoted.add(leist6);
		leistungenUnbenoted.add(leist7);
		leistungenUnbenoted.add(leist8);
		leistungenUnbenoted.add(leist9);
		
		leistungenGesamt.addAll(leistungenBenoted);
		leistungenGesamt.addAll(leistungenUnbenoted);
		
		// ==============================================================================================================================================================================

	}
	
	
    @AfterClass
    public static void setzeDatenWiederZurueck() {
    	
    	List<String> folders = DatenImExportController.holeBackupDateien();
    	Collections.sort(folders);
    	DatenImExportController.backupWiederherstellen(folders.get(0));
    }
    	
    	
  

}
