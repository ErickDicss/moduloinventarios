package com.example.pruebainventariostres;

import android.app.Application;

import java.util.ArrayList;

public class gurusystem extends Application {

    public ArrayList<String> getaProductosGolden() {
        return aProductosGolden;
    }

    public void setaProductosGolden(ArrayList<String> aProductosGolden) {
        this.aProductosGolden = aProductosGolden;
    }

    public ArrayList<String> getaProductosCompetencia() {
        return aProductosCompetencia;
    }

    public void setaProductosCompetencia(ArrayList<String> aProductosCompetencia) {
        this.aProductosCompetencia = aProductosCompetencia;
    }

    private ArrayList<String> aProductosGolden;
    private ArrayList <String> aProductosCompetencia;
}
