package hu.aut.bme.householdbudget.activities;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import hu.aut.bme.androidwallet.R;

import hu.aut.bme.householdbudget.database.DatabaseManager;
import hu.aut.bme.householdbudget.database.DatabaseProperties;

/**
 * Created by David on 2016.11.23..
 */

public class SettingsActivity extends AppCompatActivity {



    private Button DeleteAllButton;

    private DatabaseManager DbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        DbHelper = new DatabaseManager(this);


        DeleteAllButton= (Button) findViewById(R.id.delete_all_button);



        DeleteAllButton.setOnClickListener(new View.OnClickListener(){

            public void onClick(View v) {
                SQLiteDatabase db = DbHelper.getWritableDatabase();
                db.delete(DatabaseProperties.TABLE_NAME,null,null);


            }


        });






    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.menu_settings, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();


        if (id == R.id.settings_menu_option_one) {
            Intent openMainActivity= new Intent(SettingsActivity.this, MainActivity.class);
            openMainActivity.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
            startActivity(openMainActivity);
            return  true;
        }

        if (id == R.id.settings_menu_option_two) {
            Intent openMainActivity= new Intent(SettingsActivity.this, SummaryActivity.class);
            openMainActivity.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
            startActivity(openMainActivity);
            return  true;
        }

        if (id == R.id.settings_menu_option_three) {
            Intent openMainActivity= new Intent(SettingsActivity.this, DiagrammActivity.class);
            openMainActivity.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
            startActivity(openMainActivity);
            return  true;
        }


        return super.onOptionsItemSelected(item);
    }


}