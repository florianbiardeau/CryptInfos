package com.example.cryptinfos;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Date;
import java.util.Locale;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Activité lancée lors du clique sur une crypto dans l'activité HomeActivity
 */
public class CryptoActivity extends AppCompatActivity {

    private LineChart lineChart;
    private ImageView iconCrypto;
    private TextView nomCrypto;
    private TextView rangCrypto;
    private TextView prixCrypto;
    private TextView volumeCrypto;
    private TextView marketCapCrypto;
    private TextView supplyDispoCrypto;
    private TextView supplyTotaleCrypto;


    private RecyclerView recyclerView;
    private RecyclerViewAdapterCryptoActivity adapter;
    private String id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.crypto_layout);

        // Récupération des éléments de la page
        iconCrypto = findViewById(R.id.iconCrypto);
        nomCrypto = findViewById(R.id.nomCrypto);
        rangCrypto = findViewById(R.id.rangCrypto);
        prixCrypto = findViewById(R.id.prixCrypto);
        volumeCrypto = findViewById(R.id.volumeCrypto);
        marketCapCrypto = findViewById(R.id.marketCapCrypto);
        supplyDispoCrypto = findViewById(R.id.supplyDispoCrypto);
        supplyTotaleCrypto = findViewById(R.id.supplyTotaleCrypto);
        lineChart = findViewById(R.id.lineChart);
        recyclerView = findViewById(R.id.recyclerViewExplorers);

        // Partie sur le RecyclerView contenant les cryptos
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        // Récupération de la ToolBar
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // Récupération de l'ID de la crypto depuis l'intent
        Intent i = getIntent();
        id = i.getStringExtra("id");

        // Chargement des données de la crypto et du graphique
        crypto();
        charts();
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
            Intent intent = new Intent(this, HomeActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
            return true;
        } else if (id == R.id.reload) {
            crypto();
            return true;
        }
        return true;
    }

    /**
     * Méthode appelé au démarrage de l'activité. Elle récupère les infos sur la crypto sélectionnée et les affiche
     */
    public void crypto() {
        ExecutorService executor = Executors.newSingleThreadExecutor();
        Handler handler = new Handler(Looper.getMainLooper());
        CryptoActivity context = this;
        executor.execute(new Runnable() {
            @Override
            public void run() {
                String data = getDataFromHTTP("https://openapiv1.coinstats.app/coins/" + id, "srkjExa1IBZMMoDmd5YTI4sWbLpce8KFavfrzjbKKvU=");
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            CoinObject coin = decodeJson(data);
                            Glide.with(context)
                                    .load(coin.getIconUrl())
                                    .into(iconCrypto);
                            nomCrypto.setText("Nom : " + coin.getName() + " (" + coin.getSymbol() + ")");
                            rangCrypto.setText("Rang : " + coin.getRank());
                            prixCrypto.setText("Prix : " + coin.getPrice());
                            volumeCrypto.setText("Volume : " + coin.getVolume());
                            marketCapCrypto.setText("Market Cap  : " + coin.getMarketCap());
                            supplyDispoCrypto.setText("Supply disponible : " + coin.getAvailableSupply());
                            supplyTotaleCrypto.setText("Supply totale : " + coin.getTotalSupply());
                        } catch (Exception e) {
                            Toast.makeText(CryptoActivity.this, "Erreur lors du chargement de la crypto", Toast.LENGTH_LONG).show();
                        }
                    }
                });
            }
        });
    }

    /**
     * Méthode appelé au démarrage de l'activité. Elle récupère les prix de la crypto sélectionnée et les affiche
     */
    public void charts(){
        ExecutorService executor = Executors.newSingleThreadExecutor();
        Handler handler = new Handler(Looper.getMainLooper());
        executor.execute(() -> {
            String data = getDataFromHTTP("https://openapiv1.coinstats.app/coins/" + id + "/charts?period=all", "srkjExa1IBZMMoDmd5YTI4sWbLpce8KFavfrzjbKKvU=");
            handler.post(() -> {
                try {
                    updateChart(data);
                } catch (Exception e) {
                    Toast.makeText(CryptoActivity.this, "Erreur lors du chargement du graphique", Toast.LENGTH_LONG).show();
                }
            });
        });
    }

    /**
     * Récupère le JSON fourni par l'API https://openapi.coinstats.app/ selon la requete
     * @param requete Requête a envoyer à l'API
     * @param apiKey Clé API privé pour faire la requête
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
            Toast.makeText(CryptoActivity.this, "Erreur lors de la récupération des données", Toast.LENGTH_LONG).show();
        }
        return result.toString();
    }

    /**
     * Décode le JSON renvoyé par l'API
     * @param json JSON a décodé
     * @return Retourne un CoinObject contenant toutes les informations de la crypto
     */
    public CoinObject decodeJson(String json){
        try {
            JSONObject obj = new JSONObject(json);

            String nom = obj.getString("name");
            String iconUrl = obj.getString("icon");
            String symbole = obj.getString("symbol");
            int rang = obj.getInt("rank");
            double prix = obj.getDouble("price");
            double volume = obj.getDouble("volume");
            double marketCap = obj.getDouble("marketCap");
            int supplyDispo = obj.getInt("availableSupply");
            int supplyTotale = obj.getInt("totalSupply");

            JSONArray explorersArray = obj.getJSONArray("explorers");

            List<String> explorerLinks = new ArrayList<>();
            for (int i = 0; i < explorersArray.length(); i++) {
                explorerLinks.add(explorersArray.getString(i));
            }

            adapter = new RecyclerViewAdapterCryptoActivity(this, explorerLinks);
            recyclerView.setAdapter(adapter);

            return new CoinObject(id, nom, symbole, iconUrl, prix, rang, volume, marketCap, supplyDispo, supplyTotale);

        } catch (Exception e) {
            Toast.makeText(CryptoActivity.this, "Erreur lors du décodage du JSON", Toast.LENGTH_LONG).show();
            return null;
        }
    }

    /**
     * Mets à jour le graphique avec les prix de la crypto
     * @param json JSON contenant les prix de la crypto
     */
    private void updateChart(String json) {
        try {
            ArrayList<String> dates = new ArrayList<>(); // Stocker les dates en texte

            JSONArray history = new JSONArray(json); // Pas besoin de JSONObject, c'est déjà un tableau

            ArrayList<Entry> entries = new ArrayList<>();
            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());

            for (int i = 0; i < history.length(); i++) {
                JSONArray dataPoint = history.getJSONArray(i);
                long timestamp = dataPoint.getLong(0) * 1000; // Convertir en millisecondes
                float price = (float) dataPoint.getDouble(1);

                entries.add(new Entry(i, price)); // L'index i est utilisé pour X
                dates.add(new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(new Date(timestamp)));
            }


            if (entries.isEmpty()) {
                Toast.makeText(CryptoActivity.this, "Aucune donnée pour le graphique", Toast.LENGTH_LONG).show();
                return;
            }

            LineDataSet dataSet = new LineDataSet(entries, "Prix de la Crypto");
            dataSet.setColor(Color.BLUE);
            dataSet.setValueTextColor(Color.BLACK);
            dataSet.setLineWidth(2f);

            LineData lineData = new LineData(dataSet);
            lineChart.setData(lineData);
            lineChart.invalidate();

            // Formatter pour afficher les dates sur l'axe X
            XAxis xAxis = lineChart.getXAxis();
            xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
            xAxis.setGranularity(1f);
            xAxis.setValueFormatter(new IndexAxisValueFormatter(dates));

        } catch (Exception e) {
            Toast.makeText(CryptoActivity.this, "Erreur lors de la mise à jour du graphique", Toast.LENGTH_LONG).show();
        }
    }
}