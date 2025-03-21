package com.example.cryptinfos;

public class Coin {
    private String name;
    private String symbol;
    private String icon;
    private int rank;
    private double price;

    public Coin(String name, String symbol, String icon, int rank, double price) {
        this.name = name;
        this.symbol = symbol;
        this.icon = icon;
        this.rank = rank;
        this.price = price;
    }

    public String getName() {
        return name;
    }

    public String getSymbol() {
        return symbol;
    }

    public String getIcon() {
        return icon;
    }

    public int getRank() {
        return rank;
    }

    public double getPrice() {
        return price;
    }
}
