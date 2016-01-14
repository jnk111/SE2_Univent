package se2.praktikum.projekt.models.person;

import java.util.Date;

import se2.praktikum.projekt.models.person.fachwerte.Adresse;

//@JsonDeserialize(as=AbstrPerson.class)
public interface IPerson {
	
	/**
	 * Gibt den Vornamen einer Person zurück
	 * @return Vorname
	 */
	public String getVorname();

	/**
	 * Setzt den Vornamen einer Person
	 * @param vorname
	 */
	public void setVorname(String vorname);

	/**
	 * Gibt den Nachnamen einer Person zurück
	 * @return nachname
	 */
	public String getNachname();

	
	/**
	 * Setzt den Nachnamen einer Person
	 * @param nachname
	 */
	public void setNachname(String nachname);

	/**
	 * Gibt den Benutzernamen einer Person zurück
	 * @return benutzername
	 */
	public String getBenutzername();

	/**
	 * Setzt den Benutzernamen einer Person
	 * @param benutzername
	 */
	public void setBenutzername(String benutzername);

	/**
	 * gibt das Geburtsdatum einer Person zurück
	 * @return Geburtsdatum
	 */
	public Date getGebDatum();

	/**
	 * Setzt das Geburtsdatum einer Person
	 * @param gebDatum
	 */
	public void setGebDatum(Date gebDatum);

	/**
	 * Gibt den Geburtsort einer Person zurück
	 * @return Geburtsort
	 */
	public String getGebOrt();

	/**
	 * Setzt den Geburtsort einer Person
	 * @param gebOrt
	 */
	public void setGebOrt(String gebOrt);

	/**
	 * Gibt die Adresse einer Person zurück
	 * @return die Adresse
	 */
	public Adresse getAdresse();

	/**
	 * Setzt die Adresse einer Person
	 * @param adresse
	 */
	public void setAdresse(Adresse adresse);

	/**
	 * gibt das zuständige Department einer Person zurück
	 * @return Department
	 */
	public String getDepartment();

	
	/**
	 * Setzt das zuständige Department einer Person
	 * @param department
	 */
	public void setDepartment(String department);


	/**
	 * Gibt die Email-Adresse einer Person zurück
	 * @return die Email-Adresse
	 */
	public EMail getEMail();

	/**
	 * Setzt die Email-Adresse einer Person
	 * @param emailAdresse
	 */
	public void setEMail(EMail emailAdresse);

	String getVollerName();

	void setVollerName(String vollerName);

	String getPasswort();

	void setPasswort(String passwort);
	
	public int getAktuellerUserCount();
	
	public void setAktuellerUserCount(int count);

}
