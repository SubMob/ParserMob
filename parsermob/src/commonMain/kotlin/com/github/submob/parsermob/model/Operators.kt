/*
 * Copyright (c) 2020 Mustafa Ozhan. All rights reserved.
 */

package com.github.submob.parsermob.model

@Suppress("MagicNumber")
enum class Operators(val sign: String, val precedence: Int) {
    PLUS("+", 2),
    MINUS("-", 2),
    MULTIPLY("x", 3),
    DIVISION("/", 4),
    UNARY("u", 6)
}
