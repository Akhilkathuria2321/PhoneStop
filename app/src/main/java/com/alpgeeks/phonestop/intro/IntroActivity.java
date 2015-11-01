package com.alpgeeks.phonestop.intro;

import android.content.Intent;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.support.v7.app.AppCompatActivity;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.Button;

import com.alpgeeks.phonestop.R;
import com.alpgeeks.phonestop.home.HomeActivity;


/**
 * Introduction activity for the user to understand features of PhoneStop.
 * There are 3 screens for the Intro. ZoomOutPageTransformer is used to make
 * transitions looks better.
 * Skip Button enabled to skip introduction and move to HomeActivity.
 */
public class IntroActivity extends FragmentActivity {
    private final String LOG_TAG = IntroActivity.class.getSimpleName();

    // Number of slides to show for app intro
    public static final int NUM_INTRO_SLIDES = 3;
    private ViewPager introPager;
    private PagerAdapter introPagerAdapter;

    private Button mSkipButton;

    private int imageArray[] = { R.drawable.intro_1, R.drawable.intro_2,
            R.drawable.intro_3};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_intro);

        // Initiate ViewPager and set Custom Slide Adapter
        introPager = (ViewPager) findViewById(R.id.pager);
        introPager.setPageTransformer(true, new ZoomOutPageTransformer());
        introPagerAdapter = new ScreenSlidePagerAdapter(getSupportFragmentManager());
        introPager.setAdapter(introPagerAdapter);

        mSkipButton = (Button)findViewById(R.id.skip_intro_button);
        mSkipButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(IntroActivity.this, HomeActivity.class);
                startActivity(intent);
            }
        });
    }

    public void onBackPressed() {
        if (introPager.getCurrentItem() == 0) {
            // If the user is currently looking at the first step, allow the system to handle the
            // Back button. This calls finish() on this activity and pops the back stack.
            super.onBackPressed();
        } else {
            // Otherwise, select the previous step.
            introPager.setCurrentItem(introPager.getCurrentItem() - 1);
        }
    }

    /**
     * A simple pager adapter that represents 3 ScreenSlidePageFragment objects, in
     * sequence.
     */
    private class ScreenSlidePagerAdapter extends FragmentStatePagerAdapter {
        public ScreenSlidePagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            Fragment fragment;
            switch (position) {
                case 0:
                    fragment = new IntroPage1Fragment();
                    mSkipButton.setText(getString(R.string.skip_message));
                    break;
                case 1:
                    fragment = new IntroPage2Fragment();
                    mSkipButton.setText(getString(R.string.skip_message));
                    break;
                case 2:
                    fragment = new IntroPage3Fragment();
                    mSkipButton.setText(getString(R.string.skip_message));
                    break;
                default:
                    fragment = new IntroPage1Fragment();
            }
            return fragment;
        }

        @Override
        public int getCount() {
            return NUM_INTRO_SLIDES;
        }    }

    /**
     * PageTransformer for App introduction.
     */
    private class ZoomOutPageTransformer implements ViewPager.PageTransformer {
        private static final float MIN_SCALE = 0.85f;
        private static final float MIN_ALPHA = 0.5f;

        public void transformPage(View view, float position) {
            int pageWidth = view.getWidth();
            int pageHeight = view.getHeight();

            if (position < -1) { // [-Infinity,-1)
                // This page is way off-screen to the left.
                view.setAlpha(0);

            } else if (position <= 1) { // [-1,1]
                // Modify the default slide transition to shrink the page as well
                float scaleFactor = Math.max(MIN_SCALE, 1 - Math.abs(position));
                float vertMargin = pageHeight * (1 - scaleFactor) / 2;
                float horzMargin = pageWidth * (1 - scaleFactor) / 2;
                if (position < 0) {
                    view.setTranslationX(horzMargin - vertMargin / 2);
                } else {
                    view.setTranslationX(-horzMargin + vertMargin / 2);
                }

                // Scale the page down (between MIN_SCALE and 1)
                view.setScaleX(scaleFactor);
                view.setScaleY(scaleFactor);

                // Fade the page relative to its size.
                view.setAlpha(MIN_ALPHA +
                        (scaleFactor - MIN_SCALE) /
                                (1 - MIN_SCALE) * (1 - MIN_ALPHA));

            } else { // (1,+Infinity]
                // This page is way off-screen to the right.
                view.setAlpha(0);
            }
        }
    }

    private class DepthPageTransformer implements ViewPager.PageTransformer {
        private static final float MIN_SCALE = 0.75f;

        public void transformPage(View view, float position) {
            int pageWidth = view.getWidth();

            if (position < -1) { // [-Infinity,-1)
                // This page is way off-screen to the left.
                view.setAlpha(0);

            } else if (position <= 0) { // [-1,0]
                // Use the default slide transition when moving to the left page
                view.setAlpha(1);
                view.setTranslationX(0);
                view.setScaleX(1);
                view.setScaleY(1);

            } else if (position <= 1) { // (0,1]
                // Fade the page out.
                view.setAlpha(1 - position);

                // Counteract the default slide transition
                view.setTranslationX(pageWidth * -position);

                // Scale the page down (between MIN_SCALE and 1)
                float scaleFactor = MIN_SCALE
                        + (1 - MIN_SCALE) * (1 - Math.abs(position));
                view.setScaleX(scaleFactor);
                view.setScaleY(scaleFactor);

            } else { // (1,+Infinity]
                // This page is way off-screen to the right.
                view.setAlpha(0);
            }
        }
    }
}
