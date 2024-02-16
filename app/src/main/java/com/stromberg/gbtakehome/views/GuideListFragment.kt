package com.stromberg.gbtakehome.views

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.UiThread
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.stromberg.gbtakehome.BuildConfig
import com.stromberg.gbtakehome.GuideRepository
import com.stromberg.gbtakehome.R
import com.stromberg.gbtakehome.adapters.GuideListAdapter
import com.stromberg.gbtakehome.databinding.GuideListFragmentBinding
import com.stromberg.gbtakehome.models.local.Guide
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject


@AndroidEntryPoint
class GuideListFragment : Fragment() {
    private lateinit var binding: GuideListFragmentBinding
    private val adapter: GuideListAdapter = GuideListAdapter()
    private val viewModel: GuideListViewModel by viewModels()

    @Inject lateinit var guideRepository: GuideRepository

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = GuideListFragmentBinding.inflate(layoutInflater, container, false)

        binding.list.adapter = adapter

        binding.swipeRefreshLayout.setOnRefreshListener {
            viewModel.fetchGuides()
        }

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.guides.observe(viewLifecycleOwner) { guides ->
            handleGuides(guides)
        }

        binding.buildText.text = BuildConfig.BUILD_TYPE

        binding.swipeRefreshLayout.isRefreshing = true
        viewModel.fetchGuides()
    }

    private fun handleGuides(guides: List<Guide>?) {
        binding.swipeRefreshLayout.isRefreshing = true

        if (guides != null) {
            adapter.submitList(guides)

            binding.list.isVisible = guides.isNotEmpty()
        } else {
            showErrorDialog()
        }

        binding.swipeRefreshLayout.isRefreshing = false
    }

    @UiThread
    private fun showErrorDialog() {
        MaterialAlertDialogBuilder(requireContext())
            .setMessage(getString(R.string.error_message))
            .setPositiveButton(getString(R.string.ok)) { dialog, _ ->
                dialog.dismiss()
            }
            .setNegativeButton(getString(R.string.retry)) { dialog, _ ->
                dialog.dismiss()
                viewModel.fetchGuides()
            }
            .create()
            .show()
    }
}