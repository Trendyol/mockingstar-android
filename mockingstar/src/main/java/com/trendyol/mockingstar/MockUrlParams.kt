package com.trendyol.mockingstar

data class MockUrlParams(
	val address: String = "10.0.2.2",
	val port: Int = 8008
) {
	fun buildMockUrl(): String {
		return "http://$address:$port/mock"
	}
}
