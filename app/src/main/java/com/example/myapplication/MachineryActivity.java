package com.example.myapplication;

import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.google.android.gms.tasks.Task;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.functions.FirebaseFunctions;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;


public class MachineryActivity extends AppCompatActivity {
    private TabLayout tabLayout;
    private ViewPager viewPager;
    private AssetMasterData assetMasterData;
    private SystematicView systematicView;
    private ComponentView componentView;
    private Report report;
    private Pictures pictures;
    private Control control;
    private FirebaseFunctions mFunctions;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_machinery);
        tabLayout = findViewById(R.id.tabLayout);
        viewPager = findViewById(R.id.viewPager);

        assetMasterData = new AssetMasterData();
        systematicView = new SystematicView();
        componentView = new ComponentView();
        report = new Report();
        pictures = new Pictures();
        control = new Control();

        tabLayout.setupWithViewPager(viewPager);

        ViewPagerAdapter viewPagerAdaper = new ViewPagerAdapter(getSupportFragmentManager(), 0);
        viewPagerAdaper.addFragment(assetMasterData, "Anlagenstammdaten");
        viewPagerAdaper.addFragment(systematicView, "Systematische Ansicht");
        viewPagerAdaper.addFragment(componentView, "Komponentenbasierte Ansicht");
        viewPagerAdaper.addFragment(report, "Meldung");
        viewPagerAdaper.addFragment(pictures, "Bilder");
        viewPagerAdaper.addFragment(control, "Steuerung");

        viewPager.setAdapter(viewPagerAdaper);
    }

    private class ViewPagerAdapter extends FragmentPagerAdapter {

        private List<Fragment> fragmentList = new ArrayList<>();
        private List<String> fragmentTitle = new ArrayList<>();

        ViewPagerAdapter(@NonNull FragmentManager fm, int behavior) {
            super(fm, behavior);
        }

        void addFragment(Fragment fragment, String title){
            fragmentList.add(fragment);
            fragmentTitle.add(title);
        }

        @NonNull
        @Override
        public Fragment getItem(int position) {
            return fragmentList.get(position);
        }

        @Override
        public int getCount() {
            return fragmentList.size();
        }

        @Nullable
        @Override
        public CharSequence getPageTitle(int position) {
            return fragmentTitle.get(position);
        }
    }

}





