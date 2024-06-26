package com.trendyol.mockingstar

import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import okhttp3.HttpUrl.Companion.toHttpUrl
import okhttp3.Interceptor
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.RequestBody.Companion.toRequestBody
import okhttp3.Response
import okio.Buffer
import java.nio.charset.Charset
import java.nio.charset.StandardCharsets

class MockingStarInterceptor(
	private val params: MockUrlParams = MockUrlParams(),
	private val header: Map<String, String> = emptyMap(),
) : Interceptor {

	@OptIn(ExperimentalSerializationApi::class)
	override fun intercept(chain: Interceptor.Chain): Response {
		val originalRequest = chain.request()

		val buffer = Buffer()

		originalRequest.body?.writeTo(buffer)

		val contentType = originalRequest.body?.contentType()
		val charset: Charset = contentType?.charset(StandardCharsets.UTF_8) ?: StandardCharsets.UTF_8

		val requestBody = MockingStarRequestBody(
			method = originalRequest.method,
			url = originalRequest.url.toString(),
			headers = originalRequest.headers.toMap(),
			body = buffer.readString(charset).takeIf { it.isNotEmpty() },
		)
		val json = Json { explicitNulls = false }
		val requestString = json.encodeToString(requestBody)

		val newRequest = originalRequest.newBuilder()
			.url(params.buildMockUrl().toHttpUrl())
			.addHeader("disableLiveEnvironment", "false")
			.addHeader("Content-Type", "application/json")
			.also { builder ->
				header.entries.forEach { builder.addHeader(it.key, it.value) }
			}
			.post(requestString.toRequestBody("application/json".toMediaType())).build()

		return chain.proceed(newRequest)
	}
}
