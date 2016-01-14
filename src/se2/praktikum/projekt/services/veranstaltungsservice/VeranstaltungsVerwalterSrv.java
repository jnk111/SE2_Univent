package se2.praktikum.projekt.services.veranstaltungsservice;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import se2.praktikum.projekt.datenimexport.backuptables.DBAnmeldetermin;
import se2.praktikum.projekt.dbms.DBConnector;
import se2.praktikum.projekt.models.gruppe.fachwerte.Uhrzeit;
import se2.praktikum.projekt.models.veranstaltung.*;
import se2.praktikum.projekt.services.loginservice.ServicePool;

public class VeranstaltungsVerwalterSrv {
	
	private ServicePool servicePool;
	private Connection connection;
	
	public VeranstaltungsVerwalterSrv(ServicePool servicePool) {
		
		this.setServicePool(servicePool);
		connect();
		
	}
	public void connect()
	{
		connection = DBConnector.getConnection();
	}
	

	public boolean bearbeiteVrst(Veranstaltung va) 
	{
		String qry = "update Veranstaltung set maid = ?, minTeilnTeam = ?, maxTeilnTeam = ? where Fkuerzel = ?";
		PreparedStatement stmt = null;
		
		try {
			stmt = connection.prepareStatement(qry);
			stmt.setInt(1, va.getProfessor().getMaID().getId());
			stmt.setInt(2, va.getMinTeilnTeam());
			stmt.setInt(3, va.getMaxTeilnTeam());
			stmt.setString(4, va.getFach().getFachKuerzel());
			stmt.executeUpdate();
			stmt.close();
			
		} catch (SQLException e) {
			
			try {
				stmt.close();
				return false;
				
			} catch (SQLException e1) {
				
				return false;
			}
		}
		return true;
	}

	public boolean erstelleVrst(Veranstaltung va) {
		
		String typ = null;
		boolean erfolg = false;
		
		if(va instanceof Praktikum){
			typ = "Praktikum";
		}else if(va instanceof WP){
			typ = "WPP";
		}else if(va instanceof Projekt){
			typ = "PO";
		}
		
		String qry = "insert into Veranstaltung values(?, ?, ?, ?, ?, ?, ?, ?)";
		
		try {
			
			PreparedStatement stmt = connection.prepareStatement(qry);
			stmt.setString(1, va.getFach().getFachKuerzel());
			stmt.setString(2, va.getFach().getfBKuerzel());
			stmt.setString(3, va.getFach().getFachBezeichnung());
			stmt.setInt(4, va.getSemester());
			stmt.setString(5, typ);
			stmt.setInt(6, va.getMinTeilnTeam());
			stmt.setInt(7, va.getMaxTeilnTeam());
			stmt.setInt(8, va.getProfessor().getMaID().getId());
		
			stmt.executeUpdate();
			
			erfolg = true;
			
			
		} catch (SQLException e) {
			
			e.printStackTrace();
			return false;
		}
		
		
		return erfolg;
		
	}

	public boolean loescheVrst(Veranstaltung veranstaltung) {
		
		boolean erfolg = false;
		String qry = "delete Veranstaltung where fkuerzel = ?";
		
		try {
			
			PreparedStatement stmt = connection.prepareStatement(qry);
			stmt.setString(1, veranstaltung.getFach().getFachKuerzel());
			stmt.executeUpdate();
			erfolg = true;
			
		} catch (SQLException e) {
			
			return false;
		}
		
		return erfolg;
	}
	public boolean speichereAnmeldeFristen(String vTyp, String aTyp, Date dStart, 
												Date dEnde, Uhrzeit uStart,
														Uhrzeit uEnde) {
		
		String qry2 = "update Anmeldetermin set dStart = ?, dEnde = ?, uzStart = ?, uzEnde = ? where veranstaltungstyp = ? and anmeldetyp = ?";
		
		
		try {
			PreparedStatement stmt = connection.prepareStatement(qry2);
			stmt.setDate(1, new java.sql.Date(dStart.getTime()));
			stmt.setDate(2, new java.sql.Date(dEnde.getTime()));
			stmt.setString(3, uStart.toString());
			stmt.setString(4, uEnde.toString());
	
			stmt.setString(5, vTyp);
			stmt.setString(6, aTyp);
			
			stmt.executeUpdate();
			stmt.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		}
		return true;
	}
	public List<DBAnmeldetermin> getAnmeldeFristen() {
		
		List<DBAnmeldetermin> termine = new ArrayList<>();
		String qry = "select * from Anmeldetermin";
		try {
			PreparedStatement stmt = connection.prepareStatement(qry);
			ResultSet rs = stmt.executeQuery();
			
			while(rs.next()){
				
				String vTyp = rs.getString("veranstaltungstyp");
				String aTyp = rs.getString("anmeldetyp");
				Date dStart = new Date(rs.getDate("dStart").getTime());
				Date dEnde = new Date(rs.getDate("dEnde").getTime());
				Uhrzeit uzStart = Uhrzeit.getUhrzeit(rs.getString("uzStart"));
				Uhrzeit uzEnde = Uhrzeit.getUhrzeit(rs.getString("uzEnde"));
				
				DBAnmeldetermin term = new DBAnmeldetermin(vTyp, aTyp, dStart, dEnde, uzStart, uzEnde);
				termine.add(term);
			}
		} catch (SQLException e) {
			e.printStackTrace();
			return new ArrayList<>();
		}
		
		
		return termine;
	}
	public ServicePool getServicePool() {
		return servicePool;
	}
	public void setServicePool(ServicePool servicePool) {
		this.servicePool = servicePool;
	}
}