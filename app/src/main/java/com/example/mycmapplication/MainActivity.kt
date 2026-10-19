package com.example.mycmapplication

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.mycmapplication.navigation.AppNavigationHost
import com.example.mycmapplication.ui.theme.MyCMApplicationTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MyCMApplicationTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    AppNavigationHost(modifier = Modifier.padding(innerPadding))
                }
            }
        }
    }
}

// Helper to map die values 1..6 to vector drawables
fun getDiceDrawable(value: Int): Int {
    return when (value) {
        1 -> R.drawable.ic_dice_1
        2 -> R.drawable.ic_dice_2
        3 -> R.drawable.ic_dice_3
        4 -> R.drawable.ic_dice_4
        5 -> R.drawable.ic_dice_5
        else -> R.drawable.ic_dice_6
    }
}

// -------------------------------------------------------------
// MAIN ROLLER SCREEN
// -------------------------------------------------------------
@Composable
fun MainRollScreen(
    onNavigateToResult: (Int) -> Unit
) {
    var currentDie by remember { mutableIntStateOf((1..6).random()) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    colors = listOf(Color(0xFF1E3C72), Color(0xFF2A5298))
                )
            )
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = "DICE ROLLER",
            style = MaterialTheme.typography.headlineLarge.copy(
                fontWeight = FontWeight.Bold,
                color = Color.White,
                letterSpacing = 2.sp
            )
        )

        Text(
            text = "Roll as much as you want, then tap 'Go to Result'",
            style = MaterialTheme.typography.bodyMedium.copy(color = Color.White.copy(alpha = 0.8f)),
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(top = 8.dp, bottom = 32.dp)
        )

        Card(
            shape = RoundedCornerShape(24.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            elevation = CardDefaults.cardElevation(defaultElevation = 8.dp),
            modifier = Modifier.padding(16.dp)
        ) {
            Column(
                modifier = Modifier.padding(32.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Image(
                    painter = painterResource(id = getDiceDrawable(currentDie)),
                    contentDescription = "Die Face $currentDie",
                    modifier = Modifier.size(140.dp)
                )

                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    text = "Current Roll: $currentDie",
                    style = MaterialTheme.typography.titleLarge.copy(
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF1E3C72)
                    )
                )
            }
        }

        Spacer(modifier = Modifier.height(32.dp))

        Button(
            onClick = { currentDie = (1..6).random() },
            modifier = Modifier
                .fillMaxWidth()
                .height(54.dp),
            shape = RoundedCornerShape(16.dp),
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFFF9800))
        ) {
            Text(
                text = "🎲 ROLL DIE",
                style = MaterialTheme.typography.titleMedium.copy(
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
            )
        }

        Spacer(modifier = Modifier.height(12.dp))

        Button(
            onClick = { onNavigateToResult(currentDie) },
            modifier = Modifier
                .fillMaxWidth()
                .height(54.dp),
            shape = RoundedCornerShape(16.dp),
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF4CAF50))
        ) {
            Text(
                text = "GO TO RESULT ➔",
                style = MaterialTheme.typography.titleMedium.copy(
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
            )
        }
    }
}

// -------------------------------------------------------------
// DIE RESULT 1: FAVORITE TRAVEL BADGE + REFRESH MOCK TEMP
// -------------------------------------------------------------
@Composable
fun DieResult1Screen(
    initialTemp: Int = 22,
    onBack: () -> Unit
) {
    var mockTemp by remember { mutableIntStateOf(initialTemp) }

    Column(modifier = Modifier.fillMaxSize()) {
        Surface(
            color = Color(0xFF2A5298),
            modifier = Modifier.fillMaxWidth()
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 8.dp, vertical = 6.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Button(
                    onClick = onBack,
                    colors = ButtonDefaults.buttonColors(containerColor = Color.White.copy(alpha = 0.2f)),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Text("⬅ Back", color = Color.White, fontWeight = FontWeight.Bold)
                }

                Text(
                    text = "Die Result: 1",
                    style = MaterialTheme.typography.titleMedium.copy(
                        color = Color.White,
                        fontWeight = FontWeight.Bold
                    )
                )

                Button(
                    onClick = { mockTemp = (-5..38).random() },
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFFF9800)),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Text("Refresh 🌡️", color = Color.White, fontWeight = FontWeight.Bold)
                }
            }
        }

        TravelBadgeScreen(mockTemp = mockTemp, modifier = Modifier.weight(1f))
    }
}

// -------------------------------------------------------------
// DIE RESULT 2: INCREMENT BY TWO BUTTON
// -------------------------------------------------------------
@Composable
fun DieResult2Screen(
    initialValue: Int = 2,
    onBack: () -> Unit
) {
    var dieValue by remember { mutableIntStateOf(initialValue) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF5F5F5))
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Button(
                onClick = onBack,
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF1E3C72)),
                shape = RoundedCornerShape(12.dp)
            ) {
                Text("⬅ Back", color = Color.White)
            }
            Spacer(modifier = Modifier.weight(1f))
            Text(
                text = "Die Result: $dieValue",
                style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold)
            )
            Spacer(modifier = Modifier.weight(1f))
        }

        Card(
            shape = RoundedCornerShape(24.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            elevation = CardDefaults.cardElevation(defaultElevation = 6.dp),
            modifier = Modifier.padding(16.dp)
        ) {
            Column(
                modifier = Modifier.padding(32.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Image(
                    painter = painterResource(id = getDiceDrawable(if (dieValue in 1..6) dieValue else 2)),
                    contentDescription = "Die Face $dieValue",
                    modifier = Modifier.size(130.dp)
                )

                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    text = "Die Value: $dieValue",
                    style = MaterialTheme.typography.headlineMedium.copy(
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.primary
                    )
                )
            }
        }

        Button(
            onClick = {
                dieValue = (dieValue + 2) % 6
                if(dieValue == 0) dieValue = 6
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(54.dp),
            shape = RoundedCornerShape(16.dp),
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF673AB7))
        ) {
            Text(
                text = "➕ Increment Die by Two (+2)",
                style = MaterialTheme.typography.titleMedium.copy(
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
            )
        }
    }
}

// -------------------------------------------------------------
// DIE RESULT 3: TEXTFIELD TO UPDATE DIE
// -------------------------------------------------------------
@Composable
fun DieResult3Screen(
    initialValue: Int = 3,
    onBack: () -> Unit
) {
    var dieValue by remember { mutableIntStateOf(initialValue) }
    var textInput by remember { mutableStateOf(initialValue.toString()) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF5F5F5))
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Button(
                onClick = onBack,
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF1E3C72)),
                shape = RoundedCornerShape(12.dp)
            ) {
                Text("⬅ Back", color = Color.White)
            }
            Spacer(modifier = Modifier.weight(1f))
            Text(
                text = "Die Result: $dieValue",
                style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold)
            )
            Spacer(modifier = Modifier.weight(1f))
        }

        Card(
            shape = RoundedCornerShape(24.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            elevation = CardDefaults.cardElevation(defaultElevation = 6.dp),
            modifier = Modifier.padding(16.dp)
        ) {
            Column(
                modifier = Modifier.padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Image(
                    painter = painterResource(id = getDiceDrawable(if (dieValue in 1..6) dieValue else 3)),
                    contentDescription = "Die Face $dieValue",
                    modifier = Modifier.size(130.dp)
                )

                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    text = "Current Value: $dieValue",
                    style = MaterialTheme.typography.headlineMedium.copy(
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.primary
                    )
                )

                Spacer(modifier = Modifier.height(16.dp))

                OutlinedTextField(
                    value = textInput,
                    onValueChange = { newText ->
                        textInput = newText
                        newText.toIntOrNull()?.let { parsed ->
                            if (parsed in 1..6) {
                                dieValue = parsed
                            } else {
                                dieValue = 3
                            }
                        }
                    },
                    label = { Text("Update Die Value (1-6)") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))
    }
}

// -------------------------------------------------------------
// DIE RESULT 4: SIX NAVIGATION BUTTONS
// -------------------------------------------------------------
@Composable
fun DieResult4Screen(
    onNavigateToScreen: (Int) -> Unit,
    onBack: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF5F5F5))
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Button(
                onClick = onBack,
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF1E3C72)),
                shape = RoundedCornerShape(12.dp)
            ) {
                Text("⬅ Back", color = Color.White)
            }
            Spacer(modifier = Modifier.weight(1f))
            Text(
                text = "Die Result: 4",
                style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold)
            )
            Spacer(modifier = Modifier.weight(1f))
        }

        Spacer(modifier = Modifier.height(16.dp))

        Image(
            painter = painterResource(id = R.drawable.ic_dice_4),
            contentDescription = "Die 4",
            modifier = Modifier.size(90.dp)
        )

        Spacer(modifier = Modifier.height(12.dp))

        Text(
            text = "Select any destination screen:",
            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.SemiBold)
        )

        Spacer(modifier = Modifier.height(12.dp))

        Column(
            modifier = Modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            val screenButtons = listOf(
                "Go to Screen 1 (Travel Badge)" to 1,
                "Go to Screen 2 (+2 Increment)" to 2,
                "Go to Screen 3 (TextField Input)" to 3,
                "Go to Screen 4 (6 Navigation Buttons)" to 4,
                "Go to Screen 5 (Simple Result)" to 5,
                "Go to Screen 6 (Dice Game)" to 6
            )

            screenButtons.forEach { (label, screenNum) ->
                Button(
                    onClick = { onNavigateToScreen(screenNum) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(46.dp),
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = when (screenNum) {
                            1 -> Color(0xFF3F51B5)
                            2 -> Color(0xFF673AB7)
                            3 -> Color(0xFF009688)
                            4 -> Color(0xFFFF9800)
                            5 -> Color(0xFFE91E63)
                            else -> Color(0xFF4CAF50)
                        }
                    )
                ) {
                    Text(text = label, fontWeight = FontWeight.Bold, color = Color.White)
                }
            }
        }
    }
}

// -------------------------------------------------------------
// DIE RESULT 5: SIMPLE RESULT SCREEN
// -------------------------------------------------------------
@Composable
fun DieResult5Screen(
    initialValue: Int = 5,
    onBack: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF5F5F5))
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Button(
                onClick = onBack,
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF1E3C72)),
                shape = RoundedCornerShape(12.dp)
            ) {
                Text("⬅ Back", color = Color.White)
            }
            Spacer(modifier = Modifier.weight(1f))
            Text(
                text = "Die Result: $initialValue",
                style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold)
            )
            Spacer(modifier = Modifier.weight(1f))
        }

        Card(
            shape = RoundedCornerShape(24.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            elevation = CardDefaults.cardElevation(defaultElevation = 6.dp),
            modifier = Modifier.padding(16.dp)
        ) {
            Column(
                modifier = Modifier.padding(32.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Image(
                    painter = painterResource(id = getDiceDrawable(if (initialValue in 1..6) initialValue else 5)),
                    contentDescription = "Die Face $initialValue",
                    modifier = Modifier.size(140.dp)
                )

                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    text = "Die Result: $initialValue",
                    style = MaterialTheme.typography.headlineLarge.copy(
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFFE91E63)
                    )
                )
            }
        }

        Button(
            onClick = onBack,
            modifier = Modifier
                .fillMaxWidth()
                .height(54.dp),
            shape = RoundedCornerShape(16.dp),
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF1E3C72))
        ) {
            Text("⬅ Back to Main Roller", color = Color.White, fontWeight = FontWeight.Bold)
        }
    }
}

// -------------------------------------------------------------
// DIE RESULT 6: TWO DICE GAME WITH WIN/LOSS & NAVIGATION
// -------------------------------------------------------------
@Composable
fun DieResult6Screen(
    initialValue: Int = 6,
    onNavigateToResult: (Int) -> Unit,
    onBack: () -> Unit
) {
    val firstDie = initialValue
    var secondDie by remember { mutableStateOf<Int?>(null) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF5F5F5))
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Button(
                onClick = onBack,
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF1E3C72)),
                shape = RoundedCornerShape(12.dp)
            ) {
                Text("⬅ Back", color = Color.White)
            }
            Spacer(modifier = Modifier.weight(1f))
            Text(
                text = "Die Result: 6",
                style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold)
            )
            Spacer(modifier = Modifier.weight(1f))
        }

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Die 1
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text("Die 1", fontWeight = FontWeight.Bold)
                Spacer(modifier = Modifier.height(8.dp))
                Image(
                    painter = painterResource(id = getDiceDrawable(firstDie)),
                    contentDescription = "First Die",
                    modifier = Modifier.size(100.dp)
                )
                Text("Value: $firstDie", fontWeight = FontWeight.SemiBold)
            }

            // Die 2
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text("Die 2", fontWeight = FontWeight.Bold)
                Spacer(modifier = Modifier.height(8.dp))
                if (secondDie != null) {
                    Image(
                        painter = painterResource(id = getDiceDrawable(secondDie!!)),
                        contentDescription = "Second Die",
                        modifier = Modifier.size(100.dp)
                    )
                    Text("Value: ${secondDie!!}", fontWeight = FontWeight.SemiBold)
                } else {
                    Box(
                        modifier = Modifier
                            .size(100.dp)
                            .background(Color.LightGray.copy(alpha = 0.4f), RoundedCornerShape(12.dp)),
                        contentAlignment = Alignment.Center
                    ) {
                        Text("?", style = MaterialTheme.typography.headlineLarge)
                    }
                    Text("Roll to reveal", style = MaterialTheme.typography.bodySmall)
                }
            }
        }

        if (secondDie != null) {
            val userWon = secondDie!! >= firstDie
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(
                    containerColor = if (userWon) Color(0xFFE8F5E9) else Color(0xFFFFEBEE)
                ),
                shape = RoundedCornerShape(16.dp)
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = if (userWon) "YOU WON!" else "YOU LOST!",
                        style = MaterialTheme.typography.headlineSmall.copy(
                            fontWeight = FontWeight.Bold,
                            color = if (userWon) Color(0xFF2E7D32) else Color(0xFFC62828)
                        )
                    )

                    Text(
                        text = if (userWon)
                            "New roll (${secondDie!!}) is ≥ current roll ($firstDie)"
                        else
                            "New roll (${secondDie!!}) is < current roll ($firstDie)",
                        style = MaterialTheme.typography.bodyMedium,
                        textAlign = TextAlign.Center
                    )

                    if (userWon) {
                        Spacer(modifier = Modifier.height(12.dp))
                        Button(
                            onClick = { onNavigateToResult(secondDie!!) },
                            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF2E7D32)),
                            shape = RoundedCornerShape(12.dp)
                        ) {
                            Text(
                                "Go to Result (Screen ${secondDie!!}) ➔",
                                color = Color.White,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                }
            }
        }

        Button(
            onClick = { secondDie = (1..6).random() },
            modifier = Modifier
                .fillMaxWidth()
                .height(54.dp),
            shape = RoundedCornerShape(16.dp),
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF2196F3))
        ) {
            Text(
                text = "🎲 Roll Second Die",
                style = MaterialTheme.typography.titleMedium.copy(
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
            )
        }
    }
}

// -------------------------------------------------------------
// REUSABLE TRAVEL BADGE SCREEN (ASSIGNMENT #1 COMPONENT)
// -------------------------------------------------------------
@OptIn(ExperimentalLayoutApi::class)
@Composable
fun TravelBadgeScreen(mockTemp: Int = 22, modifier: Modifier = Modifier) {
    val context = LocalContext.current
    var isBookmarked by remember { mutableStateOf(false) }
    var isVerified by remember { mutableStateOf(true) }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    colors = listOf(Color(0xFFA8C0FF), Color(0xFF3F2B96))
                )
            )
            .verticalScroll(rememberScrollState())
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "DIGITAL TRAVEL BADGE",
            style = MaterialTheme.typography.labelLarge.copy(
                letterSpacing = 3.sp,
                fontWeight = FontWeight.ExtraBold,
                color = Color.White
            ),
            modifier = Modifier.padding(top = 8.dp, bottom = 12.dp)
        )

        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(24.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
            elevation = CardDefaults.cardElevation(defaultElevation = 12.dp)
        ) {
            Column {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(200.dp)
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.ic_landscape_kyoto),
                        contentDescription = "Kyoto Sunset Landscape",
                        modifier = Modifier.fillMaxSize(),
                        contentScale = ContentScale.Crop
                    )

                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(
                                Brush.verticalGradient(
                                    colors = listOf(Color.Transparent, Color.Black.copy(alpha = 0.75f)),
                                    startY = 100f
                                )
                            )
                    )

                    Surface(
                        modifier = Modifier
                            .padding(12.dp)
                            .align(Alignment.TopStart),
                        color = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.9f),
                        shape = RoundedCornerShape(16.dp)
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp)
                        ) {
                            Icon(
                                painter = painterResource(id = R.drawable.ic_location),
                                contentDescription = null,
                                tint = MaterialTheme.colorScheme.onPrimaryContainer,
                                modifier = Modifier.size(16.dp)
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(
                                text = "Japan • Kansai",
                                style = MaterialTheme.typography.labelSmall.copy(
                                    fontWeight = FontWeight.Bold,
                                    color = MaterialTheme.colorScheme.onPrimaryContainer
                                )
                            )
                        }
                    }

                    IconButton(
                        onClick = {
                            isBookmarked = !isBookmarked
                            val msg = if (isBookmarked) "Saved Kyoto to Favorites!" else "Removed from Favorites"
                            Toast.makeText(context, msg, Toast.LENGTH_SHORT).show()
                        },
                        modifier = Modifier
                            .align(Alignment.TopEnd)
                            .padding(8.dp)
                            .background(Color.Black.copy(alpha = 0.4f), CircleShape)
                    ) {
                        val iconColor by animateColorAsState(
                            if (isBookmarked) Color(0xFFFFD700) else Color.White,
                            label = "bookmarkColor"
                        )
                        Icon(
                            painter = painterResource(
                                id = if (isBookmarked) R.drawable.ic_bookmark_filled else R.drawable.ic_bookmark_border
                            ),
                            contentDescription = "Bookmark",
                            tint = iconColor,
                            modifier = Modifier.size(20.dp)
                        )
                    }

                    Image(
                        painter = painterResource(id = R.drawable.ic_passport_stamp),
                        contentDescription = "Passport Stamp",
                        modifier = Modifier
                            .size(80.dp)
                            .align(Alignment.BottomEnd)
                            .padding(end = 12.dp, bottom = 12.dp)
                            .rotate(-12f)
                    )

                    Column(
                        modifier = Modifier
                            .align(Alignment.BottomStart)
                            .padding(16.dp)
                    ) {
                        Text(
                            text = "Kyoto, Japan",
                            style = MaterialTheme.typography.headlineLarge.copy(
                                fontWeight = FontWeight.Bold,
                                color = Color.White
                            )
                        )
                        Text(
                            text = "Cultural Capital of 1,000 Temples",
                            style = MaterialTheme.typography.bodySmall.copy(
                                color = Color.White.copy(alpha = 0.9f)
                            )
                        )
                    }
                }

                Column(modifier = Modifier.padding(16.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                painter = painterResource(id = R.drawable.ic_star),
                                contentDescription = "Rating Star",
                                tint = Color(0xFFFFB300),
                                modifier = Modifier.size(20.dp)
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(
                                text = "4.9",
                                fontWeight = FontWeight.Bold,
                                style = MaterialTheme.typography.bodyMedium
                            )
                            Text(
                                text = " (14.2k reviews)",
                                style = MaterialTheme.typography.bodySmall,
                                color = Color.Gray
                            )
                        }

                        Surface(
                            color = Color(0xFFE8F5E9),
                            shape = RoundedCornerShape(12.dp)
                        ) {
                            Row(
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Icon(
                                    painter = painterResource(id = R.drawable.ic_calendar),
                                    contentDescription = "Best Season",
                                    tint = Color(0xFF2E7D32),
                                    modifier = Modifier.size(14.dp)
                                )
                                Spacer(modifier = Modifier.width(4.dp))
                                Text(
                                    text = "Best: Mar - May",
                                    style = MaterialTheme.typography.labelSmall.copy(
                                        color = Color(0xFF2E7D32),
                                        fontWeight = FontWeight.SemiBold
                                    )
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    WeatherCardWidget(mockTemp = mockTemp)

                    Spacer(modifier = Modifier.height(16.dp))

                    Text(
                        text = "HIGHLIGHTS & CULTURE",
                        style = MaterialTheme.typography.labelMedium.copy(
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.primary
                        )
                    )
                    Spacer(modifier = Modifier.height(6.dp))

                    FlowRow(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        verticalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        TagChip(label = "⛩️ Fushimi Inari")
                        TagChip(label = "🏯 Golden Pavilion")
                        TagChip(label = "🎋 Bamboo Grove")
                        TagChip(label = "🍵 Tea Ceremonies")
                        TagChip(label = "🌸 Cherry Blossoms")
                    }

                    Spacer(modifier = Modifier.height(16.dp))
                    HorizontalDivider(color = Color.LightGray.copy(alpha = 0.5f))
                    Spacer(modifier = Modifier.height(16.dp))

                    TravelerBadgeFooter(isVerified = isVerified)

                    Spacer(modifier = Modifier.height(16.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        OutlinedButton(
                            onClick = {
                                Toast.makeText(context, "Sharing Travel Badge...", Toast.LENGTH_SHORT).show()
                            },
                            modifier = Modifier.weight(1f),
                            shape = RoundedCornerShape(12.dp)
                        ) {
                            Icon(
                                painter = painterResource(id = R.drawable.ic_share),
                                contentDescription = "Share",
                                modifier = Modifier.size(18.dp)
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Text("Share")
                        }

                        Button(
                            onClick = {
                                isVerified = !isVerified
                                val stateMsg = if (isVerified) "Badge Verified!" else "Badge Unverified"
                                Toast.makeText(context, stateMsg, Toast.LENGTH_SHORT).show()
                            },
                            modifier = Modifier.weight(1f),
                            shape = RoundedCornerShape(12.dp),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = if (isVerified) Color(0xFF1B5E20) else MaterialTheme.colorScheme.primary
                            )
                        ) {
                            Icon(
                                painter = painterResource(id = R.drawable.ic_explore),
                                contentDescription = "Explore",
                                modifier = Modifier.size(18.dp)
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(if (isVerified) "Verified" else "Verify")
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun WeatherCardWidget(mockTemp: Int = 22) {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        color = Color(0xFFE1F5FE),
        border = BorderStroke(1.dp, Color(0xFF81D4FA))
    ) {
        Row(
            modifier = Modifier
                .padding(12.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Image(
                    painter = painterResource(id = R.drawable.ic_weather_sunny),
                    contentDescription = "Weather Icon",
                    modifier = Modifier.size(44.dp)
                )
                Spacer(modifier = Modifier.width(10.dp))
                Column {
                    Text(
                        text = "${mockTemp}°C • Sunny",
                        style = MaterialTheme.typography.titleMedium.copy(
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF0277BD)
                        )
                    )
                    Text(
                        text = "Kyoto Current Weather",
                        style = MaterialTheme.typography.bodySmall.copy(color = Color(0xFF01579B))
                    )
                }
            }

            Column(horizontalAlignment = Alignment.End) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_water_drop),
                        contentDescription = "Humidity",
                        tint = Color(0xFF0288D1),
                        modifier = Modifier.size(14.dp)
                    )
                    Spacer(modifier = Modifier.width(2.dp))
                    Text(
                        text = "48% Hum",
                        style = MaterialTheme.typography.labelSmall.copy(color = Color(0xFF0277BD))
                    )
                }
                Spacer(modifier = Modifier.height(2.dp))
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_wind),
                        contentDescription = "Wind",
                        tint = Color(0xFF0288D1),
                        modifier = Modifier.size(14.dp)
                    )
                    Spacer(modifier = Modifier.width(2.dp))
                    Text(
                        text = "12 km/h",
                        style = MaterialTheme.typography.labelSmall.copy(color = Color(0xFF0277BD))
                    )
                }
            }
        }
    }
}

@Composable
fun TagChip(label: String) {
    Surface(
        color = MaterialTheme.colorScheme.surfaceVariant,
        shape = RoundedCornerShape(8.dp)
    ) {
        Text(
            text = label,
            modifier = Modifier.padding(horizontal = 10.dp, vertical = 5.dp),
            style = MaterialTheme.typography.labelMedium.copy(
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        )
    }
}

@Composable
fun TravelerBadgeFooter(isVerified: Boolean) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(
                color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.4f),
                shape = RoundedCornerShape(16.dp)
            )
            .padding(12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Image(
            painter = painterResource(id = R.drawable.ic_traveler_avatar),
            contentDescription = "Traveler Profile",
            modifier = Modifier
                .size(52.dp)
                .clip(CircleShape)
                .border(2.dp, MaterialTheme.colorScheme.primary, CircleShape)
        )

        Spacer(modifier = Modifier.width(12.dp))

        Column(modifier = Modifier.weight(1f)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    text = "Explorer: Rafael Santos",
                    style = MaterialTheme.typography.bodyMedium.copy(
                        fontWeight = FontWeight.Bold
                    )
                )
                if (isVerified) {
                    Spacer(modifier = Modifier.width(4.dp))
                    Icon(
                        painter = painterResource(id = R.drawable.ic_check_verified),
                        contentDescription = "Verified Badge",
                        tint = Color(0xFF2E7D32),
                        modifier = Modifier.size(16.dp)
                    )
                }
            }
            Text(
                text = "Passport ID: #JP-2026-KYT",
                style = MaterialTheme.typography.bodySmall.copy(color = Color.Gray)
            )
            Text(
                text = "Visited: October 2026",
                style = MaterialTheme.typography.labelSmall.copy(
                    color = MaterialTheme.colorScheme.primary,
                    fontWeight = FontWeight.Medium
                )
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun MainRollPreview() {
    MyCMApplicationTheme {
        MainRollScreen(onNavigateToResult = {})
    }
}

@Preview(showBackground = true)
@Composable
fun DieResult2Preview() {
    MyCMApplicationTheme {
        DieResult2Screen(initialValue = 2, onBack = {})
    }
}

@Preview(showBackground = true)
@Composable
fun DieResult3Preview() {
    MyCMApplicationTheme {
        DieResult3Screen(initialValue = 3, onBack = {})
    }
}

@Preview(showBackground = true)
@Composable
fun DieResult4Preview() {
    MyCMApplicationTheme {
        DieResult4Screen(onNavigateToScreen = {}, onBack = {})
    }
}

@Preview(showBackground = true)
@Composable
fun DieResult6Preview() {
    MyCMApplicationTheme {
        DieResult6Screen(initialValue = 6, onNavigateToResult = {}, onBack = {})
    }
}
