package taxi.kassa.view.notifications

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import kotlinx.android.synthetic.main.fragment_notification.*
import org.koin.androidx.viewmodel.ext.android.getSharedViewModel
import taxi.kassa.R

class NotificationFragment : Fragment(R.layout.fragment_notification) {

    private lateinit var viewModel: NotificationsViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = getSharedViewModel()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.selectedNotification.observe(viewLifecycleOwner) {
            title_tv.text = it.title
            message_tv.text = it.message
        }

        back_arrow.setOnClickListener { activity?.onBackPressed() }
        ok_button.setOnClickListener { activity?.onBackPressed() }
    }
}