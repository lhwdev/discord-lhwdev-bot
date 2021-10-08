package com.lhwdev.utils

import kotlinx.coroutines.flow.Flow
import kotlin.reflect.KClass


interface EventSubscriber<B : Any> {
	fun <T : B> on(type: KClass<T>): Flow<T>
	
	fun <T : B> on(type: KClass<T>, listener: suspend (T) -> Unit): SubscribeHandle
}


interface SubscribeHandle {
	fun unsubscribe()
}
