package it.smasini.utility.library.activities;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;

/**
 * Created by Simone Masini on 11/01/2018.
 */

public abstract class BaseActivity extends AppCompatActivity {

    private ContextCallback contextCallback;

    public void setContextCallback(ContextCallback contextCallback) {
        this.contextCallback = contextCallback;
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        if(contextCallback!=null){
            if(contextCallback.onContextItemSelected(item))
                return true;
        }
        return super.onContextItemSelected(item);
    }

    @Override
    public void onContextMenuClosed(Menu menu) {
        super.onContextMenuClosed(menu);
        if(contextCallback!=null)
            contextCallback.onContextClosed(menu);
    }

    public interface ContextCallback{
        void onContextClosed(Menu menu);
        boolean onContextItemSelected(MenuItem item);
    }
}
