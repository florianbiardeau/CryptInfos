package com.example.cryptinfos;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

/**
 * Classe de base servant à afficher la toolbar. Chaque activité en héritera pour afficher la toolbar.
 */
public class BaseActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getLayoutResourceId()); // Charge le layout de l'activité enfant
        setupToolbar();
    }

    /**
     * @return Retourne le layout de l'activité home
     */
    protected int getLayoutResourceId() {
        return R.layout.home_layout;
    }

    /**
     * Récupère la toolbar pour l'enregistrer dans notre activité comme supportActionBar
     */
    private void setupToolbar() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        if (toolbar != null) {
            setSupportActionBar(toolbar);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.reload) {
            reloadPage();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * Méthode à Overrider pour définir le reload de la page courante
     */
    protected void reloadPage() {
        Toast.makeText(this, "Reload non défini pour cette page", Toast.LENGTH_SHORT).show();
    }


}
