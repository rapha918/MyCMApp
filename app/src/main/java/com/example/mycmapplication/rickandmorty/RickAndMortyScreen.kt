package com.example.mycmapplication.rickandmorty

import android.widget.Toast
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.mycmapplication.R
import com.example.mycmapplication.ui.theme.MyCMApplicationTheme

@Composable
fun RickAndMortyScreen(
    viewModel: RickAndMortyViewModel = viewModel(),
    onBack: () -> Unit = {}
) {
    val context = LocalContext.current
    val selectedCategory by viewModel.selectedCategory.collectAsState()
    val filterQuery by viewModel.filterQuery.collectAsState()
    val uiState by viewModel.uiState.collectAsState()
    val totalRolls by viewModel.totalRolls.collectAsState()
    val savedHistory by viewModel.savedHistory.collectAsState()
    val toastMessage by viewModel.toastMessage.collectAsState()

    LaunchedEffect(toastMessage) {
        toastMessage?.let { msg ->
            Toast.makeText(context, msg, Toast.LENGTH_SHORT).show()
            viewModel.clearToastMessage()
        }
    }

    RickAndMortyContent(
        selectedCategory = selectedCategory,
        filterQuery = filterQuery,
        uiState = uiState,
        totalRolls = totalRolls,
        savedHistory = savedHistory,
        onCategorySelected = { viewModel.setCategory(it) },
        onQueryChanged = { viewModel.setFilterQuery(it) },
        onRollItem = { viewModel.rollRandomItem() },
        onSaveItem = { viewModel.saveCurrentItem() },
        onLoadLastSaved = { viewModel.loadLastSavedItem() },
        onSelectHistoryItem = { viewModel.selectItemFromHistory(it) },
        onClearHistory = { viewModel.clearHistory() },
        onRetry = { viewModel.loadData() },
        onBack = onBack
    )
}

@Composable
fun RickAndMortyContent(
    selectedCategory: CategoryType,
    filterQuery: String,
    uiState: RickAndMortyUiState,
    totalRolls: Int,
    savedHistory: List<RickAndMortyItem>,
    onCategorySelected: (CategoryType) -> Unit,
    onQueryChanged: (String) -> Unit,
    onRollItem: () -> Unit,
    onSaveItem: () -> Unit,
    onLoadLastSaved: () -> Unit,
    onSelectHistoryItem: (RickAndMortyItem) -> Unit,
    onClearHistory: () -> Unit,
    onRetry: () -> Unit,
    onBack: () -> Unit
) {
    val keyboardController = LocalSoftwareKeyboardController.current

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    colors = listOf(Color(0xFF0D1B2A), Color(0xFF1B263B), Color(0xFF415A77))
                )
            )
            .verticalScroll(rememberScrollState())
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Top Header
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Button(
                onClick = onBack,
                colors = ButtonDefaults.buttonColors(containerColor = Color.White.copy(alpha = 0.2f)),
                shape = RoundedCornerShape(12.dp)
            ) {
                Text("⬅ Back", color = Color.White, fontWeight = FontWeight.Bold)
            }
            Spacer(modifier = Modifier.weight(1f))
            Text(
                text = "Rick & Morty Explorer",
                style = MaterialTheme.typography.titleLarge.copy(
                    fontWeight = FontWeight.ExtraBold,
                    color = Color(0xFF00E676),
                    letterSpacing = 1.sp
                )
            )
            Spacer(modifier = Modifier.weight(1f))
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Category Selection Component
        Text(
            text = "SELECT CATEGORY",
            style = MaterialTheme.typography.labelMedium.copy(
                fontWeight = FontWeight.Bold,
                color = Color.White.copy(alpha = 0.8f)
            )
        )
        Spacer(modifier = Modifier.height(8.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            CategoryType.entries.forEach { category ->
                val isSelected = category == selectedCategory
                FilterChip(
                    selected = isSelected,
                    onClick = { onCategorySelected(category) },
                    label = {
                        Text(
                            text = category.displayName,
                            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
                        )
                    },
                    colors = FilterChipDefaults.filterChipColors(
                        selectedContainerColor = Color(0xFF00E676),
                        selectedLabelColor = Color.Black,
                        containerColor = Color.White.copy(alpha = 0.15f),
                        labelColor = Color.White
                    ),
                    shape = RoundedCornerShape(16.dp)
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Filter Input Field
        OutlinedTextField(
            value = filterQuery,
            onValueChange = onQueryChanged,
            label = { Text("Filter ${selectedCategory.displayName} by name...", color = Color.White.copy(alpha = 0.7f)) },
            leadingIcon = {
                Icon(
                    painter = painterResource(id = R.drawable.ic_explore),
                    contentDescription = "Search Icon",
                    tint = Color(0xFF00E676),
                    modifier = Modifier.size(20.dp)
                )
            },
            trailingIcon = {
                if (filterQuery.isNotEmpty()) {
                    IconButton(onClick = { onQueryChanged("") }) {
                        Icon(
                            painter = painterResource(id = R.drawable.ic_share),
                            contentDescription = "Clear Search",
                            tint = Color.White,
                            modifier = Modifier.size(18.dp)
                        )
                    }
                }
            },
            singleLine = true,
            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Search),
            keyboardActions = KeyboardActions(onSearch = { keyboardController?.hide() }),
            shape = RoundedCornerShape(16.dp),
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(16.dp))

        // UI State Handling
        when (uiState) {
            is RickAndMortyUiState.Loading -> {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(260.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        CircularProgressIndicator(color = Color(0xFF00E676))
                        Spacer(modifier = Modifier.height(12.dp))
                        Text(
                            text = "Fetching data from Rick & Morty API...",
                            color = Color.White.copy(alpha = 0.8f)
                        )
                    }
                }
            }

            is RickAndMortyUiState.Error -> {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(20.dp),
                    colors = CardDefaults.cardColors(containerColor = Color(0xFFFFEBEE))
                ) {
                    Column(
                        modifier = Modifier.padding(24.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = "⚠️ " + uiState.message,
                            color = Color(0xFFC62828),
                            textAlign = TextAlign.Center,
                            style = MaterialTheme.typography.bodyLarge
                        )
                        Spacer(modifier = Modifier.height(12.dp))
                        Button(
                            onClick = onRetry,
                            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFC62828))
                        ) {
                            Text("Retry Search", color = Color.White)
                        }
                    }
                }
            }

            is RickAndMortyUiState.Success -> {
                // API Statistics & Roll Counter Card
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = Color(0xFF1B263B))
                ) {
                    Column(modifier = Modifier.padding(14.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Text(
                                    text = "Total Rolls (Synced): ",
                                    style = MaterialTheme.typography.labelMedium.copy(color = Color.Gray)
                                )
                                Text(
                                    text = "$totalRolls",
                                    style = MaterialTheme.typography.titleMedium.copy(
                                        fontWeight = FontWeight.Bold,
                                        color = Color(0xFFFFD700)
                                    )
                                )
                            }

                            Surface(
                                color = Color(0xFF00E676).copy(alpha = 0.2f),
                                shape = RoundedCornerShape(12.dp)
                            ) {
                                Text(
                                    text = "Retrieved in view: ${uiState.retrievedCount} items",
                                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                                    style = MaterialTheme.typography.labelSmall.copy(
                                        fontWeight = FontWeight.Bold,
                                        color = Color(0xFF00E676)
                                    )
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(6.dp))

                        Text(
                            text = "Total ${selectedCategory.displayName}s in API: ${uiState.totalInApi}",
                            style = MaterialTheme.typography.bodySmall.copy(color = Color.LightGray)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Item Display Card
                val item = uiState.selectedItem
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(24.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    elevation = CardDefaults.cardElevation(defaultElevation = 10.dp)
                ) {
                    Column(
                        modifier = Modifier.padding(20.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        if (item.imageUrl.isNotBlank()) {
                            AsyncImage(
                                model = ImageRequest.Builder(LocalContext.current)
                                    .data(item.imageUrl)
                                    .crossfade(true)
                                    .build(),
                                contentDescription = item.name,
                                modifier = Modifier
                                    .size(200.dp)
                                    .clip(RoundedCornerShape(20.dp)),
                                contentScale = ContentScale.Crop,
                                placeholder = painterResource(id = R.drawable.ic_traveler_avatar),
                                error = painterResource(id = R.drawable.ic_traveler_avatar)
                            )
                        } else {
                            Image(
                                painter = painterResource(id = R.drawable.ic_traveler_avatar),
                                contentDescription = item.name,
                                modifier = Modifier
                                    .size(200.dp)
                                    .clip(RoundedCornerShape(20.dp)),
                                contentScale = ContentScale.Crop
                            )
                        }

                        Spacer(modifier = Modifier.height(14.dp))

                        // Item Name
                        Text(
                            text = item.name,
                            style = MaterialTheme.typography.headlineMedium.copy(
                                fontWeight = FontWeight.Bold,
                                color = Color(0xFF0D1B2A)
                            ),
                            textAlign = TextAlign.Center
                        )

                        Spacer(modifier = Modifier.height(12.dp))

                        // Two JSON Schema Fields
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceEvenly
                        ) {
                            Surface(
                                color = Color(0xFFE8F5E9),
                                shape = RoundedCornerShape(12.dp),
                                modifier = Modifier.weight(1f).padding(end = 6.dp)
                            ) {
                                Column(
                                    modifier = Modifier.padding(10.dp),
                                    horizontalAlignment = Alignment.CenterHorizontally
                                ) {
                                    Text(
                                        text = item.field1Label.uppercase(),
                                        style = MaterialTheme.typography.labelSmall.copy(color = Color.Gray)
                                    )
                                    Text(
                                        text = item.field1Value,
                                        style = MaterialTheme.typography.bodyMedium.copy(
                                            fontWeight = FontWeight.Bold,
                                            color = Color(0xFF2E7D32)
                                        ),
                                        textAlign = TextAlign.Center
                                    )
                                }
                            }

                            Surface(
                                color = Color(0xFFE1F5FE),
                                shape = RoundedCornerShape(12.dp),
                                modifier = Modifier.weight(1f).padding(start = 6.dp)
                            ) {
                                Column(
                                    modifier = Modifier.padding(10.dp),
                                    horizontalAlignment = Alignment.CenterHorizontally
                                ) {
                                    Text(
                                        text = item.field2Label.uppercase(),
                                        style = MaterialTheme.typography.labelSmall.copy(color = Color.Gray)
                                    )
                                    Text(
                                        text = item.field2Value,
                                        style = MaterialTheme.typography.bodyMedium.copy(
                                            fontWeight = FontWeight.Bold,
                                            color = Color(0xFF0277BD)
                                        ),
                                        textAlign = TextAlign.Center
                                    )
                                }
                            }
                        }

                        Spacer(modifier = Modifier.height(16.dp))

                        // Actions Row: Roll, Save, Load
                        Button(
                            onClick = onRollItem,
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(48.dp),
                            shape = RoundedCornerShape(14.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF00E676))
                        ) {
                            Text(
                                text = "🎲 Roll Random Item",
                                style = MaterialTheme.typography.titleMedium.copy(
                                    fontWeight = FontWeight.Bold,
                                    color = Color.Black
                                )
                            )
                        }

                        Spacer(modifier = Modifier.height(8.dp))

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Button(
                                onClick = onSaveItem,
                                modifier = Modifier.weight(1f).height(44.dp),
                                shape = RoundedCornerShape(12.dp),
                                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF2196F3))
                            ) {
                                Text("💾 Save Item", color = Color.White, fontWeight = FontWeight.Bold)
                            }

                            OutlinedButton(
                                onClick = onLoadLastSaved,
                                modifier = Modifier.weight(1f).height(44.dp),
                                shape = RoundedCornerShape(12.dp)
                            ) {
                                Text("📂 Load Last", fontWeight = FontWeight.Bold)
                            }
                        }
                    }
                }
            }
        }

        // Saved Items History Section
        AnimatedVisibility(visible = savedHistory.isNotEmpty()) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 20.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "SAVED ITEMS HISTORY (${savedHistory.size})",
                        style = MaterialTheme.typography.labelMedium.copy(
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )
                    )

                    Text(
                        text = "Clear All",
                        style = MaterialTheme.typography.labelSmall.copy(color = Color(0xFFFF5252)),
                        modifier = Modifier.clickable { onClearHistory() }
                    )
                }

                Spacer(modifier = Modifier.height(10.dp))

                LazyRow(
                    horizontalArrangement = Arrangement.spacedBy(10.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    items(savedHistory) { historyItem ->
                        Card(
                            modifier = Modifier
                                .width(140.dp)
                                .clickable { onSelectHistoryItem(historyItem) },
                            shape = RoundedCornerShape(16.dp),
                            colors = CardDefaults.cardColors(containerColor = Color(0xFF1B263B))
                        ) {
                            Column(
                                modifier = Modifier.padding(10.dp),
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                if (historyItem.imageUrl.isNotBlank()) {
                                    AsyncImage(
                                        model = historyItem.imageUrl,
                                        contentDescription = historyItem.name,
                                        modifier = Modifier
                                            .size(70.dp)
                                            .clip(RoundedCornerShape(12.dp)),
                                        contentScale = ContentScale.Crop
                                    )
                                } else {
                                    Image(
                                        painter = painterResource(id = R.drawable.ic_traveler_avatar),
                                        contentDescription = historyItem.name,
                                        modifier = Modifier
                                            .size(70.dp)
                                            .clip(RoundedCornerShape(12.dp))
                                    )
                                }

                                Spacer(modifier = Modifier.height(6.dp))

                                Text(
                                    text = historyItem.name,
                                    style = MaterialTheme.typography.labelMedium.copy(
                                        color = Color.White,
                                        fontWeight = FontWeight.Bold
                                    ),
                                    maxLines = 1,
                                    overflow = TextOverflow.Ellipsis
                                )

                                Text(
                                    text = historyItem.category.displayName,
                                    style = MaterialTheme.typography.labelSmall.copy(color = Color(0xFF00E676))
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun RickAndMortyPreview() {
    MyCMApplicationTheme {
        val mockItem = RickAndMortyItem(
            id = 1,
            name = "Evil Morty",
            imageUrl = "",
            field1Label = "Status",
            field1Value = "Alive",
            field2Label = "Species",
            field2Value = "Human",
            category = CategoryType.CHARACTER
        )
        RickAndMortyContent(
            selectedCategory = CategoryType.CHARACTER,
            filterQuery = "morty",
            uiState = RickAndMortyUiState.Success(
                totalInApi = 826,
                retrievedCount = 20,
                selectedItem = mockItem,
                allItems = listOf(mockItem)
            ),
            totalRolls = 4,
            savedHistory = listOf(mockItem),
            onCategorySelected = {},
            onQueryChanged = {},
            onRollItem = {},
            onSaveItem = {},
            onLoadLastSaved = {},
            onSelectHistoryItem = {},
            onClearHistory = {},
            onRetry = {},
            onBack = {}
        )
    }
}
