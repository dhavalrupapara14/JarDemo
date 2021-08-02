package com.jar.demo.network.model

import com.squareup.moshi.Json

data class ImageListResponse(
    @field:Json(name = "results") val results: List<ImageItem>
)