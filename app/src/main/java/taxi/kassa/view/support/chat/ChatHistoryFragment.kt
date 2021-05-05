package taxi.kassa.view.support.chat

import android.os.Bundle
import android.view.View
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import kotlinx.android.synthetic.main.fragment_chat_history.*
import org.koin.android.ext.android.inject
import taxi.kassa.R
import taxi.kassa.model.responses.Message
import taxi.kassa.util.Constants.MESSAGES_COUNTER
import taxi.kassa.util.EndlessScrollListener
import taxi.kassa.util.PreferenceManager
import taxi.kassa.util.oneClick
import taxi.kassa.util.showToast

class ChatHistoryFragment : Fragment(R.layout.fragment_chat_history) {

    private val viewModel by inject<ChatHistoryViewModel>()
    private lateinit var adapter: ChatHistoryAdapter
    private var nextOffset = ""
    private var firstLoad = true

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        call_history_title.oneClick()

        with(viewModel) {
            getMessages("")

            error.observe(viewLifecycleOwner) { context?.showToast(it) }

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

                empty_tv.isVisible = adapter.itemCount == 0
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