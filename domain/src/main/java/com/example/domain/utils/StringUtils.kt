package com.example.domain.utils

import java.util.Locale
import kotlin.math.abs
import kotlin.math.log10
import kotlin.math.pow

fun formatNumberAbbreviated(number: Long): String {
    // 1. -999 ~ 999 사이는 그대로 반환
    if (number > -1000 && number < 1000) {
        return number.toString()
    }

    // 2. 음수 처리를 위해 부호는 따로 저장하고 절대값으로 계산
    val isNegative = number < 0
    // Long.MIN_VALUE의 절대값은 Long 범위를 초과하므로 toDouble()을 먼저 사용
    val absNumber = abs(number.toDouble())

    // 3. 접미사 결정 (a, b, c, ...)
    // log10(숫자) / 3을 통해 1000의 거듭제곱 수를 구함 (1000 -> 1, 1,000,000 -> 2)
    val exponent = (log10(absNumber) / 3).toInt()
    val suffixIndex = exponent - 1

    // Long 타입은 최대 'f' (Quintillion) 정도까지 표현 가능
    val suffixes = "abcdefghijklmnopqrstuvwxyz"

    // 범위 초과 시 마지막 문자를 사용하도록 안전장치 마련
    val suffix = if (suffixIndex < suffixes.length) {
        suffixes[suffixIndex]
    } else {
        suffixes.last()
    }

    // 4. 표시할 숫자 계산
    // 1000의 거듭제곱으로 나눔
    val divisor = 1000.0.pow(exponent)
    val valueToDisplay = absNumber / divisor

    // 5. 최종 문자열 포맷팅
    // 소수점 첫째 자리 + 접미사 (예: "1.2a")
    // Locale.US를 사용하여 시스템 설정과 관계없이 항상 '.'를 소수점 구분자로 사용
    val formattedString = String.format(Locale.US, "%.1f%c", valueToDisplay, suffix)

    return if (isNegative) "-$formattedString" else formattedString
}