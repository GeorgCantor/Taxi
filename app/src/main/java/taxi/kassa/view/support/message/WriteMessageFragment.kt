package taxi.kassa.view.support.message

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.NavHostFragment.findNavController
import kotlinx.android.synthetic.main.fragment_write_message.*
import org.koin.android.ext.android.inject
import taxi.kassa.R
import taxi.kassa.util.hideKeyboard
import taxi.kassa.util.setVisibility
import taxi.kassa.util.showToast

class WriteMessageFragment : Fragment(R.layout.fragment_write_message) {

    private val viewModel by inject<WriteMessageViewModel>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        message_edit_text.requestFocus()

        back_arrow.setOnClickListener { activity?.onBackPressed() }

        with(viewModel) {
            isProgressVisible.observe(viewLifecycleOwner) { progress_bar.setVisibility(it) }

            error.observe(viewLifecycleOwner) { context?.showToast(it) }

            send_button.setOnClickListener {
                if (message_edit_text.text.isBlank()) {
                    message_edit_text.error = getString(R.string.input_message_error)
                    return@setOnClickListener
                }

                sendMessage(message_edit_text.text.toString())
            }

            isMessageSent.observe(viewLifecycleOwner) { sent ->
                if (sent) findNavController(this@WriteMessageFragment).navigate(R.id.action_writeMessageFragment_to_successFragment)
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        requireView().hideKeyboard()
    }
}