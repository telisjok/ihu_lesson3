package gr.ihu.lab.ihuweather_01;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by labuser on 7/4/2017.
 */

public class ForecastFragment extends Fragment {

    private ArrayAdapter<String> adapter;

    String[] data = {
                        "Mon 6/23 - Sunny - 31/17",
                        "Tue 6/24 - Foggy - 21/8",
                        "Wed 6/25 - Cloudy - 22/17",
                        "Thurs 6/26 - Rainy - 18/11",
                        "Fri 6/27 - Foggy - 21/10",
                        "Sat 6/28 - Error! - 23/18",
                        "Sun 6/29 - Sunny - 20/7"
                    };


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.forecast_fragment, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if(id == R.id.action_refresh) {
            AsyncTask<String, Void, String[]> weatherTask = new FetchWeatherTask();
            weatherTask.execute();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public class FetchWeatherTask extends AsyncTask<String, Void, String[]> {

        @Override
        protected String[] doInBackground(String... voids) {

            HttpURLConnection urlConnection = null;
            BufferedReader reader = null;
            String forecastString = null;
            int numDays = 7;

            try{
                URL url
                        = new URL("http://api.openweathermap.org/data/2.5/forecast/daily?q=thessaloniki,gr&units=metric&APPID=8cbf55d68127d9483386b81e1ab1cd8d&cnt=" + numDays);

                urlConnection = (HttpURLConnection)url.openConnection();
                urlConnection.setRequestMethod("GET");
                urlConnection.connect();

                InputStream inputStream = urlConnection.getInputStream();
                StringBuffer buffer = new StringBuffer();

                if(inputStream == null){
                    return null;
                }

                reader = new BufferedReader(new InputStreamReader(inputStream));

                String line;
                while((line = reader.readLine()) != null){
                    buffer.append(line + "\n");
                }

                if(buffer.length() == 0){
                    return null;
                }

                forecastString = buffer.toString();

                Log.i("ForecastFragment", forecastString);

                return WeatherParser.parseWeatherFromJSON(numDays, forecastString);

            }catch (Exception e){
                Log.e("ForecastFragment", "Error", e);
            }
            finally{
                urlConnection.disconnect();
                try {
                    reader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

           return null;
        }

        @Override
        protected void onPostExecute(String[] weatherResults) {
            if(weatherResults!=null){
                adapter.clear();
                for(String s : weatherResults){ //for(int i=0; i<weatherResults.length; i++){
                    adapter.add(s);             //  String s = weatherResults[i];
                }                               //  adapter.add(s); }
            }
        }
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_main, container, false);

        ArrayList<String> dummyData = new ArrayList<>(Arrays.asList(data));

        adapter = new ArrayAdapter<String>( getActivity(), R.layout.list_item_forecast, R.id.list_item_forecast_textview, dummyData );

        ListView listView = (ListView) rootView.findViewById(R.id.listview_forecast);
        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener(){

            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                final String Item = adapter.getItem(position);

                Toast toast = Toast.makeText(getActivity(), Item, Toast.LENGTH_LONG);
                toast.show();
            }
        });

        return rootView;
    }


}