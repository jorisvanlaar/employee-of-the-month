package com.jorisvanlaar.employeeofthemonth;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

public class ViewPagerAdapter extends FragmentStatePagerAdapter {

    public ViewPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public int getCount() {
        return 2;
    }

    @Override
    public Fragment getItem(int i) {
        switch (i) {
            case 0:
                return new PhotoFragment();
            case 1:
                return new GalleryFragment();
            default:
                return null;
        }
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        switch (position) {
            case 0:
                return "Photo";
            case 1:
                return "Gallery";
            default:
                return null;
        }
    }

    @Override
    public int getItemPosition(@NonNull Object object) {
        if (object instanceof PhotoFragment) {
            return POSITION_UNCHANGED;
        }
        return POSITION_NONE;
    }
}
