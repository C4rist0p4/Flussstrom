package com.example.myapplication.database.converter;

import androidx.room.TypeConverter;

import com.example.myapplication.database.entiy.Meldungstyp;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.List;

public class MeldungstypeConverter {

    private static Gson gson = new Gson();
    private static Type type = new TypeToken<List<Meldungstyp>>(){}.getType();

    @TypeConverter
    public static Meldungstyp stringToMeldungstypData(String json) {
        return gson.fromJson(json, type);
    }

    @TypeConverter
    public static String meldungstypDataToString(HashMap meldungstyp) {
        return gson.toJson(meldungstyp, type);
    }
}
