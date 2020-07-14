package com.example.ipet.entities;

public class CasoOng {

    private Caso caso;
    private Ong ong;

    public CasoOng(Caso caso, Ong ong) {
        this.caso = caso;
        this.ong = ong;
    }

    public Caso getCaso() {
        return caso;
    }

    public void setCaso(Caso caso) {
        this.caso = caso;
    }

    public Ong getOng() {
        return ong;
    }

    public void setOng(Ong ong) {
        this.ong = ong;
    }

    @Override
    public String toString() {
        return "CasoOng{" +
                "caso=" + caso +
                ", ong=" + ong +
                '}';
    }
}
