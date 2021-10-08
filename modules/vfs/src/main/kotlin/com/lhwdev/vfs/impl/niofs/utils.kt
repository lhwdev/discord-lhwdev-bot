package com.lhwdev.vfs.impl.niofs

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext


internal suspend inline fun <R> io(crossinline block: suspend () -> R): R = wrapErrors {
	withContext(Dispatchers.IO) { block() }
}
