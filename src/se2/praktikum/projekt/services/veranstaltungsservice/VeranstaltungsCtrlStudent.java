package se2.praktikum.projekt.services.veranstaltungsservice;

import java.sql.Connection;
import java.util.List;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import se2.praktikum.projekt.dbms.DBConnector;
import se2.praktikum.projekt.models.gruppe.Gruppe;
import se2.praktikum.projekt.models.meldungen.Meldung;
import se2.praktikum.projekt.models.meldungen.Teameinladung;
import se2.praktikum.projekt.models.person.Student;
import se2.praktikum.projekt.models.person.fachwerte.MatrikelNr;
import se2.praktikum.projekt.models.person.fachwerte.UserID;
import se2.praktikum.projekt.models.team.Team;
import se2.praktikum.projekt.models.team.fachwerte.TeamID;
import se2.praktikum.projekt.services.loginservice.ServicePool;
import se2.praktikum.projekt.tools.Mapper;

@Controller
public class VeranstaltungsCtrlStudent {
	
	private static Connection connection;
	private static ServicePool servicePool;
	
	public void connect(){
		setConnection(DBConnector.getConnection());
	}

	/**
	 * Ermittelt alle Gruppen fuer ein Pflichtpraktikum innerhalb der Teamanmeldefrist 
	 * fuer Pflichtpraktika aus der Datenbank fuer das uebergebene Fachkeurzel. Ermittelt 
	 * nur Gruppen, die noch  Teamkapazitaet frei haben und fuer die der Student noch keine 
	 * erfolgreiche PVL-Leistung erhalten und für die er noch nicht mit einem anderen Team 
	 * angemeldet ist.
	 * 
	 * @return : Liste von Gruppen fuer die Teamanmeldung
	 */
	@RequestMapping(value="/gruppenTeamPraktikum/{id}", method=RequestMethod.POST)
	public static @ResponseBody List<Gruppe> getFreieGruppenTeamPraktikum(@RequestBody String fachkuerzel,
																		  @PathVariable(value = "id") String id){

		return servicePool.getStudentVerwSrv().getGruppenTeam(fachkuerzel, "Praktikum", MatrikelNr.getMatrikelNr(Integer.parseInt(id)));
	}
	
	/**
	 * Ermittelt alle Gruppen fuer ein Pflichtpraktikum innerhalb der Einzelanmeldefrist aus 
	 * der Datenbank fuer das uebergebene Fachkeurzel. Ermittelt nur Gruppen, die noch 
	 * Teamkapazitaet frei haben oder die Teams enthalten, die noch nicht die maximale
	 * Anzahl an Mitgliedern erreicht haben und fuer die der Student noch keine erfolgreiche PVL-Leistung erhalten 
	 * und für die er noch nicht mit einem anderen Team angemeldet ist.
	 * 
	 * @return : Liste von Gruppen fuer die Einzelanmeldung
	 */
	@RequestMapping(value="/gruppenEinzelPraktikum/{id}", method=RequestMethod.POST)
	public static @ResponseBody List<Gruppe> getFreieGruppenEinzelPraktikum(@RequestBody String fachkuerzel,
																			@PathVariable(value = "id") String id){
		return servicePool.getStudentVerwSrv().getGruppenEinzel(fachkuerzel, "Praktikum", UserID.getID(Integer.parseInt(id)));
	}
	
	
	/**
	 * Ermittelt alle Gruppen fuer ein Wahlpflichtpraktikum innerhalb der Teamanmeldefrist 
	 * fuer Wahlpflichtpraktika aus der Datenbank fuer das uebergebene Fachkuerzel. Ermittelt 
	 * nur Gruppen, die noch Teamkapazitaet frei haben und fuer die der Student noch keine 
	 * erfolgreiche PVL-Leistung erhalten und für die er noch nicht mit einem anderen Team 
	 * angemeldet ist.
	 * 
	 * @return : Liste von Gruppen fuer die Teamanmeldung
	 */
	@RequestMapping(value="/gruppenTeamWP/{id}", method=RequestMethod.POST)
	public static @ResponseBody List<Gruppe> getFreieGruppenTeamWP(@RequestBody String fachkuerzel,
																   @PathVariable(value = "id") String id){
		return servicePool.getStudentVerwSrv().getGruppenTeam(fachkuerzel, "WPP", MatrikelNr.getMatrikelNr(Integer.parseInt(id)));
	}
	
	/**
	 * Ermittelt alle Gruppen fuer ein Wahlpflichtpraktikum innerhalb der Einzelanmeldefrist fuer 
	 * Wahlpflichtpraktika aus der Datenbank fuer das uebergebene Fachkeurzel. Ermittelt nur Gruppen, 
	 * die noch Teamkapazitaet frei haben oder die Teams enthalten, die noch nicht die maximale
	 * Anzahl an Mitgliedern erreicht haben und fuer die der Student noch keine erfolgreiche 
	 * PVL-Leistung erhalten und für die er noch nicht mit einem anderen Team angemeldet ist.
	 * 
	 * @return : Liste von Gruppen fuer die Einzelanmeldung
	 */
	@RequestMapping(value="/gruppenEinzelWP/{id}", method=RequestMethod.POST)
	public static @ResponseBody List<Gruppe> getFreieGruppenEinzelWP(@RequestBody String fachkuerzel,
																	 @PathVariable(value = "id") String id){
		return servicePool.getStudentVerwSrv().getGruppenEinzel(fachkuerzel, "WPP", UserID.getID(Integer.parseInt(id)));
	}
	
	/**
	 * Ermittelt alle Gruppen fuer ein Projekt innerhalb der Teamanmeldefrist fuer Projekte aus 
	 * der Datenbank fuer das uebergebene Fachkuerzel. Ermittelt nur Gruppen, die noch 
	 * Teamkapazitaet frei haben und fuer die
	 * der Student noch keine erfolgreiche PVL-Leistung erhalten und für die er noch nicht 
	 * mit einem anderen Team angemeldet ist.
	 * 
	 * @return : Liste von Gruppen fuer die Teamanmeldung
	 */
	@RequestMapping(value="/gruppenTeamPO/{id}", method=RequestMethod.POST)
	public static @ResponseBody List<Gruppe> getFreieGruppenTeamPO(@RequestBody String fachkuerzel,
																   @PathVariable(value = "id") String id){
		return servicePool.getStudentVerwSrv().getGruppenTeam(fachkuerzel, "PO", MatrikelNr.getMatrikelNr(Integer.parseInt(id)));
	}
	
	/**
	 * Ermittelt alle Gruppen fuer ein Projekte innerhalb der Einzelanmeldefrist fuer 
	 * Projekte aus der Datenbank fuer das uebergebene Fachkeurzel. Ermittelt nur Gruppen, 
	 * die noch Teamkapazitaet frei haben oder die Teams enthalten, die noch nicht die maximale
	 * Anzahl an Mitgliedern erreicht haben und fuer die der Student noch keine erfolgreiche 
	 * PVL-Leistung erhalten und für die er noch nicht mit einem anderen Team angemeldet ist.
	 * 
	 * @return : Liste von Gruppen fuer die Einzelanmeldung
	 */
	@RequestMapping(value="/gruppenEinzelPO/{id}", method=RequestMethod.POST)
	public static @ResponseBody List<Gruppe> getFreieGruppenEinzelPO(@RequestBody String fachkuerzel,
																	 @PathVariable(value = "id") String id){
		return servicePool.getStudentVerwSrv().getGruppenEinzel(fachkuerzel, "PO", UserID.getID(Integer.parseInt(id)));
	}
	
	
	/**
	 * Holt alle Studenten, die als moegliche Teammitglieder in Frage kommen fuer das uebergebene
	 * Fachkuerzel. Dies sind nur Studenten, die in noch keinem Team angemeldet sind und noch keine
	 * erfolgreiche PVL-Leistung erhalten haben.
	 * 
	 * @param fachkuerzel : Das Fachkuerzel fuer potentielles Team
	 */
	@RequestMapping(value="/studenten/{id}", method=RequestMethod.POST)
	public static @ResponseBody List<Student> getAlleStudenten(@RequestBody String fachkuerzel,
															   @PathVariable(value = "id") String id){
		return servicePool.getStudentVerwSrv().getAlleStudenten(fachkuerzel, UserID.getID(Integer.parseInt(id)));
	}
	
	/**
	 * Sendet eine Teameinladung an jedes Mitglied (sofern noch nicht geschehen) 
	 * des Teams außer dem Sender, die Einladungen werden in der Datenbank gespeichert. 
	 * Nachdem die Einladungen gespeichert wurden, wird das Team fuer die gewaehlte 
	 * Gruppe vorgemerkt.
	 * 
	 * @param team : Das Team, welches die Einladung erhaelt
	 * @return boolean : einladung gespeichert / speichern fehlgeschlagen
	 */
	@RequestMapping(value="/einladung/{id}", method=RequestMethod.POST)
	public static synchronized @ResponseBody boolean sendeTeamEinladung(@RequestBody String team,
																	    @PathVariable(value = "id") String id){
				
		Team t = Mapper.mapTeam(team);
		return servicePool.getTeamVerwSrv().sendeTeamEinladung(t, UserID.getID(Integer.parseInt(id)));
	}
	
	/**
	 * Holt alle Meldungen fuer den aktuell eingeloggten Studenten aus der Datenbank
	 * 
	 * @return Liste der aktuellen Meldungen fuer den Studenten
	 */
	@RequestMapping(value="/meldungen/{id}", method=RequestMethod.GET)
	public static @ResponseBody List<Meldung> holeAlleMeldungen(@PathVariable(value = "id") String id){
		
		return servicePool.getTeamVerwSrv().getAlleMeldungen(UserID.getID(Integer.parseInt(id)));
	}
	
	
	/**
	 * Nimmt eine Teameinladung an und bestaetigt das Mitglied in dem Team.
	 * Die Teameinladung wird anschließend geloescht. Loest diese Annahme der
	 * Einladung die letzte noch fehlende Bestaetigung aus um das Team vollstaendig
	 * in der Gruppe anzumelden, wird der Teamplatz der Gruppe an dieses Team ver-
	 * geben und das Team ist vollstaendig in der Gruppe angemeldet.
	 * Alle anderen noch vorgemerkten Teams werden geloescht und deren Mitglieder
	 * erhalten eine Gruppevollmeldung.
	 * 
	 * @param teameinladung : Die angenommene Teameinladung als JSON-String
	 * @return boolean : Einladung annehmen erfolgreich/nicht erfolgreich
	 */
	@RequestMapping(value="/einlAnnehmen", method=RequestMethod.POST)
	public synchronized static @ResponseBody boolean nehmeTeamEinladungAn(@RequestBody String teameinladung){
		
		Teameinladung e = (Teameinladung) Mapper.mapTeameinladung(teameinladung);
		return servicePool.getTeamVerwSrv().nehmeTeamEinladungAn(e);
	}
	
	/**
	 * Lehnt eine Teameinladung ab. Das ablehende Mitgliet(aktuell eingeloggter Student)
	 * wird aus dem Team geloescht sowie die Einladung. Der Absender der Einladung 
	 * erhaelt dann eine Ablehnungsmeldung, um ein neues Mitglied suchen zu koennen.
	 * 
	 * @param Teameinladung : Die Teameinladung die abgelehnt wurde
	 * @return boolean : erfolgreich/nicht erfolgreich
	 */
	@RequestMapping(value="/einlAblehnen", method=RequestMethod.POST)
	public synchronized static @ResponseBody boolean lehneTeamEinladungAb(@RequestBody String teameinladung){
		
		Teameinladung te = (Teameinladung) Mapper.mapTeameinladung(teameinladung);
		
		return servicePool.getTeamVerwSrv().lehneTeamEinladungAb(te);
	}

	/**
	 * Zieht eine noch nicht bestaetigte Einladung an ein potentielles Teammitglied
	 * zurueck. Die Einladung sowie das Mitglied wird aus dem Team geloescht. Hat 
	 * das Team keine Mitglieder mehr, wird es ebenfalls geloescht
	 * 
	 * @param args : args[0] = Team-ID, args[1] = Matrikelnummer des entfernten Mitglieds
	 * @return boolean : erfolgreich/nicht erfolgreich
	 */
	@RequestMapping(value="/einlZurueck", method=RequestMethod.POST)
	public synchronized static @ResponseBody boolean zieheEinladungZurueck(@RequestBody String args){
		
		String arg [] = args.split(":");
		TeamID id = TeamID.getTeamID(arg[0]);
		MatrikelNr matrNr = MatrikelNr.getMatrikelNr(Integer.parseInt(arg[1]));
		
		return servicePool.getTeamVerwSrv().zieheEinladungZurueck(id, matrNr);
	}
	
	
	/**
	 * Das uebergebene Team wird verlassen (vom aktuell eingeloggten Studenten)
	 * Das mitglied wird aus dem Team entfernt und alle anderen bereits 
	 * bestaetigten Teammitglieder erhalten eine Meldung, dass das Team ver-
	 * lassen wurde.
	 * 
	 * @param teamString : Das verlassene Team als JSON-String
	 * @return boolean : erfolgreich/nicht erfolgreich
	 */
	@RequestMapping(value="/verlasseTeam/{id}", method=RequestMethod.POST)
	public synchronized static @ResponseBody boolean teamVerlassen(@RequestBody String teamString,
																   @PathVariable(value = "id") String id){
		

		Team team = Mapper.mapTeam(teamString);

		return servicePool.getTeamVerwSrv().verlasseTeam(team, UserID.getID(Integer.parseInt(id)));
		
	}
	
	/**
	 * Speichert eine Einzelanmeldung, wenn die Teamanmeldephase abgeschlossen ist
	 * aber die Einzelanmeldephase noch laeuft. Hierfuer wird zuerst versucht ein 
	 * Team in der Gruppe zu finden, welches noch nicht die maximale Anzahl an
	 * Mitgliedern erreicht wurde. Gibt es ein solches Team nicht und es ist noch
	 * mindestens ein Teamslot in der Gruppe unbenutzt, wird automatisch ein neues
	 * Team erstellt. Dieses Team wird dann fuer weitere Einzelanmelder dieser Gruppe
	 * automatisch bevorzugt, solange noch Mitgliederplaetze vorhanden sind.
	 * 
	 * @param gruppeString : Die Gruppe der Anmeldung als JSoN-String
	 * @return boolean : erfolgreich/nicht erfolgreich
	 */
	@RequestMapping(value="/einzelanmeldung/{id}", method=RequestMethod.POST)
	public synchronized static @ResponseBody boolean speichereEinzelAnmeldung(@RequestBody String gruppeString,
																			  @PathVariable(value = "id") String id){
		
		Gruppe gruppe = Mapper.mapGruppe(gruppeString);
		return servicePool.getStudentVerwSrv().speichereEinzelAnmeldung(gruppe, UserID.getID(Integer.parseInt(id)));		
	}
	
	/**
	 * Holt alle Teams des aktuell eingeloggten Studenten, bestaetigte und 
	 * undbestaetigte und listet diese in "Meine Teams" auf
	 * @return List<Team> : Die Teams des eingeloggten Studenten
	 */
	@RequestMapping(value="/getAlleTeams/{id}", method=RequestMethod.GET)
	public static @ResponseBody List<Team> getAlleTeamsAktuellerStudent(@PathVariable(value = "id") String id){	
		
		List<Team> teams = servicePool.getStudentVerwSrv().getAlleTeamsAktuellerStudent(UserID.getID(Integer.parseInt(id)));
		return teams;
	}
	
	
	/**
	 * Holt eine die Gruppe in der das uebergebene Team angemeldet oder
	 * vorgemerkt ist aus der Datenbank.
	 * @param team : Das Team als JSON-String
	 * @return Die Gruppe in der das Team angemeldet oder vorgemerkt ist
	 */
	@RequestMapping(value="/gruppe", method=RequestMethod.POST)
	public static @ResponseBody Gruppe getGruppe(@RequestBody String team){	
		
		Team t = Mapper.mapTeam(team);
		Gruppe gruppe = servicePool.getStudentVerwSrv().getGruppe(t);
		return gruppe;
	}
	
	
	

	/**
	 * Setzt einen Servicepool fÃŒr diese Schnittstelle
	 * @param pool
	 */
	public static void setServicePool(ServicePool pool) {

		servicePool = pool;
		
	}

	public static Connection getConnection() {
		return connection;
	}

	public static void setConnection(Connection connection) {
		VeranstaltungsCtrlStudent.connection = connection;
	}
}