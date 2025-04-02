package com.example.cryptinfos;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
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
import org.json.JSONException;
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

public class CryptoActivity extends AppCompatActivity {

    private EditText cryptoInput;
    private Button btnSearch;
    private TextView cryptoInfo;

    private RecyclerView recyclerView;
    private ExplorerAdapter adapter;
    private List<String> explorerLinks;
    private Toolbar toolbar;

    private LineChart lineChart;
    private final Handler handler = new Handler(Looper.getMainLooper());

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.crypto_layout);

        cryptoInfo = findViewById(R.id.cryptoInfo);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Intent i = getIntent();
        String id = i.getStringExtra("id");

        lineChart = findViewById(R.id.lineChart);

        recyclerView = findViewById(R.id.recyclerViewExplorers);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        explorerLinks = new ArrayList<>();
        crypto(id);
        charts(id);
    }

    public void crypto(String id) {
        ExecutorService executor = Executors.newSingleThreadExecutor();
        Handler handler = new Handler(Looper.getMainLooper());
        executor.execute(new Runnable() {
            @Override
            public void run() {
                String data = getDataFromHTTP("https://openapiv1.coinstats.app/coins/" + id, "srkjExa1IBZMMoDmd5YTI4sWbLpce8KFavfrzjbKKvU=");
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            cryptoInfo.setText(decodeJson(data));
                        } catch (Exception e) {
                            cryptoInfo.setText("Erreur lors de la recup crypto");
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

    public String decodeJson(String json){
        try {
            JSONObject obj = new JSONObject(json);

            String nom = obj.getString("name");
            String acronyme = obj.getString("symbol");
            String prix = obj.getString("price");

            JSONArray explorersArray = obj.getJSONArray("explorers");

            for (int i = 0; i < explorersArray.length(); i++) {
                explorerLinks.add(explorersArray.getString(i));
            }

            adapter = new ExplorerAdapter(this, explorerLinks);
            recyclerView.setAdapter(adapter);

            return "nom : " + nom
                    + "\nacronyme : " + acronyme
                    + "\nprix : $" + prix;

        } catch (Exception e) {
            return "Erreur de décodage JSON";
        }
    }

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
                cryptoInfo.setText("Aucune donnée pour le graphique");
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
            e.printStackTrace();
            cryptoInfo.setText("Erreur lors de la mise à jour du graphique");
        }
    }


    public void charts(String id){
        ExecutorService executor = Executors.newSingleThreadExecutor();
        Handler handler = new Handler(Looper.getMainLooper());
        executor.execute(() -> {
            String data = getDataFromHTTP("https://openapiv1.coinstats.app/coins/" + id + "/charts?period=all", "srkjExa1IBZMMoDmd5YTI4sWbLpce8KFavfrzjbKKvU=");
            System.out.println(data);
            handler.post(() -> {
                try {
                    updateChart(data);
                } catch (Exception e) {
                    cryptoInfo.setText("Erreur lors de la recup crypto");
                }
            });
        });
    }

    private void updateUI(String message) {
        handler.post(() -> cryptoInfo.setText(message));
    }
}