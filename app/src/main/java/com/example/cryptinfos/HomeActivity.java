package com.example.cryptinfos;

import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
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
    RecyclerView recycler;
    List<Coin> coinList = new ArrayList<>();
    NetworkReceiver networkReceiver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        recycler = findViewById(R.id.adapter);
        LinearLayoutManager lmn = new LinearLayoutManager(this);
        lmn.setOrientation(RecyclerView.VERTICAL);
        recycler.setLayoutManager(lmn);
        myAdapter = new MyRecyclerViewAdapter(this, coinList);
        recycler.setAdapter(myAdapter);

        tv = findViewById(R.id.tv);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        networkReceiver = new NetworkReceiver();

        go();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        /*
        switch (item.getItemId()) {
            case R.id.reset:
                go();
                return (true);
        }
         */
        go();
        return (true);
    }

    public void go() {
        ExecutorService executor = Executors.newSingleThreadExecutor();
        Handler handler = new Handler(Looper.getMainLooper());
        executor.execute(new Runnable() {
            @Override
            public void run() {
                String data = getDataFromHTTP("https://openapiv1.coinstats.app/coins?limit=200", "srkjExa1IBZMMoDmd5YTI4sWbLpce8KFavfrzjbKKvU=");
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            List<Coin> coins = decodeJSON(data);
                            coinList.clear();
                            coinList.addAll(coins);
                            myAdapter.notifyDataSetChanged();
                        } catch (Exception e) {
                            tv.setText("Erreur lors de la recup crypto");
                        }
                    }
                });
            }
        });
    }

    public String getDataFromHTTP(String param, String apiKey) {
        StringBuilder result = new StringBuilder();
        HttpURLConnection connexion = null;
        try {
            // Création de l'URL
            URL url = new URL(param);
            connexion = (HttpURLConnection) url.openConnection();

            // Définition de la méthode GET
            connexion.setRequestMethod("GET");

            // Ajout des en-têtes (API key et accept: application/json)
            connexion.setRequestProperty("X-API-KEY", apiKey);
            connexion.setRequestProperty("accept", "application/json");

            // Lecture de la réponse
            InputStream inputStream = connexion.getInputStream();
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
            BufferedReader bf = new BufferedReader(inputStreamReader);

            String ligne;
            while ((ligne = bf.readLine()) != null) {
                result.append(ligne);
            }

            // Fermeture des flux
            inputStream.close();
            bf.close();

            // Déconnexion de la connexion
            connexion.disconnect();
        } catch (Exception e) {
            // En cas d'erreur, renvoie un message d'erreur
            result = new StringBuilder("Erreur lors de la récupération des données");
            e.printStackTrace();
        }
        return result.toString();
    }

    public List<Coin> decodeJSON(String json) throws JSONException {
        List<Coin> coins = new ArrayList<>();

        JSONObject jsonObject = new JSONObject(json);
        JSONArray coinsArray = jsonObject.getJSONArray("result");

        for (int i = 0; i < coinsArray.length(); i++) {
            JSONObject coinObject = coinsArray.getJSONObject(i);

            String id = coinObject.getString("id");
            String name = coinObject.getString("name");
            String symbol = coinObject.getString("symbol");
            double price = coinObject.getDouble("price");
            String icon = coinObject.getString("icon");

            // Créer un objet Coin et l'ajouter à la liste
            Coin coin = new Coin(id, name, symbol, icon, price);
            coins.add(coin);
        }

        return coins;
    }

    @Override
    protected void onResume() {
        super.onResume();
        IntentFilter filter = new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION);
        registerReceiver(networkReceiver, filter);
    }

    @Override
    protected void onPause(){
        super.onPause();
        unregisterReceiver(networkReceiver);
    }
}