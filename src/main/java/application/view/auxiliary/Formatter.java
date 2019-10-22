package application.view.auxiliary;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.DecimalFormat;
import java.text.Normalizer;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.regex.Pattern;

public abstract class Formatter {

    public static String formatDateHour(Date date){
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy 'às' HH:mm");
        return sdf.format(date);
    }

    public static String formatDate(Date date){
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        return sdf.format(date);
    }

    public static String formatHour(Date date){
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
        return sdf.format(date);
    }

    public static Date toDate(String date){
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
            sdf.setLenient(false);
            return resetDate(sdf.parse(date));
        } catch (ParseException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static Date resetDate(Date date){
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        return calendar.getTime();
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
        String nfdNormalizedString = Normalizer.normalize(str, Normalizer.Form.NFD);
        Pattern pattern = Pattern.compile("\\p{InCombiningDiacriticalMarks}+");
        String noAcc = pattern.matcher(nfdNormalizedString).replaceAll("");
        return noAcc.replaceAll("[~^´`]", "")
                .replaceAll("[^0-Z .]", "");
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

    public static String formatSHA512(String sha512){
        try {
            MessageDigest m =  MessageDigest.getInstance("SHA-512");
            m.update(sha512.getBytes(),0,sha512.length());
            sha512 = new BigInteger(1,m.digest()).toString(16);

            if (sha512.length() != 32){
                int zeros = 32 - sha512.length();
                StringBuilder passBuilder = new StringBuilder(sha512);
                for (int i = 0; i < zeros; i++){
                    passBuilder.insert(0, "0");
                }
                sha512 = passBuilder.toString();
            }

        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return sha512;
    }
}
