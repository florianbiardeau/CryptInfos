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

    // Attribut servant à savoir si on a déjà eu un changement de connexion auparavant
    private boolean firstCheck = true;

    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent.getAction() != null && intent.getAction().equals(ConnectivityManager.CONNECTIVITY_ACTION)) {
            ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
            if (cm != null) {
                NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
                boolean isConnected = activeNetwork != null && activeNetwork.isConnectedOrConnecting();

                // Si c'est le premier appel ...
                if (firstCheck) {
                    // ... et qu'on n'est pas connecté à Internet, on mets firstcheck à false pour notifier que l'on a déjà changer d'état ...
                    if (!isConnected) {
                        firstCheck = false;
                    } else { // ... et si on est connecté on ne fait rien (stop la méthode) car c'est "normal"
                        return;
                    }
                }

                // Si ce n'est pas le premier appel ...
                if (isConnected) {
                    // ... c'est qu'on a déjà perdu notre connexion une première fois donc on notifie qu'on la regagné ...
                    Toast.makeText(context, "📶 Connexion rétablie", Toast.LENGTH_LONG).show();
                } else { // ... et si on est pas connecté on le notifie tout simplement
                    Toast.makeText(context, "❌ Connexion perdue, les données ne seront plus mis à jour", Toast.LENGTH_LONG).show();
                }
            }
        }
    }
}
