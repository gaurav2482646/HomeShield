package com.bricon.homeshield;

import android.app.PendingIntent;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.NavigationView;

import android.support.v4.app.NotificationManagerCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.NotificationCompat;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class NavigationActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener,AdapterView.OnItemClickListener {
    TextView nameTextView,welcomeTextView;
    SharedPreferences preferences;
    SharedPreferences.Editor editor;
    ListView listView;
    MyAdapter adapter;
    ArrayList<DataObject> arrayList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_navigation);
        preferences= PreferenceManager.getDefaultSharedPreferences(NavigationActivity.this);
        editor=preferences.edit();
        listView = (ListView) findViewById(R.id.listView);
        arrayList = new Utils().getSensorList();
        adapter = new MyAdapter();

        listView.setAdapter(adapter);
        listView.setOnItemClickListener(this);




       /* TextView name_navheader= (TextView) findViewById(R.id.nameTextVew);
        name_navheader.setText("d");*/
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

     /*   FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });*/

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);

        View header=navigationView.getHeaderView(0);
        nameTextView = (TextView)header.findViewById(R.id.nameTextVew);
        welcomeTextView = (TextView)findViewById(R.id.welcome);
        welcomeTextView.setText("Welcome "+preferences.getString(AppConstants.email,null));
        nameTextView.setText(preferences.getString(AppConstants.name,null));

        navigationView.setNavigationItemSelectedListener(this);

    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        Toast.makeText(NavigationActivity.this, "Under Development: "+arrayList.get(i).title, Toast.LENGTH_SHORT).show();
        showBigNotification(arrayList.get(i).title,"There is an alert regarding "+arrayList.get(i).title);
    }

    public  void showBigNotification(String data,String message) {
        int icon = R.drawable.logohomeshield;
        String notificationTitle = data;
        android.support.v4.app.NotificationCompat.Builder mBuilder = null;

        mBuilder = new NotificationCompat.Builder(getApplicationContext())
                .setSmallIcon(icon)
                .setStyle(new NotificationCompat.BigTextStyle().bigText(message))
                .setContentTitle(
                        notificationTitle)
                .setColor(Color.parseColor("#1976d2"))
                .setContentText(message).setAutoCancel(true);

        NotificationManagerCompat notificationManager = NotificationManagerCompat
                .from(getApplicationContext());
        notificationManager.notify(123, mBuilder.build());

    }



    class MyAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return arrayList.size();
        }

        @Override
        public Object getItem(int position) {
            return arrayList.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            LayoutInflater inflater = NavigationActivity.this.getLayoutInflater();
            convertView = inflater.inflate(R.layout.list_item_one, null);
            TextView index, value;
            LinearLayout background = (LinearLayout) convertView.findViewById(R.id.background);
            index = (TextView) convertView.findViewById(R.id.index);
            value = (TextView) convertView.findViewById(R.id.value);


            value.setText(arrayList.get(position).title);






            if(position%2==0) {
                background.setBackgroundColor(getResources().getColor(R.color.color1));


            }
            else {
                background.setBackgroundColor(getResources().getColor(R.color.color2));


            }
            return convertView;
        }
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.navigation, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.logout) {
            editor.putBoolean(AppConstants.registered,false).commit();

            startActivity(new Intent(NavigationActivity.this, LoginActivity.class));

            finish();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_camera) {
            // Handle the camera action
        } else if (id == R.id.nav_gallery) {

        } else if (id == R.id.nav_slideshow) {

        } else if (id == R.id.nav_manage) {

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
