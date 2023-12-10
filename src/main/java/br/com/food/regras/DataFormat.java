package br.com.food.regras;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;

public class DataFormat {

    private DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

    private Date dataInicio;

    private Date dataFim;

    public Date FormatDataInicio(String inicio) {

        try {
            dataInicio = dateFormat.parse(inicio);
        } catch (ParseException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return dataInicio;
    }

    public Date FormatDataFim(String fim) {

        try {
            dataFim = dateFormat.parse(fim);
        } catch (ParseException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return dataFim;
    }

    public Date FormatData(String data,String pattern) {
        DateFormat format = new SimpleDateFormat(pattern);
        Date d = null;
        try {
            d = format.parse(data);
            System.out.println(d);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return d;
    }


    public static Date FormatData(Date data,String pattern) {
        DateFormat format = new SimpleDateFormat(pattern);
        Date d = null;
        try {
            d = format.parse(format.format(data));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return d;
    }

    public static Date getDataByPatternDate(Date data, String pattern) {
        Date retorno = null;
        if(data != null){
            SimpleDateFormat formatter = new SimpleDateFormat(pattern, Locale.getDefault());
            String resposta = formatter.format(data);
            try {
                retorno = formatter.parse(resposta);
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
        return retorno;
    }

    public static String getDataByPattern(Date data, String pattern) {
        String resposta = "";
        String defaultPattern = "dd/MM/yyyy";
        if(pattern != null){
            defaultPattern = pattern;
        }
        if(data != null){
            SimpleDateFormat formatter = new SimpleDateFormat(defaultPattern, Locale.getDefault());
            resposta = formatter.format(data);
        }
        return resposta;
    }

    public static Date convertStringToDate(String date){
        GregorianCalendar calendar = new GregorianCalendar();
        calendar.set(Calendar.YEAR, Integer.parseInt(date.substring(0, 4)));
        calendar.set(Calendar.MONTH, (Integer.parseInt(date.substring(4, 6))-1));
        calendar.set(Calendar.DAY_OF_MONTH, Integer.parseInt(date.substring(6, 8)));
        return calendar.getTime();
    }
}
