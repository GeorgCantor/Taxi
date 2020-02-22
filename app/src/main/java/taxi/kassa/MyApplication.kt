package taxi.kassa

import android.app.Application
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin
import taxi.kassa.di.appModule
import taxi.kassa.di.repositoryModule
import taxi.kassa.di.viewModelModule

class MyApplication : Application() {

    override fun onCreate() {
        super.onCreate()

        startKoin {
            androidContext(this@MyApplication)
            modules(arrayListOf(appModule, viewModelModule, repositoryModule))
        }
    }
}