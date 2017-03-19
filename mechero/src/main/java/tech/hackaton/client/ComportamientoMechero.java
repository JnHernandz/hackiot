package tech.hackaton.client;

import java.util.concurrent.ThreadLocalRandom;

/**
 * Created by j.hernandez on 3/18/2017.
 */
public class ComportamientoMechero {

    private int x;
    private double maximo;

    public ComportamientoMechero(){
        x=0;
        maximo = 25.0;
    }

    public double siguieteValor(){
        int stepsBeforeRegularization = 20;
        if ( x < stepsBeforeRegularization ){
            x++;
            return Math.sin(Math.toRadians(90/stepsBeforeRegularization*x)) * maximo;
        }
        x++;
        return maximo + (Math.random() < 0.5? -1: 1) * ThreadLocalRandom.current().nextDouble(2, 5);
    }
}
