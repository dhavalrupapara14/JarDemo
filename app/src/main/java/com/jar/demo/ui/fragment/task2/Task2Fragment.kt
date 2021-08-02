package com.jar.demo.ui.fragment.task2

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

/**
 * Task 2 fragments showing the popular images as grid of 3 columns from unsplash public api.
 */
class Task2Fragment : BaseFragment() {
    private lateinit var binding: FragmentTask3Binding

    /*private val viewModel: Task2ViewModel by viewModels {
        Task2ViewModelFactory(Task2Repository())
    }*/
    private val viewModel: Task2ViewModel by viewModels()
    private lateinit var imagesAdapter: PhotoAdapter
    private lateinit var layoutManager: GridLayoutManager
    private val columns = 3

    companion object {
        fun newInstance() = Task2Fragment()
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
        initUi()

        //First api call
        viewModel.getPhotosList()
    }

    private fun initObservers() {
        // Avoid setting observers again if already active.
        if (viewModel.loadingLiveData.hasActiveObservers())
            return

        // Observer for loading indicator for api call / recycler view
        viewModel.loadingLiveData.observe(viewLifecycleOwner, Observer {
            imagesAdapter.updateFooterLoaderState(it)
        })

        // Observer to handle api errors
        viewModel.apiErrorLiveData.observe(viewLifecycleOwner, Observer {
            handleApiError(it)
        })

        // Observer to handle api success response.
        viewModel.photosListResponseLiveData.observe(viewLifecycleOwner, Observer {
            // Assuming that there won't be duplication of records.
            // So adding records directly to the adapter without duplication check.
            if (it.isNotEmpty()) {
                val count = imagesAdapter.itemCount
                imagesAdapter.addItems(it)
                imagesAdapter.notifyItemRangeInserted(count, it.size)
            } else {
                // These cases mostly won't occur for this example, hence not implementing it.
                if (imagesAdapter.itemCount == 0) {
                    //TODO: No data.
                    showToast("No data", Toast.LENGTH_SHORT)
                } else {
                    //TODO: no more data
                    showToast("No more data", Toast.LENGTH_SHORT)
                }
            }
        })
    }

    /**
     * Initialize views here.
     */
    private fun initUi() {
        imagesAdapter = PhotoAdapter(requireContext())
        layoutManager = GridLayoutManager(requireContext(), columns)
        // Span size lookup as we want to show our loader at the bottom always instead of as a cell inside a column of a grid.
        layoutManager.spanSizeLookup = object: GridLayoutManager.SpanSizeLookup() {
            override fun getSpanSize(position: Int): Int {
                return if (position == (imagesAdapter.itemCount - imagesAdapter.getFooterCount())) {
                    columns
                } else 1
            }
        }

        // Item decoration to give padding between cells.
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

        // Set layout manager and adapter to recycler view.
        binding.ft2RvGrid.layoutManager = layoutManager
        binding.ft2RvGrid.adapter = imagesAdapter

        // recycler view scroll listener for pagination
        binding.ft2RvGrid.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                try {
                    // For pagination, list counts must be >= 1 page limit. If not, then it means that only 1 page is available.
                    if (imagesAdapter.getListCount() >= (viewModel.limit - 1)) {
                        // If last visible item position is reached then hit the pagination call if the call is not happening already.
                        if (layoutManager.findLastVisibleItemPosition() >= (imagesAdapter.itemCount - imagesAdapter.getFooterCount())
                            && viewModel.loadingLiveData.value != true
                        ) {
                            binding.ft2RvGrid.post {
                                // Pagination Api call
                                viewModel.getPhotosList()
                            }
                        }
                    }
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
        })
    }
}