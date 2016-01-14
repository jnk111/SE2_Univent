package se2.praktikum.projekt.datenimexport;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import se2.praktikum.projekt.services.loginservice.ServicePool;

@Controller
public class DatenImExportController {
	
	private static ServicePool servicePool;
	
	/**
	 * Stellt Daten aus einem Backup wieder her
	 * @param foldername : Ordner in dem die Backupdateien enthalten sind
	 * @return boolean : backup angelegt/fehlgeschlagen
	 */
	@RequestMapping(value="/wiederherstellen", method=RequestMethod.POST)
	public static @ResponseBody boolean backupWiederherstellen(@RequestBody String foldername){
		return servicePool.getDatenImpSrv().backupWiederherstellen(foldername);


	}
	
	/**
	 * Gibt eine mit Backupordnern zurück
	 * @return Ordnernamen der Backupdateien im Format bkp_long (Datum)
	 */
	@RequestMapping(value="/getfiles", method=RequestMethod.GET)
	public static @ResponseBody List<String> holeBackupDateien(){
		
		return servicePool.getDatenImpSrv().holeBackupDateien();


	}
	
	
	/**
	 * Gibt eine Liste von Dateien zurueck, die im Import-Ordner liegen
	 * @return Liste Namen von Dateien im Importordner
	 */
	@RequestMapping(value="/getfilesImp", method=RequestMethod.GET)
	public static @ResponseBody List<String> holeImpDateien(){
		
		return servicePool.getDatenImpSrv().holeImpDateien();


	}
	
	/**
	 * Gibt eine Liste von Dateien zurueck, die im Export-Ordner liegen
	 * @return Liste Namen von Dateien im Exportordner
	 */
	@RequestMapping(value="/getfilesEx", method=RequestMethod.GET)
	public static @ResponseBody List<String> holeExDateien(){
		
		return servicePool.getDatenImpSrv().holeExDateien();


	}
	
	
	/**
	 * Exportiert alle Veranstaltungsbasisdaten ohne Gruppen und Teams
	 * @return boolean : Export erfolgreich/nicht erfolgreich
	 */
	@RequestMapping(value="/vabasisdatenexport", method=RequestMethod.GET)
	public static @ResponseBody boolean vaDatenExportieren(){
		return servicePool.getDatenExSrv().exportiereAlleVABasisDaten();

	}
	
	/**
	 * Exportiert alle Veranstaltungsbasisdaten mit den zugehoerigen Gruppen
	 * Es wird eine Datei für die VA-Basisdaten angelegt und für die zugehoerigen
	 * Gruppendaten
	 * @return boolean : Export erfolgreich/nicht erfolgreich
	 */
	@RequestMapping(value="/vadatengruppexport", method=RequestMethod.GET)
	public static @ResponseBody boolean vaDatenGruppExportieren(){
		
		return servicePool.getDatenExSrv().exportiereAlleVADatenMitGruppen();
	}
	
	
	/**
	 * Exportiert alle Veranstaltungsbasisdaten mit den zugehoerigen Gruppen und Teams
	 * Es wird je eine Datei für die VA-Basisdaten, die zugehoerigen
	 * Gruppendaten und zugehörigen Teamdaten angelegt
	 * @return boolean : Export erfolgreich/nicht erfolgreich
	 */
	@RequestMapping(value="/vadatengruppteamexport", method=RequestMethod.GET)
	public static @ResponseBody boolean vaDatenGruppTeamExportieren(){
		return servicePool.getDatenExSrv().exportiereAlleVADatenMitGruppenUndTeams();

	}
	
	
	/**
	 * Exportiert alle Professorendaten in der Datenbank inkl. aller Informationen
	 * Es wird je eine Datei mit den exportierten Professoren angelegt.
	 * 
	 * @return boolean : Export erfolgreich/nicht erfolgreich
	 */
	@RequestMapping(value="/exp_professoren", method=RequestMethod.GET)
	public static @ResponseBody boolean exportiereProfDaten(){
		return servicePool.getDatenExSrv().exportiereProfDaten()
				&& servicePool.getDatenExSrv().exportiereAssitDaten()
				 && servicePool.getDatenExSrv().exportiereVerwMADaten();

	}
	
	
	/**
	 * Exportiert alle Studentendaten in der Datenbank inkl. aller Informationen
	 * Es wird je eine Datei mit den exportierten Studenten angelegt.
	 * 
	 * @return boolean : Export erfolgreich/nicht erfolgreich
	 */
	@RequestMapping(value="/exp_studenten", method=RequestMethod.GET)
	public static @ResponseBody boolean exportiereStudDaten(){
		return servicePool.getDatenExSrv().exportiereStudenten();

	}
	
	
	/**
	 * Exportiert alle Assistentendaten in der Datenbank inkl. aller Informationen
	 * Es wird je eine Datei mit den exportierten Assistenten angelegt.
	 * 
	 * @return boolean : Export erfolgreich/nicht erfolgreich
	 */
	@RequestMapping(value="/exp_assitenten", method=RequestMethod.GET)
	public static @ResponseBody boolean exportiereAssistDaten(){
		return servicePool.getDatenExSrv().exportiereAssitDaten();

	}
	
	
	/**
	 * Exportiert alle Verwaltungsmitarbeiterdaten in der Datenbank inkl. aller Informationen
	 * Es wird je eine Datei mit den exportierten Verwaltungsmitarbeitern angelegt.
	 * 
	 * @return boolean : Export erfolgreich/nicht erfolgreich
	 */
	@RequestMapping(value="/exp_verwmitarbeiter", method=RequestMethod.GET)
	public static @ResponseBody boolean exportiereVerwMADaten(){
		return servicePool.getDatenExSrv().exportiereVerwMADaten();

	}
	
	
	/**
	 * Exportiert alle Leistungen aus der Datenbank inkl. aller Informationen über die Leistung
	 * Es wird je eine Datei mit den exportierten Leistungen angelegt.
	 * 
	 * @return boolean : Export erfolgreich/nicht erfolgreich
	 */
	@RequestMapping(value="/exp_leistungen", method=RequestMethod.GET)
	public static @ResponseBody boolean exportiereLeistungenDaten(){
		return servicePool.getDatenExSrv().exportiereAlleLeistungenDaten();

	}
	
	/**
	 * Importiert externe Studierendendaten in die Datenbank. Daten muessen als JSON-Datei vorliegen
	 * und syntaktisch zur Importschnittstelle passen (alle DB-Attribute als JSON-Info vorhanden)
	 * Nicht vorhandene Informationen oder Attribute muessen mit "extern" gekennzeichnet werden
	 * Bereits vorhandene MatrikelNummern werden durch neue ersetzt.
	 * Ist ein Eintrag bereits enthalten, wird nichts importiert.
	 * 
	 * @param filename : Dateiname der zu importierenden Datei
	 * @return boolean : Import erfolgreich/nicht erfolgreich
	 */
	@RequestMapping(value="/imp_studenten", method=RequestMethod.POST)
	public synchronized static @ResponseBody boolean importiereStudentenDaten(@RequestBody String filename){
		return servicePool.getDatenImpSrv().importiereStudenten(filename);

	}
	
	
	/**
	 * Importiert externe Professorendaten in die Datenbank. Daten muessen als JSON-Datei vorliegen
	 * und syntaktisch zur Importschnittstelle passen (alle DB-Attribute als JSON-Info vorhanden)
	 * Nicht vorhandene Informationen oder Attribute muessen mit "extern" gekennzeichnet werden
	 * Bereits vorhandene MAIDs werden durch neue ersetzt.
	 * Ist ein Eintrag bereits enthalten, wird nichts importiert.
	 * 
	 * @param filename : Dateiname der zu importierenden Datei
	 * @return boolean : Import erfolgreich/nicht erfolgreich
	 */
	@RequestMapping(value="/imp_professoren", method=RequestMethod.POST)
	public synchronized static @ResponseBody boolean importiereProfessorenDaten(@RequestBody String filename){
		return servicePool.getDatenImpSrv().importiereProfessoren(filename);

	}
	
	
	/**
	 * Importiert externe Assistendaten in die Datenbank. Daten muessen als JSON-Datei vorliegen
	 * und syntaktisch zur Importschnittstelle passen (alle DB-Attribute als JSON-Info vorhanden)
	 * Nicht vorhandene Informationen oder Attribute muessen mit "extern" gekennzeichnet werden
	 * Bereits vorhandene MAIDs werden durch neue ersetzt.
	 * Ist ein Eintrag bereits enthalten, wird nichts importiert.
	 * 
	 * @param filename : Dateiname der zu importierenden Datei
	 * @return boolean : Import erfolgreich/nicht erfolgreich
	 */
	@RequestMapping(value="/imp_assistenten", method=RequestMethod.POST)
	public synchronized static @ResponseBody boolean importiereAssistDaten(@RequestBody String filename) {
		
		return servicePool.getDatenImpSrv().importiereAssistenten(filename);
	}
	
	
	/**
	 * Importiert externe Veranstaltungsbasis in die Datenbank. Daten muessen als JSON-Datei vorliegen
	 * und syntaktisch zur Importschnittstelle passen (alle DB-Attribute als JSON-Info vorhanden)
	 * Nicht vorhandene Informationen oder Attribute muessen mit "extern" gekennzeichnet werden
	 * Bereits vorhandene Fachkuerzel (Primary Key) werden durch ein "E" fuer extern ergaenzt..
	 * Ist ein Eintrag bereits enthalten, wird nichts importiert.
	 * 
	 * @param filename : Dateiname der zu importierenden Datei
	 * @return boolean : Import erfolgreich/nicht erfolgreich
	 */
	@RequestMapping(value="/imp_vabasisdaten", method=RequestMethod.POST)
	public synchronized static @ResponseBody boolean importiereVABasisDaten(@RequestBody String filename){
		return servicePool.getDatenImpSrv().importiereVABasisDaten(filename);

	}
	
	
	/**
	 * Importiert externe Gruppendaten in die Datenbank. Daten muessen als JSON-Datei vorliegen
	 * und syntaktisch zur Importschnittstelle passen (alle DB-Attribute als JSON-Info vorhanden)
	 * Nicht vorhandene Informationen oder Attribute muessen mit "extern" gekennzeichnet werden
	 * Die zugehoerigen Veranstaltungsbasisdaten muessen vorliegen, damit Fremdschluessel referenziert
	 * werden koennen.
	 * Ist ein Eintrag bereits enthalten, wird nichts importiert.
	 * 
	 * @param filename : Dateiname der zu importierenden Datei
	 * @return boolean : Import erfolgreich/nicht erfolgreich
	 */
	@RequestMapping(value="/imp_vagruppdaten", method=RequestMethod.POST)
	public synchronized static @ResponseBody boolean importiereVAGruppDaten(@RequestBody String filename){
		return servicePool.getDatenImpSrv().importiereVAGruppDaten(filename);

	}
	
	
	/**
	 * Fuehrt eine Datensicherung der aktuell vorhandenen Daten in der Datenbank durch.
	 * Es werden alle Daten und alle Informationen exportiert und in einem Ordner 
	 * (Syntax: "bkp_" + "todayDate.getTime()") gespeichert.
	 * 
	 * @return Backup erfolgreich/nicht erfolgreich
	 */
	@RequestMapping(value="/backup", method=RequestMethod.GET)
	public static @ResponseBody boolean datenSicherung(){
		
		return servicePool.getDatenExSrv().sichereAlleDaten();

	}


	public static ServicePool getServicePool() {
		return servicePool;
	}


	public static void setServicePool(ServicePool sp) {
		servicePool = sp;
	}	
	

}
