package com.example.tiago.roupas.fragment;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.NotificationCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.tiago.roupas.R;
import com.example.tiago.roupas.activity.TextoActivity;

public class FragmentNotificacao extends Fragment {

    private Button btnSimples;

    public static final String NOTIFICATION_CHANNEL_ID = "10001";

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_notificacao, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        getActivity().setTitle(R.string.nav_header_notificacao);

        this.btnSimples = (Button) getActivity().findViewById(R.id.btnSimples);
        this.btnSimples.setOnClickListener( btnSimplesOnClickListener );
    }

    private View.OnClickListener btnSimplesOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {

            switch (v.getId()){

                case R.id.btnSimples:

                    criarNotificacaoSimples();

                    break;
            }
        }
    };

    public void criarNotificacaoSimples(){

        int id = 1;
        String titulo = "Novo Medicamento Cadastrado";
        String texto = "Adicionado o medicamento XYZ";
        int icone = android.R.drawable.ic_dialog_info;

        Intent intent = new Intent(getContext(),TextoActivity.class);
        intent.putExtra("txt","Detalhes do medicamento cadastrado");

        PendingIntent pendingIntent = getPendingIntent(id, intent, getContext());

        NotificationCompat.Builder notificacao = new NotificationCompat.Builder(getContext(),getString(R.string.default_notification_channel_id));
        notificacao.setSmallIcon( icone ).setContentTitle(titulo).setContentText(texto);
        notificacao.setContentIntent( pendingIntent );
        notificacao.setAutoCancel(true);

        NotificationManager nm = (NotificationManager) getActivity().getSystemService(Context.NOTIFICATION_SERVICE);

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O)
        {
            int importance = NotificationManager.IMPORTANCE_HIGH;
            NotificationChannel notificationChannel = new NotificationChannel(NOTIFICATION_CHANNEL_ID, "NOTIFICATION_CHANNEL_NAME", importance);
            notificationChannel.enableLights(true);
            notificationChannel.setLightColor(Color.RED);
            notificationChannel.enableVibration(true);
            notificationChannel.setVibrationPattern(new long[]{100, 200, 300, 400, 500, 400, 300, 200, 400});
            notificacao.setChannelId(NOTIFICATION_CHANNEL_ID);
            nm.createNotificationChannel(notificationChannel);
        }


        nm.notify(id, notificacao.build());
    }

    private PendingIntent getPendingIntent(int id, Intent intent, Context context){

        TaskStackBuilder stackBuilder = TaskStackBuilder.create(context);
        stackBuilder.addParentStack(intent.getComponent());
        stackBuilder.addNextIntent(intent);

        return stackBuilder.getPendingIntent(id, PendingIntent.FLAG_UPDATE_CURRENT);
    }
}
