package gr.ihu.lab.ihuweather_01;

import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class DetailedForecast extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detailed_forecast);

        if(savedInstanceState == null){
            getSupportFragmentManager().beginTransaction().add(R.id.activity_detailed_forecast, new PlaceholderFragment()).commit();
        }
    }

    public static class PlaceholderFragment extends Fragment {

        @Nullable
        @Override
        public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.activity_detailed_forecast, container, false);

            Intent getIntent = getActivity().getIntent();

            if(getIntent != null && getIntent.hasExtra(Intent.EXTRA_TEXT)){
                String theData = getIntent.getStringExtra(Intent.EXTRA_TEXT);

                TextView forecastDetails = (TextView) rootView.findViewById(R.id.detail_text);

                forecastDetails.setText(theData);
            }

            return rootView;
        }
    }
}
