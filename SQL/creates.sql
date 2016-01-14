

Create table Department
(
  DKuerzel              varChar(10),
  DBezeichnung          varChar(50)   constraint DepartmentDBezeichnungNotN not Null,
  constraint PKDepartment PRIMARY KEY(DKuerzel)
);

Create table Fachbereich
(
  FBKuerzel             varChar(10),
  FBBezeichnung         varChar(50)   constraint FachbereichFBBezeichnungNotN not Null,
  DKuerzel              varChar(10),
  constraint PKFachbereich PRIMARY KEY(FBKuerzel),
  constraint FKFachbereich FOREIGN KEY(DKuerzel) references Department
);

Create table Professor
(
  MAID                  Number(15),
  username              varChar(10),
  vorname               varChar(30)    constraint ProfessorVornameNotN not Null,
  nachname              varChar(30)    constraint ProfessorNachnameNotN not Null,
  Titel		              varChar(20),
  passwort              varChar(30)    constraint ProfessorPasswortNotN not Null,
  strasse               varChar(30)    constraint ProfessorStrasseNotN not Null,
  hausNr                Number(3)      constraint ProfessorHausNr not Null,
  plz                   varChar(5)     constraint ProfessorplzNotN not Null,
  ort                   varChar(30)    constraint ProfessorortNotN not Null,
  gebDatum              Date,
  gebOrt                varChar(30),
  email                 varChar(50),
  DKuerzel              varChar(10),
  constraint PKProfessor PRIMARY KEY(MAID),
  constraint FKProfessorD FOREIGN KEY (DKuerzel) references Department,
  /*constraint CheckProfessorusername check (regexp_like(username,'^o[a-z][a-z][0-9]+$')),
  constraint CheckProfessorpasswort check (regexp_like(passwort,'^[A-Za-z1-9_-.@!§$%&?]+$')),
  constraint CheckProfessorplz check (regexp_like(PLZ,'^[0-9]*$')),
  constraint CheckProfessoremail check (regexp_like(email,'^[A-Za-z1-9_-.]+@[A-Za-z1-9_-.]+.[A-Za-z1-9_-.]+$')),*/
  constraint UniqueUsernameProfessor UNIQUE(username)
);

Create table Assistent
(
  MAID                  Number(15),
  username              varChar(10),
  vorname               varChar(30)    constraint AssistentVornameNotN not Null,
  nachname              varChar(30)    constraint AssistentNachnameNotN not Null,
  passwort              varChar(30)    constraint AssistentPasswortNotN not Null,
  strasse               varChar(30)    constraint AssistentStrasseNotN not Null,
  hausNr                Number(3)      constraint AssistentHausNr not Null,
  plz                   varChar(5)     constraint AssistentplzNotN not Null,
  ort                   varChar(30)    constraint AssistentortNotN not Null,
  gebDatum              Date,
  gebOrt                varChar(30),
  email                 varChar(50),
  DKuerzel              varChar(10),
  constraint PKAssistent PRIMARY KEY(MAID),
  constraint FKAssistentD FOREIGN KEY (DKuerzel) references Department,
 /* constraint CheckAssistentusername check (regexp_like(username,'^m[a-z][a-z][0-9]+$')),
  constraint CheckAssistentpasswort check (regexp_like(passwort,'^[A-Za-z1-9_-.@!§$%&?]+$')),
  constraint CheckAssistentplz check (regexp_like(PLZ,'^[0-9]*$')),
  constraint CheckAssistentemail check (regexp_like(email,'^[A-Za-z1-9_-.]+@[A-Za-z1-9_-.]+.[A-Za-z1-9_-.]+$')),*/
  constraint UniqueUsernameAssistent UNIQUE(username)
);

Create table Veranstaltung
(
  FKuerzel              varChar(10),
  FBKuerzel	            varChar(10),
  FBezeichnung          varChar(100)   constraint VeranstaltungFBezeichnungNotN not Null,
  Semester              Number(2)     constraint VeranstaltungSemesterNotN not Null,
  Typ		                varChar(10)   constraint VeranstaltungTypNotN not Null,
  minTeilnTeam          Number(2),
  maxTeilnTeam          Number(2),
  MAID		              Number(15),
  constraint PKVeranstaltung PRIMARY KEY(FKuerzel),
  constraint FKVeranstaltungFBK FOREIGN KEY(FBKuerzel) references Fachbereich,
  constraint FKVeranstaltungMAID FOREIGN KEY(MAID) references Professor
);

Create table Anmeldetermin
(
  Veranstaltungstyp		varChar(10),
  Anmeldetyp    varchar(10),
  dStart			date,
  dEnde     date,
  uzStart      varchar(5),
  uzEnde       varchar(5),
  constraint PKAnmeldetermin PRIMARY KEY(Veranstaltungstyp, Anmeldetyp)
);

Create table Gruppe
(
  grpNr                 Number(2),
  FKuerzel              varChar(10),
  maxTeams              Number(3)       constraint GruppemaxTeamsNotN not Null,
  ProfMAID	            Number(15),
  AssisMAID	            Number(15),
  constraint PKGruppe PRIMARY KEY (grpNr, FKuerzel),
  constraint FKGruppeFK FOREIGN KEY (FKuerzel) references Veranstaltung,
  constraint FKGruppePMAID FOREIGN KEY (ProfMAID) references Professor,
  constraint FKGruppeAMAID FOREIGN KEY (AssisMAID) references Assistent
);

Create table Termin
(
  TerminID	        varChar(12),
  FKuerzel          varChar(10),
  grpNr		          Number(2),
  Datum             Date		constraint TerminDatumNotN not Null,
  Ustart            varChar(6)	constraint TerminUstartNotN not Null,
  Uende             varChar(6)	constraint TerminUendeNotN not Null,
  raum               varChar(50),
  constraint PKTermin PRIMARY KEY (TerminID),
  constraint FKTerminGrpNr FOREIGN KEY (grpNr, FKuerzel) references Gruppe on delete cascade
  /*constraint CheckTerminUstart check (regexp_like(Ustart,'^[0-9][0-9]:[0-9][0-9]$')),*/
  /*constraint CheckTerminUende check (regexp_like(Uende,'^[0-9][0-9]:[0-9][0-9]$'))*/
);

Create table Team
(
  TeamID                Number(10) primary key,
  FKuerzel              varchar(10),
  minMitglieder         Number(2)     constraint TeamminMitgliederNotN not Null,
  maxMitglieder         Number(2)     constraint TeammaxMitgliederNotN not Null,
  grpNr		      Number(2),
  VorgGrpNr	      Number(2),
  constraint FKTeam FOREIGN Key (grpNr, FKuerzel) references Gruppe,
  constraint FKVORTeam FOREIGN Key (VorgGrpNr, FKuerzel) references Gruppe
);

create table Pruefungsordnung
(
  POKuerzel	          varChar(10),
  POBezeichnung	      varChar(60)	constraint PruefungsordnPOBezNotN not Null,
  constraint PKPruefungsordnung PRIMARY KEY(POKuerzel)
);

/* Passwortregeln(noch): Mindestens 1 Zeichen erlaubt Zahlen, klein/groß Buchstaben und diese Sonderzeichen _-.@!§$%&? */

Create table Student
(
  matrNr                Number(15),
  username              varChar(10),
  vorname               varChar(30)    constraint StudentVornameNotN not Null,
  nachname              varChar(30)    constraint StudentNachnameNotN not Null,
  passwort              varChar(30)    constraint StudentPasswortNotN not Null,
  POKuerzel	            varChar(10),
  strasse               varChar(30)    constraint StudentStrasseNotN not Null,
  hausNr                Number(3)      constraint StudentHausNr not Null,
  plz                   varChar(5)     constraint StudentplzNotN not Null,
  ort                   varChar(30)    constraint StudentortNotN not Null,
  gebDatum              Date,
  gebOrt                varChar(30),
  email                 varChar(50),
  FBKuerzel             varChar(10),
  DKuerzel              varChar(10),
  constraint PKStudetent PRIMARY KEY(matrNr),
  constraint FKStudentD FOREIGN KEY (DKuerzel) references Department,
  constraint FKStudentFB FOREIGN KEY (FBKuerzel) references Fachbereich,
  constraint FKStudentPO FOREIGN KEY (POKuerzel) references Pruefungsordnung,
  /*constraint CheckStudentusername check (regexp_like(username,'^a[a-z][a-z][0-9]+$')),*/
  /*constraint CheckStudentpasswort check (regexp_like(passwort,'^[A-Za-z1-9_-.@!§$%&?]+$')),*/
  /*constraint CheckStudentplz check (regexp_like(PLZ,'^[0-9]*$')),*/
  /*constraint CheckStudentemail check (regexp_like(email,'^[A-Za-z1-9_-.]+@[A-Za-z1-9_-.]+.[A-Za-z1-9_-.]+$')),*/
  constraint UniqueUsernameStudent UNIQUE(username)
);


Create table Verwaltungsmitarbeiter
(
  MAID                  Number(15),
  username              varChar(10),
  vorname               varChar(30)    constraint VerwaltungsMAVornameNotN not Null,
  nachname              varChar(30)    constraint VerwaltungsMANachnameNotN not Null,
  passwort              varChar(30)    constraint VerwaltungsMAPasswortNotN not Null,
  strasse               varChar(30)    constraint VerwaltungsMAStrasseNotN not Null,
  hausNr                Number(3)      constraint VerwaltungsMAHausNr not Null,
  plz                   varChar(5)     constraint VerwaltungsMAplzNotN not Null,
  ort                   varChar(30)    constraint VerwaltungsMAortNotN not Null,
  gebDatum              Date,
  gebOrt                varChar(30),
  email                 varChar(50),
  DKuerzel              varChar(10),
  constraint PKVerwaltungsMA PRIMARY KEY(MAID),
  constraint FKVerwaltungsMAD FOREIGN KEY (DKuerzel) references Department
  /*constraint CheckVerwaltungsMAusername check (regexp_like(username,'^n[a-z][a-z][0-9]+$')),
  constraint CheckVerwaltungsMAPasswort check (regexp_like(passwort,'^[A-Za-z1-9_-.@!§$%&?]+$')),
  constraint CheckVerwaltungsMAPlz check (regexp_like(PLZ,'^[0-9]*$')),
  constraint CheckVerwaltungsMAEmail check (regexp_like(email,'^[A-Za-z1-9_-.]+@[A-Za-z1-9_-.]+.[A-Za-z1-9_-.]+$')),
  constraint UniqueUsernameVerwaltungsMA UNIQUE(username)*/
);


/*Erstma nicht einfügen
Create table Veranstaltungsteilnehmer
(
FKuerzel               varChar(10),
matrNr                 Number(15),
constraint PKVeranstaltungsteilnehmer PRIMARY KEY (FKuerzel, matrNr),
constraint FKVeranstaltungsteilnehmerF FOREIGN KEY (FKuerzel) references Veranstaltung,
constraint FKVeranstaltungsteilnehmeruser FOREIGN KEY (matrNr) references Student
);
*/


Create table Teammitglied
(
  TeamID                Number(10),
  matrNr                Number(15)  ,
  bestaetigt	          varChar(6),
  einzel	              varChar(6),
  constraint PKTeammitglied PRIMARY KEY (TeamID, matrNr),
  constraint FKTeammitgliedTeam FOREIGN KEY (TeamID) references Team,
  constraint FKTeammitglieduser FOREIGN KEY (matrNr) references Student,
  constraint CheckTeambestaetigt check(bestaetigt in('true', 'false'))
);

Create table Bewertung
(
  MAID                  Number(15),
  matrNr                Number(15),
  FKuerzel              varChar(10),
  DatumPVL              Date,
  DatumNote             Date,
  Pvl                   varChar(15),
  Note                  varChar(20),
  constraint PKBewertung PRIMARY KEY (MAID, matrNr, FKuerzel),
  constraint FKBewertungMAID FOREIGN KEY (MAID) references Professor,
  constraint FKBewertunguser FOREIGN KEY (matrNr) references Student,
  constraint FKBewertungF FOREIGN KEY (FKuerzel) references Veranstaltung
  );
  /*constraint CheckNote	check(regexp_like(Note, '[0-9]\.?[0-9]?[0-9]?|1[0-5]|[A-F]|null'))*/



create table POVeranstaltungszuordnung
(
  POKuerzel             varChar(10),
  FKuerzel              varChar(10),
  constraint PKVAPruefungsordnung PRIMARY KEY(POKuerzel, FKuerzel),
  constraint FKVAPruefungsordnung FOREIGN KEY(POKuerzel) references Pruefungsordnung,
  constraint FKFKrzlPruefungsordnung FOREIGN KEY(FKuerzel) references Veranstaltung
);

create table Meldung
(
  absender	            Number(15),
  empfaenger	          Number(15),
  fkuerzel              varchar(10),
  TeamID                Number(10),
  versandDatum          Date,
  bestaetigt	          varChar(6),
  bestaetigungsdatum    Date,
  typ	  	              varchar(20),
  constraint PKMeldung PRIMARY KEY(absender, empfaenger, fkuerzel, versandDatum, typ),
  constraint FKMeldungAbsender FOREIGN KEY(absender) references Student,
  constraint FKMeldungEmpfaenger FOREIGN KEY(empfaenger) references Student,
  constraint CheckMeldungTeambestaetigt check(bestaetigt in('true', 'false', 'null'))
);



/*
Version 1 in ca. 5 Stunden


Version 2 in ca. 3 Stunden
Version 3 in ca. 3 Stunden
Version 4 in ca. 4 Stunden
Version 5 in ca. 4 Stunden
*/
commit;

