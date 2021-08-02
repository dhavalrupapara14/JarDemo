package com.jar.demo.network.model

import com.squareup.moshi.Json

/**
 * Photo item data class for unsplash public api photos list response.
 */
data class Photo(
    @field:Json(name = "id") val id: String?,
    @field:Json(name = "urls") val urls: PhotoUrls?
)