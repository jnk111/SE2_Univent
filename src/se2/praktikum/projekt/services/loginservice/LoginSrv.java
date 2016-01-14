package se2.praktikum.projekt.services.loginservice;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;
import org.springframework.web.servlet.ModelAndView;

import se2.praktikum.projekt.datenimexport.DatenExportService;
import se2.praktikum.projekt.datenimexport.DatenImExportController;
import se2.praktikum.projekt.datenimexport.DatenImportService;
import se2.praktikum.projekt.dbms.DBConnector;
import se2.praktikum.projekt.models.person.IAngestellter;
import se2.praktikum.projekt.models.person.EMail;
import se2.praktikum.projekt.models.person.IPerson;
import se2.praktikum.projekt.models.person.Professor;
import se2.praktikum.projekt.models.person.Student;
import se2.praktikum.projekt.models.person.VerwaltMitarbeiter;
import se2.praktikum.projekt.models.person.fachwerte.Adresse;
import se2.praktikum.projekt.models.person.fachwerte.MAID;
import se2.praktikum.projekt.models.person.fachwerte.MatrikelNr;
import se2.praktikum.projekt.models.person.fachwerte.PLZ;
import se2.praktikum.projekt.models.person.fachwerte.UserID;
import se2.praktikum.projekt.services.leistungenservice.LeistungenAnzSrv;
import se2.praktikum.projekt.services.leistungenservice.LeistungenCtrlAdmin;
import se2.praktikum.projekt.services.leistungenservice.LeistungenCtrlStudent;
import se2.praktikum.projekt.services.leistungenservice.LeistungenEintrSrv;
import se2.praktikum.projekt.services.newsservice.NewsController;
import se2.praktikum.projekt.services.newsservice.NewsSrv;
import se2.praktikum.projekt.services.veranstaltungsservice.AnzeigerSrv;
import se2.praktikum.projekt.services.veranstaltungsservice.GruppVerwSrv;
import se2.praktikum.projekt.services.veranstaltungsservice.StudentVerwSrv;
import se2.praktikum.projekt.services.veranstaltungsservice.TeamVerwSrv;
import se2.praktikum.projekt.services.veranstaltungsservice.VeranstaltungsCtrlAdmin;
import se2.praktikum.projekt.services.veranstaltungsservice.VeranstaltungsCtrlStudent;
import se2.praktikum.projekt.services.veranstaltungsservice.VeranstaltungsVerwalterSrv;
import se2.praktikum.projekt.tools.ErrorLogger;

public class LoginSrv {
	
	
	private IPerson user = null;
	private ServicePool servicePool;
	private Connection connection;
	

	public LoginSrv(ServicePool servicePool) {
		
		this.servicePool = servicePool;
	}


	/**Einen Benutzer anhand des Usernames und Passwortes aus der DB
	 * identifizieren und 
	 * @param username
	 * @param password
	 * @return
	 */
	public IPerson login(String username, String password) {

		connection = DBConnector.connect();
		
		String qry = null;
		if(username.startsWith("a")){
			qry = "select matrnr, vorname, nachname from Student where username = ? and passwort = ?";
		}else if(username.startsWith("o")){
			qry = "select maid, vorname, nachname from Professor where username = ? and passwort = ?";
		}else if(username.startsWith("n")){
			qry = "select maid, vorname, nachname from Verwaltungsmitarbeiter where username = ? and passwort = ?";
		}
		
		try {
			
			if(qry != null){
				
				PreparedStatement stmt = connection.prepareStatement(qry);
				stmt.setString(1, username);
				stmt.setString(2, password);
				
				ResultSet rs = stmt.executeQuery();
				IPerson user = null;
				MatrikelNr matrNr = null;
				MAID maid = null;
				String vorname = null;
				String nachname = null;
				
				while(rs.next()){
					
					vorname = rs.getString("vorname");
					nachname = rs.getString("nachname");
					
					if(username.startsWith("a")){
						matrNr = MatrikelNr.getMatrikelNr(rs.getInt("matrNr"));
						user = new Student(matrNr, vorname, nachname);
					}else if(username.startsWith("o")){
						maid = MAID.getMAID(rs.getInt("maid"));
						user = new Professor(maid, vorname, nachname);
						
					}else if(username.startsWith("n")){
						maid = MAID.getMAID(rs.getInt("maid"));
						user = new VerwaltMitarbeiter(maid, vorname, nachname);
					}
				}
				
				stmt.close();
				
				if(user != null){
					user.setBenutzername(username);
					user.setPasswort(null);
					this.user = user;
					this.servicePool.addUser(user);
					erzeugeServices();
				}
			}
		} catch (SQLException e) {
			ErrorLogger.log(e);
			return null;
		}
		
		return user;
		
	}
	
	
	/**
	 * Den aktuellen Benutzer abmelden
	 * @return 
	 * @return
	 */
	public ModelAndView logout(UserID userID){
		
		this.servicePool.removeUser(userID);
		this.user = null;
		ModelAndView mv = new ModelAndView("login");
		return mv;
	}

	public IPerson getUser() {
		return user;
	}

	public void setUser(IPerson user) {
		this.user = user;
	}
	
	/**
	 * Erzeugt alle nötigen Services bei erfolgreichem Login
	 * @return der angelegte Servicepool.
	 */
	public ServicePool erzeugeServices(){
		
		
		// Erzeuge Services in Servicespool
		servicePool.setlAnzSrv(new LeistungenAnzSrv(servicePool));
		servicePool.setGruppVerwSrv(new GruppVerwSrv(servicePool));
		servicePool.setlEintrSrv(new LeistungenEintrSrv(servicePool));
		servicePool.setTeamVerwSrv(new TeamVerwSrv(servicePool));
		servicePool.setVeranVerwSrv(new VeranstaltungsVerwalterSrv(servicePool));
		servicePool.setVerAnzSrv(new AnzeigerSrv(servicePool));
		servicePool.setStudentVerwSrv(new StudentVerwSrv(servicePool));
		servicePool.setDatenExportSrv(new DatenExportService(servicePool));
		servicePool.setDatenImpSrv(new DatenImportService(servicePool));
		servicePool.setNewsSrv(new NewsSrv(servicePool));
		
		VeranstaltungsCtrlAdmin.setServicePool(servicePool);
		LeistungenCtrlAdmin.setServicePool(servicePool);
		VeranstaltungsCtrlStudent.setServicePool(servicePool);
		DatenImExportController.setServicePool(servicePool);
		NewsController.setServicePool(servicePool);
		LeistungenCtrlStudent.setServicePool(servicePool);
		
		return servicePool;
		
	}



	
	/**
	 * Lädt das Mainpanel (Angestellter)
	 * HINWEIS: Erstmal wird für beide Benutzer die gleiche View geladen,
	 * da Studentpanel noch nicht erstellt
	 * @return View
	 */
	public ModelAndView ladeAngestPanel() {
		
		this.user = new VerwaltMitarbeiter(MAID.getMAID(1), "Maria", "Albricht");
		ModelAndView mv = new ModelAndView("apanel");
			// informiere... (zb "fail"-View laden -> muss erst noch erstellt werden)
		return mv;
	}
	
	
	/**
	 * Lädt das Mainpanel (Angestellter)
	 * HINWEIS: Erstmal wird für beide Benutzer die gleiche View geladen,
	 * da Studentpanel noch nicht erstellt
	 * @return View
	 */
	public ModelAndView ladeStudPanel() {
		
		//this.user = new Student(MatrikelNr.getMatrikelNr(1111126), "Helge", "Schneider");
		//erzeugeServices();
		ModelAndView mv = new ModelAndView("spanel");
			// informiere... (zb "fail"-View laden -> muss erst noch erstellt werden)
		return mv;
	}


	public ServicePool getServicePool() {
		return servicePool;
	}


	public void setServicePool(ServicePool servicePool) {
		this.servicePool = servicePool;
	}


	public IPerson getProfilInformationen(UserID userID) {
		
		String qry = null;
		IPerson user = servicePool.getUser(userID);
		if(user instanceof Student){
			qry = "select * from Student where matrnr = ?";
		}else if(user instanceof Professor){
			qry = "select * from Professor where maid = ?";
		}else if(user instanceof VerwaltMitarbeiter){
			qry = "select * from Verwaltungsmitarbeiter where maid = ?";
		}
		
		IPerson profil = null;
		
		try {
			PreparedStatement stmt = connection.prepareStatement(qry);
			MatrikelNr matrNr = null;
			MAID maid = null;
			
			if(qry.contains("Student")){
				
				Student stud = (Student) this.user;
				matrNr = stud.getMatrNr();
				stmt.setInt(1, matrNr.getMatrNr());
			}else{
				IAngestellter angest = (IAngestellter) this.user;
				maid = angest.getMaID();
				stmt.setInt(1, maid.getId());
			}
			
			ResultSet rs = stmt.executeQuery();
			
			while(rs.next()){
				
				String strasse = rs.getString("strasse");
				int hausNr = rs.getInt("hausNr");
				String stadt = rs.getString("ort");
				Date gebDatum = new Date(rs.getDate("gebDatum").getTime());
				String gebOrt = rs.getString("gebOrt");
				String departm = rs.getString("dkuerzel");
				EMail email = new EMail(rs.getString("email"));
				PLZ plz = PLZ.getPostLeitzahl(rs.getString("plz"));
				Adresse adresse = Adresse.getAdresse(strasse, hausNr, plz, stadt);
				if(qry.contains("Student")){
					String fbkuerzel = rs.getString("fbkuerzel");
					String po = rs.getString("POKuerzel");
					profil = new Student(matrNr, this.user.getVorname(), this.user.getNachname(), this.user.getBenutzername(), null, gebDatum, gebOrt, adresse, departm, fbkuerzel, email);
					((Student) profil).setPo(po);
					
				}else if(qry.contains("Professor")){
					
					profil = new Professor(maid, rs.getString("titel"), this.user.getVorname(), this.user.getNachname(), this.user.getBenutzername(), null, gebDatum, gebOrt, adresse, departm, email);
				}else {
					
					profil = new VerwaltMitarbeiter(maid, this.user.getVorname(), this.user.getNachname(), this.user.getBenutzername(), null, gebDatum, gebOrt, adresse, departm, email);
				}
			}
			
			stmt.close();
		} catch (SQLException e) {
			ErrorLogger.log(e);
			return null;
		}
		return profil;
	}
	
	

}
