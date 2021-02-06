package com.example.shugan.my1;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    Button btn1,btn2,btn3;
    String CityID;

    ListView t;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
       t = findViewById(R.id.tt);
        TextView textView = (TextView) findViewById(R.id.text);
         btn1=findViewById(R.id.button);
         btn2=findViewById(R.id.button1);
         btn3=findViewById(R.id.button2);
      WeatherDataService weatherDataService =new WeatherDataService(MainActivity.this);

      // RequestQueue queue = Volley.newRequestQueue(this);

       // String url ="https://www.metaweather.com/api/location/search/?query="+textView.getText().toString();
         btn1.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View v) {


                weatherDataService.getCityId(textView.getText().toString(), new WeatherDataService.VolleyResponseListener() {
                    @Override
                    public void onResponse(String CityID) {
                        Toast.makeText(MainActivity.this,"Return an ID of:" + CityID,Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onError(String message) {
                        Toast.makeText(MainActivity.this,"Something is Wrong" ,Toast.LENGTH_SHORT).show();
                    }
                });


// Add the request to the RequestQueue.
                 //queue.add(stringRequest);
                // MySingleton.getInstance(MainActivity.this).addToRequestQueue(request);
             }
         });
        btn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //Toast.makeText(MainActivity.this,"test" ,Toast.LENGTH_LONG).show();

                weatherDataService.getForecastsID("44418", new WeatherDataService.ForecastbyIDResponse() {


                    @Override
                    public void onResponse(List<WeatherReportModel> weatherReportModels) {
                   // Toast.makeText(MainActivity.this, weatherReportModels.toString(),Toast.LENGTH_SHORT).show();
                        ArrayAdapter arrayAdapter =new ArrayAdapter(MainActivity.this, android.R.layout.simple_list_item_1,weatherReportModels);
                        t.setAdapter(arrayAdapter);
                    }
                    @Override
                    public void onError(String message) {
                        Toast.makeText(MainActivity.this,"NO" ,Toast.LENGTH_LONG).show();
                    }
                }) ;
               // MySingleton.getInstance(context).addToRequestQueue(request);

// Add the request to the RequestQueue.
                //queue.add(stringRequest);
                // MySingleton.getInstance(MainActivity.this).addToRequestQueue(request);
            }
        });

        btn3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //Toast.makeText(MainActivity.this,"test" ,Toast.LENGTH_LONG).show();

                weatherDataService.getCityIDForecastsbyname("London", new WeatherDataService.Forecastbycallback() {


                    @Override
                    public void onResponse(List<WeatherReportModel> weatherReportModels) {
                        // Toast.makeText(MainActivity.this, weatherReportModels.toString(),Toast.LENGTH_SHORT).show();
                        ArrayAdapter arrayAdapter =new ArrayAdapter(MainActivity.this, android.R.layout.simple_list_item_1,weatherReportModels);
                        t.setAdapter(arrayAdapter);
                    }
                    @Override
                    public void onError(String message) {
                        Toast.makeText(MainActivity.this,"NO" ,Toast.LENGTH_LONG).show();
                    }
                }); ;
                // MySingleton.getInstance(context).addToRequestQueue(request);

// Add the request to the RequestQueue.
                //queue.add(stringRequest);
                // MySingleton.getInstance(MainActivity.this).addToRequestQueue(request);
            }
        });



    }

}