package com.example.submissiononevtwo.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.example.submissiononevtwo.databinding.FragmentFollowBinding
import com.example.submissiononevtwo.ui.FollowFragmentViewModel
import com.example.submissiononevtwo.ui.ReviewAdapter

class FollowFragment : Fragment() {

    private lateinit var binding: FragmentFollowBinding
    private val viewModel: FollowFragmentViewModel by viewModels()
    private lateinit var adapter: ReviewAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentFollowBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        adapter = ReviewAdapter()

        val position = requireArguments().getInt(ARG_POSITION)
        val username = requireArguments().getString(ARG_USERNAME)

        if (position == 1) {
            viewModel.fetchFollowing(username ?: "")
            viewModel.isLoading.observe(viewLifecycleOwner) { isLoading ->
                showLoading(isLoading)
            }
            viewModel.following.observe(viewLifecycleOwner) { following ->
                adapter.submitList(following)
            }
        } else {
            viewModel.fetchFollowers(username ?: "")
            viewModel.isLoading.observe(viewLifecycleOwner) { isLoading ->
                showLoading(isLoading)
            }
            viewModel.followers.observe(viewLifecycleOwner) { followers ->
                adapter.submitList(followers)
            }
        }

        binding.recyclerViewFollow.adapter = adapter
    }

    private fun showLoading(isLoading: Boolean) {
        if (isLoading) {
            binding.progressBar.visibility = View.VISIBLE
        } else {
            binding.progressBar.visibility = View.GONE
        }
    }

    companion object {
        const val ARG_POSITION = "position"
        const val ARG_USERNAME = "username"
    }
}
