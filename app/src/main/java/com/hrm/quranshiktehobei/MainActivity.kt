package com.hrm.quranshiktehobei

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

private val Green = Color(0xFF126B45)
private val LightGreen = Color(0xFFEAF5EF)
private val Cream = Color(0xFFFFFBF0)

data class MenuItem(val title: String, val subtitle: String, val icon: androidx.compose.ui.graphics.vector.ImageVector)

data class Ayah(
    val number: Int,
    val arabic: String,
    val pronunciation: String,
    val meaning: String,
    val words: List<Pair<String, String>>
)

private val demoAyahs = listOf(
    Ayah(
        1,
        "بِسْمِ اللَّهِ الرَّحْمَنِ الرَّحِيمِ",
        "বিসমিল্লাহির রাহমানির রাহিম",
        "পরম করুণাময়, অতি দয়ালু আল্লাহর নামে।",
        listOf("بِسْمِ" to "নামে", "اللَّهِ" to "আল্লাহর", "الرَّحْمَنِ" to "পরম করুণাময়", "الرَّحِيمِ" to "অতি দয়ালু")
    ),
    Ayah(
        2,
        "الْحَمْدُ لِلَّهِ رَبِّ الْعَالَمِينَ",
        "আলহামদু লিল্লাহি রব্বিল আলামীন",
        "সমস্ত প্রশংসা আল্লাহর, যিনি সকল জগতের প্রতিপালক।",
        listOf("الْحَمْدُ" to "সমস্ত প্রশংসা", "لِلَّهِ" to "আল্লাহর জন্য", "رَبِّ" to "প্রতিপালক", "الْعَالَمِينَ" to "সকল জগত")
    )
)

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent { QuranApp() }
    }
}

@Composable
fun QuranApp() {
    var screen by remember { mutableStateOf("home") }
    var selectedSurah by remember { mutableStateOf("সূরা আল-ফাতিহা") }

    MaterialTheme(
        colorScheme = lightColorScheme(
            primary = Green,
            secondary = Color(0xFFB78A28),
            background = Color(0xFFF7FAF7)
        )
    ) {
        Scaffold(
            containerColor = Color(0xFFF7FAF7),
            bottomBar = {
                NavigationBar {
                    NavigationBarItem(
                        selected = screen == "home",
                        onClick = { screen = "home" },
                        icon = { Icon(Icons.Default.Home, null) },
                        label = { Text("Home") }
                    )
                    NavigationBarItem(
                        selected = screen == "quran",
                        onClick = { screen = "quran" },
                        icon = { Icon(Icons.Default.MenuBook, null) },
                        label = { Text("কুরআন") }
                    )
                    NavigationBarItem(
                        selected = screen == "learn",
                        onClick = { screen = "learn" },
                        icon = { Icon(Icons.Default.School, null) },
                        label = { Text("শেখা") }
                    )
                    NavigationBarItem(
                        selected = screen == "bookmark",
                        onClick = { screen = "bookmark" },
                        icon = { Icon(Icons.Default.Bookmark, null) },
                        label = { Text("সংরক্ষিত") }
                    )
                    NavigationBarItem(
                        selected = screen == "profile",
                        onClick = { screen = "profile" },
                        icon = { Icon(Icons.Default.Person, null) },
                        label = { Text("প্রোফাইল") }
                    )
                }
            }
        ) { padding ->
            Box(Modifier.padding(padding)) {
                when (screen) {
                    "home" -> HomeScreen(
                        onMenu = { key ->
                            screen = key
                        }
                    )
                    "quran" -> QuranScreen(
                        selectedSurah = selectedSurah,
                        onSurah = { selectedSurah = it; screen = "surah" },
                        onBack = { screen = "home" }
                    )
                    "surah" -> SurahScreen(
                        surah = selectedSurah,
                        onBack = { screen = "quran" }
                    )
                    "learn" -> LearnScreen(onBack = { screen = "home" })
                    "bookmark" -> SimpleScreen("সংরক্ষিত", "আপনার Bookmark ও Notes এখানে থাকবে।", Icons.Default.Bookmark)
                    "profile" -> SimpleScreen("আমার Progress", "Lesson, Quiz Score ও Daily Streak এখানে দেখাবে।", Icons.Default.BarChart)
                    "tajweed" -> SimpleScreen("তাজবিদ শেখা", "মাখরাজ, মাদ, গুন্নাহ, ক্বলকলাহ, ইখফা, ইদগাম ইত্যাদি Lesson এখানে থাকবে।", Icons.Default.RecordVoiceOver)
                    "vocabulary" -> SimpleScreen("আরবি শব্দ শেখা", "Arabic শব্দ + উচ্চারণ + বাংলা অর্থ + Audio + Practice।", Icons.Default.Translate)
                    "grammar" -> SimpleScreen("আরবি Grammar", "Nahw, Sarf, Pronoun, Verb, Sentence Structure ইত্যাদি।", Icons.Default.MenuBook)
                    "speaking" -> SimpleScreen("আরবি কথা বলা", "দৈনন্দিন Arabic Conversation ও Speaking Practice।", Icons.Default.RecordVoiceOver)
                    "audio" -> SimpleScreen("অডিও প্র্যাকটিস", "শুনুন, Repeat করুন এবং নিজের পড়া Practice করুন।", Icons.Default.Headphones)
                    "quiz" -> QuizScreen()
                }
            }
        }
    }
}

@Composable
fun AppHeader(title: String, onBack: (() -> Unit)? = null) {
    Row(
        modifier = Modifier.fillMaxWidth().padding(horizontal = 20.dp, vertical = 16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        if (onBack != null) {
            IconButton(onClick = onBack) { Icon(Icons.Default.ArrowBack, "Back") }
        }
        Column(Modifier.weight(1f)) {
            Text(title, fontSize = 24.sp, fontWeight = FontWeight.Bold, color = Green)
        }
    }
}

@Composable
fun HomeScreen(onMenu: (String) -> Unit) {
    val menus = listOf(
        MenuItem("কুরআন শেখা", "সূরা, আয়াত, উচ্চারণ, পূর্ণ অর্থ ও শব্দে শব্দে অর্থ", Icons.Default.MenuBook),
        MenuItem("আরবি বর্ণমালা", "একদম শুরু থেকে Arabic Alphabet", Icons.Default.TextFields),
        MenuItem("আরবি শব্দ শেখা", "Quranic + দৈনন্দিন Vocabulary", Icons.Default.Translate),
        MenuItem("আরবি কথা বলা", "দৈনন্দিন Conversation Practice", Icons.Default.RecordVoiceOver),
        MenuItem("তাজবিদ শেখা", "শুদ্ধ তিলাওয়াতের নিয়ম", Icons.Default.RecordVoiceOver),
        MenuItem("আরবি Grammar", "Nahw, Sarf ও বাক্য গঠন", Icons.Default.MenuBook),
        MenuItem("অডিও প্র্যাকটিস", "শুনুন ও Repeat করে Practice করুন", Icons.Default.Headphones),
        MenuItem("কুইজ", "শেখার পর নিজেকে যাচাই করুন", Icons.Default.Quiz)
    )
    LazyColumn(
        modifier = Modifier.fillMaxSize().padding(horizontal = 18.dp),
        contentPadding = PaddingValues(bottom = 24.dp)
    ) {
        item {
            Column(Modifier.fillMaxWidth().padding(top = 22.dp, bottom = 16.dp)) {
                Text("কুরআন শিখতেই হবে", fontSize = 30.sp, fontWeight = FontWeight.ExtraBold, color = Green)
                Text("শুদ্ধভাবে পড়ুন • আরবি শিখুন • অর্থ বুঝুন", fontSize = 15.sp, color = Color.DarkGray)
            }
            Card(
                colors = CardDefaults.cardColors(containerColor = Green),
                shape = RoundedCornerShape(22.dp),
                modifier = Modifier.fillMaxWidth().padding(bottom = 18.dp)
            ) {
                Column(Modifier.padding(22.dp)) {
                    Text("আজকের লক্ষ্য", color = Color.White, fontSize = 14.sp)
                    Text("১টি Lesson সম্পূর্ণ করুন", color = Color.White, fontSize = 22.sp, fontWeight = FontWeight.Bold)
                    Spacer(Modifier.height(12.dp))
                    LinearProgressIndicator(progress = { 0.25f }, modifier = Modifier.fillMaxWidth())
                }
            }
        }
        items(menus) { item ->
            val key = when (item.title) {
                "কুরআন শেখা" -> "quran"
                "আরবি বর্ণমালা" -> "learn"
                "আরবি শব্দ শেখা" -> "vocabulary"
                "আরবি কথা বলা" -> "speaking"
                "তাজবিদ শেখা" -> "tajweed"
                "আরবি Grammar" -> "grammar"
                "অডিও প্র্যাকটিস" -> "audio"
                else -> "quiz"
            }
            Card(
                modifier = Modifier.fillMaxWidth().padding(vertical = 6.dp).clickable { onMenu(key) },
                shape = RoundedCornerShape(18.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                elevation = CardDefaults.cardElevation(2.dp)
            ) {
                Row(Modifier.padding(18.dp), verticalAlignment = Alignment.CenterVertically) {
                    Surface(shape = RoundedCornerShape(14.dp), color = LightGreen) {
                        Icon(item.icon, null, tint = Green, modifier = Modifier.padding(13.dp).size(28.dp))
                    }
                    Spacer(Modifier.width(16.dp))
                    Column(Modifier.weight(1f)) {
                        Text(item.title, fontSize = 18.sp, fontWeight = FontWeight.Bold)
                        Text(item.subtitle, fontSize = 13.sp, color = Color.Gray)
                    }
                    Icon(Icons.Default.ChevronRight, null, tint = Green)
                }
            }
        }
    }
}

@Composable
fun QuranScreen(selectedSurah: String, onSurah: (String) -> Unit, onBack: () -> Unit) {
    val surahs = listOf("সূরা আল-ফাতিহা", "সূরা আল-ইখলাস", "সূরা আল-ফালাক", "সূরা আন-নাস", "সূরা আল-কাওসার")
    Column(Modifier.fillMaxSize()) {
        AppHeader("কুরআন শেখা", onBack)
        OutlinedTextField(
            value = "",
            onValueChange = {},
            placeholder = { Text("সূরা খুঁজুন...") },
            leadingIcon = { Icon(Icons.Default.Search, null) },
            modifier = Modifier.fillMaxWidth().padding(horizontal = 18.dp)
        )
        LazyColumn(contentPadding = PaddingValues(18.dp)) {
            items(surahs) { surah ->
                Card(
                    modifier = Modifier.fillMaxWidth().padding(vertical = 5.dp).clickable { onSurah(surah) },
                    colors = CardDefaults.cardColors(containerColor = Color.White)
                ) {
                    Row(Modifier.padding(18.dp), verticalAlignment = Alignment.CenterVertically) {
                        Text("۝", color = Color(0xFFB78A28), fontSize = 25.sp)
                        Spacer(Modifier.width(14.dp))
                        Text(surah, fontSize = 18.sp, fontWeight = FontWeight.Bold, modifier = Modifier.weight(1f))
                        Icon(Icons.Default.ChevronRight, null, tint = Green)
                    }
                }
            }
        }
    }
}

@Composable
fun SurahScreen(surah: String, onBack: () -> Unit) {
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(bottom = 30.dp)
    ) {
        item { AppHeader(surah, onBack) }
        item {
            Text(
                "আরবি → বাংলা উচ্চারণ → পূর্ণ অর্থ → শব্দে শব্দে অর্থ",
                modifier = Modifier.padding(horizontal = 20.dp, vertical = 4.dp),
                color = Color.Gray,
                fontSize = 13.sp
            )
        }
        items(demoAyahs) { ayah ->
            AyahCard(ayah)
        }
    }
}

@Composable
fun AyahCard(ayah: Ayah) {
    Card(
        modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 8.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        shape = RoundedCornerShape(20.dp)
    ) {
        Column(Modifier.padding(20.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text("আয়াত ${ayah.number}", fontWeight = FontWeight.Bold, color = Green, modifier = Modifier.weight(1f))
                IconButton(onClick = {}) { Icon(Icons.Default.VolumeUp, "Audio") }
                IconButton(onClick = {}) { Icon(Icons.Default.BookmarkBorder, "Bookmark") }
            }
            Text(
                ayah.arabic,
                modifier = Modifier.fillMaxWidth().padding(vertical = 10.dp),
                textAlign = TextAlign.End,
                fontSize = 29.sp,
                lineHeight = 46.sp
            )
            HorizontalDivider()
            Text("উচ্চারণ", fontWeight = FontWeight.Bold, color = Green, modifier = Modifier.padding(top = 12.dp))
            Text(ayah.pronunciation, fontSize = 17.sp)
            Text("পূর্ণ অর্থ", fontWeight = FontWeight.Bold, color = Green, modifier = Modifier.padding(top = 12.dp))
            Text(ayah.meaning, fontSize = 16.sp)
            Text("শব্দে শব্দে অর্থ", fontWeight = FontWeight.Bold, color = Green, modifier = Modifier.padding(top = 16.dp, bottom = 8.dp))
            ayah.words.forEach { (arabic, meaning) ->
                Row(Modifier.fillMaxWidth().padding(vertical = 4.dp)) {
                    Text(arabic, fontSize = 22.sp, modifier = Modifier.weight(1f), textAlign = TextAlign.End)
                    Text("  —  $meaning", fontSize = 15.sp, modifier = Modifier.weight(1.5f))
                }
            }
        }
    }
}

@Composable
fun LearnScreen(onBack: () -> Unit) {
    val lessons = listOf(
        "Lesson 1 — আরবি হরফ পরিচিতি",
        "Lesson 2 — যবর, যের, পেশ",
        "Lesson 3 — তানভীন",
        "Lesson 4 — জযম",
        "Lesson 5 — তাশদীদ",
        "Lesson 6 — মাদ",
        "Lesson 7 — হরফ জোড়া দেওয়া"
    )
    Column(Modifier.fillMaxSize()) {
        AppHeader("আরবি বর্ণমালা", onBack)
        LazyColumn(contentPadding = PaddingValues(18.dp)) {
            item {
                Text("একদম শুরু থেকে ধাপে ধাপে", color = Color.Gray, modifier = Modifier.padding(bottom = 10.dp))
            }
            items(lessons) { lesson ->
                Card(
                    modifier = Modifier.fillMaxWidth().padding(vertical = 5.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White)
                ) {
                    Row(Modifier.padding(17.dp), verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.PlayCircle, null, tint = Green)
                        Spacer(Modifier.width(14.dp))
                        Text(lesson, fontWeight = FontWeight.SemiBold, modifier = Modifier.weight(1f))
                        Icon(Icons.Default.ChevronRight, null)
                    }
                }
            }
        }
    }
}

@Composable
fun QuizScreen() {
    var selected by remember { mutableStateOf<String?>(null) }
    Column(Modifier.fillMaxSize().padding(20.dp)) {
        AppHeader("কুইজ")
        Text("“رَبّ” শব্দের অর্থ কী?", fontSize = 24.sp, fontWeight = FontWeight.Bold)
        Spacer(Modifier.height(18.dp))
        listOf("প্রতিপালক", "আলো", "গ্রন্থ", "পথ").forEach { answer ->
            OutlinedButton(
                onClick = { selected = answer },
                modifier = Modifier.fillMaxWidth().padding(vertical = 5.dp)
            ) { Text(answer) }
        }
        selected?.let {
            Text(
                if (it == "প্রতিপালক") "সঠিক উত্তর! ✓" else "আবার চেষ্টা করুন।",
                color = Green,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(top = 18.dp)
            )
        }
    }
}

@Composable
fun SimpleScreen(title: String, description: String, icon: androidx.compose.ui.graphics.vector.ImageVector) {
    Column(Modifier.fillMaxSize().padding(20.dp), horizontalAlignment = Alignment.CenterHorizontally) {
        Spacer(Modifier.height(50.dp))
        Icon(icon, null, tint = Green, modifier = Modifier.size(70.dp))
        Spacer(Modifier.height(20.dp))
        Text(title, fontSize = 28.sp, fontWeight = FontWeight.Bold, color = Green)
        Spacer(Modifier.height(10.dp))
        Text(description, textAlign = TextAlign.Center, color = Color.Gray)
    }
}
