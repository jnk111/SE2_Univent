package se2.praktikum.projekt.services.leistungenservice;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import se2.praktikum.projekt.dbms.DBConnector;
import se2.praktikum.projekt.models.leistungen.Leistung;
import se2.praktikum.projekt.models.leistungen.Note;
import se2.praktikum.projekt.models.person.Professor;
import se2.praktikum.projekt.models.person.Student;
import se2.praktikum.projekt.models.person.fachwerte.MAID;
import se2.praktikum.projekt.models.person.fachwerte.MatrikelNr;
import se2.praktikum.projekt.models.veranstaltung.Fach;
import se2.praktikum.projekt.services.loginservice.ServicePool;
import se2.praktikum.projekt.tools.ErrorLogger;

public class LeistungenAnzSrv {

	private Connection connection;
	private Map<String, Boolean> pvlMapping;
	private ServicePool servicePool;

	/**
	 * Initialisiert einen neuen LeistungenAnzSrv
	 * 
	 * @param servicePool
	 */
	public LeistungenAnzSrv(ServicePool servicePool) {

		this.servicePool = servicePool;
		this.pvlMapping = new HashMap<>();
		this.pvlMapping.put("erhalten", true);
		this.pvlMapping.put("nicht erhalten", false);
		connect();
		

	}
	
	public void connect(){
		connection = DBConnector.getConnection();
	}

	/**
	 * 
	 * Holt alle Leistungen für die übergebenen Parameter aus der DB
	 * 
	 * @param fachkuerzel
	 *            : Das Fachkuerzel (primary key)
	 * @return Liste Leistungen
	 */
	public List<Leistung> getLeistungen(String fachkuerzel) {

		List<Leistung> leistungen = new ArrayList<>();
		String qry = "select * from bewertung where fkuerzel = '"+fachkuerzel+"'";
		
		try {
			
			
			Statement statement = connection.createStatement();
			ResultSet rs = statement.executeQuery(qry);
			
			while (rs.next()) {
				
				MatrikelNr matrnr = MatrikelNr.getMatrikelNr(rs.getInt("matrnr"));
				MAID maid = MAID.getMAID(rs.getInt("MAID"));
				Student stud = getStudent(matrnr);
				java.sql.Date datumPVLSQL = rs.getDate("datumpvl");
				java.sql.Date datumNoteSQL = rs.getDate("datumnote");
				Date datumpvl = null;
				Date datumnote = null;
				if(datumPVLSQL != null){
					datumpvl = new Date(rs.getDate("datumpvl").getTime());
				}
				
				if(datumNoteSQL != null){
					datumnote = new Date(rs.getDate("datumnote").getTime());
				}
				
				Professor prof = getProfessor(maid);
				boolean pvl = Boolean.parseBoolean(rs.getString("pvl"));
				Note note = new Note(rs.getString("note"));
				Fach fach = new Fach(fachkuerzel);
				Leistung leistung = new Leistung(fach, stud, prof, datumpvl, datumnote, pvl, note);
				leistungen.add(leistung);
			}
			
			statement.close();
		} catch (SQLException e) {
			
			ErrorLogger.log(e);
		}
		return leistungen;
		
	}
 
	private Professor getProfessor(MAID maid) throws SQLException {
		
		String qry = "select vorname, nachname from professor where maid = "+maid.getId();
		
		Statement stmt = connection.createStatement();
		ResultSet rs = stmt.executeQuery(qry);
		Professor prof = null;
		
		while(rs.next()){
			
			prof = new Professor(maid, rs.getString("vorname"), rs.getString("nachname"));
			
		}
		
		stmt.close();
		return prof;
	}

	private Student getStudent(MatrikelNr matrnr) throws SQLException {
		
		String qry = "select vorname, nachname from Student where matrNr = "+matrnr.getMatrNr();
		
		Statement stmt = connection.createStatement();
		ResultSet rs = stmt.executeQuery(qry);
		Student stud = null;
		
		while(rs.next()){
			
			stud = new Student(matrnr, rs.getString("vorname"), rs.getString("nachname"));
			
		}
		
		stmt.close();
		
		return stud;
	}


	/**
	 * Ermittelt alle Fachkuerzel für die übergebenen Parameter
	 * 
	 * @param fachbereich
	 * @param typ
	 * @param semester
	 * @return
	 */
	public List<String> getFachkuerzel(String fachbereich, String typ,
			int semester) {

		connect(); // Temporär		
		List<String> fachkuerzel = new ArrayList<String>();
		
		Statement statement = null;
		String qry = "select FKuerzel from Veranstaltung where FBKuerzel = '" + fachbereich + "' AND Typ = '" + typ + "' AND Semester = " + semester;
		System.out.println(qry);
			
		try {
			statement = connection.createStatement();
			ResultSet rs = statement.executeQuery(qry);
			
			while (rs.next()) {
				String fk = rs.getString("Fkuerzel");
				System.out.println(fk);
				fachkuerzel.add(fk);
			}
			
			statement.close();
		} catch (SQLException e) {
			ErrorLogger.log(e);
		} 
	

		// Fachkuerzel aus der Liste ermitteln
		return fachkuerzel;
	}

	/**
	 * Ermittelt alle Gruppennummern zu den übergebenen Parametern
	 * 
	 * @param typ
	 * @param semester
	 * @return
	 */
	public List<Integer> getGruppenNummern(String fachkuerzel) {


		List<Integer> gruppennummern = new ArrayList<>();

		Statement statement = null;
		String qry = "select * from Gruppe where FKuerzel = '" + fachkuerzel
				+ "'";
		System.out.println(qry);
		try {
			statement = connection.createStatement();
			ResultSet rs = statement.executeQuery(qry);
			while (rs.next()) {
				int grpnr = rs.getInt("GRPNR");
				gruppennummern.add(grpnr);
			}
			statement.close();
		} catch (SQLException e) {
			ErrorLogger.log(e);
		} 
		
		return gruppennummern;
	}

	public List<Leistung> getLeistungenStudent(MatrikelNr userID) {
		
		List<Leistung> leistungen = new ArrayList<>();
		Student user = (Student) servicePool.getUser(userID);
		
		String qry = "select * from bewertung where matrnr = ? and datumPVL is not null and datumNote is not null";
		try {
			PreparedStatement stmt = connection.prepareStatement(qry);
			stmt.setInt(1, user.getMatrNr().getMatrNr());
			ResultSet rs = stmt.executeQuery();
			while(rs.next()){
				
				MAID maid = MAID.getMAID(rs.getInt("MAID"));
				Date datumpvl = new Date(rs.getDate("datumpvl").getTime());
				Date datumnote = new Date(rs.getDate("datumnote").getTime());
				Professor prof = getProfessor(maid);
				boolean pvl = Boolean.parseBoolean(rs.getString("pvl"));
				Note note = new Note(rs.getString("note"));
				Fach fach = new Fach(rs.getString("fkuerzel"));
				Leistung leist = new Leistung(fach, user, prof, datumpvl, datumnote, pvl, note);
				leistungen.add(leist);
			}

			
		} catch (SQLException e) {
			ErrorLogger.log(e);
			return null;
		}
		
		return leistungen;
	}


}
