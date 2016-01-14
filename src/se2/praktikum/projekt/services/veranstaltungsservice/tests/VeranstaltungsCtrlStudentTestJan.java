package se2.praktikum.projekt.services.veranstaltungsservice.tests;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import se2.praktikum.projekt.datenimexport.DatenImExportController;
import se2.praktikum.projekt.dbms.DBConnector;
import se2.praktikum.projekt.models.gruppe.Gruppe;
import se2.praktikum.projekt.models.gruppe.Termin;
import se2.praktikum.projekt.models.leistungen.Leistung;
import se2.praktikum.projekt.models.meldungen.GruppevollMeldung;
import se2.praktikum.projekt.models.meldungen.Meldung;
import se2.praktikum.projekt.models.meldungen.MeldungTyp;
import se2.praktikum.projekt.models.meldungen.Teameinladung;
import se2.praktikum.projekt.models.person.Assistent;
import se2.praktikum.projekt.models.person.Professor;
import se2.praktikum.projekt.models.person.Student;
import se2.praktikum.projekt.models.person.fachwerte.MAID;
import se2.praktikum.projekt.models.person.fachwerte.MatrikelNr;
import se2.praktikum.projekt.models.team.Team;
import se2.praktikum.projekt.models.team.fachwerte.TeamID;
import se2.praktikum.projekt.models.veranstaltung.Fach;
import se2.praktikum.projekt.models.veranstaltung.Praktikum;
import se2.praktikum.projekt.models.veranstaltung.Veranstaltung;
import se2.praktikum.projekt.services.loginservice.LoginSrv;
import se2.praktikum.projekt.services.loginservice.ServicePool;
import se2.praktikum.projekt.services.veranstaltungsservice.VeranstaltungsCtrlStudent;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class VeranstaltungsCtrlStudentTestJan {

	private static LoginSrv login;
	private static Student currStud;
	private static ServicePool servicePool;
	private static List<Gruppe> gruppen;
	private static List<Gruppe> gruppenMitPVL;
	private static List<Student> studentenHabenPVL;
	private static List<Student> studentenHabenTeam;
	private static List<Student> studenten;
	private static Connection connection;
	// private static List<Gruppe> volleGruppen;
	// private static List<Gruppe> nichtVolleGruppen;
	
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		
		servicePool = new ServicePool();
		login = new LoginSrv(servicePool);
		login.login("naa001", "naa001");
		connection = DBConnector.getConnection();
		servicePool = login.getServicePool();
		
		currStud = new Student(MatrikelNr.getMatrikelNr(1111111),"Martin", "Schmidt");
		servicePool.getUsers().put(MatrikelNr.getMatrikelNr(1111111), currStud);
		
		List<String> folders = DatenImExportController.holeBackupDateien();
		Collections.sort(folders);
		DatenImExportController.backupWiederherstellen(folders.get(0));
		loescheBewertungen();
		
		gruppen = new ArrayList<>();
		studentenHabenPVL = new ArrayList<>();
		studentenHabenTeam = new ArrayList<>();
		gruppenMitPVL = new ArrayList<>();
		studenten = new ArrayList<>();
		
		
		
		setUp();
	}
	
	/* Methoden die getestet werden muessen
	 * ============================================================================================================================
	 * public @ResponseBody List<Gruppe> getFreieGruppenTeam(@RequestBody String fachkuerzel)							-> erledigt
	 * public @ResponseBody List<Gruppe> getFreieGruppenEinzel(@RequestBody String fachkuerzel)							-> erledigt
	 * public @ResponseBody List<Student> getAlleStudenten(@RequestBody String fachkuerzel)								-> erledigt
	 * public static synchronized @ResponseBody boolean sendeTeamEinladung(@RequestBody String team)					-> erledigt
	 * public static @ResponseBody List<Meldung> holeAlleMeldungen()													-> erledigt
	 * public static synchronized @ResponseBody boolean nehmeTeamEinladungAn(@RequestBody Teameinladung teameinladung)	-> erledigt
	 * public static @ResponseBody boolean lehneTeamEinladungAb(@RequestBody Teameinladung teameinladung)				-> erledigt
	 * public static @ResponseBody boolean teamVerlassen(@RequestBody Team team)										-> erledigt
	 * public static @ResponseBody boolean speichereEinzelAnmeldung(@RequestBody List<String> gruppen)					-> erledigt
	 * public @ResponseBody List<Team> getAlleTeamsAktuellerStudent()													-> erledigt
	 * public @ResponseBody boolean holeStartAnmeldedatumTeam()															-> später
	 * public @ResponseBody boolean holeStartAnmeldedatumEinzel()														-> später
 	 * public @ResponseBody boolean holeEndeAnmeldedatumTeam()															-> später
	 * public @ResponseBody boolean holeEndeAnmeldedatumEinzel()														-> später
	 
	
	 * 
	 * ============================================================================================================================
	 */
	
	@Test
	public void test1GetFreieGruppenTeam() {													// 1.
		
		Professor huebner = new Professor(MAID.getMAID(1), "Martin", "Huebner");
		Assistent blank = new Assistent(MAID.getMAID(11), "Ilona", "Blank");
		List<Termin> terminePrp1 = servicePool.getVerAnzSrv().getTermine(4, "PRP1");
		List<Gruppe> freieGruppen = VeranstaltungsCtrlStudent.getFreieGruppenTeamPraktikum("PRP1", ""+currStud.getMatrNr().getMatrNr());
		freieGruppen.addAll(VeranstaltungsCtrlStudent.getFreieGruppenTeamPraktikum("MGP", ""+currStud.getMatrNr().getMatrNr()));
		freieGruppen.addAll(VeranstaltungsCtrlStudent.getFreieGruppenTeamPraktikum("PRP2", ""+currStud.getMatrNr().getMatrNr()));
		Gruppe volleGruppePrp2 = new Gruppe("PRP2", 4, terminePrp1, huebner, blank, 2, 2);
		
		
		try{
			assertTrue(!freieGruppen.isEmpty());
			assertFalse(freieGruppen.contains(volleGruppePrp2));
			assertTrue(gruppen.contains(volleGruppePrp2));
			assertFalse(freieGruppen.containsAll(gruppenMitPVL));
			
		}catch(AssertionError e){
			
			System.out.println("Fehler testGetFreieGruppenTeam");
			System.out.println("================");
			System.out.println(volleGruppePrp2);
			System.out.println();
			
			for(Gruppe gr: freieGruppen){
				System.out.println(gr.toString());
			}
			
		}
	}
	
	
	
	@Test
	public void test2GetFreieGruppenEinzel(){										// 2.
		
		Professor huebner = new Professor(MAID.getMAID(1), "Martin", "Huebner");
		Assistent blank = new Assistent(MAID.getMAID(11), "Ilona", "Blank");
		
		List<Termin> terminePrp1 = servicePool.getVerAnzSrv().getTermine(4, "PRP1");
		List<Termin> terminePrp2 = servicePool.getVerAnzSrv().getTermine(4, "PRP2");
		List<Gruppe> freieGruppen = VeranstaltungsCtrlStudent.getFreieGruppenEinzelPraktikum("PRP1", "" + currStud.getMatrNr().getMatrNr());
		freieGruppen.addAll(VeranstaltungsCtrlStudent.getFreieGruppenEinzelPraktikum("PRP2", "" + currStud.getMatrNr().getMatrNr()));
		Gruppe volleGruppePrp1 = new Gruppe("PRP1", 4, terminePrp1, huebner, blank, 2, 2);
		Gruppe nichtVolleGruppeEinzel = new Gruppe("PRP2", 4, terminePrp2, huebner, blank, 2, 2);
		
		try{
			assertTrue(!freieGruppen.isEmpty());
			assertFalse(freieGruppen.contains(volleGruppePrp1));
			assertTrue(gruppen.contains(volleGruppePrp1));
			assertTrue(freieGruppen.contains(nichtVolleGruppeEinzel));
			assertTrue(gruppen.contains(nichtVolleGruppeEinzel));
			
		}catch(AssertionError e){
			
			System.out.println("Fehler testGetFreieGruppenEinzel");
			System.out.println("================================");
			System.out.println(volleGruppePrp1);
			System.out.println();
			for(Gruppe gr: freieGruppen){
				System.out.println(gr.toString());
			}			
		}
	}
	
	@Test
	public void test3GetAlleStudenten(){										// 3.
		
		
		List<Student> gueltigeStudenten = VeranstaltungsCtrlStudent.getAlleStudenten("PRP1", "" + currStud.getMatrNr().getMatrNr());		
		assertTrue(!gueltigeStudenten.containsAll(studentenHabenTeam));
		assertTrue(!gueltigeStudenten.containsAll(studentenHabenPVL));
		
	}
	
	@Test
	public void test4TeamVormerkenNachSendeTeamEinladung(){					// 4.
		
		ObjectMapper mapper = new ObjectMapper();
		List<Student> mitglieder = new ArrayList<>();
		int indexStud = (int) (Math.random() * studenten.size());
		Student mg1 = studenten.get(indexStud);
		indexStud = (int) (Math.random() * studenten.size());
		Student mg2 = studenten.get(indexStud);
		
		while(mg1.equals(mg2)){
			
			indexStud = (int) (Math.random() * studenten.size());
			mg2 = studenten.get(indexStud);
		}
		
		indexStud = (int) (Math.random() * studenten.size());
		Student mg3 = studenten.get(indexStud);
		
		while(mg3.equals(mg2) || mg3.equals(mg1)){
			indexStud = (int) (Math.random() * studenten.size());
			mg3 = studenten.get(indexStud);
		}
		
		assertFalse(mg1.equals(mg2));
		assertFalse(mg1.equals(mg3));
		assertFalse(mg2.equals(mg3));;
		
		mitglieder.add(mg1);
		mitglieder.add(mg2);
		
	
		Team team1 = new Team(null, 2, 3, mitglieder, -1, 4, "DBP");
		Team team2 = new Team(null, 2, 3, mitglieder, -1, 4, "BSP"); 
		
		try {
			
			team1.setTeamID(neueTeamId());
			String team1Json = mapper.writeValueAsString(team1);
			String team2Json = mapper.writeValueAsString(team2);
			servicePool.addUser(mg1);
			currStud = mg1;
			assertTrue(VeranstaltungsCtrlStudent.sendeTeamEinladung(team1Json, currStud.getMatrNr().getMatrNr() + ""));
			team2.setTeamID(neueTeamId());
			assertTrue(VeranstaltungsCtrlStudent.sendeTeamEinladung(team2Json, currStud.getMatrNr().getMatrNr() + ""));
			assertTrue(einladungenWurdenAngelegt(team1, mg1));
			assertTrue(einladungenWurdenAngelegt(team2, mg1));
			assertTrue(mitgliedIstBestaetigt(team1, (Student) servicePool.getUser(currStud.getMatrNr())));
			assertTrue(mitgliedIstBestaetigt(team2, (Student) servicePool.getUser(currStud.getMatrNr())));
			
			for(Student stud: team1.getMitglieder()){
				
				assertFalse(mitgliedIstBestaetigt(team1, stud));
			}
			
			for(Student stud: team2.getMitglieder()){
				
				assertFalse(mitgliedIstBestaetigt(team2, stud));
			}
			
			assertTrue(teamWurdeAngelegt(team1));
			assertTrue(teamWurdeAngelegt(team2));
			assertTrue(teamHatMitglieder(team1));
			assertTrue(teamHatMitglieder(team2));
			
			mitglieder.clear();
			mitglieder.add(mg3);
			
			team1Json = mapper.writeValueAsString(team1);
			team2Json = mapper.writeValueAsString(team2);
			
			// Nachträgliches Mitglied -> Schlägt fehl, wenn dieses Team noch einmal angelegt wird
			assertTrue(VeranstaltungsCtrlStudent.sendeTeamEinladung(team1Json, "" + currStud.getMatrNr().getMatrNr()));
			assertTrue(VeranstaltungsCtrlStudent.sendeTeamEinladung(team2Json, "" + currStud.getMatrNr().getMatrNr()));
			assertTrue(einladungenWurdenAngelegt(team1, mg1));
			assertTrue(einladungenWurdenAngelegt(team2, mg1));
			
			assertTrue(mitgliedIstBestaetigt(team1, (Student) servicePool.getUser(currStud.getMatrNr())));
			assertTrue(mitgliedIstBestaetigt(team2, (Student) servicePool.getUser(currStud.getMatrNr())));
			
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	
	@Test
	public void test5LehneTeamEinladungAb(){										// 5.
		
		ObjectMapper mapper = new ObjectMapper();
		
		// Team 1
		Student stud1 = new Student(MatrikelNr.getMatrikelNr(1111112),"Clara", "Schulz");		// Absender
		Student stud2 = new Student(MatrikelNr.getMatrikelNr(1111113),"Achmed", "Swolyski");
		List<Student> mitgliederTeam1 = new ArrayList<>(Arrays.asList(stud2));
		Team team1 = new Team(null, 2, 3, mitgliederTeam1, -1, 6, "PRP2");
		
		// Team 2
		// Student stud3 = new Student(MatrikelNr.getMatrikelNr(1111114),"James", "Bond");			// Absender
		// Student stud4 = new Student(MatrikelNr.getMatrikelNr(1111121),"Natsu", "Dragneel");
		// List<Student> mitgliederTeam2 = new ArrayList<>(Arrays.asList(stud4));
		// Team team2 = new Team(null, 2, 2,  mitgliederTeam2, -1, 6, "PRP2");
		
		// Team 3																				// Absender
		// Student stud5 = new Student(MatrikelNr.getMatrikelNr(1111122),"Patrick", "Schuller");
		// Student stud6 = new Student(MatrikelNr.getMatrikelNr(1111123),"Sercy", "Lannister");
		// List<Student> mitgliederTeam3 = new ArrayList<>(Arrays.asList(stud6));
		// Team team3 = new Team(null, 2, 3, mitgliederTeam3, -1, 6, "PRP2");
		
		// Team 1 vormerken -> erfolgreich
		// =================================================================================================
		try{
			
			servicePool.addUser(stud1);
			currStud = stud1;
			String team1String = mapper.writeValueAsString(team1);
			assertTrue(VeranstaltungsCtrlStudent.sendeTeamEinladung(team1String, "" + currStud.getMatrNr().getMatrNr()));
			assertTrue(einladungenWurdenAngelegt(team1, stud1));
			team1.setTeamID(getTeamID(team1));
			assertTrue(teamWurdeAngelegt(team1));
			Teameinladung einladung = getTeamEinladung(team1, stud1, stud2);
			team1.getMitglieder().add(stud1);
			assertTrue(VeranstaltungsCtrlStudent.lehneTeamEinladungAb(mapper.writeValueAsString(einladung)));
			
			assertTrue(teamMitgliedGeloescht(stud2, team1));
			assertTrue(teamEinladungGeloescht(team1, stud2));
			assertTrue(teamAblehnungenVorhanden(team1));
			
		}catch(AssertionError e){
			
			System.err.println("Fehler - testLehneTeamEinladungAb() - Team 1");
			System.err.println("==============================================");
			System.err.println(team1.toString());
			System.err.println("Absender: " + stud1.toString());
			System.err.println("Empfaenger: " + stud2.toString());
			System.err.println();
			e.printStackTrace();
			
		} catch (JsonProcessingException e) {
			
			e.printStackTrace();
			
		} catch (SQLException e) {
			
			e.printStackTrace();
		}
	}
	
	
	@Test
	public void test6VerlasseTeamWaehrendAnmeldephase(){							// 6.
		
		ObjectMapper mapper = new ObjectMapper();
		
		// Team 1
		Student stud1 = new Student(MatrikelNr.getMatrikelNr(1111112),"Clara", "Schulz");		// Absender
		Student stud2 = new Student(MatrikelNr.getMatrikelNr(1111113),"Achmed", "Swolyski");
		Student stud3 = new Student(MatrikelNr.getMatrikelNr(1111114),"James", "Bond");	
		List<Student> mitgliederTeam1 = new ArrayList<>(Arrays.asList(stud1, stud2, stud3));
		Team team1 = new Team(null, 2, 3, mitgliederTeam1, -1, 5, "DBP");
	
		
		// Team 1 anmelden -> erfolgreich
		// =================================================================================================
		try{
			
			servicePool.addUser(stud1);
			currStud = stud1;
			String team1String = mapper.writeValueAsString(team1);
			assertTrue(VeranstaltungsCtrlStudent.sendeTeamEinladung(team1String, "" + currStud.getMatrNr().getMatrNr()));
			assertTrue(einladungenWurdenAngelegt(team1, stud1));
			team1.setTeamID(getTeamID(team1));
			assertTrue(teamWurdeAngelegt(team1));
			Teameinladung einladung1 = getTeamEinladung(team1, stud1, stud2);
			Teameinladung einladung2 = getTeamEinladung(team1, stud1, stud3);
			assertTrue(VeranstaltungsCtrlStudent.nehmeTeamEinladungAn(mapper.writeValueAsString(einladung1)));
			assertTrue(einladungGeloescht(einladung1));
			assertFalse(einladungGeloescht(einladung2));
			assertTrue(mitgliedIstBestaetigt(team1, stud2));
			assertFalse(mitgliedIstBestaetigt(team1, stud3));
			assertFalse(teamIstAngemeldet(team1));
			servicePool.addUser(stud2);
			currStud = stud2;
			team1.getMitglieder().add(stud1);
			team1String = mapper.writeValueAsString(team1);
			assertTrue(VeranstaltungsCtrlStudent.teamVerlassen(team1String, "" + currStud.getMatrNr().getMatrNr()));
			assertTrue(teamMitgliedGeloescht(stud2, team1));
			team1.getMitglieder().remove(stud2);
			assertTrue(teamAustrittsMeldungVorhanden(team1));

		}catch(AssertionError e){
			
			System.err.println("Fehler - testVerlasseTeamWaehrendAnmeldephase() - Team 1");
			System.err.println("========================================================");
			System.err.println(team1.toString());
			System.err.println("Absender: " + stud1.toString());
			System.err.println("Empfaenger: " + stud2.toString());
			System.err.println();
			e.printStackTrace();
			
		} catch (JsonProcessingException e) {
			
			e.printStackTrace();
			
		} catch (SQLException e) {
			
			e.printStackTrace();
		}
		
	}
	
	
	@Test
	public void test7AnmeldeProzedurEinladungAnnehmen(){							// 7.
		
		ObjectMapper mapper = new ObjectMapper();
		// Team 1
		Student stud1 = new Student(MatrikelNr.getMatrikelNr(1111112),"Clara", "Schulz");		// Absender
		Student stud2 = new Student(MatrikelNr.getMatrikelNr(1111113),"Achmed", "Swolyski");
		List<Student> mitgliederTeam1 = new ArrayList<>(Arrays.asList(stud1, stud2));
		Team team1 = new Team(null, 2, 3, mitgliederTeam1, -1, 5, "PRP2");
		
		// Team 2
		Student stud3 = new Student(MatrikelNr.getMatrikelNr(1111114),"James", "Bond");			// Absender
		Student stud4 = new Student(MatrikelNr.getMatrikelNr(1111121),"Natsu", "Dragneel");
		List<Student> mitgliederTeam2 = new ArrayList<>(Arrays.asList(stud3, stud4));
		Team team2 = new Team(null, 2, 2,  mitgliederTeam2, -1, 5, "PRP2");
		
		// Team 3																				// Absender
		Student stud5 = new Student(MatrikelNr.getMatrikelNr(1111122),"Patrick", "Schuller");
		Student stud6 = new Student(MatrikelNr.getMatrikelNr(1111123),"Sercy", "Lannister");
		List<Student> mitgliederTeam3 = new ArrayList<>(Arrays.asList(stud5, stud6));
		Team team3 = new Team(null, 2, 3, mitgliederTeam3, -1, 5, "PRP2");
		
		// Team 4																				// Absender
		Student stud7 = new Student(MatrikelNr.getMatrikelNr(1111124),"Bob", "McCabe");
		Student stud8 = new Student(MatrikelNr.getMatrikelNr(1111125),"Tim", "Brahms");
		List<Student> mitgliederTeam4 = new ArrayList<>(Arrays.asList(stud7, stud8));
		Team team4 = new Team(null, 2, 2, mitgliederTeam4, -1, 5, "PRP2");
		
		
		// Team 5																				
		Student stud9 = new Student(MatrikelNr.getMatrikelNr(1111120),"Johny", "McBeal");		// Absender
		Student stud10 = new Student(MatrikelNr.getMatrikelNr(1111115), "Patrick", "Baecker");
		
		List<Student> mitgliederTeam5 = new ArrayList<>(Arrays.asList(stud9, stud10));
		Team team5= new Team(null, 2, 2, mitgliederTeam5, -1, 5, "PRP2");
		
	
		
		// Team 1 anmelden -> erfolgreich
		// =================================================================================================
		try{
			
			servicePool.addUser(stud1);
			currStud = stud1;
			Student absender = (Student) servicePool.getUser(currStud.getMatrNr());
			String team1String = mapper.writeValueAsString(team1);
			assertTrue(VeranstaltungsCtrlStudent.sendeTeamEinladung(team1String, "" + currStud.getMatrNr().getMatrNr()));
			assertTrue(einladungenWurdenAngelegt(team1, absender));
			team1.setTeamID(getTeamID(team1));
			assertTrue(teamWurdeAngelegt(team1));
			Teameinladung einladung1 = getTeamEinladung(team1, stud1, stud2);
			assertTrue(VeranstaltungsCtrlStudent.nehmeTeamEinladungAn(mapper.writeValueAsString(einladung1)));
			assertTrue(einladungGeloescht(einladung1));
			assertTrue(mitgliedIstBestaetigt(team1, stud2));
			assertTrue(teamIstAngemeldet(team1));
			
			for(Student stud: team1.getMitglieder()){
				
				Gruppe gruppe = new Gruppe(team1.getFachkuerzel(), team1.getVorgGrpNr());
				gruppe.setProfessor(getProfessor(gruppe));
				assertTrue(hatUnbewerteteLeistung(stud, gruppe));
			}
			
		}catch(AssertionError e){
			
			System.err.println("Fehler - testAnmeldeProzedurEinladungAnnehmen() - Team 1");
			System.err.println("========================================================");
			System.err.println(team1.toString());
			System.err.println("Absender: " + stud1.toString());
			System.err.println("Empfaenger: " + stud2.toString());
			System.err.println();
			e.printStackTrace();
			
		} catch (JsonProcessingException e) {
			
			e.printStackTrace();
			
		} catch (SQLException e) {
			
			e.printStackTrace();
		}
		
		
		// Team 2 anmelden -> erfolgreich
		// =================================================================================================
		try{
			
			servicePool.addUser(stud3);
			currStud = stud3;
			String team2String = mapper.writeValueAsString(team2);
			assertTrue(VeranstaltungsCtrlStudent.sendeTeamEinladung(team2String, "" + currStud.getMatrNr().getMatrNr()));
			assertTrue(einladungenWurdenAngelegt(team2, stud3));
			team2.setTeamID(getTeamID(team2));
			assertTrue(teamWurdeAngelegt(team2));
			Teameinladung einladung2 = getTeamEinladung(team2, stud3, stud4);
			assertTrue(VeranstaltungsCtrlStudent.nehmeTeamEinladungAn(mapper.writeValueAsString(einladung2)));
			assertTrue(einladungGeloescht(einladung2));
			assertTrue(mitgliedIstBestaetigt(team2, stud4));
			assertTrue(teamIstAngemeldet(team2));
			
			for(Student stud: team2.getMitglieder()){
				
				Gruppe gruppe = new Gruppe(team2.getFachkuerzel(), team2.getVorgGrpNr());
				gruppe.setProfessor(getProfessor(gruppe));
				assertTrue(hatUnbewerteteLeistung(stud, gruppe));
			}
			
		}catch(AssertionError e){
			
			System.err.println("Fehler - testAnmeldeProzedurEinladungAnnehmen() - Team 2");
			System.err.println("========================================================");
			System.err.println(team2.toString());
			System.err.println("Absender: " + stud3.toString());
			System.err.println("Empfaenger: " + stud4.toString());
			System.err.println();
			e.printStackTrace();
			
		} catch (JsonProcessingException e) {
			
			e.printStackTrace();
			
		} catch (SQLException e) {
			
			e.printStackTrace();
		}
		
		// Team 5 vormerken -> erfolgreich
		// =================================================================================================
		try{
			Gruppe gruppe = new Gruppe("PRP2", 5);
			int anzTeamsVorher = servicePool.getVerAnzSrv().getTeamanzahl(gruppe);
			servicePool.addUser(stud9);
			currStud = stud9;
			String team5String = mapper.writeValueAsString(team5);
			assertTrue(VeranstaltungsCtrlStudent.sendeTeamEinladung(team5String, "" + currStud.getMatrNr().getMatrNr()));
			assertTrue(einladungenWurdenAngelegt(team5, stud9));
			team5.setTeamID(getTeamID(team5));
			assertTrue(teamWurdeAngelegt(team5));
			assertTrue(servicePool.getVerAnzSrv().getTeamanzahl(gruppe) == anzTeamsVorher);
			
			for(Student stud: team5.getMitglieder()){
				
				Gruppe gr = new Gruppe(team5.getFachkuerzel(), team5.getVorgGrpNr());
				gr.setProfessor(getProfessor(gruppe));
				assertFalse(hatUnbewerteteLeistung(stud, gr));
			}
			
		}catch(AssertionError e){
			System.err.println("Fehler - testAnmeldeProzedurEinladungAnnehmen() - Team 5 - vormerken");
			System.err.println("====================================================================");
			System.err.println(team5.toString());
			System.err.println();
			e.printStackTrace();
			
		} catch (JsonProcessingException e) {
			
			e.printStackTrace();
			
		} catch (SQLException e) {
			
			e.printStackTrace();
		}
		
		// Team 3 anmelden -> erfolgreich
		// =================================================================================================
		try{
			
			servicePool.addUser(stud5);
			currStud = stud5;
			String team3String = mapper.writeValueAsString(team3);
			assertTrue(VeranstaltungsCtrlStudent.sendeTeamEinladung(team3String, "" + currStud.getMatrNr().getMatrNr()));
			assertTrue(einladungenWurdenAngelegt(team3, stud5));
			team3.setTeamID(getTeamID(team3));
			assertTrue(teamWurdeAngelegt(team3));
			Teameinladung einladung3 = getTeamEinladung(team3, stud5, stud6);
			assertTrue(VeranstaltungsCtrlStudent.nehmeTeamEinladungAn(mapper.writeValueAsString(einladung3)));
			assertTrue(einladungGeloescht(einladung3));
			assertTrue(mitgliedIstBestaetigt(team3, stud6));
			assertTrue(teamIstAngemeldet(team3));
			
			for(Student stud: team3.getMitglieder()){
				
				Gruppe gr = new Gruppe(team3.getFachkuerzel(), team3.getVorgGrpNr());
				gr.setProfessor(getProfessor(gr));
				assertTrue(hatUnbewerteteLeistung(stud, gr));
			}
			
		}catch(AssertionError e){
			
			System.err.println("Fehler - testAnmeldeProzedurEinladungAnnehmen() - Team 3");
			System.err.println("========================================================");
			System.err.println(team2.toString());
			System.err.println("Absender: " + stud3.toString());
			System.err.println("Empfaenger: " + stud4.toString());
			System.err.println();
			e.printStackTrace();
			
		} catch (JsonProcessingException e) {
			
			e.printStackTrace();
			
		} catch (SQLException e) {
			
			e.printStackTrace();
		}
		
		
		// Team 5 gelöscht, da Team 3 vorher angemeldet
		try{
			
			team5.getMitglieder().add(stud9);
			assertTrue(gruppeVollMeldungenVorhanden(team5));
			assertTrue(teamGeloescht(team5));
			assertTrue(teamEinladungenGeloescht(team5));
			
			for(Student stud: team5.getMitglieder()){
				
				Gruppe gr = new Gruppe(team5.getFachkuerzel(), team5.getVorgGrpNr());
				gr.setProfessor(getProfessor(gr));
				assertFalse(hatUnbewerteteLeistung(stud, gr));
			}
			
		}catch (AssertionError e){
			
			System.err.println("Fehler - testAnmeldeProzedurEinladungAnnehmen() - Team 5 gelöscht");
			System.err.println("=================================================================");
			System.err.println(team5.toString());
			e.printStackTrace();
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		
		// Team 4 anmelden -> nicht erfolgreich
		// =================================================================================================
		try{
			
			servicePool.addUser(stud7);
			currStud = stud7;
			String team4String = mapper.writeValueAsString(team4);
			assertFalse(VeranstaltungsCtrlStudent.sendeTeamEinladung(team4String, "" + currStud.getMatrNr().getMatrNr()));
			
		}catch(AssertionError e){
			
			System.err.println("Fehler - testAnmeldeProzedurEinladungAnnehmen() - Team 4");
			System.err.println("========================================================");
			System.err.println(team2.toString());
			System.err.println("Absender: " + stud3.toString());
			System.err.println("Empfaenger: " + stud4.toString());
			System.err.println();
			e.printStackTrace();
			
		} catch (JsonProcessingException e) {
			
			e.printStackTrace();
		}
	}
	
	
	
	@Test
	public void test8GetAlleTeamsAktuellerStudent(){
		
		try{
			
			Student user = new Student(MatrikelNr.getMatrikelNr(1111118), "Bully", "Herbig");
			servicePool.addUser(user);
			currStud = user;
			List<Team> alleTeams = VeranstaltungsCtrlStudent.getAlleTeamsAktuellerStudent("" + currStud.getMatrNr().getMatrNr());
			
			for(Team team: alleTeams){
				
				assertTrue(!team.getMitglieder().isEmpty());
			}
			
			assertTrue(anzahlTeammitgliedEintraege(user) == alleTeams.size());
			
		}catch(AssertionError e){
			
		}catch(SQLException e1){
			
			
		}
	} 
	
	@Test
	public void test9SpeichereEinzelanmeldung(){	
		
		ObjectMapper mapper = new ObjectMapper();
		Student einzelanmelder = new Student(MatrikelNr.getMatrikelNr(1111127), "Max", "Mustermann");
		servicePool.addUser(einzelanmelder);
		currStud = einzelanmelder;
		List<String> fachkuerzel = servicePool.getDatenExSrv().getAlleFachkuerzel();
		List<Gruppe> volleGruppen = getGueltigeGruppen(getAlleGruppen(fachkuerzel));
		List<Gruppe> nichtVolleGruppen = servicePool.getStudentVerwSrv().getGruppenEinzel("Alle", "Praktikum", currStud.getMatrNr());
		volleGruppen.removeAll(nichtVolleGruppen);
		
		try{
			for(Gruppe g: nichtVolleGruppen){
				
				List<Team> teamsVorher = servicePool.getVerAnzSrv().getAllTeilnehmer(g);
				String grString = mapper.writeValueAsString(g);
				
				if(servicePool.getStudentVerwSrv().hatNochKeinTeam(einzelanmelder, g.getFachkuerzel())){
					assertTrue(VeranstaltungsCtrlStudent.speichereEinzelAnmeldung(grString, "" + currStud.getMatrNr().getMatrNr()));
					List<Team> teamsNachher = servicePool.getVerAnzSrv().getAllTeilnehmer(g);
					
					if(teamsVorher.size() == teamsNachher.size()){ // Kein neues Team wurde angelegt
						
						System.out.println("Kein Neues Team!");
						assertFalse(teamsVorher.equals(teamsNachher)); // Mitglieder haben sich verändert
						Team team = getTeamMitNeuemTeilnehmer(teamsNachher, einzelanmelder);
						assertTrue(team != null);
						assertTrue(team.getMitglieder().contains(einzelanmelder));
						assertTrue(mitgliedIstBestaetigt(team, einzelanmelder));
						assertTrue(teamIstAngemeldet(team));
						
					}else{
						
						System.out.println("Neues Team!");
						Team team = getTeamMitNeuemTeilnehmer(teamsNachher, einzelanmelder);
						assertTrue(team != null);
						assertTrue(team.getMitglieder().contains(einzelanmelder));
						assertTrue(servicePool.getVerAnzSrv().getMitglieder(team.getTeamID()).size() == 1);
						assertTrue(mitgliedIstBestaetigt(team, einzelanmelder));
						assertTrue(teamIstAngemeldet(team));
						
					}
					
					assertTrue(hatUnbewerteteLeistung(einzelanmelder, g));
				}else{
					assertFalse(VeranstaltungsCtrlStudent.speichereEinzelAnmeldung(grString, "" + currStud.getMatrNr().getMatrNr()));
				}
				

			}
			
		} catch (AssertionError e){
			
			e.printStackTrace();
			fail();
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	
	/* HELPERS */
	/* ================================================================================================================= */

	private Team getTeamMitNeuemTeilnehmer(List<Team> teamsNachher, Student einzelanmelder) {
		
		for(Team team: teamsNachher){
			
			for(Student mg: team.getMitglieder()){
				
				if(mg.equals(einzelanmelder)){
					
					return team;
				}
				
			}
		}
		
		return null;
	}

	private List<Gruppe> getAlleGruppen(List<String> fachkuerzel) {
		List<Gruppe> gruppen = new ArrayList<>();
		
		for(String fk: fachkuerzel){
			
			Veranstaltung va = new Praktikum();
			va.setFach(new Fach(fk));
			gruppen.addAll(servicePool.getVerAnzSrv().getAllGruppen(va));
		}
		
		return gruppen;
	}

	private int anzahlTeammitgliedEintraege(Student user) throws SQLException {

		String qry = "select count(*) as count from teammitglied where matrNr = ?";
		PreparedStatement stmt = connection.prepareStatement(qry);
		stmt.setInt(1, user.getMatrNr().getMatrNr());
		ResultSet rs = stmt.executeQuery();
		int count = 0;
		while(rs.next()){
			
			count = rs.getInt("count");
		}
		stmt.close();
		
		
		return count;
		
	}

	private TeamID neueTeamId() throws SQLException {
		
		List<TeamID> teamIds = new ArrayList<>();
		String qry = "select teamid from team";
		PreparedStatement stmt = connection.prepareStatement(qry);
		ResultSet rs = stmt.executeQuery();
		
		while(rs.next()){
			teamIds.add(TeamID.getTeamID(rs.getString("teamid")));
		}
		stmt.close();
		
		int maxTeamId = Integer.MIN_VALUE;
		
		for(TeamID tid: teamIds){
			
			int id = Integer.parseInt(tid.getId());
			
			if(id > maxTeamId){
				maxTeamId = id;
			}
		}
		
		return TeamID.getTeamID("" + (++maxTeamId));
		
	}

	private TeamID getTeamID(Team team1) throws SQLException {
		
		String qry = "select teamid from Team where fkuerzel = ? and VorgGrpNr = ?";
		PreparedStatement stmt = connection.prepareStatement(qry);
		stmt.setString(1, team1.getFachkuerzel());
		stmt.setInt(2, team1.getVorgGrpNr());
		ResultSet rs = stmt.executeQuery();
		TeamID id = null;
		while(rs.next()){
			
			id = TeamID.getTeamID(rs.getString("teamid"));
		}
		stmt.close();
		return id;
		
	}

	private boolean mitgliedIstBestaetigt(Team team1, Student student) throws SQLException {
		
		String qry = "select bestaetigt from teammitglied where matrnr = ? and teamid = ?";
		Connection connection = DBConnector.getConnection();
		PreparedStatement stmt = connection.prepareStatement(qry);
		stmt.setInt(1, student.getMatrNr().getMatrNr());
		stmt.setInt(2, Integer.parseInt(team1.getTeamID().getId()));
		ResultSet rs = stmt.executeQuery();
		String bestaetigt = null;
		while(rs.next()){
			
			bestaetigt = rs.getString("bestaetigt");
		}
		stmt.close();
		
		return Boolean.parseBoolean(bestaetigt);
		
	}

	private boolean teamHatMitglieder(Team team1) throws SQLException {
		
		String qry = "select count(*) as count from teammitglied where teamid = ?";
		PreparedStatement stmt = connection.prepareStatement(qry);
		stmt.setInt(1, Integer.parseInt(team1.getTeamID().getId()));
		ResultSet rs = stmt.executeQuery();
		int count = 0;
		while(rs.next()){
			count = rs.getInt("count");
		}
		stmt.close();
		
		return count == team1.getMitglieder().size() + 1;
	}

	private boolean einladungenWurdenAngelegt(Team team, Student absender) {
		
		team.getMitglieder().remove(absender);
		
		for(int i = 0; i < team.getMitglieder().size(); i++){
			
			Teameinladung einladung = new Teameinladung(absender, 
					team.getMitglieder().get(i), new Date(), team, 
					false, null, MeldungTyp.TEAMEINL);
			
			Student stud = team.getMitglieder().get(i);
			servicePool.removeUser(absender.getMatrNr());
			servicePool.addUser(stud);
			currStud = stud;
			List<Meldung> meldungen = VeranstaltungsCtrlStudent.holeAlleMeldungen("" + currStud.getMatrNr().getMatrNr());
			servicePool.removeUser(currStud.getMatrNr());
			servicePool.addUser(absender);
			currStud = absender;
			
			try{
				
				assertTrue(meldungen.contains(einladung));
				
				assertTrue(einladung.equals(meldungen.get(meldungen.indexOf(einladung))));

			}catch (AssertionError e){
				
				if(einladung.getTeam().getTeamID() != null){
					
					System.err.println("Fehler testSendeTeamEinladung");
					System.err.println("=============================");
					System.err.println(einladung.toString());
					System.err.println();
					System.err.println("-----------------------------");
					System.err.println("Meldungen 1");
					System.err.println("=============================");
					
					for(Meldung m: meldungen){
						System.err.println(m.toString());
					}
					
					System.err.println("Meldungen 2");
					System.err.println("=============================");
					
					for(Meldung m: meldungen){
						System.err.println(m.toString());
					}
					
					System.err.println("-----------------------------");
					
					e.printStackTrace();
				}

				
				return false;
			}
			
		}
		return true;
	}

	private boolean teamWurdeAngelegt(Team team1) throws SQLException {
		
		String qry = "select * from team where teamid = ?";
		
		PreparedStatement stmt = connection.prepareStatement(qry);
		stmt.setInt(1, Integer.parseInt(team1.getTeamID().getId()));
		ResultSet rs = stmt.executeQuery();
		TeamID teamId = null;
		String fachkuerzel = null;
		int minMitglieder = 0;
		int maxMitglieder = 0;
		int grpNr = 0;
		
		while(rs.next()){
			
			teamId = TeamID.getTeamID(rs.getString("teamid"));
			fachkuerzel = rs.getString("fkuerzel");
			minMitglieder = rs.getInt("minMitglieder");
			maxMitglieder = rs.getInt("maxMitglieder");
			grpNr = rs.getInt("VorgGrpNr");
			
		}
		
		stmt.close();
		
		return team1.getFachkuerzel().equals(fachkuerzel)
				&& (teamId != null && team1.getTeamID().equals(teamId))
				&& team1.getMinTeiln() == minMitglieder
				&& team1.getMaxTeiln() == maxMitglieder
				&& team1.getVorgGrpNr() == grpNr;
	}	


	private Professor getProfessor(Gruppe gruppe) throws SQLException {
		String qry = "select vorname, nachname, maid from professor where maid in (select profmaid from gruppe where grpNr = ? and fkuerzel = ?)";
		PreparedStatement stmt = connection.prepareStatement(qry);
		stmt.setInt(1, gruppe.getGrpNr());
		stmt.setString(2, gruppe.getFachkuerzel());
		ResultSet rs = stmt.executeQuery();
		String vorname = null;
		String nachname = null;
		MAID maid = null;
		while(rs.next()){
			vorname = rs.getString("vorname");
			nachname = rs.getString("nachname");
			maid = MAID.getMAID(rs.getInt("maid"));
		}
		
		stmt.close();
		
		return new Professor(maid, vorname, nachname);
	}

	private boolean teamEinladungenGeloescht(Team team5) throws SQLException {
		
		String qry = "select count(*) as count from Meldung where teamid = ? and typ = ?";
		PreparedStatement stmt = connection.prepareStatement(qry);
		stmt.setInt(1, Integer.parseInt(team5.getTeamID().getId()));
		stmt.setString(2, MeldungTyp.TEAMEINL);
		ResultSet rs = stmt.executeQuery();
		int count = 0;
		while(rs.next()){
			count = rs.getInt("count");
		}		
		stmt.close();
		
		return count == 0;
	}
	
	
	private boolean hatUnbewerteteLeistung(Student student, Gruppe gr) throws SQLException {
		
		Leistung leistung = new Leistung(new Fach(gr.getFachkuerzel()), student, gr.getProfessor());
		String qry = "select * from bewertung where maid = ? and fkuerzel = ? and matrnr = ?";
		PreparedStatement stmt = connection.prepareStatement(qry);
		stmt.setInt(1, gr.getProfessor().getMaID().getId());
		stmt.setString(2, gr.getFachkuerzel());
		stmt.setInt(3, student.getMatrNr().getMatrNr());
		ResultSet rs = stmt.executeQuery();
		MAID profMaid = null;
		MatrikelNr matrNr = null;
		String fkuerzel = null;
		java.sql.Date datumPVL = null;
		java.sql.Date datumNote = null;
		String pvl = null;
		String note = null;
		
		while(rs.next()){
			
			profMaid = MAID.getMAID(rs.getInt("maid"));
			matrNr = MatrikelNr.getMatrikelNr(rs.getInt("matrnr"));
			fkuerzel = rs.getString("fkuerzel");
			datumPVL = rs.getDate("datumPVL");
			datumNote = rs.getDate("datumNote");
			pvl = rs.getString("pvl");
			note = rs.getString("note");
		}
		
		stmt.close();
		
		if(profMaid == null && matrNr == null){
			return false;
		}
		
		return profMaid.equals(leistung.getProfessor().getMaID()) 
				&& matrNr.equals(leistung.getStudent().getMatrNr())
				&& fkuerzel.equals(leistung.getFach().getFachKuerzel())
				&& datumPVL == null
				&& datumNote == null
				&& Boolean.parseBoolean(pvl) == leistung.isPvl()
				&& note == null;
	}

	@SuppressWarnings("unused")
	private int anzahlEinzelAnmeldungen(Student student) throws SQLException {
		
		String qry = "select count(*) as count from einzelanmeldung where matrnr = ?";
		PreparedStatement stmt = connection.prepareStatement(qry);
		stmt.setInt(1, student.getMatrNr().getMatrNr());
		ResultSet rs = stmt.executeQuery();
		int count = 0;
		while(rs.next()){
			
			count = rs.getInt("count");
		}
		
		stmt.close();
		return count;
		
	}

	private List<Gruppe> getGueltigeGruppen(List<Gruppe> alleGruppen) {
		
		List<Gruppe> gruppen = new ArrayList<>();
		List<String> fachkuerzel = new ArrayList<>();
		
		for(Gruppe gr: alleGruppen){
			
			if(!fachkuerzel.contains(gr.getFachkuerzel())){
				
				fachkuerzel.add(gr.getFachkuerzel());
			}
		}
		
		
		for(Gruppe gr: alleGruppen){
			
			for(String fk: fachkuerzel){
				
				if(gr.getFachkuerzel().equals(fk)
						&& hatNochkeineGruppeMitFachkuerzel(gruppen, fk)){
					
					gruppen.add(gr);
				}
			}
		}

		
		return gruppen;
	}

	private boolean hatNochkeineGruppeMitFachkuerzel(List<Gruppe> gruppen2, String fk) {
		
		for(Gruppe gr: gruppen2){
			
			if(gr.getFachkuerzel().equals(fk)){
				
				return false;
			}
		}
		
		return true;
	}

	private boolean teamAustrittsMeldungVorhanden(Team team1) throws SQLException {
		
//		List<Meldung> austrittsMeldungen = new ArrayList<>();
//		List<Meldung> alleMeldungen = new ArrayList<>();

		
		String qry = "select count(*) as count from Meldung where typ = ? and teamid = ?";
		PreparedStatement stmt = connection.prepareStatement(qry);
		stmt.setString(1, MeldungTyp.TEAMAUSTR);
		stmt.setInt(2, Integer.parseInt(team1.getTeamID().getId()));
		ResultSet rs = stmt.executeQuery();
		int count = 0;
		
		// ALT
		
//		String qry = "select * from Meldung where typ = ?";
//		PreparedStatement stmt = connection.prepareStatement(qry);
//		stmt.setString(1, MeldungTyp.TEAMAUSTR);
		
//		while(rs.next()){
//					
//			Student absender = getStudent(MatrikelNr.getMatrikelNr(rs.getInt("absender")));
//			Student empfaenger = getStudent(MatrikelNr.getMatrikelNr(rs.getInt("empfaenger")));
//			Date versandDatum = new Date(rs.getDate("versanddatum").getTime());
//			Meldung austrittsMeldung = new TeamAustrittsmeldung(absender, empfaenger, versandDatum, team1, MeldungTyp.TEABLEHN);
//			austrittsMeldungen.add(austrittsMeldung);	
//			servicePool.setUser(empfaenger);
//			alleMeldungen.addAll(VeranstaltungsCtrlStudent.holeAlleMeldungen());
//					
//		}
		
		while(rs.next()){
			
			count = rs.getInt("count");
		}
		
		stmt.close();
		
		return count > 0; // Austrittmeldungen sind vorhanden
	}

	private boolean teamAblehnungenVorhanden(Team team1) throws SQLException {
		
//		List<Meldung> ablehnungen = new ArrayList<>();
//		List<Meldung> alleMeldungen = new ArrayList<>();
		String qry = "select count(*) as count from Meldung where typ = ? and teamid = ?";
		PreparedStatement stmt = connection.prepareStatement(qry);
		stmt.setString(1, MeldungTyp.TEABLEHN);
		stmt.setInt(2, Integer.parseInt(team1.getTeamID().getId()));
		int count = 0;
		
		// ALT
//		String qry = "select * from Meldung where typ = ?";
//		PreparedStatement stmt = connection.prepareStatement(qry);
//		stmt.setString(1, MeldungTyp.TEABLEHN);
		
				
//		while(rs.next()){
//					
//			Student absender = getStudent(MatrikelNr.getMatrikelNr(rs.getInt("absender")));
//			Student empfaenger = getStudent(MatrikelNr.getMatrikelNr(rs.getInt("empfaenger")));
//			Date versandDatum = new Date(rs.getDate("versanddatum").getTime());
//			Meldung ablehnung = new TEAblehnung(absender, empfaenger, versandDatum, team1, MeldungTyp.TEABLEHN);
//			ablehnungen.add(ablehnung);	
//			servicePool.setUser(empfaenger);
//			alleMeldungen.addAll(VeranstaltungsCtrlStudent.holeAlleMeldungen());
//					
//		}
		ResultSet rs = stmt.executeQuery();
		while(rs.next()){
			count = rs.getInt("count");
		}
		
		stmt.close();
		
		return count == 1; // genau eine muss vorhanden sein
				
		
		
	}

	private boolean teamEinladungGeloescht(Team team1, Student stud1) throws SQLException {
		
		String qry = "select count(*) as count from meldung where teamid = ? and empfaenger = ? and fkuerzel = ?";
		PreparedStatement stmt = connection.prepareStatement(qry);
		stmt.setInt(1, Integer.parseInt(team1.getTeamID().getId()));
		stmt.setInt(2, stud1.getMatrNr().getMatrNr());
		stmt.setString(3, team1.getFachkuerzel());
		ResultSet rs = stmt.executeQuery();
		int count = 0;
		
		while(rs.next()){
			
			count = rs.getInt("count");
		}
		stmt.close();
		return count == 0;
	}

	private boolean teamMitgliedGeloescht(Student stud1, Team team1) throws SQLException {
		
		String qry = "select matrnr from teammitglied where teamid = ? and matrnr = ?";
		PreparedStatement stmt = connection.prepareStatement(qry);
		stmt.setInt(1, Integer.parseInt(team1.getTeamID().getId()));
		stmt.setInt(2, stud1.getMatrNr().getMatrNr());
		ResultSet rs = stmt.executeQuery();
		MatrikelNr matrNr = null;
		
		while(rs.next()){
			
			matrNr = MatrikelNr.getMatrikelNr(rs.getInt("matrnr"));
		}
		stmt.close();
		return matrNr == null;
		
		
		
	}

	private boolean teamGeloescht(Team team5) throws SQLException {
		
		String qry1 = "select count(*) as count1 from teammitglied where teamid = ?";
		String qry2 = "select count(*) as count2 from team where teamid = ?";
		PreparedStatement stmt = connection.prepareStatement(qry1);
		stmt.setInt(1, Integer.parseInt(team5.getTeamID().getId()));
		ResultSet rs1 = stmt.executeQuery();
		int countMitgl = 0;
		int countTeam = 0;
		
		while(rs1.next()){
			
			countMitgl = rs1.getInt("count1");
		}
		
		stmt.close();
		
		PreparedStatement stmt2 = connection.prepareStatement(qry2);
		stmt2.setInt(1, Integer.parseInt(team5.getTeamID().getId()));
		ResultSet rs2 = stmt2.executeQuery();
		
		while(rs2.next()){
			countTeam = rs2.getInt("count2");
		}
		
		stmt2.close();
		
		return countMitgl == 0 && countTeam == 0;
		
		
		
	}

	private boolean gruppeVollMeldungenVorhanden(Team team5) throws SQLException {
		
		Student absender = new Student(MatrikelNr.getMatrikelNr(99999999), "SYSTEM", "SYSTEM");
		List<Meldung> meldungenExpected = new ArrayList<>();
		List<Meldung> meldungenResult = new ArrayList<>();
		String qry = "select * from Meldung where teamid = ? and absender = ? and empfaenger = ?";
		
		for(Student stud: team5.getMitglieder()){
				
			PreparedStatement stmt = connection.prepareStatement(qry);
			stmt.setInt(1, Integer.parseInt(team5.getTeamID().getId()));
			stmt.setInt(2, absender.getMatrNr().getMatrNr());
			stmt.setInt(3, stud.getMatrNr().getMatrNr());
			ResultSet rs = stmt.executeQuery();
			Date versandDatum = new Date(new java.sql.Date(System.currentTimeMillis()).getTime());
			Meldung gruppeVoll = new GruppevollMeldung(absender, stud, versandDatum, team5, MeldungTyp.GRUPPE_VOLL);
			meldungenExpected.add(gruppeVoll);
				
			while(rs.next()){
					
				Student abs = getStudent(MatrikelNr.getMatrikelNr(rs.getInt("absender")));
				Student empf = getStudent(MatrikelNr.getMatrikelNr(rs.getInt("empfaenger")));
				Meldung grVoll = new GruppevollMeldung(abs, empf, versandDatum, team5, MeldungTyp.GRUPPE_VOLL);
				meldungenResult.add(grVoll);
					
			}
			
			stmt.close();
		}
			
		return meldungenExpected.equals(meldungenResult);
	}

	private Student getStudent(MatrikelNr matrikelNr) throws SQLException {
		
		String qry = "select vorname, nachname from Student where matrnr = ?";
		PreparedStatement stmt = connection.prepareStatement(qry);
		stmt.setInt(1, matrikelNr.getMatrNr());
		ResultSet rs = stmt.executeQuery();
		Student stud = null;
		
		while(rs.next()){
			
			stud = new Student(matrikelNr, rs.getString("vorname"), rs.getString("nachname"));
		}
		stmt.close();
		
		return stud;
		
	}

	private boolean teamIstAngemeldet(Team team1) throws SQLException {
		
		String qry = "select grpNr from team where teamid = ?";
		PreparedStatement stmt = connection.prepareStatement(qry);
		stmt.setInt(1, Integer.parseInt(team1.getTeamID().getId()));
		ResultSet rs = stmt.executeQuery();
		int grpNr = 0;
		
		while(rs.next()){
			
			grpNr = rs.getInt("grpNr");
		}
		
		stmt.close();
		
		return grpNr != 0;
	}

	private boolean einladungGeloescht(Teameinladung einladung1) throws SQLException {
		
		Student absender = (Student) einladung1.getAbsender();
		Student empfaenger = (Student) einladung1.getEmpfaenger();
		String qry = "select * from meldung where absender = ? and empfaenger = ? and teamid = ?";
		PreparedStatement stmt = connection.prepareStatement(qry);
		stmt.setInt(1, absender.getMatrNr().getMatrNr());
		stmt.setInt(2, empfaenger.getMatrNr().getMatrNr());
		stmt.setInt(3, Integer.parseInt(einladung1.getTeam().getTeamID().getId()));
		ResultSet rs = stmt.executeQuery();
		
		if(rs.next()){
			stmt.close();
			return false;
		}
		stmt.close();
		return true;
	}

	private Teameinladung getTeamEinladung(Team team1, Student absender, Student empfaenger) throws SQLException {
		
		String qry = "select * from meldung where absender = ? and empfaenger = ? and teamid = ?";
		PreparedStatement stmt = connection.prepareStatement(qry);
		stmt.setInt(1, absender.getMatrNr().getMatrNr());
		stmt.setInt(2, empfaenger.getMatrNr().getMatrNr());
		stmt.setInt(3, Integer.parseInt(team1.getTeamID().getId()));
		ResultSet rs = stmt.executeQuery();
		Teameinladung einladung = null;
		
		while(rs.next()){
			
			Date versandDatum = new Date(rs.getDate("versanddatum").getTime());
			String fkuerzel = rs.getString("fkuerzel");
			TeamID teamId = TeamID.getTeamID(rs.getString("teamid"));
			boolean bestaetigt = Boolean.parseBoolean(rs.getString("bestaetigt"));
			Date sqlDate = rs.getDate("bestaetigungsdatum");
			Date bestaetDatum = null;
			if(sqlDate != null){
				bestaetDatum = new Date(sqlDate.getTime());
			}
			assertTrue(team1.getFachkuerzel().equals(fkuerzel));
			assertTrue(team1.getTeamID().equals(teamId));
			einladung = new Teameinladung(absender, empfaenger, versandDatum, team1, bestaetigt, bestaetDatum, MeldungTyp.TEAMEINL);
			
		}
		stmt.close();
		return einladung;
	}

	private static void setUp() {
		
		Veranstaltung prp1 = new Praktikum();
		prp1.setFach(new Fach("PRP1"));
		
		Veranstaltung prp2 = new Praktikum();
		prp2.setFach(new Fach("PRP2"));
		
		Veranstaltung mgp = new Praktikum();
		mgp.setFach(new Fach("MGP"));
		
		// Studenten nicht gefiltert
		// Mitglieder
		Student stud2 = new Student(MatrikelNr.getMatrikelNr(1111112),"Clara", "Schulz");
		Student stud3 = new Student(MatrikelNr.getMatrikelNr(1111113),"Achmed", "Swolyski");
		Student stud4 = new Student(MatrikelNr.getMatrikelNr(1111114),"James", "Bond");
		Student stud5 = new Student(MatrikelNr.getMatrikelNr(1111121),"Natsu", "Dragneel");
		Student stud6 = new Student(MatrikelNr.getMatrikelNr(1111122),"Patrick", "Schuller");
		Student stud7 = new Student(MatrikelNr.getMatrikelNr(1111123),"Sercy", "Lannister");
		Student stud8 = new Student(MatrikelNr.getMatrikelNr(1111124),"Bob", "McCabe");
		Student stud9 = new Student(MatrikelNr.getMatrikelNr(1111125),"Tim", "Brahms");
		Student stud10 = new Student(MatrikelNr.getMatrikelNr(1111120),"Johny", "McBeal");
		// Student stud11 = new Student(MatrikelNr.getMatrikelNr(1111115), "Patrick", "Baecker");
		
		studenten.add(stud2);
		studenten.add(stud3);
		studenten.add(stud4);
		studenten.add(stud5);
		studenten.add(stud6);
		studenten.add(stud7);
		studenten.add(stud8);
		studenten.add(stud9);
		studenten.add(stud10);
		// PRP1
		studentenHabenTeam.add(stud5);
		studentenHabenTeam.add(stud8);
		
		
		// MGP
		studentenHabenPVL.add(stud2);
		studentenHabenPVL.add(stud3);
		
		
		gruppen.addAll(servicePool.getVerAnzSrv().getAllGruppen(prp1));
		gruppen.addAll(servicePool.getVerAnzSrv().getAllGruppen(prp2));
		gruppen.addAll(servicePool.getVerAnzSrv().getAllGruppen(mgp));
		
		// Gruppen VAs schon erledigt (user)
		getGruppenMitStudentPVL();
		
		
	}

	@SuppressWarnings("unused")
	private static List<Gruppe> getNichtVolleGruppen() {

		List<Gruppe> nichtVolleGruppen = new ArrayList<>();
		
		for(Gruppe gr: gruppen){
			
			if(gr.getAnzTeams() < gr.getMaxTeams()){
				
				nichtVolleGruppen.add(gr);
			}else if(gr.getAnzTeams() >= gr.getMaxTeams()){
				
				List<Team> teams = servicePool.getVerAnzSrv().getAllTeilnehmer(gr);
				
				for(Team team: teams){
					
					if(team.getMitglieder().size() < team.getMaxTeiln()){
						nichtVolleGruppen.add(gr);
					}
				}
				
			}
		}
		return nichtVolleGruppen;
	}

	@SuppressWarnings("unused")
	private static List<Gruppe> getVolleGruppen(List<Gruppe> gruppen) {

		List<Gruppe> volleGruppen = new ArrayList<>();
		
		for(Gruppe gr: gruppen){
			
			if(gr.getAnzTeams() >= gr.getMaxTeams()){
				List<Team> teams = servicePool.getVerAnzSrv().getAllTeilnehmer(gr);
				for(Team team: teams){
					
					if(team.getMitglieder().size() >= team.getMaxTeiln()){
						
						volleGruppen.add(gr);
					}
				}
				
			}
		}
		return volleGruppen;
	}

	private static void getGruppenMitStudentPVL() {
		
		for(Gruppe gr: gruppen){
			if(gr.getFachkuerzel().equals("PRP1") 
				|| gr.getFachkuerzel().equals("MGP")){
				
				gruppenMitPVL.add(gr);
				
			}
		}
		
	}
	
	private static void loescheBewertungen() {
		
		String qry = "delete Bewertung";
		try {
			PreparedStatement stmt = connection.prepareStatement(qry);
			stmt.executeUpdate();
			stmt.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		
	}

	
    @AfterClass
    public static void setzeDatenWiederZurueck() {
    	
    	List<String> folders = DatenImExportController.holeBackupDateien();
    	Collections.sort(folders);
    	DatenImExportController.backupWiederherstellen(folders.get(0));
    }
}
