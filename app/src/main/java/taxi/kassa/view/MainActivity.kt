package taxi.kassa.view

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.fragment.NavHostFragment
import kotlinx.android.synthetic.main.activity_main.*
import taxi.kassa.R
import taxi.kassa.util.Constants.TOKEN
import taxi.kassa.util.PreferenceManager

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val token = PreferenceManager(this).getString(TOKEN) ?: ""

        val navHostFragment = navHostFragment as NavHostFragment
        val inflater = navHostFragment.navController.navInflater
        val graph = inflater.inflate(R.navigation.nav_graph)

        if (token.isEmpty()) {
            graph.startDestination = R.id.introFragment
        } else {
            graph.startDestination = R.id.profileFragment
        }

        navHostFragment.navController.graph = graph
    }
}
