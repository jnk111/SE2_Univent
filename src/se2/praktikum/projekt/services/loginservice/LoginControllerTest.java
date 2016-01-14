package se2.praktikum.projekt.services.loginservice;

import static org.junit.Assert.assertTrue;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;

import org.junit.BeforeClass;
import org.junit.Test;
import org.springframework.web.servlet.ModelAndView;

import se2.praktikum.projekt.models.person.EMail;
import se2.praktikum.projekt.models.person.IPerson;
import se2.praktikum.projekt.models.person.Professor;
import se2.praktikum.projekt.models.person.Student;
import se2.praktikum.projekt.models.person.VerwaltMitarbeiter;
import se2.praktikum.projekt.models.person.fachwerte.Adresse;
import se2.praktikum.projekt.models.person.fachwerte.MAID;
import se2.praktikum.projekt.models.person.fachwerte.MatrikelNr;
import se2.praktikum.projekt.models.person.fachwerte.PLZ;
import se2.praktikum.projekt.testresource.DbTestLogin;

public class LoginControllerTest {
	

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		
		
	}

	@Test
	public void testLoginLogout() {
		
		
		// Student
		IPerson student = new Student(MatrikelNr.getMatrikelNr(1111111),"Martin", "Schmidt");
		student.setBenutzername("aaa001");
		student.setPasswort(student.getBenutzername());
		String panel = LoginController.einloggen(new ArrayList<>(Arrays.asList(student.getBenutzername(), student.getPasswort(), DbTestLogin.A_KENNUNG, DbTestLogin.PASSWORT)));
		assertTrue(panel != null);
		assertTrue(panel.equals("student"));
		assertTrue(LoginController.getloginSrv().getUser() != null);
		assertTrue(LoginController.getloginSrv().getUser() instanceof Student);
		
		ModelAndView mv = LoginController.ausloggen(""+((Student) student).getMatrNr().getMatrNr());
		assertTrue(mv.getModel().equals(new ModelAndView("login").getModel()));
		
		student.setPasswort("falschespw");
		panel = LoginController.einloggen(new ArrayList<>(Arrays.asList(student.getBenutzername(), student.getPasswort(), DbTestLogin.A_KENNUNG, DbTestLogin.PASSWORT)));
		assertTrue(panel == null);
		assertTrue(LoginController.getloginSrv().getUser() == null);
		student.setPasswort(student.getBenutzername());
		student.setBenutzername("falscher_benutzername");
		panel = LoginController.einloggen(new ArrayList<>(Arrays.asList(student.getBenutzername(), student.getPasswort(), DbTestLogin.A_KENNUNG, DbTestLogin.PASSWORT)));
		assertTrue(panel == null);
		assertTrue(LoginController.getloginSrv().getUser() == null);
		
		// Professor
		IPerson professor = new Professor(MAID.getMAID(1), "Martin", "Huebner");
		professor.setBenutzername("oaa001");
		professor.setPasswort(professor.getBenutzername());
		
		panel = LoginController.einloggen(new ArrayList<>(Arrays.asList(professor.getBenutzername(), professor.getPasswort(), DbTestLogin.A_KENNUNG, DbTestLogin.PASSWORT)));
		assertTrue(panel != null);
		assertTrue(panel.equals("angestellter"));
		assertTrue(LoginController.getloginSrv().getUser() != null);
		assertTrue(LoginController.getloginSrv().getUser() instanceof Professor);
		
		mv = LoginController.ausloggen("" + MAID.getMAID(1).getId());
		assertTrue(mv.getModel().equals(new ModelAndView("login").getModel()));
		
		professor.setPasswort("falschespw");
		panel = LoginController.einloggen(new ArrayList<>(Arrays.asList(professor.getBenutzername(), professor.getPasswort(), DbTestLogin.A_KENNUNG, DbTestLogin.PASSWORT)));
		assertTrue(panel == null);
		assertTrue(LoginController.getloginSrv().getUser() == null);
		
		professor.setPasswort(professor.getBenutzername());
		professor.setBenutzername("falscher_benutzername");
		
		panel = LoginController.einloggen(new ArrayList<>(Arrays.asList(professor.getBenutzername(), professor.getPasswort(), DbTestLogin.A_KENNUNG, DbTestLogin.PASSWORT)));
		assertTrue(panel == null);
		assertTrue(LoginController.getloginSrv().getUser() == null);
		
		
		// Verwaltungsmitarbeiter
		// 1, 'naa001', 'Maria', 'Albricht',
		IPerson verwMitarbeiter = new Professor(MAID.getMAID(1), "Maria", "Albricht");
		verwMitarbeiter.setBenutzername("naa001");
		verwMitarbeiter.setPasswort(verwMitarbeiter.getBenutzername());
		
		panel = LoginController.einloggen(new ArrayList<>(Arrays.asList(verwMitarbeiter.getBenutzername(), verwMitarbeiter.getPasswort(), DbTestLogin.A_KENNUNG, DbTestLogin.PASSWORT)));
		assertTrue(panel != null);
		assertTrue(panel.equals("angestellter"));
		assertTrue(LoginController.getloginSrv().getUser() != null);
		assertTrue(LoginController.getloginSrv().getUser() instanceof VerwaltMitarbeiter);
		
		mv = LoginController.ausloggen(MAID.getMAID(1).getId() +"");
		assertTrue(mv.getModel().equals(new ModelAndView("login").getModel()));
		
		verwMitarbeiter.setPasswort("falschespw");
		panel = LoginController.einloggen(new ArrayList<>(Arrays.asList(verwMitarbeiter.getBenutzername(), verwMitarbeiter.getPasswort(), DbTestLogin.A_KENNUNG, DbTestLogin.PASSWORT)));
		assertTrue(panel == null);
		assertTrue(LoginController.getloginSrv().getUser() == null);
		
		verwMitarbeiter.setPasswort(verwMitarbeiter.getBenutzername());
		verwMitarbeiter.setBenutzername("falscher_benutzername");
		
		panel = LoginController.einloggen(new ArrayList<>(Arrays.asList(verwMitarbeiter.getBenutzername(), verwMitarbeiter.getPasswort(), DbTestLogin.A_KENNUNG, DbTestLogin.PASSWORT)));
		assertTrue(panel == null);
		assertTrue(LoginController.getloginSrv().getUser() == null);

	}
	
	@Test
	public void testHoleProfilInformationen(){
		
		// insert INTO Professor values(1, 'oaa001', 'Martin', 'Huebner', 
		// 'Professor Dr.', 'oaa001', 'Neue Str.', 10, '22743', 'Hamburg', 
		// TO_DATE('8-12-1965','MM-DD-YYYY'), 'Frankfurt', 'martin.huebner@haw-hamburg.de', 'I');
		
		// insert INTO Assistent values(11, 'maa001', 'Ilona', 'Blank', 'maa001', 
		// 'EricksenstrStasse', 23, '22223', 'Hamburg',TO_DATE('2-23-1981','MM-DD-YYYY'), 
		// 'Dortmund', 'ilona.blank@haw-hamburg.de', 'I');
		
		// insert INTO Student values(1111111, 'aaa001', 'Martin', 'Schmidt', 'aaa001', 
		// 'IPO2008', 'Kleine Strasse', 3, '21073', 'Hamburg', TO_DATE('12-5-1989',
		// 'MM-DD-YYYY'), 'Berlin', 'martin.schmidt@haw-hamburg.de', 'AI', 'I');
		
		// insert INTO Verwaltungsmitarbeiter values(1, 'naa001', 'Maria', 'Albricht', 
		// 'naa001', 'Frankfurterallee', 45, '22742', 'Hamburg', 
		// TO_DATE('2-14-1982','MM-DD-YYYY'), 'Hamburg', 'maria.albricht@haw-hamburg.de', 'I');
		
		MatrikelNr matrNrExpected = MatrikelNr.getMatrikelNr(1111111);
		String usernameExpected = "aaa001";
		String vornameExpected = "Martin";
		String nachnameExpected = "Schmidt";
		String poExpected = "IPO2008";
		String strasse = "Kleine Strasse";
		int hausNr = 3;
		String stadt = "Hamburg";
		PLZ plz = PLZ.getPostLeitzahl("21073");
		Adresse adresseExpected = Adresse.getAdresse(strasse, hausNr, plz, stadt);
		SimpleDateFormat format = new SimpleDateFormat("dd-MM-yyyy");
		Date gebDatumExpected = null;
		
		try {
			
			gebDatumExpected = format.parse("05-12-1989");
			
		} catch (ParseException e) {
			e.printStackTrace();
		} 
		
		String gebOrtExpected = "Berlin";
		EMail emailExpected = new EMail("martin.schmidt@haw-hamburg.de");
		String fachbereichExpected = "AI";
		String depExpected = "I";
		
		Student studExpected = new Student(matrNrExpected, 
											vornameExpected, nachnameExpected, 
											usernameExpected, null, gebDatumExpected, 
											gebOrtExpected, adresseExpected, depExpected, 
											fachbereichExpected, emailExpected);
		
		// Student
		IPerson student = new Student(MatrikelNr.getMatrikelNr(1111111),"Martin", "Schmidt");
		student.setBenutzername("aaa001");
		student.setPasswort(student.getBenutzername());
		LoginController.einloggen(new ArrayList<>(Arrays.asList(student.getBenutzername(), student.getPasswort(), DbTestLogin.A_KENNUNG, DbTestLogin.PASSWORT)));
		IPerson studResult = LoginController.holeProfilInformationen();
		assertTrue(studResult.equals(studExpected));
		assertTrue(studResult.getBenutzername().equals(usernameExpected));
		assertTrue(((Student) studResult).getPo().equals(poExpected));
		assertTrue(studResult.getAdresse().equals(adresseExpected));
		assertTrue(studResult.getGebOrt().equals(gebOrtExpected));
		assertTrue(studResult.getEMail().equals(emailExpected));
		assertTrue(((Student) studResult).getFachbereich().equals(fachbereichExpected));
		assertTrue(studResult.getDepartment().equals(depExpected));
	}

}
