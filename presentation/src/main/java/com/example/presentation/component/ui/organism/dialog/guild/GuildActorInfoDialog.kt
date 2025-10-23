package com.example.presentation.component.ui.organism.dialog.guild

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight.Companion.W900
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.domain.model.feature.actor.actor.Actor
import com.example.domain.model.feature.actor.actor.ActorInfo
import com.example.domain.model.feature.actor.actor.ActorSkills
import com.example.domain.model.feature.mockActor
import com.example.presentation.component.theme.AutoAdventureColorScheme
import com.example.presentation.component.theme.AutoAdventureTheme
import com.example.presentation.component.ui.DefaultRoundedCorner
import com.example.presentation.component.ui.SmallRoundedCorner
import com.example.presentation.component.ui.atom.BasicButton
import com.example.presentation.component.ui.atom.BasicDialog
import com.example.presentation.component.ui.atom.BasicImageBox
import com.example.presentation.component.ui.atom.ButtonType

@Composable
fun GuildActorInfoDialog(
    actor: Actor,
    onDismissButtonClicked: () -> Unit
) {
    BasicDialog(
        backHandler = onDismissButtonClicked
    ) {
        Column(
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Text(
                text = "용병 정보",
                style = MaterialTheme.typography.titleMedium,
                color = AutoAdventureColorScheme.commonText,
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth()
            )

            ActorProfile(actor)

            EquippedSkillInfo(actor.skills.filter { it.isEquipped })

            Row(
                horizontalArrangement = Arrangement.spacedBy(16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                BasicButton(
                    text = "닫기",
                    type = ButtonType.SECONDARY,
                    onClicked = onDismissButtonClicked,
                    modifier = Modifier.weight(1f)
                )

                BasicButton(
                    text = "영입",
                    type = ButtonType.PRIMARY,
                    onClicked = { /* TODO : 캐릭터 영입 로직 */ },
                    modifier = Modifier.weight(1f)
                )
            }
        }
    }
}

@Composable
private fun ActorProfile(actor: Actor) {
    Column(
        verticalArrangement = Arrangement.spacedBy(14.dp),
        modifier = Modifier
            .background(
                color = AutoAdventureColorScheme.surface,
                shape = DefaultRoundedCorner
            )
            .padding(16.dp)
            .fillMaxWidth()
    ) {
        ActorInfo(actorInfo = actor.info)

        Text(
            text = "능력치",
            style = MaterialTheme.typography.labelLarge,
            color = AutoAdventureColorScheme.commonText
        )

        ActorAttributesInfo(actor)
    }
}

@Composable
private fun ActorInfo(actorInfo: ActorInfo) {
    Row(
        horizontalArrangement = Arrangement.spacedBy(12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        BasicImageBox(
            size = 100.dp,
            galleryUri = null /* TODO : Actor profile image */
        )
        val actorInfoText =
            "${actorInfo.race}, ${actorInfo.job.nameKor}, ${actorInfo.personality.name}"
        Column(
            verticalArrangement = Arrangement.spacedBy(4.dp),
            horizontalAlignment = Alignment.Start,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(
                text = actorInfo.name,
                style = MaterialTheme.typography.labelLarge.copy(fontWeight = W900),
                color = AutoAdventureColorScheme.commonText
            )
            Text(
                text = actorInfoText,
                style = MaterialTheme.typography.labelMedium,
                color = AutoAdventureColorScheme.commonText
            )
            Text(
                text = "Lv. ${actorInfo.level}", /* TODO : 다음 레벨까지 경험치 표시 여부 */
                style = MaterialTheme.typography.labelMedium,
                color = AutoAdventureColorScheme.commonText
            )
            Text(
                text = "등급: ${actorInfo.rank}",
                style = MaterialTheme.typography.labelMedium,
                color = AutoAdventureColorScheme.commonText
            )
        }
    }
}

@Composable
private fun ActorAttributesInfo(
    actor: Actor
) {
    Row(
        horizontalArrangement = Arrangement.spacedBy(4.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        AttributeCard(
            modifier = Modifier.weight(1f),
            mainAttribute = "힘",
            mainAttributeValue = actor.stats.strength,
            firstAttribute = "체력",
            firstAttributeValue = actor.attribute.maxHp,
            secondAttribute = "공격",
            secondAttributeValue = actor.attribute.attack
        )
        AttributeCard(
            modifier = Modifier.weight(1f),
            mainAttribute = "민첩",
            mainAttributeValue = actor.stats.agility,
            firstAttribute = "속도",
            firstAttributeValue = actor.attribute.speed,
            secondAttribute = "회피",
            secondAttributeValue = actor.attribute.evasion
        )
        AttributeCard(
            modifier = Modifier.weight(1f),
            mainAttribute = "지능",
            mainAttributeValue = actor.stats.intelligence,
            firstAttribute = "마력",
            firstAttributeValue = actor.attribute.magic,
            secondAttribute = "마나",
            secondAttributeValue = actor.attribute.maxHp
        )
        AttributeCard(
            modifier = Modifier.weight(1f),
            mainAttribute = "운",
            mainAttributeValue = actor.stats.luck,
            firstAttribute = "행운",
            firstAttributeValue = actor.attribute.fortune,
            secondAttribute = "명중",
            secondAttributeValue = actor.attribute.accuracy
        )
    }
}

@Composable
private fun AttributeCard(
    modifier: Modifier,
    mainAttribute: String,
    mainAttributeValue: Long,
    firstAttribute: String,
    firstAttributeValue: Long,
    secondAttribute: String,
    secondAttributeValue: Long,
) {
    Column(
        modifier = modifier
            .background(
                /* TODO : 색상 -> card, surface 분리 */
                // AutoAdventureColorScheme.surface,
                Color.White,
                SmallRoundedCorner
            )
            .padding(4.dp),
        verticalArrangement = Arrangement.spacedBy(4.dp),
    ) {
        Text(
            text = "$mainAttribute - $mainAttributeValue",
            style = MaterialTheme.typography.labelSmall,
            color = AutoAdventureColorScheme.commonText
        )
        Text(
            text = "$firstAttribute : $firstAttributeValue",
            style = MaterialTheme.typography.labelSmall.copy(fontSize = 10.sp),
            color = AutoAdventureColorScheme.commonText,
            modifier = Modifier.padding(start = 4.dp)
        )
        Text(
            text = "$secondAttribute : $secondAttributeValue",
            style = MaterialTheme.typography.labelSmall.copy(fontSize = 10.sp),
            color = AutoAdventureColorScheme.commonText,
            modifier = Modifier.padding(start = 4.dp)
        )
    }
}

@Composable
private fun EquippedSkillInfo(skills: List<ActorSkills>) {
    /* TODO : 캐릭터가 가지고 있을 수 있는 착용 스킬 수 파악 후, 잠금 여부 확인 */
    val equippedSkill = (skills + List(4) { ActorSkills.empty() }).take(4)

    Column(
        verticalArrangement = Arrangement.spacedBy(8.dp),
        modifier = Modifier
            .background(
                color = AutoAdventureColorScheme.surface,
                shape = DefaultRoundedCorner
            )
            .padding(16.dp)
            .fillMaxWidth()
    ) {
        Text(
            text = "스킬",
            style = MaterialTheme.typography.labelLarge,
            color = AutoAdventureColorScheme.commonText
        )
        Row(
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            equippedSkill.forEach { skill ->
                SkillCard(modifier = Modifier.weight(1f), skill = skill)
            }
        }

        BasicButton(
            text = "전체 스킬 보기",
            type = ButtonType.PRIMARY,
            onClicked = { /* TODO : 전체 스킬 보기 */ }
        )
    }
}

@Composable
private fun SkillCard(
    modifier: Modifier,
    skill: ActorSkills
) {
    Column(
        modifier = modifier.background(
            /* TODO : 색상 -> card, surface 분리 */
            AutoAdventureColorScheme.surface,
            SmallRoundedCorner
        ),
        verticalArrangement = Arrangement.spacedBy(8.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        BasicImageBox(
            size = 50.dp,
            galleryUri = null /* TODO : skill Image */
        )
        Text(
            text = skill.baseSkill.name,
            style = MaterialTheme.typography.labelSmall,
            color = AutoAdventureColorScheme.commonText
        )
        Text(
            text = "(+${skill.reinforcementLevel})",
            style = MaterialTheme.typography.labelSmall.copy(fontSize = 10.sp),
            color = AutoAdventureColorScheme.commonText
        )
    }
}

@Preview(apiLevel = 34)
@Composable
private fun GuildActorInfoDialogPreview() {
    AutoAdventureTheme {
        GuildActorInfoDialog(
            actor = mockActor,
            onDismissButtonClicked = {}
        )
    }
}