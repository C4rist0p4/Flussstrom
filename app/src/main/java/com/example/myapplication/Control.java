package com.example.myapplication;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.ProgressBar;
import android.widget.Switch;
import android.widget.Toast;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;


public class Control extends Fragment {

    String ipAdresse = BuildConfig.ipAdresseEthernetrelais;
    String httpPort = BuildConfig.httpPort;
    Switch relay7;
    Switch relay8;
    ProgressBar progressBar;

    private static Control newInstance() {
        Control fragment = new Control();
        Bundle args = new Bundle();



        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_control, container, false);

        relay7 = view.findViewById(R.id.switch1);
        relay8 = view.findViewById(R.id.switch2);
        progressBar = view.findViewById(R.id.progressBar);

        HttpTask httpTask = new HttpTask(requireActivity().getApplicationContext(), progressBar, new OnEventListener<String>() {
            @Override
            public void onSuccess(String[] object) {

                try {
                    XmlPullParserFactory parserFactory = XmlPullParserFactory.newInstance();
                    XmlPullParser parser = parserFactory.newPullParser();
                    parser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, false);
                    InputStream stream = new ByteArrayInputStream(object[0].getBytes(StandardCharsets.UTF_8));

                    parser.setInput(stream, null);

                    ArrayList<String> relays = processParsing(parser);

                    if (relays.get(0).equals("ON")) {
                        relay7.setChecked(true);
                    } else {
                        relay7.setChecked(false);
                    }

                    if (relays.get(1).equals("ON")) {
                        relay8.setChecked(true);
                    } else {
                        relay8.setChecked(false);
                    }
                    setListener();

                } catch (XmlPullParserException | IOException e) {
                    e.printStackTrace();
                }
            }
            @Override
            public void onFailure(Exception e) {
                Toast.makeText(requireActivity().getApplicationContext(), "Connection Fail", Toast.LENGTH_LONG).show();
            }
        });

        httpTask.execute("http://" + ipAdresse + ":" + httpPort + "/status.xml");

        return view;
    }

    private void setListener () {
        relay7.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if(isChecked){
                sendCommand("http://" + ipAdresse + ":" + httpPort + "/status.xml?r7=1");
            }else {
                sendCommand("http://" + ipAdresse + ":" + httpPort + "/status.xml?r7=0");
            }
        });

        relay8.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if(isChecked){
                sendCommand("http://" + ipAdresse + ":" + httpPort + "/status.xml?r8=1");
            }else {
                sendCommand("http://" + ipAdresse + ":" + httpPort + "/status.xml?r8=0");
            }
        });
    }

    private void sendCommand(String URL) {
        HttpTask httpTask = new HttpTask(requireActivity().getApplicationContext(), progressBar, new OnEventListener<String>() {

            @Override
            public void onSuccess(String[] object) {
                Toast.makeText(requireActivity().getApplicationContext(), "Command Send", Toast.LENGTH_LONG).show();
            }

            @Override
            public void onFailure(Exception e) {
                Toast.makeText(requireActivity().getApplicationContext(), "Connection Fail", Toast.LENGTH_LONG).show();
            }
        });
        httpTask.execute(URL);
    }

    private ArrayList<String> processParsing(XmlPullParser parser) throws IOException, XmlPullParserException {

        ArrayList<String> relays = new  ArrayList<>();

        int eventType = parser.getEventType();
        while (eventType != XmlPullParser.END_DOCUMENT) {
            String relayName;

            relayName = parser.getName();

            if ("Relay7".equals(relayName) || "Relay8".equals(relayName)) {
                relays.add(parser.nextText());
            }

            eventType = parser.next();
        }
        return relays;
    }
}
