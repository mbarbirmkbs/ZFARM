package com.example.odataapp01.data;

import org.joda.time.LocalDateTime;

import java.util.Date;

public class CowState {

    private String Bukrs;
    private String Frmid;
    private String Grlid;
    private String Grlib;
    private String Knjgk;
    private String Nazkg;
    private String Grlime;
    private String Repro;
    private LocalDateTime ReproDate;
    private LocalDateTime DatRodj;
    private String Fazal;
    private String Brlak;
    private Integer Danul;

    public CowState() {

    }

    public CowState(String i_Bukrs,
                    String i_Frmid,
                    String i_Grlid,
                    String i_Grlib,
                    String i_Knjgk,
                    String i_Nazkg,
                    String i_Grlime,
                    String i_Repro,
                    LocalDateTime i_ReproDate,
                    LocalDateTime i_DatRodj,
                    String i_Fazal,
                    String i_Brlak,
                    Integer i_Danul) {

        this.Bukrs = i_Bukrs;
        this.Frmid = i_Frmid;
        this.Grlid = i_Grlid;
        this.Grlib = i_Grlib;
        this.Knjgk = i_Knjgk;
        this.Nazkg = i_Nazkg;
        this.Grlime = i_Grlime;
        this.Repro = i_Repro;
        this.ReproDate = i_ReproDate;
        this.DatRodj = i_DatRodj;
        this.Fazal = i_Fazal;
        this.Brlak = i_Brlak;
        this.Danul = i_Danul;

    }

    public String getBukrs() {
        return Bukrs;
    }

    public String getFrmid() {
        return Frmid;
    }

    public String getGrlid() {
        return Grlid;
    }

    public String getGrlib() {
        return Grlib;
    }

    public String getGrlime() {
        return Grlime;
    }

    public String getKnjgk() {
        return Knjgk;
    }

    public String getNazkg() {
        return Nazkg;
    }

    public String getRepro() {
        if (Repro != null){
            return Repro;
        }
        else {
            return "";
        }
    }

    public LocalDateTime getReproDate() {
        return ReproDate;
    }

    public LocalDateTime getDatRodj() {
        return DatRodj;
    }

    public String getFazal() {
        return Fazal;
    }

    public Integer getDanul() {
        return Danul;
    }

    public String getBrlak() {
        return Brlak;
    }
}
