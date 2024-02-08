package com.trendyol.mockingstar

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.gson.annotations.SerializedName
import com.trendyol.mockingstar.MockingStarInterceptor
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Query

class SampleViewModel : ViewModel() {
	private var _inputText = MutableStateFlow("")
	val inputText = _inputText.asStateFlow()

	private var _searchResults = MutableStateFlow<List<Repository>>(emptyList())
	val searchResults = _searchResults.asStateFlow()

	private val client = OkHttpClient.Builder()
		.addInterceptor(MockingStarInterceptor())
		.addNetworkInterceptor(HttpLoggingInterceptor().apply {
			level = HttpLoggingInterceptor.Level.BODY
		})
		.build()

	private val service = Retrofit.Builder()
		.client(client)
		.baseUrl(URL)
		.addConverterFactory(GsonConverterFactory.create())
		.build()
		.create(GithubService::class.java)

	fun updateInput(text: String) {
		_inputText.value = text
	}

	fun search() {
		viewModelScope.launch {
			val response = service.repositories(_inputText.value)
			val results = response.items.orEmpty().map {
				Repository(
					name = it?.fullName.orEmpty(),
					forkCount = it?.forksCount ?: 0,
					starGazerCount = it?.starGazersCount ?: 0,
					description = it?.description.orEmpty()
				)
			}
			_searchResults.update {
				results
			}
		}
	}

	companion object {
		const val URL = "https://api.github.com/"
	}
}

data class Repository(
	val name: String,
	val forkCount: Int,
	val starGazerCount: Int,
	val description: String?,
)

interface GithubService {
	@GET("search/repositories")
	suspend fun repositories(
		@Query("q") searchTerm: String,
		@Header("test") test: String = "hohoho",
		@Header("tes2") test2: String = "hohoho222",
	): SearchResultListResponse
}

data class SearchResultListResponse(
	@SerializedName("items") val items: List<SearchResultResponse?>?
)

data class SearchResultResponse(
	@SerializedName("full_name") val fullName: String?,
	@SerializedName("stargazers_count") val starGazersCount: Int?,
	@SerializedName("forks_count") val forksCount: Int?,
	@SerializedName("description") val description: String?,
	@SerializedName("id") val id: Int,
)
