package com.example.cryptinfos;

import java.util.List;

/**
 * Classe stockant les données d'une crypto pour pouvoir les afficher
 */
public class CoinObject {
    private String id;
    private String name;
    private String symbol;
    private String iconUrl;
    private double price;
    private List<String> explorers = null;


    /**
     * Constructeur utilisé par HomeActivity
     * @param id Id de la crypto
     * @param name Nom de la crypto
     * @param symbol Symbole de la crypto
     * @param iconUrl Url de l'icon de la crypto
     * @param price Prix de la crypto
     */
    public CoinObject(String id, String name, String symbol, String iconUrl, double price) {
        this.id = id;
        this.name = name;
        this.symbol = symbol;
        this.price = price;
        this.iconUrl = iconUrl;
    }

    /**
     * Constructeur utilisé par CryptoActivity
     * @param id Id de la crypto
     * @param name Nom de la crypto
     * @param symbol Symbole de la crypto
     * @param iconUrl Url de l'icon de la crypto
     * @param price Prix de la crypto
     * @param explorers Liste des url de la crypto
     */
    public CoinObject(String id, String name, String symbol, String iconUrl, double price, List<String> explorers) {
        this.id = id;
        this.name = name;
        this.symbol = symbol;
        this.price = price;
        this.iconUrl = iconUrl;
        this.explorers = explorers;
    }

    public String getId(){
        return id;
    }

    public String getName() {
        return name;
    }

    public String getSymbol() {
        return symbol;
    }

    public String getIconUrl() {
        return iconUrl;
    }

    public double getPrice() {
        return price;
    }

    public List<String> getExplorers() {
        return explorers;
    }
}
