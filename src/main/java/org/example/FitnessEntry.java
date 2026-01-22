package org.example;

public class FitnessEntry {
    private String aktivnost;
    private int trajanje; // u minutama
    private String intenzitet; // npr. "low", "medium", "high"
    private int kalorije; // izračunate kalorije
    private String date;

    public FitnessEntry(String aktivnost, int trajanje, String intenzitet, int kalorije, String date) {
        this.aktivnost = aktivnost;
        this.trajanje = trajanje;
        this.intenzitet = intenzitet;
        this.kalorije = kalorije;
        this.date = date;
    }

    public String getAktivnost() { return aktivnost; }
    public int getTrajanje() { return trajanje; }
    public String getIntenzitet() { return intenzitet; }
    public int getKalorije() { return kalorije; }
    public String getDate() { return date; }

    @Override
    public String toString() {
        return aktivnost + " (" + trajanje + " min, " + intenzitet + ") - " + kalorije + " kcal";
    }
}

