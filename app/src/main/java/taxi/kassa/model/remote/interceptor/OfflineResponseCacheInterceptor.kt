package taxi.kassa.model.remote.interceptor

import android.content.Context
import okhttp3.Interceptor
import okhttp3.Response
import taxi.kassa.util.isNetworkAvailable
import java.io.IOException

class OfflineResponseCacheInterceptor(private val context: Context) : Interceptor {

    @Throws(IOException::class)
    override fun intercept(chain: Interceptor.Chain): Response {
        var request = chain.request()

        if (!context.isNetworkAvailable()) {
            request = request.newBuilder()
                .removeHeader("Pragma")
                .header("Cache-Control", "public, only-if-cached, max-stale=" + 2419200)
                .build()
        }

        return chain.proceed(request)
    }

}
