package com.jar.demo.ui.fragment.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import com.jar.demo.R
import com.jar.demo.base.BaseFragment
import com.jar.demo.databinding.FragmentHomeBinding
import com.jar.demo.ui.fragment.task1.Task1Fragment
import com.jar.demo.ui.fragment.task2.Task2Fragment

/**
 * Home fragment containing Task 1 and Task 2 buttons to take user to the respective screens.
 */
class HomeFragment : BaseFragment() {
    private lateinit var binding: FragmentHomeBinding
    private val viewModel: HomeViewModel by viewModels()

    companion object {
        fun newInstance() = HomeFragment()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        // Set action bar title and hide back button.
        setUpActionBar(
            title = getString(R.string.app_name),
            setDisplayHomeAsUpEnabled = false
        )
        // Initialize viewmodel variable in the layout xml
        binding.viewModel = viewModel

        initObservers()
    }

    private fun initObservers() {
        // Check to avoid setting observers multiple times
        if (viewModel.viewClickLiveData.hasActiveObservers())
            return

        // button click observer
        viewModel.viewClickLiveData.observe(viewLifecycleOwner, Observer {
            when(it) {
                binding.btnTask1.id -> {
                    // open task1 fragment
                    replaceFragment(
                        Task1Fragment.newInstance(),
                        true,
                        Task1Fragment::class.java.simpleName
                    )
                }
                binding.btnTask2.id -> {
                    // open task2 fragment
                    replaceFragment(
                        Task2Fragment.newInstance(),
                        true,
                        Task2Fragment::class.java.simpleName
                    )
                }
            }
        })
    }
}