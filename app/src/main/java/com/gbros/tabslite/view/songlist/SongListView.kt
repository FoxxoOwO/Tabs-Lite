package com.gbros.tabslite.view.songlist

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.gbros.tabslite.R
import com.gbros.tabslite.data.Preference
import com.gbros.tabslite.data.tab.TabWithDataPlaylistEntry
import com.gbros.tabslite.ui.theme.AppTheme
import kotlinx.coroutines.CoroutineScope

private const val LOG_NAME = "tabslite.SongListView  "

/**
 * The view including both the list of songs and the dropdown for sorting them
 */
@Composable
fun SongListView(
    modifier: Modifier = Modifier,
    liveSongs: LiveData<List<TabWithDataPlaylistEntry>>,
    navigateByPlaylistEntryId: Boolean = false,
    navigateToTabById: (id: Int) -> Unit,
    defaultSortValue: SortBy,
    liveSortByPreference: LiveData<Preference>,
    verticalArrangement: Arrangement.Vertical = Arrangement.spacedBy(4.dp),
    emptyListText: String = stringResource(id = R.string.message_empty_list),
    onSortPreferenceChange: CoroutineScope.(Preference) -> Unit
){
    val songs by liveSongs.observeAsState(listOf())
    val sortByPreference by liveSortByPreference.observeAsState(Preference("", defaultSortValue.name))
    val sortedSongs by remember(key1 = songs, key2 = sortByPreference) {
        mutableStateOf(
            when(SortBy.valueOf(sortByPreference.value)) {
                SortBy.Name -> songs.sortedBy { it.songName }
                SortBy.Popularity -> songs.sortedByDescending { it.votes }
                SortBy.ArtistName -> songs.sortedBy { it.artistName }
                SortBy.DateAdded -> songs.sortedByDescending { it.dateAdded }
            }
        )
    }

    var sortBySelection: SortBy? by remember { mutableStateOf(SortBy.valueOf(sortByPreference.value)) }  // for storing updated value between when the selection is updated and the database is updated
    Column {
        SortByDropdown(selectedSort = SortBy.valueOf(sortByPreference.value), onOptionSelected = {
            newSortBySelection -> sortBySelection = newSortBySelection
        })
        SongList(modifier = modifier, songs = sortedSongs, navigateToTabById = navigateToTabById, navigateByPlaylistEntryId = navigateByPlaylistEntryId, verticalArrangement = verticalArrangement, emptyListText = emptyListText)
    }

    // update sort by preference when selection changes
    LaunchedEffect(key1 = sortBySelection) {
        val currentSelection = sortBySelection
        if (currentSelection != null && sortByPreference.name != "") {
            onSortPreferenceChange(sortByPreference.copy(value = currentSelection.name))
        }
        sortBySelection = null
    }
}

@Composable @Preview
fun SongListViewPreview(){
    val tabForTest1 = TabWithDataPlaylistEntry(1, 1, 1, 1, 1, 1234, 0, "Long Time Ago", "CoolGuyz", false, 5, "Chords", "", 1, 4, 3.6, 1234, "" , 123, "public", 1, "E A D G B E", "description", false, "asdf", "", ArrayList(), ArrayList(), 4, "expert", playlistDateCreated = 12345, playlistDateModified = 12345, playlistDescription = "Description of our awesome playlist", playlistTitle = "My Playlist", playlistUserCreated = true, capo = 2, contributorUserName = "Joe Blow")
    val tabForTest2 = TabWithDataPlaylistEntry(1, 1, 1, 1, 1, 1234, 0, "Long Time Ago", "CoolGuyz", false, 5, "Chords", "", 1, 4, 3.6, 1234, "" , 123, "public", 1, "E A D G B E", "description", false, "asdf", "", ArrayList(), ArrayList(), 4, "expert", playlistDateCreated = 12345, playlistDateModified = 12345, playlistDescription = "Description of our awesome playlist", playlistTitle = "My Playlist", playlistUserCreated = true, capo = 2, contributorUserName = "Joe Blow")
    val tabListForTest = MutableLiveData(listOf(tabForTest1, tabForTest2))

    AppTheme {
        val liveSortByPreference = MutableLiveData(Preference("prefName", SortBy.Name.name))
        SongListView(liveSongs = tabListForTest, navigateToTabById = {}, defaultSortValue = SortBy.DateAdded, liveSortByPreference = liveSortByPreference, onSortPreferenceChange = { })
    }
}