package hu.aut.bme.householdbudget.activities;

import android.app.DatePickerDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.ToggleButton;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

import hu.aut.bme.androidwallet.R;
import hu.aut.bme.householdbudget.database.DatabaseManager;
import hu.aut.bme.householdbudget.database.DatabaseProperties;


public class MainActivity extends AppCompatActivity  {

    private DatePickerDialog fromDatePickerDialog;
    private SimpleDateFormat dateFormatter;

    private EditText nameEditText;
    private EditText amountEditText;

    private ToggleButton typeChooserButton;
    private Button saveButton;
    private Button dateButton;
    private LinearLayout listOfRows;
    private LayoutInflater inflater;

    private TextView dateText;


    private DatabaseManager DbHelper;

    private Spinner expenseType;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        DbHelper = new DatabaseManager(this);


        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);




        nameEditText = (EditText) findViewById(R.id.salary_name);
        dateText = (TextView) findViewById(R.id.current_date);
        amountEditText = (EditText) findViewById(R.id.salary_amount);
        typeChooserButton = (ToggleButton) findViewById(R.id.expense_or_income);
        saveButton = (Button) findViewById(R.id.save_button);
        dateButton= (Button) findViewById(R.id.date_button);
        listOfRows = (LinearLayout) findViewById(R.id.list_of_rows);
        expenseType = (Spinner)findViewById(R.id.type_selection);

        Calendar c = Calendar.getInstance();

        String currentmonth;
        String currentyear= String.valueOf(c.get(Calendar.YEAR));
        if(c.get(Calendar.MONTH)+1 <10) {
            currentmonth = "0"+String.valueOf(c.get(Calendar.MONTH)+1) ;
        } else {
            currentmonth= String.valueOf(c.get(Calendar.MONTH)+1) ;

        }
        dateText.setText(currentyear+"-"+currentmonth);



        String[] items = new String[]{"Housing", "Transportation", "Food", "Entertainment", "Other"};
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, items);
        expenseType.setAdapter(adapter);



        dateFormatter = new SimpleDateFormat("yyyy-MM-dd",Locale.US);
        setDateTimeField();


        inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        loadData();


        dateButton.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){

                fromDatePickerDialog.show();
            }


        });

        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(nameEditText.getText().toString().isEmpty() ||
                        amountEditText.getText().toString().isEmpty()) {


                    Snackbar.make(listOfRows,R.string.warn_message,Snackbar.LENGTH_LONG).show();


                    return;

                }

                boolean isExpense;
                String name, date,type;
                int amount;


                View rowItem = inflater.inflate(R.layout.salary_row,null);
                TextView rowItemDate = (TextView) rowItem.findViewById(R.id.row_date);
                ImageView icon = (ImageView) rowItem.findViewById(R.id.salary_direction_icon);

                TextView rowItemSalaryName = (TextView) rowItem.findViewById(R.id.row_salary_name);

                TextView rowItemSalaryAmount = (TextView) rowItem.findViewById(R.id.row_salary_amount);


                if(typeChooserButton.isChecked()) {
                    icon.setImageResource(R.drawable.income);
                    isExpense = false;
                    type = " ";


                } else {
                    icon.setImageResource(R.drawable.expense);
                    isExpense = true;
                    type = expenseType.getSelectedItem().toString();
                }



                rowItemSalaryName.setText(nameEditText.getText().toString());
                rowItemSalaryAmount.setText(amountEditText.getText().toString());
                rowItemDate.setText(dateText.getText().toString());

                amount = Integer.valueOf(amountEditText.getText().toString());
                name = nameEditText.getText().toString();
                date = dateText.getText().toString();

                saveData(isExpense,name,amount,date,type);

                listOfRows.addView(rowItem);



            }
        });

    }


    private void saveData(boolean isExpense, String name, int amount, String date, String type){
        SQLiteDatabase db = DbHelper.getReadableDatabase();

        ContentValues values = new ContentValues();

        if(isExpense) {
            values.put(DatabaseProperties.COLUMN_EXPENSEORINCOME_TITLE, "expense");
        } else{
            values.put(DatabaseProperties.COLUMN_EXPENSEORINCOME_TITLE, "income");
        }

        values.put(DatabaseProperties.COLUMN_NAME_TITLE, name);
        values.put(DatabaseProperties.COLUMN_AMOUNT_TITLE, amount);
        values.put(DatabaseProperties.COLUMN_DATE_TITLE, date);
        values.put(DatabaseProperties.COLUMN_TYPE_TITLE,type);

        long newRowId = db.insert(DatabaseProperties.TABLE_NAME, null, values);

    }



    private void loadData(){
        SQLiteDatabase db = DbHelper.getReadableDatabase();


        Cursor cursor = db.query(
                DatabaseProperties.TABLE_NAME,                     // The table to query
                null,                               // The columns to return
                null,                                // The columns for the WHERE clause
                null,                            // The values for the WHERE clause
                null,                                     // don't group the rows
                null,                                     // don't filter by row groups
                null                                 // The sort order
        );


        String exporinc;
        String name;
        String amount;
        String date;
        String type;

        cursor.moveToFirst();
        for(int i=0;i < cursor.getCount(); i++ ){
            exporinc = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseProperties.COLUMN_EXPENSEORINCOME_TITLE)
            );
            name = cursor.getString(
                    cursor.getColumnIndexOrThrow(DatabaseProperties.COLUMN_NAME_TITLE));

            amount = cursor.getString(
                    cursor.getColumnIndexOrThrow(DatabaseProperties.COLUMN_AMOUNT_TITLE)
            );

            date = cursor.getString(
                    cursor.getColumnIndexOrThrow(DatabaseProperties.COLUMN_DATE_TITLE)
            );

            type = cursor.getString(
                    cursor.getColumnIndexOrThrow(DatabaseProperties.COLUMN_TYPE_TITLE)
            );

            View rowItem = inflater.inflate(R.layout.salary_row, null);

            TextView rowItemDate = (TextView) rowItem.findViewById(R.id.row_date);
            ImageView icon = (ImageView) rowItem.findViewById(R.id.salary_direction_icon);

            TextView rowItemSalaryName = (TextView) rowItem.findViewById(R.id.row_salary_name);

            TextView rowItemSalaryAmount = (TextView) rowItem.findViewById(R.id.row_salary_amount);

            TextView rowItemType = (TextView) rowItem.findViewById(R.id.row_type);

            if(exporinc.equals("income"))
                icon.setImageResource(R.drawable.income);
            else
                icon.setImageResource(R.drawable.expense);



            rowItemSalaryName.setText(name);
            rowItemSalaryAmount.setText(amount);
            rowItemDate.setText(date);
            rowItemType.setText(type);

            listOfRows.addView(rowItem);

            cursor.moveToNext();

        }


    }

    private void setDateTimeField() {



        Calendar newCalendar = Calendar.getInstance();
        fromDatePickerDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {

            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                Calendar newDate = Calendar.getInstance();
                newDate.set(year, monthOfYear, dayOfMonth);
                dateText.setText(dateFormatter.format(newDate.getTime()));
            }

        },newCalendar.get(Calendar.YEAR), newCalendar.get(Calendar.MONTH), newCalendar.get(Calendar.DAY_OF_MONTH));


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        SQLiteDatabase db = DbHelper.getWritableDatabase();
        int id = item.getItemId();



        if (id == R.id.main_menu_option_one) {
            Intent openMainActivity= new Intent();
            openMainActivity.setClass(MainActivity.this, SummaryActivity.class);

            openMainActivity.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
            startActivity(openMainActivity);
            return  true;
        }

        if (id == R.id.main_menu_option_two) {
            Intent openMainActivity = new Intent();
            openMainActivity.setClass(MainActivity.this, DiagrammActivity.class);
            openMainActivity.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
            startActivity(openMainActivity);
            return  true;
        }


        if (id == R.id.main_menu_option_three) {
            Intent openMainActivity = new Intent();
            openMainActivity.setClass(MainActivity.this, SettingsActivity.class);
            openMainActivity.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
            startActivity(openMainActivity);
            return  true;
        }

        return super.onOptionsItemSelected(item);
    }





}
