package se2.praktikum.projekt.services.veranstaltungsservice;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import se2.praktikum.projekt.dbms.DBConnector;
import se2.praktikum.projekt.models.gruppe.Gruppe;
import se2.praktikum.projekt.models.gruppe.Termin;
import se2.praktikum.projekt.models.gruppe.fachwerte.TerminID;
import se2.praktikum.projekt.models.gruppe.fachwerte.Uhrzeit;
import se2.praktikum.projekt.models.person.Assistent;
import se2.praktikum.projekt.models.person.EMail;
import se2.praktikum.projekt.models.person.IAngestellter;
import se2.praktikum.projekt.models.person.Professor;
import se2.praktikum.projekt.models.person.Student;
import se2.praktikum.projekt.models.person.fachwerte.Adresse;
import se2.praktikum.projekt.models.person.fachwerte.MAID;
import se2.praktikum.projekt.models.person.fachwerte.MatrikelNr;
import se2.praktikum.projekt.models.person.fachwerte.PLZ;
import se2.praktikum.projekt.models.team.Team;
import se2.praktikum.projekt.models.team.fachwerte.TeamID;
import se2.praktikum.projekt.models.veranstaltung.Fach;
import se2.praktikum.projekt.models.veranstaltung.Praktikum;
import se2.praktikum.projekt.models.veranstaltung.Projekt;
import se2.praktikum.projekt.models.veranstaltung.Veranstaltung;
import se2.praktikum.projekt.models.veranstaltung.WP;
import se2.praktikum.projekt.services.loginservice.ServicePool;
import se2.praktikum.projekt.tools.ErrorLogger;

public class AnzeigerSrv 
{
	//alle SQL-Namen sollten hier als Klassenvariablen auftauchen.
	
	private Connection connection;
	private ServicePool servicePool;
	public AnzeigerSrv(ServicePool sp)
	{
		servicePool = sp;
		connect();
	}
	
	public AnzeigerSrv()
	{
		connect();
	}

	
	public void connect(){
		connection = DBConnector.getConnection();
	}
	public List<Veranstaltung> getAllPraktika(int semester, String fachbereich)
	{
		
		
		List<Veranstaltung> pos = new ArrayList<Veranstaltung>();
		Statement statement = null;
		String qry = "select * from Veranstaltung where Semester = " + semester
					+ " AND FBKuerzel = '" + fachbereich + "' And Typ = 'Praktikum'";
		
		try 
		{
			statement = connection.createStatement();
			ResultSet rs = statement.executeQuery(qry);
			
			while(rs.next()){
				Veranstaltung vrst = new Praktikum();
				Fach fach = new Fach();
				String fBKuerzel = (rs.getString("FBKuerzel"));
				fach.setFachKuerzel(rs.getString("FKuerzel"));
				fach.setFachBezeichnung(rs.getString("FBezeichnung"));
				fach.setfBKuerzel(fBKuerzel);
				fach.setSemester((rs.getInt("Semester")));
				vrst.setFach(fach);
				MAID maid = MAID.getMAID(rs.getInt("MAID"));				
				vrst.setProfessor(getProfessor(maid));
				vrst.setMinTeilnTeam(rs.getInt("minTeilnTeam"));
				vrst.setMaxTeilnTeam(rs.getInt("maxTeilnTeam"));
				vrst.setAnzGr(getGruppenanzahl(rs.getString("FKuerzel")));
				vrst.setAnzTm(getTeamanzahl(rs.getString("FKuerzel"))); 
				vrst.setSemester(fach.getSemester());
				System.out.println(rs.getString("FKuerzel"));
				System.out.println(getTeamanzahl(rs.getString("FKuerzel")));
				fach.setFachbereich(getFachbereich(fBKuerzel));
				pos.add(vrst);			
				
				// Semester hat gefehlt und anzTm sind nur die, die schon als angemeldet gelten
				// Meine Tests schlugen fehl (mit viel weniger Code!!!!!)
			
			}
			statement.close();
		} 
		catch (SQLException e) 
		{

			ErrorLogger.log(e);
			return new ArrayList<>();
		}

		return pos;
		
	}

	

	/**
	 * Gibt eine Liste an WPs für ein gewähltes Semester und einen Fachbereich aus 
	 * 
	 * @param semester
	 * @param fachbereich
	 * @return ArrayList<Veranstaltung> : Wird die Liste leer zurückgegeben, war der Aufruf nicht erfolgreich
	 */
	public List<Veranstaltung> getAllWP(int semester, String fachbereich)
	{
		
		List<Veranstaltung> pos = new ArrayList<Veranstaltung>();
		Statement statement = null;
		String qry = "select * from Veranstaltung where Semester = " + semester
					+ " AND FBKuerzel = '" + fachbereich + "' And Typ = 'WPP'";
		
		try 
		{
			statement = connection.createStatement();
			ResultSet rs = statement.executeQuery(qry);
			
			while(rs.next()){
				Veranstaltung vrst = new WP();
				Fach fach = new Fach();
				String fBKuerzel = (rs.getString("FBKuerzel"));
				fach.setFachKuerzel(rs.getString("FKuerzel"));
				fach.setFachBezeichnung(rs.getString("FBezeichnung"));
				fach.setfBKuerzel(fBKuerzel);
				fach.setSemester((rs.getInt("Semester")));
				vrst.setFach(fach);
				MAID maid = MAID.getMAID(rs.getInt("MAID"));				
				vrst.setProfessor(getProfessor(maid));
				vrst.setMinTeilnTeam(rs.getInt("minTeilnTeam"));
				vrst.setMaxTeilnTeam(rs.getInt("maxTeilnTeam"));
				vrst.setAnzGr(getGruppenanzahl(rs.getString("FKuerzel")));
				vrst.setAnzTm(getTeamanzahl(rs.getString("FKuerzel")));
				vrst.setSemester(fach.getSemester());
				fach.setFachbereich(getFachbereich(fBKuerzel));
				pos.add(vrst);	
			
			}
			statement.close();
		} 
		catch (SQLException e) 
		{
			ErrorLogger.log(e);
			return new ArrayList<>();
		}

		return pos;
		
		
	}
	/**
	 * Gibt eine Liste an POs für ein gewähltes Semester und einen Fachbereich aus 
	 * 
	 * @param semester
	 * @param fachbereich
	 * @return ArrayList<Veranstaltung> : Wird die Liste leer zurückgegeben, war der Aufruf nicht erfolgreich
	 */
	public List<Veranstaltung> getAllPO(int semester, String fachbereich)
	{
		
		
		
		List<Veranstaltung> pos = new ArrayList<Veranstaltung>();
		Statement statement = null;
		String qry = "select * from Veranstaltung where Semester = " + semester
					+ " AND FBKuerzel = '" + fachbereich + "' And Typ = 'PO'";
		
		try 
		{
			statement = connection.createStatement();
			ResultSet rs = statement.executeQuery(qry);
			
			while(rs.next()){
				Veranstaltung vrst = new Projekt();
				Fach fach = new Fach();
				String fBKuerzel = (rs.getString("FBKuerzel"));
				fach.setFachKuerzel(rs.getString("FKuerzel"));
				fach.setFachBezeichnung(rs.getString("FBezeichnung"));
				fach.setfBKuerzel(fBKuerzel);
				fach.setSemester((rs.getInt("Semester")));
				vrst.setFach(fach);
				MAID maid = MAID.getMAID(rs.getInt("MAID"));				
				vrst.setProfessor(getProfessor(maid));
				vrst.setMinTeilnTeam(rs.getInt("minTeilnTeam"));
				vrst.setMaxTeilnTeam(rs.getInt("maxTeilnTeam"));
				vrst.setAnzGr(getGruppenanzahl(rs.getString("FKuerzel")));
				vrst.setAnzTm(getTeamanzahl(rs.getString("FKuerzel")));
				vrst.setSemester(fach.getSemester());
				fach.setFachbereich(getFachbereich(fBKuerzel));
				pos.add(vrst);	
			
			}
			statement.close();
		} 
		catch (SQLException e) 
		{
			ErrorLogger.log(e);
			return new ArrayList<>();
		}
		
		return pos;
	
		
	}
/**
 * 
 * Gibt eine Liste an Gruppen für eine bestimmte Veranstaltung
 * Schreibt bei einer SQL-Exception ein Log-Eintrag
 * Werfen die aufgerufenen Methoden eine Exception, werden diese mit dem Namen der Methode
 * im Srv gecatcht und mit username als Anhang wird ein DB-Enintrag für die Fehlermeldung geschrieben
 * Das Frontend wird bei 
 * @param veranstaltung
 * @return ArrayList<Gruppe>
 */
	public List<Gruppe> getAllGruppen(Veranstaltung veranstaltung)
	{
		
		
		List<Gruppe> gruppen = new ArrayList<Gruppe>();
		Statement statement = null;
		String qry = null;
		String fachKuerzel = veranstaltung.getFach().getFachKuerzel();
		
		 
			qry = "select * from Gruppe where FKuerzel = '" + fachKuerzel + "'";
		
		
		try 
		{
			statement = connection.createStatement();
			ResultSet rs = statement.executeQuery(qry);
			int i =0;
			
			while(rs.next()){
				int gruppenNr = rs.getInt("grpNr");
				Gruppe grp= new Gruppe();
				grp.setGrpNr(gruppenNr);
				grp.setFachkuerzel(fachKuerzel);
				grp.setMaxTeams(rs.getInt("maxTeams"));
				MAID maid = MAID.getMAID((rs.getInt("ProfMAID")));
				grp.setProfessor(getProfessor(maid));
				maid = MAID.getMAID(rs.getInt("AssisMAID"));
				grp.setAssistent(getAssistent(maid));
				grp.setTermine(getTermine(gruppenNr, fachKuerzel));
				grp.setAnzTeams(getTeamanzahl(grp));
				gruppen.add(grp);
				System.out.println(++i);
			}				
				
			
			statement.close();
		} 
		catch (SQLException e) 
		{
			ErrorLogger.log(e);
			return new ArrayList<>();
			
		}

		return gruppen;
		
	}
	//sollte eig Teamliste zurückgeben und Fachkürzel für select-Befehl auch Kürzel
	public List<Team> getAllTeilnehmer(Gruppe gruppe)
	{
		
		
		List<Team> teams = new ArrayList<>();
		Statement statement = null;
		int gruppenNr = gruppe.getGrpNr();
		String qry = "select * from Team where grpNr = " + gruppenNr + "and FKuerzel = '" + gruppe.getFachkuerzel() + "'";
		
		try 
		{
			statement = connection.createStatement();
			ResultSet rs = statement.executeQuery(qry);
			
			while(rs.next()){
				Team team = new Team();
				TeamID teamID =TeamID.getTeamID(rs.getString("TeamID"));
				team.setGrpNr(gruppenNr);
				team.setFachkuerzel(gruppe.getFachkuerzel());
				team.setMinTeiln(rs.getInt("minMitglieder"));
				team.setMaxTeiln(rs.getInt("maxMitglieder"));
				team.setTeamID(teamID);
				team.setMitglieder(getMitglieder(teamID));
				teams.add(team);
			}
			statement.close();
		} 
		catch (SQLException e) 
		{
			ErrorLogger.log(e);
			return new ArrayList<>();
			
		}

		return teams;
	}

public List<IAngestellter> getAlleProfessoren(int semester, String fachbereich)
	{
		List<IAngestellter> professoren = new ArrayList<>();
		
		Statement statement = null;
		String qry = "select * from Professor where MAID in (select MAID from Veranstaltung where semester = "+semester+""
				+ "AND FBKuerzel = '"+fachbereich+"')";
		
		try 
		{
			statement = connection.createStatement();
			ResultSet rs = statement.executeQuery(qry);
			
			while(rs.next()){
				
				MAID maid = MAID.getMAID(rs.getInt("maid"));
				String vorname = rs.getString("vorname");
				String nachname = rs.getString("nachname");
				Professor prof = new Professor(maid, vorname, nachname);
				professoren.add(prof);

			}
			statement.close();
		} 
		catch (SQLException e) 
		{
			ErrorLogger.log(e);
			return new ArrayList<>();
		}
		
		return professoren;
	}

public List<IAngestellter> getAlleAssistenten()
{
	List<IAngestellter> assistenten = new ArrayList<IAngestellter>();
	
	
	Statement statement = null;
	String qry = "select * from Assistent";
	
	try 
	{
		statement = connection.createStatement();
		ResultSet rs = statement.executeQuery(qry);
		
		while(rs.next()){
			
			MAID maid = MAID.getMAID(rs.getInt("maid"));
			String vorname = rs.getString("vorname");
			String nachname = rs.getString("nachname");
			Assistent ass = new Assistent(maid, vorname, nachname);
			assistenten.add(ass);

		}
		statement.close();
	} 
	catch (SQLException e) 
	{
		ErrorLogger.log(e);
		return new ArrayList<>();
	}
	
	return assistenten;
}
	
	public Professor getProfessor(MAID maid)
	{
		Professor professor= new Professor();
		Statement statement = null;
		String qry = "select * from Professor where MAID = " + maid.getId();
		
		try 
		{
			statement = connection.createStatement();
			ResultSet rs = statement.executeQuery(qry);
			
			while(rs.next()){
				
				PLZ plz = PLZ.getPostLeitzahl((rs.getString("plz")));
				Adresse adresse = Adresse.getAdresse(rs.getString("strasse"), rs.getInt("hausNr"), plz,rs.getString("ort"));
				EMail email = new EMail(rs.getString("email"));
				String vorname = rs.getString("vorname");
				String nachname = rs.getString("nachname");
				professor.setAdresse(adresse);
				professor.setBenutzername(rs.getString("username"));
				professor.setDepartment(rs.getString("DKuerzel"));
				professor.setEMail(email);
				professor.setGebDatum(rs.getDate("gebDatum"));
				professor.setGebOrt(rs.getString("gebOrt"));
				professor.setMaID(maid);
				System.out.println(maid.getId());
				professor.setNachname(nachname);
				professor.setVorname(vorname);
				professor.setVollerName(vorname + " " + nachname);
			
			}
			statement.close();
		} 
		catch (SQLException e) 
		{
			ErrorLogger.log(e);
			return null;
			
		}
		
		return professor;
	}
	
	public Assistent getAssistent(MAID maid)
	{
		Assistent assistent= new Assistent();
		Statement statement = null;
		String qry = "select * from Assistent where MAID = " + maid.getId();
		
		try 
		{
			statement = connection.createStatement();
			ResultSet rs = statement.executeQuery(qry);
			
			while(rs.next()){
				
				PLZ plz = PLZ.getPostLeitzahl((rs.getString("plz")));
				Adresse adresse = Adresse.getAdresse(rs.getString("strasse"), rs.getInt("hausNr"), plz, rs.getString("ort"));
				EMail email = new EMail(rs.getString("email"));
				String vorname = rs.getString("vorname");
				String nachname = rs.getString("nachname");
				assistent.setAdresse(adresse);
				assistent.setBenutzername(rs.getString("username"));
				assistent.setDepartment(rs.getString("DKuerzel"));
				assistent.setEMail(email);
				assistent.setGebDatum(rs.getDate("gebDatum"));
				assistent.setGebOrt(rs.getString("gebOrt"));
				assistent.setMaID(maid);
				assistent.setNachname(nachname);
				assistent.setVorname(vorname);
				assistent.setVollerName(vorname + " " + nachname);
			
			}
			statement.close();
		} 
		catch (SQLException e) 
		{
			ErrorLogger.log(e);
			return null;
		}
		
		return assistent;
	}
	
	public List<Termin> getTermine(int gruppenNr, String fachKuerzel)
	{
		List<Termin> termine = new ArrayList<Termin>();
		
		Statement statement = null;
		String qry = "select * from Termin where grpNr = " + gruppenNr + " and FKuerzel = '"+fachKuerzel+"'";
		
		try 
		{
			statement = connection.createStatement();
			ResultSet rs = statement.executeQuery(qry);
			
			while(rs.next()){
				
				Termin termin = new Termin();
				Uhrzeit uhrzeitAnfang = Uhrzeit.getUhrzeit(rs.getString("Ustart"));
				termin.setDatum(rs.getDate("Datum"));
				termin.setStart(uhrzeitAnfang);
				TerminID terminID = TerminID.getTerminID(rs.getString("TerminID"));
				termin.setTerminID(terminID);
				Uhrzeit uhrzeitEnde = Uhrzeit.getUhrzeit(rs.getString("Uende"));
				termin.setEnde(uhrzeitEnde);
				termin.setRaum(rs.getString("raum"));
				termine.add(termin);
			}
			statement.close();
		} 
		catch (SQLException e) 
		{
			ErrorLogger.log(e);
			return null;
			
		}
		
		return termine;
	}
	
	public List<Student> getMitglieder(TeamID teamID)
	{
		
		Statement statement = null;
		List<Student> mitglieder = new ArrayList<Student>();
		String id = teamID.getId();
		String qry = "select matrNr, vorname, nachname from Student where matrNr in (select matrNr from Teammitglied where TeamID = '"+id+"')";
		
		
		try 
		{
			statement = connection.createStatement();
			ResultSet rs = statement.executeQuery(qry);
			
			while(rs.next()){
				MatrikelNr matrNr = MatrikelNr.getMatrikelNr(rs.getInt("matrNr"));
				String vorname = rs.getString("vorname");
				String nachname = rs.getString("nachname");
				Student student = new Student(matrNr, vorname, nachname);
				mitglieder.add(student);
			}
			statement.close();
		} 
		catch (SQLException e) 
		{
			ErrorLogger.log(e);
			return new ArrayList<>();
			
		}
		
		return mitglieder;
		
	
	}
	
	public Student getStudent(String username)
	{
		Student student= new Student();
		Statement statement = null;
		String qry = "select * from Student where username = '" + username + "'";
		
		try 
		{
			statement = connection.createStatement();
			ResultSet rs = statement.executeQuery(qry);
			
			while(rs.next()){
				
				PLZ plz = PLZ.getPostLeitzahl((rs.getString("plz")));
				Adresse adresse = Adresse.getAdresse(rs.getString("strasse"), rs.getInt("hausNr"), plz, rs.getString("ort"));
				EMail email = new EMail(rs.getString("email"));
				String vorname = rs.getString("vorname");
				String nachname = rs.getString("nachname");
				student.setAdresse(adresse);
				student.setBenutzername(rs.getString("username"));
				student.setDepartment(rs.getString("DKuerzel"));
				student.setEMail(email);
				student.setFachbereich(rs.getString("FBKuerzel"));
				student.setGebDatum(rs.getDate("gebDatum"));
				student.setGebOrt(rs.getString("gebOrt"));
				student.setBenutzername(username);
				student.setNachname(nachname);
				student.setVorname(vorname);
				student.setVollerName(vorname + " " + nachname);
				MatrikelNr matrikelNr =MatrikelNr.getMatrikelNr((rs.getInt("matrNr")));
				student.setMatrNr(matrikelNr);
				
			
			}
			statement.close();
		} 
		catch (SQLException e) 
		{
			ErrorLogger.log(e);
			return null;
		}
		
		return student;
	}
	
	public int getGruppenanzahl(String fKuerzel)
	{
		Statement statement = null;
		String qry = "select Count(grpNr) as Anzahl from Gruppe where FKuerzel = '"+ fKuerzel +"'";
		int result = -1;
		try 
		{
			statement = connection.createStatement();
			ResultSet rs = statement.executeQuery(qry);
			
			while(rs.next())
			{
				
				result = rs.getInt("Anzahl");
			
			}
			statement.close();
		} 
		catch (SQLException e) 
		{
			ErrorLogger.log(e);
			return -1;
			
		}

		return result;
		
	}
	
	public int getTeamanzahl(String fKuerzel, int grpNr)
	{
		Statement statement = null;
		String qry = "select Count(TeamID) as Anzahl from Team where FKuerzel = '"+ fKuerzel +"' and grpNr =" + grpNr;
		int result = -1;
		try 
		{
			statement = connection.createStatement();
			ResultSet rs = statement.executeQuery(qry);
			
			while(rs.next())
			{
				
				result = rs.getInt("Anzahl");
			
			}
			statement.close();
		} 
		catch (SQLException e) 
		{
			ErrorLogger.log(e);
			return -1;
		}
		return result;
	}
	
	public int getTeamanzahl(String fKuerzel)
	{
		Statement statement = null;
		String qry = "select Count(TeamID) as Anzahl from Team where FKuerzel = '"
							+ fKuerzel +"' and GrpNr is not null";
		int result = -1;
		try 
		{
			statement = connection.createStatement();
			ResultSet rs = statement.executeQuery(qry);
			
			while(rs.next())
			{
				
				result = rs.getInt("Anzahl");
			
			}
			statement.close();
		} 
		catch (SQLException e) 
		{
			ErrorLogger.log(e);
			return -1;
		}
		return result;
	}
	/*
	 * TO DO!!
	 */
	public int getTeamanzahl(Gruppe gruppe)
	{
		Statement statement = null;
		String qry = "select Count(TeamID) as Anzahl from Team where FKuerzel = '"+ gruppe.getFachkuerzel() +"' and grpNr = "+gruppe.getGrpNr() + " and GrpNr is not null";
		int result = -1;
		try 
		{
			statement = connection.createStatement();
			ResultSet rs = statement.executeQuery(qry);
			
			while(rs.next())
			{
				
				result = rs.getInt("Anzahl");
			
			}
			statement.close();
		} 
		catch (SQLException e) 
		{
			ErrorLogger.log(e);
			return -1;
		}
		return result;
	}
	
	public ServicePool getPool()
	{
		return servicePool;
	}
	
	private String getFachbereich(String fBKuerzel)
			{
				String result = "";
				Statement statement = null;
				String qry = "select FBBezeichnung from Fachbereich where FBKuerzel = '"+fBKuerzel+"'";
				try 
				{
					statement = connection.createStatement();
					ResultSet rs = statement.executeQuery(qry);
					
					while(rs.next())
					{
						
						result = rs.getString("FBBezeichnung");
					
					}
					statement.close();
				} 
				catch (SQLException e) 
				{
					ErrorLogger.log(e);
					return null;
				}
				return result;
			}

	public List<Veranstaltung> holeVeranstaltungen(List<String> args) {
		
		List<Veranstaltung> veranstaltungen = new ArrayList<>();
		for(String fk: args){
			
			Veranstaltung va = getVeranstaltung(fk);
			veranstaltungen.add(va);
		}
		
		return veranstaltungen;
	}

	private Veranstaltung getVeranstaltung(String fk) {
		
		String qry = "select minTeilnTeam, maxTeilnTeam, typ from Veranstaltung where fkuerzel = ?";
		Veranstaltung va = null;
		try {
			PreparedStatement stmt = connection.prepareStatement(qry);
			stmt.setString(1, fk);
			ResultSet rs = stmt.executeQuery();
			
			while(rs.next()){
				String typ = rs.getString("typ");
				int minTeiln = rs.getInt("minTeilnTeam");
				int maxTeiln = rs.getInt("maxTeilnTeam");
				if(typ.equals("WPP")){
					
					va = new WP();
					va.setFach(new Fach(fk));
					va.setMinTeilnTeam(minTeiln);
					va.setMaxTeilnTeam(maxTeiln);
				}else if(typ.equals("PO")){
					va = new Projekt();
					va.setFach(new Fach(fk));
					va.setMinTeilnTeam(minTeiln);
					va.setMaxTeilnTeam(maxTeiln);
				}else{
					va = new Praktikum();
					va.setFach(new Fach(fk));
					va.setMinTeilnTeam(minTeiln);
					va.setMaxTeilnTeam(maxTeiln);
				}
				
			}
			
			stmt.close();
		} catch (SQLException e) {
			ErrorLogger.log(e);
			return null;
		}
		return va;
	}
	
	
			
			
}