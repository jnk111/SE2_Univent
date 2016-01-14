function Gruppe(grpNr, prof, assist, raum, maxTeams, resTeams, minTeams, anzTeams){

  this.grpNr = grpNr;
  this.professor = prof;
  this.assistent = assist;
  this.maxTeams = maxTeams;
  this.minTeams = minTeams;
  this.anzTeams = anzTeams;
  this.termine = [];
  this. teams = [];
}

// Uhrzeiten
function Time(h, m) {

  this.hours = h;
  this.min = m;

 /**
  this.getString = function() {

    var time = "";
    if (this.hours < 10) {
      time += "0";
    }
    return time += this.hours + ":" + this.min;
  }
  **/
}

// Termine
function Appointment(date, start, end, room) {

  var days = ["Sonntag", "Montag", "Dienstag", "Mittwoch", "Donnerstag", "Freitag", "Samstag"];

  this.date = date;
  // this.dateString = this.date.getString();

  this.timeStart = start;
  this.timeEnd = end;

  // this.timeStartString = this.timeStart.getString();

  // this.timeEndString = this.timeEnd.getString();

  // this.kw = "" + this.date.getWeek();


  // this.formattedDateString = days[this.date.getDay()] + ", " + this.dateString;
}


function Fach(fachKuerzel, fachBezeichnung, fachBereich, semester){

  this.fachKuerzel = fachKuerzel;
  this.fachBezeichnung = fachBezeichnung;
  this.fachBereich = fachBereich;
  this.semester = semester;
}


function Veranstaltung(fach, prof, anzTm, maxTm, anzGr, maxGr){

  this.fach = fach;
  this.professor = prof;
  this.gruppen = [];
  this.anzTm = anzTm;
  this.anzGr = anzGr;
  this.maxGr = maxGr;
}



function MatrikelNr(matrNr){
  this.matrNr = matrNr;
}

function MAID(maID){

  this.maID = maID

}

function EMail(email){
  this.email = email;
}

function PLZ(plz){
  this.plz = plz;
}

function Adresse(strasse, hausNr, plz, stadt, land){

  this.strasse = strasse;
  this.hausNr = hausNr;
  this.plz = plz;
  this.stadt = stadt;
  this.land = land;
}


function Angestellter(maID, vorname,
                  nachname, benutzername,
                  gebDatum, gebOrt,
                  adresse, department,
                  fachbereich, email){

  this.maID = maID;
  this.vorname = vorname;
  this.nachname = nachname;
  this.benutzername = benutzername;
  this.gebDatum = gebDatum;
  this. gebOrt = gebOrt;
  this.adresse = adresse;
  this.department = department;
  this.fachbereich = fachbereich;
  this.email = email;
}


function Student(matrNr, vorname,
                  nachname, benutzername,
                  gebDatum, gebOrt,
                  adresse, department,
                  fachbereich, email){

  this.matrNr = matrNr;
  this.vorname = vorname;
  this.nachname = nachname;
  this.benutzername = benutzername;
  this.gebDatum = gebDatum;
  this. gebOrt = gebOrt;
  this.adresse = adresse;
  this.department = department;
  this.fachbereich = fachbereich;
  this.email = email;
}
