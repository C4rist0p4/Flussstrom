package com.example.myapplication;

class ReportItem {
    private String reportDate;
    private String meldungstyp;
    private String bemerkungMel;

    ReportItem(String date, String meldung, String bemerkung) {
        reportDate = date;
        meldungstyp = meldung;
        bemerkungMel = bemerkung;
    }

    String getDate() { return reportDate; }

    String getMeldungstyp() {
        return meldungstyp;
    }

    String getBemerkungMel() {
        return bemerkungMel;
    }
}
