package com.jar.demo.ui.fragment.task2

import androidx.recyclerview.widget.RecyclerView
import com.jar.demo.databinding.LayoutFooterLoadingBinding
import com.jar.demo.utils.gone
import com.jar.demo.utils.visible

/**
 * Footer loader view holder class for recycler view.
 */
class FooterLoaderVH(
    private val footerBinding: LayoutFooterLoadingBinding
) : RecyclerView.ViewHolder(footerBinding.root) {

    fun bind(showLoading: Boolean) {
        if (showLoading)
            footerBinding.lflProgress.visible()
         else
            footerBinding.lflProgress.gone()
    }
}