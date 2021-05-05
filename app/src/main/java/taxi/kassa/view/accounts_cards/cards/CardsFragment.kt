package taxi.kassa.view.accounts_cards.cards

import android.content.pm.PackageManager.PERMISSION_GRANTED
import android.os.Bundle
import android.view.View
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.NavHostFragment.findNavController
import kotlinx.android.synthetic.main.fragment_cards.*
import org.koin.android.ext.android.inject
import taxi.kassa.R
import taxi.kassa.util.*
import taxi.kassa.util.Constants.NOT_FROM_PUSH

class CardsFragment : Fragment(R.layout.fragment_cards) {

    private val viewModel by inject<CardsViewModel>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        back_arrow.setOnClickListener { findNavController(this).popBackStack() }

        notification_image.setOnClickListener {
            findNavController(this).navigate(
                R.id.action_cardsFragment_to_notificationsFragment,
                Bundle().apply { putString(NOT_FROM_PUSH, NOT_FROM_PUSH) }
            )
        }

        notification_count.setOnClickListener {
            findNavController(this).navigate(
                R.id.action_cardsFragment_to_notificationsFragment,
                Bundle().apply { putString(NOT_FROM_PUSH, NOT_FROM_PUSH) }
            )
        }

        call_button.setOnClickListener { activity?.makeCall(this) }

        with(viewModel) {
            getCardsData()

            isProgressVisible.observe(viewLifecycleOwner) { progress_bar.isVisible = it }

            error.observe(viewLifecycleOwner) {
                context?.showToast(it)
                refresh_layout.isRefreshing = false
            }

            cards.observe(viewLifecycleOwner) {
                cards_recycler.setHasFixedSize(true)
                cards_recycler.adapter = CardsAdapter(it) { _, _ ->
                }
                refresh_layout.isRefreshing = false
            }

            notifications.observe(viewLifecycleOwner) {
                context?.checkSizes(it, notification_count, notification_image)
            }

            refresh_layout.setOnRefreshListener { getCardsData() }
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        if (grantResults.isNotEmpty() && grantResults[0] == PERMISSION_GRANTED) {
            activity?.makeCall(this)
        }
    }
}