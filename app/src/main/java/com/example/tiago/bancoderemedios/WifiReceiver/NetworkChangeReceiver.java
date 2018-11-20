package com.example.tiago.bancoderemedios.WifiReceiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

public class NetworkChangeReceiver extends BroadcastReceiver {

    public static final String NETWORK_CHANGE_ACTION = "com.androiderstack.broadcastreceiverdemo.NetworkChangeReceiver";

    @Override
    public void onReceive(final Context context, final Intent intent) {

        if (isOnline(context))
        {
            sendInternalBroadcast(context, "Connectedo à Internet");
        }
        else
        {
            sendInternalBroadcast(context, "Sem Conexão com à Internet");
        }
    }

    /**
     * This method is responsible to send status by internal broadcast
     *
     * @param context
     * @param status
     * */
    private void sendInternalBroadcast(Context context, String status)
    {
        try
        {
            Intent intent = new Intent();
            intent.putExtra("status", status);
            intent.setAction(NETWORK_CHANGE_ACTION);
            context.sendBroadcast(intent);
        }
        catch (Exception ex)
        {
            ex.printStackTrace();
        }
    }

    /**
     * Check if network available or not
     *
     * @param context
     * */
    public boolean isOnline(Context context)
    {
        boolean isOnline = false;
        try
        {
            ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo netInfo = cm.getActiveNetworkInfo();
            //should check null because in airplane mode it will be null
            isOnline = (netInfo != null && netInfo.isConnected());
        }
        catch (Exception ex)
        {
            ex.printStackTrace();
        }
        return isOnline;
    }
}
