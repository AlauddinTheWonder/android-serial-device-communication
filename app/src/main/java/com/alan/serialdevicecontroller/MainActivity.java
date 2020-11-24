package com.alan.serialdevicecontroller;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.alan.serialdevicecontroller.classes.Device;
import com.alan.serialdevicecontroller.classes.DeviceListener;
import com.alan.serialdevicecontroller.classes.MyDevice;
import com.alan.serialdevicecontroller.extra.Constants;
import com.alan.serialdevicecontroller.extra.Debounce;
import com.alan.serialdevicecontroller.extra.ProgressDialog;


public class MainActivity extends AppCompatActivity implements DeviceListener {

    private Device myDevice;

    private Debounce scrollDebounce;
    private ProgressDialog progressDialog;

    // Views
    private LinearLayout terminalView;
    private ScrollView scrollView;
    private EditText terminalInput;
    private TextView logView, deviceNameView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        String deviceType = getIntent().getStringExtra(Constants.EXTRA_DEVICE_TYPE);
        if (deviceType != null && MyDevice.validateDevice(deviceType)) {
            int baudRate = getIntent().getIntExtra(Constants.EXTRA_BAUD_RATE, Constants.DEFAULT_BAUD_RATE);

            this.myDevice = MyDevice.getDevice(this, deviceType, baudRate);
        } else {
            Toast.makeText(this, "Invalid device", Toast.LENGTH_SHORT).show();
            finish();
        }

        scrollDebounce = new Debounce(1000);
        progressDialog = new ProgressDialog(this);

        scrollView =  findViewById(R.id.log_scroll_view);
        logView = findViewById(R.id.log_view);
        terminalView = findViewById(R.id.terminal_view);
        terminalInput = findViewById(R.id.terminal_input);
        deviceNameView = findViewById(R.id.device_name_view);

        myDevice.setListener(this);
        myDevice.connect();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        progressDialog.dismiss();
        myDevice.onExit();
    }

    @Override
    public void onInfo(String text) {
        log("Info: " + text);
    }

    @Override
    public void onDeviceConnect(String name) {
        String str = getString(R.string.connected_str, name);
        onInfo(str);
        Toast.makeText(this, str, Toast.LENGTH_SHORT).show();
        deviceNameView.setText(str);
        deviceNameView.setTextColor(getResources().getColor(R.color.connectedColor));

        terminalView.setVisibility(View.VISIBLE);
    }

    @Override
    public void onDeviceDisconnect() {
        deviceNameView.setText(getString(R.string.not_connected_str));
        deviceNameView.setTextColor(getResources().getColor(R.color.grey));
        onInfo("Device disconnected");
        Toast.makeText(this, "Device disconnected", Toast.LENGTH_SHORT).show();

        terminalView.setVisibility(View.GONE);
    }

    @Override
    public void onExitRequest() {
        finish();
    }

    @Override
    public void onProgressStart() {
        progressDialog.show();
    }

    @Override
    public void onProgressStop() {
        progressDialog.hide();
    }

    @Override
    public void onReceivedFromDevice(String data) {
        log("< " + data);
    }

    public void onTerminalSendBtnClick(View view) {
        String text = terminalInput.getText().toString();
        log("> " + text);
        myDevice.send(text);
        terminalInput.setText("");
    }

    private void log(final String text) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                logView.append(text);
                logView.append("\n");

                scrollDebounce.attempt(new Runnable() {
                    @Override
                    public void run() {
                        scrollView.fullScroll(ScrollView.FOCUS_DOWN);
                    }
                });
            }
        });
    }

}
