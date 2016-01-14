package se2.praktikum.projekt.services.loginservice;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import se2.praktikum.projekt.models.person.Angestellter;
import se2.praktikum.projekt.models.person.IAngestellter;
import se2.praktikum.projekt.models.person.IPerson;
import se2.praktikum.projekt.models.person.Student;
import se2.praktikum.projekt.models.person.fachwerte.MAID;
import se2.praktikum.projekt.models.person.fachwerte.MatrikelNr;

/**
 * Wird in Zukunft den Login-Vorgang abwickeln.
 * Loginpage ist immer die Startseite.
 * @author jan
 *
 */
@Controller
public class LoginController {	
	
	
	private static IPerson lastLoggedUser;
	private static ServicePool servicePool = new ServicePool();
	private static LoginSrv srv = new LoginSrv(servicePool);
	
	/**
	 * Laedt die Loginseite
	 * @return	ModelAndView-Objekt der Login-Seite
	 */
	@RequestMapping(value="/", method = RequestMethod.GET)
	protected ModelAndView loadLoginView(){
		
		ModelAndView mv = new ModelAndView("login");
		return mv;

	}
	
	/**
	 * Lädt das Angestelltenpanel, wenn sich ein Mitarbeiter einloggt
	 * @return	ModelAndView-Objekt des Angestellten-Panels
	 */
	@RequestMapping(value="/angestellter", method = RequestMethod.GET)
	public ModelAndView ladeAngestelltenPanel(){
		
		ModelAndView mv = srv.ladeAngestPanel();
		System.out.println("Called");
		return mv;
	}
	
	/**
	 * Holt alle Profilinformationen des eingeloggten Users aus 
	 * der Datenbank.
	 * 
	 * @return Personenobjekt mit allen Profilinformationen
	 */
	@RequestMapping(value="/profil", method = RequestMethod.GET)
	public static @ResponseBody IPerson holeProfilInformationen(){
		
		if(lastLoggedUser instanceof Student){
			return srv.getProfilInformationen(((Student) lastLoggedUser).getMatrNr());
		}else{
			return srv.getProfilInformationen(((Angestellter) lastLoggedUser).getMaID());
			
		}
		
	}
	
	
	/**
	 * Lädt das Studentenpanel, wenn sich ein Mitarbeiter einloggt
	 * @return	ModelAndView-Objekt des Studenten-Panels
	 */
	@RequestMapping(value="/student", method = RequestMethod.GET)
	public ModelAndView ladeStudentenPanel(){
		
		ModelAndView mv = srv.ladeStudPanel();
		System.out.println("Called");
		return mv;
	}
	
	
	/**
	 * Loggt einen User ein, wenn Benutzername und Passwort korrekt.
	 * 
	 * @param	args : args[0] = username, args[1] = passwort
	 * @return	String panel : Das zu ladene Panel, wird im Frontend
	 *                         weiterverarbeitet
	 */
	@RequestMapping(value="/login", method = RequestMethod.POST, produces="text/plain")
	public static synchronized @ResponseBody String einloggen(@RequestBody List<String> args){
		
		String panel = null; // Das zu ladene Panel
		String username = args.get(0);	// Username
		String password = args.get(1);  // Passwort
		//String akennung = args.get(2);
		//String passwortdb = args.get(3);
		
		IPerson user = srv.login(username, password); // Benutzer einloggen
		
		// Speichere Panel informationen anhand des eingeloggten Benutzertyp -> Wird im Frontend ausgewertet
		if(user != null && user instanceof Student){
			panel = "student";
		}else if(user != null && user instanceof IAngestellter){
			
			panel = "angestellter";
		}
		
		lastLoggedUser = user;
		return panel;
	}


	/**
	 * Meldet den aktuell eingeloggten Benutzer vom System an
	 * und laedt die Loginseite
	 * return ModelAndView der Loginseite
	 */
	@RequestMapping(value="/logout/{id}", method = RequestMethod.GET)
	public static synchronized ModelAndView ausloggen(@PathVariable(value = "id") String id)
	{
		System.out.println("Logged Out");
		ModelAndView mv = null;
		if(id.length() < 7){
			mv = srv.logout(MAID.getMAID(Integer.parseInt(id)));
		}else{
			mv = srv.logout(MatrikelNr.getMatrikelNr(Integer.parseInt(id)));
		}
		
		return mv;
	}
	
	public static LoginSrv getloginSrv(){
		return srv;
	}

	
}