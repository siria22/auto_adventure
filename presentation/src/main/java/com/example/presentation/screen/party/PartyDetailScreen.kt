package com.example.presentation.screen.party

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.domain.model.feature.actor.base.BaseActor
import com.example.domain.model.feature.party.Party
import com.example.domain.model.feature.party.PartyDetail
import com.example.domain.model.feature.party.PartyMemberDetail
import com.example.domain.model.feature.types.PartyPosition
import com.example.presentation.R
import com.example.presentation.component.theme.AutoAdventureTheme
import com.example.presentation.utils.error.ErrorDialog
import com.example.presentation.utils.error.ErrorDialogState
import com.example.presentation.utils.nav.safePopBackStack
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.MutableSharedFlow

@Composable
fun PartyDetailScreen(
    navController: NavController,
    argument: PartyDetailArgument,
    data: PartyData
) {
    var errorDialogState by remember { mutableStateOf(ErrorDialogState.idle()) }
    val coroutineScope: CoroutineScope = rememberCoroutineScope()
    var isAddMemberDialogVisible by remember { mutableStateOf(false) }
    var isSelectedSlotOccupied by remember { mutableStateOf(false) }

    LaunchedEffect(argument.event) {
        argument.event.collect { event ->
            when (event) {
                is PartyDetailEvent.DataFetch.Error -> {
                    errorDialogState = ErrorDialogState.fromErrorEvent(event)
                }

                is PartyDetailEvent.ShowAddMemberDialog -> {
                    isAddMemberDialogVisible = true
                }
            }
        }
    }

    Scaffold(
        topBar = {
            Surface(
                modifier = Modifier.fillMaxWidth(),
                color = Color(0xFFDEDEDE),
                shadowElevation = 4.dp
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 8.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    IconButton(onClick = { navController.safePopBackStack() }) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back",
                            tint = Color.Black,
                            modifier = Modifier.size(32.dp)
                        )
                    }
                    Text(
                        text = "파티 상세",
                        style = MaterialTheme.typography.titleLarge,
                        color = Color.Black,
                    )
                    IconButton(onClick = { /* TODO: Info action */ }) {
                        Icon(
                            imageVector = Icons.Default.Info,
                            contentDescription = "Info",
                            tint = Color.Black,
                            modifier = Modifier.size(32.dp)
                        )
                    }
                }
            }
        },
    ) { innerPadding ->
        Box(modifier = Modifier.padding(innerPadding)) {
            PartyDetailScreenContents(
                state = argument.state,
                data = data,
                intent = argument.intent,
                onClose = { navController.safePopBackStack() },
                onSlotClick = { slotIndex, isOccupied ->
                    isSelectedSlotOccupied = isOccupied
                    argument.intent(PartyDetailIntent.OnSlotClick(slotIndex))
                }
            )
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

    if (isAddMemberDialogVisible) {
        AddMemberDialog(
            availableActors = data.availableActors,
            showRemoveButton = isSelectedSlotOccupied,
            onDismiss = { isAddMemberDialogVisible = false },
            onSelect = { actor ->
                argument.intent(PartyDetailIntent.OnMemberSelected(actor.id))
                isAddMemberDialogVisible = false
            },
            onRemove = {
                argument.intent(PartyDetailIntent.OnRemoveMember)
                isAddMemberDialogVisible = false
            }
        )
    }
}

@Composable
private fun PartyDetailScreenContents(
    state: PartyDetailState,
    data: PartyData,
    intent: (PartyDetailIntent) -> Unit,
    onClose: () -> Unit,
    onSlotClick: (Int, Boolean) -> Unit
) {
    Box(modifier = Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(16.dp),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            if (data.partyDetail != null) {
                val partyDetail = data.partyDetail
                Column(modifier = Modifier.weight(1f, fill = false)) {
                    Text(
                        text = partyDetail.party.name,
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = Color.Black,
                        modifier = Modifier.padding(bottom = 16.dp)
                    )

                    Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
                        partyDetail.members.sortedBy { it.slotIndex }.forEach { member ->
                            PartyMemberItem(
                                member = member,
                                onClick = { onSlotClick(member.slotIndex, true) }
                            )
                        }

                        if (partyDetail.members.size < 4) {
                            val occupiedSlots = partyDetail.members.map { it.slotIndex }
                            val nextSlot = (0..3).firstOrNull { it !in occupiedSlots }

                            if (nextSlot != null) {
                                EmptyMemberSlot(onClick = { onSlotClick(nextSlot, false) })
                            }
                        }
                    }
                }

                Column(
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                    modifier = Modifier.padding(top = 16.dp)
                ) {
                    val actionButtonColor = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFF8B8B8B),
                        contentColor = Color.White
                    )
                    val closeButtonColor = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFF5C5C5C),
                        contentColor = Color.White
                    )
                    val buttonShape = RoundedCornerShape(4.dp)
                    val buttonModifier = Modifier
                        .fillMaxWidth()
                        .height(48.dp)

                    Button(
                        onClick = { /* TODO */ },
                        modifier = buttonModifier,
                        colors = actionButtonColor,
                        shape = buttonShape
                    ) {
                        Text("파티 인벤토리", fontWeight = FontWeight.Bold)
                    }
                    Button(
                        onClick = { /* TODO */ },
                        modifier = buttonModifier,
                        colors = actionButtonColor,
                        shape = buttonShape
                    ) {
                        Text("파티 액션", fontWeight = FontWeight.Bold)
                    }
                    Button(
                        onClick = { /* TODO */ },
                        modifier = buttonModifier,
                        colors = actionButtonColor,
                        shape = buttonShape
                    ) {
                        Text("퀘스트 부여", fontWeight = FontWeight.Bold)
                    }
                    Button(
                        onClick = onClose,
                        modifier = buttonModifier,
                        colors = closeButtonColor,
                        shape = buttonShape
                    ) {
                        Text("닫기", fontWeight = FontWeight.Bold)
                    }
                }
            }
        }

        if (state is PartyDetailState.OnProgress) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.White.copy(alpha = 0.5f)),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }
        }
    }
}

@Composable
fun PartyMemberItem(member: PartyMemberDetail, onClick: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() },
        verticalAlignment = Alignment.Top
    ) {
        Box {
            Image(
                painter = painterResource(id = R.drawable.ic_launcher_foreground),
                contentDescription = "Avatar",
                modifier = Modifier
                    .size(64.dp)
                    .clip(RoundedCornerShape(8.dp))
                    .background(Color.White)
                    .border(1.dp, Color(0xFFC8C8C8), RoundedCornerShape(8.dp))
            )
            if (member.isLeader) {
                Icon(
                    imageVector = Icons.Default.Star,
                    contentDescription = "Leader",
                    tint = Color(0xFFFFD700),
                    modifier = Modifier
                        .size(24.dp)
                        .align(Alignment.BottomEnd)
                        .offset(x = 6.dp, y = 6.dp)
                )
            }
        }

        Spacer(modifier = Modifier.width(16.dp))

        Column(verticalArrangement = Arrangement.spacedBy(2.dp)) {
            Text(
                text = member.name,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = Color.Black
            )
            val positionText = if (member.position == PartyPosition.FRONT) "전열" else "후열"
            Text(
                text = "${member.jobName}, $positionText",
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = FontWeight.Bold,
                color = Color.Black
            )
            Text(
                text = "HP: ${member.currentHp}/${member.maxHp}",
                style = MaterialTheme.typography.labelMedium,
                fontWeight = FontWeight.Bold,
                color = Color.Black
            )
            Text(
                text = "MP: ${member.currentMp}/${member.maxMp}",
                style = MaterialTheme.typography.labelMedium,
                fontWeight = FontWeight.Bold,
                color = Color.Black
            )
            Text(
                text = "LV : ${member.level}",
                style = MaterialTheme.typography.labelMedium,
                fontWeight = FontWeight.Bold,
                color = Color.Black
            )
        }
    }
}

@Composable
fun EmptyMemberSlot(onClick: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .padding(vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(64.dp)
                .border(1.dp, Color(0xFFAFAFAF), RoundedCornerShape(8.dp))
                .background(Color.Transparent),
            contentAlignment = Alignment.Center
        ) {

        }
        Spacer(modifier = Modifier.width(16.dp))
        Text(
            text = "+ 파티 멤버 추가",
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold,
            color = Color.Gray
        )
    }
}

@Composable
fun AddMemberDialog(
    availableActors: List<BaseActor>,
    showRemoveButton: Boolean,
    onDismiss: () -> Unit,
    onSelect: (BaseActor) -> Unit,
    onRemove: () -> Unit
) {
    Dialog(onDismissRequest = onDismiss) {
        Surface(
            shape = RoundedCornerShape(16.dp),
            color = Color.White,
            modifier = Modifier
                .fillMaxWidth()
                .height(400.dp)
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "동료 영입",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(bottom = 16.dp),
                    color = Color.Black
                )

                if (showRemoveButton) {
                    Button(
                        onClick = onRemove,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 8.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF5C5C5C)) // 빨간색
                    ) {
                        Text("이 멤버를 파티에서 제외", fontWeight = FontWeight.Bold, color = Color.White)
                    }
                    Spacer(modifier = Modifier.height(8.dp))
                }

                if (availableActors.isEmpty()) {
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .fillMaxWidth(),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "영입 가능한 모험가가 없습니다.",
                            color = Color.Gray
                        )
                    }
                } else {
                    LazyColumn(
                        modifier = Modifier.weight(1f),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        items(availableActors) { actor ->
                            ActorSelectionItem(actor = actor, onClick = { onSelect(actor) })
                        }
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                Button(
                    onClick = onDismiss,
                    modifier = Modifier.fillMaxWidth(),
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF5C5C5C))
                ) {
                    Text("닫기", fontWeight = FontWeight.Bold)
                }
            }
        }
    }
}

@Composable
fun ActorSelectionItem(actor: BaseActor, onClick: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() },
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFFF5F5F5))
    ) {
        Row(
            modifier = Modifier.padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(40.dp)
                    .clip(RoundedCornerShape(4.dp))
                    .background(Color.Gray)
            )
            Spacer(modifier = Modifier.width(12.dp))
            Column {
                Text(
                    text = actor.name,
                    fontWeight = FontWeight.Bold,
                    color = Color.Black
                )
                Text(
                    text = "${actor.actorCategory} | LV.1",
                    style = MaterialTheme.typography.bodySmall,
                    color = Color.DarkGray
                )
            }
        }
    }
}

@Preview(apiLevel = 34)
@Composable
private fun PartyDetailScreenPreview() {
    val dummyParty = Party(
        id = 1L,
        name = "제 1파티 - 행복전도사",
        isOnAdventure = false,
        adventureStartTime = null
    )

    val dummyMembers = listOf(
        PartyMemberDetail(
            characterId = 1L,
            name = "캐릭터 이름은 15자까지만.",
            jobName = "전사",
            level = 24,
            currentHp = 200,
            maxHp = 200,
            currentMp = 100,
            maxMp = 100,
            position = PartyPosition.FRONT,
            slotIndex = 0,
            isLeader = true
        ),
        PartyMemberDetail(
            characterId = 2L,
            name = "샤이 - ★",
            jobName = "아처",
            level = 24,
            currentHp = 200,
            maxHp = 200,
            currentMp = 100,
            maxMp = 100,
            position = PartyPosition.BACK,
            slotIndex = 1,
            isLeader = false
        )
    )

    val dummyDetail = PartyDetail(
        party = dummyParty,
        members = dummyMembers
    )

    val dummyArgument = PartyDetailArgument(
        state = PartyDetailState.Init,
        event = MutableSharedFlow(),
        intent = {}
    )

    val dummyData = PartyData(
        partyDetail = dummyDetail
    )

    AutoAdventureTheme {
        PartyDetailScreen(
            navController = rememberNavController(),
            argument = dummyArgument,
            data = dummyData
        )
    }
}