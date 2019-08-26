package com.northmeter.meshble.northmeter.activity;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.view.MenuItem;
import android.widget.Toast;

import com.northmeter.meshble.R;
import com.northmeter.meshble.camera.activity.CaptureActivity;

import static android.support.v4.app.ActivityCompat.requestPermissions;

/**
 * Created by dyd on 2017/8/29.
 */
public class NavigationItemListener implements NavigationView.OnNavigationItemSelectedListener{
    private final Activity mActivity;
    private final DrawerLayout drawerlayout;

    public NavigationItemListener(Activity activity, DrawerLayout drawerlayout) {
        mActivity = activity;
        this.drawerlayout = drawerlayout;
    }

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_camera) {
            // Handle the camera action
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                if(mActivity.checkSelfPermission(Manifest.permission.CAMERA)== PackageManager.PERMISSION_GRANTED&&
                        mActivity.checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION)== PackageManager.PERMISSION_GRANTED){
                    //method to get Images
                    Intent intent = new Intent();
                    intent.setClass(mActivity, CaptureActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    mActivity.startActivityForResult(intent, MainActivity.SCANNIN_GREQUEST_CODE);
                }else{
                    if(mActivity.shouldShowRequestPermissionRationale(Manifest.permission.CAMERA)){
                        Toast.makeText(mActivity,"Your Permission is needed to get access the camera or location",Toast.LENGTH_LONG).show();
                    }
                    mActivity.requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE,Manifest.permission.READ_EXTERNAL_STORAGE,
                            Manifest.permission.CAMERA,Manifest.permission.ACCESS_FINE_LOCATION}, MainActivity.REQUEST_CAMERARESULT );
                }
            }else{
                Intent intent = new Intent();
                intent.setClass(mActivity, CaptureActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                mActivity.startActivityForResult(intent, MainActivity.SCANNIN_GREQUEST_CODE);
            }
        } else if (id == R.id.nav_gallery) {

        } else if (id == R.id.nav_slideshow) {

        } else if (id == R.id.nav_manage) {

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }

        drawerlayout.closeDrawer(GravityCompat.START);
        return true;
    }



}
