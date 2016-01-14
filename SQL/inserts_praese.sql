/*Dies sind die ersten Testdaten Inserts.

Erstmal nur die Inserts fuer alles noetige von Veranstaltungen.
zu beachten: A-Kennung wird erstmal auch als Passwort genutzt.
To-Do: Gruppe, Termin(bin eingepennt)
Ungetestet: ERM und Daten, da aufgrund der anderen Praktika leider die Zeit fehlte
Student hat A-Kennung, Assistenten hat M-Kennung, Verwaltungsmitarbeiter N-Kennung und Professoren O-Kennung
*/

/* Department: DKuerzel, DBezeichnung */
insert INTO Department values('I', 'Informatik');

/* Fachbereich: FBKuerzel, FBBezeichnung, DKuerzel */
insert INTO Fachbereich values('AI', 'Angewandte Informatik', 'I');
/*insert INTO Fachbereich values('TI', 'Technische Informatik', 'I');
insert INTO Fachbereich values('WI', 'Wirtschafts Informatik', 'I');*/

/* Professor: MAID, usernanme, vorname, nachname, Titel, passwort, strasse, hausNr, plz, ort,
gebDatum, gebOrt, email, DKuerzel */
/* Professor: MAID, usernanme, vorname, nachname, Titel, passwort, strasse, hausNr, plz, ort,
gebDatum, gebOrt, email, DKuerzel */
insert INTO Professor values(1, 'oaa001', 'Martin', 'Huebner', 'Professor Dr.', 'oaa001', 'Neue Str.', 10, '22743', 'Hamburg', TO_DATE('8-12-1965','MM-DD-YYYY'), 'Frankfurt', 'martin.huebner@haw-hamburg.de', 'I');
insert INTO Professor values(2, 'oaa002', 'Julia', 'Padberg', 'Professor Dr.', 'oaa002', 'Bluetenallee', 142, '23412', 'Hamburg',TO_DATE('12-1-1965','MM-DD-YYYY') , 'Hamburg', 'julia.padberg@haw-hamburg.de', 'I');
insert INTO Professor values(3, 'oaa003', 'Axel', 'Schmolitzky', 'Professor Dr.', 'oaa003', 'Kalten Muehlen', 4, '21762', 'Hamburg', TO_DATE('3-21-1980','MM-DD-YYYY'), 'Berlin', 'axel.schmolitzky@haw-hamburg.de', 'I');
insert INTO Professor values(4, 'oaa004', 'Wolfgang', 'Gerken', 'Professor Dr.', 'oaa004', 'Ruebenhall', 32, '20743', 'Hamburg',TO_DATE('6-29-1963','MM-DD-YYYY'), 'Bonn', 'wolfgang.gerken@haw-hamburg.de', 'I');
insert INTO Professor values(5, 'oaa005', 'Michael', 'Neitzke', 'Professor Dr.', 'oaa005', 'Schattenweg', 43, '23722', 'Hamburg',TO_DATE('2-15-1971','MM-DD-YYYY'), 'Freistatt', 'michael.neitzke@haw-hamburg.de', 'I');
insert INTO Professor values(6, 'oaa006', 'Klaus-Peter', 'Kossakowski', 'Professor Dr.', 'oaa006', 'Blumenstrasse', 65, '21743', 'Hamburg',TO_DATE('6-9-1969','MM-DD-YYYY'), 'Hamburg', 'klaus-peter.kossakowski@haw-hamburg.de', 'I');
insert INTO Professor values(7, 'oaa007', 'Stephan', 'Pareigis', 'Professor Dr.', 'oaa007', 'Segelallee', 35, '23253', 'Hamburg',TO_DATE('4-23-1970','MM-DD-YYYY'), 'Hamburg', 'stephan.pareigis@haw-hamburg.de', 'I');
insert INTO Professor values(8, 'oaa008', 'Martin', 'Becke', 'Professor Dr.', 'oaa008', 'Bruchstrasse', 112, '21211', 'Hamburg',TO_DATE('10-13-1975','MM-DD-YYYY'), 'Berlin', 'martin.becke@haw-hamburg.de', 'I');
insert INTO Professor values(9, 'oaa009', 'Olaf', 'Zukunft', 'Professor Dr.', 'oaa008', 'Bruchstrasse', 112, '21211', 'Hamburg',TO_DATE('10-13-1975','MM-DD-YYYY'), 'Berlin', 'martin.becke@haw-hamburg.de', 'I');

/* Assistent: MAID, usernanme, vorname, nachname, passwort, strasse, hausNr, plz, ort,
gebDatum, gebOrt, email, DKuerzel */
insert INTO Assistent values(11, 'maa001', 'Ilona', 'Blank', 'maa001', 'EricksenstrStasse', 23, '22223', 'Hamburg',TO_DATE('2-23-1981','MM-DD-YYYY'), 'Dortmund', 'ilona.blank@haw-hamburg.de', 'I');
insert INTO Assistent values(22, 'maa002', 'Gerhard', 'Oelker', 'maa002', 'Spaltenweg', 1, '21057', 'Hamburg', TO_DATE('3-29-1969','MM-DD-YYYY'), 'Freistatt', 'gerhard.oelker@haw-hamburg.de', 'I');
insert INTO Assistent values(33, 'maa003', 'Hartmut', 'Schulz', 'maa003', 'Ruebenhall', 22, '20743', 'Hamburg', TO_DATE('4-12-1973','MM-DD-YYYY'), 'Hamburg', 'hartmut.schulz@haw-hamburg.de', 'I');

/* Veranstaltung: FKuerzel, FBKuerzel, FBezeichnung, Semester, Typ, minTeilnTeam, maxTeilnTeam, MAID  */
insert INTO Veranstaltung values('PR1', 'AI', 'Programmieren 1', 1, 'Vorlesung', 0, 0, 3);
insert INTO Veranstaltung values('PRP1', 'AI', 'Programmieren Praktikum 1', 1, 'Praktikum', 2, 3, 3);
insert INTO Veranstaltung values('MG', 'AI', 'Mathematische Grundlagen', 1, 'Vorlesung',0, 0, 4);
insert INTO Veranstaltung values('MGP', 'AI', 'Mathematische Grundlagen Praktikum', 1, 'Praktikum',2, 3, 4);
insert INTO Veranstaltung values('PR2', 'AI', 'Programmieren 2', 2, 'Vorlesung', 0, 0, 3);
insert INTO Veranstaltung values('PRP2', 'AI', 'Programmieren Praktikum 2', 2, 'Praktikum', 2, 2, 3);
insert INTO Veranstaltung values('DB', 'AI', 'Datenbanken', 2, 'Vorlesung', 0, 0, 4);
insert INTO Veranstaltung values('DBP', 'AI', 'Datenbanken Praktikum', 2, 'Praktikum', 2, 3, 4);
insert INTO Veranstaltung values('BS', 'AI', 'Betriebssysteme', 3, 'Vorlesung', 0, 0, 1);
insert INTO Veranstaltung values('BSP', 'AI', 'Betriebssysteme Praktikum', 3, 'Praktikum', 3, 4, 1);
insert INTO Veranstaltung values('GKA', 'AI', 'Graphentheoretischekonzepte und Algorithmen', 3, 'Vorlesung', 0, 0, 2);
insert INTO Veranstaltung values('GKAP', 'AI', 'Graphentheoretischekonzepte und Algorithmen Praktikum', 3, 'Praktikum', 2, 3, 2);
insert INTO Veranstaltung values('WPDBD', 'AI', 'Wahlpflicht Datenbank Design', 4, 'WP', 0, 0, 4);
insert INTO Veranstaltung values('WPDBDP', 'AI', 'Wahlpflicht Datenbank Design Praktikum', 4, 'WPP', 1, 2, 4);
insert INTO Veranstaltung values('WPPN', 'AI', 'Wahlpflicht Petrinetze', 4, 'WP', 0, 0, 2);
insert INTO Veranstaltung values('WPPNP', 'AI', 'Wahlpflicht Petrinetze Praktikum', 4, 'WPP', 1, 2, 2);
insert INTO Veranstaltung values('WPNSBD', 'AI', 'Wahlpflicht NoSQL und BigData', 4, 'WP', 0, 0, 9);
insert INTO Veranstaltung values('WPNSBDP', 'AI', 'Wahlpflicht NoSQL und BigData Praktikum', 4, 'WPP', 1, 2, 9);
insert INTO Veranstaltung values('WPDE', 'AI', 'Wahlpflicht Deeply Embedded', 4, 'WP', 0, 0, 7);
insert INTO Veranstaltung values('WPDEP', 'AI', 'Wahlpflicht Deeply Embedded Praktikum', 4, 'WPP', 1, 2, 7);
insert INTO Veranstaltung values('POCTF', 'AI', 'Capture The Flag', 5, 'PO', 2, 3, 6);
insert INTO Veranstaltung values('POLA', 'AI', 'Lernende Agenten', 5, 'PO', 2, 3, 5);
insert INTO Veranstaltung values('POSR', 'AI', 'Software4Refugees', 5, 'PO', 2, 3, 7);
insert INTO Veranstaltung values('POWRTC', 'AI', 'WebRTC in Puplic Domains', 5, 'PO', 2, 3, 8);

/* Anmeldetermin: Veranstaltungstyp, Anmeldedatum . Startuhrzeit, Enduhrzeit*/
insert INTO Anmeldetermin values('Praktikum', 'Team', TO_DATE('12-23-2015','MM-DD-YYYY'), TO_DATE('12-24-2015','MM-DD-YYYY'), '08:00', '00:00');
insert INTO Anmeldetermin values('Praktikum', 'Einzel', TO_DATE('12-25-2015','MM-DD-YYYY'), TO_DATE('12-26-2015','MM-DD-YYYY'), '08:00', '00:00');
insert INTO Anmeldetermin values('WPP', 'Team', TO_DATE('12-23-2015','MM-DD-YYYY'), TO_DATE('12-24-2015','MM-DD-YYYY'), '08:00', '00:00');
insert INTO Anmeldetermin values('WPP', 'Einzel', TO_DATE('12-25-2015','MM-DD-YYYY'), TO_DATE('12-26-2015','MM-DD-YYYY'), '08:00', '00:00');
insert INTO Anmeldetermin values('PO', 'Team', TO_DATE('12-23-2015','MM-DD-YYYY'), TO_DATE('12-24-2015','MM-DD-YYYY'),'08:00', '00:00');
insert INTO Anmeldetermin values('PO', 'Einzel', TO_DATE('12-25-2015','MM-DD-YYYY'), TO_DATE('12-26-2015','MM-DD-YYYY'),'08:00', '00:00');

/* Gruppe: grpNr, FKuerzel, maxTeiln, ProfMAID, AssisMAID */
insert INTO Gruppe values(1, 'PRP1', 14, 3, 11);
insert INTO Gruppe values(2, 'PRP1', 14, 3, 11);
insert INTO Gruppe values(3, 'PRP1', 14, 3, 11);
insert INTO Gruppe values(4, 'PRP1', 2, 3, 11);

insert INTO Gruppe values(1, 'MGP', 18, 4, 22);
insert INTO Gruppe values(2, 'MGP', 18, 4, 22);
insert INTO Gruppe values(3, 'MGP', 18, 4, 22);

insert INTO Gruppe values(1, 'PRP2', 14, 3, 11);
insert INTO Gruppe values(2, 'PRP2', 14, 3, 11);
insert INTO Gruppe values(3, 'PRP2', 14, 3, 11);
insert INTO Gruppe values(4, 'PRP2', 2, 3, 11);


insert INTO Gruppe values(1, 'DBP', 14, 4, 33);
insert INTO Gruppe values(2, 'DBP', 14, 4, 22);
insert INTO Gruppe values(3, 'DBP', 14, 4, 33);

insert INTO Gruppe values(1, 'BSP', 14, 1, 11);
insert INTO Gruppe values(2, 'BSP', 14, 1, 11);
insert INTO Gruppe values(3, 'BSP', 14, 1, 11);


insert INTO Gruppe values(1, 'GKAP', 14, 2, 22);
insert INTO Gruppe values(2, 'GKAP', 14, 2, 22);
insert INTO Gruppe values(3, 'GKAP', 14, 2, 22);

insert INTO Gruppe values(1, 'WPDBDP', 14, 4, 11);
insert INTO Gruppe values(1, 'WPPNP', 14, 4, 22);
insert INTO Gruppe values(1, 'WPNSBDP', 14, 4, 11);
insert INTO Gruppe values(1, 'WPDEP', 14, 7, 22);


insert INTO Gruppe values(1, 'POCTF', 14, 6, 33);
insert INTO Gruppe values(1, 'POLA', 14, 5, 11);
insert INTO Gruppe values(1, 'POSR', 14, 7, 33);
insert INTO Gruppe values(1, 'POWRTC', 14, 8, 11);


/* Termin: TerminID, FKuerzel, grpNr, Datum, Ustart, Uende, raum */
insert INTO Termin values('PRP1G1T1', 'PRP1', 1, TO_DATE('10-8-2015','MM-DD-YYYY'), '08:15', '11:30', '1101b');
insert INTO Termin values('PRP1G1T2', 'PRP1', 1, TO_DATE('10-29-2015','MM-DD-YYYY'), '08:15', '11:30', '1101b');
insert INTO Termin values('PRP1G1T3', 'PRP1', 1, TO_DATE('11-19-2015','MM-DD-YYYY'), '08:15', '11:30', '1101b');
insert INTO Termin values('PRP1G1T4', 'PRP1', 1, TO_DATE('12-10-2015','MM-DD-YYYY'), '08:15', '11:30', '1101b');
insert INTO Termin values('PRP1G2T1', 'PRP1', 2, TO_DATE('10-15-2015','MM-DD-YYYY'), '08:15', '11:30', '1101b');
insert INTO Termin values('PRP1G2T2', 'PRP1', 2, TO_DATE('11-5-2015','MM-DD-YYYY'), '08:15', '11:30', '1101b');
insert INTO Termin values('PRP1G2T3', 'PRP1', 2, TO_DATE('11-26-2015','MM-DD-YYYY'), '08:15', '11:30', '1101b');
insert INTO Termin values('PRP1G2T4', 'PRP1', 2, TO_DATE('12-17-2015','MM-DD-YYYY'), '08:15', '11:30', '1101b');
insert INTO Termin values('PRP1G3T1', 'PRP1', 3, TO_DATE('10-22-2015','MM-DD-YYYY'), '08:15', '11:30', '1101b');
insert INTO Termin values('PRP1G3T2', 'PRP1', 3, TO_DATE('11-12-2015','MM-DD-YYYY'), '08:15', '11:30', '1101b');
insert INTO Termin values('PRP1G3T3', 'PRP1', 3, TO_DATE('12-3-2015','MM-DD-YYYY'), '08:15', '11:30', '1101b');
insert INTO Termin values('PRP1G3T4', 'PRP1', 3, TO_DATE('1-7-2016','MM-DD-YYYY'), '08:15', '11:30', '1101b');
insert INTO Termin values('PRP1G4T1', 'PRP1', 4, TO_DATE('10-22-2015','MM-DD-YYYY'), '08:15', '11:30', '1101b');
insert INTO Termin values('PRP1G4T2', 'PRP1', 4, TO_DATE('11-12-2015','MM-DD-YYYY'), '08:15', '11:30', '1101b');
insert INTO Termin values('PRP1G4T3', 'PRP1', 4, TO_DATE('12-3-2015','MM-DD-YYYY'), '08:15', '11:30', '1101b');
insert INTO Termin values('PRP1G4T4', 'PRP1', 4, TO_DATE('1-7-2016','MM-DD-YYYY'), '08:15', '11:30', '1101b');

insert INTO Termin values('MGPG1T1', 'MGP', 1, TO_DATE('10-8-2015','MM-DD-YYYY'), '08:15', '11:30', 'Stiftstr.69 R.01.10');
insert INTO Termin values('MGPG1T2', 'MGP', 1, TO_DATE('10-30-2015','MM-DD-YYYY'), '12:30', '15:45', 'Stiftstr.69 R.01.10');
insert INTO Termin values('MGPG1T3', 'MGP', 1, TO_DATE('11-20-2015','MM-DD-YYYY'), '12:30', '15:45', 'Stiftstr.69 R.01.10');
insert INTO Termin values('MGPG1T4', 'MGP', 1, TO_DATE('12-11-2015','MM-DD-YYYY'), '12:30', '15:45', 'Stiftstr.69 R.01.10');
insert INTO Termin values('MGPG2T1', 'MGP', 2, TO_DATE('10-8-2015','MM-DD-YYYY'), '08:15', '11:30', 'Stiftstr.69 R.01.10');
insert INTO Termin values('MGPG2T2', 'MGP', 2, TO_DATE('11-6-2015','MM-DD-YYYY'), '12:30', '15:45', 'Stiftstr.69 R.01.10');
insert INTO Termin values('MGPG2T3', 'MGP', 2, TO_DATE('11-27-2015','MM-DD-YYYY'), '12:30', '15:45', 'Stiftstr.69 R.01.10');
insert INTO Termin values('MGPG2T4', 'MGP', 2, TO_DATE('12-18-2015','MM-DD-YYYY'), '12:30', '15:45', 'Stiftstr.69 R.01.10');
insert INTO Termin values('MGPG3T1', 'MGP', 3, TO_DATE('10-23-2015','MM-DD-YYYY'), '12:30', '15:45', 'Stiftstr.69 R.01.10');
insert INTO Termin values('MGPG3T2', 'MGP', 3, TO_DATE('11-13-2015','MM-DD-YYYY'), '12:30', '15:45', 'Stiftstr.69 R.01.10');
insert INTO Termin values('MGPG3T3', 'MGP', 3, TO_DATE('12-4-2015','MM-DD-YYYY'), '12:30', '15:45', 'Stiftstr.69 R.01.10');
insert INTO Termin values('MGPG3T4', 'MGP', 3, TO_DATE('1-8-2016','MM-DD-YYYY'), '12:30', '15:45', 'Stiftstr.69 R.01.10');

insert INTO Termin values('PRP2G1T1', 'PRP2', 1, TO_DATE('10-8-2015','MM-DD-YYYY'), '08:15', '11:30', '1101a');
insert INTO Termin values('PRP2G1T2', 'PRP2', 1, TO_DATE('10-26-2015','MM-DD-YYYY'), '08:15', '11:30', '1101a');
insert INTO Termin values('PRP2G1T3', 'PRP2', 1, TO_DATE('11-16-2015','MM-DD-YYYY'), '08:15', '11:30', '1101a');
insert INTO Termin values('PRP2G1T4', 'PRP2', 1, TO_DATE('12-7-2015','MM-DD-YYYY'), '08:15', '11:30', '1101a');
insert INTO Termin values('PRP2G2T1', 'PRP2', 2, TO_DATE('10-12-2015','MM-DD-YYYY'), '08:15', '11:30', '1101a');
insert INTO Termin values('PRP2G2T2', 'PRP2', 2, TO_DATE('11-2-2015','MM-DD-YYYY'), '08:15', '11:30', '1101a');
insert INTO Termin values('PRP2G2T3', 'PRP2', 2, TO_DATE('11-23-2015','MM-DD-YYYY'), '08:15', '11:30', '1101a');
insert INTO Termin values('PRP2G2T4', 'PRP2', 2, TO_DATE('12-14-2015','MM-DD-YYYY'), '08:15', '11:30', '1101a');
insert INTO Termin values('PRP2G3T1', 'PRP2', 3, TO_DATE('10-19-2015','MM-DD-YYYY'), '08:15', '11:30', '1101a');
insert INTO Termin values('PRP2G3T2', 'PRP2', 3, TO_DATE('11-9-2015','MM-DD-YYYY'), '08:15', '11:30', '1101a');
insert INTO Termin values('PRP2G3T3', 'PRP2', 3, TO_DATE('11-30-2015','MM-DD-YYYY'), '08:15', '11:30', '1101a');
insert INTO Termin values('PRP2G3T4', 'PRP2', 3, TO_DATE('1-4-2016','MM-DD-YYYY'), '08:15', '11:30', '1101a');
insert INTO Termin values('PRP2G4T1', 'PRP2', 4, TO_DATE('10-19-2015','MM-DD-YYYY'), '08:15', '11:30', '1101a');
insert INTO Termin values('PRP2G4T2', 'PRP2', 4, TO_DATE('11-9-2015','MM-DD-YYYY'), '08:15', '11:30', '1101a');
insert INTO Termin values('PRP2G4T3', 'PRP2', 4, TO_DATE('11-30-2015','MM-DD-YYYY'), '08:15', '11:30', '1101a');
insert INTO Termin values('PRP2G4T4', 'PRP2', 4, TO_DATE('1-4-2016','MM-DD-YYYY'), '08:15', '11:30', '1101a');

insert INTO Termin values('DBPG1T1', 'DBP', 1, TO_DATE('10-8-2015','MM-DD-YYYY'), '08:15', '11:30', '0380b');
insert INTO Termin values('DBPG1T2', 'DBP', 1, TO_DATE('10-29-2015','MM-DD-YYYY'), '12:30', '15:45', '0380b');
insert INTO Termin values('DBPG1T3', 'DBP', 1, TO_DATE('11-19-2015','MM-DD-YYYY'), '12:30', '15:45', '0380b');
insert INTO Termin values('DBPG1T4', 'DBP', 1, TO_DATE('12-10-2015','MM-DD-YYYY'), '12:30', '15:45', '0380b');
insert INTO Termin values('DBPG2T1', 'DBP', 2, TO_DATE('10-15-2015','MM-DD-YYYY'), '12:30', '15:45', '0380b');
insert INTO Termin values('DBPG2T2', 'DBP', 2, TO_DATE('10-8-2015','MM-DD-YYYY'), '08:15', '11:30','0380b');
insert INTO Termin values('DBPG2T3', 'DBP', 2, TO_DATE('11-26-2015','MM-DD-YYYY'), '12:30', '15:45', '0380b');
insert INTO Termin values('DBPG2T4', 'DBP', 2, TO_DATE('12-17-2015','MM-DD-YYYY'), '12:30', '15:45', '0380b');
insert INTO Termin values('DBPG3T1', 'DBP', 3, TO_DATE('10-22-2015','MM-DD-YYYY'), '12:30', '15:45', '0380b');
insert INTO Termin values('DBPG3T2', 'DBP', 3, TO_DATE('11-12-2015','MM-DD-YYYY'), '12:30', '15:45', '0380b');
insert INTO Termin values('DBPG3T3', 'DBP', 3, TO_DATE('12-3-2015','MM-DD-YYYY'), '12:30', '15:45', '0380b');
insert INTO Termin values('DBPG3T4', 'DBP', 3, TO_DATE('1-7-2016','MM-DD-YYYY'), '12:30', '15:45', '0380b');

insert INTO Termin values('BSPG1T1', 'BSP', 1, TO_DATE('10-9-2015','MM-DD-YYYY'), '08:15', '11:30', 'Steindamm 94 R.0.01');
insert INTO Termin values('BSPG1T2', 'BSP', 1, TO_DATE('10-8-2015','MM-DD-YYYY'), '08:15', '11:30', 'Steindamm 94 R.0.01');
insert INTO Termin values('BSPG1T3', 'BSP', 1, TO_DATE('11-20-2015','MM-DD-YYYY'), '08:15', '11:30', 'Steindamm 94 R.0.01');
insert INTO Termin values('BSPG1T4', 'BSP', 1, TO_DATE('12-11-2015','MM-DD-YYYY'), '08:15', '11:30', 'Steindamm 94 R.0.01');
insert INTO Termin values('BSPG2T1', 'BSP', 2, TO_DATE('10-16-2015','MM-DD-YYYY'), '08:15', '11:30', 'Steindamm 94 R.0.01');
insert INTO Termin values('BSPG2T2', 'BSP', 2, TO_DATE('11-6-2015','MM-DD-YYYY'), '08:15', '11:30', 'Steindamm 94 R.0.01');
insert INTO Termin values('BSPG2T3', 'BSP', 2, TO_DATE('11-27-2015','MM-DD-YYYY'), '08:15', '11:30', 'Steindamm 94 R.0.01');
insert INTO Termin values('BSPG2T4', 'BSP', 2, TO_DATE('12-18-2015','MM-DD-YYYY'), '08:15', '11:30', 'Steindamm 94 R.0.01');
insert INTO Termin values('BSPG3T1', 'BSP', 3, TO_DATE('10-23-2015','MM-DD-YYYY'), '08:15', '11:30', 'Steindamm 94 R.0.01');
insert INTO Termin values('BSPG3T2', 'BSP', 3, TO_DATE('11-13-2015','MM-DD-YYYY'), '08:15', '11:30', 'Steindamm 94 R.0.01');
insert INTO Termin values('BSPG3T3', 'BSP', 3, TO_DATE('12-4-2015','MM-DD-YYYY'), '08:15', '11:30', 'Steindamm 94 R.0.01');
insert INTO Termin values('BSPG3T4', 'BSP', 3, TO_DATE('1-8-2016','MM-DD-YYYY'), '08:15', '11:30', 'Steindamm 94 R.0.01');

insert INTO Termin values('GKAPG1T1', 'GKAP', 1, TO_DATE('10-6-2015','MM-DD-YYYY'), '08:15', '11:30', '1101c');
insert INTO Termin values('GKAPG1T2', 'GKAP', 1, TO_DATE('10-27-2015','MM-DD-YYYY'), '08:15', '11:30', '1101c');
insert INTO Termin values('GKAPG1T3', 'GKAP', 1, TO_DATE('10-8-2015','MM-DD-YYYY'), '08:15', '11:30', '1101c');
insert INTO Termin values('GKAPG1T4', 'GKAP', 1, TO_DATE('12-8-2015','MM-DD-YYYY'), '08:15', '11:30', '1101c');
insert INTO Termin values('GKAPG2T1', 'GKAP', 2, TO_DATE('10-13-2015','MM-DD-YYYY'), '08:15', '11:30', '1101c');
insert INTO Termin values('GKAPG2T2', 'GKAP', 2, TO_DATE('11-3-2015','MM-DD-YYYY'), '08:15', '11:30', '1101c');
insert INTO Termin values('GKAPG2T3', 'GKAP', 2, TO_DATE('11-24-2015','MM-DD-YYYY'), '08:15', '11:30', '1101c');
insert INTO Termin values('GKAPG2T4', 'GKAP', 2, TO_DATE('12-15-2015','MM-DD-YYYY'), '08:15', '11:30', '1101c');
insert INTO Termin values('GKAPG3T1', 'GKAP', 3, TO_DATE('10-20-2015','MM-DD-YYYY'), '08:15', '11:30', '1101c');
insert INTO Termin values('GKAPG3T2', 'GKAP', 3, TO_DATE('11-10-2015','MM-DD-YYYY'), '08:15', '11:30', '1101c');
insert INTO Termin values('GKAPG3T3', 'GKAP', 3, TO_DATE('12-1-2015','MM-DD-YYYY'), '08:15', '11:30', '1101c');
insert INTO Termin values('GKAPG3T4', 'GKAP', 3, TO_DATE('1-5-2016','MM-DD-YYYY'), '08:15', '11:30', '1101c');

insert INTO Termin values('WPPDBDG1T1', 'WPDBDP', 1, TO_DATE('4-4-2016','MM-DD-YYYY'), '08:15', '11:30', '0380a');
insert INTO Termin values('WPPDBDG1T2', 'WPDBDP', 1, TO_DATE('4-11-2016','MM-DD-YYYY'), '08:15', '11:30', '0380a');
insert INTO Termin values('WPPDBDG1T3', 'WPDBDP', 1, TO_DATE('4-25-2016','MM-DD-YYYY'), '08:15', '11:30', '0380a');
insert INTO Termin values('WPPDBDG1T4', 'WPDBDP', 1, TO_DATE('5-2-2016','MM-DD-YYYY'), '08:15', '11:30', '0380a');
insert INTO Termin values('WPPDBDG1T5', 'WPDBDP', 1, TO_DATE('5-9-2016','MM-DD-YYYY'), '08:15', '11:30', '0380a');
insert INTO Termin values('WPPDBDG1T6', 'WPDBDP', 1, TO_DATE('5-30-2016','MM-DD-YYYY'), '08:15', '11:30', '0380a');

insert INTO Termin values('WPPPNG1T1', 'WPPNP', 1, TO_DATE('4-4-2016','MM-DD-YYYY'), '08:15', '11:30', '1101c');
insert INTO Termin values('WPPPNG1T2', 'WPPNP', 1, TO_DATE('4-11-2016','MM-DD-YYYY'), '08:15', '11:30', '1101c');
insert INTO Termin values('WPPPNG1T3', 'WPPNP', 1, TO_DATE('4-18-2016','MM-DD-YYYY'), '08:15', '11:30', '1101c');
insert INTO Termin values('WPPPNG1T4', 'WPPNP', 1, TO_DATE('5-9-2016','MM-DD-YYYY'), '08:15', '11:30', '1101c');
insert INTO Termin values('WPPPNG1T5', 'WPPNP', 1, TO_DATE('5-16-2016','MM-DD-YYYY'), '08:15', '11:30', '1101c');
insert INTO Termin values('WPPPNG1T6', 'WPPNP', 1, TO_DATE('5-23-2016','MM-DD-YYYY'), '08:15', '11:30', '1101c');

insert INTO Termin values('WPDEPG1T1', 'WPDEP', 1, TO_DATE('4-5-2016','MM-DD-YYYY'), '12:30', '15:45', '0765');
insert INTO Termin values('WPDEPG1T2', 'WPDEP', 1, TO_DATE('4-12-2016','MM-DD-YYYY'), '12:30', '15:45', '0765');
insert INTO Termin values('WPDEPG1T3', 'WPDEP', 1, TO_DATE('4-19-2016','MM-DD-YYYY'), '12:30', '15:45', '0765');
insert INTO Termin values('WPDEPG1T4', 'WPDEP', 1, TO_DATE('5-10-2016','MM-DD-YYYY'), '12:30', '15:45', '0765');
insert INTO Termin values('WPDEPG1T5', 'WPDEP', 1, TO_DATE('5-17-2016','MM-DD-YYYY'), '12:30', '15:45', '0765');
insert INTO Termin values('WPDEPG1T6', 'WPDEP', 1, TO_DATE('5-24-2016','MM-DD-YYYY'), '12:30', '15:45', '0765');

insert INTO Termin values('WPNSBDPG1T1', 'WPNSBDP', 1, TO_DATE('4-5-2016','MM-DD-YYYY'), '08:15', '11:30', '0380b');
insert INTO Termin values('WPNSBDPG1T2', 'WPNSBDP', 1, TO_DATE('4-12-2016','MM-DD-YYYY'), '08:15', '11:30', '0380b');
insert INTO Termin values('WPNSBDPG1T3', 'WPNSBDP', 1, TO_DATE('4-26-2016','MM-DD-YYYY'), '08:15', '11:30', '0380b');
insert INTO Termin values('WPNSBDPG1T4', 'WPNSBDP', 1, TO_DATE('5-3-2016','MM-DD-YYYY'), '08:15', '11:30', '0380b');
insert INTO Termin values('WPNSBDPG1T5', 'WPNSBDP', 1, TO_DATE('5-10-2016','MM-DD-YYYY'), '08:15', '11:30', '0380b');
insert INTO Termin values('WPNSBDPG1T6', 'WPNSBDP', 1, TO_DATE('5-31-2016','MM-DD-YYYY'), '08:15', '11:30', '0380b');

insert INTO Termin values('POCTFG1T1', 'POCTF', 1, TO_DATE('4-6-2016','MM-DD-YYYY'), '12:30', '15:45', 'n.B. 5.16'); 
insert INTO Termin values('POCTFG1T2', 'POCTF', 1, TO_DATE('4-20-2016','MM-DD-YYYY'), '12:30', '15:45', 'n.B. 5.16');
insert INTO Termin values('POCTFG1T3', 'POCTF', 1, TO_DATE('4-27-2016','MM-DD-YYYY'), '12:30', '15:45', 'n.B. 5.16');
insert INTO Termin values('POCTFG1T4', 'POCTF', 1, TO_DATE('5-4-2016','MM-DD-YYYY'), '12:30', '15:45', 'n.B. 5.16');
insert INTO Termin values('POCTFG1T5', 'POCTF', 1, TO_DATE('5-18-2016','MM-DD-YYYY'), '12:30', '15:45', 'n.B. 5.16');
insert INTO Termin values('POCTFG1T6', 'POCTF', 1, TO_DATE('5-25-2016','MM-DD-YYYY'), '12:30', '15:45', 'n.B. 5.16');

insert INTO Termin values('POLAG1T1', 'POLA', 1, TO_DATE('4-6-2016','MM-DD-YYYY'), '08:15', '11:30', 'n.B. 1.05');
insert INTO Termin values('POLAG1T2', 'POLA', 1, TO_DATE('4-13-2016','MM-DD-YYYY'), '08:15', '11:30', 'n.B. 1.05');
insert INTO Termin values('POLAG1T3', 'POLA', 1, TO_DATE('4-27-2016','MM-DD-YYYY'), '08:15', '11:30', 'n.B. 1.05');
insert INTO Termin values('POLAG1T4', 'POLA', 1, TO_DATE('5-4-2016','MM-DD-YYYY'), '08:15', '11:30', 'n.B. 1.05');
insert INTO Termin values('POLAG1T5', 'POLA', 1, TO_DATE('5-11-2016','MM-DD-YYYY'), '08:15', '11:30', 'n.B. 1.05');
insert INTO Termin values('POLAG1T6', 'POLA', 1, TO_DATE('5-25-2016','MM-DD-YYYY'), '08:15', '11:30', 'n.B. 1.05');

insert INTO Termin values('POWRTCG1T1', 'POWRTC', 1, TO_DATE('4-7-2016','MM-DD-YYYY'), '12:30', '15:45', '1101a'); 
insert INTO Termin values('POWRTCG1T2', 'POWRTC', 1, TO_DATE('4-21-2016','MM-DD-YYYY'), '12:30', '15:45', '1101a');
insert INTO Termin values('POWRTCG1T3', 'POWRTC', 1, TO_DATE('4-28-2016','MM-DD-YYYY'), '12:30', '15:45', '1101a');
insert INTO Termin values('POWRTCG1T4', 'POWRTC', 1, TO_DATE('5-5-2016','MM-DD-YYYY'), '12:30', '15:45', '1101a');
insert INTO Termin values('POWRTCG1T5', 'POWRTC', 1, TO_DATE('5-19-2016','MM-DD-YYYY'), '12:30', '15:45', '1101a');
insert INTO Termin values('POWRTCG1T6', 'POWRTC', 1, TO_DATE('5-26-2016','MM-DD-YYYY'), '12:30', '15:45', '1101a');

insert INTO Termin values('POSRG1T1', 'POSR', 1, TO_DATE('4-7-2016','MM-DD-YYYY'), '08:15', '11:30', '0380b');
insert INTO Termin values('POSRG1T2', 'POSR', 1, TO_DATE('4-14-2016','MM-DD-YYYY'), '08:15', '11:30', '0380b');
insert INTO Termin values('POSRG1T3', 'POSR', 1, TO_DATE('4-28-2016','MM-DD-YYYY'), '08:15', '11:30', '0380b');
insert INTO Termin values('POSRG1T4', 'POSR', 1, TO_DATE('5-5-2016','MM-DD-YYYY'), '08:15', '11:30', '0380b');
insert INTO Termin values('POSRG1T5', 'POSR', 1, TO_DATE('5-12-2016','MM-DD-YYYY'), '08:15', '11:30', '0380b');
insert INTO Termin values('POSRG1T6', 'POSR', 1, TO_DATE('5-26-2016','MM-DD-YYYY'), '08:15', '11:30', '0380b');

/* Team: TeamID, FKuerzel, minMitglieder, maxMitglieder, grpNr, VorgGrpNr */ /* noch füllen */
insert INTO Team values(1, 'PRP1', 2, 3, null, 1);
insert INTO Team values(2, 'PRP1', 2, 3, 2, 2);
insert INTO Team values(3, 'PRP1', 2, 3, null, 2);
insert INTO Team values(24, 'PRP1', 2, 2, 4, 4);
insert INTO Team values(25, 'PRP1', 2, 2, 4, 4);
insert INTO Team values(4, 'MGP', 2, 3, null, 1);
insert INTO Team values(5, 'MGP', 2, 3, 3, 3);

insert INTO Team values(28, 'PRP2', 2, 2, 4, 4);
insert INTO Team values(29, 'PRP2', 2, 3, 4, 4);
insert INTO Team values(6, 'PRP2', 2, 3, null, 1);
insert INTO Team values(7, 'PRP2', 2, 3, null, 2);
insert INTO Team values(8, 'PRP2', 2, 3, 3, 3);
insert INTO Team values(9, 'PRP2', 2, 3, 3, 3);
insert INTO Team values(26, 'PRP2', 2, 3, 3, 3);
insert INTO Team values(27, 'PRP2', 2, 3, 3, 3);
insert INTO Team values(10, 'DBP', 2, 3, 3, 3);
insert INTO Team values(11, 'DBP', 2, 3, 3, 3);
insert INTO Team values(12, 'DBP', 2, 3, 2, 3);
insert INTO Team values(13, 'DBP', 2, 3, 1, 1);
insert INTO Team values(14, 'DBP', 2, 3, 2, 2);

insert INTO Team values(15, 'BSP', 3, 4, 2, 2);
insert INTO Team values(16, 'BSP', 3, 4, null, 1);
insert INTO Team values(17, 'GKAP', 2, 3, 3, 3);
insert INTO Team values(18, 'GKAP', 2, 3, 2, 2);

insert INTO Team values(19, 'WPDBDP', 1, 2, 1, 1);
insert INTO Team values(20, 'WPPNP', 1, 2, 1, 1);

insert INTO Team values(21, 'POCTF', 2, 3, 1, 1);
insert INTO Team values(22, 'POLA', 2, 3, null, 1);
insert INTO Team values(23, 'POLA', 2, 3, null, 1);

/* Pruefungsordnung: POKuerzel, POBezeichnung */
insert INTO Pruefungsordnung values('IPO2008', 'Informatik Pruefungsordnung 2008');
insert INTO Pruefungsordnung values('IPO2014', 'Informatik Pruefungsordnung 2014');
/* insert INTO Pruefungsordnung values('MTPO2013', 'Medientechnik Pruefungsordnung 2013'); */

/* Student: matrNr, usernanme, vorname, nachname, passwort, POKuerzel, strasse, hausNr, plz, ort,
gebDatum, gebOrt, email, FBKuerzel, DKuerzel */ 
/*5*5't, 5*3't, 5*2't Semestler, melden sich an Veranstaltung in ihrem Semester und mehrere in ein Semester davor */
insert INTO Student values(1111111, 'aaa001', 'Martin', 'Schmidt', 'aaa001', 'IPO2008', 'Kleine Strasse', 3, '21073', 'Hamburg', TO_DATE('12-5-1989','MM-DD-YYYY'), 'Berlin', 'martin.schmidt@haw-hamburg.de', 'AI', 'I');
insert INTO Student values(1111112, 'aaa002', 'Clara', 'Schulz', 'aaa002', 'IPO2008', 'Bleichestrasse', 54, '20343', 'Hamburg', TO_DATE('8-23-1995','MM-DD-YYYY'), 'Hamburg', 'clara.schulz@haw-hamburg.de', 'AI', 'I');
insert INTO Student values(1111113, 'aaa003', 'Achmed', 'Swolyski', 'aaa003', 'IPO2008', 'Kleine Born', 43, '22034', 'Hamburg', TO_DATE('7-13-1993','MM-DD-YYYY'), 'Moskau', 'achmed.swolsyki@haw-hamburg.de', 'AI', 'I');
insert INTO Student values(1111114, 'aaa004', 'James', 'Bond', 'aaa004', 'IPO2008', 'Buente', 26, '21075', 'Hamburg', TO_DATE('10-29-1989','MM-DD-YYYY'), 'Hamburg', 'james.bond@haw-hamburg.de', 'AI', 'I');
insert INTO Student values(1111115, 'aaa005', 'Patrick', 'Baecker', 'aaa005', 'IPO2008', 'Fliessweg', 666, '26545', 'Hamburg', TO_DATE('2-21-1987','MM-DD-YYYY'), 'Bremen', 'patrick.baecker@haw-hamburg.de', 'AI', 'I');
insert INTO Student values(1111116, 'aaa006', 'Sarah', 'Conner', 'aaa006', 'IPO2014', 'Srilanka', 44, '21000', 'Hamburg', TO_DATE('12-5-1989','MM-DD-YYYY'), 'Hamburg', 'sarah.conner@haw-hamburg.de', 'AI', 'I');
insert INTO Student values(1111117, 'aaa007', 'Erwin', 'Schmidt', 'aaa007', 'IPO2014', 'Grosse Strasse', 384, '21073', 'Hamburg', TO_DATE('12-16-1990','MM-DD-YYYY'), 'Hamburg', 'erwin.schmidt@haw-hamburg.de', 'AI', 'I');
insert INTO Student values(1111118, 'aaa008', 'Bully', 'Herbig', 'aaa008', 'IPO2008', 'Systemweg', 101, '11011', 'Hamburg', TO_DATE('6-19-1993','MM-DD-YYYY'), 'Berlin', 'bully.herbig@haw-hamburg.de', 'AI', 'I');
insert INTO Student values(1111119, 'aaa009', 'Chantal', 'Schroeder', 'aaa009', 'IPO2008', 'Schrödeweg', 31, '20343', 'Hamburg', TO_DATE('5-31-1994','MM-DD-YYYY'), 'Hamburg', 'chantal.schroeder@haw-hamburg.de', 'AI', 'I');
insert INTO Student values(1111120, 'aaa010', 'Johny', 'McBeal', 'aaa010', 'IPO2008', 'Statuenstrasse', 92, '23138', 'Hamburg', TO_DATE('7-13-1995','MM-DD-YYYY'), 'Florida', 'johnny-mcbeal@haw-hamburg.de', 'AI', 'I');
insert INTO Student values(1111121, 'aaa011', 'Natsu', 'Dragneel', 'aaa011', 'IPO2014', 'Feenweg', 777, '77777', 'Hamburg', TO_DATE('10-25-1991','MM-DD-YYYY'), 'Tokio', 'natsu.dragneel@haw-hamburg.de', 'AI', 'I');
insert INTO Student values(1111122, 'aaa012', 'Patrick', 'Schuller', 'aaa012', 'IPO2014', 'Brandstrasse', 112, '23456', 'Hamburg', TO_DATE('12-23-1988','MM-DD-YYYY'), 'Hamburg', 'patrick.schuller@haw-hamburg.de', 'AI', 'I');
insert INTO Student values(1111123, 'aaa013', 'Sercy', 'Lannister', 'aaa013', 'IPO2014', 'Königinnenweg', 12, '21432', 'Hamburg', TO_DATE('12-5-1989','MM-DD-YYYY'), 'Koenigslande', 'sercy.lannister@haw-hamburg.de', 'AI', 'I');
insert INTO Student values(1111124, 'aaa014', 'Bob', 'McCabe', 'aaa014', 'IPO2014', 'Ehestorferweg', 12, '21071', 'Hamburg', TO_DATE('12-5-1989','MM-DD-YYYY'), 'Schottland', 'bob.mccabe@haw-hamburg.de', 'AI', 'I');
insert INTO Student values(1111125, 'aaa015', 'Tim', 'Brahms', 'aaa015', 'IPO2014', 'Elderstrasse', 77, '24572', 'Hamburg', TO_DATE('12-5-1989','MM-DD-YYYY'), 'Hamburg', 'tim.brahms@haw-hamburg.de', 'AI', 'I');
insert INTO Student values(1111126, 'aaa016', 'Helge', 'Schneider', 'aaa016', 'IPO2014', 'Elderstrasse', 77, '24572', 'Hamburg', TO_DATE('12-5-1989','MM-DD-YYYY'), 'Hamburg', 'helge.scheider@haw-hamburg.de', 'AI', 'I');
insert INTO Student values(1111127, 'aaa017', 'Max', 'Mustermann', 'aaa017', 'IPO2014', 'Elderstrasse', 77, '24572', 'Hamburg', TO_DATE('12-5-1989','MM-DD-YYYY'), 'Hamburg', 'max.mustermann@haw-hamburg.de', 'AI', 'I');*/
insert INTO Student values(99999999, null, 'SYSTEM', 'SYSTEM', 'system', null, 'system', 0, '0', 'system', null, null, null, null, null);
/* Verwaltungsmitarbeiter: MAID, usernanme, vorname, nachname, passwort, strasse, hausNr, plz, ort,
gebDatum, gebOrt, email, DKuerzel */
insert INTO Verwaltungsmitarbeiter values(1, 'naa001', 'Maria', 'Albricht', 'naa001', 'Frankfurterallee', 45, '22742', 'Hamburg', TO_DATE('2-14-1982','MM-DD-YYYY'), 'Hamburg', 'maria.albricht@haw-hamburg.de', 'I');
insert INTO Verwaltungsmitarbeiter values(2, 'naa002', 'Jan', 'Meyer', 'naa002', 'Albrechtstrasse', 32, '21412', 'Hamnburg', TO_DATE('5-1-1985','MM-DD-YYYY'), 'Berlin', 'jan.meyer@haw-hamburg.de', 'I');


/* Teammitglied, TeamID, matrNr, bestaetigt */
insert INTO Teammitglied values(1, 1111121, 'true', 'false');
insert INTO Teammitglied values(1, 1111124, 'false', 'false');
insert INTO Teammitglied values(2, 1111123, 'true', 'false');
insert INTO Teammitglied values(2, 1111122, 'true', 'false');
insert INTO Teammitglied values(3, 1111125, 'true', 'false');
insert INTO Teammitglied values(3, 1111124, 'false', 'false');
insert INTO Teammitglied values(4, 1111122, 'true', 'false');
insert INTO Teammitglied values(4, 1111125, 'false', 'false');
insert INTO Teammitglied values(5, 1111123, 'true', 'false');
insert INTO Teammitglied values(5, 1111121, 'true', 'false');

insert INTO Teammitglied values(6, 1111118, 'true', 'false');
insert INTO Teammitglied values(6, 1111122, 'false', 'false');
insert INTO Teammitglied values(7, 1111117, 'true', 'false');
insert INTO Teammitglied values(7, 1111116, 'false', 'false');
insert INTO Teammitglied values(8, 1111125, 'true', 'false');
insert INTO Teammitglied values(8, 1111121, 'true', 'false');
insert INTO Teammitglied values(9, 1111120, 'true', 'false');
insert INTO Teammitglied values(9, 1111124, 'true', 'false');
insert INTO Teammitglied values(10, 1111122, 'true', 'false');
insert INTO Teammitglied values(10, 1111120, 'true', 'false');
insert INTO Teammitglied values(11, 1111118, 'true', 'false');
insert INTO Teammitglied values(11, 1111117, 'true', 'false');
insert INTO Teammitglied values(12, 1111121, 'true', 'false');
insert INTO Teammitglied values(12, 1111125, 'true', 'false');
insert INTO Teammitglied values(13, 1111123, 'true', 'false');
insert INTO Teammitglied values(13, 1111124, 'true', 'false');
insert INTO Teammitglied values(14, 1111119, 'true', 'false');
insert INTO Teammitglied values(14, 1111116, 'true', 'false');

insert INTO Teammitglied values(15, 1111118, 'true', 'false');
insert INTO Teammitglied values(15, 1111116, 'true', 'false');
insert INTO Teammitglied values(16, 1111120, 'true','false');
insert INTO Teammitglied values(16, 1111119, 'false', 'false');
insert INTO Teammitglied values(17, 1111118, 'true', 'false');
insert INTO Teammitglied values(17, 1111117, 'true', 'false');
insert INTO Teammitglied values(18, 1111119, 'true', 'false');
insert INTO Teammitglied values(18, 1111120, 'true', 'false');

insert INTO Teammitglied values(19, 1111112, 'true', 'false');
insert INTO Teammitglied values(19, 1111113, 'true', 'false');
insert INTO Teammitglied values(20, 1111114, 'true', 'false');
insert INTO Teammitglied values(20, 1111115, 'true', 'false');

insert INTO Teammitglied values(21, 1111111, 'true', 'false');
insert INTO Teammitglied values(21, 1111115, 'true', 'false');
insert INTO Teammitglied values(22, 1111113, 'true', 'false');
insert INTO Teammitglied values(22, 1111114, 'false', 'false');
insert INTO Teammitglied values(23, 1111112, 'true', 'false');
insert INTO Teammitglied values(23, 1111114, 'false', 'false');
insert INTO Teammitglied values(24, 1111118, 'true', 'false');
insert INTO Teammitglied values(24, 1111122, 'false', 'false');
insert INTO Teammitglied values(25, 1111115, 'true', 'false');
insert INTO Teammitglied values(25, 1111113, 'true', 'false');
insert INTO Teammitglied values(26, 1111119, 'false', 'false');
insert INTO Teammitglied values(26, 1111118, 'true', 'false');
insert INTO Teammitglied values(27, 1111114, 'true', 'false');
insert INTO Teammitglied values(27, 1111115, 'true', 'false');

/* Bewertung: MAID, matrNr, FKuerzel, DatumPvl, DatumNote, Pvl, Note */ /* noch füllen, 5*5't Semester, 5*3't Semester, alle fächer davor ausser, das Semester direkt darunter */

insert INTO Bewertung values(3, 1111111, 'PRP1', TO_DATE('1-13-2014','MM-DD-YYYY'), TO_DATE('2-5-2014','MM-DD-YYYY'), 'true', '12');
insert INTO Bewertung values(3, 1111112, 'PRP1', TO_DATE('1-13-2014','MM-DD-YYYY'), TO_DATE('2-5-2014','MM-DD-YYYY'), 'true', '14');
insert INTO Bewertung values(3, 1111113, 'PRP1', TO_DATE('1-13-2014','MM-DD-YYYY'), TO_DATE('2-5-2014','MM-DD-YYYY'), 'true', '8');
insert INTO Bewertung values(3, 1111114, 'PRP1', TO_DATE('1-13-2014','MM-DD-YYYY'), TO_DATE('2-5-2014','MM-DD-YYYY'), 'true', '13');
insert INTO Bewertung values(3, 1111115, 'PRP1', TO_DATE('1-13-2014','MM-DD-YYYY'), TO_DATE('2-5-2014','MM-DD-YYYY'), 'true', '6');
insert INTO Bewertung values(4, 1111111, 'MGP', TO_DATE('1-13-2014','MM-DD-YYYY'), TO_DATE('2-5-2014','MM-DD-YYYY'), 'true', '12');
insert INTO Bewertung values(4, 1111112, 'MGP', TO_DATE('1-13-2014','MM-DD-YYYY'), TO_DATE('2-5-2014','MM-DD-YYYY'), 'true', '11');
insert INTO Bewertung values(4, 1111113, 'MGP', TO_DATE('1-13-2014','MM-DD-YYYY'), TO_DATE('2-5-2014','MM-DD-YYYY'), 'true', '9');
insert INTO Bewertung values(4, 1111114, 'MGP', TO_DATE('1-13-2014','MM-DD-YYYY'), TO_DATE('2-5-2014','MM-DD-YYYY'), 'true', '7');
insert INTO Bewertung values(4, 1111115, 'MGP', TO_DATE('1-13-2014','MM-DD-YYYY'), TO_DATE('2-5-2014','MM-DD-YYYY'), 'true', '13');
insert INTO Bewertung values(3, 1111116, 'PRP1', TO_DATE('1-13-2015','MM-DD-YYYY'), TO_DATE('2-5-2015','MM-DD-YYYY'), 'true', '5');
insert INTO Bewertung values(3, 1111117, 'PRP1', TO_DATE('1-13-2015','MM-DD-YYYY'), TO_DATE('2-5-2015','MM-DD-YYYY'), 'true', '7');
insert INTO Bewertung values(3, 1111118, 'PRP1', TO_DATE('1-13-2015','MM-DD-YYYY'), TO_DATE('2-5-2015','MM-DD-YYYY'), 'true', '6');
insert INTO Bewertung values(3, 1111119, 'PRP1', TO_DATE('1-13-2015','MM-DD-YYYY'), TO_DATE('2-5-2015','MM-DD-YYYY'), 'true', '10');
insert INTO Bewertung values(3, 1111120, 'PRP1', TO_DATE('1-13-2015','MM-DD-YYYY'), TO_DATE('2-5-2015','MM-DD-YYYY'), 'true', '11');
insert INTO Bewertung values(4, 1111116, 'MGP', TO_DATE('1-13-2015','MM-DD-YYYY'), TO_DATE('2-5-2015','MM-DD-YYYY'), 'true', '12');
insert INTO Bewertung values(4, 1111117, 'MGP', TO_DATE('1-13-2015','MM-DD-YYYY'), TO_DATE('2-5-2015','MM-DD-YYYY'), 'true', '11');
insert INTO Bewertung values(4, 1111118, 'MGP', TO_DATE('1-13-2015','MM-DD-YYYY'), TO_DATE('2-5-2015','MM-DD-YYYY'), 'true', '9');
insert INTO Bewertung values(4, 1111119, 'MGP', TO_DATE('1-13-2015','MM-DD-YYYY'), TO_DATE('2-5-2015','MM-DD-YYYY'), 'true', '7');
insert INTO Bewertung values(4, 1111120, 'MGP', TO_DATE('1-13-2015','MM-DD-YYYY'), TO_DATE('2-5-2015','MM-DD-YYYY'), 'true', '13');
insert INTO Bewertung values(3, 1111121, 'PRP1', TO_DATE('1-13-2016','MM-DD-YYYY'), null, 'false', null);
insert INTO Bewertung values(3, 1111122, 'PRP1', TO_DATE('1-13-2016','MM-DD-YYYY'), null, 'false', null);
insert INTO Bewertung values(3, 1111123, 'PRP1', TO_DATE('1-13-2016','MM-DD-YYYY'), null, 'false', null);
insert INTO Bewertung values(3, 1111124, 'PRP1', TO_DATE('1-13-2016','MM-DD-YYYY'), null, 'false', null);
insert INTO Bewertung values(3, 1111125, 'PRP1', TO_DATE('1-13-2016','MM-DD-YYYY'), null, 'false', null);
insert INTO Bewertung values(4, 1111121, 'MGP', TO_DATE('1-13-2016','MM-DD-YYYY'), null, 'false', null);
insert INTO Bewertung values(4, 1111122, 'MGP', TO_DATE('1-13-2016','MM-DD-YYYY'), null, 'false', null);
insert INTO Bewertung values(4, 1111123, 'MGP', TO_DATE('1-13-2016','MM-DD-YYYY'), null, 'false', null);
insert INTO Bewertung values(4, 1111124, 'MGP', TO_DATE('1-13-2016','MM-DD-YYYY'), null, 'false', null);
insert INTO Bewertung values(4, 1111125, 'MGP', TO_DATE('1-13-2016','MM-DD-YYYY'), null, 'false', null);

insert INTO Bewertung values(3, 1111111, 'PRP2', null, null, 'false', '11');
insert INTO Bewertung values(3, 1111112, 'PRP2', TO_DATE('6-19-2014','MM-DD-YYYY'), TO_DATE('7-15-2014','MM-DD-YYYY'), 'true', '9');
insert INTO Bewertung values(3, 1111113, 'PRP2', TO_DATE('6-19-2014','MM-DD-YYYY'), TO_DATE('7-15-2014','MM-DD-YYYY'), 'true', '8');
insert INTO Bewertung values(3, 1111114, 'PRP2', TO_DATE('6-19-2014','MM-DD-YYYY'), TO_DATE('7-15-2014','MM-DD-YYYY'), 'true', '5');
insert INTO Bewertung values(3, 1111115, 'PRP2', TO_DATE('6-19-2014','MM-DD-YYYY'), TO_DATE('7-15-2014','MM-DD-YYYY'), 'true', '6');
insert INTO Bewertung values(4, 1111111, 'DBP', TO_DATE('6-19-2014','MM-DD-YYYY'), TO_DATE('7-15-2014','MM-DD-YYYY'), 'true', '15');
insert INTO Bewertung values(4, 1111112, 'DBP', TO_DATE('6-19-2014','MM-DD-YYYY'), TO_DATE('7-15-2014','MM-DD-YYYY'), 'true', '12');
insert INTO Bewertung values(4, 1111113, 'DBP', TO_DATE('6-19-2014','MM-DD-YYYY'), TO_DATE('7-15-2014','MM-DD-YYYY'), 'true', '9');
insert INTO Bewertung values(4, 1111114, 'DBP', TO_DATE('6-19-2014','MM-DD-YYYY'), TO_DATE('7-15-2014','MM-DD-YYYY'), 'true', '5');
insert INTO Bewertung values(4, 1111115, 'DBP', TO_DATE('6-19-2014','MM-DD-YYYY'), TO_DATE('7-15-2014','MM-DD-YYYY'), 'true', '8');
insert INTO Bewertung values(3, 1111116, 'PRP2', TO_DATE('6-19-2015','MM-DD-YYYY'), null, 'false', null);
insert INTO Bewertung values(3, 1111117, 'PRP2', TO_DATE('6-19-2015','MM-DD-YYYY'), null, 'false', null);
insert INTO Bewertung values(3, 1111118, 'PRP2', TO_DATE('6-19-2015','MM-DD-YYYY'), null, 'false', null);
insert INTO Bewertung values(3, 1111119, 'PRP2', TO_DATE('6-19-2015','MM-DD-YYYY'), null, 'false', null);
insert INTO Bewertung values(3, 1111120, 'PRP2', TO_DATE('6-19-2015','MM-DD-YYYY'), null, 'false', null);
insert INTO Bewertung values(4, 1111116, 'DBP', TO_DATE('6-19-2015','MM-DD-YYYY'), null, 'false', null);
insert INTO Bewertung values(4, 1111117, 'DBP', TO_DATE('6-19-2015','MM-DD-YYYY'), null, 'false', null);
insert INTO Bewertung values(4, 1111118, 'DBP', TO_DATE('6-19-2015','MM-DD-YYYY'), null, 'false', null);
insert INTO Bewertung values(4, 1111119, 'DBP', TO_DATE('6-19-2015','MM-DD-YYYY'), null, 'false', null);
insert INTO Bewertung values(4, 1111120, 'DBP', TO_DATE('6-19-2015','MM-DD-YYYY'), null, 'false', null);

insert INTO Bewertung values(1, 1111111, 'BSP', null, null, 'false', null);
insert INTO Bewertung values(1, 1111112, 'BSP', TO_DATE('1-13-2015','MM-DD-YYYY'), TO_DATE('2-5-2015','MM-DD-YYYY'), 'true', '12');
insert INTO Bewertung values(1, 1111113, 'BSP', TO_DATE('1-13-2015','MM-DD-YYYY'), TO_DATE('2-5-2015','MM-DD-YYYY'), 'true', '9');
insert INTO Bewertung values(1, 1111114, 'BSP', TO_DATE('1-13-2015','MM-DD-YYYY'), TO_DATE('2-5-2015','MM-DD-YYYY'), 'true', '10');
insert INTO Bewertung values(1, 1111115, 'BSP', TO_DATE('1-13-2015','MM-DD-YYYY'), TO_DATE('2-5-2015','MM-DD-YYYY'), 'true', '15');
insert INTO Bewertung values(2, 1111111, 'GKAP', TO_DATE('1-13-2015','MM-DD-YYYY'), TO_DATE('2-5-2015','MM-DD-YYYY'), 'true', '11');
insert INTO Bewertung values(2, 1111112, 'GKAP', TO_DATE('1-13-2015','MM-DD-YYYY'), TO_DATE('2-5-2015','MM-DD-YYYY'), 'true', '11');
insert INTO Bewertung values(2, 1111113, 'GKAP', TO_DATE('1-13-2015','MM-DD-YYYY'), TO_DATE('2-5-2015','MM-DD-YYYY'), 'true', '6');
insert INTO Bewertung values(2, 1111114, 'GKAP', TO_DATE('1-13-2015','MM-DD-YYYY'), TO_DATE('2-5-2015','MM-DD-YYYY'), 'true', '7');
insert INTO Bewertung values(2, 1111115, 'GKAP', TO_DATE('1-13-2015','MM-DD-YYYY'), TO_DATE('2-5-2015','MM-DD-YYYY'), 'true', '12');

insert INTO Bewertung values(4, 1111111, 'WPDBDP', TO_DATE('6-19-2015','MM-DD-YYYY'), null, 'false', null);
insert INTO Bewertung values(4, 1111112, 'WPDBDP', TO_DATE('6-19-2015','MM-DD-YYYY'), null, 'false', null);
insert INTO Bewertung values(4, 1111113, 'WPDBDP', TO_DATE('6-19-2015','MM-DD-YYYY'), null, 'false', null);
insert INTO Bewertung values(4, 1111114, 'WPDBDP', TO_DATE('6-19-2015','MM-DD-YYYY'), null, 'false', null);
insert INTO Bewertung values(4, 1111115, 'WPDBDP', TO_DATE('6-19-2015','MM-DD-YYYY'), null, 'false', null);
insert INTO Bewertung values(2, 1111111, 'WPPNP', TO_DATE('6-19-2015','MM-DD-YYYY'), null, 'false', null);
insert INTO Bewertung values(2, 1111112, 'WPPNP', TO_DATE('6-19-2015','MM-DD-YYYY'), null, 'false', null);
insert INTO Bewertung values(2, 1111113, 'WPPNP', TO_DATE('6-19-2015','MM-DD-YYYY'), null, 'false', null);
insert INTO Bewertung values(2, 1111114, 'WPPNP', TO_DATE('6-19-2015','MM-DD-YYYY'), null, 'false', null);
insert INTO Bewertung values(2, 1111115, 'WPPNP', TO_DATE('6-19-2015','MM-DD-YYYY'), null, 'false', null);


/* POVeranstaltungszuordnung:  POKuerzel, FKuerzel */
insert INTO POVeranstaltungszuordnung values('IPO2008', 'PRP1');
insert INTO POVeranstaltungszuordnung values('IPO2008', 'MGP');
insert INTO POVeranstaltungszuordnung values('IPO2008', 'PRP2');
insert INTO POVeranstaltungszuordnung values('IPO2008', 'DBP');
insert INTO POVeranstaltungszuordnung values('IPO2008', 'BSP');
insert INTO POVeranstaltungszuordnung values('IPO2008', 'GKAP');

insert INTO POVeranstaltungszuordnung values('IPO2014', 'PRP1');
insert INTO POVeranstaltungszuordnung values('IPO2014', 'MGP');
insert INTO POVeranstaltungszuordnung values('IPO2014', 'PRP2');
insert INTO POVeranstaltungszuordnung values('IPO2014', 'DBP');
insert INTO POVeranstaltungszuordnung values('IPO2014', 'BSP');
insert INTO POVeranstaltungszuordnung values('IPO2014', 'GKAP');

/*insert INTO Meldung values(1111112, 1111111, 'PRP1', 11, TO_DATE('6-19-2015','MM-DD-YYYY'), 'false', null, 'Teameinladung');*/


/* Meldung: absender, empfaenger, versandDatum, bestaetigt, bestaetigungsdatum, Typ */ /* noch füllen, = anzahl false in Teammitglieder */

/* commit um die Datenuebertragung festzulegen */
commit;

/* Alle Daten ca. 9 Stunden */
