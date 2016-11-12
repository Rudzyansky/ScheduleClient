package ru.falseteam.schedule;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.vk.sdk.VKSdk;

import ru.falseteam.schedule.data.MainData;
import ru.falseteam.schedule.data.StaticData;
import ru.falseteam.schedule.data.VkData;
import ru.falseteam.schedule.listeners.Redrawable;
import ru.falseteam.schedule.listeners.Redrawer;
import ru.falseteam.schedule.management.FragmentManagement;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, Redrawable {

    private Fragment fragmentMain = new FragmentMain();
    private Fragment fragmentManagement = new FragmentManagement();
    private Fragment fragmentDebug = new FragmentDebug();

    private View navHeader;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        if (!getResources().getBoolean(R.bool.debug))
            navigationView.getMenu().removeItem(R.id.nav_debug);
        navigationView.setNavigationItemSelectedListener(this);
        navHeader = navigationView.getHeaderView(0);

        getSupportFragmentManager().beginTransaction().replace(R.id.content_main, fragmentMain).commit();
        navigationView.setCheckedItem(R.id.nav_main);

        Redrawer.add(this);
        redraw();

        if (!VKSdk.isLoggedIn())
            VKSdk.login(this);
        else
            VkData.vkUpdate();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        VkData.vkUpdate();
    }

    @Override
    public void redraw() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                // Обновление боковой менюшки
                ((TextView) navHeader.findViewById(R.id.group)).setText(MainData.getCurrentGroup().name());
                ((TextView) navHeader.findViewById(R.id.name)).setText(VkData.getName());
                ((ImageView) navHeader.findViewById(R.id.userIcon)).setImageBitmap(VkData.getUserIcon());
            }
        });
    }

    @Override
    protected void onDestroy() {
        Redrawer.remove(this);
        super.onDestroy();
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
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_about:
                showAboutDialog();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void showAboutDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(R.string.about);
        builder.setMessage(String.format(getString(R.string.about_message), StaticData.getClientVersion()));
        builder.setPositiveButton("Ok", null);

        builder.show();
    }


    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        switch (item.getItemId()) {
            case R.id.nav_main:
                ft.replace(R.id.content_main, fragmentMain);
                break;
            case R.id.nav_management:
                ft.replace(R.id.content_main, fragmentManagement);
                break;
            case R.id.nav_debug:
                ft.replace(R.id.content_main, fragmentDebug);
                break;
        }
        ft.commit();

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public void setFragment(Fragment fragment) {
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.content_main, fragment).commit();
    }
}
