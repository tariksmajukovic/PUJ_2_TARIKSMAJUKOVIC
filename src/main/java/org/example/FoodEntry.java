package org.example;

public class FoodEntry {
    private String naziv;
    private int kalorije;
    private String date; // npr. "2026-01-22"

    public FoodEntry(String naziv, int kalorije, String date) {
        this.naziv = naziv;
        this.kalorije = kalorije;
        this.date = date;
    }

    public String getNaziv() {
        return naziv;
    }

    public int getKalorije() {
        return kalorije;
    }

    public String getDate() {
        return date;
    }

    public void setNaziv(String naziv) {
        this.naziv = naziv;
    }

    public void setKalorije(int kalorije) {
        this.kalorije = kalorije;
    }

    public void setDate(String date) {
        this.date = date;
    }

    @Override
    public String toString() {
        return naziv + " - " + kalorije + " kcal (" + date + ")";
    }
}

