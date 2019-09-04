package taxi.kassa.api;

import com.google.gson.GsonBuilder;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ApiManager {

    public static String access_token = "";
    private static final String API_VERSION = "1";
    private static final String BASE_URL = "https://kassa.taxi/api/";

    public static API getHttpClient() {

        Interceptor interceptor = new Interceptor() {
            @Override
            public Response intercept(Chain chain) throws IOException {
                Request request = chain.request().newBuilder()
                        .addHeader("Content-Type", "application/json")
                        .addHeader("X-Requested-With", "XMLHttpRequest")
                        .addHeader("Accept", "application/json")
                        .addHeader("v", API_VERSION)
                        .addHeader("token", access_token)
                        .build();
                return chain.proceed(request);
            }
        };

        OkHttpClient client = new OkHttpClient
                .Builder()
                .readTimeout(60, TimeUnit.SECONDS)
                .connectTimeout(60, TimeUnit.SECONDS)
                .addInterceptor(interceptor)
                .build();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .client(client)
                .build();

        return retrofit.create(API.class);
    }

}
