package com.jar.demo.ui.fragment.task3

import android.graphics.Rect
import android.os.Bundle
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.jar.demo.R
import com.jar.demo.base.BaseFragment
import com.jar.demo.databinding.FragmentTask3Binding
import com.jar.demo.utils.dpToPixel
import com.jar.demo.ui.fragment.task2.Task2ViewModel

/**
 * Similar to Task 2, it shows the same grid view but hits another public api to get images for testing purpose.
 */
class Task3Fragment  : BaseFragment() {
    private lateinit var binding: FragmentTask3Binding

    private val viewModel: Task2ViewModel by viewModels()
    private lateinit var imagesAdapter: ImagesAdapter
    private lateinit var layoutManager: GridLayoutManager
    private val columns = 3

    companion object {
        fun newInstance() = Task3Fragment()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //Required to handle back press of action bar from fragment
        setHasOptionsMenu(true)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> activity?.onBackPressed()
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentTask3Binding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        // Set action bar title and show back button.
        setUpActionBar(
            title = getString(R.string.task_2),
            setDisplayHomeAsUpEnabled = true
        )

        initObservers()

        imagesAdapter = ImagesAdapter(requireContext())
        layoutManager = GridLayoutManager(requireContext(), columns)
        layoutManager.spanSizeLookup = object: GridLayoutManager.SpanSizeLookup() {
            override fun getSpanSize(position: Int): Int {
                return if (position == (imagesAdapter.itemCount - imagesAdapter.getFooterCount())) {
                    columns
                } else 1
            }
        }
        val spacingLeftRight = dpToPixel(3f, requireContext())
        val spacingTopBottom = dpToPixel(3f, requireContext())
        binding.ft2RvGrid.addItemDecoration(object : RecyclerView.ItemDecoration() {
            override fun getItemOffsets(
                outRect: Rect,
                view: View,
                parent: RecyclerView,
                state: RecyclerView.State
            ) {
                super.getItemOffsets(outRect, view, parent, state)
                outRect.set(spacingLeftRight, spacingTopBottom, spacingLeftRight, spacingTopBottom)
            }
        })
        binding.ft2RvGrid.layoutManager = layoutManager
        binding.ft2RvGrid.adapter = imagesAdapter
        //scroll listener for pagination
        binding.ft2RvGrid.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                try {
                    /**
                     * Add following check in order to prevent api call from happening when list items are less than limit for first page.
                     * If you change a tab and list has only 1 record then this api will call will happen for respective page. To prevent this,
                     * the condition is added
                     */
                    if (imagesAdapter.getListCount() >= (viewModel.limit - 1)) {
                        if (layoutManager.findLastVisibleItemPosition() >= (imagesAdapter.itemCount - imagesAdapter.getFooterCount())
                            && viewModel.loadingLiveData.value != true
                        ) {
                            binding.ft2RvGrid.post {
                                // Pagination Api call
                                viewModel.getImagesList()
                            }
                        }
                    }
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
        })

        //First api call
        viewModel.getImagesList()
    }

    private fun initObservers() {
        // Avoid setting observers again if already active.
        if (viewModel.loadingLiveData.hasActiveObservers())
            return

        viewModel.loadingLiveData.observe(viewLifecycleOwner, Observer {
            imagesAdapter.updateFooterLoaderState(it)
        })

        viewModel.apiErrorLiveData.observe(viewLifecycleOwner, Observer {
            handleApiError(it)
        })

        viewModel.imageListResponseLiveData.observe(viewLifecycleOwner, Observer {
            // Assuming that there won't be duplication of records.
            // So adding records directly to the adapter without duplication check.
            if (it.results.isNotEmpty()) {
                val count = imagesAdapter.itemCount
                imagesAdapter.addItems(it.results)
                imagesAdapter.notifyItemRangeInserted(count, it.results.size)
            } else {
                if (imagesAdapter.itemCount == 0) {
                    //TODO: No data
                    showToast("No data", Toast.LENGTH_SHORT)
                } else {
                    //TODO: no more data
                    showToast("No more data", Toast.LENGTH_SHORT)
                }
            }
        })
    }
}