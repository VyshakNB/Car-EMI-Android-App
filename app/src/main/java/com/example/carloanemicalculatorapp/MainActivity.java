package com.example.carloanemicalculatorapp;

import androidx.appcompat.app.AppCompatActivity;


import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.os.RemoteException;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;


public class MainActivity extends AppCompatActivity  {

    ICarServiceInterface cService;
    TextView txtresult,pa, dp, ir,ltm;
    Button calculateemi,clearbutton;
    EditText pri,dpt;
    Spinner intrate,loan;
    String[] users = {"","0","1","2","3","4","5","6","7","8","9","10","11","12","13","14","15","16","17","18","19","20",
            "21","22","23","24","25","26","27","28","29","30","31","32","33","34","35","36","37","38","39","40",
            "41","42","43","44","45","46","47","48","49","50"};


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        pri = findViewById(R.id.pri);
        dpt = findViewById(R.id.dpt);
        intrate = findViewById(R.id.spinner1);
        loan = findViewById(R.id.spinner2);
        pa = findViewById(R.id.pa);
        dp = findViewById(R.id.dp);
        ir = findViewById(R.id.ir);
        ltm = findViewById(R.id.ltm);
        //spinnera=findViewById(R.id.textView2);
        //spinnerb=findViewById(R.id.textView4);

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item,users);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        intrate.setAdapter(adapter);
        intrate.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                //spinnera.setText(intrate.getSelectedItem().toString());
                String text = parent.getItemAtPosition(position).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, users);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        loan.setAdapter(adapter);
        loan.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                //spinnerb.setText(loan.getSelectedItem().toString());
                String text = parent.getItemAtPosition(position).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        calculateemi = findViewById(R.id.emibtn);
        clearbutton = findViewById(R.id.clrbtn);
        clearbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v){
                pri.setText("");
                dpt.setText("");
                intrate.setSelection(0);
                loan.setSelection(0);
                txtresult.setText("");
            }
        });

        txtresult = findViewById(R.id.result);
        calculateemi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Float principalamount = Float.valueOf(pri.getText().toString());
                Float downpayment = Float.valueOf(dpt.getText().toString());
                Float interestrate = Float.valueOf(intrate.getSelectedItem().toString());
                int lnterm = Integer.parseInt(loan.getSelectedItem().toString());

                try {
                    Float result = cService.carcal(principalamount,downpayment,interestrate,lnterm);
                    String roundoff=String.format("%.2f",result);
                    txtresult.setText("Monthly EMI in Rs is :" +roundoff);
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
            }
        });

        Intent it = new Intent(this,CarEMIService.class);
        bindService(it,sconnection,BIND_AUTO_CREATE);
    }

    ServiceConnection sconnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            Toast.makeText(getBaseContext(),"Service Connected",Toast.LENGTH_LONG).show();
            cService = ICarServiceInterface.Stub.asInterface(service);
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {

        }
    };

}