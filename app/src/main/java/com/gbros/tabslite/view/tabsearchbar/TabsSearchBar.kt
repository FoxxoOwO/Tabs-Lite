package com.gbros.tabslite.view.tabsearchbar

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SearchBar
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.gbros.tabslite.R
import com.gbros.tabslite.ui.theme.AppTheme
import com.gbros.tabslite.utilities.UgApi

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TabsSearchBar(
    modifier: Modifier = Modifier,
    initialQueryText: String = "",
    leadingIcon: @Composable () -> Unit = { Icon(
        imageVector = ImageVector.vectorResource(id = R.drawable.ic_launcher_foreground),
        contentDescription = null,
        tint = MaterialTheme.colorScheme.onSurfaceVariant
    )},
    onSearch: (query: String) -> Unit
) {
    var query by remember { mutableStateOf(initialQueryText) }
    var active by remember { mutableStateOf(false) }
    val lazyColumnState = rememberLazyListState()
    var searchSuggestions: List<String> by remember { mutableStateOf(listOf()) }

    LaunchedEffect(key1 = query) {
        searchSuggestions = if (query.isNotEmpty()) {
            UgApi.searchSuggest(query)
        } else {
            listOf()
        }
    }

    SearchBar(
        query = query,
        onQueryChange = {query = it},
        onSearch = {q -> if(q.isNotBlank()) {onSearch(q)}},
        active = active,
        onActiveChange = {active = it},
        leadingIcon = leadingIcon,
        trailingIcon = {
            Icon(
                imageVector = Icons.Default.Search,
                contentDescription = stringResource(id = R.string.app_action_description_search)
            )
        },
        placeholder = {
            Text(text = stringResource(id = R.string.app_action_search))
        },
        modifier = modifier
    ) {
        LazyColumn(verticalArrangement = Arrangement.spacedBy(2.dp), state = lazyColumnState) {
            items(items = searchSuggestions) {searchSuggestion ->
                SearchSuggestion(suggestionText = searchSuggestion, modifier = Modifier.fillMaxWidth()) {
                    if (searchSuggestion.isNotBlank())
                        onSearch(searchSuggestion)
                }
            }
        }
    }
}

private fun isValidQuery(query: String): Boolean {
    return query.isNotBlank()
}

@Composable @Preview
fun TabsSearchBarPreview() {
    AppTheme {
        TabsSearchBar {}
    }
}