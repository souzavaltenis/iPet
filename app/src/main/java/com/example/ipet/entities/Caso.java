package com.example.ipet.entities;

import com.google.firebase.firestore.DocumentReference;

import java.io.Serializable;

public class Caso implements Serializable {

    private String titulo;
    private String descricao;
    private Double valor;
    private DocumentReference ong;

    public Caso(){
    }

    public Caso(String titulo, String descricao, Double valor, DocumentReference ong) {
        this.titulo = titulo;
        this.descricao = descricao;
        this.valor = valor;
        this.ong = ong;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public Double getValor() {
        return valor;
    }

    public void setValor(Double valor) {
        this.valor = valor;
    }

    public DocumentReference getOng() {
        return ong;
    }

    public void setOng(DocumentReference ong) {
        this.ong = ong;
    }

    @Override
    public String toString() {
        return "Caso{" +
                "titulo='" + titulo + '\'' +
                ", descricao='" + descricao + '\'' +
                ", valor=" + valor +
                ", ong=" + ong +
                '}';
    }
}
