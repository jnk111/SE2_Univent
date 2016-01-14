package se2.praktikum.projekt.services.veranstaltungsservice;

import java.util.Date;
import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import se2.praktikum.projekt.datenimexport.backuptables.DBAnmeldetermin;
import se2.praktikum.projekt.models.gruppe.Gruppe;
import se2.praktikum.projekt.models.gruppe.fachwerte.Uhrzeit;
import se2.praktikum.projekt.models.person.IAngestellter;
import se2.praktikum.projekt.models.person.fachwerte.MatrikelNr;
import se2.praktikum.projekt.models.team.Team;
import se2.praktikum.projekt.models.team.fachwerte.TeamID;
import se2.praktikum.projekt.models.veranstaltung.Veranstaltung;
import se2.praktikum.projekt.services.loginservice.ServicePool;
import se2.praktikum.projekt.tools.Mapper;

@Controller
public class VeranstaltungsCtrlAdmin {

	private static ServicePool servicePool = new ServicePool();
	
	/**
	 * holt alle Pflichtpraktika für eine gegebenes Semester und Fachbereich.
	 * 
	 * @param args : args[0] = semester, args[1] = fachbereich
	 * @return	Liste Veranstaltungen für die übergebenen Parameter
	 */
	@RequestMapping(value="/pflichtpraktika", method=RequestMethod.POST)
	public static @ResponseBody List<Veranstaltung> praktikaAnzeigen(@RequestBody List<String> args){
		
		AnzeigerSrv srv = servicePool.getVerAnzSrv();
		int semester = Integer.parseInt(args.get(0));
		return srv.getAllPraktika(semester, args.get(1));
	
	}
	
	/**
	 * Holt alle Veranstaltungen fuer die uebergebenen Fachkuerzel.
	 * 
	 * @param fachkuerzel : Liste der Fachkuerzel
	 * @return List<String> : Liste von Veranstaltungen fuer die uebergebenen Fachkuerzel inkl.
	 * 		   				  aller Informationen
	 */
	@RequestMapping(value="/getVeranstaltungen", method=RequestMethod.POST)
	public static @ResponseBody List<Veranstaltung> holeVeranstaltungen(@RequestBody List<String> fachkuerzel){
		
		AnzeigerSrv srv = servicePool.getVerAnzSrv();
		List<Veranstaltung> vas = srv.holeVeranstaltungen(fachkuerzel);
		return vas;
	
	}
	
	/**
	 * Holt alle Professoren für eine gegebenes Semester und einen Fachbereich
	 * zustaendig sind aus der Datenbank. 
	 * 
	 * @param args : args[0] = semester, args[1] = fachbereich
	 * @return	List<IAngestellter> : Liste von Professoren für die übergebenen
	 *								  Parameter
	 */
	@RequestMapping(value="/profs", method=RequestMethod.POST)
	public static @ResponseBody List<IAngestellter> getProfessoren(@RequestBody List<String> args){
		
		AnzeigerSrv srv = servicePool.getVerAnzSrv();
		int semester = Integer.parseInt(args.get(0));
		return srv.getAlleProfessoren(semester, args.get(1));
		
		
	}
	
	
	/**
	 * Holt alle Assistenten aus der Datenbank. 
	 * 
	 * @param args : args[0] = semester, args[1] = fachbereich
	 * @return	List<IAngestellter> : Liste aller Assistenten
	 */
	@RequestMapping(value="/assistenten", method=RequestMethod.GET)
	public static @ResponseBody List<IAngestellter> getAssistenten(){
		
		AnzeigerSrv srv = servicePool.getVerAnzSrv();
		List<IAngestellter> assist = srv.getAlleAssistenten();
		return assist;
		
		
	}
	
	
	/**
	 * Holt alle WP für eine gegebenes Semester und Fachbereich
	 * 
	 * @param args : args[0] = semester, args[1] = fachbereich
	 * @return	Liste Veranstaltungen für die übergebenen Parameter
	 */
	@RequestMapping(value="/wp", method=RequestMethod.POST)
	public static @ResponseBody List<Veranstaltung> wpAnzeigen(@RequestBody List<String> args){
		
		AnzeigerSrv srv = servicePool.getVerAnzSrv();
		int semester = Integer.parseInt(args.get(0));
		return srv.getAllWP(semester, args.get(1));
		
	}
	
	
	/**
	 * Holt alle PO für ein gegebenes Semester und Fachbereich
	 * 
	 * @param args : args[0] = semester, args[1] = fachbereich
	 * @return	Liste Veranstaltungen für die übergebenen Parameter
	 */
	@RequestMapping(value="/po", method=RequestMethod.POST)
	public static @ResponseBody List<Veranstaltung> poAnzeigen(@RequestBody List<String> args){
		
		AnzeigerSrv srv = servicePool.getVerAnzSrv();
		int semester = Integer.parseInt(args.get(0));
		return srv.getAllPO(semester, args.get(1));
		
	}
	
	/**
	 * Speichert eine bearbeitete Veranstaltung in der Datenbank.
	 * 
	 * @param veranstaltung : Die editierte Veranstaltung als JSON-String
	 * @return	boolean : speichern erfolgreich/nicht erfolgreich
	 */
	@RequestMapping(value="/vbearbeiten", method=RequestMethod.POST)
	public synchronized static @ResponseBody boolean veranstaltungBearbeiten(@RequestBody String veranstaltung){
		
		VeranstaltungsVerwalterSrv srv = servicePool.getVeranVerwSrv();
		System.out.println(veranstaltung);
		return srv.bearbeiteVrst(Mapper.mapPaktikum(veranstaltung));
	}
	
	/**
	 * Speichert eine erstellte Veranstaltung in der Datenbank.
	 * 
	 * @param veranstaltung : Die neue Veranstaltung als JSON-String
	 * @return boolean : Speichern erfolgreich/nicht erfolgreich
	 */
	@RequestMapping(value="/vErstellen", method=RequestMethod.POST)
	public static synchronized @ResponseBody boolean veranstaltungErstellen(@RequestBody String va){
		
		
		VeranstaltungsVerwalterSrv srv = servicePool.getVeranVerwSrv();
		
		Veranstaltung veran = null;
		if(va.contains("WPP")){
			veran = Mapper.mapWP(va);
		}else if(va.contains("PO")){
			veran = Mapper.mapPO(va);
		}else{
			veran = Mapper.mapPaktikum(va);
		}
		return srv.erstelleVrst(veran);
	}
	
	/**
	 * Löscht eine uebergebene Veranstaltung aus der Datenbank
	 * 
	 * @param veranstaltung : Die zu loeschende Vernanstaltung als JSON-String
	 * @return boolean : loeschen erfolgreich/nicht erfolgreich
	 */
	@RequestMapping(value="/vLoeschen", method=RequestMethod.POST)
	public static synchronized @ResponseBody boolean veranstaltungLoeschen(@RequestBody String veranstaltung){
		Veranstaltung va = Mapper.mapPaktikum(veranstaltung);
		VeranstaltungsVerwalterSrv srv = servicePool.getVeranVerwSrv();
		return srv.loescheVrst(va);
	}
	
	/**
	 * Holt alle Gruppen zu einer einer Veranstaltung aus der Datenbank
	 * 
	 * @param veranstaltung : Die Veranstaltung als JSON-String
	 * @return Liste der Gruppen zu einer Veranstaltung
	 */
	@RequestMapping(value="/gruppUebersicht", method=RequestMethod.POST)
	public static @ResponseBody List<Gruppe> gruppenUebersichtAnzeigenPraktika(@RequestBody String veranstaltung){
		
		Veranstaltung va = Mapper.mapPaktikum(veranstaltung);
		AnzeigerSrv srv = servicePool.getVerAnzSrv();
		List<Gruppe> gruppen = srv.getAllGruppen(va);
		
		return gruppen;
	}
	
	
	/**
	 * Speichert eine neu erstellte Gruppe in der Datenbank
	 * @param gruppe : Die erstellte gruppe als JSON-String
	 * @return boolean : Speichern erfolgreich/nicht erfolgreich
	 */
	@RequestMapping(value="/gruppErstellen", method=RequestMethod.POST)
	public static synchronized @ResponseBody boolean gruppeErstellen(@RequestBody String gruppe){
		
		Gruppe gr = Mapper.mapGruppe(gruppe);
		GruppVerwSrv srv = servicePool.getGruppVerwSrv();
		boolean erfolg = srv.erstelleGruppe(gr);
		return erfolg;
		

	}
	
	/**
	 * Speichert eine bearbeitete Gruppe in der Datenbank
	 * @param gruppe : Die editierte Gruppe als JSON-String
	 * @return	boolean : speichern erfolgreich/nicht erfolgreich
	 */
	@RequestMapping(value="/gruppBearbeiten", method=RequestMethod.POST)
	public static synchronized @ResponseBody boolean gruppeBearbeiten(@RequestBody String gruppe){
		
		System.out.println(gruppe);
		Gruppe gr = Mapper.mapGruppe(gruppe);
		GruppVerwSrv srv = servicePool.getGruppVerwSrv();
		boolean erfolg = srv.bearbeiteGruppe(gr);
		
		return erfolg;
	}
	
	/**
	 * Löscht eine Gruppe aus der Datenbank
	 * 
	 * @param gruppe : die zu löschende Gruppe als JSON-String
	 * @return boolean : loeschen erfolgreich/nicht erfolgreich
	 */
	@RequestMapping(value="/gruppLoeschen", method=RequestMethod.POST)
	public static synchronized @ResponseBody boolean gruppeLoeschen(@RequestBody String gruppe){
		Gruppe gr = Mapper.mapGruppe(gruppe);
		GruppVerwSrv srv = servicePool.getGruppVerwSrv();
		
		boolean erfolg = srv.loescheGruppe(gr);
		
		return erfolg;
	}
	
	/**
	 * Ermittelt alle Teams einer Gruppe aus der Datenbank
	 * 
	 * @param gruppe : Die gruppe der Teilnehmer als JSON-String.
	 * @return Liste von Teams zu dieser Gruppe
	 */
	@RequestMapping(value="/tmUebersicht", method=RequestMethod.POST)
	public static @ResponseBody List<Team> teilnehmerUebersichtAnzeigen(@RequestBody String gruppe){
		
		AnzeigerSrv srv = servicePool.getVerAnzSrv();
		Gruppe gr = Mapper.mapGruppe(gruppe);
		
		List<Team> teams = srv.getAllTeilnehmer(gr);
		
		
		return teams;
	}
	
	/**
	 * Entfernt einen Teilnehmer aus einer Gruppe und Team
	 * 
	 * @param args : args[0] = TeamID, args[1] = MatrikelNummer als JSON-String
	 * @return boolean : loeschen erfolgreich/nicht erfolgreich.
	 */
	@RequestMapping(value="/tmEntfernen", method=RequestMethod.POST)
	public static synchronized @ResponseBody boolean tmEntfernen(@RequestBody List<String> args){

		MatrikelNr matrNr = MatrikelNr.getMatrikelNr(Integer.parseInt(args.get(1)));
		TeamID teamId = TeamID.getTeamID(args.get(0));	
		TeamVerwSrv srv = servicePool.getTeamVerwSrv();
		boolean erfolgreich = srv.entferneTeilnehmer(teamId, matrNr);
		return erfolgreich;
	}
	
	
	/**
	 * Speichert die Anmeldefristen zu den uebergebenen Parametern in der Datenbank
	 * @param args : args[0] = Veranstaltungstyp, args[1] = Anmeldetyp, args[2] = Anmeldestartdatum
	 *               args[3] = Anmeldeendedatum, args[4] = Anmeldestartuhrzeit
	 *               args[5] = Anmeldeenduhrzeit
	 *               
	 * @return boolean : Speichern erfolgreich/nicht erfolgreich
	 */
	@RequestMapping(value="/speichereAnmeldefristen", method=RequestMethod.POST)
	public synchronized static @ResponseBody boolean speichereAnmeldefrist(@RequestBody List<String> args){
		
		System.out.println(args);
		String vTyp = args.get(0);
		String aTyp = args.get(1);
		Date dStart = new Date(Long.parseLong(args.get(2)));
		Date dEnde = new Date(Long.parseLong(args.get(3)));
		Uhrzeit uStart = Uhrzeit.getUhrzeit(args.get(4));
		Uhrzeit uEnde = Uhrzeit.getUhrzeit(args.get(5));
		
		return servicePool.getVeranVerwSrv().speichereAnmeldeFristen(vTyp, aTyp, dStart, dEnde, uStart, uEnde);
	}
	
	
	/**
	 * Holt die gespeicherten Anmeldefristen aus der Datenbank 
	 * (alle Anmeldetypen, Veranstaltungstypen und Uhrzeiten)
	 * 
	 * @return Liste von Anmeldefristen
	 */
	@RequestMapping(value="/holeAnmeldefristen", method=RequestMethod.GET)
	public static @ResponseBody List<DBAnmeldetermin> holeAnmeldeFristen(){
		
		return servicePool.getVeranVerwSrv().getAnmeldeFristen();
	}
	
	
	
	
	
	
	/**
	 * Setzt einen Servicepool für diese Schnittstelle
	 * @param pool
	 */
	public static void setServicePool(ServicePool pool) 
	{
		
		servicePool = pool;
		
	}
	
	public static ServicePool getServicePool() {
		
		return servicePool;
		
	}
	
	

}
