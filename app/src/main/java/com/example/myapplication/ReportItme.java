package com.example.myapplication;

public class ReportItme {
    private String reportDate;
    private String meldungstyp;
    private String bemerkungMel;

    public ReportItme(String date, String meldung, String bemerkung) {
        reportDate = date;
        meldungstyp = meldung;
        bemerkungMel = bemerkung;
    }

    public String getDate() { return reportDate; }

    public String getMeldungstyp() {
        return meldungstyp;
    }

    public String getBemerkungMel() {
        return bemerkungMel;
    }
}
