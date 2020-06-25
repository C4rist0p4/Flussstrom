package com.example.myapplication.database.entiy;

import androidx.room.Entity;
import androidx.room.PrimaryKey;
import androidx.room.TypeConverter;
import androidx.room.TypeConverters;

import com.example.myapplication.database.converter.MeldungstypeConverter;

import java.util.HashMap;
import java.util.List;


@Entity
public class Meldungen {

    @PrimaryKey(autoGenerate = true)
    private int uid;

    private String SystemName;

    private String datum;

    //@TypeConverters(MeldungstypeConverter.class)
    private String meldungstyp;

    private String bemerkungMel;

    private String timestamp_device;

    public Meldungen(String SystemName, String datum, String meldungstyp, String bemerkungMel, String timestamp_device) {
        this.SystemName = SystemName;
        this.datum = datum;
        this.meldungstyp = meldungstyp;
        this.bemerkungMel = bemerkungMel;
        this.timestamp_device = timestamp_device;
    }

    public void setSystemName(String systemName) {
        SystemName = systemName;
    }

    public void setMeldungstyp(String meldungstyp) {
        this.meldungstyp = meldungstyp;
    }

    public void setUid(int uid) {
        this.uid = uid;
    }

    public String getBemerkungMel() {
        return bemerkungMel;
    }

    public int getUid() {
        return uid;
    }

    public String getSystemName() {
        return SystemName;
    }

    public String getDatum() {
        return datum;
    }

    public String getMeldungstyp() {
        return meldungstyp;
    }
    public String getTimestamp_device() {
        return timestamp_device;
    }
}
