package com.example.atlom.inge2lab.hijo;

import android.content.Context;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.atlom.inge2lab.R;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;


/**
 * Created by atlom on 29/10/17.
 */

public class HijoAdapterList extends ArrayAdapter<Hijo> {
    public HijoAdapterList(Context context, List<Hijo> hijos){
        super(context,0,hijos);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Obtener inflater.
        LayoutInflater inflater = (LayoutInflater) getContext()
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        // ¿Existe el view actual?
        if (null == convertView) {
            convertView = inflater.inflate(R.layout.list_item_hijo, parent, false);
        }
        ImageView avatar = (ImageView) convertView.findViewById(R.id.iv_avatar);
        TextView nombre = (TextView) convertView.findViewById(R.id.tv_name);
        TextView fecha = (TextView) convertView.findViewById(R.id.tv_fecha);

        Hijo hijo = getItem(position);

        Glide.with(getContext()).load(hijo.getAvatar()).into(avatar);
        nombre.setText(hijo.getNombre());

        Date date = new Date();
        String hoy = (String) DateFormat.format("yyyy-MM-dd", date.getTime());
        long edad = diferenciaFechas(hoy,hijo.getFechaNacimiento());
        fecha.setText("Nacimiento: "+hijo.getFechaNacimiento() + " \nEdad: "+edad+" años");

        return convertView;

    }

    private long diferenciaFechas(String inicio, String llegada){

        Date fechaInicio = null;
        Date fechaLlegada = null;

        // configuramos el formato en el que esta guardada la fecha en
        //  los strings que nos pasan
        SimpleDateFormat formato = new SimpleDateFormat("yyyy-MM-dd");

        try {
            // aca realizamos el parse, para obtener objetos de tipo Date de
            // las Strings
            fechaInicio = formato.parse(inicio);
            fechaLlegada = formato.parse(llegada);

        } catch (ParseException e) {
            // Log.e(TAG, "Funcion diferenciaFechas: Error Parse " + e);
        } catch (Exception e){
            // Log.e(TAG, "Funcion diferenciaFechas: Error " + e);
        }

        // tomamos la instancia del tipo de calendario
        Calendar calendarInicio = Calendar.getInstance();
        Calendar calendarFinal = Calendar.getInstance();

        // Configramos la fecha del calendatio, tomando los valores del date que
        // generamos en el parse
        calendarInicio.setTime(fechaInicio);
        calendarFinal.setTime(fechaLlegada);

        // obtenemos el valor de las fechas en milisegundos
        long milisegundos1 = calendarInicio.getTimeInMillis();
        long milisegundos2 = calendarFinal.getTimeInMillis();

        int i = fechaInicio.getYear()+1900;
        int p = fechaLlegada.getYear()+1900;


        // tomamos la diferencia
        //long diferenciaMilisegundos = milisegundos2 - milisegundos1;

        // Despues va a depender en que formato queremos  mostrar esa
        // diferencia, minutos, segundo horas, dias, etc, aca van algunos
        // ejemplos de conversion

        // calcular la diferencia en dias
        //long diffdias = diferenciaMilisegundos / (24 * 60 * 60 * 1000);

        // devolvemos el resultado en un string
        return i - p;
    }
}
