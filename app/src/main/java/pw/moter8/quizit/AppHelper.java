package pw.moter8.quizit;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import com.rengwuxian.materialedittext.MaterialEditText;


public class AppHelper {

    public static boolean isNetworkAvail(Context context) {
        ConnectivityManager manager = (ConnectivityManager)
                context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = manager.getActiveNetworkInfo();

        return (networkInfo != null && networkInfo.isConnected());
    }

}
