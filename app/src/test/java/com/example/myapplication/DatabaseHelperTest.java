package com.example.myapplication;

import android.os.Build;

import androidx.test.core.app.ApplicationProvider;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.HashMap;


@RunWith(RobolectricTestRunner.class)
@Config(sdk = {Build.VERSION_CODES.O_MR1})
public class DatabaseHelperTest {

    private DatabaseHelper db;

    public DatabaseHelperTest() {

    }

    @Before
    public void setup() {
        db = new DatabaseHelper(ApplicationProvider.getApplicationContext());
    }

    @Test
    public void User() {
        HashMap<String, String> userid = new HashMap<>();
        userid.put("idBenutzer", "5");
        db.addUser(userid);

        assertEquals(db.getUser().get(0), "5");
    }

    @Test
    public void SystemId() {
        HashMap<String, String> systemID = new HashMap<>();

        systemID.put("anlage","[{FK_Anlage: 7}]");

        db.addSystemId(systemID);

        assertEquals(db.getSystemId().get(0), "7");
    }

    @Test
    public void Messages() {
        String systemName = "1";
        HashMap<String, ArrayList> data = new HashMap();

        ArrayList<HashMap> mesAL = new ArrayList<>();
        HashMap<String, String> mes = new HashMap<>();
        mes.put("fk_anlagen", "1");
        mes.put("fk_meldungstyp", "3");
        mes.put("datum"," 2020-07-07");
        mes.put("bemerkungMel", "Schaltschranktuer wurde geoeffnet!!!");
        mesAL.add(mes);

        data.put("allmeldungen", mesAL);

        db.setMessages(systemName , data);

        assertEquals(db.getAllMessages("1").get(0).getBemerkungMel(), "Schaltschranktuer wurde geoeffnet!!!");
    }
}