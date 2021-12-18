package com.lhwdev.utils


public fun botError(
	message: String? = null,
	cause: Throwable? = null
): Nothing = throw BotError(message = message, cause = cause)


public class BotError(message: String? = null, cause: Throwable? = null) : RuntimeException(message, cause)
