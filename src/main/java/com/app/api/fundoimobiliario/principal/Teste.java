package com.app.api.fundoimobiliario.principal;

import com.app.commons.utils.Utils;

import java.time.LocalDate;

public class Teste {

    public static void main(String agrs[]){
        try {
            String line = "[2022-04-01, 0.5]";
            line = line.replace("[", "");
            line = line.replace("]", "");
            String[] arr = line.split(",");
            LocalDate dataDividendo = Utils.converteStringToLocalDateTime3(arr[0]);
            Double dividendo = Double.parseDouble(arr[1]);

            System.out.println("terminou ....");
        }
        catch (Exception e){
            System.out.println("Mensagem error: " + e.getMessage());
        }
    }
}
