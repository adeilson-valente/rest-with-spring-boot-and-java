package br.com.demo.converters;

public class NumberConverter {
    public static boolean isNumeric(String number){
        if(number == null){
            return false;
        }

        String num =number.replaceAll(",", ".");

        return num.matches("[-+]?[0-9]*\\.?[0-9]+");
    }

    public static double convertToDouble(String number){
        if(number == null){
            return 0D;
        }

        String num =number.replaceAll(",", ".");

        if(isNumeric(num)){
            return Double.parseDouble(num);
        }
        return 0D;
    }
}
