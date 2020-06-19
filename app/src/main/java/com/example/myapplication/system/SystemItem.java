package com.example.myapplication.system;

public class SystemItem {
    private String systemName;
    private String systemOutput24;
    private String systemOutputAverage;

    public SystemItem(String name, String output24, String outputAverage) {
        systemName = name;
        systemOutput24 = output24;
        systemOutputAverage = outputAverage;
    }

    public String getName() { return systemName; }

    public String getOutput24() {
        return systemOutput24;
    }

    public String getOutputAverage() {
        return systemOutputAverage;
    }
}
