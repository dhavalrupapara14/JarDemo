package com.jar.demo.ui.fragment.task1

import android.os.Bundle
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import com.jar.demo.R
import com.jar.demo.base.BaseFragment
import com.jar.demo.databinding.FragmentTask1Binding
import com.jar.demo.utils.dpToPixel

/**
 * Task 1 fragment showing a blank container and 2 shapes at the bottom with undo button.
 *
 * When you tap on either of the shapes, the same will be added at a random position in above container
 * without collision with already added shapes.
 *
 * When you tap undo icon, the last added shape in the above container will be removed.
 *
 * When container is full of shapes and no space to add a shape without collision,
 * you'll see a toast showing the same message.
 */
class Task1Fragment : BaseFragment() {
    private lateinit var binding: FragmentTask1Binding
    private val viewModel: Task1ViewModel by viewModels()

    // Shape id to keep count of added shaped, it will also be used for removing shapes.
    private var shapeId = 0
    // Clicked shape drawable res id, to be used after the shape position is calculated asynchronously.
    private var clickedShapeDrawableResId: Int = -1
    // Flag to indicate that process for finding a shape position is running (true) or completed (false)
    private var findingShapePosition: Boolean = false

    companion object {
        fun newInstance() = Task1Fragment()
        // We are fixing the shape size as 50dp. The same can be given through layout xml.
        // But we also need size in pixels for further calculation, hence assigning it programmatically.
        const val SHAPE_SIZE_DP = 50f
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
        binding = FragmentTask1Binding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        // Set action bar title and show back button.
        setUpActionBar(
            title = getString(R.string.task_1),
            setDisplayHomeAsUpEnabled = true
        )
        // Initialize view model variable in the layout xml
        binding.viewModel = viewModel

        initObservers()

        // initialize shape width and height in pixels.
        viewModel.shapeWidthPx = dpToPixel(SHAPE_SIZE_DP, requireActivity())
        viewModel.shapeHeightPx = viewModel.shapeWidthPx
    }

    private fun initObservers() {
        // Check to avoid setting observers multiple times
        if (viewModel.viewClickLiveData.hasActiveObservers())
            return

        // button click observer
        viewModel.viewClickLiveData.observe(viewLifecycleOwner, Observer {
            /**
             * If existing process of adding a shape is not completed then
             * do not start a new one on tap on either of the shapes.
             *
             * Also, it's better to prevent user to access undo button if process is running.
             */
            when(it) {
                binding.ivSquare.id -> {
                    if (!findingShapePosition) {
                        addShapeWithoutCollision(R.drawable.shape_square)
                    } else {
                        showLoadingCancellable()
                    }
                }
                binding.ivCircle.id -> {
                    if (!findingShapePosition) {
                        addShapeWithoutCollision(R.drawable.shape_circle)
                    } else {
                        showLoadingCancellable()
                    }
                }
                binding.ivUndo.id -> {
                    if (!findingShapePosition) {
                        removeShape()
                    } else {
                        showLoadingCancellable()
                    }
                }
            }
        })

        // Shape position observer
        viewModel.shapePosLiveData.observe(viewLifecycleOwner, Observer { shapePos ->
            if (shapePos != null) {
                if (shapePos.first != -1) {
                    // We have a valid position

                    val ivShape = ImageView(requireContext())
//                  val lp = FrameLayout.LayoutParams(FrameLayout.LayoutParams.WRAP_CONTENT, FrameLayout.LayoutParams.WRAP_CONTENT)
                    val lp = FrameLayout.LayoutParams(viewModel.shapeWidthPx, viewModel.shapeHeightPx)
                    lp.leftMargin = shapePos.first
                    lp.topMargin = shapePos.second
                    ivShape.layoutParams = lp

                    ivShape.setImageResource(clickedShapeDrawableResId)
                    shapeId++
                    ivShape.id = shapeId

                    // Add shape to container and shape position to list.
                    binding.flShapeContainer.addView(ivShape)
                    viewModel.shapePosList.add(shapePos)
                } else {
                    // We didn't get any valid position.
                    showToast("No space available to add the shape without collision", Toast.LENGTH_LONG)
                }
                findingShapePosition = false
                hideLoadingCancellable()
            }
        })
    }

    /**
     * Calculate container size once. We will add shapes into this container.
     */
    private fun calculateContainerSizeOnce() {
        if (viewModel.shapeContainerWidth == 0) {
            viewModel.shapeContainerWidth = binding.flShapeContainer.width
            viewModel.shapeContainerHeight =
                binding.flShapeContainer.height - (binding.flShapeContainer.height - binding.divider.top)
        }
    }

    /**
     * Remove last added shape from the container.
     */
    private fun removeShape() {
        if (shapeId >= 1) {
            try {
                binding.flShapeContainer.removeViewAt(shapeId - 1)
                viewModel.shapePosList.removeAt(shapeId - 1)
                shapeId--
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    /**
     * Add a shape into container without collision with other already present shapes
     */
    private fun addShapeWithoutCollision(shapeDrawableResId: Int) {
        clickedShapeDrawableResId = shapeDrawableResId
        findingShapePosition = true
        calculateContainerSizeOnce()

        // Calculate pos on background thread.
        viewModel.calculateNextCollisionFreeShapePos()
    }
}