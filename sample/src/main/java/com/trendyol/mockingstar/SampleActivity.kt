package com.trendyol.mockingstar

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.ExitToApp
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.trendyol.mockingstar.ui.theme.MockingStarAndroidTheme

class SampleActivity : ComponentActivity() {
	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		val viewModel: SampleViewModel by viewModels()
		setContent {
			MockingStarAndroidTheme {
				SampleScreen(viewModel = viewModel)
			}
		}
	}
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SampleScreen(viewModel: SampleViewModel) {
	val inputText = viewModel.inputText.collectAsStateWithLifecycle()
	val searchResults = viewModel.searchResults.collectAsStateWithLifecycle()
	val mockingEnabled = viewModel.mockingEnabled.collectAsStateWithLifecycle()
	Scaffold(
		topBar = {
			TopAppBar(title = {
				Row(
					horizontalArrangement = Arrangement.SpaceBetween,
					verticalAlignment = Alignment.CenterVertically,
					modifier = Modifier.fillMaxWidth()
				) {
					Text("Github Search", fontWeight = FontWeight.Bold)
					Button(
						onClick = { viewModel.toggleMocking() },
						modifier = Modifier.padding(end = 12.dp)
					) {
						val text = if (mockingEnabled.value) "Disable" else "Enable"
						Text("$text Mockingstar")
					}
				}
			})
		}
	) { padding ->
		Column(
			modifier = Modifier
				.padding(padding)
				.fillMaxSize()
		) {
			Row(
				modifier = Modifier.fillMaxWidth(),
				horizontalArrangement = Arrangement.SpaceEvenly,
				verticalAlignment = Alignment.CenterVertically
			) {
				TextField(
					modifier = Modifier,
					shape = RoundedCornerShape(12.dp),
					colors = TextFieldDefaults.colors(
						focusedIndicatorColor = Color.Transparent,
						unfocusedIndicatorColor = Color.Transparent
					),
					value = inputText.value,
					onValueChange = viewModel::updateInput,
					trailingIcon = {
						if (inputText.value.isNotEmpty()) {
							Icon(
								imageVector = Icons.Default.Clear,
								contentDescription = "Clear text",
								modifier = Modifier.clickable { viewModel.updateInput("") }
							)
						}
					}
				)
				Button(onClick = { viewModel.search() }) {
					Text("Search")
				}
			}
			LazyColumn(
				modifier = Modifier,
				contentPadding = PaddingValues(8.dp),
				verticalArrangement = Arrangement.spacedBy(8.dp),
				horizontalAlignment = Alignment.CenterHorizontally
			) {
				items(
					items = searchResults.value,
					key = { it.name }) { result -> RepositoryCard(repository = result) }

			}
		}
	}
}

@Composable
fun RepositoryCard(
	modifier: Modifier = Modifier,
	repository: Repository
) {
	Column(
		modifier = modifier
			.fillMaxWidth()
			.border(1.dp, Color.Gray, shape = RoundedCornerShape(12.dp))
			.padding(8.dp)
	) {
		Text(repository.name, modifier = Modifier.padding(4.dp))
		Row(modifier = Modifier.padding(top = 6.dp)) {
			Row(modifier = Modifier.padding(end = 6.dp)) {
				Icon(Icons.Default.ExitToApp, contentDescription = null)
				Text(repository.forkCount.toString())
			}
			Row {
				Icon(Icons.Default.Star, contentDescription = null)
				Text(repository.starGazerCount.toString())
			}
		}
		if (repository.description?.isNotEmpty() == true) {
			Text(repository.description, modifier = Modifier.padding(top = 6.dp))
		}
	}
}
