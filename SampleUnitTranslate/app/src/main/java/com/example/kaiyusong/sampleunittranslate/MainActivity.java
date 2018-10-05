package com.example.kaiyusong.sampleunittranslate;

import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    ImageButton Length, Area, Volume, Speed, Temperature, Energy;

    SharedPreferences pres;
    int digit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Length = findViewById(R.id.Length);
        Area = findViewById(R.id.Area);
        Volume = findViewById(R.id.Volume);
        Speed = findViewById(R.id.Speed);
        Temperature = findViewById(R.id.Temperature);
        Energy = findViewById(R.id.Energy);

        Length.setOnClickListener(this);
        Area.setOnClickListener(this);
        Volume.setOnClickListener(this);
        Speed.setOnClickListener(this);
        Temperature.setOnClickListener(this);
        Energy.setOnClickListener(this);

        digit = 2;

        PreferenceManager.setDefaultValues(this, R.xml.prefrence, false);
        pres = PreferenceManager.getDefaultSharedPreferences(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        try{
            digit = Integer.parseInt(pres.getString("Digit", "2"));
        }catch (Exception e){
            digit = -1;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.About:
                startActivity(new Intent(this, AboutActivity.class));
                return true;
            case R.id.Setting:
                startActivity(new Intent(this, SettingActivity.class));
                return true;
            case R.id.SaveDate:
                Intent intent = new Intent(this, ShowActivity.class);
                intent.putExtra("EX", "M");
                startActivity(intent);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.Length:
                Intent intent1 = new Intent(this, LengthActivity.class);
                intent1.putExtra("di", digit);
                startActivity(intent1);
                break;
            case R.id.Area:
                Intent intent2 = new Intent(this, AreaActivity.class);
                intent2.putExtra("di", digit);
                startActivity(intent2);
                break;
            case R.id.Volume:
                Intent intent3 = new Intent(this, VolumeActivity.class);
                intent3.putExtra("di", digit);
                startActivity(intent3);
                break;
            case R.id.Speed:
                Intent intent4 = new Intent(this, SpeedActivity.class);
                intent4.putExtra("di", digit);
                startActivity(intent4);
                break;
            case R.id.Temperature:
                Intent intent5 = new Intent(this, TemperatureActivity.class);
                intent5.putExtra("di", digit);
                startActivity(intent5);
                break;
            case R.id.Energy:
                Intent intent6 = new Intent(this, EnergyActivity.class);
                intent6.putExtra("di", digit);
                startActivity(intent6);
                break;
        }
    }
}
