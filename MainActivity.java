package com.sheield.myapplication;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.SparseIntArray;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    Button button;
    private SparseIntArray mErrorString;
    public static float shake =0;
    private static final int REQUEST_PERMISSION=10;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mErrorString = new SparseIntArray();
        requestAppPermissions(new String[]{Manifest.permission.CALL_PHONE,
                Manifest.permission.SEND_SMS,
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION,
                Manifest.permission.INTERNET},R.string.msg,REQUEST_PERMISSION);

        button = (Button)findViewById(R.id.button);
        Button activityButton =(Button) findViewById(R.id.button);
        activityButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view)
            {
                Intent startIntent = new Intent(MainActivity.this,Main2Activity.class);
                startActivity(startIntent);
            }
        });

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();


        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
    }


    public  void onPermissionsGranted(int Requestcode){

    }

    public void requestAppPermissions(final String[]requestPermissions,final int stringId,final int requestCode){
        mErrorString.put(requestCode,stringId);
        int permissionCheck = PackageManager.PERMISSION_GRANTED;
        boolean showRequestPermission = false;
        for(String permission: requestPermissions){
            permissionCheck = permissionCheck + ContextCompat.checkSelfPermission(this,permission);
            showRequestPermission = showRequestPermission || ActivityCompat.shouldShowRequestPermissionRationale(this,permission);

        }
        if(permissionCheck!=PackageManager.PERMISSION_GRANTED){
            if(showRequestPermission){
                Snackbar.make(findViewById(android.R.id.content), stringId, Snackbar.LENGTH_INDEFINITE).setAction("GRANT", new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        ActivityCompat.requestPermissions(MainActivity.this,requestPermissions,requestCode);
                    }
                }).show();
            }else {
                ActivityCompat.requestPermissions(this,requestPermissions,requestCode);
            }
        }else {
            onPermissionsGranted(requestCode);
        }
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        int permissionCheck = PackageManager.PERMISSION_GRANTED;
        for(int permission: grantResults){
            permissionCheck = permissionCheck+permission;
        }
        if((grantResults.length>0)&& PackageManager.PERMISSION_GRANTED==permissionCheck){
            onPermissionsGranted(requestCode);
        }else {
            Snackbar.make(findViewById(android.R.id.content), mErrorString.get(requestCode),Snackbar.LENGTH_INDEFINITE).setAction("ENABLE", new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent i=new Intent();
                    i.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                    i.setData(Uri.parse("package:" + getPackageName()));
                    i.addCategory(Intent.CATEGORY_DEFAULT);
                    i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    i.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
                    i.addFlags(Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS);
                    startActivity(i);
                }
            }).show();
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
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.Gps) {
            Toast.makeText(MainActivity.this,"Gps clicked",Toast.LENGTH_LONG).show();
            startActivity(new Intent(this, Main6Activity.class));

        } else
        if (id == R.id.action_power_1) {
            Toast.makeText(this,"Power clicked",Toast.LENGTH_LONG).show();
            Intent in= new Intent(this,LocalService.class);
            startService(in);
        }else
        if (id == R.id.action_power_2) {
            Toast.makeText(this,"Power clicked",Toast.LENGTH_LONG).show();
            Intent in= new Intent(this,LocalService.class);
            stopService(in);
        }else
        if (id == R.id.action_shake_1) {

            Intent is=new Intent(MainActivity.this,Main7Activity.class);
            startActivity(is);
        }else
        if (id == R.id.action_shake_2) {

            Intent is=new Intent(this,Main7Activity.class);
            startActivity(is);
        }else
        if (id == R.id.Sensitivity) {

            OpenSensitivityDialog();
        }

        return super.onOptionsItemSelected(item);
    }

    private void OpenSensitivityDialog() {
        final AlertDialog.Builder popDialog = new AlertDialog.Builder(this);
        final SeekBar seekbar = new SeekBar(this);
        seekbar.setMax(40);
        seekbar.setProgress((int) shake);

        popDialog.setIcon(R.drawable.shake);
        popDialog.setTitle(" Please select sensitivity 1-100 ");
        popDialog.setView(seekbar);

        seekbar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                //set shake variable
                shake = (float)i;

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }

        });
        popDialog.setPositiveButton("OK",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }

                });


        popDialog.create();
        popDialog.show();
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        if (id == R.id.nav_camera) {
            Toast.makeText(MainActivity.this, "power clicked", Toast.LENGTH_LONG).show();
            startActivity(new Intent(this, Main3Activity.class));
        } else if (id == R.id.nav_gallery) {
            Toast.makeText(MainActivity.this, "Shake the Phone clicked", Toast.LENGTH_LONG).show();
            startActivity(new Intent(this, Main4Activity.class));
        } else if (id == R.id.nav_slideshow) {
            Toast.makeText(MainActivity.this, "Location Tracker clicked", Toast.LENGTH_LONG).show();
            startActivity(new Intent(this, Main5Activity.class));
        } else if(id == R.id.nav_manage){
            Toast.makeText(MainActivity.this, "About Us clicked", Toast.LENGTH_LONG).show();
            startActivity(new Intent(this,aboutus.class));
        }


        else if (id == R.id.nav_share){
            Toast.makeText(MainActivity.this, "share clicked", Toast.LENGTH_LONG).show();
            Intent myIntent=new Intent(Intent.ACTION_SEND);
            myIntent.setType("text/plain");
            String shareBody ="Your Body here";
            String shareSub ="Your subject here";
            myIntent.putExtra(Intent.EXTRA_SUBJECT,shareSub);
            myIntent.putExtra(Intent.EXTRA_SUBJECT,shareBody);
            startActivity(Intent.createChooser(myIntent, "share using"));
        }


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
