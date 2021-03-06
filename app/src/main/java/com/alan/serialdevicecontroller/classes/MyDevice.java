package com.alan.serialdevicecontroller.classes;

import android.app.Activity;

import com.alan.serialdevicecontroller.extra.Constants;

public class MyDevice {

    public static Device getDevice(Activity context, String deviceType, int baudRate) {
        if (validateDevice(deviceType)) {
            if (deviceType.equals(Constants.DEVICE_USB)) {
                return new MyUsbDevice(context, baudRate);
            }
            else if (deviceType.equals(Constants.DEVICE_BLUETOOTH)) {
                return new MyBluetoothDevice(context, baudRate);
            }
        }
        return null;
    }

    public static Boolean validateDevice(String deviceType) {
        return deviceType.equals(Constants.DEVICE_USB) || deviceType.equals(Constants.DEVICE_BLUETOOTH);
    }

}
