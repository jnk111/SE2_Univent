package se2.praktikum.projekt.services.newsservice;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;

import se2.praktikum.projekt.datenimexport.Folders;
import se2.praktikum.projekt.dbms.DBConnector;
import se2.praktikum.projekt.services.loginservice.ServicePool;
import se2.praktikum.projekt.tools.ErrorLogger;

public class NewsSrv {
	
	private ServicePool servicePool;
	private Connection connection;
	
	public NewsSrv(ServicePool pool){
		
		this.setServicePool(pool);
		connect();
	}

	private void connect() {
		setConnection(DBConnector.getConnection());
	}
	
	
	

	public ServicePool getServicePool() {
		return servicePool;
	}

	public void setServicePool(ServicePool servicePool) {
		this.servicePool = servicePool;
	}

	public Connection getConnection() {
		return connection;
	}

	public void setConnection(Connection connection) {
		this.connection = connection;
	}

	public boolean speichere(String msg, String type) {
		
		File file = new File(Folders.NEWS_FOLDER + "/" + type + ".nws");
		
		if(file.exists()){
			file.delete();
		}
		
		try {
			FileWriter writer = new FileWriter(file, true);
			writer.write(msg);
			writer.flush();
			writer.close();
			
			
		} catch (IOException e) {
			ErrorLogger.log(e);
			return false;
		}
		
		return true;
	}

	public List<String> getNews() {
		
		List<String> news = new ArrayList<>();
		File header = new File(Folders.NEWS_FOLDER + "/HEADER" + ".nws");
		File body = new File(Folders.NEWS_FOLDER + "/BODY" + ".nws");
		String head = "";
		String bod = "";
		
		if(header.exists() && body.exists()){
			
			try {
				
				// Read Head
				FileReader reader = new FileReader(header);
				BufferedReader bfreader = new BufferedReader(reader);
				String line = null;
				
				while((line = bfreader.readLine()) != null){
					
					head += line;
					
				}
				
				reader.close();
				bfreader.close();
				
				// Read Body
				reader = new FileReader(body);
				bfreader = new BufferedReader(reader);
				line = null;
				
				while((line = bfreader.readLine()) != null){
					
					bod += line;
					
				}
				
				reader.close();
				bfreader.close();
				
				news.add(head);
				news.add(bod);
				
				
				
			} catch (FileNotFoundException e) {
				ErrorLogger.log(e);
				
			} catch (IOException e) {
				ErrorLogger.log(e);
			}
			
			
			return news;
			
			
		}

		return null;
	}

}
