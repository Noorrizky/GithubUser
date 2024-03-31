package com.example.submissiononevtwo.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.submissiononevtwo.databinding.FragmentFollowBinding

class FollowFragment : Fragment() {

    private var _binding: FragmentFollowBinding? = null
    private val binding get() = _binding!!

    companion object {
        const val ARG_POSITION = "position"
        const val ARG_USERNAME = "username"
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentFollowBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val position = requireArguments().getInt(ARG_POSITION)
        val username = requireArguments().getString(ARG_USERNAME)

        val viewModel = ViewModelProvider(this).get(FollowFragmentViewModel::class.java)

        // Ambil data pengikut jika posisi adalah 1
        if (position == 1) {
            viewModel.fetchFollowers(username ?: "")
            viewModel.followers.observe(viewLifecycleOwner) { followers ->
                // Update RecyclerView dengan data pengikut yang diterima
                // recyclerViewFollow.adapter = FollowersAdapter(followers)
                // recyclerViewFollow.visibility = View.VISIBLE
                // placeholderTextView.visibility = View.GONE
                binding.testUsername.text = "Followers count: ${followers.size}"
            }
        } else {
            // Ambil data yang diikuti jika posisi bukan 1
            viewModel.fetchFollowing(username ?: "")
            viewModel.following.observe(viewLifecycleOwner) { following ->
                // Update RecyclerView dengan data yang diikuti yang diterima
                // recyclerViewFollow.adapter = FollowingAdapter(following)
                // recyclerViewFollow.visibility = View.VISIBLE
                // placeholderTextView.visibility = View.GONE
                binding.testUsername.text = "Following count: ${following.size}"
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}


