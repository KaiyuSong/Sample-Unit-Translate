package com.example.kaiyusong.sampleunittranslate;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

public class EnergyActivity extends AppCompatActivity implements View.OnClickListener, AdapterView.OnItemSelectedListener {

    String[] Unit = new String[]{"J","kJ","cal","kcal","kWh"};
    double[] Value = new double[]{1.0,0.001,0.2388458966,0.0002388458966,0.0000002778};


    Spinner From, To;
    Button Calculate, Save, Back;
    EditText input;
    TextView output;
    int digit = 2;
    String result;
    int from_unit, to_unit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_energy);
        From = findViewById(R.id.UnitFrom);
        To = findViewById(R.id.UnitTo);
        Calculate = findViewById(R.id.Do);
        Save = findViewById(R.id.Save);
        input = findViewById(R.id.Value);
        output = findViewById(R.id.Result);
        Back = findViewById(R.id.Back);
        From.setOnItemSelectedListener(this);
        To.setOnItemSelectedListener(this);
        Calculate.setOnClickListener(this);
        Save.setOnClickListener(this);
        Back.setOnClickListener(this);
        from_unit = to_unit = 0;
        result = "0.0";
        if(savedInstanceState!=null){
            result = savedInstanceState.getString("result");
            DisPlay();
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.sub_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.About:
                startActivity(new Intent(getApplicationContext(), AboutActivity.class));
                return true;
            case R.id.SaveDate:
                Intent intent = new Intent(getApplicationContext(), ShowActivity.class);
                intent.putExtra("EX", "E");
                startActivity(intent);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }


    @Override
    public void onSaveInstanceState(Bundle outState){
        super.onSaveInstanceState(outState);
        outState.putString("result", result);
    }

    public void DisPlay(){
        output.setText(result);
    }

    @Override
    protected void onResume() {
        super.onResume();
        Intent intent = getIntent();
        digit = intent.getIntExtra("di",2);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.Do:
                Calculate();
                break;
            case R.id.Back:
                startActivity(new Intent(getApplication(), MainActivity.class));
                break;
            case R.id.Save:
                Calculate();
                SaveData();
                break;
        }
    }

    public void Calculate(){
        try {
            double value = Double.parseDouble(input.getText().toString());
            if (from_unit != to_unit){
                value = (value/Value[from_unit])*Value[to_unit];
                if(Digit(value)==0){
                    Toast.makeText(this,"Try keep more digit.",Toast.LENGTH_LONG).show();
                }
            }
            result = Double.toString(Digit(value));
            DisPlay();
        } catch (Exception e){
            Toast.makeText(this,"Please type number only.",Toast.LENGTH_LONG).show();
        }
    }

    public double Digit(double input){
        if(digit == -1){
            return input;
        } else {
            double n = Math.pow(10,digit);
            return Math.round(input * n) / n;
        }
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        Spinner spinner = (Spinner) adapterView;
        if(spinner.getId() == R.id.UnitFrom)
        {
            from_unit = i;
        }
        else if(spinner.getId() == R.id.UnitTo)
        {
            to_unit = i;
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }

    public void SaveData(){

        String function = input.getText().toString() + " " + Unit[from_unit] + " = " + result + " " + Unit[to_unit];

        if(GetData(function).getCount()==0) {

            UnitTransferSQLiteHelper helper = new UnitTransferSQLiteHelper(this);
            SQLiteDatabase db = helper.getWritableDatabase();

            ContentValues cv = new ContentValues();

            cv.put(UnitTransferSQLiteHelper.TYPE, "Energy");
            cv.put(UnitTransferSQLiteHelper.FUNCTION, function);
            db.insert(UnitTransferSQLiteHelper.TITLE, null, cv);

            db.close();
        }else{
            Toast.makeText(this,"This data already saved.",Toast.LENGTH_LONG).show();
        }
    }

    public Cursor GetData(String key) {
        UnitTransferSQLiteHelper helper = new UnitTransferSQLiteHelper(this);
        SQLiteDatabase db = helper.getReadableDatabase();

        String query = "SELECT * FROM UTransfer WHERE FUNCTION = ?";
        String[] variables = new String[]{key};
        return db.rawQuery(query, variables);
    }
}
