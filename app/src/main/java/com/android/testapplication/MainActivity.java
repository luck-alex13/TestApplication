package com.android.testapplication;

import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.text.Spanned;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.android.testapplication.fragments.HelloFragment;
import com.android.testapplication.fragments.ListFragment;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.EnumSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.TimeZone;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.TimeUnit;

import io.realm.processor.Utils;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private FragmentManager fragmentManager;
    private Toolbar toolbar;
    private FragmentTransaction fragmentTransaction;
    private Timer mTimer;
    private TextView bonusTV, nextTV;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        fragmentManager = getSupportFragmentManager();

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        bonusTV = (TextView) navigationView.getHeaderView(0).findViewById(R.id.textViewTime);
        nextTV = (TextView) navigationView.getHeaderView(0).findViewById(R.id.textViewTimeNext);
        displayFragment(ListFragment.newInstance("some param"), false);
        bonusTV.setText(getFormattedDate());
        nextTV.setText(getFormattedDate(1496966400L * 1000));
        //startTimer();1496966400
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }


    public void displayFragment(Fragment fragment, boolean first) {
        if (fragment != null) {
            fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.content_main, fragment);
            if (!first) {
                fragmentTransaction.addToBackStack(null);
            }
            fragmentTransaction.commit();
        }
    }

    public void setActivityTitle(int toolbarTitle) {
        toolbar.setTitle(toolbarTitle);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_gallery) {
            displayFragment(ListFragment.newInstance("some param"), false);
        } else if (id == R.id.nav_slideshow) {
            displayFragment(new HelloFragment(), false);
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);

        return true;
    }

    public static String getFormattedDate() {
        Calendar cal = Calendar.getInstance(TimeZone.getTimeZone("UTC"));
        DateFormat sdf = new SimpleDateFormat("dd.MM.yyyy HH : mm");
        sdf.setTimeZone(TimeZone.getTimeZone("UTC"));
        Log.d("LOG_TAG", "getTimeInMillis " + cal.getTimeInMillis());
        return sdf.format(cal.getTime());
    }

    public static String getFormattedDate(long timeStamp) {
        Calendar calendar = Calendar.getInstance();
        Calendar cal = Calendar.getInstance(TimeZone.getTimeZone("UTC"));
        calendar.setTimeInMillis(timeStamp - cal.getTimeInMillis());
        DateFormat sdf = new SimpleDateFormat("dd.MM.yyyy HH : mm");
        sdf.setTimeZone(TimeZone.getTimeZone("UTC"));
        return sdf.format(calendar.getTime());
    }


    public static String getTimeLeft(long futureTimeMillis, long currentTimeMillis) {
        Calendar cal = Calendar.getInstance();
        cal.setTimeInMillis(futureTimeMillis);
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss", new Locale("ru", "RU"));
        sdf.setTimeZone(TimeZone.getTimeZone("GMT"));
        return sdf.format(new Date(futureTimeMillis));
       /* CharSequence charSequence = DateUtils.getRelativeTimeSpanString(futureTimeMillis, currentTimeMillis, DateUtils.FORMAT_ABBREV_TIME);
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss", new Locale("ru", "RU"));*/
        //p(TAG, "next -> " + getDate(futureTimeMillis / 1000) + " current -> " + getDate(currentTimeMillis / 1000));
        /*Map<TimeUnit, Long> diff = computeDiff(futureTimeMillis, currentTimeMillis);
        String time = diff.get(TimeUnit.HOURS) + ":" + diff.get(TimeUnit.MINUTES) + ":" + diff.get(TimeUnit.SECONDS);
        return time;*/
    }

    private void startTimer() {
        if (mTimer != null) {
            mTimer.cancel();
        }
        long timerEnd = System.currentTimeMillis() / 1000 + 30;
        final long nextDate = 1496966400;
        mTimer = new Timer();
       /* String strTime = String.valueOf(Utils.getTimeLeft(timerEnd *1000,Utils.getCurrentTimeInSecond()*1000));
        bonusTV.setText(strTime);*/
        mTimer.schedule(new TimerTask() {
            @Override
            public void run() {

                if (nextDate - getCurrentTimeInSecond() > 0) {
                    final String strTime = getTimeLeft(nextDate * 1000, getCurrentTimeInSecond() * 1000);
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            bonusTV.setText(String.format(getString(R.string.next_bonus), strTime));
                        }
                    });
                } else {

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            bonusTV.setText("Получи награду");
                        }
                    });

                    this.cancel();

                }
            }
        }, 1000, 1000);
    }

    private long getCurrentTimeInSecond() {
        return System.currentTimeMillis() / 1000;
    }

    private long getTimeUTC() {
        Calendar cal = Calendar.getInstance(TimeZone.getTimeZone("GMT"));
        Log.d("LOG_TAG", "getTimeInMillis " + cal.getTimeInMillis());
        return cal.getTimeInMillis();
    }

    public static Map<TimeUnit, Long> computeDiff(long futureTimeMillis, long currentTimeMillis) {
        long diffInMillies = futureTimeMillis - currentTimeMillis;
        List<TimeUnit> units = new ArrayList<TimeUnit>(EnumSet.allOf(TimeUnit.class));
        Collections.reverse(units);
        Map<TimeUnit, Long> result = new LinkedHashMap<TimeUnit, Long>();
        long milliesRest = diffInMillies;
        for (TimeUnit unit : units) {
            long diff = unit.convert(milliesRest, TimeUnit.MILLISECONDS);
            long diffInMilliesForUnit = unit.toMillis(diff);
            milliesRest = milliesRest - diffInMilliesForUnit;
            result.put(unit, diff);
        }
        return result;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_settings:
                Spanned text;
                if (Build.VERSION.SDK_INT >= 24) {
                    text = Html.fromHtml(getString(R.string.html_text), Html.FROM_HTML_MODE_LEGACY); // for 24 api and more
                } else {
                    text = Html.fromHtml( getString(R.string.html_text)); // or for older api
                }
                MaterialDialog.Builder builder = new MaterialDialog.Builder(this)
                        .title("title")
                        .content(text)
                        .positiveText("Хорошо")
                        .onPositive(new MaterialDialog.SingleButtonCallback() {
                            @Override
                            public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                                dialog.dismiss();
                            }
                        });
                builder.build().show();
                break;
            default:
                break;
        }
        return true;
    }


}
