package com.br.tiago.roupas.Helpers;

import android.os.Build;
import android.support.annotation.RequiresApi;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.Date;

public class DataHelper {

    public static String createdAtToBr(String createdAt){

        SimpleDateFormat fmtUSA = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        SimpleDateFormat fmtBR  = new SimpleDateFormat("dd/MM/yyyy HH:mm");

        Date date = null;
        try {
            date = fmtUSA.parse(createdAt);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return fmtBR.format(date);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public static String calcularPeriodoAguardandoDonativo(String createdAt) throws ParseException {

        String dtCreatedAt = createdAt.substring(0,createdAt.length()-9);
        String textoRetorno = "";

        String[] parts = dtCreatedAt.split("-");
        int dia = Integer.parseInt( parts[2] );
        int mes = Integer.parseInt( parts[1] );
        int ano = Integer.parseInt( parts[0] );

        LocalDate dtAtual = LocalDate.now();
        LocalDate dtCadastro = LocalDate.of(ano, mes, dia);

        String dias  = Long.toString( ChronoUnit.DAYS.between(dtCadastro, dtAtual) );
        String meses = Long.toString( ChronoUnit.MONTHS.between(dtCadastro, dtAtual) );
        String anos  = Long.toString( ChronoUnit.YEARS.between(dtCadastro, dtAtual) );

        if( ChronoUnit.DAYS.between(dtCadastro, dtAtual) <= 31 ){
            textoRetorno = "Há "+dias+" dia(s)";
        }else if( ChronoUnit.MONTHS.between(dtCadastro, dtAtual) <= 12  ){
            textoRetorno = "Há "+meses+" mese(s)";
        }else{
            textoRetorno = "Há "+ChronoUnit.YEARS.between(dtCadastro, dtAtual)+" anos";
        }

        return textoRetorno;
    }
}
