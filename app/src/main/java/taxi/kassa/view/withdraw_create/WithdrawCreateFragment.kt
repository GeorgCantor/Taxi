package taxi.kassa.view.withdraw_create

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.View.VISIBLE
import android.view.ViewGroup
import androidx.appcompat.content.res.AppCompatResources.getDrawable
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import kotlinx.android.synthetic.main.fragment_withdraw_create.*
import kotlinx.android.synthetic.main.item_taxi.view.*
import org.koin.androidx.viewmodel.ext.android.getViewModel
import org.koin.core.parameter.parametersOf
import taxi.kassa.R
import taxi.kassa.model.Taxi
import taxi.kassa.util.shortToast

class WithdrawCreateFragment : Fragment() {

    private lateinit var viewModel: WithdrawCreateViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = getViewModel { parametersOf() }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? = inflater.inflate(R.layout.fragment_withdraw_create, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.getUserInfo()

        viewModel.error.observe(viewLifecycleOwner, Observer {
            activity?.shortToast(it)
        })

        viewModel.responseOwner.observe(viewLifecycleOwner, Observer { response ->
            response?.let {
                val taxis = mutableListOf<Taxi>()
                taxis.add(Taxi(R.drawable.ic_yandex, getString(R.string.yandex_title), it.balanceYandex))
                taxis.add(Taxi(R.drawable.ic_gett, getString(R.string.gett_title), it.balanceGett))
                taxis.add(Taxi(R.drawable.ic_citymobil, getString(R.string.citymobil_title), it.balanceCity))
                taxi_recycler.adapter = WithdrawTaxiAdapter(taxis) {
                    it.background = getDrawable(requireContext(), R.drawable.bg_outline_green)
                    it.check_image.visibility = VISIBLE
                }
            }
        })
    }
}