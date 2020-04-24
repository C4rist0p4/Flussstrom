package com.example.myapplication;

class ReportItem {
    private String reportDate;
    private String meldungstyp;
    private String bemerkungMel;

    ReportItem(){}

    ReportItem(String date, String meldung, String bemerkung) {
        reportDate = date;
        meldungstyp = meldung;
        bemerkungMel = bemerkung;
    }

    String getDate() { return reportDate; }

    void setDate(String date) { this.reportDate = date; }

    String getMeldungstyp() {
        return meldungstyp;
    }

    void setMeldungstyp(String meldungstyp) { this.meldungstyp = meldungstyp; }

    String getBemerkungMel() { return bemerkungMel; }

    void setBemerkungMel(String bemerkungMel) { this.bemerkungMel = bemerkungMel; }
}
