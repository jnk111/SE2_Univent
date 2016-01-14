package se2.praktikum.projekt.models.leistungen;

import java.util.Date;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import se2.praktikum.projekt.models.person.Professor;
import se2.praktikum.projekt.models.person.Student;
import se2.praktikum.projekt.models.veranstaltung.Fach;

/**
 * Repräsentiert eine Studienleistung
 * @author jan
 *
 */
public class Leistung {
	
	private Fach fach;				// Das Fach
	private Student student; 		// Der Student
	private Professor professor; // Der benotente Professor
	private Date datumPVL;			// Eintragedatum PVL
	private Date datumNote;			// Eintragedatum Note	
	private boolean pvl;			// PVL
	private Note note;				// Note
	
	
	/**
	 * Default-Konstruktor
	 * Initialisiert eine Leistung
	 * Felder können separat über Setter gesetzt werden
	 */
	public Leistung(){
		
		this(null,null,null,null, null, false, null);
	}
	
	
	/**
	 * Initialisiert eine noch nicht bewertete Leistung
	 * Wird in der DB mit jeder Anmeldung zu einem Praktikum angelegt
	 * @param fach : Das Fach
	 * @param student : Der benotete Student 
	 * @param prof : Der benotende Prof
	 */
	public Leistung(Fach fach, Student student, Professor prof){
		
		this(fach, student, prof, null, null, false, null);
	}
	
	public Leistung(Fach fach, Student student, Professor prof, boolean pvl, Note note){
		
		this(fach, student, prof, null, null, false, note);
	}
	
	
	/**
	 * Initialsiert eine bewertete Leistung mit den übergebenen Parametern
	 * @param fach	: Das Fach
	 * @param student : Der Student 
	 * @param prof : Der benotende Prof
	 * @param datumPVL : Datum PVL
	 * @param datumNote : Datum Note
	 * @param pvl : PVL
	 * @param note1 : Note
	 */
	@JsonCreator
	public Leistung(@JsonProperty("fach") Fach fach,
					@JsonProperty("student") Student student,
					@JsonProperty("professor") Professor prof,
					@JsonProperty("datumPVL") Date datumPVL,
					@JsonProperty("datumNote") Date datumNote,
					@JsonProperty("pvl") boolean pvl,
					@JsonProperty("note") Note note1)	{
		
		this.fach = fach;
		this.student = student;
		this.professor = prof;
		this.datumPVL = datumPVL;
		this.datumNote = datumNote;
		this.pvl = pvl;
		this.note = note1;
	}

	/**
	 * Gibt das Fach zurück
	 * @return Fach
	 */
	public Fach getFach() {
		return fach;
	}

	/**
	 * Setzt das Fach
	 * @param fach
	 */
	public void setFach(Fach fach) {
		this.fach = fach;
	}
	
	/**
	 * Gibt den zu benotenden Studenten zurück
	 * @return Student
	 */
	public Student getStudent() {
		return student;
	}

	/**
	 * Setzt den zu benotetenden Studenten
	 * @param student
	 */
	public void setStudent(Student student) {
		this.student = student;
	}

	/**
	 * Gibt den benotenden Prof zurück
	 * @return Prof
	 */
	public Professor getProfessor() {
		return professor;
	}

	/**
	 * Setzt den benotenden Prof
	 * @param professor 
	 */
	public void setProfessor(Professor professor) {
		this.professor = professor;
	}

	/**
	 * Gibt das Eintragedatum der PVL zurück
	 * @return DAtum PVL
	 */
	public Date getDatumPVL() {
		return datumPVL;
	}

	/**
	 * Setzt das Eintragedatum der PVL
	 * @param datumPVL
	 */
	public void setDatumPVL(Date datumPVL) {
		this.datumPVL = datumPVL;
	}

	/**
	 * Gibt das Eintragedatum der Note zurück
	 * @return DAtum Note
	 */
	public Date getDatumNote() {
		return datumNote;
	}

	/**
	 * Setzt das Eintragedatum der Note
	 * @param NoteBKP
	 */
	public void setDatumNote(Date datumNote) {
		this.datumNote = datumNote;
	}

	/**
	 * Gibt zurück ob PVL erfolgreich
	 * @return boolean
	 */
	public boolean isPvl() {
		return pvl;
	}

	/**
	 * Setzt den PVL-Status
	 * @param pvl
	 */
	public void setPvl(boolean pvl) {
		this.pvl = pvl;
	}

	/**
	 * Gibt die Note der Leistung zurück
	 * @return Note
	 */
	public Note getNote() {
		return note;
	}

	/**
	 * Setzt die Note der Leistung
	 * @param note
	 */
	public void setNote(Note note) {
		this.note = note;
	}
	

	@Override
	public int hashCode() {
		
		return this.getFach().hashCode()
				+ (this.isPvl()+"").hashCode()
				+ this.getProfessor().hashCode()
				+ this.getStudent().hashCode();
	}

	@Override
	public boolean equals(Object obj) {
		
		if(obj == null){
			return false;
		}
		
		
		if(obj instanceof Leistung){
			
			Leistung l = (Leistung) obj;
			
			if(this.note != null && l.getNote() != null){
				
				return l.getProfessor().equals(this.getProfessor())
						&& l.getStudent().equals(this.getStudent())
						 && l.getFach().equals(this.getFach())
						  && l.getNote().equals(this.getNote())
				 		   && l.isPvl() == this.isPvl();
			}else{
				
				return l.getProfessor().equals(this.getProfessor())
						&& l.getStudent().equals(this.getStudent())
						 && l.getFach().equals(this.getFach())
				 		   && l.isPvl() == this.isPvl();
			}
			

		}else{
			return false;
		}
	}

	@Override
	public String toString() {

		String note = null;
		if(this.note != null){
			
			note = this.note.toString();
		}
		return this.fach.getFachKuerzel() + ", " + this.professor.toString() + ", " + this.student.toString()
				+ ", " + note + ", " + this.pvl;
	}
}
