package com.example.myapplication;

import android.os.Build;
import androidx.test.core.app.ApplicationProvider;

import com.google.gson.Gson;
import com.google.gson.internal.LinkedTreeMap;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;
import static org.junit.Assert.assertEquals;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
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

/*    @Test
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
    }*/

    @Test
    public void SystemDetails()  {
        InputStream in = this.getClass().getClassLoader().getResourceAsStream("SystemDetails.json");
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(in));

        Gson gson = new Gson();
        ArrayList sysDatails = gson.fromJson(bufferedReader, ArrayList.class);

        HashMap hmap = new HashMap<String, Object>();

        LinkedTreeMap<String, Object> ltm = (LinkedTreeMap) sysDatails.get(0);
        for (Object key : ltm.keySet()) {
            if (key.equals("fk_anlagentyp")) {
                HashMap h= new HashMap<String, Object>();
                h.put("leistung", 2);
                hmap.put(key, h);
            } else {
                hmap.put(key, ltm.get(key));
            }
        }

        ArrayList<HashMap> al = new ArrayList();
        al.add(hmap);

        db.setSystemDetails(al);

       HashMap getHmap = db.getSystemDetails("Neugattersleben");

        assertEquals(getHmap.get("installationsort"), "Neugattersleben");
    }
}