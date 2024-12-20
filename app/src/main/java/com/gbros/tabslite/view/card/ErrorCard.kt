package com.gbros.tabslite.view.card

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.gbros.tabslite.R
import com.gbros.tabslite.ui.theme.AppTheme

@Composable
fun ErrorCard(text: String) {
    GenericInformationCard(
        text = text,
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.errorContainer),
        icon = Icons.Default.Warning,
        iconContentDescription = stringResource(id = R.string.error)
    )
}

@Composable @Preview
private fun ErrorCardPreview() {
    AppTheme {
        ErrorCard(text = "Error!  Something bad happened and now we need to show this message.")
    }
}