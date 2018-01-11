package it.smasini.utility.library.activities;

import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import it.smasini.utility.library.R;

/**
 * Created by smasini on 11/08/16.
 */
public abstract class DrawerActivity extends BaseActivity {

    protected DrawerLayout drawer;
    protected String tagFragmentDefault;
    protected int resTitleFragmentDefault;

    @Override
    public void onBackPressed() {
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            Fragment f = getSupportFragmentManager().findFragmentByTag(tagFragmentDefault);
            if(f == null || !f.isAdded()){
                createAndInsertFragment(tagFragmentDefault, resTitleFragmentDefault);
            }else{
                super.onBackPressed();
            }
        }
    }

    public void setDrawerToggle(Toolbar toolbar){
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();
    }

    public void setCustomActionBar(Toolbar toolbar){
        setSupportActionBar(toolbar);
        setDrawerToggle(toolbar);
    }

    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        int titleRes;
        String tag;
        DrawerFragmentType dft = defineTagAndTitle(id);
        if(dft!=null){
            titleRes = dft.getResTitle();
            tag = dft.getTag();
            if(!tag.equals("")){
                createAndInsertFragment(tag, titleRes);
                setChekedItem(id);
            }
        }
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public abstract DrawerFragmentType defineTagAndTitle(int id);
    public abstract Fragment getFragmentByTag(String tag);
    public abstract int getMainConteinerId();
    public abstract void setChekedItem(int id);

    private void createAndInsertFragment(String tag, int string){
        if(tag == null || tag.equals("")){
            return;
        }
        FragmentManager fm = getSupportFragmentManager();
        Fragment fragment = fm.findFragmentByTag(tag);
        if(fragment == null){
            fragment = getFragmentByTag(tag);
        }

        fm.beginTransaction()
                .replace(getMainConteinerId(), fragment, tag)
                .commit();
        setTitleActionBar(string);
    }

    public void setTitleActionBar(int titleRes){
        String title = getString(titleRes);
        setTitleActionBar(title);
    }

    public void setTitleActionBar(String title){
        ActionBar actionBar = getSupportActionBar();
        if(actionBar!=null){
            actionBar.setTitle(title);
        }
    }

}
