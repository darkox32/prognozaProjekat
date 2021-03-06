package android.singidunum.ac.prognozaprojekat;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.concurrent.ExecutionException;

public class MainActivity extends AppCompatActivity {

    TextView  temp, location, desc;
    EditText editLocation;
    Button buttonSearch;

    class Weather extends AsyncTask<String, Void, String>{

        @Override
        protected String doInBackground(String... adress) {
            //proveri dal valjda URL, dohvati data
            try {
                URL url = new URL(adress[0]);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.connect();

                InputStream is = connection.getInputStream();
                InputStreamReader isr = new InputStreamReader(is);

                int data = isr.read();
                String content = "";

                while(data != -1){
                  char ch = (char) data;
                    content = content + ch;
                    data = isr.read();
                }
                return content;

            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }
    }

    public void search(View view){
        editLocation = findViewById(R.id.edit_location);
        buttonSearch = findViewById(R.id.buttonSearch);
        temp = findViewById(R.id.temperature);
        location = findViewById(R.id.location);
        desc = findViewById(R.id.weather_condition);

        //lokacija koja se pretrazuje
        String lName = editLocation.getText().toString();

        String content;
        Weather weather = new Weather();
        try {
            content = weather.execute("https://openweathermap.org/data/2.5/weather?q="+ lName + "&appid=439d4b804bc8187953eb36d2a8c26a02").get();
            Log.i("content",content);

            //Uzmi delove koje trebaju iz API-a
            JSONObject jsonObject = new JSONObject(content);
            String weatherData = jsonObject.getString("weather");
            String mainTemperature = jsonObject.getString("main"); //ovako se zove u api-u main, nije ovaj ranije koriscen
            String cName = jsonObject.getString("name"); //country name = c
            /*Log.i("weatherData", weatherData);*/

            //uzmi opis vremena
            String main = "";
            JSONArray array = new JSONArray(weatherData);
            for(int i=0; i<array.length(); i++){
                JSONObject weatherPart = array.getJSONObject(i);
                main = weatherPart.getString("main");
            }

            //uzmi temp iz main objekta, makni decimale
            JSONObject mainTemp = new JSONObject(mainTemperature);
            String temperature = mainTemp.getString("temp");
            if (temperature.contains(".")) {
                temperature = temperature.substring(0,temperature.indexOf("."));
            }
            /*Log.i("Temperature",temperature);*/

            location.setText(cName);
            temp.setText(temperature);
            desc.setText(main);

        } catch (ExecutionException | InterruptedException | JSONException e) {
            e.printStackTrace();
        }
    }

    public void logout(View view){
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }
}