package me.zeroandone.technology.rushupdelivery.utils;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

public class InternetConnector_Receiver extends BroadcastReceiver {
	public static InternetConnector_Receiver.ConnectivityReceiverListener connectivityReceiverListener;
	int counter=0;

	public InternetConnector_Receiver() {
	}

	@Override
	public void onReceive(Context context, Intent intent) {
		try {

				ConnectivityManager connectivityManager = (ConnectivityManager) context
						.getSystemService(Context.CONNECTIVITY_SERVICE);
				NetworkInfo networkInfo = connectivityManager
						.getActiveNetworkInfo();

			if (networkInfo!= null) {

					// do subroutines here
					if ( networkInfo.isConnected()) {
						connectivityReceiverListener.onNetworkConnectionChanged(true);
					} else if(!networkInfo.isConnected())
						connectivityReceiverListener.onNetworkConnectionChanged(false);


				}

		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	public interface ConnectivityReceiverListener {
		void onNetworkConnectionChanged(boolean isConnected);
	}
}
