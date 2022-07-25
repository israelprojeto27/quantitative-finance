package com.app.commons.utils;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class Utils {

    private static final String FORMAT_DATA = "dd/MM/yyyy";

    private static final String FORMAT_DATA_V2 = "yyyy-MM-dd";

    private static List<String> listPeriodo = Arrays.asList("diario", "semanal", "mensal");

    public static LocalDate converteStringToLocalDateTime(String data) {

        try {
            if (data.length() == 9){
                data = "0" + data;
            }
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern(FORMAT_DATA);
            return LocalDate.parse(data, formatter);
        }
        catch(Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static LocalDate converteStringToLocalDateTime3(String data) {

        try {
            if (data.length() == 9){
                data = "0" + data;
            }
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern(FORMAT_DATA_V2);
            return LocalDate.parse(data, formatter);
        }
        catch(Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static String converteStringToLocalDateTimeString(String data) {

        // 2020-10-29T15:06:19.777
        try {
            if (data.length() == 9){
                data = "0" + data;
            }

            data = data.substring(0, 10);

            DateTimeFormatter formatter = DateTimeFormatter.ofPattern(FORMAT_DATA_V2);
            LocalDate dt = LocalDate.parse(data, formatter);

            DateTimeFormatter formatter2 = DateTimeFormatter.ofPattern("dd/MM/yyyy");
            return dt.format(formatter2);
        }
        catch(Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static LocalDateTime converteStringToLocalDateTime2(String data) {

        try {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern(FORMAT_DATA);
            return LocalDateTime.parse(data, formatter);
        }
        catch(Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static String converteLocalDateTimeToString(LocalDateTime dataHora) {

        try {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");
            return dataHora.format(formatter);
        }
        catch(Exception e) {
            return "";
        }
    }

    public static String converteLocalDateToString(LocalDate dataHora) {

        try {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
            return dataHora.format(formatter);
        }
        catch(Exception e) {
            return "";
        }
    }


    public static double converterDoubleDoisDecimais(double precoDouble) {
        DecimalFormat fmt = new DecimalFormat("0.00");
        String string = fmt.format(precoDouble);
        String[] part = string.split("[,]");
        String string2 = part[0]+"."+part[1];
        double preco = Double.parseDouble(string2);
        return preco;
    }


    public static String converterDoubleDoisDecimaisString(double preco) {
        Locale lo = new Locale("pt", "BR");
        NumberFormat cf = NumberFormat.getCurrencyInstance(lo);
        return  cf.format(preco);
    }

    public static double converterDoubleQuatroDecimais(double precoDouble) {
        DecimalFormat fmt = new DecimalFormat("0.0000");
        String string = fmt.format(precoDouble);
        String[] part = string.split("[,]");
        String string2 = part[0]+"."+part[1];
        double preco = Double.parseDouble(string2);
        return preco;
    }

    public static double converterDoubleSeisDecimais(double precoDouble) {
        DecimalFormat fmt = new DecimalFormat("0.000000");
        String string = fmt.format(precoDouble);
        String[] part = string.split("[,]");
        String string2 = part[0]+"."+part[1];
        double preco = Double.parseDouble(string2);
        return preco;
    }

    public static String formatDataHistorico(String dataFmt) {

        try {
            SimpleDateFormat fmtEntrada = new SimpleDateFormat("yyyy-MM-dd");
            SimpleDateFormat fmtSaida = new SimpleDateFormat("dd/MM/yyyy");
            Date data = fmtEntrada.parse(dataFmt);

            return fmtSaida.format(data);
        }
        catch(Exception e) {
            System.out.println("Message error: " + e.getMessage());
        }
        return null;
    }


    public static LocalDate convertePeriodoExtrato(String periodo) {

        LocalDate dataInicio = LocalDate.now();
        long minusMonth = 0;
        if (periodo.equals("mesCorrente")) {
            minusMonth = 0;
        }
        else if (periodo.equals("ultimoMes")) {
            minusMonth = 1;
        }
        else if (periodo.equals("ultimoTresMeses")) {
            minusMonth = 3;
        }
        else if (periodo.equals("ultimoSeisMeses")) {
            minusMonth = 6;
        }
        else if (periodo.equals("ultimoDozeMeses")) {
            minusMonth = 12;
        }
        else if (periodo.equals("ultimoVinteQuatroMeses")) {
            minusMonth = 24;
        }
        else if (periodo.equals("ultimoVTrintaSeisMeses")) {
            minusMonth = 36;
        }

        if ( minusMonth == 0){
            return LocalDate.now();
        }
        else {
            LocalDate dtInicio = dataInicio.minusMonths(minusMonth);
            LocalDate dt = dtInicio.withDayOfMonth(1);
            return  dt;
        }
    }

    public static String  convertePeriodoExtratoFmt(String  periodo) {

        String ret = "";
        if (periodo.equals("mesCorrente")) {
            ret = "Mês Corrente";
        }
        else if (periodo.equals("ultimoMes")) {
            ret = "Último Mês";
        }
        else if (periodo.equals("ultimoTresMeses")) {
            ret = "Últimos 3 Meses";
        }
        else if (periodo.equals("ultimoSeisMeses")) {
            ret = "Últimos 6 Meses";
        }
        else if (periodo.equals("ultimoDozeMeses")) {
            ret = "Últimos 12 Meses";
        }
        else if (periodo.equals("ultimoVinteQuatroMeses")) {
            ret = "Últimos 24 Meses";
        }
        else if (periodo.equals("ultimoVTrintaSeisMeses")) {
            ret = "Últimos 36 Meses";
        }

        return ret;
    }

    public static boolean isPeriodValid(String periodo){
        if ( periodo != null && listPeriodo.contains(periodo)){
            return true;
        }
        else
            return false;
    }
}
