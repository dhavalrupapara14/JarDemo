package com.jar.demo.ui.fragment.task2

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.jar.demo.JarApp
import com.jar.demo.databinding.ItemImagesListBinding
import com.jar.demo.databinding.LayoutFooterLoadingBinding
import com.jar.demo.network.model.Photo
import com.jar.demo.utils.decodeUrl
import com.jar.demo.utils.glide.ImageUtils
import javax.inject.Inject

/**
 * Adapter class for images grid view for Task 2.
 */
class PhotoAdapter(
    val context: Context
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    companion object {
        const val LIST = 1
        const val FOOTER_LOADER = 2
    }

    private var imagesList: ArrayList<Photo> = ArrayList()

    @Inject
    lateinit var imageUtils: ImageUtils
    var showLoading: Boolean = false

    init {
        JarApp.getAppComponent().inject(this)
    }

    fun addItems(itemsList: List<Photo>) {
        imagesList.addAll(itemsList)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            LIST -> {
                ImagesAdapterVH(
                    ItemImagesListBinding.inflate(
                        LayoutInflater.from(parent.context), parent,
                        false
                    )
                )
            }
            else -> {
                FooterLoaderVH(
                    LayoutFooterLoadingBinding.inflate(
                        LayoutInflater.from(parent.context), parent,
                        false
                    )
                )
            }
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when(getItemViewType(position)) {
            LIST -> (holder as ImagesAdapterVH).onBind(imagesList[position])
            else -> (holder as FooterLoaderVH).bind(showLoading)
        }
    }

    override fun getItemViewType(position: Int): Int {
        return if (position < getListCount()) {
            LIST
        } else {
            FOOTER_LOADER
        }
    }

    override fun getItemCount(): Int {
        return getListCount() + getFooterCount()
    }

    fun getFooterCount(): Int {
        return 1
    }

    fun getListCount(): Int {
        return imagesList.size
    }

    /**
     * Show/hide state update with respect to api call.
     */
    fun updateFooterLoaderState(showLoading: Boolean) {
        this.showLoading = showLoading
        try {
            notifyItemChanged(itemCount - 1)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    /**
     * View holder class for images view type (LIST view type).
     */
    inner class ImagesAdapterVH(
        private val holderBinding: ItemImagesListBinding
    ) : RecyclerView.ViewHolder(holderBinding.root) {

        fun onBind(photo: Photo) {
            if (photo.urls?.small.isNullOrBlank()) {
                holderBinding.iilImageView.setImageBitmap(null)
            } else {
                //Need to decode the url before loading as it contains some encoded values for some characters.
                imageUtils.loadImage(decodeUrl(photo.urls?.small!!), holderBinding.iilImageView, context)
            }
        }
    }
}