package taxi.kassa.view.notifications

import android.Manifest
import android.content.ActivityNotFoundException
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.NavHostFragment.findNavController
import kotlinx.android.synthetic.main.fragment_notifications.*
import org.koin.androidx.viewmodel.ext.android.getSharedViewModel
import org.koin.core.parameter.parametersOf
import taxi.kassa.R
import taxi.kassa.model.Notification
import taxi.kassa.util.*
import taxi.kassa.util.Constants.NOTIFICATIONS
import taxi.kassa.util.Constants.NOT_FROM_PUSH
import taxi.kassa.util.Constants.PUSH_COUNTER
import taxi.kassa.util.Constants.SUPPORT_PHONE_NUMBER

class NotificationsFragment : Fragment() {

    private lateinit var viewModel: NotificationsViewModel
    private lateinit var notifications: ArrayList<Notification>
    private lateinit var manager: PreferenceManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = getSharedViewModel { parametersOf() }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? = inflater.inflate(R.layout.fragment_notifications, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val callback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                back()
            }
        }
        activity?.onBackPressedDispatcher?.addCallback(viewLifecycleOwner, callback)

        viewModel.notifications.observe(viewLifecycleOwner) {
            notifications = it as ArrayList<Notification>

            notifications_recycler.adapter = NotificationsAdapter(it) { notification ->
                viewModel.setSelectedNotification(notification)
                findNavController(this).navigate(R.id.action_notificationsFragment_to_notificationFragment)
            }

            empty_tv.visibility = if (it.isNullOrEmpty()) VISIBLE else GONE

            manager = PreferenceManager(requireContext())

            val oldPushesSize = manager.getInt(PUSH_COUNTER)
            oldPushesSize?.let { oldSize ->
                if (it.size > oldSize) {
                    notification_count.text = (it.size - oldSize).toString()
                    notification_count.visible()
                    notification_image.invisible()
                } else {
                    notification_count.invisible()
                    notification_image.visible()
                }
            }

            manager.saveInt(PUSH_COUNTER, it.size)
        }

        phone_image.setOnClickListener { makeCall() }

        close_tv.setOnClickListener { activity?.onBackPressed() }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            makeCall()
        }
    }

    override fun onDetach() {
        super.onDetach()
        if (::notifications.isInitialized) {
            notifications.map {
                if (it.isNew) it.isNew = false
            }
            manager.saveNotifications(NOTIFICATIONS, notifications)
        }
    }

    private fun makeCall() {
        val callIntent = Intent(Intent.ACTION_CALL)
        callIntent.data = Uri.parse("tel:${SUPPORT_PHONE_NUMBER}")

        if (ActivityCompat.checkSelfPermission(requireContext(), Manifest.permission.CALL_PHONE)
            != PackageManager.PERMISSION_GRANTED
        ) {
            requestPermissions(arrayOf(Manifest.permission.CALL_PHONE), 10)
            return
        } else {
            try {
                startActivity(callIntent)
            } catch (ex: ActivityNotFoundException) {
                requireActivity().shortToast(getString(R.string.not_find_call_app))
            }
        }
    }

    private fun back() {
        // check if the app is running by clicking on the notification
        when (arguments?.get(NOT_FROM_PUSH)) {
            NOT_FROM_PUSH -> findNavController(this).popBackStack()
            else -> findNavController(this).navigate(R.id.action_notificationsFragment_to_profileFragment)
        }
    }
}