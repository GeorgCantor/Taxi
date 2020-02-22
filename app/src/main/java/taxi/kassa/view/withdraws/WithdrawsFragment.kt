package taxi.kassa.view.withdraws

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.fragment.NavHostFragment.findNavController
import kotlinx.android.synthetic.main.fragment_withdraws.*
import org.koin.androidx.viewmodel.ext.android.getViewModel
import org.koin.core.parameter.parametersOf
import taxi.kassa.R
import taxi.kassa.util.Constants.WITHDRAWAL
import taxi.kassa.util.shortToast

class WithdrawsFragment : Fragment() {

    private lateinit var viewModel: WithdrawsViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = getViewModel { parametersOf() }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? = inflater.inflate(R.layout.fragment_withdraws, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.getWithdraws()

        viewModel.progressIsVisible.observe(viewLifecycleOwner, Observer { visible ->
            progress_bar.visibility = if (visible) View.VISIBLE else View.GONE
        })

        viewModel.error.observe(viewLifecycleOwner, Observer {
            activity?.shortToast(it)
        })

        viewModel.withdraws.observe(viewLifecycleOwner, Observer {
            withdraws_recycler.adapter = WithdrawsAdapter(it.info) { withdraw ->
                val bundle = Bundle()
                bundle.putParcelable(WITHDRAWAL, withdraw)
                findNavController(this).navigate(R.id.action_withdrawsFragment_to_withdrawFragment, bundle)
            }
        })

        back_arrow.setOnClickListener {
            activity?.onBackPressed()
        }
    }
}