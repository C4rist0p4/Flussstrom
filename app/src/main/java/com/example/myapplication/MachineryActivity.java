package com.example.myapplication;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;
import java.util.List;

public class MachineryActivity extends AppCompatActivity {
    private TabLayout tabLayout;
    private ViewPager viewPager;
    private AssetMasterData assetMasterData;
    private SystematicView systematicView;
    private ComponentView componentView;
    private Report report;
    private Pictures pictures;
    private Control control;

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

        Intent intent = getIntent();
        String name = intent.getStringExtra("name");
        Bundle bundle = new Bundle();
        bundle.putString("SystemName", name);
        assetMasterData.setArguments(bundle);
        report.setArguments(bundle);
        componentView.setArguments(bundle);

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





