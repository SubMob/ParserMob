/*
 * Copyright (c) 2020 Mustafa Ozhan. All rights reserved.
 */

package com.github.mustafaozhan.parsermob.error

class BadFormatException(
    exception: Exception = Exception(),
    msg: String = "Bad Format"
) : Exception(msg, exception)
