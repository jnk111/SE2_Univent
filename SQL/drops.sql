/*
Lösch-Befehle der Tabellen, falls nötig:
*/
drop table Department cascade constraints;
drop table Assistent cascade constraints;
drop table Bewertung cascade constraints;

drop table Fachbereich cascade constraints;
drop table Gruppe cascade constraints;
drop table Meldung cascade constraints;
drop table POVeranstaltungszuordnung cascade constraints;
drop table Pruefungsordnung cascade constraints;
drop table Professor cascade constraints;
drop table Student cascade constraints;
drop table Team cascade constraints;
drop table Teammitglied cascade constraints;
drop table Termin cascade constraints;
drop table Veranstaltung cascade constraints;
drop table Verwaltungsmitarbeiter cascade constraints;

drop table Anmeldetermin cascade constraints;
drop table Einzelanmeldung cascade constraints;

commit;

