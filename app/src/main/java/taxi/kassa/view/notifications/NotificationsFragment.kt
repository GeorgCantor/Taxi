package taxi.kassa.view.notifications

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import kotlinx.android.synthetic.main.fragment_notifications.*
import org.koin.androidx.viewmodel.ext.android.getViewModel
import org.koin.core.parameter.parametersOf
import taxi.kassa.R

class NotificationsFragment : Fragment() {

    private lateinit var viewModel: NotificationsViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = getViewModel { parametersOf() }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? = inflater.inflate(R.layout.fragment_notifications, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.getNotifications()

        viewModel.notifications.observe(viewLifecycleOwner, Observer {
            notifications_recycler.adapter = NotificationsAdapter(it)
        })

        close_tv.setOnClickListener { activity?.onBackPressed() }
    }
}