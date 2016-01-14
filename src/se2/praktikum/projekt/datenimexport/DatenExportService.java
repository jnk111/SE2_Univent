package se2.praktikum.projekt.datenimexport;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import se2.praktikum.projekt.datenimexport.backuptables.DBAnmeldetermin;
import se2.praktikum.projekt.datenimexport.backuptables.DBDepartment;
import se2.praktikum.projekt.datenimexport.backuptables.DBFachbereich;
import se2.praktikum.projekt.datenimexport.backuptables.DBPOVeranZuordung;
import se2.praktikum.projekt.datenimexport.backuptables.DBTeammitglied;
import se2.praktikum.projekt.datenimexport.backuptables.DBPruefungsordnung;
import se2.praktikum.projekt.dbms.DBConnector;
import se2.praktikum.projekt.models.gruppe.Gruppe;
import se2.praktikum.projekt.models.gruppe.Termin;
import se2.praktikum.projekt.models.gruppe.fachwerte.Uhrzeit;
import se2.praktikum.projekt.models.leistungen.Leistung;
import se2.praktikum.projekt.models.meldungen.Meldung;
import se2.praktikum.projekt.models.person.Assistent;
import se2.praktikum.projekt.models.person.EMail;
import se2.praktikum.projekt.models.person.Professor;
import se2.praktikum.projekt.models.person.Student;
import se2.praktikum.projekt.models.person.VerwaltMitarbeiter;
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

public class DatenExportService {
	
	
	private ServicePool servicePool;
	private Connection connection;
	
	public DatenExportService(ServicePool servicePool){
		
		this.servicePool = servicePool;
		connect();
		
		
	}

	private void connect() {
		
		connection = DBConnector.getConnection();
	}

	
	public boolean saveVABasisDatenAsJson(List<String> fachkuerzel, File fileVeran){
		
		ObjectMapper mapper = new ObjectMapper();
		List<Veranstaltung> veranstaltungen = getVABasisDaten(fachkuerzel);

		if(!veranstaltungen.isEmpty()){
			try {
				FileWriter headerWriter = new FileWriter(fileVeran, true);
				headerWriter.write(TableNames.VERANSTALTUNG + "\r" + "\n");
				headerWriter.flush();
				headerWriter.close();
				
			} catch (IOException e1) {
				ErrorLogger.log(e1);
				return false;
			}
			for(Veranstaltung va: veranstaltungen){
				
				try {
					
					String json = mapper.writeValueAsString(va);
					FileWriter writer = new FileWriter(fileVeran, true);
					writer.write(json + "\r" + "\n");
					writer.flush();
					writer.close();
					
				} catch (JsonProcessingException e) {
					
					ErrorLogger.log(e);
					return false;
				} catch (IOException e) {

					
					ErrorLogger.log(e);
					return false;
				}
			}		
		}
		return true;
	}
	
	public boolean saveVAGruppenDatenAsJson(List<String> fachkuerzel, File fileGrupp){
		
		ObjectMapper mapper = new ObjectMapper();
		List<Gruppe> gruppen = getVAGruppen(fachkuerzel);
		
		if(!gruppen.isEmpty()){
			try {
				FileWriter headerWriter = new FileWriter(fileGrupp, true);
				headerWriter.write(TableNames.GRUPPE + "\r" + "\n");
				headerWriter.flush();
				headerWriter.close();
				
			} catch (IOException e1) {
				ErrorLogger.log(e1);
				return false;
			}
			
			for(Gruppe gr: gruppen){
				
				try {
					
					String json = mapper.writeValueAsString(gr);
					FileWriter writer = new FileWriter(fileGrupp, true);
					writer.write(json + "\r" + "\n");
					writer.flush();
					writer.close();
					
				} catch (JsonProcessingException e) {
					
					ErrorLogger.log(e);
					return false;
				} catch (IOException e) {
					ErrorLogger.log(e);
					return false;
				}
			}
			
		}
		
		return true;

	}
	
	public boolean saveVATeamDatenAsJson(List<String> fachkuerzel, File fileTeam){
		
		ObjectMapper mapper = new ObjectMapper();
		List<Team> teams = getVAGruppenTeams(fachkuerzel);
		
		if(!teams.isEmpty()){
			
			try {
				FileWriter headerWriter = new FileWriter(fileTeam, true);
				headerWriter.write(TableNames.TEAM + "\r" + "\n");
				headerWriter.flush();
				headerWriter.close();
				
			} catch (IOException e1) {
				ErrorLogger.log(e1);
				return false;
			}
			for(Team team: teams){
				
				try {
					
					String json = mapper.writeValueAsString(team);
					FileWriter writer = new FileWriter(fileTeam, true);
					writer.write(json + "\r" + "\n");
					writer.flush();
					writer.close();
					
				} catch (JsonProcessingException e) {
					
					ErrorLogger.log(e);
					return false;
				} catch (IOException e) {

					ErrorLogger.log(e);
					return false;
				}
			}
			
			
			
		}
		return true;

	}
	
	
	public List<Veranstaltung> getVABasisDaten(List<String> fachkuerzel){
		
		List<Veranstaltung> veranstaltungen = new ArrayList<>();
		for(String fk: fachkuerzel){
			
			String qry = "select * from Veranstaltung where FKuerzel = '"+fk+"'";
			
			try {
				
				Statement stmt = this.connection.createStatement();
				ResultSet rs = stmt.executeQuery(qry);
				Veranstaltung va = null;
				
				while(rs.next()){
					
					String fKuerzel = rs.getString("FKuerzel");
					String fbKuerzel = rs.getString("FBKuerzel");
					String fBezeichn = rs.getString("FBezeichnung");
					int semester = rs.getInt("semester");
					Fach fach = new Fach(fKuerzel, fBezeichn, null, semester, fbKuerzel);
					String typ = rs.getString("Typ");
					int minTeilnTeam = rs.getInt("minTeilnTeam");
					int maxTeilnTeam = rs.getInt("maxTeilnTeam");
					int anzGr = servicePool.getVerAnzSrv().getGruppenanzahl(fKuerzel);
					int anzTm = servicePool.getVerAnzSrv().getTeamanzahl(fKuerzel);
					MAID maid = MAID.getMAID(rs.getInt("MAID"));
					Professor prof = getProfessor(maid);
					
					switch(typ){
					
						case "Praktikum":{
						
							va = new Praktikum(fach, semester, prof, anzTm, anzGr, minTeilnTeam, maxTeilnTeam);
							break;
					
						}
						case "WP": {
							va = new WP(fach, semester, prof, anzTm, anzGr, minTeilnTeam, maxTeilnTeam);
							break;
						}
						
						case "PO": {
							va = new Projekt(fach, semester, prof, anzTm, anzGr, minTeilnTeam, maxTeilnTeam);
							break;
						}
						case "WPP": {
							va = new WP(fach, semester, prof, anzTm, anzGr, minTeilnTeam, maxTeilnTeam);
							break;
						}
						case "Vorlesung": {
							va = new Praktikum(fach, semester, prof, anzTm, anzGr, minTeilnTeam, maxTeilnTeam);
							break;
						}
					}
					va.setTyp(typ);
					veranstaltungen.add(va);
				}
				
				stmt.close();
			} catch (SQLException e) {
				
				ErrorLogger.log(e);
				return new ArrayList<>();
			}
		}
		
		return veranstaltungen;
	}
	
	public List<Gruppe> getVAGruppen(List<String> fachkuerzel) {
		
		List<Gruppe> gruppen = new ArrayList<>();
		List<Veranstaltung> veranstaltungen = getVABasisDaten(fachkuerzel);
		
		for(Veranstaltung va: veranstaltungen){
		
			gruppen.addAll(getGruppenDatenFuerVA(va));
		}
		
		return gruppen;
	}
	
	private List<Gruppe> getGruppenDatenFuerVA(Veranstaltung va) {
		
		List<Gruppe> gruppen = new ArrayList<>();
		String qry = "select * from Gruppe where Fkuerzel='"+va.getFach().getFachKuerzel()+"'";
		
		try {
			
			Statement stmt = connection.createStatement();
			ResultSet rs = stmt.executeQuery(qry);
			
			while(rs.next()){
				
				int grpNr = rs.getInt("grpNr");
				String fKuerzel = rs.getString("FKuerzel");
				Gruppe gr = new Gruppe(fKuerzel, grpNr);
				int maxTeams = rs.getInt("maxTeams");
				int anzTeams = servicePool.getVerAnzSrv().getTeamanzahl(gr);
				MAID profMaid = MAID.getMAID(rs.getInt("ProfMAID"));
				MAID assisMaid = MAID.getMAID(rs.getInt("AssisMAID"));
				List<Termin> termine = servicePool.getVerAnzSrv().getTermine(grpNr, fKuerzel);
				Professor prof = getProfessor(profMaid);
				Assistent assist = getAssistent(assisMaid);
				gr = new Gruppe(fKuerzel, grpNr,termine, prof, assist, maxTeams,anzTeams);
				gruppen.add(gr);
			}
			stmt.close();
			
		} catch (SQLException e) {
			
			ErrorLogger.log(e);
			return new ArrayList<>();
			
		}
		
		return gruppen;
	}
	
	public List<Team> getVAGruppenTeams(List<String> fachkuerzel) {
		
		List<Team> teams = new ArrayList<>();
		List<Gruppe> gruppen = getVAGruppen(fachkuerzel);
		
		for(Gruppe gr: gruppen){
			
			String qry = "select * from Team where FKuerzel = '"+gr.getFachkuerzel()+"' and VorgGrpNr = " + gr.getGrpNr();
			
			try {
				
				Statement stmt = connection.createStatement();
				ResultSet rs = stmt.executeQuery(qry);
				
				// Alle angemeldeten Teams
				while(rs.next()){
					
					TeamID teamId = TeamID.getTeamID(rs.getString("TeamID"));
					int minMitglieder = rs.getInt("minMitglieder");
					int maxMitglieder = rs.getInt("maxMitglieder");
					int grpNr = rs.getInt("grpNr");
					if(grpNr == 0){
						grpNr = -1;
					}
					
					int vorgGrpNr = rs.getInt("vorggrpnr");
					List<Student> mitglieder = getTeamMitglieder(teamId);
					
					Team team = new Team(teamId, minMitglieder, maxMitglieder, mitglieder, grpNr, vorgGrpNr, gr.getFachkuerzel());
					teams.add(team);
					
				}	
				stmt.close();
			} catch (SQLException e) {
				ErrorLogger.log(e);
				return new ArrayList<>();
			}
		}
		
		return teams;
	}

	
	private List<Student> getTeamMitglieder(TeamID teamId) {
		
		List<Student> mitglieder = new ArrayList<>();
		List<MatrikelNr> matrNummern = new ArrayList<>();
		
		String qry = "select * from Teammitglied where TeamID ='"+teamId.getId()+"'";
		
		try {
			
			Statement stmt = connection.createStatement();
			ResultSet rs = stmt.executeQuery(qry);
			
			while(rs.next()){
				
				MatrikelNr matrNr = MatrikelNr.getMatrikelNr(rs.getInt("matrNr"));
				matrNummern.add(matrNr);
			}
			stmt.close();
			
		} catch (SQLException e) {
			ErrorLogger.log(e);
			return new ArrayList<>();
		}
		
		for(MatrikelNr nr: matrNummern){
			 
			String qry2 = "select vorname, nachname from Student where matrNr = "+nr.getMatrNr();
			
			
			try {
				
				Statement stmt = connection.createStatement();
				ResultSet rs = stmt.executeQuery(qry2);
				
				while(rs.next()){
					
					String vorname = rs.getString("vorname");
					String nachname = rs.getString("nachname");
					
					Student stud = new Student(nr, vorname, nachname);
					
					mitglieder.add(stud);
				}
				stmt.close();
				
			} catch (SQLException e) {
				
				ErrorLogger.log(e);
				return new ArrayList<>();
			}
			
		}
		
		return mitglieder;
		
		
	}

	
	private Professor getProfessor(MAID maid) {
		
		String qry = "select vorname, nachname from professor where maid = "+maid.getId();
		Professor prof = null;
	
		try {
			Statement stmt = connection.createStatement();
			ResultSet rs = stmt.executeQuery(qry);
			
			while(rs.next()){
				
				String vorname = rs.getString("vorname");
				String nachname = rs.getString("nachname");
				prof = new Professor(maid, vorname, nachname);
				
			}
			stmt.close();
		} catch (SQLException e) {
			
			ErrorLogger.log(e);
		}
		
		return prof;
	}
	
	
	private Assistent getAssistent(MAID assisMaid) {
		String qry = "select vorname, nachname from assistent where maid = "+assisMaid.getId();
		Assistent assis = null;
	
		try {
			Statement stmt = connection.createStatement();
			ResultSet rs = stmt.executeQuery(qry);
			
			while(rs.next()){
				
				String vorname = rs.getString("vorname");
				String nachname = rs.getString("nachname");
				assis = new Assistent(assisMaid, vorname, nachname);
				
			}
			
			stmt.close();
			
		} catch (SQLException e) {
			
			ErrorLogger.log(e);
		}
		
		return assis;
	}

	public boolean sichereAlleDaten() {
	
		Date date = new Date();
		File fileFolder = new File(Folders.BACKUP_FOLDER + "/bkp_" + date.getTime());
		fileFolder.mkdirs();
		
		boolean erfolg = sichereDepartments(new File(fileFolder.getPath() + "/00_departments.bkp"));
		erfolg &= sichereFachbereiche(new File(fileFolder.getPath() + "/01_fachbereiche.bkp"));
		erfolg &= sichereProfessoren(new File(fileFolder.getPath() + "/02_professoren.bkp"));
		erfolg &= sichereAssistenten(new File(fileFolder.getPath() + "/03_assistenten.bkp"));
		erfolg &= sicherePruefungsordnungen(new File(fileFolder.getPath() + "/04_pruefordn.bkp"));
		erfolg &= sichereAlleStudenten(new File(fileFolder.getPath()  + "/05_studenten.bkp"));
		erfolg &= sichereAlleVerwaltMitarbeiter(new File(fileFolder.getPath() + "/06_verwmitarbeiter.bkp"));
		
		List<String> fachkuerzel = getAlleFachkuerzel();
		erfolg &= saveVABasisDatenAsJson(fachkuerzel, new File(fileFolder.getPath() + "/07_veranstaltungen.bkp"));
		erfolg &= sichereAnmeldeTermine(new File(fileFolder.getPath() + "/08_anmeldetermine.bkp"));
		erfolg &= saveVAGruppenDatenAsJson(fachkuerzel, new File(fileFolder.getPath() + "/09_gruppen.bkp"));
		erfolg &= saveVATeamDatenAsJson(fachkuerzel, new File(fileFolder.getPath() + "/10_teams.bkp"));
		erfolg &= sichereTeammitglieder(new File(fileFolder.getPath() + "/11_teammitglieder.bkp"));
		erfolg &= sichereBewertungen(fachkuerzel, new File(fileFolder.getPath() + "/12_bewertungen.bkp"));
		erfolg &= sicherePOVeranZuOrdn(new File(fileFolder.getPath() + "/13_poveranzuordn.bkp"));
		erfolg &= sichereAlleMeldungen(new File(fileFolder.getPath() + "/14_meldungen.bkp"));
		
		
		return erfolg;
		

	}
	
	private boolean sichereAlleMeldungen(File file) {
		
		List<Meldung> meldungen = servicePool.getTeamVerwSrv().getAlleMeldungenBKP();
		ObjectMapper mapper = new ObjectMapper();
		
		if(!meldungen.isEmpty()){
			
			try {
				FileWriter headerWriter = new FileWriter(file, true);
				headerWriter.write(TableNames.MELDUNG + "\r" + "\n");
				headerWriter.flush();
				headerWriter.close();
				
			} catch (IOException e1) {
				ErrorLogger.log(e1);
				return false;
			}
			for(Meldung meld: meldungen){
				
				
				try {
					String json = mapper.writeValueAsString(meld);
					FileWriter writer = new FileWriter(file, true);
					writer.write(json + "\r" + "\n");
					writer.flush();
					writer.close();
					
				} catch (JsonProcessingException e) {
					
					ErrorLogger.log(e);
					return false;
				} catch (IOException e) {
					
					ErrorLogger.log(e);
					return false;
				}				
			}
			
		}

		return true;
	}


	private boolean sichereTeammitglieder(File file) {
		
		List<DBTeammitglied> mitglieder = getAlleDBTeammitglieder();
		ObjectMapper mapper = new ObjectMapper();
		
		if(!mitglieder.isEmpty()){
			
			try {
				FileWriter headerWriter = new FileWriter(file, true);
				headerWriter.write(TableNames.TEAM_MITGLIED + "\r" + "\n");
				headerWriter.flush();
				headerWriter.close();
				
			} catch (IOException e1) {
				ErrorLogger.log(e1);
			}
			for(DBTeammitglied mg: mitglieder){
				
				
				try {
					String json = mapper.writeValueAsString(mg);
					FileWriter writer = new FileWriter(file, true);
					writer.write(json + "\r" + "\n");
					writer.flush();
					writer.close();
					
				} catch (JsonProcessingException e) {
					
					ErrorLogger.log(e);
					return false;
				} catch (IOException e) {
					
					ErrorLogger.log(e);
					return false;
				}				
			}
			
		}
			
		
		return true;
	}

	public List<DBTeammitglied> getAlleDBTeammitglieder() {
		List<DBTeammitglied> mitglieder = new ArrayList<>();
		String qry = "select * from teammitglied";
		try {
			PreparedStatement stmt = connection.prepareStatement(qry);
			ResultSet rs = stmt.executeQuery();
			while(rs.next()){
				TeamID teamid = TeamID.getTeamID("" + rs.getInt("teamid"));
				MatrikelNr matrNr = MatrikelNr.getMatrikelNr(rs.getInt("matrNr"));
				String bestaetigt = rs.getString("bestaetigt");
				String einzel = rs.getString("einzel");
				DBTeammitglied tmg = new DBTeammitglied(teamid, matrNr, bestaetigt, einzel);
				mitglieder.add(tmg);
			}
			stmt.close();
		} catch (SQLException e) {
			ErrorLogger.log(e);
			return new ArrayList<>();
		}
		return mitglieder;
	}


	private boolean sicherePOVeranZuOrdn(File file) {
		
		List<DBPOVeranZuordung> poVeranZuordn = getAllePoVeranZuOrdn();
		ObjectMapper mapper = new ObjectMapper();
		
		if(!poVeranZuordn.isEmpty()){
			
			try {
				FileWriter headerWriter = new FileWriter(file, true);
				headerWriter.write(TableNames.PO_VERAN_ZUORDN + "\r" + "\n");
				headerWriter.flush();
				headerWriter.close();
				
			} catch (IOException e1) {
				ErrorLogger.log(e1);
				return false;
			}
			for(DBPOVeranZuordung pov: poVeranZuordn){
				
				
				try {
					String json = mapper.writeValueAsString(pov);
					FileWriter writer = new FileWriter(file, true);
					writer.write(json + "\r" + "\n");
					writer.flush();
					writer.close();
					
				} catch (JsonProcessingException e) {
					
					ErrorLogger.log(e);
					return false;
				} catch (IOException e) {
					
					ErrorLogger.log(e);
					return false;
				}				
			}
			
		}

		return true;
		

	}

	public List<DBPOVeranZuordung> getAllePoVeranZuOrdn() {
		
		List<DBPOVeranZuordung> dbpo = new ArrayList<>();
		String qry = "select * from POVeranstaltungszuordnung";
		try {
			PreparedStatement stmt = connection.prepareStatement(qry);
			ResultSet rs = stmt.executeQuery();
			
			while(rs.next()){
				String poKuerzel = rs.getString("pokuerzel");
				String fkuerzel = rs.getString("fkuerzel");
				
				DBPOVeranZuordung dbpoe = new DBPOVeranZuordung(poKuerzel, fkuerzel);
				dbpo.add(dbpoe);
			}
			
			stmt.close();
		} catch (SQLException e) {
			ErrorLogger.log(e);
			return new ArrayList<>();
		}
		
		return dbpo;
	}

	private boolean sichereBewertungen(List<String> fachkuerzel, File file) {
		
		ObjectMapper mapper = new ObjectMapper();
		List<Leistung> alleLeistungen = new ArrayList<>();
		for(String fk: fachkuerzel){
			
			alleLeistungen.addAll(servicePool.getlAnzSrv().getLeistungen(fk));
		}
		
		if(!alleLeistungen.isEmpty()){
			
			try {
				FileWriter headerWriter = new FileWriter(file, true);
				headerWriter.write(TableNames.BEWERTUNG + "\r" + "\n");
				headerWriter.flush();
				headerWriter.close();
				
			} catch (IOException e1) {
				ErrorLogger.log(e1);
				return false;
			}
			for(Leistung leist: alleLeistungen){
				
				try {
					String json = mapper.writeValueAsString(leist);
					FileWriter writer = new FileWriter(file, true);
					writer.write(json + "\r" + "\n");
					writer.flush();
					writer.close();
					
				} catch (JsonProcessingException e) {
					
					ErrorLogger.log(e);
					return false;
				} catch (IOException e) {
					
					ErrorLogger.log(e);
					return false;
				}				
			}
			
		}

		return true;
	}


	private boolean sichereFachbereiche(File file) {
		
		ObjectMapper mapper = new ObjectMapper();
		List<DBFachbereich> fachbereiche = getAlleDBFachbereiche();
		
		if(!fachbereiche.isEmpty()){
			try {
				FileWriter headerWriter = new FileWriter(file, true);
				headerWriter.write(TableNames.FACHBEREICH + "\r" + "\n");
				headerWriter.flush();
				headerWriter.close();
				
			} catch (IOException e1) {
				ErrorLogger.log(e1);
			}
			for(DBFachbereich dbfb: fachbereiche){
				
				
				try {
					String json = mapper.writeValueAsString(dbfb);
					FileWriter writer = new FileWriter(file, true);
					writer.write(json + "\r" + "\n");
					writer.flush();
					writer.close();
					
				} catch (JsonProcessingException e) {
					
					ErrorLogger.log(e);
					return false;
				} catch (IOException e) {
					
					ErrorLogger.log(e);
					return false;
				}				
			}
			
		}

		return true;
	}

	public List<DBFachbereich> getAlleDBFachbereiche() {
		List<DBFachbereich> fachbereiche = new ArrayList<>();
		String qry = "select * from fachbereich";
		
		
		try {
			PreparedStatement stmt = connection.prepareStatement(qry);
			ResultSet rs = stmt.executeQuery();
			while(rs.next()){
				String fbkuerzel = rs.getString("fbkuerzel");
				String fbbezeichnung = rs.getString("fbbezeichnung");
				String dkuerzel = rs.getString("dkuerzel");
				DBFachbereich dbfb = new DBFachbereich(fbkuerzel, fbbezeichnung, dkuerzel);
				fachbereiche.add(dbfb);
			}
			
			stmt.close();
		} catch (SQLException e) {
			ErrorLogger.log(e);
			return new ArrayList<>();
		}

		return fachbereiche;
	}

	private boolean sichereDepartments(File file) {
		ObjectMapper mapper = new ObjectMapper();
		List<DBDepartment> dbdepartments = getAlleDepartments();
		
		if(!dbdepartments.isEmpty()){
			
			try {
				FileWriter headerWriter = new FileWriter(file, true);
				headerWriter.write(TableNames.DEPARTMENT + "\r" + "\n");
				headerWriter.flush();
				headerWriter.close();
				
			} catch (IOException e1) {
				ErrorLogger.log(e1);
				return false;
			}
			for(DBDepartment dbdp: dbdepartments){
				
				
				try {
					String json = mapper.writeValueAsString(dbdp);
					FileWriter writer = new FileWriter(file, true);
					writer.write(json + "\r" + "\n");
					writer.flush();
					writer.close();
					
				} catch (JsonProcessingException e) {
					
					ErrorLogger.log(e);
					return false;
				} catch (IOException e) {
					
					ErrorLogger.log(e);
					return false;
				}				
			}
			
		}

		return true;
	}

	public List<DBDepartment> getAlleDepartments() {
		List<DBDepartment> departments = new ArrayList<>();
		String qry = "select * from department";
		try {
			PreparedStatement stmt = connection.prepareStatement(qry);
			ResultSet rs = stmt.executeQuery();
			while(rs.next()){
				String dkuerzel = rs.getString("dkuerzel");
				String dbezeichnung = rs.getString("dbezeichnung");
				DBDepartment dbdp = new DBDepartment(dkuerzel, dbezeichnung);
				departments.add(dbdp);
			}
			stmt.close();
		} catch (SQLException e) {
			ErrorLogger.log(e);
			return new ArrayList<>();
		}
		return departments;
	}



	private boolean sichereAlleVerwaltMitarbeiter(File fileVerwMA) {
		
		
		ObjectMapper mapper = new ObjectMapper();
		List<VerwaltMitarbeiter> verwMitarbeiter = getAlleVerwMitarbeiter();
		
		if(!verwMitarbeiter.isEmpty()){
			try {
				FileWriter headerWriter = new FileWriter(fileVerwMA, true);
				headerWriter.write(TableNames.VERW_MA + "\r" + "\n");
				headerWriter.flush();
				headerWriter.close();
				
			} catch (IOException e1) {
				ErrorLogger.log(e1);
				return false;
			}
			
			for(VerwaltMitarbeiter ma: verwMitarbeiter){
				
				
				try {
					String json = mapper.writeValueAsString(ma);
					FileWriter writer = new FileWriter(fileVerwMA, true);
					writer.write(json + "\r" + "\n");
					writer.flush();
					writer.close();
					
				} catch (JsonProcessingException e) {
					
					ErrorLogger.log(e);
					return false;
				} catch (IOException e) {
					
					ErrorLogger.log(e);
					return false;
				}				
			}
			
		}

		return true;
	}

	public List<VerwaltMitarbeiter> getAlleVerwMitarbeiter() {
		
		List<VerwaltMitarbeiter> verwMitarbeiter = new ArrayList<>();
		String qry = "select * from Verwaltungsmitarbeiter";
		try {
			PreparedStatement stmt = connection.prepareStatement(qry);
			ResultSet rs = stmt.executeQuery();
			MAID maid = null;
			String benutzername = null;
			String passwort = null;
			String vorname = null;
			String nachname = null;
			Adresse adresse = null;
			Date gebDatum = null;
			String gebOrt = null;
			EMail email = null;
			String department = null;
			
			while(rs.next()){
				
				maid = MAID.getMAID(rs.getInt("maid"));
				benutzername = rs.getString("username");
				passwort = rs.getString("passwort");
				vorname = rs.getString("vorname");
				nachname = rs.getString("nachname");
				
				// Adresse
				int hausNr = rs.getInt("hausnr");
				String strasse = rs.getString("strasse");
				PLZ plz = PLZ.getPostLeitzahl(rs.getString("plz"));
				String ort = rs.getString("ort");
				adresse = Adresse.getAdresse(strasse, hausNr, plz, ort);
				
				java.sql.Date gebDatumSQL = rs.getDate("gebdatum");
				
				if(gebDatumSQL != null){
					gebDatum = new Date(gebDatumSQL.getTime());
				}
				
				gebOrt = rs.getString("gebOrt");
				email = new EMail(rs.getString("email"));
				department = rs.getString("dkuerzel");
				
				VerwaltMitarbeiter ma = new VerwaltMitarbeiter(maid, vorname, nachname, 
											   	benutzername, passwort, gebDatum, gebOrt, 
											   	adresse, department, email);
				
				verwMitarbeiter.add(ma);
			}
			stmt.close();
		} catch (SQLException e) {
			
			ErrorLogger.log(e);
			return new ArrayList<>();
		}
		
		return verwMitarbeiter;
	}

	private boolean sichereAlleStudenten(File fileStud) {
		
		ObjectMapper mapper = new ObjectMapper();
		List<Student> alleStudenten = getAlleStudenten();
		
		if(!alleStudenten.isEmpty()){
			try {
				FileWriter headerWriter = new FileWriter(fileStud, true);
				headerWriter.write(TableNames.STUDENT + "\r" + "\n");
				headerWriter.flush();
				headerWriter.close();
				
			} catch (IOException e1) {
				ErrorLogger.log(e1);
				return false;
			}
			for(Student stud: alleStudenten){
				
				
				try {
					String json = mapper.writeValueAsString(stud);
					FileWriter writer = new FileWriter(fileStud, true);
					writer.write(json + "\r" + "\n");
					writer.flush();
					writer.close();
					
				} catch (JsonProcessingException e) {
					
					ErrorLogger.log(e);
					return false;
				} catch (IOException e) {
					
					ErrorLogger.log(e);
					return false;
				}				
			}
			
		}

		return true;
	}

	public List<Student> getAlleStudenten() {
		
		List<Student> studenten = new ArrayList<>();
		String qry = "select * from Student";
		try {
			PreparedStatement stmt = connection.prepareStatement(qry);
			ResultSet rs = stmt.executeQuery();
			
			MatrikelNr matrNr = null;
			String benutzername = null;
			String vorname = null;
			String nachname = null;
			Adresse adresse = null;
			Date gebDatum = null;
			String gebOrt = null;
			EMail email = null;
			String passwort = null;
			String fachbereich = null;
			String po = null;
			String department = null;
			
			while(rs.next()){
				
				matrNr = MatrikelNr.getMatrikelNr(rs.getInt("matrNr"));
				benutzername = rs.getString("username");
				vorname = rs.getString("vorname");
				nachname = rs.getString("nachname");
				passwort = rs.getString("passwort");
				po = rs.getString("pokuerzel");
				// Adresse
				int hausNr = rs.getInt("hausnr");
				String strasse = rs.getString("strasse");
				PLZ plz = PLZ.getPostLeitzahl(rs.getString("plz"));
				String ort = rs.getString("ort");
				adresse = Adresse.getAdresse(strasse, hausNr, plz, ort);
				
				java.sql.Date gebDatumSQL = rs.getDate("gebdatum");
				
				if(gebDatumSQL != null){
					gebDatum = new Date(gebDatumSQL.getTime());
				}
				
				gebOrt = rs.getString("gebOrt");
				email = new EMail(rs.getString("email"));
				fachbereich = rs.getString("fbkuerzel");
				department = rs.getString("dkuerzel");
				
				Student stud = new Student(matrNr, vorname, nachname, benutzername, passwort, gebDatum, gebOrt, adresse, department, fachbereich, email);
				stud.setPo(po);
				studenten.add(stud);
				
			}
			
			stmt.close();
		} catch (SQLException e) {
			ErrorLogger.log(e);
			new ArrayList<>();
		}
		return studenten;
	}

	private boolean sicherePruefungsordnungen(File filePruefOrdn) {
		
		ObjectMapper mapper = new ObjectMapper();
		List<DBPruefungsordnung> pruefOrdnungen = getAllePruefOrdnungen();
		
		if(!pruefOrdnungen.isEmpty()){
			
			try {
				FileWriter headerWriter = new FileWriter(filePruefOrdn, true);
				headerWriter.write(TableNames.PRUEFORDN + "\r" + "\n");
				headerWriter.flush();
				headerWriter.close();
				
			} catch (IOException e1) {
				ErrorLogger.log(e1);
				return false;
			}
			
			for(DBPruefungsordnung ordnung: pruefOrdnungen){
				
				
				try {
					String json = mapper.writeValueAsString(ordnung);
					FileWriter writer = new FileWriter(filePruefOrdn, true);
					writer.write(json + "\r" + "\n");
					writer.flush();
					writer.close();
					
				} catch (JsonProcessingException e) {
					
					ErrorLogger.log(e);
					return false;
				} catch (IOException e) {
					
					ErrorLogger.log(e);
					return false;
				}				
			}
			
		}

		return true;
	}

	public List<DBPruefungsordnung> getAllePruefOrdnungen() {
		
		List<DBPruefungsordnung> pruefOrdnungen = new ArrayList<>();
		String qry = "select * from pruefungsordnung";
		try {
			PreparedStatement stmt = connection.prepareStatement(qry);
			ResultSet rs = stmt.executeQuery();
			
			while(rs.next()){
				
				String poKuerzel = rs.getString("pokuerzel");
				String poBezeichnung = rs.getString("poBezeichnung");
				DBPruefungsordnung ord = new DBPruefungsordnung(poKuerzel, poBezeichnung);
				pruefOrdnungen.add(ord);
			}
			
			stmt.close();
		} catch (SQLException e) {
			ErrorLogger.log(e);
			return new ArrayList<>();
		}
		
		return pruefOrdnungen;
		
	}

	private boolean sichereAnmeldeTermine(File fileAnmTermine) {
		
		ObjectMapper mapper = new ObjectMapper();
		List<DBAnmeldetermin> anmTermine = getAlleAnmeldetermine();
		
		if(!anmTermine.isEmpty()){
			try {
				FileWriter headerWriter = new FileWriter(fileAnmTermine, true);
				headerWriter.write(TableNames.ANMELDETERMIN + "\r" + "\n");
				headerWriter.flush();
				headerWriter.close();
				
			} catch (IOException e1) {
				ErrorLogger.log(e1);
				return false;
			}
			for(DBAnmeldetermin termin: anmTermine){
				
				
				try {
					String json = mapper.writeValueAsString(termin);
					FileWriter writer = new FileWriter(fileAnmTermine, true);
					writer.write(json + "\r" + "\n");
					writer.flush();
					writer.close();
					
				} catch (JsonProcessingException e) {
					
					ErrorLogger.log(e);
					return false;
				} catch (IOException e) {
					
					ErrorLogger.log(e);
					return false;
				}				
			}
			
		}

		return true;
		
		
	}

	public List<DBAnmeldetermin> getAlleAnmeldetermine() {

		List<DBAnmeldetermin> anmTermine = new ArrayList<>();
		String qry = "select * from Anmeldetermin";
		try {
			PreparedStatement stmt = connection.prepareStatement(qry);
			ResultSet rs = stmt.executeQuery();
			
			while(rs.next()){
				
				String vTyp = rs.getString("Veranstaltungstyp");
				String aTyp = rs.getString("Anmeldetyp");
				java.sql.Date anmStartDatumSQL = rs.getDate("dStart");
				java.sql.Date anmEndeDatumSQL = rs.getDate("dEnde");
				Date anmStartDatum = null;
				Date anmEndeDatum = null;
				if(anmStartDatumSQL != null){
					anmStartDatum = new Date(anmStartDatumSQL.getTime());
					
				}
				
				if(anmEndeDatumSQL != null){
					anmEndeDatum = new Date(anmEndeDatumSQL.getTime());
					
				}
				
				Uhrzeit uzStart = Uhrzeit.getUhrzeit(rs.getString("uzStart"));
				Uhrzeit uzEnde = Uhrzeit.getUhrzeit(rs.getString("uzEnde"));
				
				DBAnmeldetermin anmTermin = new DBAnmeldetermin(vTyp, aTyp, anmStartDatum, anmEndeDatum, uzStart, uzEnde);
				anmTermine.add(anmTermin);
				
				
			}
			
			stmt.close();
			
		} catch (SQLException e) {
			ErrorLogger.log(e);
			return new ArrayList<>();
		}
		
		return anmTermine;
	}

	public List<String> getAlleFachkuerzel() {
		
		List<String> fachkuerzel = new ArrayList<>();
		String qry = "select fkuerzel from veranstaltung";
		try {
			PreparedStatement stmt = connection.prepareStatement(qry);
			ResultSet rs = stmt.executeQuery();
			
			while(rs.next()){
				
				fachkuerzel.add(rs.getString("fkuerzel"));
				
			}
		} catch (SQLException e) {
			
			ErrorLogger.log(e);
			return new ArrayList<>();
			
		}
		
		return fachkuerzel;
		
	}

	private boolean sichereAssistenten(File fileAssist) {
		
		ObjectMapper mapper = new ObjectMapper();
		List<Assistent> assistenten = getAlleAssistDaten();
		
		if(!assistenten.isEmpty()){
			try {
				FileWriter headerWriter = new FileWriter(fileAssist, true);
				headerWriter.write(TableNames.ASSISTENT + "\r" + "\n");
				headerWriter.flush();
				headerWriter.close();
				
			} catch (IOException e1) {
				ErrorLogger.log(e1);
				return false;
			}
			for(Assistent assist: assistenten){
				
				
				try {
					String json = mapper.writeValueAsString(assist);
					FileWriter writer = new FileWriter(fileAssist, true);
					writer.write(json + "\r" + "\n");
					writer.flush();
					writer.close();
					
				} catch (JsonProcessingException e) {
					
					ErrorLogger.log(e);
					return false;
				} catch (IOException e) {
					
					ErrorLogger.log(e);
					return false;
				}				
			}
		}

		return true;
	}

	public List<Assistent> getAlleAssistDaten() {
		
		List<Assistent> assistenten = new ArrayList<>();
		String qry = "select * from Assistent";
		try {
			PreparedStatement stmt = connection.prepareStatement(qry);
			ResultSet rs = stmt.executeQuery();
			MAID maid = null;
			String benutzername = null;
			String vorname = null;
			String nachname = null;
			Adresse adresse = null;
			Date gebDatum = null;
			String gebOrt = null;
			EMail email = null;
			String department = null;
			String passwort;
			
			while(rs.next()){
				
				maid = MAID.getMAID(rs.getInt("maid"));
				benutzername = rs.getString("username");
				vorname = rs.getString("vorname");
				nachname = rs.getString("nachname");
				passwort = rs.getString("passwort");
				
				// Adresse
				int hausNr = rs.getInt("hausnr");
				String strasse = rs.getString("strasse");
				PLZ plz = PLZ.getPostLeitzahl(rs.getString("plz"));
				String ort = rs.getString("ort");
				adresse = Adresse.getAdresse(strasse, hausNr, plz, ort);
				
				java.sql.Date gebDatumSQL = rs.getDate("gebdatum");
				
				if(gebDatumSQL != null){
					gebDatum = new Date(gebDatumSQL.getTime());
				}
				
				gebOrt = rs.getString("gebOrt");
				email = new EMail(rs.getString("email"));
				department = rs.getString("dkuerzel");
				
				Assistent assist = new Assistent(maid, vorname, nachname, 
											   	benutzername, passwort, gebDatum, gebOrt, 
											   	adresse, department, email);
				
				assistenten.add(assist);
			}
			
			stmt.close();
		} catch (SQLException e) {
			
			ErrorLogger.log(e);
			return new ArrayList<>();
		}
		
		return assistenten;
	}

	private boolean sichereProfessoren(File fileProfs) {
		
		ObjectMapper mapper = new ObjectMapper();
		List<Professor> professoren = getAlleProfDaten();
		
		if(!professoren.isEmpty()){
			try {
				FileWriter headerWriter = new FileWriter(fileProfs, true);
				headerWriter.write(TableNames.PROFESSOR + "\r" + "\n");
				headerWriter.flush();
				headerWriter.close();
				
			} catch (IOException e1) {
				ErrorLogger.log(e1);
				return false;
			}
			for(Professor prof: professoren){
				
				
				try {
					String json = mapper.writeValueAsString(prof);
					FileWriter writer = new FileWriter(fileProfs, true);
					writer.write(json + "\r" + "\n");
					writer.flush();
					writer.close();
					
				} catch (JsonProcessingException e) {
					
					ErrorLogger.log(e);
					return false;
				} catch (IOException e) {
					
					ErrorLogger.log(e);
					return false;
				}				
			}
			
		}

		return true;
		
	}

	public List<Professor> getAlleProfDaten() {
		
		List<Professor> professoren = new ArrayList<>();
		/*
		 * Professor: MAID, usernanme, vorname, nachname, Titel, passwort, strasse, hausNr, plz, ort,
		* gebDatum, gebOrt, email, DKuerzel 
		* insert INTO Professor values(1, 'oaa001', 'Martin', 'Huebner', 'Professor Dr.', 'oaa001', 'Neue Str.', 10, '22743', 'Hamburg', TO_DATE('8-12-1965','MM-DD-YYYY'), 'Frankfurt', 'martin.huebner@haw-hamburg.de', 'I');
		*/
		String qry = "select * from Professor";
		try {
			PreparedStatement stmt = connection.prepareStatement(qry);
			ResultSet rs = stmt.executeQuery();
			MAID maid = null;
			String benutzername = null;
			String vorname = null;
			String nachname = null;
			String titel = null;
			Adresse adresse = null;
			Date gebDatum = null;
			String gebOrt = null;
			EMail email = null;
			String department = null;
			String passwort = null;
			
			while(rs.next()){
				
				maid = MAID.getMAID(rs.getInt("maid"));
				benutzername = rs.getString("username");
				passwort = rs.getString("passwort");
				titel = rs.getString("titel");
				vorname = rs.getString("vorname");
				nachname = rs.getString("nachname");
				titel = rs.getString("titel");
				
				// Adresse
				int hausNr = rs.getInt("hausnr");
				String strasse = rs.getString("strasse");
				PLZ plz = PLZ.getPostLeitzahl(rs.getString("plz"));
				String ort = rs.getString("ort");
				adresse = Adresse.getAdresse(strasse, hausNr, plz, ort);
				java.sql.Date gebDatumSQL = rs.getDate("gebdatum");
				
				if(gebDatumSQL != null){
					gebDatum = new Date(gebDatumSQL.getTime());
				}
				
				gebOrt = rs.getString("gebOrt");
				email = new EMail(rs.getString("email"));
				department = rs.getString("dkuerzel");
				
				Professor prof = new Professor(maid, titel, vorname, nachname, 
											   benutzername, passwort, gebDatum, gebOrt, 
											   adresse, department, email);
				
				professoren.add(prof);
			}
			
			stmt.close();
		} catch (SQLException e) {
			
			ErrorLogger.log(e);
			return new ArrayList<>();
		}
		
		return professoren;
	}

	public boolean exportiereVABasisDaten(List<String> fachkuerzel) {
		
		Date date = new Date();
		File fileFolder = new File(Folders.EXP_FOLDER + "/exp_" + date.getTime() + "_" + "veranstaltungen");
		fileFolder.mkdirs();
		
		return saveVABasisDatenAsJson(fachkuerzel, new File(fileFolder.getPath() + "/00_veranstaltungen.exp"));
	}

	public boolean exportiereVADatenMitGruppen(List<String> fachkuerzel) {
		
		Date date = new Date();
		File fileFolder = new File(Folders.EXP_FOLDER + "/exp_" + date.getTime() + "_" + "VaMitGruppen");
		fileFolder.mkdirs();
		
		return saveVABasisDatenAsJson(fachkuerzel, new File(fileFolder.getPath() + "/00_veranstaltungen.exp")) 
				&& saveVAGruppenDatenAsJson(fachkuerzel, new File(fileFolder.getPath() + "/01_gruppen.exp"));
	}

	public boolean exportiereVADatenMitGruppenUndTeams(List<String> fachkuerzel) {
		
		Date date = new Date();
		File fileFolder = new File(Folders.EXP_FOLDER + "/exp_" + date.getTime() + "_" + "VaMitGruppUndTeams");
		fileFolder.mkdirs();
		
		return saveVABasisDatenAsJson(fachkuerzel, new File(fileFolder.getPath() + "/00_veranstaltungen.exp")) 
				&& saveVAGruppenDatenAsJson(fachkuerzel, new File(fileFolder.getPath() + "/01_gruppen.exp"))
				&& saveVATeamDatenAsJson(fachkuerzel, new File(fileFolder.getPath() + "/02_teams.exp"));
	}

	public boolean exportiereProfDaten() {
		
		Date date = new Date();
		File fileFolder = new File(Folders.EXP_FOLDER + "/exp_" + date.getTime() + "_" + "professoren");
		fileFolder.mkdirs();
		
		return sichereProfessoren(new File(fileFolder.getPath() + "/00_professoren.exp"));
	}

	public boolean exportiereAssitDaten() {
		
		Date date = new Date();
		File fileFolder = new File(Folders.EXP_FOLDER + "/exp_" + date.getTime() + "_" + "assistenten");
		fileFolder.mkdirs();
		
		return sichereAssistenten(new File(fileFolder.getPath() + "/00_assistenten.exp"));
	}

	public boolean exportiereVerwMADaten() {

		Date date = new Date();
		File fileFolder = new File(Folders.EXP_FOLDER + "/exp_" + date.getTime() + "_" + "verwmitarbeiter");
		fileFolder.mkdirs();
		
		return sichereAlleVerwaltMitarbeiter(new File(fileFolder.getPath() + "/00_verwmitarbeiter.exp"));
	}

	public boolean exportiereLeistungenDaten(List<String> fachkuerzel) {
		
		Date date = new Date();
		File fileFolder = new File(Folders.EXP_FOLDER + "/exp_" + date.getTime() + "_" + "leistungen");
		fileFolder.mkdirs();
		
		return sichereBewertungen(fachkuerzel, new File(fileFolder.getPath() + "/00_bewertungen.exp"));
	}

	public boolean exportiereStudenten() {
		
		Date date = new Date();
		File fileFolder = new File(Folders.EXP_FOLDER + "/exp_" + date.getTime() + "_" + "studenten");
		fileFolder.mkdirs();
		
		return sichereAlleStudenten(new File(fileFolder.getPath() + "/00_studenten.exp"));
	}

	public boolean exportiereAlleVABasisDaten() {
		List<String> fachkuerzel = getAlleFachkuerzel();
		return exportiereVABasisDaten(fachkuerzel);
	}

	public boolean exportiereAlleVADatenMitGruppen() {
		List<String> fachkuerzel = getAlleFachkuerzel();
		return exportiereVADatenMitGruppen(fachkuerzel);
	}

	public boolean exportiereAlleVADatenMitGruppenUndTeams() {
		List<String> fachkuerzel = getAlleFachkuerzel();
		return exportiereVADatenMitGruppenUndTeams(fachkuerzel);
	}

	public boolean exportiereAlleLeistungenDaten() {
		List<String> fachkuerzel = getAlleFachkuerzel();
		return exportiereLeistungenDaten(fachkuerzel);
	}

}
