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
    RecyclerView recycler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        List<Coin> coins = new ArrayList<>();
        recycler = findViewById(R.id.adapter);
        LinearLayoutManager lmn = new LinearLayoutManager(this);
        lmn.setOrientation(RecyclerView.VERTICAL);
        recycler.setLayoutManager(lmn);
        myAdapter = new MyRecyclerViewAdapter(coins);
        recycler.setAdapter(myAdapter);

        tv = findViewById(R.id.tv);
    }

    public void go(View view) {
        ExecutorService executor = Executors.newSingleThreadExecutor();
        Handler handler = new Handler(Looper.getMainLooper());
        executor.execute(new Runnable() {
            @Override
            public void run() {
                String data = getDataFromHTTP("https://openapiv1.coinstats.app/coins", "srkjExa1IBZMMoDmd5YTI4sWbLpce8KFavfrzjbKKvU=");
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            List<Coin> coinList = decodeJSON(data);
                            myAdapter = new MyRecyclerViewAdapter(coinList);
                            recycler.setAdapter(myAdapter);
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
        List<Coin> coinList = new ArrayList<>();

        JSONObject jsonObject = new JSONObject(json);
        JSONArray coinsArray = jsonObject.getJSONArray("result");

        for (int i = 0; i < coinsArray.length(); i++) {
            JSONObject coinObject = coinsArray.getJSONObject(i);

            String icon = coinObject.getString("icon");
            String name = coinObject.getString("name");
            String symbol = coinObject.getString("symbol");
            int rank = coinObject.getInt("rank");
            double price = coinObject.getDouble("price");

            // Créer un objet Coin et l'ajouter à la liste
            Coin coin = new Coin(name, symbol, icon, rank, price);
            coinList.add(coin);
        }

        return coinList;
    }

}