package hu.aut.bme.householdbudget.activities;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import com.google.android.gms.common.api.GoogleApiClient;

import java.util.Calendar;

import hu.aut.bme.androidwallet.R;
import hu.aut.bme.householdbudget.database.DatabaseManager;
import hu.aut.bme.householdbudget.database.DatabaseProperties;

/**
 * Created by David on 2016.11.23..
 */

public class SummaryActivity extends AppCompatActivity {




    private int income;
    private int expense;


    TextView savingamount;
    TextView incomeamount;
    TextView expenseamount;

    DatabaseManager DbHelper;

    private GoogleApiClient client;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_summary);
        DbHelper = new DatabaseManager(this);





        incomeamount = (TextView) findViewById(R.id.income_this_month);
        savingamount = (TextView) findViewById(R.id.total_saving_amount);
        expenseamount = (TextView) findViewById(R.id.expense_this_month);

        loadSummaryData();





    }

    public void loadSummaryData() {
        SQLiteDatabase db = DbHelper.getReadableDatabase();

        income = 0;
        expense = 0;




        Calendar c = Calendar.getInstance();

        String currentmonth;
        if(c.get(Calendar.MONTH)+1 <10) {
            currentmonth = "0"+String.valueOf(c.get(Calendar.MONTH)+1) ;
        } else {
            currentmonth= String.valueOf(c.get(Calendar.MONTH)+1) ;

        }

        String currentyear= String.valueOf(c.get(Calendar.YEAR)) ;

        String[] projection = {
                DatabaseProperties.COLUMN_AMOUNT_TITLE
        };

        String[] selectionArgs = { currentyear + "-" + currentmonth + "%" };


        Cursor cursor = db.query(
                DatabaseProperties.TABLE_NAME,                     // The table to query
                null,                               // The columns to return
                DatabaseProperties.COLUMN_DATE_TITLE + " LIKE ?",          // The columns for the WHERE clause
                selectionArgs,                            // The values for the WHERE clause
                null,                                     // don't group the rows
                null,                                     // don't filter by row groups
                null                                 // The sort order
        );

        String  exporinc;


        cursor.moveToFirst();
        for(int i=0;i < cursor.getCount(); i++ ){
            exporinc = cursor.getString(
                    cursor.getColumnIndexOrThrow(DatabaseProperties.COLUMN_EXPENSEORINCOME_TITLE)
            );

            if(exporinc.equals("income")) {
                income+= cursor.getInt( cursor.getColumnIndexOrThrow(DatabaseProperties.COLUMN_AMOUNT_TITLE));

            }else{
                expense+= cursor.getInt( cursor.getColumnIndexOrThrow(DatabaseProperties.COLUMN_AMOUNT_TITLE));

            }
            cursor.moveToNext();
        }

        incomeamount.setText(String.valueOf(income));
        expenseamount.setText(String.valueOf(expense));
        savingamount.setText(String.valueOf(income-expense));

        if(income-expense<0){
            savingamount.setTextColor(Color.RED);
        }else{
            savingamount.setTextColor(Color.GREEN);
        }


    }




    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.menu_details, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();



        if (id == R.id.summary_menu_option_one) {
            Intent openMainActivity= new Intent(SummaryActivity.this, MainActivity.class);
            openMainActivity.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
            startActivity(openMainActivity);
            return  true;
        }

        if (id == R.id.summary_menu_option_two) {
            Intent openMainActivity= new Intent(SummaryActivity.this, DiagrammActivity.class);
            openMainActivity.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
            startActivity(openMainActivity);
            return  true;
        }

        if (id == R.id.summary_menu_option_three) {
            Intent openMainActivity= new Intent(SummaryActivity.this, SettingsActivity.class);
            openMainActivity.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
            startActivity(openMainActivity);
            return  true;
        }


        return super.onOptionsItemSelected(item);
    }


}
