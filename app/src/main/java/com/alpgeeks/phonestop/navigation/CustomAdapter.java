package com.alpgeeks.phonestop.navigation;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.alpgeeks.phonestop.R;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

/**
 * Created by dexter on 10-28-2015.
 */
public class CustomAdapter extends BaseAdapter {
    ArrayList<com.alpgeeks.phonestop.model.PackageInfo> applist;
    Context context;
    int [] imageId;
    static ArrayList<String> blockedPackages = new ArrayList<String>();
    private static LayoutInflater inflater=null;
    public CustomAdapter(Activity mainActivity, ArrayList<com.alpgeeks.phonestop.model.PackageInfo> app) {
        // TODO Auto-generated constructor stub
        applist=app;
        context=mainActivity;
        // imageId=prgmImages;
        inflater = ( LayoutInflater )context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        //String ret = "";

        try {
            InputStream inputStream = mainActivity.openFileInput("Profile.ini");

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



    }
    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return applist.size() ;
    }

    @Override
    public Object getItem(int position) {
        // TODO Auto-generated method stub
        return position;
    }

    @Override
    public long getItemId(int position) {
        // TODO Auto-generated method stub
        return position;
    }

    public class Holder
    {
        TextView tv;
        ImageView img;
        CheckBox app;
    }
    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        // TODO Auto-generated method stub
        final Holder holder=new Holder();
        View rowView;
        rowView = inflater.inflate(R.layout.fragment_app_list_display, null);
        holder.tv=(TextView) rowView.findViewById(R.id.app_list_display);
        holder.app=(CheckBox)rowView.findViewById(R.id.app_selected);
        holder.img=(ImageView) rowView.findViewById(R.id.imageView1);
        holder.tv.setText(applist.get(position).appname);
        //Drawable myDrawable = context.getResources();
        for (String blockedPackage: blockedPackages
                ) {
            if(blockedPackage.equalsIgnoreCase(applist.get(position).pname))
            {
                holder.app.setChecked(true);
                applist.get(position).selected=true;
            }
        }
        holder.img.setImageDrawable(applist.get(position).icon);
        //holder.img.setImageResource(applist.get(position).icon);
        holder.app.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                applist.get(position).selected=holder.app.isChecked();
            }
        });
        rowView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                Toast.makeText(context, "You Clicked " + applist.get(position).pname,
                        Toast.LENGTH_SHORT).show();
                // AppsActivity ap = new AppsActivity();
                //ap.set(applist.get(position).pname+"."+applist.get(position).appname);
                Toast.makeText(context, "Disabled " + applist.get(position).pname,
                        Toast.LENGTH_SHORT).show();
            }
        });

        return rowView;
    }
}
