package com.alpgeeks.phonestop.library;

import android.app.Activity;
import android.database.Cursor;
import android.net.Uri;

/**
 * Created by Akki on 10/26/2015.
 */
public class SmsReader {
    public static final String INBOX = "content://sms/inbox";
    public static boolean smsReceived(Activity activity) {
        Cursor cursor = activity.getContentResolver().query(Uri.parse(INBOX), null, null, null, null);

        String msgData = "";
        if (cursor.moveToFirst()) { // must check the result to prevent exception
            do {
              //  for(int idx=0;idx<cursor.getColumnCount();idx++)
               // {
                int idx=0;
                    msgData += " " + cursor.getColumnName(idx) + ":" + cursor.getString(idx);
               // }
                // use msgData
            } while (cursor.moveToNext());
        } else {
            // empty box, no SMS
        }

        System.out.println("Yes "+ msgData);
        return false;
    }
}
