package se2.praktikum.projekt.services.veranstaltungsservice;


import java.sql.Connection;
import java.util.Date;
import java.util.HashMap;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import se2.praktikum.projekt.dbms.DBConnector;
import se2.praktikum.projekt.models.gruppe.Gruppe;
import se2.praktikum.projekt.models.person.Assistent;
import se2.praktikum.projekt.models.person.Professor;
import se2.praktikum.projekt.models.person.Student;
import se2.praktikum.projekt.models.person.fachwerte.MAID;
import se2.praktikum.projekt.models.person.fachwerte.MatrikelNr;
import se2.praktikum.projekt.models.person.fachwerte.UserID;
import se2.praktikum.projekt.models.team.Team;
import se2.praktikum.projekt.models.team.fachwerte.TeamID;
import se2.praktikum.projekt.models.veranstaltung.Fach;
import se2.praktikum.projekt.models.veranstaltung.Praktikum;
import se2.praktikum.projekt.models.veranstaltung.Projekt;
import se2.praktikum.projekt.models.veranstaltung.Veranstaltung;
import se2.praktikum.projekt.models.veranstaltung.WP;
import se2.praktikum.projekt.services.loginservice.ServicePool;
import se2.praktikum.projekt.tools.ErrorLogger;

public class StudentVerwSrv {

	private ServicePool servicePool;
	private Connection connection;

	public StudentVerwSrv(ServicePool servicePool) {

		this.servicePool = servicePool;
		connect();
	}

	public void connect() {
		connection = DBConnector.getConnection();
	}

	/**
	 * Holt alle Gruppen für ein Pflichtpraktikum für Datenpuffer
	 * 
	 * @param fachkuerzel
	 *            : Das Kuerzel des Pflichtpraktikums Beispiel "Alle": Alle
	 *            Gruppen für jedes Pflichtpraktikum, "GKA": Alle Gruppen für
	 *            GKA
	 * @param vTyp 
	 * @param userID 
	 * @return Liste von Gruppen für das Fachkuerzel
	 */
	public List<Gruppe> getGruppenTeam(String fachkuerzel, String vTyp, MatrikelNr userID) {
		
		Student user = (Student) this.servicePool.getUser(userID);
		List<Gruppe> alleGruppen = new ArrayList<>();
		List<Gruppe> gruppen = new ArrayList<>();
		Veranstaltung va = new Praktikum();
		va.setFach(new Fach(fachkuerzel));
		
		try {
			
			if(fachkuerzel.equals("Alle")){
				gruppen = getAlleGruppen(vTyp, "team", user);
			}else{
				alleGruppen.addAll(servicePool.getVerAnzSrv().getAllGruppen(va));
				gruppen = entferneUngueltigeGruppen(alleGruppen, user, "team");
			}
				
		} catch (SQLException e) {
			ErrorLogger.log(e);
			return new ArrayList<>();
				
		}

		return gruppen;
	}

	private List<Gruppe> getAlleGruppen(String veranTyp, String anmTyp, Student user) throws SQLException {
		
		String qry = "select fkuerzel from veranstaltung where typ = ?";
		List<Gruppe> alleGruppen = new ArrayList<>();
		List<Gruppe> gruppen = new ArrayList<>();
		List<String> fachkuerzel = new ArrayList<>();
		PreparedStatement stmt = connection.prepareStatement(qry);
		stmt.setString(1, veranTyp);
		ResultSet rs = stmt.executeQuery();
		
		
		while(rs.next()){
			
			fachkuerzel.add(rs.getString("fkuerzel"));
		}
		
		stmt.close();
		
		for(String fk: fachkuerzel){
			
			Veranstaltung prak = new Praktikum();
			prak.setFach(new Fach(fk));
			alleGruppen.addAll(servicePool.getVerAnzSrv().getAllGruppen(prak));
		}
		
		gruppen = entferneUngueltigeGruppen(alleGruppen, user, anmTyp);
		
		return gruppen;
		
	}


	private List<Gruppe> entferneUngueltigeGruppen(List<Gruppe> alleGruppen, Student user, String typ) throws SQLException {
		
		List<String> fachkuerzel = new ArrayList<>();
		List<Gruppe> gruppen = new ArrayList<>();
		
		for(Gruppe gr: alleGruppen){
			
			if(!fachkuerzel.contains(gr.getFachkuerzel())){
				
				fachkuerzel.add(gr.getFachkuerzel());
			}
		}
		
		
		for(String fk: fachkuerzel){
			
			if(hatNochKeinePvl(user, fk)
				&& hatNochKeinTeam(user, fk)){
				
				for(Gruppe gr: alleGruppen){
					
					if(gr.getFachkuerzel().equals(fk)){
						
						gruppen.add(gr);
					}
				}
			}
		}
		
		List<Gruppe> returnGruppen = new ArrayList<Gruppe>();
		
		if(typ.equals("team")){
	 		for(Gruppe gr: gruppen){
	 			
	 			if(hatNochTeamKapazitaeten(gr)){
	 				returnGruppen.add(gr);
	 			}
			}
		}else{
			
	 		for(Gruppe gr: gruppen){
	 			
	 			if(hatNochKapazitaeten(gr)){
	 				returnGruppen.add(gr);
	 			}
			}
			
		}

		
		return returnGruppen;
		
	}
	
	private boolean hatNochKapazitaeten(Gruppe g) {
		
		if(servicePool.getVerAnzSrv().getTeamanzahl(g) < g.getMaxTeams()){
			return true;
		}else{
			List<Team> teams = servicePool.getVerAnzSrv().getAllTeilnehmer(g);
			for(Team team: teams){
				if(team.getMitglieder().size() < team.getMaxTeiln()){
					return true;
				}
			}
		}
		return false;
	}

	private boolean hatNochTeamKapazitaeten(Gruppe gr) {
		
		return servicePool.getVerAnzSrv().getTeamanzahl(gr) < gr.getMaxTeams();
	}

	/**
	 * Holt alle Studenten zu dem Fachbereich und der Prüfungsordnung des
	 * anmeldenden Studenten für Datenpuffer
	 * 
	 * @param fachkuerzel
	 *            : Fachbereich des anmeldenden Studenten
	 * @param userID 
	 * @return Liste von Studenten
	 */
	public List<Student> getAlleStudenten(String fachkuerzel, UserID userID) {

		
		List<Student> studenten = new ArrayList<Student>();
		String qry = "select matrnr, vorname, nachname from Student where fbkuerzel = ?";
		
		try {
			PreparedStatement stmt = connection.prepareStatement(qry);
			stmt.setString(1, "AI");		// Erstmal AI -> Später Login
			ResultSet rs = stmt.executeQuery();
			
			while(rs.next()){
				
				MatrikelNr matrNr = MatrikelNr.getMatrikelNr(rs.getInt("matrNr"));
				String vorname = rs.getString("vorname");
				String nachname = rs.getString("nachname");
				Student stud = new Student(matrNr, vorname, nachname);
				
				if(hatNochKeinePvl(stud, fachkuerzel)
						&& hatNochKeinTeam(stud, fachkuerzel)
						  && !stud.equals(servicePool.getUser(userID))){
					
					studenten.add(stud);
				}
				
			}
			stmt.close();
			
		} catch (SQLException e) {
			ErrorLogger.log(e);
		}

		return studenten;
	}


	public boolean hatNochKeinTeam(Student stud, String fachkuerzel) {
		
		Map<TeamID, Boolean> map = new HashMap<>();
		String qry = "select teamid, bestaetigt from teammitglied where matrnr = ?";
		//List<TeamID> teamIds = new ArrayList<>();
		try {
			
			PreparedStatement stmt = connection.prepareStatement(qry);
			stmt.setInt(1, stud.getMatrNr().getMatrNr());
			ResultSet rs = stmt.executeQuery();
			
			while(rs.next()){
				
				TeamID id = TeamID.getTeamID(rs.getString("teamid"));
				map.put(id, Boolean.parseBoolean(rs.getString("bestaetigt")));
			}
			
			for(TeamID id: map.keySet()){
				
				String qry2 = "select fkuerzel from team where teamid = ?";
				stmt = connection.prepareStatement(qry2);
				stmt.setInt(1, Integer.parseInt(id.getId()));
				rs = stmt.executeQuery();
				
				while(rs.next()){
					
					if(rs.getString("fkuerzel").equals(fachkuerzel)
							&& map.get(id)){
						
						return false;
					}
				}
				stmt.close();
			}
			
		} catch (SQLException e) {
			
			ErrorLogger.log(e);
		}
		return true;
	}

	private boolean hatNochKeinePvl(Student stud, String fachkuerzel) throws SQLException {

		Map<String, Boolean> pvlMap = new HashMap<>();
		pvlMap.put("erhalten", true);
		pvlMap.put("nicht erhalten", false);
		String qry = "select pvl, datumPVL from bewertung where matrnr = ? and fkuerzel = ?";
		PreparedStatement stmt = connection.prepareStatement(qry);
		stmt.setInt(1, stud.getMatrNr().getMatrNr());
		stmt.setString(2, fachkuerzel);
		ResultSet rs = stmt.executeQuery();
		String pvlString = null;
		boolean pvl = false;
		
		while(rs.next()){
			
			pvlString = rs.getString("pvl");
			pvl = Boolean.parseBoolean(pvlString);
			
		}
		
		if(pvlString == null){
			return true;
		}
		
		if(pvl){
			
			return false;
		}
		stmt.close();
		
		return true;
	}

	/**
	 * Holt die Gruppe in der das Team vertreten ist
	 * 
	 * @param Teameinladung
	 * @return
	 */
	public Gruppe getGruppe(Team team) {

		Statement statement = null;
		String qry = "select grpNr,FKuerzel,maxTeams,ProfMAID, AssisMAID, p.vorname, p.nachname, a.vorname as vn, a.nachname as nn from Gruppe g, Assistent a, Professor p where ProfMAID = p.MAID AND AssisMAID = a.MAID AND g.grpNr = "
				+ team.getVorgGrpNr()
				+ " AND FKuerzel = '"
				+ team.getFachkuerzel()
				+ "'";
		Gruppe gruppe = new Gruppe();

		System.out.println(qry);
		try {
			statement = connection.createStatement();
			ResultSet rs = statement.executeQuery(qry);

			while (rs.next()) {
				gruppe.setGrpNr(rs.getInt("grpNr"));
				gruppe.setFachkuerzel(rs.getString("FKuerzel"));
				gruppe.setAssistent(new Assistent(MAID.getMAID(rs
						.getInt("AssisMAID")), rs.getString("vn"), rs
						.getString("nn")));
				gruppe.setProfessor(new Professor(MAID.getMAID(rs
						.getInt("ProfMAID")), rs.getString("vorname"), rs
						.getString("nachname")));
				gruppe.setMaxTeams(rs.getInt("maxTeams"));
				gruppe.setTermine(servicePool.getVerAnzSrv().getTermine(gruppe.getGrpNr(), gruppe.getFachkuerzel()));
			}
			statement.close();
		} catch (SQLException e) {
			ErrorLogger.log(e);
			return null;
		}

		return gruppe;
	}

	/**
	 * Prüft ob die Anmeldephase für Teams abgelaufen ist ab diesem Zeitpunkt
	 * werden Einzelanmeldungen freigeschaltet
	 * 
	 * @return abgelaufen (ja/nein)
	 */
	public boolean pruefeAnmeldePhaseAbgelaufenFuerTeams() {

		boolean erg = false;

		Statement statement = null;
		String qry = "select EinzelStartDatum from Veranstaltung";

		try {
			statement = connection.createStatement();
			ResultSet rs = statement.executeQuery(qry);

			long zeit = System.currentTimeMillis();
			if (rs.getDate("EinzelStartDatum").before(new Date(zeit)))
				erg = true;

			statement.close();
		} catch (SQLException e) {
			ErrorLogger.log(e);
		}
		

		return erg;

	}

	public boolean speichereEinzelAnmeldung(Gruppe gruppe, UserID userID){
		
		boolean erfolg = false;
		Student user = (Student) servicePool.getUser(userID);
	
		try {
			if(hatNochKapazitaeten(gruppe)
					&& hatNochKeinTeam(user, gruppe.getFachkuerzel())){
				
				erfolg = sucheFreienTeamPlatz(user, gruppe, userID);
				erfolg &= legeLeistungAn(user, gruppe);	
			}else{
				erfolg = false;;
			}
		}catch (SQLException e) {
			ErrorLogger.log(e);
			return false;
		}
			
		return erfolg;
		
		
	}


	private boolean sucheFreienTeamPlatz(Student user, Gruppe gruppe, UserID userID) throws SQLException {
		
		boolean erfolg = false;
		List<Team> teams = servicePool.getVerAnzSrv().getAllTeilnehmer(gruppe);
		for(Team team: teams){
			
			if(team.getMitglieder().size() < team.getMaxTeiln()){
				
				team.getMitglieder().add(user);
				return this.servicePool.getTeamVerwSrv().bestaetigeTeammitgliedNachEinzelanmeldung(team, user);
			}
		}
		
		Team neuesTeam = null;
		if(!teams.isEmpty()){
			
			TeamID neueId = neueTeamId();
			neuesTeam = new Team(neueId, teams.get(0).getMinTeiln(), 
								 teams.get(0).getMaxTeiln(), 
								 new ArrayList<>(Arrays.asList(user)), 
								 gruppe.getGrpNr(), gruppe.getGrpNr(), 
								 gruppe.getFachkuerzel());
		}else{
			
			Veranstaltung va = getVeranstaltung(gruppe);
			neuesTeam = new Team(neueTeamId(), 
					  va.getMinTeilnTeam(), 
					  va.getMaxTeilnTeam(), 
					  new ArrayList<>(Arrays.asList(user)), 
					  gruppe.getGrpNr(), gruppe.getGrpNr(), 
					  gruppe.getFachkuerzel());
		}
		
		erfolg = this.servicePool.getTeamVerwSrv().legeTeamAn(neuesTeam, "einzel", userID);
		erfolg &= this.servicePool.getTeamVerwSrv().meldeTeamAn(neuesTeam);
		
		return erfolg;
	}
	
	private Veranstaltung getVeranstaltung(Gruppe gruppe) {
		
		String qry = "select * from Veranstaltung where fkuerzel = ?";
		Veranstaltung va = null;
		try {
			PreparedStatement stmt = connection.prepareStatement(qry);
			stmt.setString(1, gruppe.getFachkuerzel());
			ResultSet rs = stmt.executeQuery();
			while(rs.next()){
				
				String fbKuerzel = rs.getString("fbkuerzel");
				String fBezeichnung = rs.getString("fbezeichnung");
				int semester = rs.getInt("semester");
				String typ = rs.getString("typ");
				int minTeilnTeam = rs.getInt("minTeilnTeam");
				int maxTeilnTeam = rs.getInt("maxTeilnTeam");
				int anzTm = this.servicePool.getVerAnzSrv().getTeamanzahl(gruppe.getFachkuerzel());
				int anzGr = this.servicePool.getVerAnzSrv().getGruppenanzahl(gruppe.getFachkuerzel());
				MAID maid = MAID.getMAID(rs.getInt("maid"));
				Professor prof = servicePool.getVerAnzSrv().getProfessor(maid);
				
				Fach fach = new Fach(gruppe.getFachkuerzel(), fBezeichnung, null, semester, fbKuerzel);
				
				if(typ.equals("Praktikum")){
					
					va = new Praktikum(fach, fach.getSemester(), prof, anzTm, anzGr, minTeilnTeam, maxTeilnTeam);
				}else if(typ.equals("WPP")){
					va = new WP(fach, fach.getSemester(), prof, anzTm, anzGr, minTeilnTeam, maxTeilnTeam);
				}else if(typ.equals("PO")){
					va = new Projekt(fach, fach.getSemester(), prof, anzTm, anzGr, minTeilnTeam, maxTeilnTeam);
				}
				
			}
			
			stmt.close();
		} catch (SQLException e) {
			ErrorLogger.log(e);
			return null;
		}
		
		return va;
	}

	private TeamID neueTeamId() throws SQLException {
		
		List<TeamID> teamIds = new ArrayList<>();
		String qry = "select teamid from team";
		PreparedStatement stmt = connection.prepareStatement(qry);
		ResultSet rs = stmt.executeQuery();
		
		while(rs.next()){
			teamIds.add(TeamID.getTeamID(rs.getString("teamid")));
		}
		stmt.close();
		
		int maxTeamId = Integer.MIN_VALUE;
		
		for(TeamID tid: teamIds){
			
			int id = Integer.parseInt(tid.getId());
			
			if(id > maxTeamId){
				maxTeamId = id;
			}
		}
		
		return TeamID.getTeamID("" + (++maxTeamId));
		
	}

	public boolean legeLeistungAn(Student user, Gruppe gruppe) {
		
		boolean erfolg = false;
		String qry = "insert into Bewertung values(?, ?, ?, ?, ?, ?, ?)";
		try {
			gruppe.setProfessor(getProfessor(gruppe));
			PreparedStatement stmt = connection.prepareStatement(qry);
			stmt.setInt(1, gruppe.getProfessor().getMaID().getId());
			stmt.setInt(2, user.getMatrNr().getMatrNr());
			stmt.setString(3, gruppe.getFachkuerzel());
			stmt.setDate(4, null);
			stmt.setDate(5, null);
			stmt.setString(6, "false");
			stmt.setString(7, null);
			stmt.executeUpdate();
			stmt.close();
			erfolg = true;
			
		} catch (SQLException e) {
			ErrorLogger.log(e);
			return false;
		}
		
		return erfolg;
	}
	
	private Professor getProfessor(Gruppe gruppe) throws SQLException {
		String qry = "select vorname, nachname, maid from professor where maid in (select profmaid from gruppe where grpNr = ? and fkuerzel = ?)";
		PreparedStatement stmt = connection.prepareStatement(qry);
		stmt.setInt(1, gruppe.getGrpNr());
		stmt.setString(2, gruppe.getFachkuerzel());
		ResultSet rs = stmt.executeQuery();
		String vorname = null;
		String nachname = null;
		MAID maid = null;
		while(rs.next()){
			vorname = rs.getString("vorname");
			nachname = rs.getString("nachname");
			maid = MAID.getMAID(rs.getInt("maid"));
		}
		
		stmt.close();
		
		return new Professor(maid, vorname, nachname);
	}

	public boolean istNochNichtAngemeldet(Student user, Gruppe gruppe) throws SQLException {
		
		String qry = "select matrnr from einzelanmeldung where matrnr = ? and grpNr = ? and fkuerzel = ?";
		PreparedStatement stmt = connection.prepareStatement(qry);
		stmt.setInt(1, user.getMatrNr().getMatrNr());
		stmt.setInt(2, gruppe.getGrpNr());
		stmt.setString(3, gruppe.getFachkuerzel());
		ResultSet rs = stmt.executeQuery();
		MatrikelNr matrNr = null;
		while(rs.next()){
			int result = rs.getInt("matrnr");
			if(result != 0){
				
				matrNr = MatrikelNr.getMatrikelNr(result);
			}
		}
		
		stmt.close();
		return matrNr == null && hatNochKeinTeam(user, gruppe.getFachkuerzel());
	}

	public List<Team> getAlleTeamsAktuellerStudent(UserID userID){

		List<Team> teams = new ArrayList<Team>();
		Statement statement = null;
		Student user = (Student) servicePool.getUser(userID);
		String qry = "select * from Teammitglied, Team where Teammitglied.TeamID = Team.TeamID AND matrNr = " + user.getMatrNr().getMatrNr();

		try {
			statement = connection.createStatement();
			ResultSet rs = statement.executeQuery(qry);

			while (rs.next()) {
				
				boolean bestaetigt = Boolean.parseBoolean(rs.getString("bestaetigt"));
				
				if(bestaetigt){
					Team team = new Team();
					team.setFachkuerzel(rs.getString("FKuerzel"));
					team.setGrpNr(rs.getInt("grpNr"));
					team.setVorgGrpNr(rs.getInt("VorgGrpNr"));
					team.setMinTeiln(rs.getInt("minmitglieder"));
					team.setMaxTeiln(rs.getInt("maxmitglieder"));
					TeamID id = TeamID.getTeamID(rs.getString("TeamID"));
					team.setTeamID(id);
					team.setMitglieder(getMitglieder(id));
					teams.add(team);
				}

			}

			statement.close();
		} catch (SQLException e) {
			ErrorLogger.log(e);
			return new ArrayList<>();
		}

		return teams;
	}
	
	private List<Student> getMitglieder(TeamID teamID){
		
		List<Student> mitglieder = new ArrayList<Student>();		
		Statement statement = null;
		String qry = "select * from Student s, Teammitglied t where s.matrNr = t.matrNr AND TeamID = '" + teamID.getId() + "'";

		try {
			statement = connection.createStatement();
			ResultSet rs = statement.executeQuery(qry);

			while (rs.next()) {
				Student student = new Student();
				String vorname = rs.getString("vorname");
				String nachname = rs.getString("nachname");
				student.setVorname(vorname);
				student.setNachname(nachname);
				boolean bestaetigt = Boolean.parseBoolean(rs.getString("bestaetigt"));
				student.setBestaetigt(bestaetigt);
				student.setEinzel(Boolean.parseBoolean(rs.getString("einzel")));
				student.setVollerName(vorname + " " + nachname);
				student.setMatrNr(MatrikelNr.getMatrikelNr(rs.getInt("matrNr")));
				mitglieder.add(student);
			}

			statement.close();
		} catch (SQLException e) {
			ErrorLogger.log(e);
		}
		
		return mitglieder;
	}

	/**
	 * Setzt einen Servicepool für diese Schnittstelle
	 * 
	 * @param pool
	 */
	public void setServicePool(ServicePool pool) {

		servicePool = pool;

	}

	public List<Gruppe> getGruppenEinzel(String fachkuerzel, String veranTyp, UserID userID) {

		Student user = (Student) this.servicePool.getUser(userID);
		List<Gruppe> alleGruppen = new ArrayList<>();
		List<Gruppe> gruppen = new ArrayList<>();
		Veranstaltung va = new Praktikum();
		va.setFach(new Fach(fachkuerzel));
		
		try {
			
			if(fachkuerzel.equals("Alle")){
				gruppen.addAll(getAlleGruppen(veranTyp, "einzel", user));
			}else{
				alleGruppen.addAll(servicePool.getVerAnzSrv().getAllGruppen(va));
				
				gruppen = entferneUngueltigeGruppen(alleGruppen, user, "einzel");
			}
				
		} catch (SQLException e) {
			ErrorLogger.log(e);
			return new ArrayList<>();
				
		}

		return gruppen;
	}
}