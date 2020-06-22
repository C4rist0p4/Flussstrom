package com.example.myapplication.report;

public class ReportItem {
    private String reportDate;
    private String meldungstyp;
    private String bemerkungMel;

    public ReportItem(){}

    ReportItem(String date, String meldung, String bemerkung) {
        reportDate = date;
        meldungstyp = meldung;
        bemerkungMel = bemerkung;
    }

    String getDate() { return reportDate; }

    public void setDate(String date) { this.reportDate = date; }

    String getMeldungstyp() {
        return meldungstyp;
    }

    public void setMeldungstyp(String meldungstyp) { this.meldungstyp = meldungstyp; }

    public String getBemerkungMel() { return bemerkungMel; }

    public void setBemerkungMel(String bemerkungMel) { this.bemerkungMel = bemerkungMel; }
}
