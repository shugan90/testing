package com.example.shugan.my1;

import android.content.Context;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class WeatherDataService {

    public static final String Queary_For_CityID = "https://www.metaweather.com/api/location/search/?query=";
    public static final String Queary_For_Weather =  "https://www.metaweather.com/api/location/";
    Context context;
    String CityID;

    public WeatherDataService(Context context) {
        this.context = context;
    }


    public interface VolleyResponseListener {
    void onResponse(String CityID);

    void onError(String message);
    }

    public String getCityId(String cityName,VolleyResponseListener volleyResponseListener) {
        String url = Queary_For_CityID + cityName;
        // JSON ARRAY REQUEST
        JsonArrayRequest request = new JsonArrayRequest(Request.Method.GET, url, null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                CityID = "";
                try {
                    JSONObject CityInfo = response.getJSONObject(0);
                    CityID = CityInfo.getString("woeid");
                    Toast.makeText(context, "CityId:" + CityID, Toast.LENGTH_SHORT).show();
                    volleyResponseListener.onResponse(CityID);
                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(context, "Something Wrong", Toast.LENGTH_SHORT).show();
                    volleyResponseListener.onError("something Wrong");
                }
            }
        }, new Response.ErrorListener() {
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(context, "Something Wrong", Toast.LENGTH_SHORT).show();
            }


        });
        MySingleton.getInstance(context).addToRequestQueue(request);
        return CityID;
    }
    public interface ForecastbyIDResponse {
        void onError(String message);
        void onResponse(List<WeatherReportModel> weatherReportModels);

    }
   public void getForecastsID(String CityID, ForecastbyIDResponse forecastbyIDResponse){

        List<WeatherReportModel> weatherReportModels =new ArrayList<>();
        String url=Queary_For_Weather + CityID;
     //String url="https://www.metaweather.com/api/location/" + CityID;

        // get json object
       JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
         //Toast.makeText(context,response.toString(),Toast.LENGTH_LONG).show();
                //get property called Consolidated weather which is array

                try {
                    JSONArray consolidated_weather_list=response.getJSONArray("consolidated_weather");
                    WeatherReportModel first_day = new WeatherReportModel();

                    for(int i=0; i<consolidated_weather_list.length(); i++) {
                        //get each item in array and aassign into weatherreportmodel object
                        JSONObject first_day_report = (JSONObject) consolidated_weather_list.get(i);
                        first_day.setId(first_day_report.getInt("id"));

                        first_day.setWeather_state_name(first_day_report.getString("weather_state_name"));
                        first_day.setWeather_state_abbr(first_day_report.getString("weather_state_abbr"));
                        first_day.setWind_direction_compass(first_day_report.getString("wind_direction_compass"));
                        first_day.setCreated(first_day_report.getString("created"));
                        first_day.setApplicable_date(first_day_report.getString("applicable_date"));
                        first_day.setMin_temp(first_day_report.getLong("min_temp"));
                        first_day.setMax_temp(first_day_report.getLong("max_temp"));
                        first_day.setThe_temp(first_day_report.getLong("the_temp"));
                        first_day.setWind_speed(first_day_report.getLong("wind_speed"));
                        first_day.setWind_direction(first_day_report.getLong("wind_direction"));
                        first_day.setAir_pressure(first_day_report.getInt("air_pressure"));
                        first_day.setHumidity(first_day_report.getInt("humidity"));
                        first_day.setVisibility(first_day_report.getLong("visibility"));
                        first_day.setPredictability(first_day_report.getInt("predictability"));

                    weatherReportModels.add(first_day);
                    }
                 forecastbyIDResponse.onResponse(weatherReportModels);

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(context, "Something Wrong", Toast.LENGTH_SHORT).show();

            }

        });
       MySingleton.getInstance(context).addToRequestQueue(request);

            //get each item in array and aassing into weatherreportmodel object

    }
    public interface Forecastbycallback{
        void onError(String message);
        void onResponse(List<WeatherReportModel> weatherReportModels);

    }
    public void getCityIDForecastsbyname(String cityName,Forecastbycallback forecastbycallback){
        getCityId(cityName, new VolleyResponseListener() {
            @Override
            public void onResponse(String CityID) {
            getForecastsID(CityID, new ForecastbyIDResponse() {
                @Override
                public void onError(String message) {

                }

                @Override
                public void onResponse(List<WeatherReportModel> weatherReportModels) {
                    forecastbycallback.onResponse(weatherReportModels);
                }
            });
            }

            @Override
            public void onError(String message) {

            }
        });
    }
}
