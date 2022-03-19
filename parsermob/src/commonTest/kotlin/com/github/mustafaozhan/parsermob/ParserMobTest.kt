/*
 * Copyright (c) 2020 Mustafa Ozhan. All rights reserved.
 */

package com.github.mustafaozhan.parsermob

import com.github.submob.parsermob.ParserMob
import kotlin.test.Test
import kotlin.test.assertEquals

@Suppress("TooManyFunctions")
class ParserMobTest {
    private val parser = ParserMob()

    @Test
    fun addition() = assertEquals(4.0, parser.calculate("1+3"))

    @Test
    fun additionWithMultiply() = assertEquals(10.0, parser.calculate("1+3*3"))

    @Test
    fun additionWithMultiplyParenthesis() = assertEquals(12.0, parser.calculate("(1+3)*3"))

    @Test
    fun percentage() = assertEquals(40.0, parser.calculate("200%20"))

    @Test
    fun orderOfParenthesis() = assertEquals(60.0, parser.calculate("20+200%20"))

    @Test
    fun subtraction() = assertEquals(-2.0, parser.calculate("1-3"))

    @Test
    fun division() = assertEquals(4 / 5.0, parser.calculate("4/5"))

    @Test
    fun multiply() = assertEquals(4 * 5.0, parser.calculate("4*5"))

    @Test
    fun multipleSubtraction() = assertEquals(-8.0, parser.calculate("10-9-9"))

    @Test
    fun unaryMinus() = assertEquals(-9.0, parser.calculate("-9"))

    @Test
    fun negativeAddition() = assertEquals(-18.6, parser.calculate("-9-9.6"))

    @Test
    fun positiveDivisionByZero() = assertEquals(Double.POSITIVE_INFINITY, parser.calculate("9/0"))

    @Test
    fun negativeDivisionByZero() = assertEquals(
        Double.NEGATIVE_INFINITY,
        parser.calculate("-9.0/0")
    )

    @Test
    fun checkIfFirstDigitAcceptable() {
        assertEquals(Double.NaN, parser.calculate(")"))
        assertEquals(Double.NaN, parser.calculate("/"))
        assertEquals(Double.NaN, parser.calculate("%"))
        assertEquals(Double.NaN, parser.calculate("*"))
    }

    @Test
    fun multipleDivision() = assertEquals(1.0, parser.calculate("20/10/2"))

    @Test
    fun precision2() = assertEquals(0.33, parser.calculate("1/3", 2))

    @Test
    fun precision4() = assertEquals(0.3333, parser.calculate("1/3", 4))

    @Test
    fun precision2WithRound() = assertEquals(0.67, parser.calculate("2/3", 2))

    @Test
    fun precision4WithRound() = assertEquals(0.6667, parser.calculate("2/3", 4))

    @Test
    fun random() = assertEquals(192.0, parser.calculate("1.6/0.05-6.4/0.16+20/0.1"))

    @Test
    fun random2() = assertEquals(5.121, parser.calculate("1.3x2.2+1.33x1.7"))

    @Test
    fun random3() = assertEquals(386.0, parser.calculate("9/45x5*8+9x42"))

    @Test
    fun random4() = assertEquals(-3.8, parser.calculate("1+3.2-2x4"))

    @Test
    fun random5() = assertEquals(75.0, parser.calculate("6x2+3.2/0.2-2x4+120/2-5"))
}
