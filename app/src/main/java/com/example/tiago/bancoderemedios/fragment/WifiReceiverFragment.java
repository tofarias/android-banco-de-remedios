package com.example.tiago.bancoderemedios.fragment;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.tiago.bancoderemedios.R;
import com.example.tiago.bancoderemedios.WifiReceiver.NetworkChangeReceiver;

public class WifiReceiverFragment extends Fragment {

    TextView statusTv;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_wifi_receiver, container, false);
    }


    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getActivity().setTitle("Check Wifi");

        statusTv = (TextView) getActivity().findViewById(R.id.activityMain_status);

        registerReceiver();
    }

    /**
     * This method is responsible to register receiver with NETWORK_CHANGE_ACTION.
     * */
    private void registerReceiver()
    {
        try
        {
            IntentFilter intentFilter = new IntentFilter();
            intentFilter.addAction(NetworkChangeReceiver.NETWORK_CHANGE_ACTION);
            getActivity().registerReceiver( internalNetworkChangeReceiver, intentFilter);
        }
        catch (Exception ex)
        {
            ex.printStackTrace();
        }
    }

    @Override
    public void onDestroyView() {

        try
        {
            // Make sure to unregister internal receiver in onDestroy().
            getActivity().unregisterReceiver(internalNetworkChangeReceiver);
        }
        catch (Exception ex)
        {
            ex.printStackTrace();
        }

        super.onDestroyView();
    }

    /**
     * This is internal BroadcastReceiver which get status from external receiver(NetworkChangeReceiver)
     * */
    InternalNetworkChangeReceiver internalNetworkChangeReceiver = new InternalNetworkChangeReceiver();
    class InternalNetworkChangeReceiver extends BroadcastReceiver
    {
        @Override
        public void onReceive(Context context, Intent intent) {

            if (statusTv != null)
                statusTv.setText(intent.getStringExtra("status"));
        }
    }
}