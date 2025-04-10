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
    private int rank;
    private double volume;
    private double marketCap;
    private int availableSupply;
    private int totalSupply;


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
     * @param rank Rang de la crypto
     * @param volume Volume de la crypto
     * @param marketCap Market Cap de la crypto
     * @param availableSupply Supply disponible de la crypto
     * @param totalSupply Supply totale de la crypto
     */
    public CoinObject(String id, String name, String symbol, String iconUrl, double price, int rank, double volume, double marketCap, int availableSupply, int totalSupply) {
        this.id = id;
        this.name = name;
        this.symbol = symbol;
        this.price = price;
        this.iconUrl = iconUrl;
        this.rank = rank;
        this.volume = volume;
        this.marketCap = marketCap;
        this.availableSupply = availableSupply;
        this.totalSupply = totalSupply;
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

    public int getRank() {
        return rank;
    }

    public double getVolume() {
        return volume;
    }

    public double getMarketCap() {
        return marketCap;
    }

    public int getAvailableSupply() {
        return availableSupply;
    }

    public int getTotalSupply() {
        return totalSupply;
    }

}
