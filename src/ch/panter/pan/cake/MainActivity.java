package ch.panter.pan.cake;

import ch.panter.pan.cake.R;

import android.os.Bundle;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.view.Menu;

public class MainActivity extends Activity {
	
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        // Get authentication token from preferences.
        SharedPreferences settings = getSharedPreferences(
                getString(R.string.preference_file_key), Context.MODE_PRIVATE);
        String auth_key = settings.getString("authToken", "");
        
        // Decide which Activity to start next depending on the
        // availability of an authentication token.
        Intent intent;
        if (auth_key.length() > 0) {
        	intent = new Intent(this, AuthenticationActivity.class);
        } else {
        	intent = new Intent(this, AuthenticationActivity.class);
        }
        startActivity(intent);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }
}
