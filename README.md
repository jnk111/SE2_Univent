# Univent 1.3

Der Univent-Projekt Ordner mit allen nötigen Libaries [SQL-Files](/SQL) und der vorgesehenen Ordnerstruktur (localhost-Version).


Userguide:
====================
[Userguide - Univent v1.3](/Univent-Userguide.pdf)


Allgemeine Info
===============
<li><strong>Installation nach Clonen des Projektes:</strong>
 <ul>
  <li>Der Ordner backend_resources muss noch angelegt werden (Pfad: siehe .gitignore)</li>
  <li>Die Datei [database.config](/database.config) muss dann in diesen Ordner eingefuegt werden.</li>
  <li>Nur die beiden Zeilen A-Kennung und Passwort sind zu beachten</li>
  <li>Deine A-Kennung und ein Passwort jeweils dort eintragen (ohne Leerzeichen)
<li>Der Datenbankzugriff wird damit gewährleistet</li>
 </ul>
</li>


Setup
=====
1.  Eclipse Luna oder Mars EE (normales Luna oder Mars reicht nicht) 	herunterladen und starten.
2.  Clone dieses Repository.
3.  In Eclipse: File -> Import -> Existing Project into Workspace -> 
4.  "Select Root Directory" anwählen
5.  SE2-Praktikumssoftware-Ordner auswaehlen
6.  Finish klicken
7.  Tomcat 8 herunterladen und in Eclipse einrichten.

WICHTIG:
--------
In der Klasse se2.praktikum.projekt.datenimexport.Folders, die Dateipfade (String Konstanten) so ändern, dass sie in "/dein/pfad/zum/workspace/SE2-Praktikumssoftware" fuehren. Dort liegen auch die anderen benoetigten Ordner "backend_resources", "datenexport", etc, ...


