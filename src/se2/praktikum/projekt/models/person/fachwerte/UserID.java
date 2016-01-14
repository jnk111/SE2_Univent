package se2.praktikum.projekt.models.person.fachwerte;

public abstract class UserID {

	public static UserID getID(int parseInt) {
		
		return MatrikelNr.getMatrikelNr(parseInt);

	}
	
	@Override
	public abstract boolean equals(Object o);
	

}
