package com.example.dhiman.muse;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Handler;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.example.dhiman.muse.Adapters.SampleFragmentPagerAdapter;
import com.example.dhiman.muse.Fragments.Dashboard;
import com.example.dhiman.muse.Fragments.MiniPlayerFragment;
import com.example.dhiman.muse.Fragments.MyMusic;
import com.example.dhiman.muse.Models.ModelSong;
import com.example.dhiman.muse.app.ApplicationVariable;
import com.example.dhiman.muse.app.CommonMethods;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements Dashboard.onPlaySongListener {
    private DrawerLayout mDrawerLayout;
    private ProgressDialog dialog;
    private Dashboard dashboard;
    private MyMusic myMusic;
    private MiniPlayerFragment miniPlayerFragment;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if(ApplicationVariable.accoundData.token == null){
            Intent i =new Intent(this,LoginActivity.class);
            startActivity(i);
            finish();
        }
        initView();
        startService(new Intent(this, MusicService.class));
    }

    private void initView() {
        mDrawerLayout = findViewById(R.id.drawer_layout);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionbar = getSupportActionBar();
        actionbar.setDisplayHomeAsUpEnabled(true);
        actionbar.setHomeAsUpIndicator(R.drawable.ic_menu_black_24dp);

        // Get the ViewPager and set it's PagerAdapter so that it can display items
        new Handler().post(new Runnable() {
            @Override
            public void run() {
                ViewPager viewPager = (ViewPager) findViewById(R.id.viewpager);

                dashboard = new Dashboard();
                myMusic = new MyMusic();
                miniPlayerFragment = (MiniPlayerFragment) getSupportFragmentManager().findFragmentById(R.id.fragment_mini_player);
                viewPager.setAdapter(new SampleFragmentPagerAdapter(getSupportFragmentManager(),dashboard,myMusic));

                // Give the TabLayout the ViewPager
                TabLayout tabLayout = (TabLayout) findViewById(R.id.sliding_tabs);
                tabLayout.setupWithViewPager(viewPager);
            }
        });


        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(
                new NavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(MenuItem menuItem) {
                        // set item as selected to persist highlight
                        menuItem.setChecked(true);
                        // close drawer when item is tapped

                        // Add code here to update the UI based on the item selected
                        // For example, swap UI fragments here
                        if(menuItem.getItemId() == R.id.action_logout){
                            logoutFromAccount();
                        }
                        mDrawerLayout.closeDrawers();
                        return true;
                    }
                });
        mDrawerLayout.addDrawerListener(
                new DrawerLayout.DrawerListener() {
                    @Override
                    public void onDrawerSlide(View drawerView, float slideOffset) {
                        // Respond when the drawer's position changes
                    }

                    @Override
                    public void onDrawerOpened(View drawerView) {
                        // Respond when the drawer is opened
                    }

                    @Override
                    public void onDrawerClosed(View drawerView) {
                        // Respond when the drawer is closed
                    }

                    @Override
                    public void onDrawerStateChanged(int newState) {
                        // Respond when the drawer motion state changes
                    }
                }
        );
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                mDrawerLayout.openDrawer(GravityCompat.START);
                return true;

            case R.id.action_search:
                Toast.makeText(this,"search Called",Toast.LENGTH_SHORT).show();


        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.main_menu,menu);
        return true;
    }

    private void logoutFromAccount() {
        CommonMethods.logout(this);
        dialog = new ProgressDialog(this);
        dialog.setMessage("Logging out ...");
        dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        dialog.show();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                dialog.dismiss();
                startActivity(new Intent(MainActivity.this,LoginActivity.class));
                finish();
            }
        },1000);
    }

    @Override
    public void playSong(ArrayList<ModelSong> songList, int position) {
        if (miniPlayerFragment != null) {
            // If article frag is available, we're in two-pane layout...
            Log.d("DEBUG","PlayerCallback MainActivty: playlist= "+ songList +"position" + position);
            // Call a method in the ArticleFragment to update its content
           miniPlayerFragment.playSong(songList,position,this);
        } else {
            // Otherwise, we're in the one-pane layout and must swap frags...
            // Create fragment and give it an argument for the selected article
            MiniPlayerFragment newFragment = new MiniPlayerFragment();
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            newFragment.playSong(songList,position,this);
            // Replace whatever is in the fragment_container view with this fragment,
            // and add the transaction to the back stack so the user can navigate back
            transaction.replace(R.id.fragment_mini_player, newFragment);
            transaction.addToBackStack(null);
            // Commit the transaction
            transaction.commit();
        }
    }

    @Override
    protected void onDestroy() {
        stopService(new Intent(this,MusicService.class));
        super.onDestroy();
    }
}
