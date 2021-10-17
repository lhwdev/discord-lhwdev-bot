package com.lhwdev.utils

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.filterIsInstance
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach


public interface EventSubscriber<T> {
	public val eventFlow: SharedFlow<T>
}


public interface SubscribeHandle {
	public fun unsubscribe()
}


public inline fun <reified T> EventSubscriber<T>.on(scope: CoroutineScope, noinline block: suspend (T) -> Unit) {
	eventFlow
		.filterIsInstance<T>()
		.onEach(block)
		.launchIn(scope)
}
