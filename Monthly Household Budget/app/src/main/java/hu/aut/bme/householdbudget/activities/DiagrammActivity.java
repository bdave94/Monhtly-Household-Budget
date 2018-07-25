package hu.aut.bme.householdbudget.activities;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.google.android.gms.common.api.GoogleApiClient;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import hu.aut.bme.androidwallet.R;
import hu.aut.bme.householdbudget.database.DatabaseManager;
import hu.aut.bme.householdbudget.database.DatabaseProperties;

/**
 * Created by David on 2016.11.25.
 */

public class DiagrammActivity extends AppCompatActivity {

    private LineChart monthlyExpenseIncome;
    private BarChart expenseTypevalues;

    private DatabaseManager DbHelper;

    private GoogleApiClient client;

    private  ArrayList<Integer> incomeAmounts;
    private ArrayList<Integer> expenseAmounts;
    private ArrayList<Integer> expenseTypesAmounts;

    private int totalExpense;
    private int totalIncome;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_diagram);
        DbHelper = new DatabaseManager(this);

        incomeAmounts = new ArrayList<>();
        expenseAmounts = new ArrayList<>();
        expenseTypesAmounts = new ArrayList<>(5);

        monthlyExpenseIncome = (LineChart) findViewById(R.id.current_month_expinc_chart);
        expenseTypevalues =(BarChart) findViewById(R.id.expense_types_chart);

        loadDiagramData();
        setCharts();


    }

    private void setCharts(){
        List<Entry> monthlyExpenses = new ArrayList<Entry>();
        List<Entry> monthlyIncomes = new ArrayList<Entry>();

        for(int i=0; i< 12; i++) {

            monthlyExpenses.add(new Entry(expenseAmounts.get(i), i));
            monthlyIncomes.add(new Entry(incomeAmounts.get(i), i));
        }

        LineDataSet expenseDataSet = new LineDataSet(monthlyExpenses, "Expense");
        LineDataSet incomeDataSet = new LineDataSet(monthlyIncomes, "Income");

        expenseDataSet.setColor(Color.RED);
        expenseDataSet.setCircleColor(Color.RED);

        expenseDataSet.setAxisDependency(YAxis.AxisDependency.LEFT);
        incomeDataSet.setAxisDependency(YAxis.AxisDependency.LEFT);

        List<String> months = new ArrayList<String>();

        months.add("January");
        months.add("February");
        months.add("March");
        months.add("April");
        months.add("May");
        months.add("June");
        months.add("July");
        months.add("August");
        months.add("September");
        months.add("October");
        months.add("November");
        months.add("December");


        List<ILineDataSet> dataSets=new ArrayList<ILineDataSet>();
        dataSets.add(expenseDataSet);
        dataSets.add(incomeDataSet);

        LineData lineData = new LineData(months,dataSets);

        monthlyExpenseIncome.setData(lineData);
        monthlyExpenseIncome.invalidate();


        List<String> types = new ArrayList<String>();

        types.add("Housing");
        types.add("Transportation");
        types.add("Food");
        types.add("Entertainment");
        types.add("Other");



        List<BarEntry> expenseAmounts = new ArrayList<>();


        for (int j =0; j < 5; j++){
            expenseAmounts.add(new BarEntry(expenseTypesAmounts.get(j),j));

        }



        BarDataSet set = new BarDataSet(expenseAmounts, "Expenses");
        List<IBarDataSet> bardataSets=new ArrayList<IBarDataSet>();
        bardataSets.add(set);
        BarData data = new BarData(types,bardataSets);

        expenseTypevalues.setData(data);


        expenseTypevalues.invalidate();


    }

    public void loadDiagramData() {
        SQLiteDatabase db = DbHelper.getReadableDatabase();

        expenseAmounts.clear();
        incomeAmounts.clear();
        expenseTypesAmounts.clear();

        totalExpense=0;
        totalIncome=0;

        Calendar c = Calendar.getInstance();


        String currentyear= String.valueOf(c.get(Calendar.YEAR)) ;


        Cursor cursor;
        String  exporinc;
        String currentmonth;

        int housing=0;
        int transportation = 0;
        int food = 0;
        int entertainment = 0;
        int other = 0;

        for(int j=1; j< 13; j++) {

            if(j <10) {
                 currentmonth = "0"+String.valueOf(j) ;
            } else {
                 currentmonth = String.valueOf(j);

            }
            String[] selectionArgs = { currentyear + "-" + currentmonth + "%" };


             cursor = db.query(
                    DatabaseProperties.TABLE_NAME,                     // The table to query
                    null,                               // The columns to return
                    DatabaseProperties.COLUMN_DATE_TITLE + " LIKE ?",          // The columns for the WHERE clause
                    selectionArgs,                            // The values for the WHERE clause
                    null,                                     // don't group the rows
                    null,                                     // don't filter by row groups
                    null                                 // The sort order
            );

            int income = 0;
            int expense  = 0;




            cursor.moveToFirst();
            for (int i = 0; i < cursor.getCount(); i++) {

                exporinc = cursor.getString(
                        cursor.getColumnIndexOrThrow(DatabaseProperties.COLUMN_EXPENSEORINCOME_TITLE)
                );

                if (exporinc.equals("income")) {
                    income += cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseProperties.COLUMN_AMOUNT_TITLE));

                } else {
                    expense += cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseProperties.COLUMN_AMOUNT_TITLE));

                    if(j ==c.get(Calendar.MONTH)+1){
                        int typeExpense = cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseProperties.COLUMN_AMOUNT_TITLE));

                        switch (cursor.getString(cursor.getColumnIndexOrThrow(DatabaseProperties.COLUMN_TYPE_TITLE))){

                            case "Housing":
                                housing+=typeExpense;
                                break;
                            case "Transportation":
                                transportation+=typeExpense;
                                break;
                            case "Food":
                                food+=typeExpense;
                                break;
                            case "Entertainment":
                                entertainment+=typeExpense;
                                break;

                            case "Other":
                                other+=typeExpense;
                                break;



                        }

                    }


                }
                cursor.moveToNext();

            }
            incomeAmounts.add(income);
            expenseAmounts.add(expense);

            totalExpense+=expense;
            totalIncome+=income;



            cursor.close();
        }
        expenseTypesAmounts.add(housing);
        expenseTypesAmounts.add(transportation);
        expenseTypesAmounts.add(food);
        expenseTypesAmounts.add(entertainment);
        expenseTypesAmounts.add(other);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.menu_diagram, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();


        if (id == R.id.diagram_menu_option_one) {
            Intent openMainActivity= new Intent(DiagrammActivity.this, MainActivity.class);
            openMainActivity.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
            startActivity(openMainActivity);
            return  true;
        }

        if (id == R.id.diagram_menu_option_two) {
            Intent openMainActivity= new Intent(DiagrammActivity.this, SummaryActivity.class);
            openMainActivity.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
            startActivity(openMainActivity);
            return  true;
        }

        if (id == R.id.diagram_menu_option_three) {
            Intent openMainActivity= new Intent(DiagrammActivity.this, SettingsActivity.class);
            openMainActivity.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
            startActivity(openMainActivity);
            return  true;
        }


        return super.onOptionsItemSelected(item);
    }


}