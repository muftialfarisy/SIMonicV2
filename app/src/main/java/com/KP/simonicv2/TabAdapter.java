package com.KP.simonicv2;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.KP.simonicv2.Profile.ProfileFragment;
import com.KP.simonicv2.Reportc.ReportcFragment;
import com.KP.simonicv2.Reportm.ReportmFragment;
import com.KP.simonicv2.Suhu.SuhuFragment;

public class TabAdapter extends FragmentPagerAdapter {
    Context context;
    int totalTabs;
    public TabAdapter(@NonNull Context c, FragmentManager fm, int totalTabs) {
        super(fm, totalTabs);
        context = c;
        this.totalTabs = totalTabs;
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                ProfileFragment profile = new ProfileFragment();
                return profile;
            case 1:
                ReportcFragment reportc = new ReportcFragment();
                return reportc;
            case 2:
                ReportmFragment reportm = new ReportmFragment();
                return reportm;
            case 3:
                SuhuFragment suhu = new SuhuFragment();
                return suhu;
            case 4:
                PositionFragment posisi = new PositionFragment();
                return posisi;
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return totalTabs;
    }
}
