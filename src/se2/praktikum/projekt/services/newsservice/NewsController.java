package se2.praktikum.projekt.services.newsservice;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import se2.praktikum.projekt.services.loginservice.ServicePool;

@Controller
public class NewsController {
	
	private static ServicePool servicePool;
	
	/**
	 * Speichert eine Newsueberschrift in einer Textdatei
	 * @param header : Die Ueberschrift der News
	 * @return boolean : erfolgreich/nicht erfolgreich
	 */
	@RequestMapping(value="/newsHeader", method=RequestMethod.POST)
	public static synchronized @ResponseBody boolean speichereHeader(@RequestBody String header){
		
		//System.out.println(header);
		return servicePool.getNewsSrv().speichere(header, NewsElems.HEADER);
	}
	
	
	/**
	 * Speichert eine Newsbody in einer Textdatei
	 * @param header : Der Body der News
	 * @return boolean : erfolgreich/nicht erfolgreich
	 */
	@RequestMapping(value="/newsBody", method=RequestMethod.POST)
	public static synchronized @ResponseBody boolean speichereBody(@RequestBody String body){
		
		//System.out.println(body);
		return servicePool.getNewsSrv().speichere(body, NewsElems.BODY);
	}
	
	/**
	 * Holt die aktuellen News aus einer Textdatei und gibt Ueberschrift und Body
	 * separat als Liste zurueck
	 * 
	 * @return Liste mit Newselementen (Header, Body)
	 */
	@RequestMapping(value="/holeNews", method=RequestMethod.GET)
	public static synchronized @ResponseBody List<String> holeNews(){
		
		
		return servicePool.getNewsSrv().getNews();
	}


	public static ServicePool getServicePool() {
		return servicePool;
	}


	public static void setServicePool(ServicePool servicePool) {
		NewsController.servicePool = servicePool;
	}

}
