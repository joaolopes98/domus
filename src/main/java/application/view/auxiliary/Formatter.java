package application.view.auxiliary;

import java.text.DecimalFormat;
import java.text.Normalizer;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public abstract class Formatter {

    public static String formatDateHour(Date date){
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy 'às' HH:mm");
        return sdf.format(date);
    }

    public static String formatDate(Date date){
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        return sdf.format(date);
    }

    public static Date toDate(String date){
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
            sdf.setLenient(false);
            return sdf.parse(date);
        } catch (ParseException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static double unmaskMoney(String money){
        String text = money.replace("R$", "")
                .replace(".","")
                .replace(",",".");

        return Double.parseDouble(text);
    }

    public static int unmaskInteger(String integer){
        integer = integer.replaceAll("//D", "");
        return Integer.parseInt(integer);
    }

    public static String formatMoney(double value){
        DecimalFormat decimalFormat = new DecimalFormat("'R$' #,##0.00");
        return decimalFormat.format(value).replace("-", "");
    }

    public static String formatStringCode(int code){
        DecimalFormat decimalFormat = new DecimalFormat("000");
        return decimalFormat.format(code);
    }

    public static String removeAccentuation(String str) {
        return Normalizer.normalize(str, Normalizer.Form.NFD).replaceAll("[^\\p{ASCII}]", "");
    }

    public static String unmaskOnlyNumber(String numbers){
        return numbers.replaceAll("\\D", "");
    }

    public static String formatDocument(String document){
        document = unmaskOnlyNumber(document);
        return document.replaceAll("(\\d{3})(\\d{3})(\\d{3})(\\d+)", "$1.$2.$3-$4");
    }

    public static String formatPhone(String phone){
        if(phone.length() == 11){
            return "(" + phone.substring(0,2) + ")" + phone.substring(2,7) + "-" + phone.substring(7);
        } else {
            return "(" + phone.substring(0,2) + ")" + phone.substring(2,6) + "-" + phone.substring(6);
        }
    }
}
