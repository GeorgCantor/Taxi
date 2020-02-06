package taxi.kassa.model.remote

import android.content.Context
import okhttp3.Cache
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Response
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import taxi.kassa.BuildConfig
import taxi.kassa.model.remote.interceptor.OfflineResponseCacheInterceptor
import taxi.kassa.model.remote.interceptor.ResponseCacheInterceptor
import java.io.File
import java.io.IOException
import java.util.concurrent.TimeUnit

object ApiClient {

    private var access_token = ""
    private const val API_VERSION = "1"

    fun create(context: Context): ApiService {

        val loggingInterceptor = HttpLoggingInterceptor()
        loggingInterceptor.level =
            if (BuildConfig.DEBUG) HttpLoggingInterceptor.Level.BODY else HttpLoggingInterceptor.Level.NONE

        val interceptor: Interceptor = object : Interceptor {
            @Throws(IOException::class)
            override fun intercept(chain: Interceptor.Chain): Response {
                val request = chain.request().newBuilder()
                    .addHeader("Content-Type", "application/json")
                    .addHeader("X-Requested-With", "XMLHttpRequest")
                    .addHeader("Accept", "application/json")
                    .addHeader("v", API_VERSION)
                    .addHeader("token", access_token)
                    .build()
                return chain.proceed(request)
            }
        }

        val okHttpClient = OkHttpClient().newBuilder()
            .addNetworkInterceptor(ResponseCacheInterceptor())
            .addInterceptor(OfflineResponseCacheInterceptor(context))
            .addInterceptor(loggingInterceptor)
            .addInterceptor(interceptor)
            .cache(Cache(File(context.cacheDir, "ResponsesCache"), (10 * 1024 * 1024).toLong()))
            .connectTimeout(20, TimeUnit.SECONDS)
            .readTimeout(20, TimeUnit.SECONDS)
            .writeTimeout(20, TimeUnit.SECONDS)
            .build()

        val retrofit = Retrofit.Builder()
            .baseUrl(BuildConfig.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .client(okHttpClient)
            .build()

        return retrofit.create(ApiService::class.java)
    }
}