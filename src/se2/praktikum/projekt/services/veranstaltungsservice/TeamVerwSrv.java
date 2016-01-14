package se2.praktikum.projekt.services.veranstaltungsservice;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import se2.praktikum.projekt.dbms.DBConnector;
import se2.praktikum.projekt.models.gruppe.Gruppe;
import se2.praktikum.projekt.models.meldungen.GruppevollMeldung;
import se2.praktikum.projekt.models.meldungen.Meldung;
import se2.praktikum.projekt.models.meldungen.MeldungTyp;
import se2.praktikum.projekt.models.meldungen.TEAblehnung;
import se2.praktikum.projekt.models.meldungen.TeamAustrittsmeldung;
import se2.praktikum.projekt.models.meldungen.Teameinladung;
import se2.praktikum.projekt.models.person.Student;
import se2.praktikum.projekt.models.person.fachwerte.MatrikelNr;
import se2.praktikum.projekt.models.person.fachwerte.UserID;
import se2.praktikum.projekt.models.team.Team;
import se2.praktikum.projekt.models.team.fachwerte.TeamID;
import se2.praktikum.projekt.services.loginservice.ServicePool;
import se2.praktikum.projekt.tools.ErrorLogger;

public class TeamVerwSrv {

	private ServicePool servicePool;
	private Connection connection;

	public TeamVerwSrv(ServicePool servicePool) {

		this.servicePool = servicePool;
		connect();
	}

	public void connect() {
		connection = DBConnector.getConnection();
	}

	public boolean meldeTeamAn(Team team) throws SQLException {
		
		boolean erfolg = false;
		if(!istTeamKapazitaetGruppeErreicht(team)){
			
			String qry = "update Team set GrpNr = ? where teamid = ?";
			PreparedStatement stmt = connection.prepareStatement(qry);
			stmt.setInt(1, team.getVorgGrpNr());
			stmt.setInt(2, Integer.parseInt(team.getTeamID().getId()));
			stmt.executeUpdate();
			stmt.close();
			
			team.setGrpNr(team.getVorgGrpNr());
			erfolg = legeLeistungenAn(team);
		}else{
			loescheTeam(team);
		}
		
		if(istTeamKapazitaetGruppeErreicht(team)){
			
			erfolg = loescheAlleVorgemerktenTeams(team.getFachkuerzel(), team.getGrpNr());
		}
		
		
		return erfolg;
		
	}



	private boolean legeLeistungenAn(Team team) {
		
		for(Student stud: team.getMitglieder()){
			
			Gruppe gr = new Gruppe(team.getFachkuerzel(), team.getGrpNr());
			
			if(!this.servicePool.getStudentVerwSrv().legeLeistungAn(stud, gr)){
				return false;
			}
		}
		
		return true;
	}

	private boolean loescheAlleVorgemerktenTeams(String fachkuerzel, int grpNr) {
		
		boolean erfolg = false;
		List<Team> teams = new ArrayList<>();
		String qry = "select teamid from team where VorgGrpNr = ? and fkuerzel = ? and grpNr is null";
		
		try {
			PreparedStatement stmt = connection.prepareStatement(qry);
			stmt.setInt(1, grpNr);
			stmt.setString(2, fachkuerzel);
			ResultSet rs = stmt.executeQuery();
			while(rs.next()){
				
				TeamID id = TeamID.getTeamID(rs.getString("teamid"));
				Team team = new Team();
				team.setTeamID(id);
				team.setFachkuerzel(fachkuerzel);
				team.setGrpNr(grpNr);
				team.setMitglieder(servicePool.getVerAnzSrv().getMitglieder(team.getTeamID()));
				teams.add(team);
			}
			erfolg = true;
			
			stmt.close();
		} catch (SQLException e) {
			ErrorLogger.log(e);
			return false;
			
		}

		
		for(Team team: teams){
			
			erfolg &= loescheTeam(team);
			erfolg &= loescheTeamEinladungen(team);
			erfolg &= sendeGruppeVollMeldung(team);
		}
		
		return erfolg;
	}

	private boolean loescheTeamEinladungen(Team team) {
		
		String qry = "delete Meldung where teamid = ? and fkuerzel = ? and typ = ?";
		try {
			PreparedStatement stmt = connection.prepareStatement(qry);
			stmt.setInt(1, Integer.parseInt(team.getTeamID().getId()));
			stmt.setString(2, team.getFachkuerzel());
			stmt.setString(3, MeldungTyp.TEAMEINL);
			stmt.executeUpdate();
			stmt.close();
		} catch (SQLException e) {
			ErrorLogger.log(e);
			return false;
		}
		return true;
		
		
	}

	private boolean loescheTeam(Team team) {
		
		String qry = "delete Teammitglied where teamid = ?";
		String qry2 = "delete Team where teamid = ?";
		
		try {
			PreparedStatement stmt = connection.prepareStatement(qry);
			stmt.setInt(1, Integer.parseInt(team.getTeamID().getId()));
			stmt.executeUpdate();
			stmt = connection.prepareStatement(qry2);
			stmt.setInt(1, Integer.parseInt(team.getTeamID().getId()));
			stmt.executeUpdate();
			stmt.close();
			
		} catch (SQLException e) {
			ErrorLogger.log(e);
			return false;
			
		}
		
		return true;
	}

	private boolean istTeamKapazitaetGruppeErreicht(Team team) throws SQLException {
		
		Gruppe gruppe = new Gruppe(team.getFachkuerzel(), team.getVorgGrpNr());
		
		String qry = "select maxTeams from gruppe where fkuerzel = ? and grpNr = ?";
		PreparedStatement stmt = connection.prepareStatement(qry);
		stmt.setString(1, team.getFachkuerzel());
		stmt.setInt(2, team.getVorgGrpNr());
		ResultSet rs = stmt.executeQuery();
		int maxTeams = 0;
		while(rs.next()){
			maxTeams = rs.getInt("maxTeams");
		}
		
		stmt.close();
		int anzTeams = this.servicePool.getVerAnzSrv().getTeamanzahl(gruppe);
		return anzTeams >= maxTeams;
		
	}


	private boolean sendeGruppeVollMeldung(Team team) {
		
		Student absender = new Student(MatrikelNr.getMatrikelNr(99999999), "SYSTEM", "");
		String qry = "insert into Meldung values(?, ?, ?, ?, ?, ?, ?, ?)";
		
		try {
			
			PreparedStatement stmt = null;
			for(Student stud: team.getMitglieder()){
				stmt = connection.prepareStatement(qry);
				stmt.setInt(1, absender.getMatrNr().getMatrNr());
				stmt.setInt(2, stud.getMatrNr().getMatrNr());
				stmt.setString(3, team.getFachkuerzel());
				stmt.setInt(4, Integer.parseInt(team.getTeamID().getId()));
				stmt.setDate(5, new java.sql.Date(new Date().getTime()));
				stmt.setString(6, null);
				stmt.setDate(7, null);
				stmt.setString(8, MeldungTyp.GRUPPE_VOLL);
				stmt.executeUpdate();
			}
			stmt.close();
		} catch (SQLException e) {
			
			ErrorLogger.log(e);
			return false;
			
		}
		
		
		return true;
		
	}

	/**
	 * Speichert eine abgelehnte Teameinladung
	 * 
	 * @param Teameinladung
	 *            : Die Teameinladung die abgelehnt wurde
	 * @return boolean : erfolgreich/nicht erfolgreich
	 */
	public boolean sendeTeamEinladung(Team team, UserID userID) {
		
		try {
			if(!istTeamKapazitaetGruppeErreicht(team)){
				
				
				String qry = "insert into Meldung values(?, ?, ?, ?, ?, ?, ?, ?)";
				Student absender = (Student) this.servicePool.getUser(userID);
				
				team.getMitglieder().remove(absender);
					
				for(Student mg: team.getMitglieder()){
					
					PreparedStatement stmt = connection.prepareStatement(qry);
					stmt.setInt(1, absender.getMatrNr().getMatrNr());
					stmt.setInt(2, mg.getMatrNr().getMatrNr());
					stmt.setString(3, team.getFachkuerzel());
					
					if(team.getTeamID() == null){
						int teamId = getTeamId(team);
						team.setTeamID(TeamID.getTeamID("" + teamId));
						stmt.setInt(4, teamId);
					}
					loescheTeameinladung(team, mg, absender);
					loescheTeamAustrittsmeldung(team, mg);
					loescheTeamAustrittsmeldung(team, absender);
					loescheTEAblehnung(team, absender);
					loescheTEAblehnung(team, mg);
					if(istNochKeinTeammitglied(mg, team)){
						
						stmt.setInt(4, Integer.parseInt(team.getTeamID().getId()));
						stmt.setDate(5, new java.sql.Date(System.currentTimeMillis()));
						stmt.setString(6, "" + false);
						stmt.setDate(7, null);
						stmt.setString(8, "Teameinladung");
						stmt.executeUpdate();
						
					}
					

					stmt.close();
			
				} 
								
				return legeTeamAn(team, "team", userID);
			}
			
		}catch (SQLException e) {
			
			ErrorLogger.log(e);
			return false;
		}
		
		return false;
		
	}
		


	

	private boolean istNochKeinTeammitglied(Student mg, Team team) {
		
		String qry = "select count(*) as count from teammitglied where teamid = ? and matrnr = ? and bestaetigt = ?";
		int count = 0;
		try {
			PreparedStatement stmt = connection.prepareStatement(qry);
			stmt.setInt(1, Integer.parseInt(team.getTeamID().getId()));
			stmt.setInt(2, mg.getMatrNr().getMatrNr());
			stmt.setString(3, "true");
			ResultSet rs = stmt.executeQuery();
			
			while(rs.next()){
				
				count = rs.getInt("count");
			}
			
			stmt.close();
		} catch (SQLException e) {
			ErrorLogger.log(e);
		}
		
		return count == 0;
	}

	private void loescheTeamAustrittsmeldung(Team team, Student mg) {
		
		String qry = "delete Meldung where typ = ? and empfaenger = ? and teamid = ?";
		try {
			PreparedStatement stmt = connection.prepareStatement(qry);
			stmt.setString(1, MeldungTyp.TEAMAUSTR);
			stmt.setInt(2, mg.getMatrNr().getMatrNr());
			stmt.setInt(3, Integer.parseInt(team.getTeamID().getId()));
			
			
			stmt.executeUpdate();
			stmt.close();
			
		} catch (SQLException e) {
			ErrorLogger.log(e);
		}
		
		
	}

	private void loescheTeameinladung(Team team, Student mg, Student absender) {
		
		String qry = "delete Meldung where typ = ? and absender = ? and empfaenger = ? and fkuerzel = ? and teamid = ?";
		try {
			PreparedStatement stmt = connection.prepareStatement(qry);
			stmt.setString(1, MeldungTyp.TEAMEINL);
			stmt.setInt(2, absender.getMatrNr().getMatrNr());
			stmt.setInt(3, mg.getMatrNr().getMatrNr());
			stmt.setString(4, team.getFachkuerzel());
			stmt.setInt(5, Integer.parseInt(team.getTeamID().getId()));
			stmt.executeUpdate();
			stmt.close();
		} catch (SQLException e) {
			ErrorLogger.log(e);
		}
		
	}

	/**
	 * Speichert eine abgelehnte Teameinladung
	 * 
	 * @param Teameinladung
	 *            : Die Teameinladung die abgelehnt wurde
	 * @return boolean : erfolgreich/nicht erfolgreich
	 */
	public boolean lehneTeamEinladungAb(Teameinladung teameinladung) {
		
		boolean erfolg = false;
		Student empfaenger = (Student) teameinladung.getEmpfaenger();
		String qry = "delete Teammitglied where teamid = ? and matrnr = ?";
		teameinladung.getTeam().getMitglieder().remove(empfaenger);
		try {
			PreparedStatement stmt = connection.prepareStatement(qry);
			stmt.setInt(1, Integer.parseInt(teameinladung.getTeam().getTeamID().getId()));
			stmt.setInt(2, empfaenger.getMatrNr().getMatrNr());
			stmt.executeUpdate();
			erfolg = loescheTeamEinladung(teameinladung);
			erfolg &= loescheTEAblehnung(teameinladung.getTeam(), empfaenger); // Falls zuvor schon welche angelegt
			erfolg &= sendeTEAblehnung(teameinladung.getTeam(), empfaenger);  
			stmt.close();
			
		} catch (SQLException e) {
			ErrorLogger.log(e);
			return false;
		}
		
		
		return erfolg;
	}

	private boolean loescheTEAblehnung(Team team, Student empfaenger) {
		
		String qry = "delete Meldung where typ = ? and empfaenger = ? and teamid = ?";
		try {
			PreparedStatement stmt = connection.prepareStatement(qry);
			stmt.setString(1, MeldungTyp.TEABLEHN);
			stmt.setInt(2, empfaenger.getMatrNr().getMatrNr());
			stmt.setInt(3, Integer.parseInt(team.getTeamID().getId()));
			
			
			stmt.executeUpdate();
			stmt.close();
			
		} catch (SQLException e) {
			ErrorLogger.log(e);
		}
		
		return true;
	}

	/**
	 * Sendet eine Teameinladungsablehnung nachdem eine abgelehnte Teameinladung
	 * gespeichert wurde
	 * @param empfaenger2 
	 * 
	 * @param ablehnung
	 *            : Die Teameinladungsablehnung
	 * @return erfolgreich/nicht erfolgreich
	 */
	private boolean sendeTEAblehnung(Team team, Student empfaenger) {
		
		boolean erfolg = false;
		String qry = "insert into Meldung values(?, ?, ?, ?, ?, ?, ?, ?)";
		java.sql.Date date = new java.sql.Date(System.currentTimeMillis());
		
		try {
			
			for(Student stud: team.getMitglieder()){
				
				if(mitgliedIstBestaetigt(stud, team)){
					
					PreparedStatement stmt = connection.prepareStatement(qry);
					stmt.setInt(1, empfaenger.getMatrNr().getMatrNr());;
					stmt.setInt(2, stud.getMatrNr().getMatrNr());
					stmt.setString(3, team.getFachkuerzel());
					stmt.setInt(4, Integer.parseInt(team.getTeamID().getId()));
					stmt.setDate(5, date);
					stmt.setString(6, "null");
					stmt.setDate(7, null);
					stmt.setString(8, MeldungTyp.TEABLEHN);
					stmt.executeUpdate();
					stmt.close();
					
				}

			}
			
			erfolg = true;
			
		} catch (SQLException e) {
			
			ErrorLogger.log(e);
			return false;
		}
		
		
		return erfolg;
	}

	private boolean mitgliedIstBestaetigt(Student stud, Team team) throws SQLException {
		String qry = "select bestaetigt from teammitglied where matrNr = ? and teamid = ?";
		PreparedStatement stmt = connection.prepareStatement(qry);
		stmt.setInt(1, stud.getMatrNr().getMatrNr());
		stmt.setInt(2, Integer.parseInt(team.getTeamID().getId()));
		ResultSet rs = stmt.executeQuery();
		boolean bestaetigt = false;
		
		while(rs.next()){
			
			bestaetigt = Boolean.parseBoolean(rs.getString("bestaetigt"));
		}
		
		stmt.close();
		return bestaetigt;
	}

	public boolean nehmeTeamEinladungAn(Teameinladung teameinladung) {
		
		boolean erfolg = false;
		Team team = teameinladung.getTeam();
		Student empfaenger = (Student) teameinladung.getEmpfaenger();
		
		String qry = "update teammitglied set bestaetigt = 'true' where teamid = ? and matrnr = ?";
		try {
			
			PreparedStatement stmt = connection.prepareStatement(qry);
			stmt.setInt(1, Integer.parseInt(team.getTeamID().getId()));
			stmt.setInt(2, empfaenger.getMatrNr().getMatrNr());
			stmt.executeUpdate();
			erfolg = loescheTeamEinladung(teameinladung);
			stmt.close();
			
		} catch (SQLException e) {
			
			ErrorLogger.log(e);
			return false;
			
		}
		
		try{
			
			if(pruefeAlleEinladungenBestaetigt(team)){
				
				erfolg &= meldeTeamAn(team);
			}
			
		}catch (SQLException e) {
			
			ErrorLogger.log(e);
			return false;
			
		}
		return erfolg;
		

	}

	private boolean loescheTeamEinladung(Teameinladung teameinladung) {
		
		boolean erfolg = false;
		Student absender = (Student) teameinladung.getAbsender();
		Student empfaenger = (Student) teameinladung.getEmpfaenger();
		Team team = teameinladung.getTeam();
		
		String qry = "delete Meldung where absender = ? and empfaenger = ? and fkuerzel = ? and teamid = ? and typ = ?";
		try {
			PreparedStatement stmt = connection.prepareStatement(qry);
			stmt.setInt(1, absender.getMatrNr().getMatrNr());
			stmt.setInt(2, empfaenger.getMatrNr().getMatrNr());
			stmt.setString(3, team.getFachkuerzel());
			stmt.setInt(4, Integer.parseInt(team.getTeamID().getId()));
			stmt.setString(5, MeldungTyp.TEAMEINL);
			stmt.executeUpdate();
			stmt.close();
			erfolg = true;
			
		} catch (SQLException e) {
			ErrorLogger.log(e);
			return false;
		}
	
		
		return erfolg;
	}

	private boolean pruefeAlleEinladungenBestaetigt(Team team) throws SQLException {
		
		List<Boolean> bestaetigungen = new ArrayList<>();
		String qry = "select bestaetigt from Teammitglied where teamid = ?";
		PreparedStatement stmt = connection.prepareStatement(qry);
		stmt.setInt(1, Integer.parseInt(team.getTeamID().getId()));
		ResultSet rs = stmt.executeQuery();
		
		
		while(rs.next()){
			
			bestaetigungen.add(Boolean.parseBoolean(rs.getString("bestaetigt")));
		}
		
		stmt.close();
		for(Boolean bestaet: bestaetigungen){
			
			if(!bestaet){
				return false;
			}
		}
		
		if(bestaetigungen.size() >= team.getMinTeiln()){
			return true;
		}
		
		return false;
		
	}

	public boolean entferneTeilnehmer(TeamID teamId, MatrikelNr matrNr) {
		
		String qry = "delete Teammitglied where teamid = ? and matrnr = ?";
		boolean erfolgreich = false;
		
		try {
			PreparedStatement stmt = connection.prepareStatement(qry);
			stmt.setString(1, teamId.getId());
			stmt.setInt(2, matrNr.getMatrNr());
			stmt.executeUpdate();
			
			if(hatKeineMitglieder(teamId)){
				
				String qry2 = "delete Team where teamid = ?";
				stmt = connection.prepareStatement(qry2);
				stmt.setString(1, teamId.getId());
				stmt.executeUpdate();
				
			}
			stmt.close();
			erfolgreich = true;
		} catch (SQLException e) {
			
			ErrorLogger.log(e);
			return false;
			
		}
		
		return erfolgreich;
		
	}

	private boolean hatKeineMitglieder(TeamID teamId) throws SQLException {
		
		String qry = "select count(matrNr) as count from Teammitglied where teamid = ?";
		
		PreparedStatement stmt = connection.prepareStatement(qry);
		stmt.setString(1, teamId.getId());
		ResultSet rs = stmt.executeQuery();
		int count = 0;
		
		while(rs.next()){
			
			count = rs.getInt("count");
		}
		
		stmt.close();
		
		return count < 1;
	}

	public boolean legeTeamAn(Team t, String typ, UserID userID) {
				
		String qry = "insert into team values(?, ?, ?, ?, ?, ?)";
		
		if(teamNochNichtErstellt(t)){
			
			try {
				PreparedStatement stmt = connection.prepareStatement(qry);
				stmt.setInt(1, Integer.parseInt(t.getTeamID().getId()));
				stmt.setString(2, t.getFachkuerzel());
				stmt.setInt(3, t.getMinTeiln());
				stmt.setInt(4, t.getMaxTeiln());
				stmt.setNull(5, java.sql.Types.INTEGER);
				stmt.setInt(6, t.getVorgGrpNr());
				stmt.executeUpdate();
				stmt.close();
				
			} catch (SQLException e) {
				ErrorLogger.log(e);
				return false;
				
			}
			
		}
		
		return fuegeMitgliederEin(t, typ, userID);
		
	}

	private boolean teamNochNichtErstellt(Team t) {
		
		String qry = "select count(*) as count from team where teamid = ?";
		int count = 0;
		
		try {
			PreparedStatement stmt = connection.prepareStatement(qry);
			stmt.setInt(1, Integer.parseInt(t.getTeamID().getId()));
			ResultSet rs = stmt.executeQuery();
			
			while(rs.next()){
				
				count = rs.getInt("count");
				
			}
			
			stmt.close();
			
		} catch (SQLException e) {
			ErrorLogger.log(e);
		}
		
		return count == 0;
	}

	private boolean fuegeMitgliederEin(Team t, String typ, UserID userID) {
		
		boolean erfolg = false;
		String qry = "insert into Teammitglied values(?, ?, ?, ?)";
		try {
			PreparedStatement stmt = null;
			Student absender = (Student) this.servicePool.getUser(userID);
			
			if(abenderNochNichtImTeam(t, absender)){
				stmt = connection.prepareStatement(qry);
				stmt.setInt(1, Integer.parseInt(t.getTeamID().getId()));
				stmt.setInt(2, absender.getMatrNr().getMatrNr());
				stmt.setString(3, "true");
				
				if(typ.equals("team")){
					stmt.setString(4, "false");
				}else if(typ.equals("einzel")){
					stmt.setString(4, "true");
				}
				
				
				stmt.executeUpdate();
			}

			
			t.getMitglieder().remove(absender);
			
			for(Student stud: t.getMitglieder()){
				
				if(istNochKeinTempTeammitglied(stud, t)){
					
					stmt = connection.prepareStatement(qry);
					stmt.setInt(1, Integer.parseInt(t.getTeamID().getId()));
					stmt.setInt(2, stud.getMatrNr().getMatrNr());
					
					stmt.setString(3, "false");
					if(typ.equals("team")){
						stmt.setString(4, "false");
					}else if(typ.equals("einzel")){
						stmt.setString(4, "true");
					}
					
					stmt.executeUpdate();
					stmt.close();
				}
				
				
			}
			
			erfolg = true;
			
		
		} catch (SQLException e) {
			
			ErrorLogger.log(e);
			return false;
			
		}
		return erfolg;
		
		
	}

	private boolean istNochKeinTempTeammitglied(Student stud, Team t) {
		String qry = "select count(*) as count from teammitglied where teamid = ? and matrnr = ?";
		int count = 0;
		try {
			PreparedStatement stmt = connection.prepareStatement(qry);
			stmt.setInt(1, Integer.parseInt(t.getTeamID().getId()));
			stmt.setInt(2, stud.getMatrNr().getMatrNr());
			ResultSet rs = stmt.executeQuery();
			
			while(rs.next()){
				
				count = rs.getInt("count");
			}
			
			stmt.close();
		} catch (SQLException e) {
			ErrorLogger.log(e);
		}
		
		return count == 0;
	}

	private boolean abenderNochNichtImTeam(Team t, Student absender) {
		String qry = "select count(*) as count from teammitglied where matrNr = ? and teamid = ?";
		int count = 0;
		try {
			
			PreparedStatement stmt = connection.prepareStatement(qry);
			stmt.setInt(1, absender.getMatrNr().getMatrNr());
			stmt.setInt(2, Integer.parseInt(t.getTeamID().getId()));
			ResultSet rs = stmt.executeQuery();

			while(rs.next()){
				
				count = rs.getInt("count");
			}
			
			stmt.close();
		} catch (SQLException e) {
			ErrorLogger.log(e);
			return false;
		}
		
		return count == 0;
	}

	private int getTeamId(Team t) throws SQLException {
		
		String qry = "select count(teamid) as count from team";
		PreparedStatement stmt = connection.prepareStatement(qry);
		ResultSet rs = stmt.executeQuery();
		int count = 0;
		
		while(rs.next()){
			
			count = rs.getInt("count");
			
		}
		
		stmt.close();
		
		return ++count;
	}

	public List<Meldung> getAlleMeldungen(UserID userID) {
		
		Student empfaenger = (Student) this.servicePool.getUser(userID);
		List<Meldung> meldungen = new ArrayList<>();
		String qry = "select * from meldung where empfaenger = ?";
		Meldung currMeldung = null;
		try {
			PreparedStatement stmt = connection.prepareStatement(qry);
			stmt.setInt(1, empfaenger.getMatrNr().getMatrNr());
			ResultSet rs = stmt.executeQuery();
			
			while(rs.next()){
				
				MatrikelNr matrNr = MatrikelNr.getMatrikelNr(rs.getInt("absender"));
				Student absender = getStudent(matrNr);
				Date versandDatum = new Date(rs.getDate("versanddatum").getTime());
				String typ = rs.getString("typ");
				if(typ.equals(MeldungTyp.TEAMEINL)){
					
					Team team = getTeam(TeamID.getTeamID(rs.getString("teamid")));
					String bestaetigt = rs.getString("bestaetigt");
					Date bestDatumSQL = rs.getDate("bestaetigungsdatum");
					Date bestDatum = null;
					if(bestDatumSQL != null){
						bestDatum = new Date(bestDatumSQL.getTime());
					}
					
					currMeldung = new Teameinladung(absender, empfaenger, versandDatum, team, Boolean.parseBoolean(bestaetigt), bestDatum, MeldungTyp.TEAMEINL);
				
				}else if(typ.equals(MeldungTyp.GRUPPE_VOLL)){
					
					Team team = getTeam(TeamID.getTeamID(rs.getString("teamid")));
					currMeldung = new GruppevollMeldung(absender, empfaenger, versandDatum, team, MeldungTyp.GRUPPE_VOLL);
				}else if(typ.equals(MeldungTyp.TEABLEHN)){
					
					Team team = getTeam(TeamID.getTeamID(rs.getString("teamid")));
					currMeldung = new TEAblehnung(absender, empfaenger, versandDatum, team, MeldungTyp.TEABLEHN);
				}else if(typ.equals(MeldungTyp.TEAMAUSTR)){
					
					Team team = getTeam(TeamID.getTeamID(rs.getString("teamid")));
					currMeldung = new TeamAustrittsmeldung(absender, empfaenger, versandDatum, team, MeldungTyp.TEAMAUSTR);
					
				}
				
				meldungen.add(currMeldung);
			}
			
			stmt.close();
		} catch (SQLException e) {
			
			ErrorLogger.log(e);
			return new ArrayList<>();
		
		}
		
		return meldungen;
	}

	private Team getTeam(TeamID teamID) throws SQLException {
		
		String qry = "select * from Team where teamid = ?";
		
		PreparedStatement stmt = connection.prepareStatement(qry);
		stmt.setInt(1, Integer.parseInt(teamID.getId()));
		
		ResultSet rs = stmt.executeQuery();
		Team team = null;
		while(rs.next()){
			
			String fachkuerzel = rs.getString("fkuerzel");
			int minMitgl = rs.getInt("minMitglieder");
			int maxMitgl = rs.getInt("maxMitglieder");
			int grpNr = rs.getInt("GrpNr");
			
			if(grpNr == 0){
				grpNr = -1;
			}
			int vorgGrpNr = rs.getInt("VorgGrpNr");
			List<Student> mitglieder = this.servicePool.getVerAnzSrv().getMitglieder(teamID);
			team = new Team(teamID, minMitgl, maxMitgl, mitglieder, grpNr, vorgGrpNr, fachkuerzel);

		}
		
		stmt.close();
		
		return team;
	}

	private Student getStudent(MatrikelNr matrikelNr) {
		
		String qry = "select vorname, nachname from Student where matrnr = ?";
		Student absender = null;
		try {
			PreparedStatement stmt = connection.prepareStatement(qry);
			stmt.setInt(1, matrikelNr.getMatrNr());
			ResultSet rs = stmt.executeQuery();
			
			while(rs.next()){
				
				String vorname = rs.getString("vorname");
				String nachname = rs.getString("nachname");
				absender = new Student(matrikelNr, vorname, nachname);
				
			}
			
			stmt.close();
		} catch (SQLException e) {
			ErrorLogger.log(e);
		}
		
		return absender;
	}
	
	public boolean verlasseTeam(Team team, UserID id) {
		
		boolean erfolg = false;
		
		Student stud = (Student) servicePool.getUser(id);
		
		try {
			boolean einzelAnmelder = isEinzelAnmelder(stud, team);
			if(!pruefeAlleEinladungenBestaetigt(team) || einzelAnmelder){
				
				team.getMitglieder().remove(stud);
				
				if(einzelAnmelder){
					erfolg = entferneTeilnehmer(team.getTeamID(), stud.getMatrNr());
				}else{
					erfolg = sendeVerlasseTeamMeldung(team, stud);
					erfolg &= entferneTeilnehmer(team.getTeamID(), stud.getMatrNr());
				}
				
				
				
				
			}
			
		} catch (SQLException e) {
			ErrorLogger.log(e);
			return false;
			
		}
		
		return erfolg;
		
	}
	


	private boolean isEinzelAnmelder(Student stud, Team team) throws SQLException {
		
		String qry = "select einzel from teammitglied where matrnr = ? and teamid = ?";
		PreparedStatement stmt = connection.prepareStatement(qry);
		stmt.setInt(1, stud.getMatrNr().getMatrNr());
		stmt.setInt(2, Integer.parseInt(team.getTeamID().getId()));
		ResultSet rs = stmt.executeQuery();
		boolean einzel = false;
		while(rs.next()){
			
			einzel = Boolean.parseBoolean(rs.getString("einzel"));
		}
		
		return einzel;
	}

	private boolean sendeVerlasseTeamMeldung(Team team, Student absender) {
		
		String qry = "insert into Meldung values(?, ?, ?, ?, ?, ?, ?, ?)";
		java.sql.Date versandDatum = new java.sql.Date(System.currentTimeMillis());
		for(Student stud: team.getMitglieder()){

			try {
				
				if(mitgliedIstBestaetigt(stud, team)){
					PreparedStatement stmt = connection.prepareStatement(qry);
					stmt.setInt(1, absender.getMatrNr().getMatrNr());
					stmt.setInt(2, stud.getMatrNr().getMatrNr());
					stmt.setString(3, team.getFachkuerzel());
					stmt.setInt(4, Integer.parseInt(team.getTeamID().getId()));
					stmt.setDate(5, versandDatum);
					stmt.setString(6, "null");
					stmt.setDate(7, null);
					stmt.setString(8, MeldungTyp.TEAMAUSTR);
					stmt.executeUpdate();
					stmt.close();
				}
				
			} catch (SQLException e) {
				ErrorLogger.log(e);
				return false;
			}
		}
		
		return true;
	}


	public boolean bestaetigeTeammitgliedNachEinzelanmeldung(Team team, Student user) {
		
		boolean erfolg = false;
		String qry = "insert into teammitglied values(?, ?, ?, ?)";
		try {
			PreparedStatement stmt = connection.prepareStatement(qry);
			stmt.setInt(1, Integer.parseInt(team.getTeamID().getId()));
			stmt.setInt(2, user.getMatrNr().getMatrNr());
			stmt.setString(3, "true");
			stmt.setString(4, "true");
			stmt.executeUpdate();
			stmt.close();
			erfolg = true;
		} catch (SQLException e) {
			ErrorLogger.log(e);
			return false;
		}
		
		return erfolg;
	}

	public boolean zieheEinladungZurueck(TeamID id, MatrikelNr matrNr) {
		
		String qry = "delete Meldung where typ = ? and teamid = ? and empfaenger = ?";
		try {
			PreparedStatement stmt = connection.prepareStatement(qry);
			stmt.setString(1, MeldungTyp.TEAMEINL);
			stmt.setInt(2, Integer.parseInt(id.getId()));
			stmt.setInt(3, matrNr.getMatrNr());
			stmt.executeUpdate();
		} catch (SQLException e) {

			ErrorLogger.log(e);
			return false;
		}
		
		return entferneTeilnehmer(id, matrNr);
	}

	public List<Meldung> getAlleMeldungenBKP() {
		List<Meldung> meldungen = new ArrayList<>();
		String qry = "select * from meldung";
		Meldung currMeldung = null;
		try {
			PreparedStatement stmt = connection.prepareStatement(qry);

			ResultSet rs = stmt.executeQuery();
			
			while(rs.next()){
				
				MatrikelNr matrNrAbs = MatrikelNr.getMatrikelNr(rs.getInt("absender"));
				Student absender = getStudent(matrNrAbs);
				MatrikelNr matrNrStud = MatrikelNr.getMatrikelNr(rs.getInt("empfaenger"));
				Student empfaenger = getStudent(matrNrStud);
				Date versandDatum = new Date(rs.getDate("versanddatum").getTime());
				String typ = rs.getString("typ");
				if(typ.equals(MeldungTyp.TEAMEINL)){
					
					Team team = getTeam(TeamID.getTeamID(rs.getString("teamid")));
					String bestaetigt = rs.getString("bestaetigt");
					Date bestDatumSQL = rs.getDate("bestaetigungsdatum");
					Date bestDatum = null;
					if(bestDatumSQL != null){
						bestDatum = new Date(bestDatumSQL.getTime());
					}
					
					currMeldung = new Teameinladung(absender, empfaenger, versandDatum, team, Boolean.parseBoolean(bestaetigt), bestDatum, MeldungTyp.TEAMEINL);
				
				}else if(typ.equals(MeldungTyp.GRUPPE_VOLL)){
					
					Team team = getTeam(TeamID.getTeamID(rs.getString("teamid")));
					currMeldung = new GruppevollMeldung(absender, empfaenger, versandDatum, team, MeldungTyp.GRUPPE_VOLL);
				}else if(typ.equals(MeldungTyp.TEABLEHN)){
					
					Team team = getTeam(TeamID.getTeamID(rs.getString("teamid")));
					currMeldung = new TEAblehnung(absender, empfaenger, versandDatum, team, MeldungTyp.TEABLEHN);
				}else if(typ.equals(MeldungTyp.TEAMAUSTR)){
					
					Team team = getTeam(TeamID.getTeamID(rs.getString("teamid")));
					currMeldung = new TeamAustrittsmeldung(absender, empfaenger, versandDatum, team, MeldungTyp.TEAMAUSTR);
					
				}
				
				meldungen.add(currMeldung);
			}
			
			stmt.close();
		} catch (SQLException e) {
			
			ErrorLogger.log(e);
			return new ArrayList<>();
		
		}
		
		return meldungen;
	}




}