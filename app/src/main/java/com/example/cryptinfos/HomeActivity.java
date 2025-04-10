package com.example.cryptinfos;

import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

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

/**
 * Activité lancée au démarrage de l'application
 */
public class HomeActivity extends AppCompatActivity {
    RecyclerViewAdapterHomeActivity myAdapter;
    RecyclerView recycler;
    List<CoinObject> coinList = new ArrayList<>();
    NetworkReceiver networkReceiver;
    Button btn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home_layout);
        recycler = findViewById(R.id.adapter);
        LinearLayoutManager lmn = new LinearLayoutManager(this);
        lmn.setOrientation(RecyclerView.VERTICAL);
        recycler.setLayoutManager(lmn);
        myAdapter = new RecyclerViewAdapterHomeActivity(this, coinList);
        recycler.setAdapter(myAdapter);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        networkReceiver = new NetworkReceiver();

        btn = findViewById(R.id.boutonExchangeActivity);

        IntentFilter filter = new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION);
        registerReceiver(networkReceiver, filter);

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
        int id = item.getItemId();
        if (id == R.id.home) {
            return true;
        } else if (id == R.id.reload) {
            go();
            return true;
        }
        return true;
    }

    /**
     * Méthode appelé au démarrage de l'activité. Elle récupère les 200 premières cryptos et les affiche
     */
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
                            List<CoinObject> coins = decodeJSON(data);
                            coinList.clear();
                            coinList.addAll(coins);
                            myAdapter.notifyDataSetChanged();
                        } catch (Exception e) {
                            // tv.setText("Erreur lors de la recup crypto");
                        }
                    }
                });
            }
        });
    }

    /**
     * Récupère le JSON fourni par l'API https://openapi.coinstats.app/ selon la requete
     *
     * @param requete Requête a envoyer à l'API
     * @param apiKey  Clé API privé pour faire la requête
     * @return Retourne le JSON retourné par l'API
     */
    public String getDataFromHTTP(String requete, String apiKey) {
        StringBuilder result = new StringBuilder();
        HttpURLConnection connexion = null;
        try {
            // Création de l'URL
            URL url = new URL(requete);
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

    /**
     * Décode le JSON renvoyé par l'API
     *
     * @param json JSON a décodé
     * @return Retourne une List de Coin
     */
    public List<CoinObject> decodeJSON(String json) throws JSONException {
        List<CoinObject> coins = new ArrayList<>();

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
            CoinObject coin = new CoinObject(id, name, symbol, icon, price);
            coins.add(coin);
        }

        return coins;
    }

    /**
     * Méthode appelé lors du clique le bouton, pour aller à la page de conversion
     * @param view
     */
    public void changerActi(View view) {
        Intent intent = new Intent(this, ConvertActivity.class);
        this.startActivity(intent);
    }

}