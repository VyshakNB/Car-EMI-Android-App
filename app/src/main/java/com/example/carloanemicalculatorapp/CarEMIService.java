package com.example.carloanemicalculatorapp;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.os.RemoteException;

public class CarEMIService extends Service {
    public CarEMIService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        return stub;
    }

    ICarServiceInterface.Stub stub = new ICarServiceInterface.Stub() {
        @Override
        public float carcal(float pa, float dp, float ir, int lt) throws RemoteException {
            float emiamount;
            float principalamt = pa - dp;           //Subtract downpayment from principal amount
            float Interest = ((ir/12)/100);            //Convert interest for permonth and divide it by 100 to get a number

            emiamount = (float) ((float) principalamt * (Interest  * (Math.pow((1+Interest),lt))) / ((Math.pow((1+Interest),lt)) - 1));

            return emiamount;
        }
    };
}