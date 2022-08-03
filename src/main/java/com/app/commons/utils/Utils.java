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

    private static List<String> listPeriodo = Arrays.asList("diario", "semanal", "mensal");

    private static final String IGNORED_LINE = ",Date,Open,High,Low,Close,Adj Close,Volume";

    private static final String IGNORED_LINE3 = "Date,Open,High,Low,Close,Adj Close,Volume";

    private static final String IGNORED_LINE2 = "Date,Open,High,Low,Close,Adj Close,Volume,Dividends,Stock Splits";

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
        if ( line != null && !line.contains(IGNORED_LINE) && !line.contains(IGNORED_LINE2)  && !line.contains(IGNORED_LINE3)   ){
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
}
