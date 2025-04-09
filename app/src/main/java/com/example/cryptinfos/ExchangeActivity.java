package com.example.cryptinfos;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.google.android.material.button.MaterialButton;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ExchangeActivity extends AppCompatActivity {

    private Map<String, Double> coinsMap;
    private AutoCompleteTextView spinner1;
    private AutoCompleteTextView spinner2;
    private EditText edt1;
    private TextView edt2;

    private MaterialButton btn;

    private final Handler handler = new Handler(Looper.getMainLooper());

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.exchange_layout);

        spinner1 = findViewById(R.id.spinner1);
        spinner2 = findViewById(R.id.spinner2);
        edt1 = findViewById(R.id.editTextNumber1);
        edt2 = findViewById(R.id.editTextNumber2);
        btn = findViewById(R.id.switchButton);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Intent i = getIntent();

        loadCoins();

        // Mise à jour de la conversion dès que le texte change
        edt1.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // Rien
            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                convert();
            }
            @Override
            public void afterTextChanged(Editable s) {
                // Rien
            }
        });

        // Mise à jour lors du changement de sélection dans les spinners
        AdapterView.OnItemSelectedListener selectionListener = new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                convert();
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // Rien
            }
        };
        spinner1.setOnItemSelectedListener(selectionListener);
        spinner2.setOnItemSelectedListener(selectionListener);
    }

    public void loadCoins() {
        ExecutorService executor = Executors.newSingleThreadExecutor();
        Handler handler = new Handler(Looper.getMainLooper());

        executor.execute(new Runnable() {
            @Override
            public void run() {
                // Appel de l'API pour récupérer la liste des coins (ici limité à 200)
                String data = getDataFromHTTP("https://openapiv1.coinstats.app/coins?limit=200",
                        "srkjExa1IBZMMoDmd5YTI4sWbLpce8KFavfrzjbKKvU=");
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            // Décodage du JSON pour obtenir un dictionnaire associant le nom de la crypto à son prix
                            coinsMap = decodeJsonForCoinNames(data);
                            // Extraction et tri de la liste des noms de crypto
                            List<String> coinNames = new ArrayList<>(coinsMap.keySet());
                            Collections.sort(coinNames);
                            // Création d'un ArrayAdapter pour alimenter les spinners
                            ArrayAdapter<String> adapter = new ArrayAdapter<>(ExchangeActivity.this,
                                    android.R.layout.simple_spinner_item, coinNames);
                            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                            spinner1.setAdapter(adapter);
                            spinner2.setAdapter(adapter);
                        } catch (Exception e) {
                            Toast.makeText(ExchangeActivity.this, "Erreur lors du chargement des coins", Toast.LENGTH_LONG).show();
                            e.printStackTrace();
                        }
                    }
                });
            }
        });
    }


    public Map<String, Double> decodeJsonForCoinNames(String json) {
        Map<String, Double> coinMap = new HashMap<>();
        try {
            JSONObject obj = new JSONObject(json);
            JSONArray resultArray = obj.getJSONArray("result");
            for (int i = 0; i < resultArray.length(); i++) {
                JSONObject coinObj = resultArray.getJSONObject(i);
                String coinName = coinObj.getString("name");
                double price = coinObj.getDouble("price");
                coinMap.put(coinName, price);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return coinMap;
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

    /**
     * Convertit le montant saisi (en unité de la première crypto) en l'équivalent dans la deuxième crypto.
     * La conversion se fait en multipliant le montant par le prix de la première crypto puis en le divisant par le prix de la deuxième crypto.
     */
    private void convert() {
        String input = edt1.getText().toString();
        if (input.isEmpty()) {
            edt2.setText("");
            return;
        }
        try {
            double amount = Double.parseDouble(input);
            // Récupération des noms sélectionnés
            String coinName1 = spinner1.getText().toString();
            String coinName2 = spinner2.getText().toString();
            Double price1 = coinsMap.get(coinName1);
            Double price2 = coinsMap.get(coinName2);
            if (price1 != null && price2 != null && price2 != 0) {
                double result = (amount * price1) / price2;
                edt2.setText(String.format(Locale.getDefault(), "%.6f", result));
            }
        } catch (Exception e) {
            edt2.setText("Erreur");
            e.printStackTrace();
        }
    }

    public void switchCrypto(View v) {
        String coin1 = spinner1.getText().toString();
        String coin2 = spinner2.getText().toString();

        // On échange les textes des deux AutoCompleteTextView
        spinner1.setText(coin2, false);
        spinner2.setText(coin1, false);

        // Recalcul automatique après le switch
        convert();
    }
}