package taxi.kassa.view.support.chat

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import kotlinx.android.synthetic.main.fragment_chat_history.*
import org.koin.androidx.viewmodel.ext.android.getViewModel
import org.koin.core.parameter.parametersOf
import taxi.kassa.R
import taxi.kassa.model.responses.Message
import taxi.kassa.util.*
import taxi.kassa.util.Constants.MESSAGES_COUNTER

class ChatHistoryFragment : Fragment() {

    private lateinit var viewModel: ChatHistoryViewModel
    private lateinit var adapter: ChatHistoryAdapter
    private var nextOffset = ""
    private var firstLoad = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = getViewModel { parametersOf() }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? = container?.inflate(R.layout.fragment_chat_history)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        with(viewModel) {
            getMessages("")

            error.observe(viewLifecycleOwner) { context?.longToast(it) }

            messages.observe(viewLifecycleOwner) {
                nextOffset = it.nextOffset ?: ""

                when (firstLoad) {
                    true -> {
                        adapter = ChatHistoryAdapter(it.messages as MutableList<Message>)
                        chat_recycler.adapter = adapter

                        firstLoad = false
                    }
                    false -> adapter.updateList(it.messages as MutableList<Message>)
                }

                empty_tv.visibility = if (adapter.itemCount == 0) VISIBLE else GONE
            }

            incomingMessages.observe(viewLifecycleOwner) {
                PreferenceManager(requireContext()).saveInt(MESSAGES_COUNTER, it.size)
            }

            val scrollListener = object : EndlessScrollListener() {
                override fun onLoadMore(page: Int, totalItemsCount: Int) {
                    getMessages(nextOffset)
                }
            }

            chat_recycler.addOnScrollListener(scrollListener)
        }

        back_arrow.setOnClickListener { activity?.onBackPressed() }
    }
}