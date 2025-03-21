package com.example.cryptinfos;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class HomeActivity extends AppCompatActivity {
    MyRecyclerViewAdapter myAdapter;
    TextView tv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        List<String> testAdapter = new ArrayList<>();
        for (int i = 0; i < 200; i++) {
            testAdapter.add("element " + i);
        }
        RecyclerView recycler = findViewById(R.id.adapter);
        LinearLayoutManager lmn = new LinearLayoutManager(this);
        lmn.setOrientation(RecyclerView.HORIZONTAL);
        recycler.setLayoutManager(lmn);
        myAdapter = new MyRecyclerViewAdapter(testAdapter);
        recycler.setAdapter(myAdapter);

        tv = findViewById(R.id.tv);
    }

    public void go(View view) {
        ExecutorService executor = Executors.newSingleThreadExecutor();
        Handler handler = new Handler(Looper.getMainLooper());
        executor.execute(new Runnable() {
            @Override
            public void run() {
                String data = getDataFromHTTP("https://api.coinstats.app/public/v1/coins?skip=0&limit=10");
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            tv.setText(decodeJSON(data));
                        } catch (JSONException e) {
                            tv.setText("Ville non trouvée");
                        }
                    }
                });
            }
        });
    }

    public String getDataFromHTTP(String param){

        StringBuilder result = new StringBuilder();
        HttpURLConnection connexion = null;
        try {
            URL url = new URL(param);
            connexion = (HttpURLConnection) url.openConnection();
            connexion.setRequestMethod("GET");
            InputStream inputStream = connexion.getInputStream();
            InputStreamReader inputStreamReader = new
                    InputStreamReader(inputStream);
            BufferedReader bf = new BufferedReader(inputStreamReader);
            String ligne = "";
            while ((ligne = bf.readLine()) != null) {
                result.append(ligne);
            }
            inputStream.close();
            bf.close();
            connexion.disconnect();
        } catch (Exception e) {
            result = new StringBuilder("Erreur ");
        }
        return result.toString();
    }

    public String decodeJSON(String json) throws JSONException {
        JSONObject jso = new JSONObject(json);
        JSONArray coins = jso.getJSONArray("coins");
        for (int i=0; ) {

        }
        return nom_crypto;
    }
}