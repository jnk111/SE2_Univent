package se2.praktikum.projekt.services.leistungenservice;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import se2.praktikum.projekt.models.leistungen.Leistung;
import se2.praktikum.projekt.services.loginservice.ServicePool;
import se2.praktikum.projekt.tools.Mapper;

@Controller
public class LeistungenCtrlAdmin {

	private static ServicePool servicePool;
	
	
	/**
	 * Holt alle Leistungen fuer die Uebergebenen Parameter
	 * @param string : args[0] = fachkuerzel, args[1] = gruppenummer, 
	 * @return Liste von Leistungen bewertet und unbewertet.
	 */
	@RequestMapping(value="/leistungen", method=RequestMethod.POST)
	public static  @ResponseBody List<Leistung> leistungenAnzeigen(@RequestBody String fachkuerzel){
		
		LeistungenAnzSrv srv = servicePool.getlAnzSrv();;
		
		List<Leistung> leistungen = srv.getLeistungen(fachkuerzel);
		
		return leistungen;
	}
	
	
	
	/**
	 * Trägt die vergebene Note in die Leistung ein und speichert dies in der DB.
	 * Der bewertete Student wird aus dem Team für das bewertete Fach geloescht.
	 * Sind alle Teilnehmer eines Teams bewertet, wird das Team geloescht.
	 * 
	 * @param leistung : Die geänderte Leistung
	 * @return boolean : eintragen erfolgreich/nicht erfolgreich
	 */
	@RequestMapping(value="/noteEintragen", method=RequestMethod.POST)
	public static @ResponseBody boolean noteEintragen(@RequestBody String leistung){
		
		System.out.println(leistung);
		Leistung leist = Mapper.mapLeistung(leistung);
		LeistungenEintrSrv srv = servicePool.getlEintrSrv();
		boolean erfolgreich = srv.trageNoteEin(leist);
		
		return erfolgreich;
	}
	
	
	/**
	 * Trägt die vergebene PVL in die Leistung ein und speichert dies in der DB
	 * @param leistung : Die geänderte Leistung
	 * @return eintragen erfolgreich/nicht erfolgreich
	 */
	@RequestMapping(value="/pvlEintragen", method=RequestMethod.POST)
	public  static @ResponseBody boolean pvlEintragen(@RequestBody String leistung){
		
		System.out.println(leistung);
		Leistung leist = Mapper.mapLeistung(leistung);
		LeistungenEintrSrv srv = servicePool.getlEintrSrv();
		boolean erfolgreich = srv.tragePVLEin(leist);
		
		return erfolgreich;
	}
	
	

	/**
	 * holt alle Fachkuerzel für die übergebenen Parameter.
	 * @param args : args[0] = fachbereich, args[1] = typ, args[2] = semester
	 * 
	 * @return	Liste Fachkuerzel für die übergebenen Parameter
	 */
	@RequestMapping(value="/fachkuerzel", method=RequestMethod.POST)
	public  static @ResponseBody List<String> holeFachkuerzel(@RequestBody List<String> args){
		
		int semester = Integer.parseInt(args.get(2));
		LeistungenAnzSrv srv = servicePool.getlAnzSrv();
		List<String> fachkuerzel = srv.getFachkuerzel(args.get(0), args.get(1), semester);
		
		return fachkuerzel;
		
	}


	/**
	 * Setzt einen Servicepool für diese Schnittstelle
	 * @param pool
	 */
	public static void setServicePool(ServicePool pool) {
		
		servicePool = pool;
		
	}


}
