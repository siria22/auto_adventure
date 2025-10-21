package com.example.domain.scripts.guild

/**
 * 길드 랭크 (E0 ~ L4)
 * 총 35개의 고유한 랭크가 있으며, 각 랭크는 10개의 레벨을 포함합니다.
 */
enum class GuildRank {
    E0, E1, E2, E3, E4, // 랭크 0-4 (레벨 1-50)
    D0, D1, D2, D3, D4, // 랭크 5-9 (레벨 51-100)
    C0, C1, C2, C3, C4, // 랭크 10-14 (레벨 101-150)
    B0, B1, B2, B3, B4, // 랭크 15-19 (레벨 151-200)
    A0, A1, A2, A3, A4, // 랭크 20-24 (레벨 201-250)
    S0, S1, S2, S3, S4, // 랭크 25-29 (레벨 251-300)
    L0, L1, L2, L3, L4; // 랭크 30-34 (레벨 301-350)

    /** 'E', 'D', 'C' 등 메인 랭크 문자 */
    val mainRank: Char get() = this.name[0]

    /** 0-4 사이의 서브 레벨 숫자 */
    val subLevel: Int get() = this.name[1].digitToInt()

}
