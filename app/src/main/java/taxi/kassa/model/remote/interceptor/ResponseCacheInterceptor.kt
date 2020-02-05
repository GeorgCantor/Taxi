package taxi.kassa.model.remote.interceptor

import okhttp3.Interceptor
import okhttp3.Response
import java.io.IOException

class ResponseCacheInterceptor : Interceptor {

    @Throws(IOException::class)
    override fun intercept(chain: Interceptor.Chain): Response {
        val originalResponse = chain.proceed(chain.request())

        return originalResponse.newBuilder()
            .removeHeader("Pragma")
            .header("Cache-Control", "public, max-age=" + 60)
            .build()
    }
}
