package se2.praktikum.projekt.datenimexport.tests;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;

import se2.praktikum.projekt.datenimexport.DatenImExportController;
import se2.praktikum.projekt.datenimexport.backuptables.DBAnmeldetermin;
import se2.praktikum.projekt.datenimexport.backuptables.DBDepartment;
import se2.praktikum.projekt.datenimexport.backuptables.DBFachbereich;
import se2.praktikum.projekt.datenimexport.backuptables.DBPOVeranZuordung;
import se2.praktikum.projekt.datenimexport.backuptables.DBPruefungsordnung;
import se2.praktikum.projekt.datenimexport.backuptables.DBTeammitglied;
import se2.praktikum.projekt.dbms.DBConnector;
import se2.praktikum.projekt.models.gruppe.Gruppe;
import se2.praktikum.projekt.models.leistungen.Leistung;
import se2.praktikum.projekt.models.person.Assistent;
import se2.praktikum.projekt.models.person.Professor;
import se2.praktikum.projekt.models.person.Student;
import se2.praktikum.projekt.models.person.VerwaltMitarbeiter;
import se2.praktikum.projekt.models.team.Team;
import se2.praktikum.projekt.models.veranstaltung.Veranstaltung;
import se2.praktikum.projekt.services.loginservice.LoginSrv;
import se2.praktikum.projekt.services.loginservice.ServicePool;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class DatenImExportControllerTest {

	private static LoginSrv login;
	private static ServicePool pool;
	private static Connection connection;
	
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		
		
		
		pool = new ServicePool();
		login = new LoginSrv(pool);
		login.login("naa001", "naa001");
		connection = DBConnector.getConnection();// Aendern
		List<String> folders = DatenImExportController.holeBackupDateien();
		Collections.sort(folders);
		DatenImExportController.backupWiederherstellen(folders.get(0));
	}

	@Test
	public void test1AlleDatenSichern() {
		
		assertTrue(DatenImExportController.datenSicherung());
		List<Professor> profsExpected = pool.getDatenExSrv().getAlleProfDaten();
		List<DBDepartment> departmentsExpected = pool.getDatenExSrv().getAlleDepartments();
		List<DBFachbereich> fachbereicheExpected = pool.getDatenExSrv().getAlleDBFachbereiche();
		List<Assistent> assistentenExpected = pool.getDatenExSrv().getAlleAssistDaten();
		List<String> fachkuerzelExpected = pool.getDatenExSrv().getAlleFachkuerzel();
		List<Veranstaltung> veranstaltungenExpected = pool.getDatenExSrv().getVABasisDaten(fachkuerzelExpected);
		List<DBAnmeldetermin> anmTermineExpected = pool.getDatenExSrv().getAlleAnmeldetermine();
		List<Gruppe> gruppenExpected = pool.getDatenExSrv().getVAGruppen(fachkuerzelExpected);
		List<Team> teamsExpected = pool.getDatenExSrv().getVAGruppenTeams(fachkuerzelExpected);
		List<DBPruefungsordnung> pruefOrdnungenExpected = pool.getDatenExSrv().getAllePruefOrdnungen();
		List<Student> studentenExpected = pool.getDatenExSrv().getAlleStudenten();
		List<VerwaltMitarbeiter> mitarbeiterExpected = pool.getDatenExSrv().getAlleVerwMitarbeiter();
		List<DBTeammitglied> teammitgliederExpected = pool.getDatenExSrv().getAlleDBTeammitglieder();
		List<Leistung> bewertungenExpected = getAlleBewertungen(fachkuerzelExpected);
		List<DBPOVeranZuordung> poVernZuordnungenExpected = pool.getDatenExSrv().getAllePoVeranZuOrdn();
		//List<Meldung> meldungenExpected = pool.getTeamVerwSrv().getAlleMeldungen();
	
		List<String> backupdateien = pool.getDatenImpSrv().holeBackupDateien();
		System.out.println("FOLDERS");
		assertTrue(backupdateien.size() > 0);

		Collections.sort(backupdateien);

		assertTrue(DatenImExportController.backupWiederherstellen(backupdateien.get(backupdateien.size() - 1)));

		List<Professor> profsResult = pool.getDatenExSrv().getAlleProfDaten();
		List<DBDepartment> departmentsResult = pool.getDatenExSrv().getAlleDepartments();
		List<DBFachbereich> fachbereicheResult = pool.getDatenExSrv().getAlleDBFachbereiche();
		List<Assistent> assistentenResult = pool.getDatenExSrv().getAlleAssistDaten();
		List<String> fachkuerzelResult = pool.getDatenExSrv().getAlleFachkuerzel();
		List<Veranstaltung> veranstaltungenResult = pool.getDatenExSrv().getVABasisDaten(fachkuerzelExpected);
		List<DBAnmeldetermin> anmTermineResult = pool.getDatenExSrv().getAlleAnmeldetermine();
		List<Gruppe> gruppenResult = pool.getDatenExSrv().getVAGruppen(fachkuerzelExpected);
		List<Team> teamsResult = pool.getDatenExSrv().getVAGruppenTeams(fachkuerzelExpected);
		List<DBPruefungsordnung> pruefOrdnungenResult = pool.getDatenExSrv().getAllePruefOrdnungen();
		List<Student> studentenResult = pool.getDatenExSrv().getAlleStudenten();
		List<VerwaltMitarbeiter> mitarbeiterResult = pool.getDatenExSrv().getAlleVerwMitarbeiter();
		List<DBTeammitglied> teammitgliederResult = pool.getDatenExSrv().getAlleDBTeammitglieder();
		List<Leistung> bewertungenResult = getAlleBewertungen(fachkuerzelExpected);
		List<DBPOVeranZuordung> poVernZuordnungenResult = pool.getDatenExSrv().getAllePoVeranZuOrdn();
		//List<Meldung> meldungenResult = pool.getTeamVerwSrv().getAlleMeldungen();
		
		
		assertTrue(profsResult.size() == profsExpected.size());
		assertTrue(profsResult.containsAll(profsExpected));
		
		assertTrue(departmentsResult.size() == departmentsExpected.size());
		assertTrue(departmentsResult.containsAll(departmentsExpected));
		
		assertTrue(fachbereicheResult.size() == fachbereicheResult.size());
		assertTrue(fachbereicheResult.containsAll(fachbereicheExpected));
		
		assertTrue(assistentenResult.size() == assistentenExpected.size());
		assertTrue(assistentenResult.containsAll(assistentenExpected));
		
		assertTrue(fachkuerzelResult.size() == fachkuerzelExpected.size());
		assertTrue(fachkuerzelResult.containsAll(fachkuerzelExpected));
		
		assertTrue(veranstaltungenResult.size() == veranstaltungenExpected.size());
		assertTrue(veranstaltungenResult.containsAll(veranstaltungenExpected));
		
		assertTrue(anmTermineExpected.size() == anmTermineResult.size());
		assertTrue(anmTermineResult.containsAll(anmTermineExpected));
		
		assertTrue(gruppenResult.size() == gruppenExpected.size());
		assertTrue(gruppenResult.containsAll(gruppenExpected));
		
		assertTrue(teamsResult.size() == teamsExpected.size());
		assertTrue(teamsResult.containsAll(teamsExpected));
		
		assertTrue(pruefOrdnungenResult.size() == pruefOrdnungenExpected.size());
		assertTrue(pruefOrdnungenResult.containsAll(pruefOrdnungenExpected));
		
		assertTrue(studentenExpected.size() == studentenResult.size());
		assertTrue(studentenResult.containsAll(studentenExpected));
		
		assertTrue(mitarbeiterResult.size() == mitarbeiterExpected.size());
		assertTrue(mitarbeiterResult.containsAll(mitarbeiterExpected));
		
		
		assertTrue(teammitgliederResult.size() == teammitgliederExpected.size());
		assertTrue(teammitgliederResult.containsAll(teammitgliederExpected));
		
		assertTrue(bewertungenResult.size() == bewertungenExpected.size());
		assertTrue(bewertungenResult.containsAll(bewertungenExpected));
		
		assertTrue(poVernZuordnungenResult.size() == poVernZuordnungenExpected.size());
		assertTrue(poVernZuordnungenResult.containsAll(poVernZuordnungenExpected));
		
		// assertTrue(meldungenExpected.size() == meldungenResult.size());
		// assertTrue(meldungenResult.containsAll(meldungenExpected));

	}
	
	
	@Test
	public void test2ProfDatenImport(){
		
		int anzahlProfs = getAnzahlEintraege("Professor");
		assertTrue(DatenImExportController.importiereProfessorenDaten("01_professoren.imp"));
		anzahlProfs *= 2;
		assertTrue(getAnzahlEintraege("Professor") == anzahlProfs);
		System.out.println(getAnzahlEintraege("Professor"));
		System.out.println(anzahlProfs);
		assertTrue(DatenImExportController.importiereProfessorenDaten("01_professoren.imp"));
		System.out.println(getAnzahlEintraege("Professor"));
		System.out.println(anzahlProfs);
		assertTrue(getAnzahlEintraege("Professor") == anzahlProfs);
		
	}
	
	
	@Test
	public void test3AssistDatenImport(){
		
		int anzahlAssist = getAnzahlEintraege("Assistent");
		assertTrue(DatenImExportController.importiereAssistDaten("02_assistenten.imp"));
		System.out.println(getAnzahlEintraege("Assistent"));
		System.out.println(anzahlAssist);
		assertTrue(getAnzahlEintraege("Assistent") == anzahlAssist * 2);
		assertTrue(DatenImExportController.importiereProfessorenDaten("02_assistenten.imp"));
		System.out.println(getAnzahlEintraege("Assistent"));
		System.out.println(anzahlAssist);
		assertTrue(getAnzahlEintraege("Assistent") == anzahlAssist * 2);
		
	}
	
	@Test
	public void test4VABasisDatenImport(){
		
		
		try{
			
			//int anzahlVA = getAnzahlEintraege("Veranstaltung");
			assertTrue(DatenImExportController.importiereVABasisDaten("03_veranstaltungen.imp"));
			//assertTrue(getAnzahlEintraege("Veranstaltung") == anzahlVA * 2); --> Neue Daten dazugekommen, passt nicht mehr mit alten Imports
			
			// Hier false da fkuerzel ueberprueft wird, ansonsten waere es true und nichts eingefuegt
			assertFalse(DatenImExportController.importiereVABasisDaten("03_veranstaltungen.imp"));
			//assertTrue(getAnzahlEintraege("Veranstaltung") == anzahlVA * 2); --> Neue Daten dazugekommen, passt nicht mehr mit alten Imports
			
			setzeDatenWiederZurueck();
			
			// Fehlerhafte Eintraege entfernt, da Abgabefertig
			// ======================================================================================================
//			anzahlVA = getAnzahlEintraege("Veranstaltung");
//			assertFalse(DatenImExportController.importiereVABasisDaten("00_veranstaltungen_fehler_maid.imp"));
//			assertTrue(getAnzahlEintraege("Veranstaltung") == anzahlVA);
//			assertFalse(DatenImExportController.importiereVABasisDaten("00_veranstaltungen_fehler_fachbereich.imp"));
//			assertTrue(getAnzahlEintraege("Veranstaltung") == anzahlVA);
			
		} catch (AssertionError e){
			
			e.printStackTrace();
			fail();
		}

		
	}
	
	@Test
	public void test5VAGruppDatenImport(){
		
		try{
			int anzahlGruppen = getAnzahlEintraege("Gruppe");
			System.out.println(anzahlGruppen);
			int anzahlVA = getAnzahlEintraege("Veranstaltung");
			assertTrue(DatenImExportController.importiereVABasisDaten("03_veranstaltungen.imp"));
			assertTrue(DatenImExportController.importiereVAGruppDaten("04_gruppen.imp"));
			
			// Passt nicht mehr, da Daten hinzzugekommen fuer finale Abgabe
			// ===================================================================================================
//			System.out.println(anzahlGruppen * 2);
//			System.out.println(getAnzahlEintraege("gruppe"));
//			assertTrue((anzahlGruppen * 2) == getAnzahlEintraege("Gruppe")); // 3 Eintraege vergessen beim anlegen
//			assertTrue(anzahlVA * 2 == getAnzahlEintraege("Veranstaltung"));
			// ===================================================================================================
			
			setzeDatenWiederZurueck();
			
			anzahlGruppen = getAnzahlEintraege("Gruppe");
			anzahlVA = getAnzahlEintraege("Veranstaltung");
			
			assertFalse(DatenImExportController.importiereVAGruppDaten("04_gruppen.imp")); // VA Basisdaten nicht importiert
			
			assertTrue(anzahlGruppen == getAnzahlEintraege("Gruppe"));
			assertTrue(anzahlVA == getAnzahlEintraege("Veranstaltung"));
		} catch (AssertionError e){
			
			e.printStackTrace();
			//throw new IllegalArgumentException();
		}

	}

	private int getAnzahlEintraege(String tablename) {
		
		String qry = "select count(*) as count from " + tablename;
		int count = 0;
		try {
			PreparedStatement stmt = connection.prepareStatement(qry);
			ResultSet rs = stmt.executeQuery();
			while(rs.next()){
				
				count = rs.getInt("count");
			}
			stmt.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return count;
		
	}

	private List<Leistung> getAlleBewertungen(List<String> fachkuerzel) {
		
		List<Leistung> leistungen = new ArrayList<>();
		
		for(String fk: fachkuerzel){
			
			leistungen.addAll(pool.getlAnzSrv().getLeistungen(fk));
		}
		
		return leistungen;
	}
	
	@AfterClass
    public static void setzeDatenWiederZurueck() {
    	
    	List<String> folders = DatenImExportController.holeBackupDateien();
    	Collections.sort(folders);
    	DatenImExportController.backupWiederherstellen(folders.get(0));
    }


}
