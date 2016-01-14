//package se2.praktikum.projekt.helper;
//
//import java.sql.Connection;
//import java.sql.ResultSet;
//import java.sql.SQLException;
//import java.sql.Statement;
//import java.util.ArrayList;
//import java.util.List;
//
//import se2.praktikum.projekt.dbms.DBConnector;
//import se2.praktikum.projekt.models.gruppe.Gruppe;
//import se2.praktikum.projekt.models.gruppe.Termin;
//import se2.praktikum.projekt.models.gruppe.fachwerte.TerminID;
//import se2.praktikum.projekt.models.gruppe.fachwerte.Uhrzeit;
//import se2.praktikum.projekt.models.person.Assistent;
//import se2.praktikum.projekt.models.person.EMail;
//import se2.praktikum.projekt.models.person.Professor;
//import se2.praktikum.projekt.models.person.Student;
//import se2.praktikum.projekt.models.person.fachwerte.Adresse;
//import se2.praktikum.projekt.models.person.fachwerte.MAID;
//import se2.praktikum.projekt.models.person.fachwerte.MatrikelNr;
//import se2.praktikum.projekt.models.person.fachwerte.PLZ;
//import se2.praktikum.projekt.models.team.Team;
//import se2.praktikum.projekt.models.team.fachwerte.TeamID;
//import se2.praktikum.projekt.models.veranstaltung.Fach;
//import se2.praktikum.projekt.models.veranstaltung.Praktikum;
//import se2.praktikum.projekt.models.veranstaltung.Projekt;
//import se2.praktikum.projekt.models.veranstaltung.Veranstaltung;
//
///**
// * Klasse zum sammeln aller Hilfsmethoden
// * 
// * @author jan und Ahmad
// */
//public class HelpBox {
//	/**
//	 * erstellt ein Veranstaltungsobjekt für FKuerzel. Bei Bedarf um weitere
//	 * Veranstaltungen erweiterbar
//	 * 
//	 * @param FKuerzel
//	 *            der zu erstellenden Veranstaltung
//	 * @return vollständig gefülltest Veranstaltungsobjekt
//	 */
//	private static Connection connection = DBConnector.getConnection();
//
//	public static Veranstaltung createVeranstaltung(String fKuerzel) {
//		Veranstaltung result = null;
//		Professor professor;
//		Fach fach;
//
//		switch (fKuerzel) {
//		case ("PRP1"):
//			result = new Praktikum();
//			result.setAnzGr(3);
//			result.setAnzTm(1);
//			result.setMaxTeilnTeam(3);
//			result.setMinTeilnTeam(2);
//			professor = getProfessor("Schmolitzky");
//			result.setProfessor(professor);
//			fach = new Fach();
//			fach.setFachKuerzel(fKuerzel);
//			fach.setFachbereich("Angewandte Informatik");
//			fach.setfBKuerzel("AI");
//			fach.setFachBezeichnung("Programmieren Praktikum 1");
//			fach.setSemester(1);
//			result.setFach(fach);
//
//			break;
//		case ("POCTF"):
//
//			result = new Projekt();
//			result.setAnzGr(1);
//			result.setAnzTm(1);
//			result.setMaxTeilnTeam(3);
//			result.setMinTeilnTeam(2);
//			Professor prof = getProfessor("Kossakowski");
//			result.setProfessor(prof);
//			fach = new Fach();
//			fach.setFachKuerzel(fKuerzel);
//			fach.setFachbereich("Angewandte Informatik");
//			fach.setfBKuerzel("AI");
//			fach.setFachBezeichnung("Capture The Flag");
//			fach.setSemester(5);
//			result.setFach(fach);
//			break;
//
//		case ("WPPN"):
//
//			result = new Projekt();
//			result.setAnzGr(0);
//			result.setAnzTm(0);
//			result.setMaxTeilnTeam(0);
//			result.setMinTeilnTeam(0);
//			professor = getProfessor("Padberg");
//			result.setProfessor(professor);
//			fach = new Fach();
//			fach.setFachKuerzel(fKuerzel);
//			fach.setFachbereich("Angewandte Informatik");
//			fach.setfBKuerzel("AI");
//			fach.setFachBezeichnung("Wahlpflicht Petrinetze");
//			fach.setSemester(4);
//			result.setFach(fach);
//			break;
//		case ("PRP1b"):
//			result = new Praktikum();
//			result.setAnzGr(3);
//			result.setAnzTm(3);
//			result.setMaxTeilnTeam(3);
//			result.setMinTeilnTeam(2);
//			professor = getProfessor("Schmolitzky");
//			result.setProfessor(professor);
//			fach = new Fach();
//			fach.setFachKuerzel(fKuerzel);
//			fach.setFachbereich("Angewandte Informatik");
//			fach.setfBKuerzel("AI");
//			fach.setFachBezeichnung("Programmieren Praktikum 1");
//			fach.setSemester(1);
//			result.setFach(fach);
//
//			break;
//		}
//		return result;
//
//	}
//
//	/**
//	 * erstellt ein Professorobjekt für Nachnamen. Bei Bedarf um weitere
//	 * Professoren erweiterbar.
//	 * 
//	 * @param name
//	 *            Nachname des Professors
//	 * @return Professorobjekt vollständig gefüllt(hardcoded)
//	 */
//
//	public static Professor getProfessor(String name) {
//		Professor professor = null;
//		MAID maid;
//		PLZ plz;
//		Adresse adresse;
//		EMail email;
//		String vorname;
//		String nachname;
//		switch (name) {
//		case ("Schmolitzky"):
//			professor = new Professor();
//			plz = PLZ.getPostLeitzahl(""+21762);
//			adresse = Adresse.getAdresse("Kalten Muehlen", 4, plz, "Hamburg");
//			email = new EMail("axel.schmolitzky@haw-hamburg.de");
//			vorname = "Axel";
//			nachname = "Schmolitzky";
//			professor.setAdresse(adresse);
//			professor.setBenutzername("oaa003");
//			professor.setDepartment("I");
//			professor.setEMail(email);
//			// Wenn bearbeitet !!!!!!!!!!!!!!!!!!!!!!!!!!!!!
//			// professor.setGebDatum("");
//			professor.setGebOrt("Berlin");
//			maid = MAID.getMAID(3);
//			professor.setMaID(maid);
//			professor.setNachname(nachname);
//			professor.setVorname(vorname);
//			professor.setVollerName(vorname + " " + nachname);
//			break;
//
//		case ("Gerken"):
//			professor = new Professor();
//			PLZ plz2 = PLZ.getPostLeitzahl(""+20743);
//			Adresse adresse2 = Adresse.getAdresse("Ruebenhall", 32, plz2, "Hamburg");
//			EMail email2 = new EMail("wolfgang.gerken@haw-hamburg.de");
//			String vorname2 = "Wolfgang";
//			String nachname2 = "Gerken";
//			professor.setAdresse(adresse2);
//			professor.setBenutzername("oaa004");
//			professor.setDepartment("I");
//			professor.setEMail(email2);
//			// Wenn bearbeitet !!!!!!!!!!!!!!!!!!!!!!!!!!!!!
//			// professor.setGebDatum("6-29-1963");
//			professor.setGebOrt("Bonn");
//			MAID maid2 = MAID.getMAID(4);
//			professor.setMaID(maid2);
//			professor.setNachname(nachname2);
//			professor.setVorname(vorname2);
//			professor.setVollerName(vorname2 + " " + nachname2);
//			break;
//		case ("Kossakowski"):
//			professor = new Professor();
//			plz = PLZ.getPostLeitzahl(""+20743);
//			adresse = Adresse.getAdresse("Blumenstrasse", 65, plz, "Hamburg");
//			EMail email3 = new EMail("klaus-peter.kossakowski@haw-hamburg.de");
//			vorname = "Klaus-Peter";
//			nachname = "Kossakowski";
//			professor.setAdresse(adresse);
//			professor.setBenutzername("oaa006");
//			professor.setDepartment("I");
//			professor.setEMail(email3);
//			// Wenn bearbeitet !!!!!!!!!!!!!!!!!!!!!!!!!!!!!
//			// professor.setGebDatum("6-29-1963");
//			professor.setGebOrt("Hamburg");
//			maid = MAID.getMAID(6);
//			professor.setMaID(maid);
//			professor.setNachname(nachname);
//			professor.setVorname(vorname);
//			professor.setVollerName(vorname + " " + nachname);
//			break;
//
//		case ("Padberg"):
//			professor = new Professor();
//			plz = PLZ.getPostLeitzahl(""+23412);
//			adresse = Adresse.getAdresse("Bluetenallee", 142, plz, "Hamburg");
//			email = new EMail("julia.padberg@haw-hamburg.de");
//			vorname = "Julia";
//			nachname = "Kossakowski";
//			professor.setAdresse(adresse);
//			professor.setBenutzername("oaa002");
//			professor.setDepartment("I");
//			professor.setEMail(email);
//			// Wenn bearbeitet !!!!!!!!!!!!!!!!!!!!!!!!!!!!!
//			// professor.setGebDatum("6-29-1963");
//			professor.setGebOrt("Hamburg");
//			maid = MAID.getMAID(2);
//			professor.setMaID(maid);
//			professor.setNachname(nachname);
//			professor.setVorname(vorname);
//			professor.setVollerName(vorname + " " + nachname);
//			break;
//
//		case ("Huebner"):
//			professor = new Professor();
//			plz = PLZ.getPostLeitzahl(""+22743);
//			adresse = Adresse.getAdresse("Neue Str.", 10, plz, "Hamburg");
//			email = new EMail("martin.huebner@haw-hamburg.de");
//			vorname = "Martin";
//			nachname = "Huebner";
//			professor.setAdresse(adresse);
//			professor.setBenutzername("oaa001");
//			professor.setDepartment("I");
//			professor.setEMail(email);
//			// Wenn bearbeitet !!!!!!!!!!!!!!!!!!!!!!!!!!!!!
//			// professor.setGebDatum("6-29-1963");
//			professor.setGebOrt("Frankfurt");
//			maid = MAID.getMAID(1);
//			professor.setMaID(maid);
//			professor.setNachname(nachname);
//			professor.setVorname(vorname);
//			professor.setVollerName(vorname + " " + nachname);
//			break;
//		}
//
//		return professor;
//	}
//
//	public static Gruppe getGruppe(String name) {
//		Gruppe grp = new Gruppe();
//
//		int anzT;
//		Assistent assi;
//		Professor prof;
//		int grpNr;
//		String fKuerzel;
//		int maxTeams;
//		List<Termin> termine;
//
//		switch (name) {
//		case ("PRP1 1"):
//			anzT = 0;
//			assi = getAssistent("Blank");
//			prof = getProfessor("Huebner");
//			grpNr = 1;
//			fKuerzel = "PRP1";
//			maxTeams = 14;
//			termine = getTermine("PRP1 1");
//
//			grp.setAnzTeams(anzT);
//			grp.setAssistent(assi);
//			grp.setFachkuerzel(fKuerzel);
//			grp.setGrpNr(grpNr);
//			grp.setMaxTeams(maxTeams);
//			grp.setProfessor(prof);
//			grp.setTermine(termine);
//			break;
//		case ("PRP1 2"):
//			anzT = 1;
//			assi = getAssistent("Blank");
//			prof = getProfessor("Huebner");
//			grpNr = 2;
//			fKuerzel = "PRP1";
//			maxTeams = 14;
//			termine = getTermine("PRP1 2");
//
//			grp.setAnzTeams(anzT);
//			grp.setAssistent(assi);
//			grp.setFachkuerzel(fKuerzel);
//			grp.setGrpNr(grpNr);
//			grp.setMaxTeams(maxTeams);
//			grp.setProfessor(prof);
//			grp.setTermine(termine);
//			break;
//
//		}
//		return grp;
//	}
//
//	private static List<Termin> getTermine(String name) {
//		List<Termin> termine = new ArrayList<Termin>();
//		Termin termin = new Termin();
//		Uhrzeit uhrzeitEnde;
//		Uhrzeit uhrzeitAnfang;
//		TerminID id;
//		switch (name) {
//		case ("PRP1 1"): 
//			termin.setTerminID(TerminID.getTerminID("PRP1G1T1"));
//			uhrzeitAnfang = new Uhrzeit(8, 15);
//			uhrzeitEnde = new Uhrzeit(11, 30);
//			termin.setEnde(uhrzeitEnde);
//			termin.setStart(uhrzeitAnfang);
//			termine.add(termin);
//			break;
//		case ("PRP1 2"): 
//			termin.setTerminID(TerminID.getTerminID("PRP1G1T2"));
//			uhrzeitAnfang = new Uhrzeit(8, 15);
//			uhrzeitEnde = new Uhrzeit(11, 30);
//			termin.setEnde(uhrzeitEnde);
//			termin.setStart(uhrzeitAnfang);
//			termine.add(termin);
//		}
//		
//
//		return termine;
//	}
//
//	public static Assistent getAssistent(String name) {
//		MAID maid;
//		PLZ plz;
//		Adresse adresse;
//		EMail email;
//		String vorname;
//		String nachname;
//		Assistent assistent = new Assistent();
//		switch (name) {
//		case ("Blank"):
//			plz = PLZ.getPostLeitzahl(""+22223);
//			adresse = Adresse.getAdresse("Ericksenstrasse", 23, plz, "Hamburg");
//			email = new EMail("ilona.blank@haw-hamburg.de");
//			vorname = "Ilona";
//			nachname = "Blank";
//			assistent.setAdresse(adresse);
//			assistent.setBenutzername("maa001");
//			assistent.setDepartment("I");
//			assistent.setEMail(email);
//			// Wenn bearbeitet !!!!!!!!!!!!!!!!!!!!!!!!!!!!!
//			// professor.setGebDatum("6-29-1963");
//			assistent.setGebOrt("Dortmund");
//			maid = MAID.getMAID(11);
//			assistent.setMaID(maid);
//			assistent.setNachname(nachname);
//			assistent.setVorname(vorname);
//			assistent.setVollerName(vorname + " " + nachname);
//			break;
//		}
//
//		return assistent;
//	}
//
//	public static Team getTeam(String string) {
//		Team team = new Team();
//		int grpNr;
//		int maxTeiln;
//		int minTeiln;
//		List<Student> mitglieder = new ArrayList<Student>();
//		TeamID iD;
//		switch (string) {
//		case ("POCTF 1"):
//			grpNr = 1;
//			maxTeiln = 3;
//			minTeiln = 2;
//			iD = TeamID.getTeamID(""+21);
//			team.setGrpNr(grpNr);
//			team.setMaxTeiln(maxTeiln);
//			team.setMinTeiln(minTeiln);
//			team.setTeamID(iD);
//			mitglieder.add(getStudent("1111113"));
//			mitglieder.add(getStudent("1111114"));
//			team.setMitglieder(mitglieder);
//			break;
//		case ("PRP1 2"):
//			grpNr = 2;
//			maxTeiln = 3;
//			minTeiln = 2;
//			
//			iD = TeamID.getTeamID(""+2);
//			team.setGrpNr(grpNr);
//			team.setMaxTeiln(maxTeiln);
//			team.setMinTeiln(minTeiln);
//			team.setTeamID(iD);
//			mitglieder.add(getStudent("1111123"));
//			mitglieder.add(getStudent("1111122"));
//			team.setMitglieder(mitglieder);
//			break;
//		}
//
//		return team;
//
//	}
//
//	public static Student getStudent(String name) {
//		Student student = new Student();
//		MatrikelNr matrNr;
//		PLZ plz;
//		Adresse adresse;
//		EMail email;
//		String vorname;
//		String nachname;
//		String fachbereich;
//		String Department;
//		String po;
//		switch (name) {
//		case ("1111113"):
//			plz = PLZ.getPostLeitzahl(""+22034);
//			adresse = Adresse.getAdresse("Kleine Born", 43, plz, "Hamburg");
//			email = new EMail("achmed.swolsyki@haw-hamburg.de");
//			vorname = "Achmed";
//			nachname = "Swolyski";
//			po = "IPO2008";
//			student.setAdresse(adresse);
//			student.setBenutzername("aaa003");
//			student.setDepartment("I");
//			student.setEMail(email);
//			student.setPo(po);
//			// Wenn bearbeitet !!!!!!!!!!!!!!!!!!!!!!!!!!!!!
//			// professor.setGebDatum("6-29-1963");
//			student.setGebOrt("Moskau");
//			matrNr = MatrikelNr.getMatrikelNr(1111113);
//			student.setMatrNr(matrNr);
//			student.setNachname(nachname);
//			student.setVorname(vorname);
//			student.setVollerName(vorname + " " + nachname);
//			break;
//		case ("1111114"):
//			plz = PLZ.getPostLeitzahl(""+21075);
//			adresse = Adresse.getAdresse("Buente", 26, plz, "Hamburg");
//			email = new EMail("james.bond@haw-hamburg.de");
//			vorname = "James";
//			nachname = "Bond";
//			po = "IPO2008";
//			student.setAdresse(adresse);
//			student.setBenutzername("aaa004");
//			student.setDepartment("I");
//			student.setEMail(email);
//			student.setPo(po);
//			// Wenn bearbeitet !!!!!!!!!!!!!!!!!!!!!!!!!!!!!
//			// professor.setGebDatum("6-29-1963");
//			student.setGebOrt("Hamburg");
//			matrNr = MatrikelNr.getMatrikelNr(1111114);
//			student.setMatrNr(matrNr);
//			student.setNachname(nachname);
//			student.setVorname(vorname);
//			student.setVollerName(vorname + " " + nachname);
//			break;
//		case ("1111122"):
//			plz = PLZ.getPostLeitzahl(""+23456);
//			adresse = Adresse.getAdresse("Brandstrasse", 112, plz, "Hamburg");
//			email = new EMail("patrick.schuller@haw-hamburg.de");
//			vorname = "Patrick";
//			nachname = "Schuller";
//			po = "IPO2014";
//			student.setAdresse(adresse);
//			student.setBenutzername("aaa012");
//			student.setDepartment("I");
//			student.setEMail(email);
//			student.setPo(po);
//			// Wenn bearbeitet !!!!!!!!!!!!!!!!!!!!!!!!!!!!!
//			// professor.setGebDatum("6-29-1963");
//			student.setGebOrt("Hamburg");
//			matrNr = MatrikelNr.getMatrikelNr(1111122);
//			student.setMatrNr(matrNr);
//			student.setNachname(nachname);
//			student.setVorname(vorname);
//			student.setVollerName(vorname + " " + nachname);
//			break;
//		case ("1111123"):
//			plz = PLZ.getPostLeitzahl(""+21432);
//			adresse = Adresse.getAdresse("Königinnenweg", 12, plz, "Hamburg");
//			email = new EMail("sercy.lannister@haw-hamburg.de");
//			vorname = "Sercy";
//			nachname = "Lannister";
//			po = "IPO2014";
//			student.setAdresse(adresse);
//			student.setBenutzername("aaa013");
//			student.setDepartment("I");
//			student.setEMail(email);
//			student.setPo(po);
//			// Wenn bearbeitet !!!!!!!!!!!!!!!!!!!!!!!!!!!!!
//			// professor.setGebDatum("6-29-1963");
//			student.setGebOrt("Koenigslande");
//			matrNr = MatrikelNr.getMatrikelNr(1111123);
//			student.setMatrNr(matrNr);
//			student.setNachname(nachname);
//			student.setVorname(vorname);
//			student.setVollerName(vorname + " " + nachname);
//			break;
//		}
//
//		return student;
//	}
//
//	public static boolean istVeranstaltungEnthalten(Veranstaltung va) {
//
//		boolean result = false;
//		Statement statement = null;
//		String fKuerzel = va.getFach().getFachKuerzel();
//		String qry = "select * from Veranstaltung where FKuerzel = '" + fKuerzel + "'";
//
//		
//		try {
//			statement = connection.createStatement();
//			ResultSet rs = statement.executeQuery(qry);
//			while (rs.next()) {
//				result = true;
//			}
//			statement.close();
//		} catch (SQLException e) {
//			e.printStackTrace();
//		} finally {
//			return result;
//		}
//	}
//
//}
