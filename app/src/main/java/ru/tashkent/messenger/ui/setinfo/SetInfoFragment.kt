package ru.tashkent.messenger.ui.setinfo

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import ru.tashkent.messenger.R
import ru.tashkent.messenger.databinding.FragmentSetInfoBinding
import ru.tashkent.messenger.viewbinding.viewBinding

class SetInfoFragment : Fragment(R.layout.fragment_set_info) {

    private val binding: FragmentSetInfoBinding by viewBinding()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.tvSubmit.setOnClickListener {
            findNavController().navigate(SetInfoFragmentDirections.actionSetInfoFragmentToMyChatsFragment())
        }
    }
}