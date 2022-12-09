/*
 * Copyright (c) 2020 Mustafa Ozhan. All rights reserved.
 */

package com.github.submob.parsermob.error

class BadFormatException(
    exception: Exception = Exception("Bad Format"),
) : Exception(exception.message, exception)
