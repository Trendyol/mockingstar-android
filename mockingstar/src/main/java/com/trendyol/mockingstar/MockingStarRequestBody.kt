package com.trendyol.mockingstar

import kotlinx.serialization.Serializable

@Serializable
internal class MockingStarRequestBody(
	val method: String,
	val url: String,
	val header: Map<String, String>,
	val body: String?,
)
