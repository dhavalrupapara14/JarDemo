package com.jar.demo.network.model

import com.squareup.moshi.Json

data class ImageItem(
    @field:Json(name = "mal_id") val id: Int,
    @field:Json(name = "image_url") val imageUrl: String?
)