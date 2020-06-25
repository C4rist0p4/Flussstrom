package com.example.myapplication.database.entiy;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class Meldungstyp {

    @PrimaryKey(autoGenerate = true)
    private int uid;

    public int idMeldungstyp;

    public String meldungsart;

    public String bemerkungMT;

    public String versandart;

    public Meldungstyp(int idMeldungstyp, String meldungsart, String bemerkungMT, String versandart) {
        this.idMeldungstyp = idMeldungstyp;
        this.meldungsart = meldungsart;
        this.bemerkungMT = bemerkungMT;
        this.versandart = versandart;
    }

    public int getUid() {
        return uid;
    }

    public void setUid(int uid) {
        this.uid = uid;
    }

    public int getIdMeldungstyp() {
        return idMeldungstyp;
    }

    public String getMeldungsart() {
        return meldungsart;
    }

    public String getBemerkungMT() {
        return bemerkungMT;
    }

    public String getVersandart() {
        return versandart;
    }
}
