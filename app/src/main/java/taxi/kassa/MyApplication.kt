package taxi.kassa

import android.app.Application
import android.content.Context
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin
import taxi.kassa.di.apiModule
import taxi.kassa.di.preferenceModule
import taxi.kassa.di.repositoryModule
import taxi.kassa.di.viewModelModule

class MyApplication : Application() {

    companion object {
        private var instance: MyApplication? = null

        fun appContext(): Context? = instance?.applicationContext
    }

    override fun onCreate() {
        super.onCreate()
        instance = this

        startKoin {
            androidContext(this@MyApplication)
            modules(listOf(apiModule, repositoryModule, preferenceModule, viewModelModule))
        }
    }
}