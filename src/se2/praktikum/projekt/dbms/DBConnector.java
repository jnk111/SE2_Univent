package se2.praktikum.projekt.dbms;

import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import org.springframework.core.io.ClassPathResource;

import se2.praktikum.projekt.tools.ErrorLogger;


/**
 * Stellt eine Verbindung zur Oracle-Datenbank her
 * @author Jan	
 *
 */
public class DBConnector {

 	private static String aKennung;	
 	private static String passwort;
 	
	private static Connection connection;
 	private static String url = "jdbc:oracle:thin:@ora14.informatik.haw-hamburg.de:1521:inf14";
 	private static String driver = "oracle.jdbc.driver.OracleDriver";
 	
 	
 	public static Connection getConnection(){
 		return connection;
 	}
 	
    public static Connection connect() 
    {
    	if (connection != null)
    	{
    		return connection;
    	}
        else 
        {
            try 
            {
            	getUserDBData();
            	Class.forName(driver);
            	connection = DriverManager.getConnection(url, aKennung, passwort);
            } 
            catch (ClassNotFoundException e) 
            {
            	e.printStackTrace();
            } 
            catch (SQLException e) 
            {
            	e.printStackTrace();
            }
            return connection;
        }
        
    }

	private static void getUserDBData() {
		
		ClassPathResource resource = new ClassPathResource("database.config");
		
		List<String> databaseInfo = new ArrayList<>();
		Scanner sc;
		try {
			File file = resource.getFile();
			sc = new Scanner(file);
			while(sc.hasNext()){
				
				String [] data = sc.nextLine().split("=");
				databaseInfo.add(data[1]);
			}
		} catch (IOException e1) {
			ErrorLogger.log(e1);
		}
		
		// database[0] = driver, database[1] = url, database[2] = A-Kennung, database[3] = Passwort
		
		driver = databaseInfo.get(0);
		url = databaseInfo.get(1);
		aKennung = databaseInfo.get(2);
		passwort = databaseInfo.get(3);
		
	}
}
