package se2.praktikum.projekt.services.veranstaltungsservice;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import se2.praktikum.projekt.dbms.DBConnector;
import se2.praktikum.projekt.models.gruppe.Gruppe;
import se2.praktikum.projekt.models.gruppe.Termin;
import se2.praktikum.projekt.services.loginservice.ServicePool;
import se2.praktikum.projekt.tools.ErrorLogger;


// Hier wurde vorher noch garnichts implementiert
public class GruppVerwSrv {
	
	private ServicePool servicePool;
	private Connection connection;

	public GruppVerwSrv(ServicePool servicePool) {
		
		this.setServicePool(servicePool);
		connect();
	}

	private void connect() {
		
		connection = DBConnector.getConnection();
	}

	public boolean erstelleGruppe(Gruppe gr) {
		
		boolean erfolg = false;
		
		String qry = "insert into gruppe values(?, ?, ?, ?, ?)";
		
		try {
			PreparedStatement stmt = connection.prepareStatement(qry);
			stmt.setInt(1, gr.getGrpNr());
			stmt.setString(2, gr.getFachkuerzel());
			stmt.setInt(3, gr.getMaxTeams());
			stmt.setInt(4, gr.getProfessor().getMaID().getId());
			stmt.setInt(5, gr.getAssistent().getMaID().getId());
			
			stmt.executeUpdate();
			stmt.close();
			erfolg = erstelleTermineFuerGruppe(gr);
			
		} catch (SQLException e) {
			ErrorLogger.log(e);
			return false;
		}
		
		
		return erfolg;
		
		
	}

	private boolean erstelleTermineFuerGruppe(Gruppe gr) throws SQLException {
		
		String qry = "insert into termin values(?, ?, ?, ?, ?, ?, ?)";
		int tIndex = 1;
		
		for(Termin t: gr.getTermine()){
			
			String tId = gr.getFachkuerzel() + "G" + gr.getGrpNr() + "T" + tIndex;
			
			PreparedStatement stmt = connection.prepareStatement(qry);
			stmt.setString(1, tId);
			stmt.setString(2, gr.getFachkuerzel());
			stmt.setInt(3, gr.getGrpNr());
			stmt.setDate(4, new java.sql.Date(t.getDatum().getTime()));
			stmt.setString(5, t.getStart().toString());
			stmt.setString(6, t.getEnde().toString());
			stmt.setString(7, t.getRaum());
			stmt.executeUpdate();
			tIndex++;
			stmt.close();
		}

		return true;
	}

	public boolean bearbeiteGruppe(Gruppe gr) {
		
		boolean erfolg = false;
		String qry = "update Gruppe set profmaid = ?, assismaid = ?, maxTeams = ? where fkuerzel = ? and grpNr = ?";
		
		try {
			PreparedStatement stmt = connection.prepareStatement(qry);
			stmt.setInt(1, gr.getProfessor().getMaID().getId());
			stmt.setInt(2, gr.getAssistent().getMaID().getId());
			stmt.setInt(3, gr.getMaxTeams());
			stmt.setString(4, gr.getFachkuerzel());
			stmt.setInt(5, gr.getGrpNr());
			
			stmt.executeUpdate();
			stmt.close();
			erfolg = updateTermine(gr);
			
		} catch (SQLException e) {
			
			ErrorLogger.log(e);
			return false;
		}
		
		return erfolg;
	}

	private boolean updateTermine(Gruppe gr) throws SQLException {
		
		boolean erfolg = entferneTermine(gr);
		
		if(erfolg){
			
			erfolg = erstelleTermineFuerGruppe(gr);
		}

		return erfolg;
	}

	private boolean entferneTermine(Gruppe gr) throws SQLException {

		
		String qry = "delete Termin where grpNr = ? and fkuerzel = ?";
		PreparedStatement stmt = connection.prepareStatement(qry);
		stmt.setInt(1, gr.getGrpNr());
		stmt.setString(2, gr.getFachkuerzel());
		stmt.executeUpdate();
		stmt.close();
		
		
		return true;
		
		
	}

	public boolean loescheGruppe(Gruppe gr) {
		
		String qry = "delete Gruppe where fkuerzel = ? and grpNr = ?";
		boolean erfolg = false;
		
		try {
			PreparedStatement stmt = connection.prepareStatement(qry);
			stmt.setString(1, gr.getFachkuerzel());
			stmt.setInt(2, gr.getGrpNr());
			stmt.executeUpdate();
			stmt.close();
			erfolg = true;
				
		}catch (SQLException e) {
			ErrorLogger.log(e);
			return false;
		}

		
		return erfolg;
	}

	public ServicePool getServicePool() {
		return servicePool;
	}

	public void setServicePool(ServicePool servicePool) {
		this.servicePool = servicePool;
	}


}
