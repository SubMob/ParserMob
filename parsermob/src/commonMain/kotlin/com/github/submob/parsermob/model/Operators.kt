/*
 * Copyright (c) 2020 Mustafa Ozhan. All rights reserved.
 */

package com.github.submob.parsermob.model

enum class Operators(val sign: String, val precedence: Int) {
    PLUS("+", precedence = 2),
    MINUS("-", precedence = 2),
    MULTIPLY("x", precedence = 3),
    DIVISION("/", precedence = 4),
    UNARY("u", precedence = 6)
}
