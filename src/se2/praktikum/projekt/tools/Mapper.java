package se2.praktikum.projekt.tools;

import java.io.IOException;
import java.util.List;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

import se2.praktikum.projekt.datenimexport.backuptables.DBAnmeldetermin;
import se2.praktikum.projekt.datenimexport.backuptables.DBDepartment;
import se2.praktikum.projekt.datenimexport.backuptables.DBEinzelanmeldung;
import se2.praktikum.projekt.datenimexport.backuptables.DBFachbereich;
import se2.praktikum.projekt.datenimexport.backuptables.DBPOVeranZuordung;
import se2.praktikum.projekt.datenimexport.backuptables.DBPruefungsordnung;
import se2.praktikum.projekt.datenimexport.backuptables.DBTeammitglied;
import se2.praktikum.projekt.models.gruppe.Gruppe;
import se2.praktikum.projekt.models.leistungen.Leistung;
import se2.praktikum.projekt.models.meldungen.GruppevollMeldung;
import se2.praktikum.projekt.models.meldungen.Meldung;
import se2.praktikum.projekt.models.meldungen.TEAblehnung;
import se2.praktikum.projekt.models.meldungen.TeamAustrittsmeldung;
import se2.praktikum.projekt.models.meldungen.Teameinladung;
import se2.praktikum.projekt.models.person.Assistent;
import se2.praktikum.projekt.models.person.Professor;
import se2.praktikum.projekt.models.person.Student;
import se2.praktikum.projekt.models.person.VerwaltMitarbeiter;
import se2.praktikum.projekt.models.team.Team;
import se2.praktikum.projekt.models.veranstaltung.Fach;
import se2.praktikum.projekt.models.veranstaltung.Praktikum;
import se2.praktikum.projekt.models.veranstaltung.Projekt;
import se2.praktikum.projekt.models.veranstaltung.Veranstaltung;
import se2.praktikum.projekt.models.veranstaltung.WP;

/**
 * Konvertiert einen übergebenen JSON-String zu der benötigten Klasse
 * @author jan
 *
 */
public class Mapper {
	
	private static ObjectMapper mapper = new ObjectMapper();
	
	public static Gruppe mapGruppe(String obj){
		
		config();
		Gruppe gr = null;
		
		try {
			gr = mapper.readValue(obj, Gruppe.class);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return gr;
	}

	public static Team mapTeam(String team) {
		
		config();
		
		Team t = null;
		
		try {
			t = mapper.readValue(team, Team.class);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return t;
	}
	
	public static List<Team> mapTeamList(String teams) {
		
		config();
		
		List<Team> teamsObj = null;
		
		try {
			teamsObj = mapper.readValue(teams, mapper.getTypeFactory().constructCollectionType(List.class, Team.class));
		} catch (IOException e) {
			e.printStackTrace();
		}
		return teamsObj;
	}
	
	
	public static Praktikum mapPaktikum(String praktikum) {
		
		config();
		
		Praktikum p = null;
		
		try {
			p = mapper.readValue(praktikum, Praktikum.class);
		} catch (IOException e) {
			ErrorLogger.log(e);
		}
		return p;
	}
	
	public static Professor mapProfessor(String professor) {
		
		config();
		
		Professor p = null;
		
		try {
			p = mapper.readValue(professor, Professor.class);
		} catch (IOException e) {
			ErrorLogger.log(e);
			return null;
		}
		return p;
	}
	
	
	public static Fach mapFach(String fach) {
		
		config();
		
		Fach f = null;
		
		try {
			f = mapper.readValue(fach, Fach.class);
		} catch (IOException e) {
			ErrorLogger.log(e);
		}
		return f;
	}
	
	private static void config(){
		
		mapper.configure(SerializationFeature.INDENT_OUTPUT, true);
		mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
	}

	
	public static DBDepartment mapDBDepartment(String dbDepartment) {
		
		config();
		
		DBDepartment dbdp = null;
		
		try {
			dbdp = mapper.readValue(dbDepartment, DBDepartment.class);
		} catch (IOException e) {
			ErrorLogger.log(e);
		}
		
		return dbdp;
		
	}

	public static DBFachbereich mapDBFachbereich(String dbFachbereich) {
		
		config();
		
		DBFachbereich dbfb = null;
		
		try {
			dbfb = mapper.readValue(dbFachbereich, DBFachbereich.class);
		} catch (IOException e) {
			ErrorLogger.log(e);
		}
		
		return dbfb;
	}

	public static Assistent mapAssistent(String assistent) {

		
		config();
		
		Assistent a = null;
		
		try {
			a = mapper.readValue(assistent, Assistent.class);
		} catch (IOException e) {
			ErrorLogger.log(e);
		}
		return a;
	}

	public static Veranstaltung mapWP(String wp) {

		config();
		
		WP p = null;
		
		try {
			p = mapper.readValue(wp, WP.class);
		} catch (IOException e) {
			ErrorLogger.log(e);
		}
		return p;
	}

	public static Veranstaltung mapPO(String projekt) {

		config();
		
		Projekt po = null;
		
		try {
			po = mapper.readValue(projekt, Projekt.class);
		} catch (IOException e) {
			ErrorLogger.log(e);
		}
		return po;
	}

	public static DBAnmeldetermin mapAnmeldetermin(String anmTermin) {
		config();
		
		DBAnmeldetermin termin = null;
		
		try {
			termin = mapper.readValue(anmTermin, DBAnmeldetermin.class);
		} catch (IOException e) {
			ErrorLogger.log(e);
		}
		return termin;
	}

	public static DBTeammitglied mapTeammitglied(String teammitglied) {
		
		config();
		
		DBTeammitglied teammg = null;
		
		try {
			teammg = mapper.readValue(teammitglied, DBTeammitglied.class);
		} catch (IOException e) {
			ErrorLogger.log(e);
		}
		return teammg;
	}

	public static Student mapStudent(String student) {
		
		config();
		
		Student stud = null;
		try {
			stud = mapper.readValue(student, Student.class);
		} catch (JsonParseException e) {
			ErrorLogger.log(e);
			return null;
		} catch (JsonMappingException e) {
			ErrorLogger.log(e);
			return null;
		} catch (IOException e) {
			ErrorLogger.log(e);
			return null;
		}
		return stud;
	}

	public static Leistung mapLeistung(String leistung) {
		
		config();
		
		Leistung leist = null;
		
		try {
			leist = mapper.readValue(leistung, Leistung.class);
		} catch (IOException e) {
			ErrorLogger.log(e);
		}
		return leist;
	}

	public static DBPOVeranZuordung mapPOVeranZuOrdn(String dbpovz) {

		config();
		
		DBPOVeranZuordung povz = null;
		
		try {
			povz = mapper.readValue(dbpovz, DBPOVeranZuordung.class);
		} catch (IOException e) {
			ErrorLogger.log(e);
		}
		return povz;
	}

	public static DBPruefungsordnung mapPruefOrdnung(String pruefordn) {
		
		config();
		
		DBPruefungsordnung po = null;
		
		try {
			po = mapper.readValue(pruefordn, DBPruefungsordnung.class);
		} catch (IOException e) {
			ErrorLogger.log(e);
		}
		return po;
	}

	public static Meldung mapTeameinladung(String teinladung) {

		config();
		
		Teameinladung teinl = null;
		
		try {
			teinl = mapper.readValue(teinladung, Teameinladung.class);
		} catch (IOException e) {
			ErrorLogger.log(e);
		}
		return teinl;
	}

	public static Meldung mapTeamAustrittsmeldung(String tAustrittsmeldung) {
		
		config();
		
		TeamAustrittsmeldung austr = null;
		
		try {
			austr = mapper.readValue(tAustrittsmeldung, TeamAustrittsmeldung.class);
		} catch (IOException e) {
			ErrorLogger.log(e);
		}
		return austr;
	}

	public static Meldung mapTEAblehnung(String line) {
		
		config();
		
		TEAblehnung abl = null;
		
		try {
			abl = mapper.readValue(line, TEAblehnung.class);
		} catch (IOException e) {
			ErrorLogger.log(e);
		}
		return abl;
	}

	public static Meldung mapGruppVollMeldung(String line) {

		
		config();
		
		GruppevollMeldung gruppVoll = null;
		
		try {
			gruppVoll = mapper.readValue(line, GruppevollMeldung.class);
		} catch (IOException e) {
			ErrorLogger.log(e);
		}
		return gruppVoll;
	}

	public static VerwaltMitarbeiter mapVerwMitarbeiter(String line) {
		
		config();
		
		VerwaltMitarbeiter ma = null;
		
		try {
			ma = mapper.readValue(line, VerwaltMitarbeiter.class);
		} catch (IOException e) {
			ErrorLogger.log(e);
		}
		return ma;
	}

	public static DBEinzelanmeldung mapEinzelanmeldung(String line) {

		config();
		DBEinzelanmeldung einzel = null;
		
		try {
			einzel = mapper.readValue(line, DBEinzelanmeldung.class);
		} catch (IOException e) {
			ErrorLogger.log(e);
		}
		return einzel;
	}

}
