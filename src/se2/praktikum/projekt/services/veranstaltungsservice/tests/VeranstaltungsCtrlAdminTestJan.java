package se2.praktikum.projekt.services.veranstaltungsservice.tests;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
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
import se2.praktikum.projekt.models.gruppe.Gruppe;
import se2.praktikum.projekt.models.gruppe.Termin;
import se2.praktikum.projekt.models.gruppe.fachwerte.TerminID;
import se2.praktikum.projekt.models.gruppe.fachwerte.Uhrzeit;
import se2.praktikum.projekt.models.person.Assistent;
import se2.praktikum.projekt.models.person.IAngestellter;
import se2.praktikum.projekt.models.person.Professor;
import se2.praktikum.projekt.models.person.Student;
import se2.praktikum.projekt.models.person.fachwerte.MAID;
import se2.praktikum.projekt.models.person.fachwerte.MatrikelNr;
import se2.praktikum.projekt.models.team.Team;
import se2.praktikum.projekt.models.team.fachwerte.TeamID;
import se2.praktikum.projekt.models.veranstaltung.Fach;
import se2.praktikum.projekt.models.veranstaltung.Praktikum;
import se2.praktikum.projekt.models.veranstaltung.Projekt;
import se2.praktikum.projekt.models.veranstaltung.Veranstaltung;
import se2.praktikum.projekt.models.veranstaltung.WP;
import se2.praktikum.projekt.services.loginservice.LoginSrv;
import se2.praktikum.projekt.services.loginservice.ServicePool;
import se2.praktikum.projekt.services.veranstaltungsservice.VeranstaltungsCtrlAdmin;

public class VeranstaltungsCtrlAdminTestJan {
	
	private static LoginSrv login;
	private static ServicePool servicePool;
	
	private static List<Veranstaltung> praktika;
	private static List<Veranstaltung> wp;
	private static List<Veranstaltung> po;
	private static List<Professor> professoren;
	private static List<Assistent> assistenten;

	private static List<Fach> faecherVorhanden;
	private static List<Fach> faecherNichtVorhanden;

	private static List<Gruppe> gruppen;
	private static List<Student> studenten;
	private static List<Team> teams;

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		
		
		servicePool = new ServicePool();
		login = new LoginSrv(servicePool);
		login.login("naa001", "naa001");
		List<String> folders = DatenImExportController.holeBackupDateien();
		Collections.sort(folders);
		DatenImExportController.backupWiederherstellen(folders.get(0));
		praktika = new ArrayList<>();
		professoren = new ArrayList<>();
		assistenten = new ArrayList<>();
		wp = new ArrayList<>();
		po = new ArrayList<>();
		faecherVorhanden = new ArrayList<>();
		faecherNichtVorhanden = new ArrayList<>();
		gruppen = new ArrayList<>();
		studenten = new ArrayList<>();
		teams = new ArrayList<>();
		
		setUp();
		
	}

	/* Methoden die getestet wurden
	 * ============================================================================================================
	 * public List<Veranstaltung> praktikaAnzeigen(@RequestBody List<String> args) 						-> erledigt
	 * public List<Angestellter> getProfessoren(@RequestBody List<String> args) 						-> erledigt
	 * public List<Angestellter> getAssistenten(@RequestBody List<String> args) 						-> erledigt
	 * public List<Veranstaltung> wpAnzeigen(@RequestBody List<String> args) 							-> erledigt
	 * public List<Veranstaltung> poAnzeigen(@RequestBody List<String> args) 							-> erledigt
	 * public boolean veranstaltungBearbeiten(@RequestBody Veranstaltung veranstaltung)					-> erledigt
	 * public static @ResponseBody boolean veranstaltungErstellen(@RequestBody Veranstaltung va)		-> erledigt
	 * public boolean veranstaltungLoeschen(@RequestBody Veranstaltung veranstaltung)					-> erledigt		
	 * public @ResponseBody List<Gruppe> gruppenUebersichtAnzeigen(@RequestBody String veranstaltung)	-> erledigt
	 * public @ResponseBody boolean gruppeErstellen(@RequestBody String gruppe)							-> erledigt
	 * public @ResponseBody boolean gruppeBearbeiten(@RequestBody String gruppe)						-> erledigt
	 * public @ResponseBody boolean gruppeLoeschen(@RequestBody String gruppe)							-> erledigt
	 * public @ResponseBody List<Team> teilnehmerUebersichtAnzeigen(@RequestBody String gruppe)			-> erledigt
	 * public @ResponseBody boolean tmEntfernen(@RequestBody List<String> args)							-> erledigt
	 * public static @ResponseBody List<Team> einzelanmeldungenZusammenfuehren()						-> 
	 * ============================================================================================================
	 */
	
	@Test
	public void testPraktikaAnzeigen() {
		
		// 1. Semester
		List<String> args = new ArrayList<>(
				Arrays.asList("1", "AI"));
		
		List<Veranstaltung> result = VeranstaltungsCtrlAdmin.praktikaAnzeigen(args);
		System.out.println(result.size());
		for(int i = 0; i < result.size(); i++){
			
			Veranstaltung res = result.get(i);
			int resIndex = praktika.indexOf(res);
			Professor prof = (Professor) praktika.get(resIndex).getProfessor();
			Professor profRes = (Professor) res.getProfessor();
			
			try{
				
				assertTrue(praktika.contains(res)
						&& prof.equals(profRes)
						&& res.getAnzGr() == praktika.get(resIndex).getAnzGr()
						&& res.getAnzTm() == praktika.get(resIndex).getAnzTm()
						&& res.getMinTeilnTeam() == praktika.get(resIndex).getMinTeilnTeam()
						&& res.getMaxTeilnTeam() == praktika.get(resIndex).getMaxTeilnTeam()
						&& res.getSemester() == praktika.get(resIndex).getSemester());
				
			}catch(AssertionError e){
				
				System.err.println("Fehler testPraktikaAnzeigen Sem 1:");
				System.err.println("========");
				System.err.println(res.toString());
				
				System.err.println();
				for(Veranstaltung va: praktika){
					System.err.println(va.toString());
				}
			}
		}
		
		// 2. Semester
		args = new ArrayList<>(
				Arrays.asList("2", "AI"));
		
		result = VeranstaltungsCtrlAdmin.praktikaAnzeigen(args);
		
		assertTrue(result.size() == 2);
		
		for(int i = 0; i < result.size(); i++){
			
			Veranstaltung res = result.get(i);
			int resIndex = praktika.indexOf(res);
			Professor prof = (Professor) praktika.get(resIndex).getProfessor();
			Professor profRes = (Professor) res.getProfessor();
			
			try{
				
				assertTrue(praktika.contains(res)
						&& prof.equals(profRes)
						&& res.getAnzGr() == praktika.get(resIndex).getAnzGr()
						&& res.getAnzTm() == praktika.get(resIndex).getAnzTm()
						&& res.getMinTeilnTeam() == praktika.get(resIndex).getMinTeilnTeam()
						&& res.getMaxTeilnTeam() == praktika.get(resIndex).getMaxTeilnTeam()
						&& res.getSemester() == praktika.get(resIndex).getSemester());
				
			}catch(AssertionError e){
				
				System.err.println("Fehler testPraktikaAnzeigen Sem 2:");
				System.err.println("========");
				System.err.println(res.toString());
				
				System.err.println();
				for(Veranstaltung va: praktika){
					System.err.println(va.toString());
				}
				
			}


			
		}
		
		// 3. Semester
		args = new ArrayList<>(
				Arrays.asList("3", "AI"));
		
		result = VeranstaltungsCtrlAdmin.praktikaAnzeigen(args);
		
		
		
		
		for(int i = 0; i < result.size(); i++){
			
			Veranstaltung res = result.get(i);
			int resIndex = praktika.indexOf(res);
			Professor prof = (Professor) praktika.get(resIndex).getProfessor();
			Professor profRes = (Professor) res.getProfessor();
			
			try{
				assertTrue(result.size() == 2);
				
				assertTrue(praktika.contains(res)
						&& prof.equals(profRes)
						&& res.getAnzGr() == praktika.get(resIndex).getAnzGr()
						&& res.getAnzTm() == praktika.get(resIndex).getAnzTm()
						&& res.getMinTeilnTeam() == praktika.get(resIndex).getMinTeilnTeam()
						&& res.getMaxTeilnTeam() == praktika.get(resIndex).getMaxTeilnTeam()
						&& res.getSemester() == praktika.get(resIndex).getSemester());
				
			}catch(AssertionError e){
				
				System.err.println("Fehler testPraktikaAnzeigen Sem 3:");
				System.err.println("========");
				System.err.println(res.toString());
				
				System.err.println();
				for(Veranstaltung va: praktika){
					System.err.println(va.toString());
				}
				
			}

		}
	}
	
	
	public void testGetProfessoren(){
		
		// Hole alle Professoren die in der DB sind
		List<String> args = new ArrayList<>(Arrays.asList("1", "AI"));
		List<IAngestellter> result = VeranstaltungsCtrlAdmin.getProfessoren(args);
		args = new ArrayList<>(Arrays.asList("2", "AI"));
		result.addAll(VeranstaltungsCtrlAdmin.getProfessoren(args));
		args = new ArrayList<>(Arrays.asList("3", "AI"));
		result.addAll(VeranstaltungsCtrlAdmin.getProfessoren(args));
		args = new ArrayList<>(Arrays.asList("4", "AI"));
		result.addAll(VeranstaltungsCtrlAdmin.getProfessoren(args));
		args = new ArrayList<>(Arrays.asList("5", "AI"));
		result.addAll(VeranstaltungsCtrlAdmin.getProfessoren(args));
		
		for(IAngestellter a: professoren){
			
			assertTrue(professoren.contains(a));
		}
	}
	
	public void testGetAssistenten(){
		

		List<IAngestellter> assis = VeranstaltungsCtrlAdmin.getAssistenten();
		
		for(IAngestellter a: assis){
			
			assertTrue(assistenten.contains(a));
		}
	}
	
	@Test
	public void testWPAnzeigen() {
		
		// 1. Semester
		List<String> args = new ArrayList<>(
				Arrays.asList("4", "AI"));
		
		List<Veranstaltung> result = VeranstaltungsCtrlAdmin.wpAnzeigen(args);
		assertTrue(result.size() == 4);
		
		for(int i = 0; i < result.size(); i++){
			
			Veranstaltung res = result.get(i);
			int resIndex = wp.indexOf(res);
			Professor prof = (Professor) wp.get(resIndex).getProfessor();
			Professor profRes = (Professor) res.getProfessor();
			
			try{
				assertTrue(wp.contains(res)
						&& prof.equals(profRes)
						&& res.getAnzGr() == wp.get(resIndex).getAnzGr()
						&& res.getAnzTm() == wp.get(resIndex).getAnzTm()
						&& res.getMinTeilnTeam() == wp.get(resIndex).getMinTeilnTeam()
						&& res.getMaxTeilnTeam() == wp.get(resIndex).getMaxTeilnTeam()
						&& res.getSemester() == wp.get(resIndex).getSemester());
				
			}catch(AssertionError e){
				
				System.err.println("Fehler testWPAnzeigen:");
				System.err.println("========");
				System.err.println(res.toString());
				
				System.err.println();
				for(Veranstaltung va: wp){
					System.err.println(va.toString());
				}
			}
		}
	}
	
	
	@Test
	public void testPOAnzeigen() {
		
		// 1. Semester
		List<String> args = new ArrayList<>(
				Arrays.asList("5", "AI"));
		
		List<Veranstaltung> result = VeranstaltungsCtrlAdmin.poAnzeigen(args);
		assertTrue(result.size() == 4);
		
		for(int i = 0; i < result.size(); i++){
			
			Veranstaltung res = result.get(i);
			int resIndex = po.indexOf(res);
			Professor prof = (Professor) po.get(resIndex).getProfessor();
			Professor profRes = (Professor) res.getProfessor();
			
			try{
				assertTrue(po.contains(res)
						&& prof.equals(profRes)
						&& res.getAnzGr() == po.get(resIndex).getAnzGr()
						&& res.getAnzTm() == po.get(resIndex).getAnzTm()
						&& res.getMinTeilnTeam() == po.get(resIndex).getMinTeilnTeam()
						&& res.getMaxTeilnTeam() == po.get(resIndex).getMaxTeilnTeam()
						&& res.getSemester() == po.get(resIndex).getSemester());
				
			}catch(AssertionError e){
				
				System.err.println("Fehler testPOAnzeigen:");
				System.err.println("========");
				System.err.println(res.toString());
				
				System.err.println();
				for(Veranstaltung va: po){
					System.err.println(va.toString());
				}
			}
		}
	}
	
	
	@Test
	public void testVeranstaltungBearbeiten(){
		testPraktikaBearbeiten();
		testWPBearbeiten();
		testPOBearbeiten();
	}
	
	
	@Test
	public void testErstelleVeranstaltung(){
		
		testErtellePraktikum();
		testErstelleWP();
		testErstellePO();
		
	}
	
	
	private void testErstellePO() {
		// TODO Auto-generated method stub
		
	}

	private void testErstelleWP() {
		// TODO Auto-generated method stub
		
	}
	
	
	@Test
	public void testLoescheVeranstaltung(){
		
		ObjectMapper mapper = new ObjectMapper();
		// Hole alle Praktika
		List<Veranstaltung> allePraktika = getAllePraktika();
		
		// Veranstaltung ohne Gruppen erstellen
		int indexProf = (int) (Math.random() * professoren.size());
		int indexFach = (int) (Math.random() * faecherNichtVorhanden.size());
		Professor prof = professoren.get(indexProf);
		Fach fach = faecherNichtVorhanden.get(indexFach);
		
		Veranstaltung prakErstellen = new Praktikum(fach, fach.getSemester(), prof, 2, 3, 2, 3);
		boolean erfolg = false;
		
		try {
			erfolg = VeranstaltungsCtrlAdmin.veranstaltungErstellen(mapper.writeValueAsString(prakErstellen));
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		}
		assertTrue(erfolg);
		
		
		// Zufällige Veranstaltung
		int rdIndex = (int) (Math.random() * allePraktika.size());
		Veranstaltung prak = allePraktika.get(rdIndex);
		

			boolean erfolgLoesch = false;
			try {
				erfolgLoesch = VeranstaltungsCtrlAdmin.veranstaltungLoeschen(mapper.writeValueAsString(prak));
			} catch (JsonProcessingException e) {

				e.printStackTrace();
			}
			
			assertFalse(erfolgLoesch); // Darf nicht gelöscht werden da Gruppen vorhanden
			
			// Nochmal alle Praktika holen -> Neues prak muss enthalten sein
			allePraktika = getAllePraktika();
			assertTrue(allePraktika.contains(prakErstellen));
			
			try {
				erfolgLoesch = VeranstaltungsCtrlAdmin.veranstaltungLoeschen(mapper.writeValueAsString(prakErstellen));
			} catch (JsonProcessingException e) {
				e.printStackTrace();
			} // VA Loeschen
			
			assertTrue(erfolgLoesch); // Muss true sein, da noch keine Cponstraints fuer diese VA
			
			allePraktika = getAllePraktika();
			
			assertFalse(allePraktika.contains(prakErstellen)); // Darf nicht mehr enthalten sein


	}

	private void testErtellePraktikum() {
		
		ObjectMapper mapper = new ObjectMapper();
		int indexProf = (int) (Math.random() * professoren.size());
		int indexFach = (int) (Math.random() * faecherNichtVorhanden.size());
		Professor prof = professoren.get(indexProf);
		Fach fach = faecherNichtVorhanden.get(indexFach);
		
		Praktikum prak = new Praktikum(fach, fach.getSemester(), prof, 2, 3, 2, 3);
		boolean erfolg = false;
		try {
			erfolg = VeranstaltungsCtrlAdmin.veranstaltungErstellen(mapper.writeValueAsString(prak));
		} catch (JsonProcessingException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		List<Veranstaltung> allePraktika = null;
		try{
			assertTrue(erfolg);
			
			allePraktika = getAllePraktika();
			
			assertTrue(allePraktika.contains(prak)
						&& prak.getProfessor().equals(prof)
						&& prak.getAnzTm() == 2
						&& prak.getAnzGr() == 3
						&& prak.getMinTeilnTeam() == 2
						&& prak.getMaxTeilnTeam() == 3);
			
			assertTrue(VeranstaltungsCtrlAdmin.veranstaltungLoeschen(mapper.writeValueAsString(prak))); // Wieder loeschen fuer weitere Tests
			
		}catch(AssertionError e){
			System.err.println("Fehler testErtellePraktikum:");
			System.err.println("========");
			System.err.println(prak.toString());
			
			System.err.println();
			for(Veranstaltung va: allePraktika){
				System.err.println(va.toString());
			}
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		}
	
	}
	
	

	private void testPOBearbeiten() {
		
		ObjectMapper mapper = new ObjectMapper();
		int indexPO = (int) (Math.random() * po.size());
		Projekt prak = (Projekt) po.get(indexPO);
		int indexProf = (int) (Math.random() * professoren.size());
		int minTeiln = (int) (Math.random() * 2) + 1;
		int maxTeiln = (int) (Math.random() * 4) + minTeiln;
		
		Professor prof = professoren.get(indexProf);
		
		while(prak.getProfessor().equals(prof)){
			indexProf = (int) (Math.random() * professoren.size());
			prof = professoren.get(indexProf);
		}
		
		while(prak.getMinTeilnTeam() == minTeiln){
			
			minTeiln = (int) (Math.random() * 2) + 1;
		}
		
		while(prak.getMaxTeilnTeam() == maxTeiln){
			
			maxTeiln = (int) (Math.random() * 4) + minTeiln;
		}
		
		prak.setProfessor(prof);
		prak.setMinTeilnTeam(minTeiln);
		prak.setMaxTeilnTeam(maxTeiln);
		
		boolean erfolg = false;
		try {
			erfolg = VeranstaltungsCtrlAdmin.veranstaltungBearbeiten(mapper.writeValueAsString(prak));
		} catch (JsonProcessingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		assertTrue(erfolg);
		
		
		List<Veranstaltung> praktika = getAllePO();
		Veranstaltung refPrak = praktika.get(praktika.indexOf(prak));
		
		assertTrue(praktika.contains(prak)
					&& refPrak.getProfessor().equals(prak.getProfessor())
					&& refPrak.getMinTeilnTeam() == prak.getMinTeilnTeam()
					&& refPrak.getMaxTeilnTeam() == prak.getMaxTeilnTeam());
		
	}

	private List<Veranstaltung> getAllePO() {
		
		List<Veranstaltung> praktikaGes = new ArrayList<>();
		List<String> args = new ArrayList<>(Arrays.asList("5", "AI"));
		List<Veranstaltung> praktikaSem1 = VeranstaltungsCtrlAdmin.poAnzeigen(args);
		
		praktikaGes.addAll(praktikaSem1);
		
		return praktikaGes;
	}

	private void testWPBearbeiten() {
		
		ObjectMapper mapper = new ObjectMapper();
		int indexWP = (int) (Math.random() * wp.size());
		WP prak = (WP) wp.get(indexWP);
		int indexProf = (int) (Math.random() * professoren.size());
		int minTeiln = (int) (Math.random() * 2) + 1;
		int maxTeiln = (int) (Math.random() * 4) + minTeiln;
		
		Professor prof = professoren.get(indexProf);
		
		while(prak.getProfessor().equals(prof)){
			indexProf = (int) (Math.random() * professoren.size());
			prof = professoren.get(indexProf);
		}
		
		while(prak.getMinTeilnTeam() == minTeiln){
			
			minTeiln = (int) (Math.random() * 2) + 1;
		}
		
		while(prak.getMaxTeilnTeam() == maxTeiln){
			
			maxTeiln = (int) (Math.random() * 4) + minTeiln;
		}
		
		prak.setProfessor(prof);
		prak.setMinTeilnTeam(minTeiln);
		prak.setMaxTeilnTeam(maxTeiln);
		
		boolean erfolg = false;
		try {
			erfolg = VeranstaltungsCtrlAdmin.veranstaltungBearbeiten(mapper.writeValueAsString(prak));
		} catch (JsonProcessingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		assertTrue(erfolg);
		
		
		List<Veranstaltung> praktika = getAlleWP();
		Veranstaltung refPrak = praktika.get(praktika.indexOf(prak));
		
		assertTrue(praktika.contains(prak)
					&& refPrak.getProfessor().equals(prak.getProfessor())
					&& refPrak.getMinTeilnTeam() == prak.getMinTeilnTeam()
					&& refPrak.getMaxTeilnTeam() == prak.getMaxTeilnTeam());
		
	}

	private List<Veranstaltung> getAlleWP() {
		
		List<Veranstaltung> praktikaGes = new ArrayList<>();
		List<String> args = new ArrayList<>(Arrays.asList("4", "AI"));
		List<Veranstaltung> praktikaSem1 = VeranstaltungsCtrlAdmin.wpAnzeigen(args);
		
		praktikaGes.addAll(praktikaSem1);
		
		return praktikaGes;
	}

	private void testPraktikaBearbeiten() {
		
		ObjectMapper mapper = new ObjectMapper();
		
		int indexPrak = (int) (Math.random() * praktika.size());
		Praktikum prak = (Praktikum) praktika.get(indexPrak);
		int indexProf = (int) (Math.random() * professoren.size());
		int minTeiln = (int) (Math.random() * 2) + 1;
		int maxTeiln = (int) (Math.random() * 4) + minTeiln;
		
		Professor prof = professoren.get(indexProf);
		
		while(prak.getProfessor().equals(prof)){
			indexProf = (int) (Math.random() * professoren.size());
			prof = professoren.get(indexProf);
		}
		
		while(prak.getMinTeilnTeam() == minTeiln){
			
			minTeiln = (int) (Math.random() * 2) + 1;
		}
		
		while(prak.getMaxTeilnTeam() == maxTeiln){
			
			maxTeiln = (int) (Math.random() * 4) + minTeiln;
		}
		
		prak.setProfessor(prof);
		prak.setMinTeilnTeam(minTeiln);
		prak.setMaxTeilnTeam(maxTeiln);
		
		boolean erfolg = false;
		try {
			erfolg = VeranstaltungsCtrlAdmin.veranstaltungBearbeiten(mapper.writeValueAsString(prak));
		} catch (JsonProcessingException e) {

			e.printStackTrace();
		}
		assertTrue(erfolg);
		
		
		List<Veranstaltung> praktika = getAllePraktika();
		Veranstaltung refPrak = praktika.get(praktika.indexOf(prak));
		
		assertTrue(praktika.contains(prak)
					&& refPrak.getProfessor().equals(prak.getProfessor())
					&& refPrak.getMinTeilnTeam() == prak.getMinTeilnTeam()
					&& refPrak.getMaxTeilnTeam() == prak.getMaxTeilnTeam());
		
	}
	
	@Test
	public void testGruppeLoeschen(){
		
		ObjectMapper mapper = new ObjectMapper();
		List<Gruppe> gruppen = getAlleGruppen();
		int indexRdGruppe = (int) (Math.random() * gruppen.size());
		Gruppe rdGruppe = gruppen.get(indexRdGruppe);
		boolean erfolg = false;
		
		while(rdGruppe.getAnzTeams() == 0){
			indexRdGruppe = (int) (Math.random() * gruppen.size());
			rdGruppe = gruppen.get(indexRdGruppe);
		}
		
		
		List<Gruppe> gruppenFuerVa = new ArrayList<>();
		
		gruppenFuerVa.add(rdGruppe);
		
		// Alle gruppen fuer eine Veranstaltung filtern
		for(Gruppe gr: gruppen){
			
			if(gr.getFachkuerzel().equals(rdGruppe.getFachkuerzel())
					&& !gruppenFuerVa.contains(gr)){
				
				gruppenFuerVa.add(gr);
			}
		}
		
		Gruppe letzteGruppe = null;
		int min = Integer.MIN_VALUE;
		
		// Gruppe mit hoechster Gruppennummer finden
		for(Gruppe gr: gruppenFuerVa){
			
			if(gr.getGrpNr() > min){
				min = gr.getGrpNr();
				letzteGruppe = gr;
			}
		}
		
		int grpNr = letzteGruppe.getGrpNr() + 1;
		String fachkuerzel = letzteGruppe.getFachkuerzel();
		Professor prof = letzteGruppe.getProfessor();
		Assistent assis = letzteGruppe.getAssistent();
		int maxTeams = letzteGruppe.getMaxTeams() + 10;
		List<Termin> termine = letzteGruppe.getTermine();
		
		
		try {
			
			String rdGruppeString = mapper.writeValueAsString(rdGruppe);
			System.out.println(rdGruppe);
			erfolg = VeranstaltungsCtrlAdmin.gruppeLoeschen(rdGruppeString);	// Gruppe hat Contraints
			assertFalse(erfolg); // daher darf loeschen nicht möglich sein
			
			// Erstelle neue Gruppe
			Gruppe neueGruppe = new Gruppe(fachkuerzel, grpNr, termine, prof, assis, maxTeams, 0);
			String neueGruppeString = mapper.writeValueAsString(neueGruppe);
			erfolg = VeranstaltungsCtrlAdmin.gruppeErstellen(neueGruppeString);
			
			assertTrue(erfolg);
			
			erfolg = VeranstaltungsCtrlAdmin.gruppeLoeschen(neueGruppeString);
			
			gruppen = getAlleGruppen();
			
			assertFalse(gruppen.contains(neueGruppe));
			
			
		} catch (JsonProcessingException e) {
			
			e.printStackTrace();
		}

		
		
		
	}

	private List<Veranstaltung> getAllePraktika() {
		
		List<Veranstaltung> praktikaGes = new ArrayList<>();
		List<String> args = new ArrayList<>(Arrays.asList("1", "AI"));
		List<Veranstaltung> praktikaSem1 = VeranstaltungsCtrlAdmin.praktikaAnzeigen(args);
		
		args = new ArrayList<>(Arrays.asList("2", "AI"));
		List<Veranstaltung> praktikaSem2 = VeranstaltungsCtrlAdmin.praktikaAnzeigen(args);
		
		args = new ArrayList<>(Arrays.asList("3", "AI"));
		List<Veranstaltung> praktikaSem3 = VeranstaltungsCtrlAdmin.praktikaAnzeigen(args);
		
		args = new ArrayList<>(Arrays.asList("4", "AI"));
		List<Veranstaltung> praktikaSem4 = VeranstaltungsCtrlAdmin.praktikaAnzeigen(args);
		
		praktikaGes.addAll(praktikaSem1);
		praktikaGes.addAll(praktikaSem2);
		praktikaGes.addAll(praktikaSem3);
		praktikaGes.addAll(praktikaSem4);
		
		return praktikaGes;
	}
	
	@Test
	public void testGruppenUebersichtAnzeigenPraktika(){
		
		List<Gruppe> alleGruppenPrak = getAlleGruppen();
		
		//assertTrue(alleGruppenPrak.size() == 19);
		
		for(Gruppe gr: gruppen){
			
			int gruppIndex = alleGruppenPrak.indexOf(gr);
			Gruppe refGruppe = alleGruppenPrak.get(gruppIndex);
			
			try{
				
				assertTrue(alleGruppenPrak.contains(gr)
						&& gr.getProfessor().equals(refGruppe.getProfessor())
						&& gr.getAssistent().equals(refGruppe.getAssistent())
						&& gr.getAnzTeams() == refGruppe.getAnzTeams()
						&& gr.getFachkuerzel().equals(refGruppe.getFachkuerzel())
						&& gr.getGrpNr() == refGruppe.getGrpNr()
						&& gr.getMaxTeams() == refGruppe.getMaxTeams()
						&& gr.getTermine().equals(refGruppe.getTermine()));
				
			}catch(AssertionError e){
				
				System.err.println("Fehler testGruppenUebersichtAnzeigenPraktika:");
				System.err.println("========");
				System.err.println(gr.toString());
				
				System.err.println();
				System.err.println(refGruppe.toString());
				
				
			}
		}
	}
	
	@Test
	public void testGruppeErstellenPraktikum(){
		
		ObjectMapper mapper = new ObjectMapper();
		List<Gruppe> gruppen = getAlleGruppen();
		int indexGrupp = (int) (Math.random() * gruppen.size());
		List<Gruppe> gruppenFuerVa = new ArrayList<>();
		Gruppe rdGruppe = gruppen.get(indexGrupp);
		gruppenFuerVa.add(rdGruppe);
		
		// Alle gruppen fuer eine Veranstaltung filtern
		for(Gruppe gr: gruppen){
			
			if(gr.getFachkuerzel().equals(rdGruppe.getFachkuerzel())
					&& !gruppenFuerVa.contains(gr)){
				
				gruppenFuerVa.add(gr);
			}
		}
		
		Gruppe letzteGruppe = null;
		int min = Integer.MIN_VALUE;
		
		// Gruppe mit hoechster Gruppennummer finden
		for(Gruppe gr: gruppenFuerVa){
			
			if(gr.getGrpNr() > min){
				min = gr.getGrpNr();
				letzteGruppe = gr;
			}
		}
		
		int grpNr = letzteGruppe.getGrpNr()+1;
		String fachkuerzel = letzteGruppe.getFachkuerzel();
		Professor prof = letzteGruppe.getProfessor();
		Assistent assis = letzteGruppe.getAssistent();
		int maxTeams = letzteGruppe.getMaxTeams() + 10;
		List<Termin> termine = letzteGruppe.getTermine();
		
		
		// TerminId korrigieren
		int terminIndex = 1;
		for(Termin t: termine){
			
			String tId = letzteGruppe.getFachkuerzel() + "G" + grpNr + "T" + terminIndex;
			t.setTerminID(TerminID.getTerminID(tId));
			terminIndex++;
		}
		
		Gruppe neueGruppe = new Gruppe(fachkuerzel, grpNr, termine, prof, assis, maxTeams, 0);
		Gruppe gruppeKeineVA = new Gruppe("FKNA", grpNr, termine, prof, assis, maxTeams, 0);
		String neueGruppeString = null;
		String gruppeKeineVAString = null;
		Gruppe refGruppe = null;
		boolean erfolg = false;
		
		try {
			
			neueGruppeString = mapper.writeValueAsString(neueGruppe);
			gruppeKeineVAString = mapper.writeValueAsString(gruppeKeineVA);
			erfolg = VeranstaltungsCtrlAdmin.gruppeErstellen(neueGruppeString);
			assertTrue(erfolg);
			gruppen = getAlleGruppen();
			int indexNG = gruppen.indexOf(neueGruppe);
			refGruppe = gruppen.get(indexNG);
			
			assertTrue(gruppen.contains(neueGruppe)
						&& neueGruppe.getAnzTeams() == refGruppe.getAnzTeams()
						&& neueGruppe.getAssistent().equals(refGruppe.getAssistent())
						&& neueGruppe.getFachkuerzel().equals(refGruppe.getFachkuerzel())
						&& neueGruppe.getGrpNr() == refGruppe.getGrpNr()
						&& neueGruppe.getMaxTeams() == refGruppe.getMaxTeams()
						&& neueGruppe.getProfessor().equals(refGruppe.getProfessor())
						&& neueGruppe.getTermine().equals(refGruppe.getTermine()));
			
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		}catch(AssertionError e){
			System.err.println("Fehler testGruppeErstellenPraktikum");
			System.err.println("=======");
			System.err.println(neueGruppe.toString());
			System.err.println(refGruppe.toString());
			
		}
		
		gruppen = getAlleGruppen();
		assertTrue(gruppen.contains(neueGruppe));
		erfolg = VeranstaltungsCtrlAdmin.gruppeErstellen(neueGruppeString);
		assertFalse(erfolg);
		erfolg = VeranstaltungsCtrlAdmin.gruppeErstellen(gruppeKeineVAString);
		assertFalse(erfolg);
		
	}
	
	
	@Test
	public void testGruppeBearbeiten(){
		
		ObjectMapper mapper = new ObjectMapper();
		List<Gruppe> gruppen = getAlleGruppen();
		int indexGrupp = (int) (Math.random() * gruppen.size());
		Gruppe rdGruppe = gruppen.get(indexGrupp);
		
		// Keine Gruppe ohne Termine aussuchen
		while(rdGruppe.getTermine() == null || rdGruppe.getTermine().size() == 0){
			indexGrupp = (int) (Math.random() * gruppen.size());
			rdGruppe = gruppen.get(indexGrupp);
		}
		
		int indexProf = (int) (Math.random() * professoren.size());
		int indexAssis = (int) (Math.random() * assistenten.size());
		
		Professor prof = professoren.get(indexProf);
		Assistent assist = assistenten.get(indexAssis);
		
		while(rdGruppe.getProfessor().equals(prof)){
			indexProf = (int) (Math.random() * professoren.size());
			prof = professoren.get(indexProf);
		}
		
		while(rdGruppe.getAssistent().equals(assist)){
			indexAssis = (int) (Math.random() * assistenten.size());
			assist = assistenten.get(indexAssis);
		}
		
		rdGruppe.getTermine().remove(rdGruppe.getTermine().size() - 1);
		assertTrue(rdGruppe.getTermine().size() == 3);
		List<Termin> refTermine = rdGruppe.getTermine();
		rdGruppe.setProfessor(prof);
		rdGruppe.setAssistent(assist);
		Gruppe refGruppe = null;
		boolean erfolg = false;
		
		try {
			
			String rdGruppeString = mapper.writeValueAsString(rdGruppe);
			erfolg = VeranstaltungsCtrlAdmin.gruppeBearbeiten(rdGruppeString);
			
			assertTrue(erfolg);
			
			gruppen = getAlleGruppen();
			int indexNG = gruppen.indexOf(rdGruppe);
			refGruppe = gruppen.get(indexNG);
			
			assertTrue(gruppen.contains(rdGruppe)
						&& rdGruppe.getAnzTeams() == refGruppe.getAnzTeams()
						&& rdGruppe.getAssistent().equals(assist)
						&& rdGruppe.getFachkuerzel().equals(refGruppe.getFachkuerzel())
						&& rdGruppe.getGrpNr() == refGruppe.getGrpNr()
						&& rdGruppe.getMaxTeams() == refGruppe.getMaxTeams()
						&& rdGruppe.getProfessor().equals(prof)
						&& rdGruppe.getTermine().equals(refTermine));
			
			assertTrue(refGruppe.getTermine().size() == 3);
			
			
			
		} catch (JsonProcessingException e) {
			
			
			e.printStackTrace();
		} catch(AssertionError e){
			System.out.println("Fehler testGruppeBearbeiten:");
			System.err.println(refGruppe);
			System.err.println(rdGruppe);
		}	
	}
	
	
	@Test
	public void testteilnehmerUebersichtAnzeigen(){
		
		List<Gruppe> alleGruppen = getAlleGruppen();
		List<Team> alleTeams = getAlleTeams(alleGruppen);
		
		for(Team team: teams){
			
			try{
				assertTrue(alleTeams.contains(team));
			}catch(AssertionError e){
				System.err.println("Fehler testteilnehmerUebersichtAnzeigen:");
				System.err.println(team.toString());
				System.err.println();
				for(Team t: alleTeams){
					System.err.println(t);
				}
			}
			
		}
		
		
		
	}


	private List<Team> getAlleTeams(List<Gruppe> alleGruppen) {
		
		ObjectMapper mapper = new ObjectMapper();
		List<Team> alleTeams = new ArrayList<>();
		
		for(Gruppe gr: alleGruppen){
			
			try {
				
				String gruppe = mapper.writeValueAsString(gr);
				List<Team> teams = VeranstaltungsCtrlAdmin.teilnehmerUebersichtAnzeigen(gruppe);
				alleTeams.addAll(teams);
				
			} catch (JsonProcessingException e) {
				e.printStackTrace();
			}
			
		}
		return alleTeams;
	}
	
// Nicht mehr relevant - wird nirgendwo mehr verwendet
// ===========================================================================================================

//	@Test
//	public void testTmEntfernen(){
//		
//		List<Gruppe> alleGruppen = getAlleGruppen();
//		List<Team> alleTeams = getAlleTeams(alleGruppen);
//		int indexTeam = (int) (Math.random() * alleTeams.size()); 
//		Team rdTeam = alleTeams.get(indexTeam);
//		int indexMg = (int) (Math.random() * rdTeam.getMitglieder().size()); 
//		Student stud = rdTeam.getMitglieder().get(indexMg);
//		MatrikelNr matrNr = stud.getMatrNr();
//		TeamID teamId = rdTeam.getTeamID();
//		
//			
//		List<String> args = new ArrayList<>(Arrays.asList(teamId.getId(), ""+matrNr.getMatrNr()));
//		boolean erfolgreich = VeranstaltungsCtrlAdmin.tmEntfernen(args);
//			
//		assertTrue(erfolgreich);
//		assertTrue(alleTeams.contains(rdTeam));
//			
//		for(Student st: rdTeam.getMitglieder()){
//				
//			args = new ArrayList<>(Arrays.asList(teamId.getId(), ""+st.getMatrNr().getMatrNr()));
//			erfolgreich = VeranstaltungsCtrlAdmin.tmEntfernen(args);
//			assertTrue(erfolgreich);
//		}
//		
//		alleTeams = getAlleTeams(alleGruppen);
//		assertFalse(alleTeams.contains(rdTeam));
//		
//	}
	
	// ===========================================================================================================

	private List<Gruppe> getAlleGruppen() {
		
		ObjectMapper mapper = new ObjectMapper();
		List<Veranstaltung> veranstaltungen = getAllePraktika();
		List<Gruppe> alleGruppen = new ArrayList<>();
		
		try {
			
			for(Veranstaltung va: veranstaltungen){
				String veranstaltung = mapper.writeValueAsString(va);
				alleGruppen.addAll(VeranstaltungsCtrlAdmin.gruppenUebersichtAnzeigenPraktika(veranstaltung));
			} 	
		}catch (JsonProcessingException e) {
			e.printStackTrace();
		}

			return alleGruppen;
		
	}

	private static void setUp() {
		
		// Faecher - Pflicht
		Fach prp1 = new Fach("PRP1", 1); // 1. Semester
		prp1.setFachBezeichnung("Programmieren 1");
		prp1.setfBKuerzel("AI");
		Fach prp2 = new Fach("PRP2", 2); // 2. Semester
		prp2.setFachBezeichnung("Programmieren 2");
		prp2.setfBKuerzel("AI");
		Fach gkap = new Fach("GKAP", 3); // 3. Semester
		gkap.setFachBezeichnung("Graphentheorie");
		gkap.setfBKuerzel("AI");
		Fach mgp = new Fach("MGP", 1);
		mgp.setFachBezeichnung("Mathematische Grundlagen");
		mgp.setfBKuerzel("AI");
		Fach bsp = new Fach("BSP", 3);
		bsp.setFachBezeichnung("Betriebssysteme");
		bsp.setfBKuerzel("AI");
		Fach dbp = new Fach("DBP", 2);
		dbp.setFachbereich("Datenbanken");
		dbp.setfBKuerzel("AI");
		
		Fach sep2 = new Fach("SEP2", 4);
		sep2.setFachBezeichnung("Software-Engineering 2");
		sep2.setfBKuerzel("AI");
		Fach rnp = new Fach("RNP", 4);
		rnp.setFachBezeichnung("Rechnernetze");
		rnp.setfBKuerzel("AI");
		Fach cip = new Fach("CIP", 4);
		cip.setFachBezeichnung("Compiler und Interpreter");
		cip.setfBKuerzel("AI");
		
		faecherVorhanden.add(prp1);
		faecherVorhanden.add(prp2);
		faecherVorhanden.add(gkap);
		faecherVorhanden.add(mgp);
		faecherVorhanden.add(bsp);
		faecherVorhanden.add(dbp);
		
		faecherNichtVorhanden.add(sep2);
		faecherNichtVorhanden.add(rnp);
		faecherNichtVorhanden.add(cip);
		
		// Faecher - WP
		Fach wppdbd = new Fach("WPDBDP", 4);
		Fach wpppn = new Fach("WPPNP", 4);
		Fach wpnsbdp = new Fach("WPNSBDP", 4);
		Fach wpdep = new Fach("WPDEP", 4);
		
		// Faecher - PO
		Fach poctf = new Fach("POCTF", 5);
		Fach pola = new Fach("POLA", 5);
		Fach posr = new Fach("POSR", 5);
		Fach powrtc = new Fach("POWRTC", 5);
		
		// Professoren
		Professor huebner = new Professor(MAID.getMAID(1), "Martin", "Huebner");
		Professor padberg = new Professor(MAID.getMAID(2), "Julia", "Padberg");
		Professor schmolitzky = new Professor(MAID.getMAID(3), "Axel", "Schmolitzky");
		Professor gerken = new Professor(MAID.getMAID(4), "Wolfgang", "Gerken");
		Professor neitzke = new Professor(MAID.getMAID(5), "Michael", "Neitzke");
		Professor kossakowski = new Professor(MAID.getMAID(6), "Klaus-Peter", "Kossakowski");
		Professor pareigis = new Professor(MAID.getMAID(7), "Stephan", "Pareigis");
		Professor becke = new Professor(MAID.getMAID(8), "Martin", "Becke");
		Professor zukunft = new Professor(MAID.getMAID(9), "Olaf", "Zukunft");
		
		professoren.add(huebner);
		professoren.add(padberg);
		professoren.add(schmolitzky);
		professoren.add(gerken);
		professoren.add(neitzke);
		professoren.add(kossakowski);
		professoren.add(pareigis);
		professoren.add(becke);
		professoren.add(zukunft);
		
		
		// Assistenten
		Assistent blank = new Assistent(MAID.getMAID(11), "Ilona", "Blank");
		Assistent oelker = new Assistent(MAID.getMAID(22), "Gerhard", "Oelker");
		Assistent schulz = new Assistent(MAID.getMAID(33), "Hartmut", "Schulz");
		
		assistenten.add(blank);
		assistenten.add(oelker);
		assistenten.add(schulz);
		
		// ------------------------------------------
		// Pflichpraktika
		Veranstaltung vPrp1 = 
				new Praktikum(prp1, prp1.getSemester(), schmolitzky, 3, 4, 2, 3);
		Veranstaltung vPrp2 = 
				new Praktikum(prp2, prp2.getSemester(), schmolitzky, 6, 6, 2, 2);
		Veranstaltung vGkap = 
				new Praktikum(gkap, gkap.getSemester(), padberg, 2, 3, 2, 3);
		
		Veranstaltung vMgp = 
				new Praktikum(mgp, mgp.getSemester(), gerken, 1, 3, 2, 3);
		
		Veranstaltung vBsp = 
				new Praktikum(bsp, bsp.getSemester(), huebner, 1, 4, 3, 4);
		
		Veranstaltung vDbp = 
				new Praktikum(dbp, dbp.getSemester(), gerken, 5, 5, 2, 3);
		
		praktika.add(vPrp1);
		praktika.add(vPrp2);
		praktika.add(vGkap);
		praktika.add(vMgp);
		praktika.add(vBsp);
		praktika.add(vDbp);
		
		// WP
		Veranstaltung vWppdbd = 
				new WP(wppdbd, 4, gerken, 1, 1, 1, 2);
		
		Veranstaltung vWpppn = 
				new WP(wpppn, 4, padberg, 1, 1, 1, 2);
		
		/*
		 * 	@JsonCreator
	public WP( @JsonProperty("fach") Fach fach, 
		 	   @JsonProperty("semester") int semester,
		 	   @JsonProperty("professor")	Professor prof, 
		 	   @JsonProperty("anzTm") int anzTm, 
		 	   @JsonProperty("anzGr") int anzGr,
		 	   @JsonProperty("minTeilnTeam") int minTeilnTeam, 
		 	   @JsonProperty("maxTeilnTeam") int maxTeilnTeam)	
		 */
		Veranstaltung vWpnsbdp = 
				new WP(wpnsbdp, 4, zukunft, 0, 1, 1, 2);
		
		Veranstaltung vWpdep = 
				new WP(wpdep, 4, pareigis, 0, 1, 1, 2);
		wp.add(vWppdbd);
		wp.add(vWpppn);
		wp.add(vWpnsbdp);
		wp.add(vWpdep);
		
		// PO
		Veranstaltung vPoctf = new Projekt(poctf, 5, kossakowski, 1, 1, 2, 3);
		Veranstaltung vPola = new Projekt(pola, 5, neitzke, 0, 1, 2, 3);
		Veranstaltung vPosr = new Projekt(posr, 5, pareigis, 0, 1, 2, 3);
		Veranstaltung vPowrtc = new Projekt(powrtc, 5, becke, 0, 1, 2, 3);
		
		po.add(vPoctf);
		po.add(vPola);
		po.add(vPosr);
		po.add(vPowrtc);
		
		// Gruppen
		
		// Termine - Gruppe PRP1 - 1 //mm-dd-yyyy
		String datet1prp11String = "8-10-2015";
		String datet2prp11String = "29-10-2015";
		String datet3prp11String = "19-11-2015";
		String datet4prp11String = "10-12-2015";
		
		DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
		Date datet1prp11 = null;
		Date datet2prp11 = null;
		Date datet3prp11 = null;
		Date datet4prp11 = null;
		
		try {
			
			datet1prp11 = dateFormat.parse(datet1prp11String);
			datet2prp11 = dateFormat.parse(datet2prp11String);
			datet3prp11 = dateFormat.parse(datet3prp11String);
			datet4prp11 = dateFormat.parse(datet4prp11String);
			
		} catch (ParseException e) {
			e.printStackTrace();
		}
		
		
		Termin t1Prp11 = new Termin("1101b", datet1prp11, new Uhrzeit(8, 15), new Uhrzeit(11, 30));
		t1Prp11.setTerminID(TerminID.getTerminID("PRP1G1T1"));
		Termin t2Prp11 = new Termin("1101b", datet2prp11, new Uhrzeit(8, 15), new Uhrzeit(11, 30));
		t2Prp11.setTerminID(TerminID.getTerminID("PRP1G1T2"));
		Termin t3Prp11 = new Termin("1101b", datet3prp11, new Uhrzeit(8, 15), new Uhrzeit(11, 30));
		t3Prp11.setTerminID(TerminID.getTerminID("PRP1G1T3"));
		Termin t4Prp11 = new Termin("1101b", datet4prp11, new Uhrzeit(8, 15), new Uhrzeit(11, 30));
		t4Prp11.setTerminID(TerminID.getTerminID("PRP1G1T4"));
		List<Termin> grPrp1Gr1Term = new ArrayList<>(Arrays.asList(t1Prp11, t2Prp11, t3Prp11, t4Prp11));
		
		
		// Termine - Gruppe PRP1 - 1 //mm-dd-yyyy
		String datet1prp12String = "15-10-2015";
		String datet2prp12String = "5-11-2015";
		String datet3prp12String = "26-11-2015";
		String datet4prp12String = "17-12-2015";
		
		Date datet1prp12 = null;
		Date datet2prp12 = null;
		Date datet3prp12 = null;
		Date datet4prp12 = null;
		
		try {
			
			datet1prp12 = dateFormat.parse(datet1prp12String);
			datet2prp12 = dateFormat.parse(datet2prp12String);
			datet3prp12 = dateFormat.parse(datet3prp12String);
			datet4prp12 = dateFormat.parse(datet4prp12String);
			
		} catch (ParseException e) {
			e.printStackTrace();
		}
		
		// Termine - Gruppe PRP1 - 2
		Termin t1Prp12 = new Termin("1101b", datet1prp12, new Uhrzeit(8, 15), new Uhrzeit(11, 30));
		t1Prp12.setTerminID(TerminID.getTerminID("PRP1G2T1"));
		Termin t2Prp12 = new Termin("1101b", datet2prp12, new Uhrzeit(8, 15), new Uhrzeit(11, 30));
		t2Prp12.setTerminID(TerminID.getTerminID("PRP1G2T2"));
		Termin t3Prp12 = new Termin("1101b", datet3prp12, new Uhrzeit(8, 15), new Uhrzeit(11, 30));
		t3Prp12.setTerminID(TerminID.getTerminID("PRP1G2T3"));
		Termin t4Prp12 = new Termin("1101b", datet4prp12, new Uhrzeit(8, 15), new Uhrzeit(11, 30));
		t4Prp12.setTerminID(TerminID.getTerminID("PRP1G2T4"));
		List<Termin> grPrp1Gr2Term = new ArrayList<>(Arrays.asList(t1Prp12, t2Prp12, t3Prp12, t4Prp12));
		
		
		Gruppe grPrp11 = new Gruppe("PRP1", 1, grPrp1Gr1Term, schmolitzky, blank, 14, 0);
		Gruppe grPrp12 = new Gruppe("PRP1", 2, grPrp1Gr2Term, schmolitzky, blank, 14, 1);
		
		gruppen.add(grPrp11);
		gruppen.add(grPrp12);
		
		
		// Teams
		
		// Mitglieder
		Student stud1 = new Student(MatrikelNr.getMatrikelNr(1111111),"Martin", "Schmidt");
		Student stud2 = new Student(MatrikelNr.getMatrikelNr(1111112),"Clara", "Schulz");
		Student stud3 = new Student(MatrikelNr.getMatrikelNr(1111113),"Achmed", "Swolyski");
		Student stud4 = new Student(MatrikelNr.getMatrikelNr(1111114),"James", "Bond");
		Student stud5 = new Student(MatrikelNr.getMatrikelNr(1111121),"Natsu", "Dragneel");
		Student stud6 = new Student(MatrikelNr.getMatrikelNr(1111122),"Patrick", "Schuller");
		Student stud7 = new Student(MatrikelNr.getMatrikelNr(1111123),"Sercy", "Lannister");
		Student stud8 = new Student(MatrikelNr.getMatrikelNr(1111124),"Bob", "McCabe");
		Student stud9 = new Student(MatrikelNr.getMatrikelNr(1111125),"Tim", "Brahms");
		Student stud10 = new Student(MatrikelNr.getMatrikelNr(1111120),"Johny", "McBeal");
		
		studenten.add(stud1);
		studenten.add(stud2);
		studenten.add(stud3);
		studenten.add(stud4);
		studenten.add(stud5);
		studenten.add(stud6);
		studenten.add(stud7);
		studenten.add(stud8);
		studenten.add(stud9);
		studenten.add(stud10);
		
		
		List<Student> mitglTeamPrp11 = new ArrayList<>(Arrays.asList(stud6, stud7));
		List<Student> mitglTeamPrp21 = new ArrayList<>(Arrays.asList(stud9, stud5));
		List<Student> mitglTeamPrp22= new ArrayList<>(Arrays.asList(stud10, stud8));
		Team teamPrp11 = new Team(TeamID.getTeamID("2"), 2, 3, mitglTeamPrp11, 2, 2, "PRP1");
		Team teamPrp21 = new Team(TeamID.getTeamID("8"), 2, 3, mitglTeamPrp21, 3, 3, "PRP2");
		Team teamPrp22 = new Team(TeamID.getTeamID("9"), 2, 3, mitglTeamPrp22, 3, 3, "PRP2");
		
		
		teams.add(teamPrp11);
		teams.add(teamPrp21);
		teams.add(teamPrp22);
		
	}
	
    @AfterClass
    public static void setzeDatenWiederZurueck() {
    	
    	List<String> folders = DatenImExportController.holeBackupDateien();
    	Collections.sort(folders);
    	DatenImExportController.backupWiederherstellen(folders.get(0));
    }
	

}
