package prakhar.com.githubrepo.network;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import prakhar.com.githubrepo.APIInterface;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by lendingkart on 2/21/2017.
 */

public class RetroClient {
    private static RetroClient sInstance;

    public static synchronized RetroClient getInstance() {
        if (sInstance == null) {
            sInstance = new RetroClient();
        }
        return sInstance;
    }

    private RetroClient() {

    }

    public Retrofit getRetrofit() {
        Gson gson = new GsonBuilder()
                .setDateFormat("yyyy-MM-dd'T'HH:mm:ssZ")
                .create();


        final Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(APIInterface.ENDPOINT)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();
        return retrofit;
    }
}
