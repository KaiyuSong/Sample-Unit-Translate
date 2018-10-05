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

public class TemperatureActivity extends AppCompatActivity implements View.OnClickListener, AdapterView.OnItemSelectedListener {

    Spinner From, To;
    Button Calculate, Save, Back;
    EditText input;
    TextView output;
    int digit = 2;
    String result;
    String from_unit, to_unit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_temperature);
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
        from_unit = to_unit = "mi";
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
                intent.putExtra("EX", "T");
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
            value = TemretureMath(value);
            result = Double.toString(Digit(value));
            DisPlay();
        } catch (Exception e){
            Toast.makeText(this,"Please type number only.",Toast.LENGTH_LONG).show();
        }
    }

    public double TemretureMath(double value){
        double key = 0.5555555555555555556;
        switch (from_unit){
            case "K":
                break;
            case "°C":
                value = value + 273.15;
                break;
            case "°F":
                value = (value+459.67)*key;
                break;
            case "°R":
                value = value*key ;
                break;
        }
        switch (to_unit){
            case "K":
                break;
            case "°C":
                value = value - 273.15;
                break;
            case "°F":
                value = (value/key)-459.67;
                break;
            case "°R":
                value = value/key;
                break;
        }
        return value;
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
            switch (i){
                case 0:
                    from_unit = "K";
                    break;
                case 1:
                    from_unit = "°C";
                    break;
                case 2:
                    from_unit = "°F";
                    break;
                case 3:
                    from_unit = "°R";
                    break;
            }
        }
        else if(spinner.getId() == R.id.UnitTo)
        {
            switch (i){
                case 0:
                    to_unit = "K";
                    break;
                case 1:
                    to_unit = "°C";
                    break;
                case 2:
                    to_unit = "°F";
                    break;
                case 3:
                    to_unit = "°R";
                    break;
            }
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }

    public void SaveData(){

        String function = input.getText().toString() + " " + from_unit + " = " + result + " " + to_unit;

        if(GetData(function).getCount()==0) {

            UnitTransferSQLiteHelper helper = new UnitTransferSQLiteHelper(this);
            SQLiteDatabase db = helper.getWritableDatabase();

            ContentValues cv = new ContentValues();

            cv.put(UnitTransferSQLiteHelper.TYPE, "Temperature");
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
