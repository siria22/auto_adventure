package com.example.presentation.component.test

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowLeft
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.domain.model.nonfeature.log.DomainLog
import com.example.presentation.component.theme.AutoAdventureColorScheme
import com.example.presentation.component.ui.SmallRoundedCorner
import com.example.presentation.component.ui.organism.AppTopBar
import com.example.presentation.component.ui.organism.TopBarInfo
import com.example.presentation.utils.error.ErrorDialog
import com.example.presentation.utils.error.ErrorDialogState

@Composable
fun TestLogDialog(
    onDismissButtonClicked: () -> Unit
) {

    var errorDialogState by remember { mutableStateOf(ErrorDialogState.idle()) }

    val viewModel: TestLogViewModel = hiltViewModel()
    val logs = viewModel.logs.collectAsStateWithLifecycle()

    val event = viewModel.eventFlow
    val intent = viewModel::onIntent

    Scaffold(
        topBar = {
            AppTopBar(
                topBarInfo = TopBarInfo(
                    text = "TestLogDialog",
                    isLeadingIconAvailable = true,
                    onLeadingIconClicked = { onDismissButtonClicked() },
                    leadingIconResource = Icons.AutoMirrored.Filled.KeyboardArrowLeft,
                    isTrailingIconAvailable = true,
                    onTrailingIconClicked = { intent(TestLogIntent.Refresh) },
                    trailingIconResource = Icons.Filled.Refresh
                ),
            )
        },
        containerColor = AutoAdventureColorScheme.background,
        contentColor = AutoAdventureColorScheme.background,
    ) { innerPadding ->
        Box(modifier = Modifier.padding(innerPadding)) {
            TestLogContent(logs = logs.value)
        }
    }

    LaunchedEffect(event) {
        event.collect { event ->
            when (event) {
                else -> {}
            }
        }
    }

    if (errorDialogState.isErrorDialogVisible) {
        ErrorDialog(
            errorDialogState = errorDialogState,
            errorHandler = {
                errorDialogState = errorDialogState.toggleVisibility()
            }
        )
    }
}


@Composable
private fun TestLogContent(logs: List<DomainLog>) {

    LazyColumn(modifier = Modifier.padding(16.dp)) {
        item {
            Text(
                text = "Logs",
                color = AutoAdventureColorScheme.commonText,
                style = MaterialTheme.typography.labelLarge,
                modifier = Modifier.padding(bottom = 4.dp)
            )
        }
        items(
            items = logs,
            key = { it.id }
        ) {
            TestLogItem(log = it)
        }
    }
}

@Composable
private fun TestLogItem(log: DomainLog) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .fillMaxWidth()
            .background(
                color = AutoAdventureColorScheme.surface,
                shape = SmallRoundedCorner
            )
            .padding(vertical = 8.dp)
    ) {
        Text(
            text = "id: ${log.id} - ${log.title} (${log.category.name})",
            color = AutoAdventureColorScheme.commonText,
            style = MaterialTheme.typography.labelMedium,
            modifier = Modifier.padding(vertical = 2.dp)
        )
        Text(
            text = "- ${log.description}",
            color = AutoAdventureColorScheme.commonText,
            style = MaterialTheme.typography.labelSmall,
            modifier = Modifier.padding(vertical = 2.dp)
        )
        Text(
            text = "Time = ${log.revealTime}, parentId = ${log.parentId}",
            color = AutoAdventureColorScheme.commonText,
            style = MaterialTheme.typography.labelSmall,
            modifier = Modifier.padding(vertical = 2.dp)
        )
    }
}