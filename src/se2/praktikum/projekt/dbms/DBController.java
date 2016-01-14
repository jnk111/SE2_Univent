package se2.praktikum.projekt.dbms;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import se2.praktikum.projekt.models.person.Student;

/**
 * Datenbank Controller für das Beispiel
 * @author jan
 *
 */
@Controller
public class DBController 
{
	DBService service = new DBService();
	
	@RequestMapping(value="/dbbeispiel", method = RequestMethod.GET)
	protected ModelAndView openDBExample()
	{
		ModelAndView mv = new ModelAndView("dbbeispiel");
		return mv;
	}
	
	@RequestMapping(value="/students", method = RequestMethod.GET)
	protected @ResponseBody List<Student> fetchStudents()
	{
		service.connect();
		List<Student> studenten = service.getAllStudents();
		return studenten;
	}
}