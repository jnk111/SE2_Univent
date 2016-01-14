package se2.praktikum.projekt.services.leistungenservice;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import se2.praktikum.projekt.models.leistungen.Leistung;
import se2.praktikum.projekt.models.person.fachwerte.MatrikelNr;
import se2.praktikum.projekt.services.loginservice.ServicePool;

@Controller
public class LeistungenCtrlStudent {
	
	
	private static ServicePool servicePool;
	/**
	 * Holt alle bewerteten Leistungen des aktuell eingeloggten Studenten aus der
	 * Datenbank.
	 * 
	 * @return Liste von vollstaendig bewerteten Leistungen (Note und PVL vorhanden)
	 */
	@RequestMapping(value="/leistungenStudent/{matrNr}", method=RequestMethod.GET)
	public static  @ResponseBody List<Leistung> leistungenStudent(@PathVariable(value = "matrNr") String matrNr){
		
		LeistungenAnzSrv srv = servicePool.getlAnzSrv();
		
		List<Leistung> leistungen = srv.getLeistungenStudent(MatrikelNr.getMatrikelNr(Integer.parseInt(matrNr)));
		
		return leistungen;
	}
	
	public static void setServicePool(ServicePool pool){
		
		servicePool = pool;
	}
	
	public static ServicePool getServicePool(){
		return servicePool;
	}

}
