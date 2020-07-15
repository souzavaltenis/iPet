package com.example.ipet.entities;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.io.Serializable;
import java.util.Objects;

public class Caso implements Serializable, Parcelable {

    private String id;
    private String titulo;
    private String descricao;
    private Double valor;
    private DocumentReference ong;

    public Caso(){
    }

    public Caso(String id, String titulo, String descricao, Double valor, DocumentReference ong) {
        this.id = id;
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

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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
                "id='" + id + '\'' +
                ", titulo='" + titulo + '\'' +
                ", descricao='" + descricao + '\'' +
                ", valor=" + valor +
                ", ong=" + ong +
                '}';
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeString(titulo);
        dest.writeString(descricao);
        dest.writeDouble(valor);
        dest.writeString(ong.getPath());
    }

    public Caso(Parcel in) {
        id = in.readString();
        titulo = in.readString();
        descricao = in.readString();
        valor = in.readDouble();
        ong = FirebaseFirestore.getInstance().document(Objects.requireNonNull(in.readString()));
    }

    public static final Parcelable.Creator<Caso> CREATOR = new Parcelable.Creator<Caso>() {
        public Caso createFromParcel(Parcel in) {
            return new Caso(in);
        }

        public Caso[] newArray(int size) {
            return new Caso[size];
        }
    };
}
