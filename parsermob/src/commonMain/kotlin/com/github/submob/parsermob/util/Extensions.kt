/*
 * Copyright (c) 2020 Mustafa Ozhan. All rights reserved.
 */

package com.github.submob.parsermob.util

import com.github.submob.parsermob.model.Operators

@Suppress("ReturnCount")
infix fun <T> String.isIn(operators: Array<T>): Boolean {
    for (operator in operators) {
        if (operator is Operators) {
            if (this == operator.sign) {
                return true
            }
        }
    }
    return false
}

infix fun <T> String.notIn(operators: Array<T>): Boolean {
    return !(this isIn operators)
}
