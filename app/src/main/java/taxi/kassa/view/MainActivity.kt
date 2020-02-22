package taxi.kassa.view

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.Navigation
import taxi.kassa.R
import taxi.kassa.util.Constants.TOKEN
import taxi.kassa.util.PreferenceManager

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val token = PreferenceManager(this).getString(TOKEN) ?: ""

        if (token.isEmpty()) {
            Navigation.findNavController(this, R.id.navHostFragment).navigate(R.id.introFragment)
        } else {
            Navigation.findNavController(this, R.id.navHostFragment).navigate(R.id.profileFragment)
        }
    }
}
