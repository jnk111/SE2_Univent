package se2.praktikum.projekt.datenimexport;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.SQLIntegrityConstraintViolationException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import se2.praktikum.projekt.datenimexport.backuptables.DBAnmeldetermin;
import se2.praktikum.projekt.datenimexport.backuptables.DBDepartment;
import se2.praktikum.projekt.datenimexport.backuptables.DBEinzelanmeldung;
import se2.praktikum.projekt.datenimexport.backuptables.DBFachbereich;
import se2.praktikum.projekt.datenimexport.backuptables.DBPOVeranZuordung;
import se2.praktikum.projekt.datenimexport.backuptables.DBPruefungsordnung;
import se2.praktikum.projekt.datenimexport.backuptables.DBTeammitglied;
import se2.praktikum.projekt.dbms.DBConnector;
import se2.praktikum.projekt.models.gruppe.Gruppe;
import se2.praktikum.projekt.models.gruppe.Termin;
import se2.praktikum.projekt.models.leistungen.Leistung;
import se2.praktikum.projekt.models.leistungen.Note;
import se2.praktikum.projekt.models.meldungen.Meldung;
import se2.praktikum.projekt.models.meldungen.MeldungTyp;
import se2.praktikum.projekt.models.meldungen.Teameinladung;
import se2.praktikum.projekt.models.meldungen.Teammeldung;
import se2.praktikum.projekt.models.person.IAngestellter;
import se2.praktikum.projekt.models.person.Assistent;
import se2.praktikum.projekt.models.person.Professor;
import se2.praktikum.projekt.models.person.Student;
import se2.praktikum.projekt.models.person.VerwaltMitarbeiter;
import se2.praktikum.projekt.models.person.fachwerte.MAID;
import se2.praktikum.projekt.models.person.fachwerte.MatrikelNr;
import se2.praktikum.projekt.models.team.Team;
import se2.praktikum.projekt.models.veranstaltung.Veranstaltung;
import se2.praktikum.projekt.services.loginservice.ServicePool;
import se2.praktikum.projekt.tools.ErrorLogger;
import se2.praktikum.projekt.tools.Mapper;

public class DatenImportService {
	private ServicePool servicePool;
	private Connection connection;
	
	public DatenImportService(ServicePool servicePool){
		
		this.servicePool = servicePool;
		connect();
		
	}

	private void connect() {
		
		connection = DBConnector.getConnection();
	}

	public boolean backupWiederherstellen(String foldername) {
		
		
		boolean erfolg = servicePool.getDatenExSrv().sichereAlleDaten();
		erfolg &= executeSQL(Folders.SQL_FOLDER + "/delete_data.sql");
		File folder = new File(Folders.BACKUP_FOLDER + "/" + foldername);
		File [] allFiles = folder.listFiles();
		Arrays.sort(allFiles);
		
		for(File entry: allFiles){
			
			System.out.println(entry.getPath());
			System.out.println("=================================================");
			System.out.println();
			try {
				FileReader reader = new FileReader(entry);
				BufferedReader bfreader = new BufferedReader(reader);
				
				String json = bfreader.readLine();
				System.out.println(json);
					
				if(json != null){
					if(json.startsWith(TableNames.DEPARTMENT)){
						erfolg &= insertDepartments(bfreader);
					}else if(json.startsWith(TableNames.FACHBEREICH)){
						erfolg &= insertFachbereiche(bfreader);
					}else if(json.startsWith(TableNames.PROFESSOR)){
						erfolg &= insertProfessoren(bfreader);
					}else if(json.startsWith(TableNames.ASSISTENT)){
						erfolg &= insertAssistenten(bfreader);
					}else if(json.startsWith(TableNames.VERW_MA)){
						erfolg &= insertVerwMitarbeiter(bfreader);
					}else if(json.startsWith(TableNames.STUDENT)){
						erfolg &= insertStudenten(bfreader);
					}else if(json.startsWith(TableNames.VERANSTALTUNG)){
						erfolg &= insertVeranstaltungen(bfreader);
					}else if(json.startsWith(TableNames.ANMELDETERMIN)){
						erfolg &= insertAnmeldetermine(bfreader);
					}else if(json.startsWith(TableNames.GRUPPE)){
						erfolg &= insertGruppen(bfreader);
					}else if(json.startsWith(TableNames.TEAM_MITGLIED)){
							erfolg &= insertTeammitglieder(bfreader);
					}else if(json.startsWith(TableNames.TEAM)){
						erfolg &= insertTeams(bfreader);
					}else if(json.startsWith(TableNames.BEWERTUNG)){
						erfolg &= insertBewertungen(bfreader);
					}else if(json.startsWith(TableNames.PO_VERAN_ZUORDN)){
						erfolg &= insertPOVeranZuOrdn(bfreader);
					}else if(json.startsWith(TableNames.PRUEFORDN)){
						erfolg &= insertPruefungsordnungen(bfreader);
					}else if(json.startsWith(TableNames.MELDUNG)){
						erfolg &= insertMeldungen(bfreader);
					}
					
				}
				bfreader.close();
				reader.close();
				
			} catch (FileNotFoundException e) {
				ErrorLogger.log(e);
				return false;
			} catch (IOException e) {
				ErrorLogger.log(e);
				return false;
			}
			
			
		}
		return erfolg;
	}


	private boolean executeSQL(String filepath) {
		
		
		try {
			FileReader reader = new FileReader(filepath);
			BufferedReader bfreader = new BufferedReader(reader);
			
			String line = null;
			
			while((line = bfreader.readLine()) != null){
				
				String qry = line;
				System.out.println(line);
				PreparedStatement stmt = connection.prepareStatement(qry);
				stmt.executeUpdate();
				stmt.close();
				
			}
			
			bfreader.close();
			reader.close();
			
			return true;
		} catch (FileNotFoundException e) {
			ErrorLogger.log(e);
			return false;
		} catch (IOException e) {
			ErrorLogger.log(e);
			return false;
		} catch (SQLException e) {
			
			ErrorLogger.log(e);
			return false;
		}
		
	}

	public boolean insertEinzelAnmeldungen(BufferedReader bfreader) {
		
		boolean erfolg = false;
		List<DBEinzelanmeldung> einzelanmeldungen = new ArrayList<>();
		DBEinzelanmeldung einzel = null;
		String line = null;
		
		try {
			while((line = bfreader.readLine()) != null){
				
				einzel = Mapper.mapEinzelanmeldung(line);
				einzelanmeldungen.add(einzel);
				
			}
			
			String qry = "insert into Einzelanmeldung values(?, ?, ?)";
			
			for(DBEinzelanmeldung e: einzelanmeldungen){
				PreparedStatement stmt = connection.prepareStatement(qry);
				stmt.setInt(1, e.getMatrNr().getMatrNr());
				stmt.setInt(2, e.getGrpNr());
				stmt.setString(3, e.getFachkuerzel());
				stmt.executeUpdate();				
				stmt.close();
			}
			erfolg = true;
		} catch (IOException e) {
			ErrorLogger.log(e);
			return false;
		} catch (SQLException e) {
			ErrorLogger.log(e);
			return false;
		}
		
		return erfolg;
	}

	public boolean insertVerwMitarbeiter(BufferedReader bfreader) {
		boolean erfolg = false;
		List<VerwaltMitarbeiter> mitarbeiter = new ArrayList<>();
		VerwaltMitarbeiter ma = null;
		String line = null;
		
		try {
			while((line = bfreader.readLine()) != null){
				
				ma = Mapper.mapVerwMitarbeiter(line);
				mitarbeiter.add(ma);
				
			}
			
			String qry = "insert into Verwaltungsmitarbeiter values(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
			
			for(VerwaltMitarbeiter m: mitarbeiter){
				PreparedStatement stmt = connection.prepareStatement(qry);
				stmt.setInt(1, m.getMaID().getId());
				stmt.setString(2, m.getBenutzername());
				stmt.setString(3, m.getVorname());
				stmt.setString(4, m.getNachname());
				stmt.setString(5, m.getPasswort());
				stmt.setString(6, m.getAdresse().getStrasse());
				stmt.setInt(7, m.getAdresse().getHausNr());
				stmt.setString(8, m.getAdresse().getPostleitzahl().getPlz());
				stmt.setString(9, m.getAdresse().getStadt());
				Date gebDatum = m.getGebDatum();
				java.sql.Date gebDatumSQL = null;
				
				if(gebDatum != null){
					gebDatumSQL = new java.sql.Date(gebDatum.getTime());
				}
				stmt.setDate(10, gebDatumSQL);
				stmt.setString(11, m.getGebOrt());
				stmt.setString(12, m.getEMail().getEmail());	
				stmt.setString(13, m.getDepartment());
				stmt.executeUpdate();				
				stmt.close();
			}
			erfolg = true;
		} catch (IOException e) {
			ErrorLogger.log(e);
			return false;
		} catch (SQLException e) {
			ErrorLogger.log(e);
			return false;
		}
		
		return erfolg;
	}

	public boolean insertMeldungen(BufferedReader bfreader) {
		
		boolean erfolg = false;
		List<Meldung> meldungen = new ArrayList<>();
		Meldung meldung = null;
		String line = null;
		
		try {
			while((line = bfreader.readLine()) != null){
				
				if(line.contains(MeldungTyp.TEAMEINL)){
					meldung = Mapper.mapTeameinladung(line);
				}else if(line.contains(MeldungTyp.TEAMAUSTR)){
					meldung = Mapper.mapTeamAustrittsmeldung(line);
					
				}else if(line.contains(MeldungTyp.TEABLEHN)){
					meldung = Mapper.mapTEAblehnung(line);
				}else if(line.contains(MeldungTyp.GRUPPE_VOLL)){
					meldung =  Mapper.mapGruppVollMeldung(line);
				}
				
				meldungen.add(meldung);
				
			}
			
			String qry = "insert into Meldung values(?, ?, ?, ?, ?, ?, ?, ?)";
			
			for(Meldung meld: meldungen){
				
				Student absender = (Student) meld.getAbsender();
				Student empfaenger = (Student) meld.getEmpfaenger();
				PreparedStatement stmt = connection.prepareStatement(qry);
				
				stmt.setInt(1, absender.getMatrNr().getMatrNr());
				stmt.setInt(2, empfaenger.getMatrNr().getMatrNr());
				stmt.setString(3, ((Teammeldung) meld).getTeam().getFachkuerzel());
				stmt.setInt(4, Integer.parseInt(((Teammeldung) meld).getTeam().getTeamID().getId()));
				stmt.setDate(5, new java.sql.Date(meld.getVersandDatum().getTime()));
				
				if(meld.getTyp().equals(MeldungTyp.TEAMEINL)
						&& meld instanceof Teameinladung){
					Teameinladung einl = (Teameinladung) meld;
					stmt.setString(6, "" + einl.isBestaetigt());
					
					java.sql.Date bestaetDatumSQL = null;
					Date bestaetDate = einl.getBestaetDatum();
					
					if(bestaetDate != null){
						bestaetDatumSQL = new java.sql.Date(bestaetDate.getTime());
					}
					
					stmt.setDate(7, bestaetDatumSQL);
				}else{
					
					stmt.setString(6, "false");
					stmt.setDate(7, null);
				}
				
				stmt.setString(8, meld.getTyp());
				stmt.executeUpdate();				
				stmt.close();
			}
			erfolg = true;
		} catch (IOException e) {
			ErrorLogger.log(e);
			return false;
		} catch (SQLException e) {
			ErrorLogger.log(e);
			return false;
		}
		
		return erfolg;
		
	}

	public boolean insertPruefungsordnungen(BufferedReader bfreader) {

		boolean erfolg = false;
		List<DBPruefungsordnung> pruefOrdnungen = new ArrayList<>();
		DBPruefungsordnung pruefOrd = null;
		String line = null;
		
		try {
			while((line = bfreader.readLine()) != null){
				
				pruefOrd = Mapper.mapPruefOrdnung(line);
				pruefOrdnungen.add(pruefOrd);
				
			}
			
			String qry = "insert into Pruefungsordnung values(?, ?)";
			
			for(DBPruefungsordnung po: pruefOrdnungen){
				PreparedStatement stmt = connection.prepareStatement(qry);
				stmt.setString(1, po.getPoKuerzel());
				stmt.setString(2, po.getPoBezeichnung());
				stmt.executeUpdate();				
				stmt.close();
			}
			erfolg = true;
		} catch (IOException e) {
			ErrorLogger.log(e);
			return false;
		} catch (SQLException e) {
			ErrorLogger.log(e);
			return false;
		}
		
		return erfolg;
	}

	public boolean insertPOVeranZuOrdn(BufferedReader bfreader) {
		
		boolean erfolg = false;
		List<DBPOVeranZuordung> povzuordnungen = new ArrayList<>();
		DBPOVeranZuordung poveran = null;
		String line = null;
		
		try {
			while((line = bfreader.readLine()) != null){
				
				poveran = Mapper.mapPOVeranZuOrdn(line);
				povzuordnungen.add(poveran);
				
			}
			
			String qry = "insert into POVeranstaltungszuordnung values(?, ?)";
			
			for(DBPOVeranZuordung pover: povzuordnungen){
				PreparedStatement stmt = connection.prepareStatement(qry);
				stmt.setString(1, pover.getPoKuerzel());
				stmt.setString(2, pover.getfKuerzel());
				stmt.executeUpdate();				
				stmt.close();
			}
			erfolg = true;
		} catch (IOException e) {
			ErrorLogger.log(e);
			return false;
		} catch (SQLException e) {
			ErrorLogger.log(e);
			return false;
		}
		
		return erfolg;
	}

	public boolean insertBewertungen(BufferedReader bfreader) {
		
		boolean erfolg = false;
		List<Leistung> leistungen = new ArrayList<>();
		Leistung leistung = null;
		String line = null;
		
		try {
			while((line = bfreader.readLine()) != null){
				
				leistung = Mapper.mapLeistung(line);
				leistungen.add(leistung);
				
			}
			
			String qry = "insert into Bewertung values(?, ?, ?, ?, ?, ?, ?)";
			
			for(Leistung leist: leistungen){
				PreparedStatement stmt = connection.prepareStatement(qry);
				stmt.setInt(1, leist.getProfessor().getMaID().getId());
				stmt.setInt(2, leist.getStudent().getMatrNr().getMatrNr());
				stmt.setString(3, leist.getFach().getFachKuerzel());
				Date datePVL = leist.getDatumPVL();
				Date dateNote = leist.getDatumNote();
				java.sql.Date datePVLSQL = null;
				java.sql.Date dateNoteSQL = null;
				if(datePVL != null){
					
					datePVLSQL = new java.sql.Date(datePVL.getTime());
				}
				
				if(dateNote != null){
					dateNoteSQL = new java.sql.Date(dateNote.getTime());
					
				}
				
				Note note = leist.getNote();
				stmt.setDate(4, datePVLSQL);
				stmt.setDate(5, dateNoteSQL);
				stmt.setString(6, "" + leist.isPvl());
				
				if(note != null){
					stmt.setString(7, note.getNote());
				}else{
					stmt.setString(7, null);
				}
				
				stmt.executeUpdate();				
				stmt.close();
			}
			erfolg = true;
		} catch (IOException e) {
			ErrorLogger.log(e);
			return false;
		} catch (SQLException e) {
			ErrorLogger.log(e);
			return false;
		}
		
		return erfolg;
	}

	public boolean insertStudenten(BufferedReader bfreader) {
		boolean erfolg = false;
		List<Student> studenten = new ArrayList<>();
		Student student = null;
		String line = null;
		
		try {
			while((line = bfreader.readLine()) != null){
				
				student = Mapper.mapStudent(line);
				studenten.add(student);
				
			}
			
			String qry = "insert into Student values(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
			
			for(Student stud: studenten){
				PreparedStatement stmt = connection.prepareStatement(qry);
				stmt.setInt(1, stud.getMatrNr().getMatrNr());
				stmt.setString(2, stud.getBenutzername());
				stmt.setString(3, stud.getVorname());
				stmt.setString(4, stud.getNachname());
				stmt.setString(5, stud.getPasswort());
				stmt.setString(6, stud.getPo());
				stmt.setString(7, stud.getAdresse().getStrasse());
				stmt.setInt(8, stud.getAdresse().getHausNr());
				stmt.setString(9, stud.getAdresse().getPostleitzahl().getPlz());
				stmt.setString(10, stud.getAdresse().getStadt());
				Date gebDatum = stud.getGebDatum();
				java.sql.Date gebDatumSQL = null;
				
				if(gebDatum != null){
					gebDatumSQL = new java.sql.Date(gebDatum.getTime());
				}
				stmt.setDate(11, gebDatumSQL);
				stmt.setString(12, stud.getGebOrt());
				stmt.setString(13, stud.getEMail().getEmail());
				stmt.setString(14, stud.getFachbereich());
				stmt.setString(15, stud.getDepartment());
				
				stmt.executeUpdate();				
				stmt.close();
			}
			erfolg = true;
		} catch (IOException e) {
			ErrorLogger.log(e);
			return false;
		} catch (SQLException e) {
			ErrorLogger.log(e);
			return false;
		}
		
		return erfolg;
	}

	public boolean insertTeammitglieder(BufferedReader bfreader) {

		boolean erfolg = false;
		List<DBTeammitglied> teammitglieder = new ArrayList<>();
		DBTeammitglied teammg = null;
		String line = null;
		
		try {
			while((line = bfreader.readLine()) != null){
				
				teammg = Mapper.mapTeammitglied(line);
				teammitglieder.add(teammg);
				
			}
			
			String qry = "insert into Teammitglied values(?, ?, ?, ?)";
			
			for(DBTeammitglied mg: teammitglieder){
				PreparedStatement stmt = connection.prepareStatement(qry);
				stmt.setInt(1, Integer.parseInt(mg.getTeamID().getId()));
				stmt.setInt(2, mg.getMatrNr().getMatrNr());
				stmt.setString(3, mg.getBestaetigt());
				stmt.setString(4, mg.getEinzel());
				stmt.executeUpdate();
				stmt.close();
			}
			erfolg = true;
		} catch (IOException e) {
			ErrorLogger.log(e);
			return false;
		} catch (SQLException e) {
			ErrorLogger.log(e);
			return false;
		}
		
		return erfolg;
		
	}

	public boolean insertTeams(BufferedReader bfreader) {
		boolean erfolg = false;
		List<Team> teams = new ArrayList<>();
		Team team = null;
		String line = null;
		
		try {
			while((line = bfreader.readLine()) != null){
				
				team = Mapper.mapTeam(line);
				teams.add(team);
				
			}
			
			for(Team tm: teams){
				System.out.println(tm);
			}
			
			String qry = "insert into Team values(?, ?, ?, ?, ?, ?)";
			
			for(Team t: teams){
				PreparedStatement stmt = connection.prepareStatement(qry);
				stmt.setInt(1, Integer.parseInt(t.getTeamID().getId()));
				stmt.setString(2, t.getFachkuerzel());
				stmt.setInt(3, t.getMinTeiln());
				stmt.setInt(4, t.getMaxTeiln());
				int grpNr = t.getGrpNr();
				
				if(grpNr == -1){
					stmt.setNull(5, java.sql.Types.INTEGER);
				}else{
					stmt.setInt(5, grpNr);
				}
				stmt.setInt(6, t.getVorgGrpNr());
				stmt.executeUpdate();
				stmt.close();
			}
			erfolg = true;
		} catch (IOException e) {
			ErrorLogger.log(e);
			return false;
		} catch (SQLException e) {
			
			ErrorLogger.log(e);
			return false;
		}
		
		return erfolg;
	}


	public boolean insertGruppen(BufferedReader bfreader) {
		boolean erfolg = false;
		List<Gruppe> gruppen = new ArrayList<>();
		Gruppe gruppe = null;
		String line = null;
		
		try {
			while((line = bfreader.readLine()) != null){
				
				gruppe = Mapper.mapGruppe(line);
				gruppen.add(gruppe);
				
			}
			
			String qry = "insert into Gruppe values(?, ?, ?, ?, ?)";
			
			for(Gruppe gr: gruppen){
				PreparedStatement stmt = connection.prepareStatement(qry);
				stmt.setInt(1, gr.getGrpNr());
				stmt.setString(2, gr.getFachkuerzel());
				stmt.setInt(3, gr.getMaxTeams());
				stmt.setInt(4, gr.getProfessor().getMaID().getId());
				stmt.setInt(5, gr.getAssistent().getMaID().getId());
				stmt.executeUpdate();
				
				int tIdIndex = 1;
				
				for(Termin term: gr.getTermine()){
					
					erfolg = insertGruppenTermin(term, gr, tIdIndex);
					++tIdIndex;
				}
				
				stmt.close();
			}
			erfolg = true;
		} catch (IOException e) {
			ErrorLogger.log(e);
			return false;
		} catch (SQLException e) {
			ErrorLogger.log(e);
			return false;
		}
		
		return erfolg;
	}

	public boolean insertGruppenTermin(Termin term, Gruppe gr, int tIdIndex) {
		boolean erfolg = false;
		String qry = "insert into Termin values(?, ?, ?, ?, ?, ?, ?)";
		String terminID = gr.getFachkuerzel() + "G" + gr.getGrpNr() + "T" + tIdIndex; 
		try {
			PreparedStatement stmt = connection.prepareStatement(qry);
			stmt.setString(1, terminID);
			stmt.setString(2, gr.getFachkuerzel());
			stmt.setInt(3, gr.getGrpNr());
			stmt.setDate(4, new java.sql.Date(term.getDatum().getTime()));
			stmt.setString(5, term.getStart().toString());
			stmt.setString(6, term.getEnde().toString());
			stmt.setString(7, term.getRaum());
			stmt.executeUpdate();
			stmt.close();
			erfolg = true;
			
		} catch (SQLException e) {
			
			ErrorLogger.log(e);
			return false;
		}
		
		return erfolg;
		
	}

	public boolean insertAnmeldetermine(BufferedReader bfreader) {
		boolean erfolg = false;
		List<DBAnmeldetermin> anmTermine = new ArrayList<>();
		DBAnmeldetermin termin = null;
		String line = null;
		
		try {
			while((line = bfreader.readLine()) != null){
				
				termin = Mapper.mapAnmeldetermin(line);
				anmTermine.add(termin);
				
			}
			
			String qry = "insert into Anmeldetermin values(?, ?, ?, ?, ?, ?)";
			
			for(DBAnmeldetermin term: anmTermine){
				PreparedStatement stmt = connection.prepareStatement(qry);
				stmt.setString(1, term.getvTyp());
				stmt.setString(2, term.getaTyp());
				stmt.setDate(3, new java.sql.Date(term.getdStart().getTime()));
				stmt.setDate(4, new java.sql.Date(term.getdEnde().getTime()));
				stmt.setString(5, term.getUzStart().toString());
				stmt.setString(6, term.getUzEnde().toString());
				stmt.executeUpdate();
				stmt.close();
			}
			erfolg = true;
		} catch (IOException e) {
			ErrorLogger.log(e);
			return false;
		} catch (SQLException e) {
			ErrorLogger.log(e);
			return false;
		}
		
		return erfolg;
	}


	public boolean insertVeranstaltungen(BufferedReader bfreader) {
		boolean erfolg = false;
		List<Veranstaltung> veranstaltungen = new ArrayList<>();
		String line = null;
		Veranstaltung veranstaltung = null;
		
		try {
			while((line = bfreader.readLine()) != null){
				
				if(line.contains("Vorlesung")){
					veranstaltung = Mapper.mapPaktikum(line);
					
				}else if(line.contains("WP")){
					veranstaltung = Mapper.mapWP(line);
				}else if(line.contains("WPP")){
					veranstaltung = Mapper.mapWP(line);
				}else if(line.contains("PO")){
					veranstaltung = Mapper.mapPO(line);
				}else{
					veranstaltung = Mapper.mapPaktikum(line);
				}
				
				veranstaltungen.add(veranstaltung);
				
			}
			
			String qry = "insert into Veranstaltung values(?, ?, ?, ?, ?, ?, ?, ?)";
			
			for(Veranstaltung va: veranstaltungen){
				PreparedStatement stmt = connection.prepareStatement(qry);
				stmt.setString(1, va.getFach().getFachKuerzel());
				stmt.setString(2, va.getFach().getfBKuerzel());
				stmt.setString(3, va.getFach().getFachBezeichnung());
				stmt.setInt(4, va.getSemester());
				stmt.setString(5, va.getTyp());
				stmt.setInt(6, va.getMinTeilnTeam());
				stmt.setInt(7, va.getMaxTeilnTeam());
				stmt.setInt(8, va.getProfessor().getMaID().getId());
				
				stmt.executeUpdate();
				stmt.close();
			}
			erfolg = true;
		} catch (IOException e) {
			ErrorLogger.log(e);
			return false;
		} catch (SQLException e) {
			ErrorLogger.log(e);
			return false;
		}
		
		return erfolg;
	}

	public boolean insertAssistenten(BufferedReader bfreader) {
		
		boolean erfolg = false;
		List<Assistent> assitenten = new ArrayList<>();
		String line = null;
		Assistent assist = null;
		
		try {
			while((line = bfreader.readLine()) != null){
				
				assist = Mapper.mapAssistent(line);
				assitenten.add(assist);
				
			}
			
			String qry = "insert into Assistent values(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
			
			for(Assistent a: assitenten){
				PreparedStatement stmt = connection.prepareStatement(qry);
				stmt.setInt(1, a.getMaID().getId());
				stmt.setString(2, a.getBenutzername());
				stmt.setString(3, a.getVorname());
				stmt.setString(4, a.getNachname());
				stmt.setString(5, a.getPasswort());
				stmt.setString(6, a.getAdresse().getStrasse());
				stmt.setInt(7, a.getAdresse().getHausNr());
				stmt.setString(8, a.getAdresse().getPostleitzahl().getPlz());
				stmt.setString(9, a.getAdresse().getStadt());
				
				if(a.getGebDatum() != null){
					
					stmt.setDate(10, new java.sql.Date(a.getGebDatum().getTime()));
				}else{
					stmt.setDate(10, null);
				}
				
				stmt.setString(11, a.getGebOrt());
				stmt.setString(12, a.getEMail().getEmail());
				stmt.setString(13, a.getDepartment());
				
				stmt.executeUpdate();
				stmt.close();
			}
			erfolg = true;
		} catch (IOException e) {
			ErrorLogger.log(e);
			return false;
		} catch (SQLException e) {
			ErrorLogger.log(e);
			return false;
		}
		
		return erfolg;
	}

	public boolean insertProfessoren(BufferedReader bfreader) {
		
		boolean erfolg = false;
		List<Professor> professoren = new ArrayList<>();
		String line = null;
		Professor prof = null;
		
		try {
			while((line = bfreader.readLine()) != null){
				
				prof = Mapper.mapProfessor(line);
				professoren.add(prof);
				
			}
			
			String qry = "insert into Professor values(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
			
			for(Professor p: professoren){
				PreparedStatement stmt = connection.prepareStatement(qry);
				stmt.setInt(1, p.getMaID().getId());
				stmt.setString(2, p.getBenutzername());
				stmt.setString(3, p.getVorname());
				stmt.setString(4, p.getNachname());
				stmt.setString(5, "Professor Dr.");
				stmt.setString(6, p.getBenutzername());
				stmt.setString(7, p.getAdresse().getStrasse());
				stmt.setInt(8, p.getAdresse().getHausNr());
				stmt.setString(9, p.getAdresse().getPostleitzahl().getPlz());
				stmt.setString(10, p.getAdresse().getStadt());
				
				if(p.getGebDatum() != null){
					
					stmt.setDate(11, new java.sql.Date(p.getGebDatum().getTime()));
				}else{
					stmt.setDate(11, null);
				}
				
				stmt.setString(12, p.getGebOrt());
				stmt.setString(13, p.getEMail().getEmail());
				stmt.setString(14, p.getDepartment());
				
				stmt.executeUpdate();
				stmt.close();
			}
			erfolg = true;
		} catch (IOException e) {
			ErrorLogger.log(e);
			return false;
		} catch (SQLException e) {
			ErrorLogger.log(e);
			return false;
		}
		
		return erfolg;
	}

	public boolean insertFachbereiche(BufferedReader bfreader) {
		boolean erfolg = false;
		List<DBFachbereich> dbFachbereiche = new ArrayList<>();
		String line = null;
		DBFachbereich dbFachbereich = null;
		
		try {
			while((line = bfreader.readLine()) != null){
				
				dbFachbereich = Mapper.mapDBFachbereich(line);
				dbFachbereiche.add(dbFachbereich);
				
			}
			
			String qry = "insert into fachbereich values(?, ?, ?)";
			
			for(DBFachbereich dbfb: dbFachbereiche){
				PreparedStatement stmt = connection.prepareStatement(qry);
				stmt.setString(1, dbfb.getFbKuerzel());
				stmt.setString(2, dbfb.getFbBezeichnung());
				stmt.setString(3, dbfb.getdKuerzel());
				stmt.executeUpdate();
				stmt.close();
			}
			erfolg = true;
		} catch (IOException e) {
			ErrorLogger.log(e);
			return false;
		} catch (SQLException e) {
			ErrorLogger.log(e);
			return false;
		}
		
		return erfolg;
		
	}

	public boolean insertDepartments(BufferedReader bfreader) {
		
		boolean erfolg = false;
		List<DBDepartment> dbDepartments = new ArrayList<>();
		String line = null;
		DBDepartment dbDepartment = null;
		
		try {
			while((line = bfreader.readLine()) != null){
				
				dbDepartment = Mapper.mapDBDepartment(line);
				dbDepartments.add(dbDepartment);
				
			}
			
			String qry = "insert into department values(?, ?)";
			
			for(DBDepartment dbdp: dbDepartments){
				PreparedStatement stmt = connection.prepareStatement(qry);
				stmt.setString(1, dbdp.getdKuerzel());
				stmt.setString(2, dbdp.getdBezeichnung());
				stmt.executeUpdate();
				stmt.close();
			}
			erfolg = true;
		} catch (IOException e) {
			ErrorLogger.log(e);
			return false;
		} catch (SQLException e) {
			ErrorLogger.log(e);
			return false;
		}
		
		return erfolg;
	}

	public List<String> holeBackupDateien() {
		final File folder = new File(Folders.BACKUP_FOLDER);
		List<String> folders = listeDateienAuf(folder);
		
		return folders;
	}
	
	public List<String> listeDateienAuf(final File folder) {
		File [] directories = folder.listFiles(File::isDirectory);
		List<String> filenames = new ArrayList<>();
		for(File f: directories){
			
			filenames.add(f.getName());
		}
		
		return filenames;
	}
	
	public List<String> listeDateienAufImp(final File folder) {
		File [] directories = folder.listFiles();
		List<String> filenames = new ArrayList<>();
		for(File f: directories){
			
			filenames.add(f.getName());
		}
		
		return filenames;
	}


	public boolean importiereStudenten(String filename) {
		
		File file = new File(Folders.IMP_FOLDER + "/" + filename);
		
		boolean erfolg = false;
		List<Student> studenten = new ArrayList<>();
		String line = null;
		Student student = null;
		
		try {
			
			FileReader reader = new FileReader(file);
			@SuppressWarnings("resource")
			BufferedReader bfreader = new BufferedReader(reader);
			line = bfreader.readLine();
			while((line = bfreader.readLine()) != null){
				
				student = Mapper.mapStudent(line);
				
				if(student != null){
					studenten.add(student);
				}else{
					return false;
				}
			}
			
			reader.close();
			bfreader.close();
			
			String qry = "insert into Student values(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
			
			for(Student stud: studenten){
				
				if(!studentenEintragBereitsEnthalten(stud)){
					
					MatrikelNr matrNr = getNeueMatrikelNr(stud);
					String username = getNeuenUsername(stud);
					if(matrNr != null){
						stud.setMatrNr(matrNr);
					}
					
					if(username != null){
						stud.setBenutzername(username);
					}
					
					PreparedStatement stmt = connection.prepareStatement(qry);
					stmt.setInt(1, stud.getMatrNr().getMatrNr());
					stmt.setString(2, stud.getBenutzername());
					stmt.setString(3, stud.getVorname());
					stmt.setString(4, stud.getNachname());
					stmt.setString(5, stud.getPasswort());
					stmt.setString(6, stud.getPo());
					stmt.setString(7, stud.getAdresse().getStrasse());
					stmt.setInt(8, stud.getAdresse().getHausNr());
					stmt.setString(9, stud.getAdresse().getPostleitzahl().getPlz());
					stmt.setString(10, stud.getAdresse().getStadt());
					Date gebDatum = stud.getGebDatum();
					java.sql.Date gebDatumSQL = null;
					
					if(gebDatum != null){
						gebDatumSQL = new java.sql.Date(gebDatum.getTime());
					}
					stmt.setDate(11, gebDatumSQL);
					stmt.setString(12, stud.getGebOrt());
					stmt.setString(13, stud.getEMail().getEmail());
					stmt.setString(14, stud.getFachbereich());
					stmt.setString(15, stud.getDepartment());
					
					stmt.executeUpdate();				
					stmt.close();
				}

			}
			erfolg = true;
		} catch (IOException e) {
			ErrorLogger.log(e);
			return false;
		} catch (SQLException e) {
			ErrorLogger.log(e);
			return false;
		}
		
		return erfolg;
		
	}

	private String getNeuenUsername(Student stud) throws SQLException {
		
		List<String> usernames = new ArrayList<>();
		String qry = "select username from Student where not matrnr = 99999999";
		PreparedStatement stmt = connection.prepareStatement(qry);
		ResultSet rs = stmt.executeQuery();
		
		while(rs.next()){
			
			String user = rs.getString("username");
			usernames.add(user);
			
		}
		
		if(usernames.contains(stud.getBenutzername())){
			
			int maxUsername = Integer.MIN_VALUE;
			
			for(String usern: usernames){
				
				int nr = Integer.parseInt(usern.replaceAll("[\\D]", ""));
				
				if(nr > maxUsername){
					maxUsername = nr;
				}
			}
			
			String usernameNeu = "aaa";
			
			if(maxUsername < 10){
				usernameNeu += "00";
			}else if(maxUsername < 100){
				
				usernameNeu += "0";
				
			}
			usernameNeu += (++maxUsername);
			return usernameNeu;
			
		}
		
		return null;
		
	}
	

	private String getNeuenUsername(IAngestellter p, String tablename) throws SQLException {

		List<String> usernames = new ArrayList<>();
		String qry = "select username from " + tablename;
		PreparedStatement stmt = connection.prepareStatement(qry);
		ResultSet rs = stmt.executeQuery();
		
		while(rs.next()){
			
			String user = rs.getString("username");
			usernames.add(user);
			
		}
		int maxUsername = Integer.MIN_VALUE;
			
		for(String usern: usernames){
				
			int nr = Integer.parseInt(usern.replaceAll("[\\D]", ""));
				
			if(nr > maxUsername){
					maxUsername = nr;
			}
		}
			
		String usernameNeu = "oaa";
			
		if(maxUsername < 10){
			usernameNeu += "00";
		}
		else if(maxUsername < 100){
				
			usernameNeu += "0";
				
		}
		usernameNeu += (++maxUsername);
		return usernameNeu;
	}

	private MatrikelNr getNeueMatrikelNr(Student stud) throws SQLException {
		
		List<MatrikelNr> matrNummern = new ArrayList<>();
		String qry = "select matrnr from Student where not matrNr = 99999999";
		PreparedStatement stmt = connection.prepareStatement(qry);
		ResultSet rs = stmt.executeQuery();
		
		while(rs.next()){
			
			MatrikelNr matrNr = MatrikelNr.getMatrikelNr(rs.getInt("matrnr"));
			matrNummern.add(matrNr);
		}
		
		stmt.close();
		
		if(matrNummern.contains(stud.getMatrNr())){
			
			int maxMatrNr = Integer.MIN_VALUE;
			
			for(MatrikelNr matrNr: matrNummern){
				
				int nr = matrNr.getMatrNr();
				if(nr > maxMatrNr){
					maxMatrNr = nr;
				}
			}
			
			return MatrikelNr.getMatrikelNr(++maxMatrNr);
		}
		
		return null;
	}
	
	
	private MAID getNeueMaid(IAngestellter p) throws SQLException {

		List<MAID> maids = new ArrayList<>();
		String qry = "select p.maid as maid1, a.maid as maid2, v.maid as maid3 from Professor p, Assistent a, Verwaltungsmitarbeiter v";
		
		PreparedStatement stmt = connection.prepareStatement(qry);
		ResultSet rs = stmt.executeQuery();
		
		while(rs.next()){
			
			MAID maid1 = MAID.getMAID(rs.getInt("maid1"));
			MAID maid2 = MAID.getMAID(rs.getInt("maid2"));
			MAID maid3 = MAID.getMAID(rs.getInt("maid3"));
			maids.add(maid1);
			maids.add(maid2);
			maids.add(maid3);
		}
		
		stmt.close();
		
		int maxMaid = Integer.MIN_VALUE;
		
		for(MAID maid: maids){
			
			int nr = maid.getId();
			if(nr > maxMaid){
				maxMaid = nr;
			}
		}
		
		return MAID.getMAID(++maxMaid);
	}

	private boolean studentenEintragBereitsEnthalten(Student stud) throws SQLException {
		
		String qry = "select count(*) as count from Student where matrNr = ? and vorname = ? and nachname = ?";
		PreparedStatement stmt = connection.prepareStatement(qry);
		stmt.setInt(1, stud.getMatrNr().getMatrNr());
		stmt.setString(2, stud.getVorname());
		stmt.setString(3, stud.getNachname());
		ResultSet rs = stmt.executeQuery();
		int count = 0;
		while(rs.next()){
			count = rs.getInt("count");
		}
		stmt.close();
		
		return count > 0;
	}
	
	private boolean veranstaltungBereitsVorhanden(Veranstaltung va) throws SQLException {
		
		String qry = "select count(*) as count from Veranstaltung where semester = ? and fbezeichnung = ?";
		PreparedStatement stmt = connection.prepareStatement(qry);
		stmt.setInt(1, va.getFach().getSemester());
		stmt.setString(2, va.getFach().getFachBezeichnung());
		ResultSet rs = stmt.executeQuery();
		int count = 0;
		
		while(rs.next()){
			count = rs.getInt("count");
		}
		stmt.close();
		
		return count > 0;
		
		
	}
	
	private boolean angestelltenEintragIstBereitsEnthalten(IAngestellter a, String tablename) throws SQLException {
		
		
		if(a == null){
			return false;
		}
		
		Date gebDatum = getGebDatum(a, tablename);
		String gebOrt = getGebort(a, tablename);
		
		String qry = "select count(*) as count from " + tablename + " where vorname = ? and nachname = ? and gebDatum = ? and gebOrt = ?";
		PreparedStatement stmt = connection.prepareStatement(qry);
		stmt.setString(1, a.getVorname());
		stmt.setString(2, a.getNachname());
		
		if(gebDatum != null){
			stmt.setDate(3, new java.sql.Date(gebDatum.getTime()));
		}else{
			stmt.setDate(3, null);
		}
		
		if(gebOrt != null){
			stmt.setString(4, gebOrt);
		}else{
			stmt.setString(4, null);
		}
		
		ResultSet rs = stmt.executeQuery();
		int count = 0;
		
		while(rs.next()){
			count = rs.getInt("count");
		}
		stmt.close();
		
		return count > 0;
	}
	
	
	

	private String getGebort(IAngestellter a, String tablename) throws SQLException {
		String qry = "select gebOrt from " + tablename + " where maid = ?";
		PreparedStatement stmt = connection.prepareStatement(qry);
		stmt.setInt(1, a.getMaID().getId());
		ResultSet rs = stmt.executeQuery();
		String gebOrt = null;
		while(rs.next()){
			
			gebOrt = rs.getString("gebOrt");
			
		}
		
		stmt.close();
		return gebOrt;
	}

	private Date getGebDatum(IAngestellter a, String tablename) throws SQLException {
		
		String qry = "select gebDatum from " + tablename + " where maid = ?";
		PreparedStatement stmt = connection.prepareStatement(qry);
		stmt.setInt(1, a.getMaID().getId());
		ResultSet rs = stmt.executeQuery();
		Date gebDatum = null;
		while(rs.next()){
			
			gebDatum = new Date(rs.getDate("gebDatum").getTime());
			
		}
		
		stmt.close();
		return gebDatum;
		
		
		
		
	}

	public boolean importiereProfessoren(String filename) {
		
		boolean erfolg = false;
		File file = new File(Folders.IMP_FOLDER + "/" + filename);
		List<Professor> professoren = new ArrayList<>();
		String line = null;
		Professor prof = null;
		
		try {
			
			FileReader reader = new FileReader(file);
			@SuppressWarnings("resource")
			BufferedReader bfreader = new BufferedReader(reader);
			line = bfreader.readLine();
			while((line = bfreader.readLine()) != null){
				
				prof = Mapper.mapProfessor(line);
				
				if(prof != null){
					professoren.add(prof);
				}else{
					return false;
				}
			}
			
			reader.close();
			bfreader.close();
			
			String qry = "insert into Professor values(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
			
			
			for(Professor p: professoren){
				
				if(!angestelltenEintragIstBereitsEnthalten(p, "Professor")){
					
					MAID maid = getNeueMaid(p);
					String username = getNeuenUsername(p, "Professor");
					
					p.setMaID(maid);
					p.setBenutzername(username);
					PreparedStatement stmt = connection.prepareStatement(qry);
					stmt.setInt(1, p.getMaID().getId());
					stmt.setString(2, p.getBenutzername());
					stmt.setString(3, p.getVorname());
					stmt.setString(4, p.getNachname());
					stmt.setString(5, "Professor Dr.");
					stmt.setString(6, p.getBenutzername());
					stmt.setString(7, p.getAdresse().getStrasse());
					stmt.setInt(8, p.getAdresse().getHausNr());
					stmt.setString(9, p.getAdresse().getPostleitzahl().getPlz());
					stmt.setString(10, p.getAdresse().getStadt());
					
					if(p.getGebDatum() != null){
						
						stmt.setDate(11, new java.sql.Date(p.getGebDatum().getTime()));
					}else{
						stmt.setDate(11, null);
					}
					
					stmt.setString(12, p.getGebOrt());
					stmt.setString(13, p.getEMail().getEmail());
					stmt.setString(14, p.getDepartment());
					
					stmt.executeUpdate();
					stmt.close();
					
				}
			}
			erfolg = true;
		} catch (IOException e) {
			ErrorLogger.log(e);
			return false;
		} catch (SQLException e) {
			ErrorLogger.log(e);
			return false;
		}
		
		return erfolg;
	}
	
	
	public boolean importiereAssistenten(String filename) {
		
		boolean erfolg = false;
		File file = new File(Folders.IMP_FOLDER + "/" + filename);
		List<Assistent> assistenten = new ArrayList<>();
		String line = null;
		Assistent assist = null;
		
		try {
			
			FileReader reader = new FileReader(file);
			@SuppressWarnings("resource")
			BufferedReader bfreader = new BufferedReader(reader);
			line = bfreader.readLine();
			while((line = bfreader.readLine()) != null){
				
				assist = Mapper.mapAssistent(line);
				
				if(assist!= null){
					assistenten.add(assist);
				}else{
					return false;
				}
			}
			
			reader.close();
			bfreader.close();
			
			String qry = "insert into Assistent values(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
			
			
			for(Assistent a: assistenten){
				
				if(!angestelltenEintragIstBereitsEnthalten(a, "Assistent")){
					
					MAID maid = getNeueMaid(a);
					String username = getNeuenUsername(a, "Assistent");
					
					a.setMaID(maid);
					a.setBenutzername(username);
					PreparedStatement stmt = connection.prepareStatement(qry);
					stmt.setInt(1, a.getMaID().getId());
					stmt.setString(2, a.getBenutzername());
					stmt.setString(3, a.getVorname());
					stmt.setString(4, a.getNachname());
					stmt.setString(5, a.getPasswort());
					stmt.setString(6, a.getAdresse().getStrasse());
					stmt.setInt(7, a.getAdresse().getHausNr());
					stmt.setString(8, a.getAdresse().getPostleitzahl().getPlz());
					stmt.setString(9, a.getAdresse().getStadt());
					
					if(a.getGebDatum() != null){
						
						stmt.setDate(10, new java.sql.Date(a.getGebDatum().getTime()));
					}else{
						stmt.setDate(10, null);
					}
					
					stmt.setString(11, a.getGebOrt());
					stmt.setString(12, a.getEMail().getEmail());
					stmt.setString(13, a.getDepartment());
					
					stmt.executeUpdate();
					stmt.close();
					
				}
			}
			erfolg = true;
		} catch (IOException e) {
			ErrorLogger.log(e);
			return false;
		} catch (SQLException e) {
			ErrorLogger.log(e);
			return false;
		}
		
		return erfolg;
	}

	public boolean importiereVABasisDaten(String filename) {
		boolean erfolg = false;
		File file = new File(Folders.IMP_FOLDER + "/" + filename);
		List<Veranstaltung> veranstaltungen = new ArrayList<>();
		String line = null;
		Veranstaltung veranstaltung = null;
		
		try {
			
			FileReader reader = new FileReader(file);
			@SuppressWarnings("resource")
			BufferedReader bfreader = new BufferedReader(reader);
			line = bfreader.readLine();
			while((line = bfreader.readLine()) != null){
				
				if(line.contains("Vorlesung")){
					veranstaltung = Mapper.mapPaktikum(line);
					
				}else if(line.contains("WP")){
					veranstaltung = Mapper.mapWP(line);
				}else if(line.contains("WPP")){
					veranstaltung = Mapper.mapWP(line);
				}else if(line.contains("PO")){
					veranstaltung = Mapper.mapPO(line);
				}else{
					veranstaltung = Mapper.mapPaktikum(line);
				}
				
				veranstaltungen.add(veranstaltung);
				
			}
			
			String qry = "insert into Veranstaltung values(?, ?, ?, ?, ?, ?, ?, ?)";
			
			for(Veranstaltung va: veranstaltungen){
				
				if(!veranstaltungBereitsVorhanden(va)){
					
					Professor prof = (Professor) va.getProfessor();
					
					if(!angestelltenEintragIstBereitsEnthalten(prof, "Professor")){
						return false;
					}
					
					String fachkuerzel = getNeuesFachkuerzel(va);
					
					if(fachkuerzel != null){
						
						va.getFach().setFachKuerzel(fachkuerzel);
					}
					
					PreparedStatement stmt = connection.prepareStatement(qry);
					stmt.setString(1, va.getFach().getFachKuerzel());
					stmt.setString(2, va.getFach().getfBKuerzel());
					stmt.setString(3, va.getFach().getFachBezeichnung());
					stmt.setInt(4, va.getSemester());
					stmt.setString(5, va.getTyp());
					stmt.setInt(6, va.getMinTeilnTeam());
					stmt.setInt(7, va.getMaxTeilnTeam());
					stmt.setInt(8, va.getProfessor().getMaID().getId());
					
					stmt.executeUpdate();
					stmt.close();
				}
			}
			erfolg = ordneVAsPOZu(veranstaltungen);
		} catch (IOException e) {
			ErrorLogger.log(e);
			return false;
		} catch (SQLException e) {
			ErrorLogger.log(e);
			return false;
		}
		
		return erfolg;
	}


	private boolean ordneVAsPOZu(List<Veranstaltung> veranstaltungen) {
		
		for(Veranstaltung va: veranstaltungen){
			String qry = "insert into POVeranstaltungszuordnung values (?, ?)";
			try {
				PreparedStatement stmt = connection.prepareStatement(qry);
				stmt.setString(1, "IPO2014");  // TODO: getNeuestePO();
				stmt.setString(2, va.getFach().getFachKuerzel());
				stmt.executeUpdate();
				stmt.close();
			} catch (SQLException e) {
				ErrorLogger.log(e);
				return false;
			}
		}

		return true;
		
	}

	private String getNeuesFachkuerzel(Veranstaltung va) throws SQLException {
		
		List<String> fachkuerzel = new ArrayList<>();
		String qry = "select fkuerzel from Veranstaltung";
		PreparedStatement stmt = connection.prepareStatement(qry);
		ResultSet rs = stmt.executeQuery();
		
		while(rs.next()){
			
			String fk = rs.getString("fkuerzel");
			fachkuerzel.add(fk);
		}
		
		String fkNeu = va.getFach().getFachKuerzel();
		if(fachkuerzel.contains(va.getFach().getFachKuerzel())){
			
			return fkNeu += "-E";
			
		}
		
		
		return null;
	}

	public boolean importiereVAGruppDaten(String filename) {
		boolean erfolg = false;
		List<Gruppe> gruppen = new ArrayList<>();
		Gruppe gruppe = null;
		String line = null;
		File file = new File(Folders.IMP_FOLDER + "/" + filename);
		
		try {
			
			FileReader reader = new FileReader(file);
			@SuppressWarnings("resource")
			BufferedReader bfreader = new BufferedReader(reader);
			line = bfreader.readLine();
			while((line = bfreader.readLine()) != null){
				
				gruppe = Mapper.mapGruppe(line);
				gruppen.add(gruppe);
				
			}
			
			String qry = "insert into Gruppe values(?, ?, ?, ?, ?)";
			
			for(Gruppe gr: gruppen){
				if(!gruppeIstBereitsVorhanden(gr)){
					
					Professor prof = gr.getProfessor();
					Assistent assist = gr.getAssistent();
					
					if(!veranstaltungBereitsVorhanden(gr.getFachkuerzel())
							|| !angestelltenEintragIstBereitsEnthalten(prof, "Professor")
							|| !angestelltenEintragIstBereitsEnthalten(assist, "Assistent")){
						
						ErrorLogger.log(new SQLIntegrityConstraintViolationException("Keine VA vorhanden"));
						return false;
					}
					
					PreparedStatement stmt = connection.prepareStatement(qry);
					stmt.setInt(1, gr.getGrpNr());
					stmt.setString(2, gr.getFachkuerzel());
					stmt.setInt(3, gr.getMaxTeams());
					stmt.setInt(4, gr.getProfessor().getMaID().getId());
					stmt.setInt(5, gr.getAssistent().getMaID().getId());
					stmt.executeUpdate();
					
					int tIdIndex = 1;
					
					for(Termin term: gr.getTermine()){
						
						erfolg = insertGruppenTermin(term, gr, tIdIndex);
						++tIdIndex;
					}
					
					stmt.close();
				}
			}

			erfolg = true;
		} catch (IOException e) {
			ErrorLogger.log(e);
			return false;
		} catch (SQLException e) {
			ErrorLogger.log(e);
			return false;
		}
		
		return erfolg;
	}

	@SuppressWarnings("unused")
	private boolean assistentEintragIstBereitsEnthalten(Assistent assist) throws SQLException {
		
		if(assist == null){
			return false;
		}
		String qry = "select count(*) as count from Assistent where maid = ? and vorname = ? and nachname = ?";
		PreparedStatement stmt = connection.prepareStatement(qry);
		stmt.setInt(1, assist.getMaID().getId());
		stmt.setString(2, assist.getVorname());
		stmt.setString(3, assist.getNachname());
		
		ResultSet rs = stmt.executeQuery();
		int count = 0;
		while(rs.next()){
			
			count = rs.getInt("count");
		}
		stmt.close();
		return count > 0;
	}

	private boolean veranstaltungBereitsVorhanden(String fachkuerzel) throws SQLException {
	
		String qry = "select count(*) as count from veranstaltung where fkuerzel = ?";
		PreparedStatement stmt = connection.prepareStatement(qry);
		stmt.setString(1, fachkuerzel);
		ResultSet rs = stmt.executeQuery();
		int count = 0;
		while(rs.next()){
			
			count = rs.getInt("count");
		}
		
		stmt.close();
		return count > 0;
	}

	private boolean gruppeIstBereitsVorhanden(Gruppe gr) throws SQLException {
		
		String qry = "select count(*) as count from gruppe where fkuerzel = ? and grpNr = ?";
		PreparedStatement stmt = connection.prepareStatement(qry);
		stmt.setString(1, gr.getFachkuerzel());
		stmt.setInt(2, gr.getGrpNr());

		ResultSet rs = stmt.executeQuery();
		int count = 0;
		
		while(rs.next()){
			
			count = rs.getInt("count");
		}
		
		stmt.close();
		
		return count > 0;
		
	}

	public List<String> holeImpDateien() {
		final File folder = new File(Folders.IMP_FOLDER);
		List<String> folders = listeDateienAufImp(folder);
		
		return folders;
	}

	public List<String> holeExDateien() {
		final File folder = new File(Folders.EXP_FOLDER);
		List<String> folders = listeDateienAuf(folder);
		return folders;
	}

}
