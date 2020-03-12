package taxi.kassa.view.withdraws

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.View.*
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.fragment.NavHostFragment.findNavController
import kotlinx.android.synthetic.main.empty_withdraws_screen.*
import kotlinx.android.synthetic.main.fragment_withdraws.*
import kotlinx.android.synthetic.main.fragment_withdraws.back_arrow
import kotlinx.android.synthetic.main.fragment_withdraws.notification_image
import org.koin.androidx.viewmodel.ext.android.getViewModel
import org.koin.core.parameter.parametersOf
import taxi.kassa.R
import taxi.kassa.util.Constants.WITHDRAW
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
            progress_bar.visibility = if (visible) VISIBLE else GONE
        })

        viewModel.error.observe(viewLifecycleOwner, Observer {
            activity?.shortToast(it)
        })

        viewModel.withdraws.observe(viewLifecycleOwner, Observer {
            empty_withdraws.visibility = if (it.count ?: 0 > 0) GONE else VISIBLE

            withdraws_recycler.adapter = WithdrawsAdapter(it.info?: mutableListOf()) { withdraw ->
                val bundle = Bundle()
                bundle.putParcelable(WITHDRAW, withdraw)
                findNavController(this).navigate(R.id.action_withdrawsFragment_to_withdrawFragment, bundle)
            }
        })

        viewModel.notifications.observe(viewLifecycleOwner, Observer {
            when (it.size) {
                0 -> {
                    notification_count.visibility = INVISIBLE
                    notification_image.visibility = VISIBLE
                }
                else -> {
                    notification_count.text = it.size.toString()
                    notification_count.visibility = VISIBLE
                    notification_image.visibility = INVISIBLE
                }
            }
        })

        back_arrow.setOnClickListener { activity?.onBackPressed() }

        back_arrow_empty.setOnClickListener { activity?.onBackPressed() }

        back_button.setOnClickListener { activity?.onBackPressed() }

        notification_image.setOnClickListener {
            findNavController(this).navigate(R.id.action_withdrawsFragment_to_notificationsFragment)
        }

        notification_count.setOnClickListener {
            findNavController(this).navigate(R.id.action_withdrawsFragment_to_notificationsFragment)
        }

        add_account_button.setOnClickListener {
            findNavController(this).navigate(R.id.action_withdrawsFragment_to_accountsFragment)
        }
    }
}