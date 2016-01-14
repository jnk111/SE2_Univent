package se2.praktikum.projekt.models.person;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonTypeName;

import se2.praktikum.projekt.models.person.fachwerte.Adresse;
import se2.praktikum.projekt.models.person.fachwerte.MAID;

@JsonTypeName("verwMitarbeiter")
public class VerwaltMitarbeiter extends Angestellter{

	

	public VerwaltMitarbeiter(){
		
		this(null, null, null);
	}
	

	public VerwaltMitarbeiter(MAID maID, String vorname,
				   	 String nachname)				 {
		
		this(maID, vorname, nachname, 
			 null, null, null, null,null, null, 
			 null);
	}
	
	
	public VerwaltMitarbeiter(MAID maID, String vorname,
				   	 String nachname, String benutzername, String passwort,
				     Date gebDatum, String gebOrt,
				     Adresse adresse, String department, 
				     EMail email)		{
		
		this.maID = maID;
		this.vorname = vorname;
		this.nachname = nachname;
		this.benutzername = benutzername;
		this.gebDatum = gebDatum;
		this.gebOrt = gebOrt;
		this.adresse = adresse;
		this.department = department;
		this.email = email;
		this.passwort = passwort;
	}
	
	@Override
	public boolean equals(Object obj) {
		
		if(obj == null){
			
			return false;
		}
		
		
		if(obj instanceof VerwaltMitarbeiter){
			
			VerwaltMitarbeiter a = (VerwaltMitarbeiter) obj;
			
			return this.getMaID().equals(a.getMaID())
					&& this.getVorname().equals(a.getVorname())
					&& this.getNachname().equals(a.getNachname());
		}else{
			return false;
		}
	}




}
