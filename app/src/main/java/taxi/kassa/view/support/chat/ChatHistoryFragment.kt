package taxi.kassa.view.support.chat

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import kotlinx.android.synthetic.main.fragment_chat_history.*
import org.koin.androidx.viewmodel.ext.android.getViewModel
import org.koin.core.parameter.parametersOf
import taxi.kassa.R
import taxi.kassa.util.Constants.MESSAGES_COUNTER
import taxi.kassa.util.PreferenceManager
import taxi.kassa.util.observe

class ChatHistoryFragment : Fragment() {

    private lateinit var viewModel: ChatHistoryViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = getViewModel { parametersOf() }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? = inflater.inflate(R.layout.fragment_chat_history, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.messages.observe(viewLifecycleOwner) {
            chat_recycler.adapter = ChatHistoryAdapter(it)
        }

        viewModel.incomingMessages.observe(viewLifecycleOwner) {
            PreferenceManager(requireContext()).saveInt(MESSAGES_COUNTER, it.size)
        }

        back_arrow.setOnClickListener { activity?.onBackPressed() }
    }
}