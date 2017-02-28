package prakhar.com.githubrepo;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

/**
 * Created by Andy on 2/24/2016.
 */
public class NetworkUtils {
    public static boolean isNetworkConnected(Context context) {
        boolean connected;
        try {
            ConnectivityManager connectivityManager = (ConnectivityManager) context
                    .getSystemService(Context.CONNECTIVITY_SERVICE);

            NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
            connected = networkInfo != null && networkInfo.isAvailable() &&
                    networkInfo.isConnected();
        } catch (Exception e) {
            System.out.println("CheckConnectivity Exception: " + e.getMessage());
            connected = false;
        }
        return connected;
    }
}
