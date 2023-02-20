package com.app.commons.utils;

import java.io.File;
import java.io.IOException;
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
import java.util.zip.ZipEntry;

public class Utils {

    private static final String FORMAT_DATA = "dd/MM/yyyy";

    private static final String FORMAT_DATA_V2 = "yyyy-MM-dd";

    private static final String FORMAT_DATA_V3 = "yyyy-MMdd";

    private static List<String> listPeriodo = Arrays.asList("diario", "semanal", "mensal");

    private static final String IGNORED_LINE = ",Date,Open,High,Low,Close,Adj Close,Volume";

    private static final String IGNORED_LINE3 = "Date,Open,High,Low,Close,Adj Close,Volume";

    private static final String IGNORED_LINE2 = "Date,Open,High,Low,Close,Adj Close,Volume,Dividends,Stock Splits";

    private static final String IGNORED_LINE4 = ",sigla,dividend_yield";

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

    public static String converteLocalDateToString(LocalDate dataHora) {

        try {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
            return dataHora.format(formatter);
        }
        catch(Exception e) {
            return "";
        }
    }

    public static String converteLocalDateToString2(LocalDate dataHora) {

        try {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy/MM");
            return dataHora.format(formatter);
        }
        catch(Exception e) {
            return "";
        }
    }

    public static double converterStringToDoubleDoisDecimais(String valor) {
        if ( valor != null && !valor.equals("")){
            return Utils.converterDoubleDoisDecimais(Double.parseDouble(valor));
        }
        else
            return 0d;
    }

    public static long converterStringToLong(String valor) {
        if ( valor != null && !valor.equals("") && !valor.replaceAll(".0", "").equals("") ){
            return Long.parseLong(valor.replaceAll(".0", ""));
        }
        else
            return 0l;
    }

    public static double converterDoubleDoisDecimais(double precoDouble) {
        DecimalFormat fmt = new DecimalFormat("0.00");
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


    public static boolean isPeriodValid(String periodo){
        if ( periodo != null && listPeriodo.contains(periodo)){
            return true;
        }
        else
            return false;
    }

    public static boolean isLineIgnored(String line){
        if ( line != null && !line.contains(IGNORED_LINE) && !line.contains(IGNORED_LINE2)  && !line.contains(IGNORED_LINE3)  && !line.contains(IGNORED_LINE4) ){
            return true;
        }
        else {
            return false;
        }
    }




    public static File newFile(File destinationDir, ZipEntry zipEntry) throws IOException {
        File destFile = new File(destinationDir, zipEntry.getName());

        String destDirPath = destinationDir.getCanonicalPath();
        String destFilePath = destFile.getCanonicalPath();

        if (!destFilePath.startsWith(destDirPath + File.separator)) {
            throw new IOException("Entry is outside of the target dir: " + zipEntry.getName());
        }

        return destFile;
    }

    public static String converterDoubleDoisDecimaisString(double preco) {
        try{
            Locale lo = new Locale("pt", "BR");
            NumberFormat cf = NumberFormat.getCurrencyInstance(lo);
            return  cf.format(preco);
        }
        catch (Exception e){
            return "";
        }
    }

    public static String converteDoubleToStringValorAbsoluto(double valor){
        try{
            Integer IntValue = (int) valor;
            return IntValue.toString();
        }
        catch (Exception e){
            return "";
        }
    }

    public static double converterDoubleQuatroDecimais(double precoDouble) {
        try{
            DecimalFormat fmt = new DecimalFormat("0.0000");
            String string = fmt.format(precoDouble);
            String[] part = string.split("[,]");
            String string2 = part[0]+"."+part[1];
            double preco = Double.parseDouble(string2);
            return preco;
        }
        catch (Exception e){
            System.out.println("Valor error: " +precoDouble);
            System.out.println("Mensagem erro: " + e.getMessage());
            return 0d;
        }
    }

    public static String converterDoubleQuatroDecimaisString(double precoDouble) {
        try{
            DecimalFormat fmt = new DecimalFormat("0.0000");
            String string = fmt.format(precoDouble);
            return string;
        }
        catch (Exception e){
            return "";
        }
    }

    public static String converterDoubleDoisDecimaisStringSemMoeda(double precoDouble) {
        try{
            DecimalFormat fmt = new DecimalFormat("0,00");
            String string = fmt.format(precoDouble);
            return string;
        }
        catch (Exception e){
            return "";
        }
    }

    public static boolean isAnoMesValid(String anoMes) {
        try {
             DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM");
             formatter.parse(anoMes);
             return true;
        }
        catch(Exception e) {
            return false;
        }
    }

    public static String getAnosMesLocalDate(LocalDate data) {
        return String.valueOf(data.getYear()) + "/" +  padLeftZeros(String.valueOf(data.getMonthValue()),2);
    }


    public static String padLeftZeros(String inputString, int length) {
        if (inputString.length() >= length) {
            return inputString;
        }
        StringBuilder sb = new StringBuilder();
        while (sb.length() < length - inputString.length()) {
            sb.append('0');
        }
        sb.append(inputString);

        return sb.toString();
    }
}
