package com.example.tiago.bancoderemedios.fragment.home;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.tiago.bancoderemedios.R;
import com.google.firebase.auth.FirebaseAuth;

public class FragmentHome extends Fragment {

    private FirebaseAuth mFirebaseAuth;

    private AppBarLayout appBar;
    private TabLayout tabs;
    private ViewPager viewPager;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        this.setTabs();
        this.setAppBar(container);
        this.setViewPager(view);
        this.setViewAdapter();

        return view;
    }

    private void setViewAdapter() {

        ViewPagerAdapter pa = new ViewPagerAdapter(getFragmentManager());
        this.viewPager.setAdapter(pa);
        this.tabs.setupWithViewPager(this.viewPager);
    }

    private void setViewPager(View view ) {

        this.viewPager = (ViewPager) view.findViewById(R.id.viewPagerHome);
    }

    private void setTabs() {

        this.tabs = new TabLayout(getActivity());
        tabs.setTabTextColors(Color.parseColor("#FFFFFF"), Color.parseColor("#FFFFFF"));
        tabs.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {

                switch (tab.getPosition()){
                    case 0: getActivity().setTitle("Aguardando Recebedores"); break;
                    case 1: getActivity().setTitle("Aguardando Donativos"); break;
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) { }

            @Override
            public void onTabReselected(TabLayout.Tab tab) { }
        });
    }

    private void setAppBar(ViewGroup container){

        View c = (View) container.getParent().getParent();
        this.appBar = (AppBarLayout) c.findViewById(R.id.appBarLayoutMain);
        this.appBar.addView(this.tabs);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        this.appBar.removeView(this.tabs);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        //getActivity().setTitle("Início");
    }

    public class ViewPagerAdapter extends FragmentStatePagerAdapter {

        String[] titulosTabs = {"Doações", "Necessidades"};
        private int NUM_TABS = 2;

        public ViewPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int i) {

            switch ( i ){

                case 0: return new DonativoTabFragment();
                case 1: return new NecessidadeTabFragment();
            }

            return null;
        }

        @Override
        public int getCount() {
            return NUM_TABS;
        }

        @Override
        public CharSequence getPageTitle(int position){
            return this.titulosTabs[position];
        }
    }
}