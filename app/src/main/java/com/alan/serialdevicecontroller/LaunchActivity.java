package com.alan.serialdevicecontroller;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import com.alan.serialdevicecontroller.extra.Constants;

public class LaunchActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    private final String[] baudRateList = {"110", "300", "600", "1200", "2400", "4800", "9600", "14400", "19200", "38400", "57600", "115200", "128000", "256000"};

    private int baudRate = Constants.DEFAULT_BAUD_RATE;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_launch);

        Spinner spinner = findViewById(R.id.baudRateView);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, R.layout.spinner_item, baudRateList);
        adapter.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);

        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(this);
        spinner.setSelection(6); // 9600 at pos 6
    }

    public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
        // An item was selected. You can retrieve the selected item using
        // parent.getItemAtPosition(pos)
        baudRate = Integer.parseInt(parent.getSelectedItem().toString());
    }

    public void onNothingSelected(AdapterView<?> parent) {
        // Another interface callback
    }

    public void onUsbClick(View view) {
        this.startRelatedActivity(Constants.DEVICE_USB);
    }

    public void onBluetoothClick(View view) {
        this.startRelatedActivity(Constants.DEVICE_BLUETOOTH);
    }

    private void startRelatedActivity(String name) {
        Intent intent = new Intent(this, MainActivity.class);
        intent.putExtra(Constants.EXTRA_DEVICE_TYPE, name);
        intent.putExtra(Constants.EXTRA_BAUD_RATE, baudRate);
        startActivity(intent);
    }

}
