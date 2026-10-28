package com.example.mycmapplication.rickandmorty

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class RickAndMortyViewModel(application: Application) : AndroidViewModel(application) {

    private val storageManager = RickAndMortyStorageManager(application.applicationContext)

    private val _selectedCategory = MutableStateFlow(CategoryType.CHARACTER)
    val selectedCategory: StateFlow<CategoryType> = _selectedCategory.asStateFlow()

    private val _filterQuery = MutableStateFlow("")
    val filterQuery: StateFlow<String> = _filterQuery.asStateFlow()

    private val _uiState = MutableStateFlow<RickAndMortyUiState>(RickAndMortyUiState.Loading)
    val uiState: StateFlow<RickAndMortyUiState> = _uiState.asStateFlow()

    private val _totalRolls = MutableStateFlow(storageManager.getTotalRolls())
    val totalRolls: StateFlow<Int> = _totalRolls.asStateFlow()

    private val _savedHistory = MutableStateFlow(storageManager.getHistory())
    val savedHistory: StateFlow<List<RickAndMortyItem>> = _savedHistory.asStateFlow()

    private val _toastMessage = MutableStateFlow<String?>(null)
    val toastMessage: StateFlow<String?> = _toastMessage.asStateFlow()

    init {
        loadData()
        syncWithFirebase()
    }

    private fun syncWithFirebase() {
        viewModelScope.launch {
            try {

                val remoteRolls = FirebaseApiClient.firebaseApi.getRolls()
                if (remoteRolls != null && remoteRolls > _totalRolls.value) {
                    _totalRolls.value = remoteRolls
                }


                val remoteHistoryMap = FirebaseApiClient.firebaseApi.getHistory()
                if (!remoteHistoryMap.isNullOrEmpty()) {
                    val remoteList = remoteHistoryMap.values.toList()
                    _savedHistory.value = remoteList
                }
            } catch (e: Exception) {
                // Fallback to local persistent storage if offline
            }
        }
    }

    fun setCategory(category: CategoryType) {
        _selectedCategory.value = category
        _filterQuery.value = ""
        loadData()
    }

    fun setFilterQuery(query: String) {
        _filterQuery.value = query
        loadData()
    }

    fun clearToastMessage() {
        _toastMessage.value = null
    }

    fun rollRandomItem() {
        val currentState = _uiState.value
        if (currentState is RickAndMortyUiState.Success && currentState.allItems.isNotEmpty()) {
            val randomItem = currentState.allItems.random()
            _uiState.value = currentState.copy(selectedItem = randomItem)
            val newRolls = storageManager.incrementRolls()
            _totalRolls.value = newRolls

            // Sync with Firebase
            viewModelScope.launch {
                try {
                    FirebaseApiClient.firebaseApi.setRolls(newRolls)
                } catch (e: Exception) {

                }
            }
        }
    }

    fun saveCurrentItem() {
        val currentState = _uiState.value
        if (currentState is RickAndMortyUiState.Success) {
            val item = currentState.selectedItem
            val updatedHistory = storageManager.saveItem(item)
            _savedHistory.value = updatedHistory
            _toastMessage.value = "💾 Saved '${item.name}' to Firebase & Local DB!"

            // Sync with Firebase Realtime Database
            viewModelScope.launch {
                try {
                    FirebaseApiClient.firebaseApi.setLastSaved(item)
                    FirebaseApiClient.firebaseApi.addHistoryItem(System.currentTimeMillis().toString(), item)
                } catch (e: Exception) {
                    // Handled gracefully
                }
            }
        }
    }

    fun loadLastSavedItem() {
        viewModelScope.launch {
            var lastItem: RickAndMortyItem? = null
            try {
                // Try fetching from Firebase first
                lastItem = FirebaseApiClient.firebaseApi.getLastSaved()
            } catch (e: Exception) {
                // Fallback to local storage
            }

            if (lastItem == null) {
                lastItem = storageManager.getLastSavedItem()
            }

            if (lastItem != null) {
                _selectedCategory.value = lastItem.category
                _uiState.value = RickAndMortyUiState.Success(
                    totalInApi = 1,
                    retrievedCount = 1,
                    selectedItem = lastItem,
                    allItems = listOf(lastItem)
                )
                _toastMessage.value = "📂 Loaded last saved item: '${lastItem.name}' (Firebase)"
            } else {
                _toastMessage.value = "⚠️ No saved item found in Firebase or Local Storage!"
            }
        }
    }

    fun selectItemFromHistory(item: RickAndMortyItem) {
        _selectedCategory.value = item.category
        _uiState.value = RickAndMortyUiState.Success(
            totalInApi = 1,
            retrievedCount = 1,
            selectedItem = item,
            allItems = listOf(item)
        )
        _toastMessage.value = "Loaded '${item.name}' from history"
    }

    fun clearHistory() {
        storageManager.clearHistory()
        _savedHistory.value = emptyList()
        _toastMessage.value = "Cleared saved items history"

        viewModelScope.launch {
            try {
                FirebaseApiClient.firebaseApi.clearHistory()
            } catch (e: Exception) {
                // Handled gracefully
            }
        }
    }

    fun loadData() {
        viewModelScope.launch {
            _uiState.value = RickAndMortyUiState.Loading
            try {
                val category = _selectedCategory.value
                val query = _filterQuery.value.trim()
                val apiService = RickAndMortyApiClient.apiService

                when (category) {
                    CategoryType.CHARACTER -> {
                        val response = apiService.getCharacters(query.ifBlank { null })
                        val items = response.results.map {
                            RickAndMortyItem(
                                id = it.id,
                                name = it.name,
                                imageUrl = it.image,
                                field1Label = "Status",
                                field1Value = it.status,
                                field2Label = "Species",
                                field2Value = it.species,
                                category = CategoryType.CHARACTER
                            )
                        }
                        if (items.isNotEmpty()) {
                            _uiState.value = RickAndMortyUiState.Success(
                                totalInApi = response.info.count,
                                retrievedCount = items.size,
                                selectedItem = items.random(),
                                allItems = items
                            )
                        } else {
                            _uiState.value = RickAndMortyUiState.Error("No characters found matching '$query'")
                        }
                    }

                    CategoryType.EPISODE -> {
                        val response = apiService.getEpisodes(query.ifBlank { null })
                        val items = response.results.map {
                            RickAndMortyItem(
                                id = it.id,
                                name = it.name,
                                imageUrl = "https://rickandmortyapi.com/api/character/avatar/${(it.id % 20) + 1}.jpeg",
                                field1Label = "Episode",
                                field1Value = it.episode,
                                field2Label = "Air Date",
                                field2Value = it.airDate,
                                category = CategoryType.EPISODE
                            )
                        }
                        if (items.isNotEmpty()) {
                            _uiState.value = RickAndMortyUiState.Success(
                                totalInApi = response.info.count,
                                retrievedCount = items.size,
                                selectedItem = items.random(),
                                allItems = items
                            )
                        } else {
                            _uiState.value = RickAndMortyUiState.Error("No episodes found matching '$query'")
                        }
                    }

                    CategoryType.LOCATION -> {
                        val response = apiService.getLocations(query.ifBlank { null })
                        val items = response.results.map {
                            RickAndMortyItem(
                                id = it.id,
                                name = it.name,
                                imageUrl = "https://rickandmortyapi.com/api/character/avatar/${((it.id * 3) % 20) + 1}.jpeg",
                                field1Label = "Type",
                                field1Value = it.type,
                                field2Label = "Dimension",
                                field2Value = it.dimension,
                                category = CategoryType.LOCATION
                            )
                        }
                        if (items.isNotEmpty()) {
                            _uiState.value = RickAndMortyUiState.Success(
                                totalInApi = response.info.count,
                                retrievedCount = items.size,
                                selectedItem = items.random(),
                                allItems = items
                            )
                        } else {
                            _uiState.value = RickAndMortyUiState.Error("No locations found matching '$query'")
                        }
                    }
                }
            } catch (e: Exception) {
                _uiState.value = RickAndMortyUiState.Error("Failed to fetch data: ${e.localizedMessage ?: "Network error"}")
            }
        }
    }
}
