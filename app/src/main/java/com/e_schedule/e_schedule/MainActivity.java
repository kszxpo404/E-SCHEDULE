package com.e_schedule.e_schedule;


import android.content.Intent;
import android.content.res.Configuration;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.NavUtils;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.util.Base64;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.e_schedule.e_schedule.fragments.fragment_detail_informasi;
import com.e_schedule.e_schedule.fragments.fragment_detail_jadwal;
import com.e_schedule.e_schedule.fragments.fragment_home;
import com.e_schedule.e_schedule.fragments.fragment_informasi;
import com.e_schedule.e_schedule.fragments.fragment_jadwal;


public class MainActivity extends BaseActivity {
    private static final String TAG = "";
    public String dimana = "";
    private NavigationView navigationView;
    private DrawerLayout drawerLayout;
    private ActionBarDrawerToggle actionBarDrawerToggle;
    String ngaran, base_foto;
    protected Cursor cursor;
    DatabaseHelper dbHelper;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        local_database();
        navigationView = (NavigationView) findViewById(R.id.navigation);
        drawerLayout = (DrawerLayout) findViewById(R.id.drawerLayout);
        setupDrawerContent(navigationView);
        actionBarDrawerToggle = setupDrawerToggle();
        drawerLayout.setDrawerListener(actionBarDrawerToggle);
        selectDrawerItem(navigationView.getMenu().getItem(0));
        identitas();
        Intent notificationIntent = getIntent();
        String  lempar = notificationIntent.getStringExtra("arah");
        String id = notificationIntent.getStringExtra("id");
        String pengalihan = "pengalihan;" + lempar;
        Log.i(TAG,pengalihan);
        String split[]  = pengalihan.split(";");
        Fragment fragment = null;
        FragmentManager fragmentManager = getSupportFragmentManager();
        Bundle args = new Bundle();
        if (split[1].equals(null)) {   } else {
         switch(split[1]) {
            case "informasi" :
                 fragment = fragment_detail_informasi.newInstance();
                 args.putString("informasi_id", id);
                fragment.setArguments(args);
                fragmentManager.beginTransaction().replace(R.id.flContent, fragment).addToBackStack("tag")
                        .commit();
                getToolbar().setTitle("Detail Informasi");
                break;

            case "jadwal" :
                fragment = fragment_detail_jadwal.newInstance();
                args.putString("jadwal_id", id);
                args.putString("status","kosong");
                fragment.setArguments(args);
                fragmentManager.beginTransaction().replace(R.id.flContent, fragment).addToBackStack("tag")
                        .commit();
                getToolbar().setTitle("Detail Jadwal");
                break;
              default :
         }
        }

}

    public void local_database(){
        dbHelper = new DatabaseHelper(this);
        try {
            dbHelper.createDataBase();
        } catch (Exception ioe) {
            Toast.makeText(getApplicationContext(), "Gagal", Toast.LENGTH_LONG).show();
        }
    }
    private void setupDrawerContent(final NavigationView navigationView) {
        navigationView.setNavigationItemSelectedListener(
                new NavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(MenuItem menuItem) {
                        selectDrawerItem(menuItem);
                        return true;
                    }
                });
    }

    private ActionBarDrawerToggle setupDrawerToggle() {
        return new ActionBarDrawerToggle(this, drawerLayout, getToolbar(), R.string.drawer_open, R.string.drawer_close);
    }

    public void selectDrawerItem(MenuItem menuItem) {
        Fragment fragment = null;
        switch (menuItem.getItemId()) {
            case R.id.menu_home:
               fragment = fragment_home.newInstance();
               break;
            case R.id.menu_jadwal:
                fragment = fragment_jadwal.newInstance();
                    break;
            case R.id.menu_info:
                fragment = fragment_informasi.newInstance();
                break;
            case R.id.logout:
                logout();
                break;
            default:
                break;
        }
if(menuItem.getItemId() == R.id.logout){
    stopService(new Intent(MainActivity.this, Notifikasi_Service.class));
    Intent keluar = new Intent(MainActivity.this, LOGIN.class);
    startActivity(keluar);
}else {

    FragmentManager fragmentManager = getSupportFragmentManager();
    fragmentManager.beginTransaction().replace(R.id.flContent, fragment).addToBackStack("tag")
            .commit();
    menuItem.setChecked(true);

    getToolbar().setTitle(menuItem.getTitle());




}
        drawerLayout.closeDrawers();
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        actionBarDrawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        actionBarDrawerToggle.onConfigurationChanged(newConfig);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (actionBarDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }
        if (item.getItemId() == android.R.id.home) {
            Intent intent = NavUtils.getParentActivityIntent(this);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            NavUtils.navigateUpTo(this, intent);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }




    @Override
    public void onBackPressed() {

        if(dimana.equals("detail jadwal")) {
            FragmentManager fragmentManager = getSupportFragmentManager();
            fragmentManager.beginTransaction().replace(R.id.flContent, fragment_jadwal.newInstance()).addToBackStack("tag")
                    .commit();
            getToolbar().setTitle("Jadwal");
            resetActionBar(false,
                    DrawerLayout.LOCK_MODE_UNLOCKED);
        } else  if(dimana.equals("detail informasi")) {
            FragmentManager fragmentManager = getSupportFragmentManager();
            fragmentManager.beginTransaction().replace(R.id.flContent, fragment_informasi.newInstance()).addToBackStack("tag")
                    .commit();
            getToolbar().setTitle("Informasi");
            resetActionBar(false,
                    DrawerLayout.LOCK_MODE_UNLOCKED);
        } else  if(dimana.equals("jadwal")) {
            FragmentManager fragmentManager = getSupportFragmentManager();
            fragmentManager.beginTransaction().replace(R.id.flContent, fragment_home.newInstance()).addToBackStack("tag")
                    .commit();
            getToolbar().setTitle("E-SCHEDULE");
            resetActionBar(false,
                    DrawerLayout.LOCK_MODE_UNLOCKED);
        } else  if(dimana.equals("informasi")) {
            FragmentManager fragmentManager = getSupportFragmentManager();
            fragmentManager.beginTransaction().replace(R.id.flContent, fragment_home.newInstance()).addToBackStack("tag")
                    .commit();
            getToolbar().setTitle("E-SCHEDULE");
            resetActionBar(false,
                    DrawerLayout.LOCK_MODE_UNLOCKED);
        }


    }

    public void logout(){
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        db.execSQL("delete from login");
        clear_jadwal();
        clear_informasi();
    }
    private void clear_jadwal() {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        db.execSQL("delete from jadwal");
    }
    private void clear_informasi(){
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        db.execSQL("delete from informasi");
    }
    public void identitas() {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        cursor = db.rawQuery("SELECT * FROM login", null);
        cursor.moveToFirst();
        if (cursor.getCount() == 0) {
        } else {
             ngaran =  cursor.getString(2).toString();
             base_foto = cursor.getString(11).toString();
        }
        View header = navigationView.getHeaderView(0);
        TextView nama = (TextView) header.findViewById(R.id.nama);
        ImageView foto = (ImageView) header.findViewById(R.id.foto);
            nama.setText(ngaran);
        if (base_foto.equals("PCFET0NUWVBFIEhUTUwgUFVCTElDICItLy9XM0MvL0RURCBIVE1MIDMuMiBGaW5hbC8vRU4iPgo8aHRtbD4KIDxoZWFkPgogIDx0aXRsZT5JbmRleCBvZiAvaW1hZ2VzL3VzZXJzPC90aXRsZT4KIDwvaGVhZD4KIDxib2R5Pgo8aDE+SW5kZXggb2YgL2ltYWdlcy91c2VyczwvaDE+CiAgPHRhYmxlPgogICA8dHI+PHRoIHZhbGlnbj0idG9wIj48aW1nIHNyYz0iL2ljb25zL2JsYW5rLmdpZiIgYWx0PSJbSUNPXSI+PC90aD48dGg+PGEgaHJlZj0iP0M9TjtPPUQiPk5hbWU8L2E+PC90aD48dGg+PGEgaHJlZj0iP0M9TTtPPUEiPkxhc3QgbW9kaWZpZWQ8L2E+PC90aD48dGg+PGEgaHJlZj0iP0M9UztPPUEiPlNpemU8L2E+PC90aD48dGg+PGEgaHJlZj0iP0M9RDtPPUEiPkRlc2NyaXB0aW9uPC9hPjwvdGg+PC90cj4KICAgPHRyPjx0aCBjb2xzcGFuPSI1Ij48aHI+PC90aD48L3RyPgo8dHI+PHRkIHZhbGlnbj0idG9wIj48aW1nIHNyYz0iL2ljb25zL2JhY2suZ2lmIiBhbHQ9IltQQVJFTlRESVJdIj48L3RkPjx0ZD48YSBocmVmPSIvaW1hZ2VzLyI+UGFyZW50IERpcmVjdG9yeTwvYT48L3RkPjx0ZD4mbmJzcDs8L3RkPjx0ZCBhbGlnbj0icmlnaHQiPiAgLSA8L3RkPjx0ZD4mbmJzcDs8L3RkPjwvdHI+Cjx0cj48dGQgdmFsaWduPSJ0b3AiPjxpbWcgc3JjPSIvaWNvbnMvaW1hZ2UyLmdpZiIgYWx0PSJbSU1HXSI+PC90ZD48dGQ+PGEgaHJlZj0iMjAxNi0wODEwMDMwODEzLmpwZyI+MjAxNi0wODEwMDMwODEzLmpwZzwvYT48L3RkPjx0ZCBhbGlnbj0icmlnaHQiPjIwMTYtMDgtMjMgMTY6NTMgIDwvdGQ+PHRkIGFsaWduPSJyaWdodCI+IDQ4SzwvdGQ+PHRkPiZuYnNwOzwvdGQ+PC90cj4KPHRyPjx0ZCB2YWxpZ249InRvcCI+PGltZyBzcmM9Ii9pY29ucy9pbWFnZTIuZ2lmIiBhbHQ9IltJTUddIj48L3RkPjx0ZD48YSBocmVmPSIyMDE2LTA4MTAwNDU1MzQuanBnIj4yMDE2LTA4MTAwNDU1MzQuanBnPC9hPjwvdGQ+PHRkIGFsaWduPSJyaWdodCI+MjAxNi0wOC0yMyAxNjo1NCAgPC90ZD48dGQgYWxpZ249InJpZ2h0Ij43LjJLPC90ZD48dGQ+Jm5ic3A7PC90ZD48L3RyPgo8dHI+PHRkIHZhbGlnbj0idG9wIj48aW1nIHNyYz0iL2ljb25zL2ltYWdlMi5naWYiIGFsdD0iW0lNR10iPjwvdGQ+PHRkPjxhIGhyZWY9IjIwMTYtMDgxMDA0NTYyNS5qcGciPjIwMTYtMDgxMDA0NTYyNS5qcGc8L2E+PC90ZD48dGQgYWxpZ249InJpZ2h0Ij4yMDE2LTA4LTIzIDE2OjU0ICA8L3RkPjx0ZCBhbGlnbj0icmlnaHQiPiAxN0s8L3RkPjx0ZD4mbmJzcDs8L3RkPjwvdHI+Cjx0cj48dGQgdmFsaWduPSJ0b3AiPjxpbWcgc3JjPSIvaWNvbnMvaW1hZ2UyLmdpZiIgYWx0PSJbSU1HXSI+PC90ZD48dGQ+PGEgaHJlZj0iMjAxNjA4MTAwMTI5MjQ1N2FiMTAxNDI1NjM3LmpwZyI+MjAxNjA4MTAwMTI5MjQ1N2FiMTAxNDI1NjM3LmpwZzwvYT48L3RkPjx0ZCBhbGlnbj0icmlnaHQiPjIwMTYtMDgtMjMgMTY6NTQgIDwvdGQ+PHRkIGFsaWduPSJyaWdodCI+IDU5SzwvdGQ+PHRkPiZuYnNwOzwvdGQ+PC90cj4KPHRyPjx0ZCB2YWxpZ249InRvcCI+PGltZyBzcmM9Ii9pY29ucy9pbWFnZTIuZ2lmIiBhbHQ9IltJTUddIj48L3RkPjx0ZD48YSBocmVmPSIyMDE2MDgxMDA0NTk1MTU3YWE5OGE3MTM5ZWYuanBnIj4yMDE2MDgxMDA0NTk1MTU3YWE5OGE3MTM5ZWYuanBnPC9hPjwvdGQ+PHRkIGFsaWduPSJyaWdodCI+MjAxNi0wOC0yMyAxNjo1NCAgPC90ZD48dGQgYWxpZ249InJpZ2h0Ij4gMzFLPC90ZD48dGQ+Jm5ic3A7PC90ZD48L3RyPgo8dHI+PHRkIHZhbGlnbj0idG9wIj48aW1nIHNyYz0iL2ljb25zL2ltYWdlMi5naWYiIGFsdD0iW0lNR10iPjwvdGQ+PHRkPjxhIGhyZWY9IjIwMTYwOTEzMDYxMjMyNTdkNzdjYjA0ZTA3Ni5qcGciPjIwMTYwOTEzMDYxMjMyNTdkNzdjYjA0ZTA3Ni5qcGc8L2E+PC90ZD48dGQgYWxpZ249InJpZ2h0Ij4yMDE2LTA5LTE3IDE2OjA0ICA8L3RkPjx0ZCBhbGlnbj0icmlnaHQiPiA2Mks8L3RkPjx0ZD4mbmJzcDs8L3RkPjwvdHI+Cjx0cj48dGQgdmFsaWduPSJ0b3AiPjxpbWcgc3JjPSIvaWNvbnMvaW1hZ2UyLmdpZiIgYWx0PSJbSU1HXSI+PC90ZD48dGQ+PGEgaHJlZj0iMjAxNjA5MTQxMTA4NDk1N2Q4Y2Q1MTgwYzJjLmpwZyI+MjAxNjA5MTQxMTA4NDk1N2Q4Y2Q1MTgwYzJjLmpwZzwvYT48L3RkPjx0ZCBhbGlnbj0icmlnaHQiPjIwMTYtMDktMTQgMTE6MDggIDwvdGQ+PHRkIGFsaWduPSJyaWdodCI+IDYySzwvdGQ+PHRkPiZuYnNwOzwvdGQ+PC90cj4KPHRyPjx0ZCB2YWxpZ249InRvcCI+PGltZyBzcmM9Ii9pY29ucy9pbWFnZTIuZ2lmIiBhbHQ9IltJTUddIj48L3RkPjx0ZD48YSBocmVmPSJwZXRpbmcucG5nIj5wZXRpbmcucG5nPC9hPjwvdGQ+PHRkIGFsaWduPSJyaWdodCI+MjAxNi0xMC0xMiAwMDo1OSAgPC90ZD48dGQgYWxpZ249InJpZ2h0Ij4yLjRLPC90ZD48dGQ+Jm5ic3A7PC90ZD48L3RyPgo8dHI+PHRkIHZhbGlnbj0idG9wIj48aW1nIHNyYz0iL2ljb25zL2ltYWdlMi5naWYiIGFsdD0iW0lNR10iPjwvdGQ+PHRkPjxhIGhyZWY9InRodW1ic18yMDE2LTA4MTAwMzA4MTMuanBnIj50aHVtYnNfMjAxNi0wODEwMDMwODEzLmpwZzwvYT48L3RkPjx0ZCBhbGlnbj0icmlnaHQiPjIwMTYtMDgtMjMgMTY6NTQgIDwvdGQ+PHRkIGFsaWduPSJyaWdodCI+IDIxSzwvdGQ+PHRkPiZuYnNwOzwvdGQ+PC90cj4KPHRyPjx0ZCB2YWxpZ249InRvcCI+PGltZyBzcmM9Ii9pY29ucy9pbWFnZTIuZ2lmIiBhbHQ9IltJTUddIj48L3RkPjx0ZD48YSBocmVmPSJ0aHVtYnNfMjAxNi0wODEwMDQ1NTM0LmpwZyI+dGh1bWJzXzIwMTYtMDgxMDA0NTUzNC5qcGc8L2E+PC90ZD48dGQgYWxpZ249InJpZ2h0Ij4yMDE2LTA4LTIzIDE2OjU0ICA8L3RkPjx0ZCBhbGlnbj0icmlnaHQiPjQuMUs8L3RkPjx0ZD4mbmJzcDs8L3RkPjwvdHI+Cjx0cj48dGQgdmFsaWduPSJ0b3AiPjxpbWcgc3JjPSIvaWNvbnMvaW1hZ2UyLmdpZiIgYWx0PSJbSU1HXSI+PC90ZD48dGQ+PGEgaHJlZj0idGh1bWJzXzIwMTYtMDgxMDA0NTYyNS5qcGciPnRodW1ic18yMDE2LTA4MTAwNDU2MjUuanBnPC9hPjwvdGQ+PHRkIGFsaWduPSJyaWdodCI+MjAxNi0wOC0yMyAxNjo1NCAgPC90ZD48dGQgYWxpZ249InJpZ2h0Ij44LjNLPC90ZD48dGQ+Jm5ic3A7PC90ZD48L3RyPgo8dHI+PHRkIHZhbGlnbj0idG9wIj48aW1nIHNyYz0iL2ljb25zL2ltYWdlMi5naWYiIGFsdD0iW0lNR10iPjwvdGQ+PHRkPjxhIGhyZWY9InRodW1ic18yMDE2MDgxMDAxMjkyNDU3YWIxMDE0MjU2MzcuanBnIj50aHVtYnNfMjAxNjA4MTAwMTI5MjQ1N2FiMTAxNDI1NjM3LmpwZzwvYT48L3RkPjx0ZCBhbGlnbj0icmlnaHQiPjIwMTYtMDgtMjMgMTY6NTQgIDwvdGQ+PHRkIGFsaWduPSJyaWdodCI+IDI0SzwvdGQ+PHRkPiZuYnNwOzwvdGQ+PC90cj4KPHRyPjx0ZCB2YWxpZ249InRvcCI+PGltZyBzcmM9Ii9pY29ucy9pbWFnZTIuZ2lmIiBhbHQ9IltJTUddIj48L3RkPjx0ZD48YSBocmVmPSJ0aHVtYnNfMjAxNjA4MTAwNDU5NTE1N2FhOThhNzEzOWVmLmpwZyI+dGh1bWJzXzIwMTYwODEwMDQ1OTUxNTdhYTk4YTcxMzllZi5qcGc8L2E+PC90ZD48dGQgYWxpZ249InJpZ2h0Ij4yMDE2LTA4LTIzIDE2OjU0ICA8L3RkPjx0ZCBhbGlnbj0icmlnaHQiPiAxNUs8L3RkPjx0ZD4mbmJzcDs8L3RkPjwvdHI+Cjx0cj48dGQgdmFsaWduPSJ0b3AiPjxpbWcgc3JjPSIvaWNvbnMvaW1hZ2UyLmdpZiIgYWx0PSJbSU1HXSI+PC90ZD48dGQ+PGEgaHJlZj0idGh1bWJzXzIwMTYwOTEzMDYxMjMyNTdkNzdjYjA0ZTA3Ni5qcGciPnRodW1ic18yMDE2MDkxMzA2MTIzMjU3ZDc3Y2IwNGUwNzYuanBnPC9hPjwvdGQ+PHRkIGFsaWduPSJyaWdodCI+MjAxNi0wOS0xNyAxNjowNCAgPC90ZD48dGQgYWxpZ249InJpZ2h0Ij4gMjhLPC90ZD48dGQ+Jm5ic3A7PC90ZD48L3RyPgo8dHI+PHRkIHZhbGlnbj0idG9wIj48aW1nIHNyYz0iL2ljb25zL2ltYWdlMi5naWYiIGFsdD0iW0lNR10iPjwvdGQ+PHRkPjxhIGhyZWY9InRodW1ic18yMDE2MDkxNDExMDg0OTU3ZDhjZDUxODBjMmMuanBnIj50aHVtYnNfMjAxNjA5MTQxMTA4NDk1N2Q4Y2Q1MTgwYzJjLmpwZzwvYT48L3RkPjx0ZCBhbGlnbj0icmlnaHQiPjIwMTYtMDktMTQgMTE6MDggIDwvdGQ+PHRkIGFsaWduPSJyaWdodCI+IDI4SzwvdGQ+PHRkPiZuYnNwOzwvdGQ+PC90cj4KICAgPHRyPjx0aCBjb2xzcGFuPSI1Ij48aHI+PC90aD48L3RyPgo8L3RhYmxlPgo8YWRkcmVzcz5BcGFjaGUvMi40LjcgKFVidW50dSkgU2VydmVyIGF0IGUtc2NoZWR1bGUucGFuZGlnLm5ldCBQb3J0IDgwPC9hZGRyZXNzPgo8L2JvZHk+PC9odG1sPgo=") ) {
            foto.setImageResource(R.drawable.icon);
        }else {
            Glide.with(MainActivity.this)
                    .load(Base64.decode(base_foto, Base64.DEFAULT))
                    .asBitmap()
                    .into(foto);
        }
    }



    public void resetActionBar(boolean childAction, int drawerMode)
    {
        if (childAction) {
            actionBarDrawerToggle.setDrawerIndicatorEnabled(false);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        } else {
            getSupportActionBar().setDisplayHomeAsUpEnabled(false);
            actionBarDrawerToggle.setDrawerIndicatorEnabled(true);
        }

        drawerLayout.setDrawerLockMode(drawerMode);
    }
}
