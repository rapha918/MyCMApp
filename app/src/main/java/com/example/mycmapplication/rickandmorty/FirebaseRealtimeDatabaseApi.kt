package com.example.mycmapplication.rickandmorty

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.PUT
import retrofit2.http.Path

interface FirebaseRealtimeDatabaseApi {
    @GET("rolls.json")
    suspend fun getRolls(): Int?

    @PUT("rolls.json")
    suspend fun setRolls(@Body count: Int): Int

    @GET("last_saved.json")
    suspend fun getLastSaved(): RickAndMortyItem?

    @PUT("last_saved.json")
    suspend fun setLastSaved(@Body item: RickAndMortyItem): RickAndMortyItem

    @GET("history.json")
    suspend fun getHistory(): Map<String, RickAndMortyItem>?

    @PUT("history/{id}.json")
    suspend fun addHistoryItem(@Path("id") id: String, @Body item: RickAndMortyItem): RickAndMortyItem

    @DELETE("history.json")
    suspend fun clearHistory()
}

object FirebaseApiClient {
    // Active Firebase Realtime Database endpoint for mycmapp
    private const val FIREBASE_BASE_URL = "https://mycmapp-ceaeb-default-rtdb.europe-west1.firebasedatabase.app/"

    val firebaseApi: FirebaseRealtimeDatabaseApi by lazy {
        Retrofit.Builder()
            .baseUrl(FIREBASE_BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(FirebaseRealtimeDatabaseApi::class.java)
    }
}
