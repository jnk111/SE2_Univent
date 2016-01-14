package se2.praktikum.projekt.services.loginservice;

import java.util.HashMap;
import java.util.Map;

import se2.praktikum.projekt.datenimexport.DatenExportService;
import se2.praktikum.projekt.datenimexport.DatenImportService;
import se2.praktikum.projekt.models.person.IAngestellter;
import se2.praktikum.projekt.models.person.IPerson;
import se2.praktikum.projekt.models.person.Student;
import se2.praktikum.projekt.models.person.fachwerte.UserID;
import se2.praktikum.projekt.services.leistungenservice.LeistungenAnzSrv;
import se2.praktikum.projekt.services.leistungenservice.LeistungenEintrSrv;
import se2.praktikum.projekt.services.newsservice.NewsSrv;
import se2.praktikum.projekt.services.veranstaltungsservice.AnzeigerSrv;
import se2.praktikum.projekt.services.veranstaltungsservice.GruppVerwSrv;
import se2.praktikum.projekt.services.veranstaltungsservice.StudentVerwSrv;
import se2.praktikum.projekt.services.veranstaltungsservice.TeamVerwSrv;
import se2.praktikum.projekt.services.veranstaltungsservice.VeranstaltungsVerwalterSrv;

public class ServicePool {
	
	private LeistungenAnzSrv lAnzSrv;
	private LeistungenEintrSrv lEintrSrv;
	private	AnzeigerSrv verAnzSrv;				// Veranstaltungsanzeigerservice
	private GruppVerwSrv gruppVerwSrv;
	private TeamVerwSrv teamVerwSrv;
	private VeranstaltungsVerwalterSrv veranVerwSrv;
	private StudentVerwSrv studentVerwSrv;
	private DatenExportService datenExSrv;
	private DatenImportService datenImpSrv;
	private NewsSrv newsSrv;
	private Map<UserID, IPerson> users;
	
	
	public ServicePool(){
		
		users = new HashMap<>();
		
	}


	public LeistungenAnzSrv getlAnzSrv() {
		return lAnzSrv;
	}


	public void setlAnzSrv(LeistungenAnzSrv lAnzSrv) {
		this.lAnzSrv = lAnzSrv;
	}


	public LeistungenEintrSrv getlEintrSrv() {
		return lEintrSrv;
	}


	public void setlEintrSrv(LeistungenEintrSrv lEintrSrv) {
		this.lEintrSrv = lEintrSrv;
	}


	public AnzeigerSrv getVerAnzSrv() {
		return verAnzSrv;
	}


	public void setVerAnzSrv(AnzeigerSrv verAnzSrv) {
		this.verAnzSrv = verAnzSrv;
	}


	public GruppVerwSrv getGruppVerwSrv() {
		return gruppVerwSrv;
	}


	public void setGruppVerwSrv(GruppVerwSrv gruppVerwSrv) {
		this.gruppVerwSrv = gruppVerwSrv;
	}


	public TeamVerwSrv getTeamVerwSrv() {
		return teamVerwSrv;
	}


	public void setTeamVerwSrv(TeamVerwSrv teamVerwSrv) {
		this.teamVerwSrv = teamVerwSrv;
	}


	public VeranstaltungsVerwalterSrv getVeranVerwSrv() {
		return veranVerwSrv;
	}


	public void setVeranVerwSrv(VeranstaltungsVerwalterSrv veranVerwSrv) {
		this.veranVerwSrv = veranVerwSrv;
	}


	public IPerson getUser(UserID id){
		return users.get(id);
	}
	
	public Map<UserID, IPerson> getUsers(){
		return users;
	}


//	public HelpBox getHelpBox() {
//		return helpBox;
//	}
//
//
//	public void setHelpBox(HelpBox helpBox) {
//		this.helpBox = helpBox;
//	}


	public StudentVerwSrv getStudentVerwSrv() {
		return studentVerwSrv;
	}


	public void setStudentVerwSrv(StudentVerwSrv studentVerwSrv) {
		this.studentVerwSrv = studentVerwSrv;
	}


	public void setDatenExportSrv(DatenExportService datenExportService) {
		
		this.setDatenImExSrv(datenExportService);
		
	}


	public DatenExportService getDatenExSrv() {
		return datenExSrv;
	}


	public void setDatenImExSrv(DatenExportService datenImExSrv) {
		this.datenExSrv = datenImExSrv;
	}


	public DatenImportService getDatenImpSrv() {
		return datenImpSrv;
	}


	public void setDatenImpSrv(DatenImportService datenImpSrv) {
		this.datenImpSrv = datenImpSrv;
	}


	public NewsSrv getNewsSrv() {
		return newsSrv;
	}


	public void setNewsSrv(NewsSrv newsSrv) {
		this.newsSrv = newsSrv;
	}


	public void addUser(IPerson user2) {
		
		if(user2 instanceof Student){
			Student stud = (Student) user2;
			this.users.put(stud.getMatrNr(), stud);
		}else{
			IAngestellter ang = (IAngestellter) user2;
			this.users.put(ang.getMaID(), user2);
		}
		
		
	}


	public void removeUser(UserID userID) {
		
		this.users.remove(userID);
		
	}





}
