package com.example.submissiononevtwo.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.submissiononevtwo.databinding.FragmentFollowBinding

class FollowFragment : Fragment() {

    private var _binding: FragmentFollowBinding? = null
    private val binding get() = _binding!!
    private lateinit var viewModel: FollowFragmentViewModel
    private lateinit var adapter: ReviewAdapter

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

        viewModel = ViewModelProvider(this).get(FollowFragmentViewModel::class.java)
        adapter = ReviewAdapter()

        binding.rvReview.layoutManager = LinearLayoutManager(requireContext())
        binding.rvReview.adapter = adapter

        val position = requireArguments().getInt(ARG_POSITION)
        val username = requireArguments().getString(ARG_USERNAME)

        if (position == 1) {
            viewModel.fetchFollowing(username ?: "")
            viewModel.following.observe(viewLifecycleOwner) { following ->
                adapter.submitList(following)
            }
        } else {
            viewModel.fetchFollowers(username ?: "")
            viewModel.followers.observe(viewLifecycleOwner) { followers ->
                adapter.submitList(followers)
            }
        }

        viewModel.isLoading.observe(viewLifecycleOwner) { isLoading ->
            if (isLoading) {
                binding.progressBar.visibility = View.VISIBLE
            } else {
                binding.progressBar.visibility = View.GONE
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        const val ARG_POSITION = "position"
        const val ARG_USERNAME = "username"
    }
}
