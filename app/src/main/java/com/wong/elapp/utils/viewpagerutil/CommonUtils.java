package com.wong.elapp.utils.viewpagerutil;

import android.content.Context;
import android.view.animation.AccelerateInterpolator;

import androidx.viewpager.widget.ViewPager;
import androidx.viewpager2.widget.ViewPager2;

import java.lang.reflect.Field;

public class CommonUtils {

    private static FixedSpeedScroller mScroller = null;
    /**
     * 设置ViewPager的滑动时间
     * @param context
     * @param viewpager ViewPager控件
     * @param DurationSwitch 滑动延时
     */
    public static void controlViewPagerSpeed(Context context, ViewPager2 viewpager, int DurationSwitch) {
        try {
            Field mField;

            mField = ViewPager2.class.getDeclaredField("mScroller");
            mField.setAccessible(true);

            mScroller = new FixedSpeedScroller(context,
                    new AccelerateInterpolator());
            mScroller.setmDuration(DurationSwitch);
            mField.set(viewpager, mScroller);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
