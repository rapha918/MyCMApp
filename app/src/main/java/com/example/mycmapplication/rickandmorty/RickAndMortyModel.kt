package com.example.mycmapplication.rickandmorty

import com.google.gson.annotations.SerializedName
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query

// Category Type Enum
enum class CategoryType(val displayName: String, val apiEndpoint: String) {
    CHARACTER("Character", "character"),
    EPISODE("Episode", "episode"),
    LOCATION("Location", "location")
}

// Info object from API
data class ApiInfo(
    val count: Int = 0,
    val pages: Int = 0,
    val next: String? = null,
    val prev: String? = null
)

// Character Data Models
data class CharacterResult(
    val id: Int,
    val name: String,
    val status: String,
    val species: String,
    val type: String,
    val gender: String,
    val image: String
)

data class CharacterResponse(
    val info: ApiInfo = ApiInfo(),
    val results: List<CharacterResult> = emptyList()
)

// Episode Data Models
data class EpisodeResult(
    val id: Int,
    val name: String,
    @SerializedName("air_date") val airDate: String,
    val episode: String
)

data class EpisodeResponse(
    val info: ApiInfo = ApiInfo(),
    val results: List<EpisodeResult> = emptyList()
)

// Location Data Models
data class LocationResult(
    val id: Int,
    val name: String,
    val type: String,
    val dimension: String
)

data class LocationResponse(
    val info: ApiInfo = ApiInfo(),
    val results: List<LocationResult> = emptyList()
)

// Generic Display Item
data class RickAndMortyItem(
    val id: Int,
    val name: String,
    val imageUrl: String,
    val field1Label: String,
    val field1Value: String,
    val field2Label: String,
    val field2Value: String,
    val category: CategoryType
)

// UI State Sealed Interface
sealed interface RickAndMortyUiState {
    object Loading : RickAndMortyUiState
    data class Success(
        val totalInApi: Int,
        val retrievedCount: Int,
        val selectedItem: RickAndMortyItem,
        val allItems: List<RickAndMortyItem>
    ) : RickAndMortyUiState
    data class Error(val message: String) : RickAndMortyUiState
}

// Retrofit API Interface
interface RickAndMortyApiService {
    @GET("character")
    suspend fun getCharacters(@Query("name") name: String? = null): CharacterResponse

    @GET("episode")
    suspend fun getEpisodes(@Query("name") name: String? = null): EpisodeResponse

    @GET("location")
    suspend fun getLocations(@Query("name") name: String? = null): LocationResponse
}

// Retrofit Client Singleton
object RickAndMortyApiClient {
    private const val BASE_URL = "https://rickandmortyapi.com/api/"

    val apiService: RickAndMortyApiService by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(RickAndMortyApiService::class.java)
    }
}
