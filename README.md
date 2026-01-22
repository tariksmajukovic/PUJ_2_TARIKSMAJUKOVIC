Setup projekta

Otvorite projekt u IntelliJ IDEA.
Provjerite da imate instaliran Java 17 ili noviji i MongoDB.
Pokrenite lokalnu MongoDB bazu.
U kodu podesite konekciju na bazu u klasi MongoDBConnection.
Pokrenite aplikaciju kroz Main.java..

Opis funkcija
SleepTrackerForm: unos sati spavanja po danima i izračun prosjeka.
PrehranaService: unos hrane i kalorija, dohvat dnevnog unosa kalorija.
FitnessService: unos fizičkih aktivnosti i potrošenih kalorija.
TransactionManager: unos prihoda i rashoda, uključujući opis transakcije.
ProfileForm: prikaz svih korisničkih podataka – unesena hrana, aktivnosti, san i financije – u jedinstvenom prikazu sa skrolanjem.
Način korištenja

Pokrenite aplikaciju.
Registrujte ili se prijavite sa korisničkim imenom i lozinkom.
Dodajte unos hrane, aktivnosti, sati spavanja ili financijske transakcije.
Pregledajte sve podatke u ProfileForm gdje se prikazuje kompletan profil.
Koristite scroll da pregledate sve unose.
Dokumentacija i napomene

Podaci se čuvaju u MongoDB kolekcijama:
users: osnovni podaci, hrana i aktivnosti.
transactions: prihodi i rashodi sa opisom.
Aplikacija je modularna – svaka funkcionalnost ima svoju klasu.
UI je napravljen u Java Swingu sa modernim dizajnom i podrškom za skrolanje.
Zaključak

Ovaj projekt demonstrira slojevitu arhitekturu (UI, servisni sloj, baza podataka) i omogućava praćenje zdravlja i financija kroz jednostavan desktop interfejs. 
Modularni pristup olakšava održavanje i proširenje funkcionalnosti.
