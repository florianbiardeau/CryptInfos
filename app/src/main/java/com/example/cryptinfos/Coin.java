package com.example.cryptinfos;

import java.util.List;

public class Coin {
    private String id;
    private String name;
    private String symbol;
    private String iconUrl;
    private double price;
    private List<String> explorers = null;

    // Utiliser par HomeActivity
    public Coin(String id, String name, String symbol, String iconUrl, double price) {
        this.id = id;
        this.name = name;
        this.symbol = symbol;
        this.price = price;
        this.iconUrl = iconUrl;
    }

    // Utiliser par CryptoActivity
    public Coin(String id, String name, String symbol, String iconUrl, double price, List<String> explorers) {
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
