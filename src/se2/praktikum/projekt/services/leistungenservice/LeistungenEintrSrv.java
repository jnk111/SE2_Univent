package se2.praktikum.projekt.services.leistungenservice;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import se2.praktikum.projekt.dbms.DBConnector;
import se2.praktikum.projekt.models.leistungen.Leistung;
import se2.praktikum.projekt.services.loginservice.ServicePool;
import se2.praktikum.projekt.tools.ErrorLogger;

public class LeistungenEintrSrv {

	private Connection connection;
	private ServicePool servicePool;
	
	public LeistungenEintrSrv(ServicePool servicePool) {
		
		this.setServicePool(servicePool);
		connect();
		
	}
	
	public void connect(){
		connection = DBConnector.getConnection();
	}

	public boolean trageNoteEin(Leistung leistung) {
		
		String qry = "update Bewertung set Note = ?, datumnote = ? where maid = ? and matrnr = ? and fkuerzel = ?";
	
		try {
			
			PreparedStatement stmt = connection.prepareStatement(qry);
			stmt.setString(1, leistung.getNote().getNote());
			stmt.setDate(2, new java.sql.Date(leistung.getDatumNote().getTime()));
			stmt.setInt(3, leistung.getProfessor().getMaID().getId());
			stmt.setInt(4, leistung.getStudent().getMatrNr().getMatrNr());
			stmt.setString(5, leistung.getFach().getFachKuerzel());
			stmt.executeUpdate();
			stmt.close();
			
		} catch (SQLException e) {
			ErrorLogger.log(e);
			return false;
		}

		return true;
	}
	
	public boolean tragePVLEin(Leistung leistung) {
		
		String qry = "update Bewertung set pvl = ?, datumpvl = ? where maid = ? and matrnr = ? and fkuerzel = ?";
		
		try {
			
			PreparedStatement stmt = connection.prepareStatement(qry);
			stmt.setString(1, "" + leistung.isPvl());
			
			if(leistung.getDatumPVL() != null){
				stmt.setDate(2, new java.sql.Date(leistung.getDatumPVL().getTime()));
			}else{
				stmt.setDate(2, null);
			}
			
			stmt.setInt(3, leistung.getProfessor().getMaID().getId());
			stmt.setInt(4, leistung.getStudent().getMatrNr().getMatrNr());
			stmt.setString(5, leistung.getFach().getFachKuerzel());
			stmt.executeUpdate();
			stmt.close();
			
		} catch (SQLException e) {
			ErrorLogger.log(e);
			return false;
		}

		return true;
	}

	public ServicePool getServicePool() {
		return servicePool;
	}

	public void setServicePool(ServicePool servicePool) {
		this.servicePool = servicePool;
	}
	
	

	

}
