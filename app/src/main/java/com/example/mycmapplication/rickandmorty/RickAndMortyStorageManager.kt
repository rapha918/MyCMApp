package com.example.mycmapplication.rickandmorty

import android.content.Context
import android.content.SharedPreferences
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class RickAndMortyStorageManager(context: Context) {
    private val prefs: SharedPreferences = context.getSharedPreferences("rick_and_morty_persistent_prefs", Context.MODE_PRIVATE)
    private val gson = Gson()

    fun getTotalRolls(): Int {
        return prefs.getInt("total_rolls", 0)
    }

    fun incrementRolls(): Int {
        val newRolls = getTotalRolls() + 1
        prefs.edit().putInt("total_rolls", newRolls).apply()
        return newRolls
    }

    fun saveItem(item: RickAndMortyItem): List<RickAndMortyItem> {
        // Save as last saved item
        val json = gson.toJson(item)
        prefs.edit().putString("last_saved_item", json).apply()

        // Append to history list
        val history = getHistory().toMutableList()
        history.add(0, item)
        val historyJson = gson.toJson(history)
        prefs.edit().putString("saved_history", historyJson).apply()

        return history
    }

    fun getLastSavedItem(): RickAndMortyItem? {
        val json = prefs.getString("last_saved_item", null) ?: return null
        return try {
            gson.fromJson(json, RickAndMortyItem::class.java)
        } catch (e: Exception) {
            null
        }
    }

    fun getHistory(): List<RickAndMortyItem> {
        val json = prefs.getString("saved_history", null) ?: return emptyList()
        return try {
            val type = object : TypeToken<List<RickAndMortyItem>>() {}.type
            gson.fromJson(json, type) ?: emptyList()
        } catch (e: Exception) {
            emptyList()
        }
    }

    fun clearHistory() {
        prefs.edit().remove("saved_history").remove("last_saved_item").apply()
    }
}
