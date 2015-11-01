package com.alpgeeks.phonestop.navigation;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.IntentService;
import android.app.usage.UsageStats;
import android.app.usage.UsageStatsManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;

import com.alpgeeks.phonestop.home.LockScreenActivity;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.SortedMap;
import java.util.TreeMap;

/**
 * An {@link IntentService} subclass for handling asynchronous task requests in
 * a service on a separate handler thread.
 * <p/>
 * TODO: Customize class - update intent actions, extra parameters and static
 * helper methods.
 */
public class AppBlockService extends IntentService {
    android.os.Handler mHandler;
    private boolean status;
    // TODO: Rename actions, choose action names that describe tasks that this
    // IntentService can perform, e.g. ACTION_FETCH_NEW_ITEMS
    private static final String ACTION_FOO = "com.example.dexter.login.action.FOO";
    private static final String ACTION_BAZ = "com.example.dexter.login.action.BAZ";

    // TODO: Rename parameters
    private static final String EXTRA_PARAM1 = "com.example.dexter.login.extra.PARAM1";
    private static final String EXTRA_PARAM2 = "com.example.dexter.login.extra.PARAM2";

    /**
     * Starts this service to perform action Foo with the given parameters. If
     * the service is already performing a task this action will be queued.
     *
     * @see IntentService
     */
    // TODO: Customize helper method
    public static void startActionFoo(Context context, String param1, String param2) {
        Intent intent = new Intent(context, AppBlockService.class);
        intent.setAction(ACTION_FOO);
        intent.putExtra(EXTRA_PARAM1, param1);
        intent.putExtra(EXTRA_PARAM2, param2);
        context.startService(intent);
    }

    /**
     * Starts this service to perform action Baz with the given parameters. If
     * the service is already performing a task this action will be queued.
     *
     * @see IntentService
     */
    // TODO: Customize helper method
    public static void startActionBaz(Context context, String param1, String param2) {
        Intent intent = new Intent(context, AppBlockService.class);
        intent.setAction(ACTION_BAZ);
        intent.putExtra(EXTRA_PARAM1, param1);
        intent.putExtra(EXTRA_PARAM2, param2);
        context.startService(intent);
    }

    public AppBlockService() {
        super("AppControl");

        mHandler = new Handler(Looper.getMainLooper());

    }
    static String dataString;
    String activityOnTop;
    String blockApp;
    @Override
    protected void onHandleIntent(Intent intent) {


        /*Profile.ini reading */
        ArrayList<String> blockedPackages = new ArrayList<String>();
        //String ret = "";

        try {
            InputStream inputStream = openFileInput("Profile.ini");

            if ( inputStream != null ) {
                InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
                BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
                String receiveString = "";
                //StringBuilder stringBuilder = new StringBuilder();

                while ( (receiveString = bufferedReader.readLine()) != null ) {
                    blockedPackages.add(receiveString);
                }

                inputStream.close();
                //ret = stringBuilder.toString();
            }
        }
        catch (FileNotFoundException e) {
            Log.e("Prashant", "File not found: " + e.toString());
        } catch (IOException e) {
            Log.e("Prashant", "Can not read file: " + e.toString());
        }


        status=true;

        while(status) {


            ActivityManager activity1 = (ActivityManager) getApplicationContext().getSystemService(Context.ACTIVITY_SERVICE);
            List<ActivityManager.RunningTaskInfo> runningTask1 = activity1.getRunningTasks(10);
            List<ActivityManager.RunningAppProcessInfo> runningTask12 = activity1.getRunningAppProcesses();
            //  List<ActivityManager.RunningServiceInfo> runningTask123 = activity1.getR(40);
          /*  for(ActivityManager.RunningServiceInfo r : runningTask123)
            {
                if( r.service.getPackageName().equals("com.whatsapp")) {
                    Log.v("KILL Before", r.service.getPackageName());

                    activity1.killBackgroundProcesses("com.whatsapp");
                    Log.v("KILL", r.service.getPackageName());
                }
                Log.v("Prashant", r.service.getPackageName());
            }
*/



            if(Integer.valueOf(Build.VERSION.SDK_INT)>=21) {
                String topPackageName = null;
                long ts = System.currentTimeMillis();
                UsageStatsManager usage = (UsageStatsManager) getApplicationContext().getSystemService(Context.USAGE_STATS_SERVICE);
                long time = System.currentTimeMillis();
                List<UsageStats> stats = usage.queryUsageStats(UsageStatsManager.INTERVAL_DAILY, ts-2000, ts);
                if (stats != null) {
                    SortedMap<Long, UsageStats> runningTask = new TreeMap<Long, UsageStats>();
                    for (UsageStats usageStats : stats) {
                        runningTask.put(usageStats.getLastTimeUsed(), usageStats);
                    }
                    if (runningTask.isEmpty()) {
                        // return null;
                    }

                    RecentUseComparator mRecentComp= new RecentUseComparator();
                    Collections.sort(stats, mRecentComp);
                    if(stats.size() >0) {
                        topPackageName = stats.get(0).getPackageName();
                        for (String packageName:blockedPackages
                                ) {

                            if (topPackageName.equals(packageName)) {

                                ActivityManager am = (ActivityManager)
                                        getApplicationContext().getSystemService(Context.ACTIVITY_SERVICE);
                                List<ActivityManager.RunningAppProcessInfo> pids = am.getRunningAppProcesses();
                                int processid = 0;
                                for (int i = 0; i < pids.size(); i++) {
                                    ActivityManager.RunningAppProcessInfo info = pids.get(i);
                                    if (info.processName.equalsIgnoreCase(topPackageName)) {
                                        processid = info.pid;
                                    }
                                }
                                //android.os.Process.killProcess(processid);
                                // activity1.killBackgroundProcesses(topPackageName);
                                //  activity1.restartPackage(topPackageName);
                                Intent i = new Intent(Intent.ACTION_MAIN);
                                i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                i.addCategory(Intent.CATEGORY_HOME);
                                startActivity(i);
                                ActivityManager activityManager = (ActivityManager) getApplicationContext().getSystemService(Activity.ACTIVITY_SERVICE);
                                activityManager.killBackgroundProcesses(packageName);


                               /* AlertDialog alertDialog = new AlertDialog.Builder().create();
                                alertDialog.setTitle("your title");
                                alertDialog.setMessage("your message");
                                alertDialog.setIcon(R.drawable.mr_ic_play_dark);
                                alertDialog.getWindow().setType(WindowManager.LayoutParams.TYPE_SYSTEM_ALERT);

                                alertDialog.show();*/
                                Intent dialogIntent = new Intent(getBaseContext(), LockScreenActivity.class);
                                dialogIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                getApplication().startActivity(dialogIntent);



                                //   try {
                                //       Thread.sleep(20000);
                                //   }catch (Exception e)
                                //   {}
                                // stopService(dialogIntent);

                               // broadcastIntent();
                                // mHandler.post(new DisplayToast(getApplicationContext(), "BlockedApp Started"));

                                // mHandler.post(new DisplayToast(this, "BlockedApp Started"));

                            }
                        }
                        // Log.v("TOP_PACKAGE", topPackageName);
                    }
                }
            }else
            {
                String topPackageName = null;
                ActivityManager activity = (ActivityManager) this.getSystemService(Context.ACTIVITY_SERVICE);
                List<ActivityManager.RunningTaskInfo> runningTask = activity.getRunningTasks(1);
                if (runningTask != null) {
                    ActivityManager.RunningTaskInfo taskTop = runningTask.get(0);
                    ComponentName componentTop = taskTop.topActivity;
                    topPackageName = componentTop.getPackageName();
                    if(topPackageName.equals(topPackageName))
                    {
                        // final Intent intent1 = new Intent(getApplicationContext(),Lock.class);
                        // startActivity(intent1);


                    }
                }
                Log.v("Prashant",topPackageName);
                //return topPackageName;
            }
            // return topPackageName;
            //mHandler.post(new DisplayToast(this, "BlockedApp Started"));
        }
    }

   /* public void broadcastIntent(){

        Intent intent = new Intent();
        intent.setAction("com.phonestop.CUSTOM_INTENT");
        sendBroadcast(intent);
    }*/
    /**
     * Handle action Foo in the provided background thread with the provided
     * parameters.
     */
    private void handleActionFoo(String param1, String param2) {
        // TODO: Handle action Foo
        throw new UnsupportedOperationException("Not yet implemented");
    }

    /**
     * Handle action Baz in the provided background thread with the provided
     * parameters.
     */
    private void handleActionBaz(String param1, String param2) {
        // TODO: Handle action Baz
        throw new UnsupportedOperationException("Not yet implemented");
    }
    @Override
    public void onDestroy() {
        status = false;
        super.onDestroy();
    }

}

class RecentUseComparator implements Comparator<UsageStats> {

    @Override
    public int compare(UsageStats lhs, UsageStats rhs) {
        return (lhs.getLastTimeUsed() > rhs.getLastTimeUsed()) ? -1 : (lhs.getLastTimeUsed() == rhs.getLastTimeUsed()) ? 0 : 1;
    }
}