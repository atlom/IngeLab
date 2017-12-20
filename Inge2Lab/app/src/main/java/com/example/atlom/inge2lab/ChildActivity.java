package com.example.atlom.inge2lab;

import android.app.Notification;
import android.app.NotificationManager;
import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.app.NotificationCompat;
import android.support.v7.widget.Toolbar;
import android.text.format.DateFormat;

import com.example.atlom.inge2lab.hijo.HijoFragment;
import com.example.atlom.inge2lab.registro.RegistroVacuna;
import com.example.atlom.inge2lab.service.Service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import static java.lang.Boolean.TRUE;


public class ChildActivity extends AppCompatActivity {

    protected int id_usuario;
    public static final int NOTIF_ALERTA_ID = 55;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_child);
        /**Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);**/

        id_usuario = getIntent().getExtras().getInt("id_usuario");
        new NotificacionTask().execute(id_usuario);
        Bundle bundle = new Bundle();
        bundle.putInt("id_usuario",id_usuario);

        //HijoFragment
        HijoFragment fragment = (HijoFragment)
                getSupportFragmentManager().findFragmentById(R.id.hijo_container);
        if(fragment == null){
            fragment = HijoFragment.newInstance();
            fragment.setArguments(bundle);
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.hijo_container,fragment)
                    .commit();
        }

    }


    private class NotificacionTask extends AsyncTask <Integer,Void,Void>{

        Service sc = new Service();
        int id;


        private Context mContext;
        private int NOTIFICATION_ID = 1;
        private Notification mNotification;
        private NotificationManager mNotificationManager;

        @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
        @Override
        protected Void doInBackground(Integer... integers) {

            Date date = new Date();
            String hoy = (String) DateFormat.format("yyyy-MM-dd", date.getTime());
            ArrayList<RegistroVacuna> lista;
            id = integers[0];
            lista = sc.getFecha(id);
            String fechavacuna;
            String msg;

            for (int i=0;i<lista.size();i++){
                fechavacuna = lista.get(i).getFecha();
                long res = diferenciaFechas(hoy,fechavacuna);
                if (res<=2 && res>=0){
                    if (res == 0){
                       msg = "Hay Vacuna Pendiente hoy";
                    }else{
                        msg = "Faltan: "+res+" dia/s para la proxima vacuna";
                    }
                    crear_notificacion(msg);
                    return null;
                }else if(res < 0){
                    res = res * -1;
                    msg ="Vacunas pendientes hace "+res+" dias.";
                    crear_notificacion(msg);
                    return null;
                }
            }

            return null;
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
            long diferenciaMilisegundos = milisegundos2 - milisegundos1;

            // Despues va a depender en que formato queremos  mostrar esa
            // diferencia, minutos, segundo horas, dias, etc, aca van algunos
            // ejemplos de conversion

            // calcular la diferencia en dias
            long diffdias = diferenciaMilisegundos / (24 * 60 * 60 * 1000);

            // devolvemos el resultado en un string
            return diffdias;
        }

        @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
        private void crear_notificacion(String msg){
            Uri sonido = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
            NotificationCompat.Builder mBuilder =
                    (NotificationCompat.Builder) new NotificationCompat.Builder(getApplicationContext())
                            .setSmallIcon(R.drawable.launcher_icon)
                            .setLargeIcon((((BitmapDrawable) getResources().getDrawable(R.drawable.launcher_icon)).getBitmap()))
                            .setContentTitle("Agenda Pediatrica")
                            .setContentText(msg)
                            .setSound(sonido)
                            .setAutoCancel(TRUE);

            NotificationManager mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
            mNotificationManager.notify(NOTIF_ALERTA_ID, mBuilder.build());
        }
    }
}
