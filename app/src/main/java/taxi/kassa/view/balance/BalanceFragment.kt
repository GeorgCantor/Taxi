package taxi.kassa.view.balance

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.View.VISIBLE
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.fragment.NavHostFragment.findNavController
import kotlinx.android.synthetic.main.fragment_balance.*
import org.koin.androidx.viewmodel.ext.android.getViewModel
import org.koin.core.parameter.parametersOf
import taxi.kassa.R
import taxi.kassa.util.*
import taxi.kassa.util.Constants.CITYMOBIL
import taxi.kassa.util.Constants.GETT
import taxi.kassa.util.Constants.PUSH_COUNTER
import taxi.kassa.util.Constants.TAXI
import taxi.kassa.util.Constants.YANDEX
import java.text.NumberFormat
import java.util.*

class BalanceFragment : Fragment() {

    private lateinit var viewModel: BalanceViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = getViewModel { parametersOf() }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? = inflater.inflate(R.layout.fragment_balance, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.progressIsVisible.observe(viewLifecycleOwner, Observer { visible ->
            progress_bar.visibility = if (visible) VISIBLE else View.GONE
        })

        viewModel.error.observe(viewLifecycleOwner, Observer {
            context?.longToast(it)
        })

        viewModel.responseOwner.observe(viewLifecycleOwner, Observer { response ->
            response?.let {
                val format = NumberFormat.getInstance(Locale("ru", "RU"))
                balance_tv.text = getString(
                    R.string.balance_format,
                    format.format(it.balanceTotal.toDouble())
                ).replace(',', '.')

                yandex_amount.text = getString(R.string.withdraw_format, it.balanceYandex)
                citymobil_amount.text = getString(R.string.withdraw_format, it.balanceCity)
                gett_amount.text = getString(R.string.withdraw_format, it.balanceGett)
                rosneft_amount.text = getString(R.string.withdraw_format, it.balanceFuel)

                yandex_amount.setColor(it.balanceYandex, R.color.balance_green, R.color.balance_red)
                withdraw_yandex_tv.setColor(it.balanceYandex, R.color.gray_intro_text, R.color.colorAccent)
                citymobil_amount.setColor(it.balanceCity, R.color.balance_green, R.color.balance_red)
                withdraw_citymobil_tv.setColor(it.balanceCity, R.color.gray_intro_text, R.color.colorAccent)
                gett_amount.setColor(it.balanceGett, R.color.balance_green, R.color.balance_red)
                withdraw_gett_tv.setColor(it.balanceGett, R.color.gray_intro_text, R.color.colorAccent)
                rosneft_amount.setColor(it.balanceFuel, R.color.balance_green, R.color.balance_red)

                withdraw_yandex_tv.isEnabled = it.balanceYandex.toFloat() > 0.0F
                withdraw_citymobil_tv.isEnabled = it.balanceCity.toFloat() > 0.0F
                withdraw_gett_tv.isEnabled = it.balanceGett.toFloat() > 0.0F
            }
        })

        viewModel.notifications.observe(viewLifecycleOwner, Observer {
            val oldPushesSize = PreferenceManager(requireContext()).getInt(PUSH_COUNTER)
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
        })

        val bundle = Bundle()

        withdraw_yandex_tv.setOnClickListener {
            bundle.putString(TAXI, YANDEX)
            findNavController(this).navigate(R.id.action_balanceFragment_to_withdrawCreateFragment, bundle)
        }

        withdraw_citymobil_tv.setOnClickListener {
            bundle.putString(TAXI, CITYMOBIL)
            findNavController(this).navigate(R.id.action_balanceFragment_to_withdrawCreateFragment, bundle)
        }

        withdraw_gett_tv.setOnClickListener {
            bundle.putString(TAXI, GETT)
            findNavController(this).navigate(R.id.action_balanceFragment_to_withdrawCreateFragment, bundle)
        }

        replenish_rosneft_tv.setOnClickListener {
            findNavController(this).navigate(R.id.action_balanceFragment_to_fuelReplenishFragment)
        }

        notification_image.setOnClickListener {
            findNavController(this).navigate(R.id.action_balanceFragment_to_notificationsFragment)
        }

        notification_count.setOnClickListener {
            findNavController(this).navigate(R.id.action_balanceFragment_to_notificationsFragment)
        }

        back_arrow.setOnClickListener { activity?.onBackPressed() }
    }
}