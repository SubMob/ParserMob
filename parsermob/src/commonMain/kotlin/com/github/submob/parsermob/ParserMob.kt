/*
 * Copyright (c) 2021 Mustafa Ozhan. All rights reserved.
 */

package com.github.submob.parsermob

import com.github.submob.parsermob.error.BadFormatException
import com.github.submob.parsermob.model.Operators
import com.github.submob.parsermob.util.Stack
import com.github.submob.parsermob.util.isIn
import com.github.submob.parsermob.util.notIn
import kotlin.math.pow
import kotlin.math.round

class ParserMob {

    private val numStack = Stack<Double>()
    private val opStack = Stack<String>()

    @Suppress("SwallowedException")
    fun calculate(
        expression: String,
        precision: Int = 3
    ) = try {
        val uExpression = convertToUExpression(expression.replace("%", "/100*"))
        val res = evaluateExpression(uExpression)
        roundToPrecision(res, precision)
    } catch (e: BadFormatException) {
        Double.NaN
    } catch (e: NumberFormatException) {
        Double.NaN
    }

    @Suppress("NestedBlockDepth")
    private fun convertToUExpression(expression: String): String {
        val sb = StringBuilder()
        for (i in expression.indices) {
            val currChar = expression[i]
            if (currChar.toString() == Operators.MINUS.sign) {
                if (i == 0) {
                    sb.append('u')
                } else {
                    val prevChar = expression[i - 1]
                    if (prevChar in "+*/(") {
                        sb.append('u')
                    } else {
                        sb.append(currChar)
                    }
                }
            } else {
                sb.append(currChar)
            }
        }
        return sb.toString()
    }

    @Suppress("MagicNumber")
    private fun roundToPrecision(value: Double, precision: Int = 3): Double {
        val corrector = 10.0.pow(precision).toInt()
        var result = round(value * corrector) / corrector
        if (result == -0.0) {
            result = 0.0
        }
        return result
    }

    @Suppress("TooGenericExceptionCaught")
    private fun computeNormalOperation(op: String) {
        try {
            when (op) {
                Operators.PLUS.sign -> {
                    val num0 = numStack.pop()
                    val num1 = numStack.pop()
                    numStack.push(num1 + num0)
                }
                Operators.MINUS.sign -> {
                    val num0 = numStack.pop()
                    val num1 = numStack.pop()
                    numStack.push(num1 - num0)
                }
                Operators.MULTIPLY.sign -> {
                    val num0 = numStack.pop()
                    val num1 = numStack.pop()
                    numStack.push(num1 * num0)
                }
                Operators.DIVISION.sign -> {
                    val num0 = numStack.pop()
                    val num1 = numStack.pop()
                    numStack.push(num1 / num0)
                }
                Operators.UNARY.sign -> {
                    val num0 = numStack.pop()
                    numStack.push(-1.0 * num0)
                }
            }
        } catch (e: IndexOutOfBoundsException) {
            clearStacks()
            throw BadFormatException(e)
        } catch (e: ArithmeticException) {
            clearStacks()
            throw BadFormatException(e)
        }
    }

    @Suppress("NestedBlockDepth", "TooGenericExceptionCaught", "ComplexMethod")
    private fun evaluateExpression(expression: String): Double {
        var i = 0
        val numString = StringBuilder()
        while (i < expression.length) {
            val currChar = expression[i]

            if (i == 0 && currChar in ")/%*") {
                throw BadFormatException()
            }

            when {
                currChar in "0123456789." -> {
                    if (i != 0 && (expression[i - 1] == ')')) {
                        performSafePushToStack(numString, "*")
                    }
                    numString.append(currChar)
                    i++
                }
                currChar.toString() isIn Operators.values() || currChar == '(' -> {
                    if (currChar == '(') {
                        // check for implicit multiply
                        if (i != 0 && expression[i - 1].toString() notIn Operators.values()) {
                            performSafePushToStack(numString, "*")
                        }
                        opStack.push("(")
                    } else {
                        performSafePushToStack(numString, currChar.toString())
                    }

                    i++
                }
                currChar == ')' -> {
                    computeBracket(numString)
                    i++
                }

                else -> {
                    if (i != 0 && expression[i - 1].toString() notIn Operators.values() &&
                        expression[i - 1] != '('
                    ) {
                        performSafePushToStack(numString, "*")
                    }
                    i++
                }
            }
        }

        if (numString.isNotEmpty()) {
            val number = numString.toString().toDouble()
            numStack.push(number)
            numString.clear()
        }
        while (!opStack.isEmpty()) {
            val op = opStack.pop()
            computeNormalOperation(op)
        }

        return try {
            numStack.pop()
        } catch (e: IndexOutOfBoundsException) {
            clearStacks()
            throw BadFormatException(e)
        }
    }

    @Suppress("NestedBlockDepth")
    private fun performSafePushToStack(
        numString: StringBuilder,
        currOp: String
    ) {
        if (numString.isNotEmpty()) {
            val number = numString.toString().toDouble()
            numStack.push(number)
            numString.clear()

            if (opStack.isEmpty()) {
                opStack.push(currOp)
            } else {
                var prevOpPrecedence = getBinaryOperatorPrecedence(opStack.peek())
                val currOpPrecedence = getBinaryOperatorPrecedence(currOp)
                if (currOpPrecedence > prevOpPrecedence) {
                    opStack.push(currOp)
                } else {
                    while (currOpPrecedence <= prevOpPrecedence) {
                        val op = opStack.pop()
                        computeNormalOperation(op)
                        if (!opStack.isEmpty()) {
                            prevOpPrecedence = getBinaryOperatorPrecedence(opStack.peek())
                        } else {
                            break
                        }
                    }
                    opStack.push(currOp)
                }
            }
        } else if (!numStack.isEmpty() || currOp == Operators.UNARY.sign) {
            opStack.push(currOp)
        }
    }

    private fun getBinaryOperatorPrecedence(
        currOp: String
    ) = Operators.values()
        .firstOrNull { it.sign == currOp }
        ?.precedence ?: -1

    private fun computeBracket(numString: StringBuilder) {
        if (numString.isNotEmpty()) {
            val number = numString.toString().toDouble()
            numStack.push(number)
            numString.clear()
        }

        var operator = popForBracket()
        while (operator != "(") {
            computeNormalOperation(operator)
            operator = popForBracket()
        }
    }

    @Suppress("TooGenericExceptionCaught")
    private fun popForBracket() = try {
        opStack.pop()
    } catch (e: IndexOutOfBoundsException) {
        clearStacks()
        throw BadFormatException(e)
    }

    private fun clearStacks() {
        numStack.clear()
        opStack.clear()
    }
}
