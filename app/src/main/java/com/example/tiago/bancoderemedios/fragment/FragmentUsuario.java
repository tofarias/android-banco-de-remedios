package com.example.tiago.bancoderemedios.fragment;

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

public class FragmentUsuario extends Fragment {

    private AppBarLayout appBar;
    private TabLayout tabs;
    private ViewPager viewPager;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_usuario, container, false);

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

        this.viewPager = (ViewPager) view.findViewById(R.id.viewPagerVP);
    }

    private void setTabs() {

        this.tabs = new TabLayout(getActivity());
        tabs.setTabTextColors(Color.parseColor("#FFFFFF"), Color.parseColor("#FFFFFF"));
    }

    private void setAppBar(ViewGroup container){

        View c = (View) container.getParent().getParent();
        this.appBar = (AppBarLayout) c.findViewById(R.id.appBarLayoutABL);
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
        getActivity().setTitle(R.string.nav_header_user);
    }

    public class ViewPagerAdapter extends FragmentStatePagerAdapter{

        String[] titulosTabs = {"Dados", "Tab2", "Tab3"};

        public ViewPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int i) {

            switch ( i ){

                case 0: return new Tab_1_Fragment();
                case 1: return new Tab_2_Fragment();
                case 2: return new Tab_3_Fragment();

            }

            return null;
        }

        @Override
        public int getCount() {
            return 3;
        }

        @Override
        public CharSequence getPageTitle(int position){
            return this.titulosTabs[position];
        }
    }
}