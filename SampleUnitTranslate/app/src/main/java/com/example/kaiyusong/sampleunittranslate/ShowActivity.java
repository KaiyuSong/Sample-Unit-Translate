package com.example.kaiyusong.sampleunittranslate;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v4.widget.SimpleCursorAdapter;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Spinner;

public class ShowActivity extends AppCompatActivity
        implements View.OnClickListener, AdapterView.OnItemSelectedListener, AdapterView.OnItemClickListener {

    Button BackHome, BackEx;
    Spinner TypeSelect;
    String key, EX;
    Cursor cursor = null;
    ListView itemsListView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show);
        TypeSelect = findViewById(R.id.TypeSelect);
        BackHome = findViewById(R.id.BH);
        BackEx = findViewById(R.id.BE);
        itemsListView = findViewById(R.id.DataList);
        BackHome.setOnClickListener(this);
        BackEx.setOnClickListener(this);
        TypeSelect.setOnItemSelectedListener(this);
        itemsListView.setOnItemClickListener(this);
        key= "Length";
        EX = "M";
    }

    @Override
    protected void onResume() {
        super.onResume();
        Intent intent = getIntent();
        EX = intent.getStringExtra("EX");
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.BH:
                startActivity(new Intent(getApplicationContext(), MainActivity.class));
                break;
            case R.id.BE:
                switch (EX){
                    case "M":
                        startActivity(new Intent(getApplicationContext(), MainActivity.class));
                        break;
                    case "E":
                        startActivity(new Intent(getApplicationContext(), EnergyActivity.class));
                        break;
                    case "L":
                        startActivity(new Intent(getApplicationContext(), LengthActivity.class));
                        break;
                    case "A":
                        startActivity(new Intent(getApplicationContext(), AreaActivity.class));
                        break;
                    case "V":
                        startActivity(new Intent(getApplicationContext(), VolumActivity.class));
                        break;
                    case "S":
                        startActivity(new Intent(getApplicationContext(), SpeedActivity.class));
                        break;
                    case "T":
                        startActivity(new Intent(getApplicationContext(), TemperatureActivity.class));
                        break;
                }
        }
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        switch (position){
            case 0:
                key = "Length";
                break;
            case 1:
                key = "Area";
                break;
            case 2:
                key = "Volume";
                break;
            case 3:
                key = "Speed";
                break;
            case 4:
                key = "Temperature";
                break;
            case 5:
                key = "Energy";
                break;

        }
        cursor = GetData();
        Show(cursor);
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {
        // TODO Auto-generated method stub
    }

    public void Show(Cursor cursor){
        String[] from = new String[]{UnitTrandsferSQLiteHelper.FUNCTION};
        int[] to = new int[]{R.id.Eq};

        SimpleCursorAdapter adapter = new SimpleCursorAdapter(this, R.layout.list_items, cursor, from, to, 0 );

        itemsListView.setAdapter(adapter);
    }

    public Cursor GetData() {
        UnitTrandsferSQLiteHelper helper = new UnitTrandsferSQLiteHelper(this);
        SQLiteDatabase db = helper.getReadableDatabase();

        String query = "SELECT * FROM UTransfer WHERE Type = ?";
        String[] variables = new String[]{key};
        return db.rawQuery(query, variables);
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        cursor.moveToPosition(i);
        String function = cursor.getString(cursor.getColumnIndex(UnitTrandsferSQLiteHelper.FUNCTION));

        UnitTrandsferSQLiteHelper helper = new UnitTrandsferSQLiteHelper(this);
        SQLiteDatabase db = helper.getWritableDatabase();

        String query = UnitTrandsferSQLiteHelper.FUNCTION + " = ?";
        String[] variables = new String[]{function};
        db.delete(UnitTrandsferSQLiteHelper.TITLE,query,variables);
        db.close();
        cursor = GetData();
        Show(cursor);
    }
}
