package gr.ihu.lab.ihuweather_01;

import android.icu.text.SimpleDateFormat;
import android.text.format.Time;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


public class WeatherParser {

    private static final String LOG_TAG = WeatherParser.class.getName();

    public static String[] parseWeatherFromJSON(int numDays, String jsonWeather) throws JSONException {
        String[] results = new String[numDays];

        String OWM_List = "list", OWM_Weather = "weather", OWM_Temp = "temp", OWM_Max = "max", OWM_Min = "min", OWM_Desc = "main", OWM_Hum = "humidity";

        JSONObject forecastObj = new JSONObject(jsonWeather);
        JSONArray weatherArray = forecastObj.getJSONArray(OWM_List);

        Time dayTime = new Time(); //android.text.format.Time
        dayTime.setToNow();
        int julianStartDay = Time.getJulianDay(System.currentTimeMillis(), dayTime.gmtoff); //julianStartDay is the current day (today)
        dayTime = new Time();

        for(int i=0;i<numDays; i++){
            //we are going to use the format: Day, Description, min/max
            String day, description, minmax, humidity;

            JSONObject dayForecast = weatherArray.getJSONObject(i);

            long dateTime;
            dateTime = dayTime.setJulianDay(julianStartDay + i); //julianStartDay is the current day (today)
            SimpleDateFormat sdf = new SimpleDateFormat("EEE, dd, MMM");
            day = sdf.format(dateTime);

            JSONObject o = dayForecast.getJSONArray(OWM_Weather).getJSONObject(0);
            description = o.getString(OWM_Desc);

            JSONObject tempObj = dayForecast.getJSONObject(OWM_Temp);
            double max = tempObj.getDouble(OWM_Max);
            double min = tempObj.getDouble(OWM_Min);

            minmax = Math.round(min)+ " - " +Math.round(max);

            int hum = dayForecast.getInt("humidity");

            results[i] = day + " | " + description + " | " + minmax + " | Humidity: " + hum + "%";
        }

        for(int i=0; i<numDays; i++){
            Log.i(LOG_TAG, "Forecast Entry: " + results[i]);
        }

        return results;
    }

}
