package tech.hackaton.client;

import com.google.gson.Gson;

/**
 * Created by j.hernandez on 3/12/2017.
 */
public class Mechero {
    public boolean encendido;
    public double llama;

    public Mechero(){
        this.encendido = false;
        this.llama = 0;
    }

    public boolean encederMechero() {
        this.encendido = true;
        System.out.println("El mechero ha sido encendido.");
        return true;
    }

    public boolean apagarMechero() {
        this.encendido = false;
        System.out.println("El mechero ha sido apagado.");
        return true;
    }

    public boolean cambiarLlama(double llama) {
        System.out.printf("Se aumentó el volumen de la llama a %.0f.\r\n", llama);
        this.llama = llama;
        return true;
    }

    public String serialize() {
        Gson gson = new Gson();
        return gson.toJson(this);
    }
}
