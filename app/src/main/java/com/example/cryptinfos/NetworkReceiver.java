package com.example.cryptinfos;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.widget.Toast;

/**
 * NetworkReceiver est BroadcastReceiver un qui permet de détecter les changements de connectivité réseau sur l'appareil.
 * Il affiche un message à l'utilisateur lorsque la connexion est perdue ou rétablie.
 * Pour éviter une alerte lors du premier démarrage, il utilise un indicateur firstCheck.
 */
public class NetworkReceiver extends BroadcastReceiver {

    private boolean firstCheck = true;

    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent.getAction() != null && intent.getAction().equals(ConnectivityManager.CONNECTIVITY_ACTION)) {
            ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
            if (cm != null) {
                NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
                boolean isConnected = activeNetwork != null && activeNetwork.isConnectedOrConnecting();

                // Si c'est le premier appel, on ne fait rien et on met firstCheck à false
                if (firstCheck) {
                    if (!isConnected) {
                        firstCheck = false;
                    } else {
                        return;
                    }
                }

                if (isConnected) {
                    Toast.makeText(context, "📶 Connexion rétablie", Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(context, "❌ Connexion perdue, les données ne seront plus mis à jour", Toast.LENGTH_LONG).show();
                }
            }
        }
    }
}
