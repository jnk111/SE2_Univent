package se2.praktikum.projekt.dbms;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import se2.praktikum.projekt.models.person.Student;
import se2.praktikum.projekt.models.person.fachwerte.MatrikelNr;

/**
 * Service der für Abfragen/Anlegen und Prüfen von Daten in der Datenbank zuständig ist.
 * @author Jan
 *
 */
public class DBService 
{
	private Connection connection;
	
	public DBService()
	{
		
	}
	
	public void connect(){
		connection = DBConnector.getConnection();
	}
	
	public List<Student> getAllStudents()
	{
		initStudents();
		
		List<Student> studenten = new ArrayList<>();
		Statement statement = null;
		String qry = "select * from student";
		
		try 
		{
			statement = connection.createStatement();
			ResultSet rs = statement.executeQuery(qry);
			
			while(rs.next()){
				Student stud = new Student();
				stud.setMatrNr(MatrikelNr.getMatrikelNr(rs.getInt("MATRNR")));
				stud.setVorname(rs.getString("VORNAME"));
				stud.setNachname(rs.getString("NACHNAME"));
				studenten.add(stud);
			}
			statement.close();
		} 
		catch (SQLException e) 
		{
			e.printStackTrace();
		}
		return studenten;
	}

	
	/**
	 * Legt Studentendaten an -> Nur Hilfsmethode für Beispiel.
	 * @throws SQLException 
	 */
	private void initStudents()
	{
		// Tabelle anlegen
		String create = "create table Student"
							+ "(MATRNR integer primary key,"
							+ "VORNAME varchar2(100),"
							+ "NACHNAME varchar2(100)"
							+ ")";
		
		
		// Studenten anlegen
		String student1 = "insert into Student(MATRNR, VORNAME, NACHNAME)"
							+ "VALUES (111111, 'Max', 'Mustermann1')";
		String student2 = "insert into Student(MATRNR, VORNAME, NACHNAME)"
							+ "VALUES (111112, 'Max', 'Mustermann1')";
		String student3 = "insert into Student(MATRNR, VORNAME, NACHNAME)"
							+ "VALUES (111113, 'Max', 'Mustermann1')";
		String student4 = "insert into Student(MATRNR, VORNAME, NACHNAME)"
							+ "VALUES (111114, 'Max', 'Mustermann1')";
		String student5 = "insert into Student(MATRNR, VORNAME, NACHNAME)"
							+ "VALUES (111115, 'Max', 'Mustermann1')";
		String student6 = "insert into Student(MATRNR, VORNAME, NACHNAME)"
							+ "VALUES (111116, 'Max', 'Mustermann1')";
		String student7 = "insert into Student(MATRNR, VORNAME, NACHNAME)"
							+ "VALUES (111117, 'Max', 'Mustermann1')";
		String student8 = "insert into Student(MATRNR, VORNAME, NACHNAME)"
							+ "VALUES (111118, 'Max', 'Mustermann1')";
		String student9 = "insert into Student(MATRNR, VORNAME, NACHNAME)"
							+ "VALUES (111119, 'Max', 'Mustermann1')";
		String student10 = "insert into Student(MATRNR, VORNAME, NACHNAME)"
							+ "VALUES (111110, 'Max', 'Mustermann1')";
		
		// Sql-Queries ausführen
		Statement statement;
		try 
		{
			statement = connection.createStatement();
			statement.executeQuery(create);
			statement.execute(student1);
			statement.execute(student2);
			statement.execute(student3);
			statement.execute(student4);
			statement.execute(student5);
			statement.execute(student6);
			statement.execute(student7);
			statement.execute(student8);
			statement.execute(student9);
			statement.execute(student10);
			statement.close();
			
		} 
		catch (SQLException e) 
		{
			// Falls Studenten schon angelegt, tue hier nichts und mach einfach weiter
		}
		
		
	}
	
	

}
