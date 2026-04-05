package com.example.presentation.screen.adventure

import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
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
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
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
import java.util.Date

@Composable
fun AdventureScreen(
    navController: NavController,
    argument: AdventureArgument,
    data: List<PartyDetail>
) {
    var errorDialogState by remember { mutableStateOf(ErrorDialogState.idle()) }
    val coroutineScope: CoroutineScope = rememberCoroutineScope()
    val context = LocalContext.current

    LaunchedEffect(argument.event) {
        argument.event.collect { event ->
            when (event) {
                is AdventureEvent.DataFetch.Error -> {
                    errorDialogState = ErrorDialogState.fromErrorEvent(event)
                }

                is AdventureEvent.ShowToast -> {
                    Toast.makeText(context, event.message, Toast.LENGTH_SHORT).show()
                }

                // [추가] 나머지 이벤트(네비게이션 등)는 무시
                else -> {}
            }
        }
    }

    Scaffold(
        topBar = {
            Surface(
                modifier = Modifier.fillMaxWidth(),
                color = Color(0xFFDEDEDE), // 다른 화면과 동일한 배경색
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
                        text = "모험",
                        style = MaterialTheme.typography.titleLarge,
                        color = Color.Black,
                    )
                    IconButton(onClick = { /* TODO: Info dialog or action */ }) {
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
            AdventureScreenContents(
                partyList = data,
                onPartyClick = { partyId ->
                    argument.intent(AdventureIntent.OnPartyClick(partyId))
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
}

@Composable
private fun AdventureScreenContents(
    partyList: List<PartyDetail>,
    onPartyClick: (Long) -> Unit
) {
    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        // 1. 상단 지도 영역 (화면의 약 35%)
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .weight(0.35f)
                .background(Color.Gray) // 로딩 전 배경색
        ) {
            Image(
                painter = painterResource(id = R.drawable.bg_guild), // TODO: 지도 이미지 리소스로 교체 필요
                contentDescription = "World Map",
                contentScale = ContentScale.Crop,
                modifier = Modifier.fillMaxSize()
            )
        }

        // 2. 하단 파티 리스트 영역
        LazyColumn(
            modifier = Modifier
                .fillMaxWidth()
                .weight(0.65f)
                .background(Color.White),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            item {
                Text(
                    text = "파티 목록",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = Color.Black
                )
            }

            items(partyList) { partyDetail ->
                AdventurePartyItem(
                    partyDetail = partyDetail,
                    onClick = { onPartyClick(partyDetail.party.id) }
                )
            }

            // 리스트 하단 여백
            item { Spacer(modifier = Modifier.height(16.dp)) }
        }
    }
}

@Composable
fun AdventurePartyItem(
    partyDetail: PartyDetail,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() },
        shape = RoundedCornerShape(8.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color.White
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        border = androidx.compose.foundation.BorderStroke(
            1.dp,
            Color(0xFFEEEEEE)
        ) // 카드 외곽선 추가 (선택사항)
    ) {
        Column(
            modifier = Modifier.padding(12.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            // 파티 이름
            Text(
                text = partyDetail.party.name,
                style = MaterialTheme.typography.titleSmall,
                fontWeight = FontWeight.Bold,
                color = Color.Black
            )

            // 멤버 아바타 리스트
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                // 최대 4슬롯 고정
                for (i in 0 until 4) {
                    val member = partyDetail.members.getOrNull(i)
                    if (member != null) {
                        // 멤버 존재 시 아바타 표시
                        Image(
                            painter = painterResource(id = R.drawable.ic_launcher_foreground), // TODO: 캐릭터별 이미지 매핑
                            contentDescription = member.name,
                            contentScale = ContentScale.Crop,
                            modifier = Modifier
                                .size(56.dp) // PartyDetailScreen의 64dp보다 약간 작게 조정 (한 줄에 4개 넣기 위함)
                                .clip(RoundedCornerShape(8.dp))
                                .background(Color.White)
                                .border(
                                    1.dp,
                                    Color(0xFFC8C8C8),
                                    RoundedCornerShape(8.dp)
                                ) // PartyDetailScreen과 동일한 색상
                        )
                    } else {
                        // 빈 슬롯 표시
                        Box(
                            modifier = Modifier
                                .size(56.dp)
                                .clip(RoundedCornerShape(8.dp))
                                .background(Color(0xFFFAFAFA)) // 약간 더 밝은 회색
                                .border(
                                    1.dp,
                                    Color(0xFFAFAFAF),
                                    RoundedCornerShape(8.dp)
                                ) // EmptyMemberSlot과 동일한 색상
                        )
                    }
                }
            }

            // 상태 표시
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color(0xFFF0F0F0), RoundedCornerShape(4.dp))
                    .padding(vertical = 8.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = if (partyDetail.party.isOnAdventure) "모험 중..." else "— 휴식 중... —",
                    style = MaterialTheme.typography.bodyMedium,
                    color = if (partyDetail.party.isOnAdventure) Color.Red else Color.Gray, // 모험 중일 때 색상 강조
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}

@Preview(apiLevel = 34)
@Composable
private fun AdventureScreenPreview() {
    // 더미 데이터 생성
    val dummyParty1 = Party(
        id = 1L,
        name = "제 1파티 - 행복전도사",
        isOnAdventure = false,
        adventureStartTime = null
    )

    val dummyMembers1 = listOf(
        PartyMemberDetail(1L, "전사1", "전사", 24, 200, 200, 100, 100, PartyPosition.FRONT, 0, true),
        PartyMemberDetail(2L, "아처1", "아처", 24, 200, 200, 100, 100, PartyPosition.BACK, 1, false),
        PartyMemberDetail(3L, "법사1", "마법사", 24, 150, 150, 200, 200, PartyPosition.BACK, 2, false)
    )

    val dummyParty2 = Party(
        id = 2L,
        name = "제 2파티 - 모험왕",
        isOnAdventure = true,
        adventureStartTime = Date()
    )

    val dummyMembers2 = listOf(
        PartyMemberDetail(4L, "기사1", "기사", 30, 300, 300, 50, 50, PartyPosition.FRONT, 0, true)
    )

    val dummyList = listOf(
        PartyDetail(dummyParty1, dummyMembers1),
        PartyDetail(dummyParty2, dummyMembers2),
        PartyDetail(dummyParty1.copy(id = 3L, name = "제 3파티 - 휴식중"), emptyList())
    )

    AutoAdventureTheme {
        AdventureScreen(
            navController = rememberNavController(),
            argument = AdventureArgument(
                intent = { },
                state = AdventureState.Init,
                event = MutableSharedFlow()
            ),
            data = dummyList
        )
    }
}