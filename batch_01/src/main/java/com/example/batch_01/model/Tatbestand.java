package com.example.batch_01.model;

import java.util.Date;

public class Tatbestand {
    public Tatbestand() {}
    public Tatbestand(Date tattag, String tatzeit, String tatort, String tatort2, String tatbestand, int betrag) {
        this.tattag = tattag;
        this.tatzeit = tatzeit;
        this.tatort = tatort;
        this.tatort2 = tatort2;
        this.tatbestand = tatbestand;
        this.betrag = betrag;
    }

    private Date tattag;
    private String tatzeit;
    private String tatort;
    private String tatort2;
    private String tatbestand;
    private int betrag;

    public Date getTattag() {
        return tattag;
    }

    public void setTattag(Date tattag) {
        this.tattag = tattag;
    }

    public String getTatzeit() {
        return tatzeit;
    }

    public void setTatzeit(String tatzeit) {
        this.tatzeit = tatzeit;
    }

    public String getTatort() {
        return tatort;
    }

    public void setTatort(String tatort) {
        this.tatort = tatort;
    }

    public String getTatort2() {
        return tatort2;
    }

    public void setTatort2(String tatort2) {
        this.tatort2 = tatort2;
    }

    public String getTatbestand() {
        return tatbestand;
    }

    public void setTatbestand(String tatbestand) {
        this.tatbestand = tatbestand;
    }

    public int getBetrag() {
        return betrag;
    }

    public void setBetrag(int betrag) {
        this.betrag = betrag;
    }
}



