package com.example.odataapp01.data;

import org.joda.time.LocalDateTime;

public class CowEntry {

    private String bukrs;
    private String grlid;
    private String tipUnosa;
    private String sazetak;
    private LocalDateTime datun;

    public CowEntry() {

    }

    public CowEntry(String i_bukrs,
                    String i_grlid,
                    String i_tipUnosa,
                    String i_sazetak,
                    LocalDateTime i_datun) {

        this.bukrs    = i_bukrs;
        this.grlid    = i_grlid;
        this.tipUnosa = i_tipUnosa;
        this.sazetak  = i_sazetak;
        this.datun    = i_datun;

    }

    public String getBukrs() {
        return bukrs;
    }

    public String getGrlid() {
        return grlid;
    }

    public String getTipUnosa() {

        switch (tipUnosa){
            case "PREM": return "Premeštaj";
            case "TELJ": return "Teljenje";
            case "PKAT": return "Promena kategorije";
            case "DUST": return "Dodavanje u stado";
            case "TRET": return "Tretman";
            case "VO":   return "Osemenjivanje";
            default:     return tipUnosa;
        }

    }

    public String getSazetak() {
        sazetak = sazetak.replace(", ",System.getProperty("line.separator"));
        return sazetak;
    }

    public LocalDateTime getDatun() {
        return datun;
    }

}
