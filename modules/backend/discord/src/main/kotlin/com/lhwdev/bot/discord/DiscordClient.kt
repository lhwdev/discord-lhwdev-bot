package com.lhwdev.bot.discord

import com.lhwdev.bot.Client
import com.lhwdev.bot.event.BotEvent
import com.lhwdev.utils.SubscribeHandle
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlin.reflect.KClass


class DiscordClient : Client {
	private inner class SubscribeHandleImpl<T : BotEvent>(val event: KClass<T>, val listener: suspend (T) -> Unit) :
		SubscribeHandle {
		override fun unsubscribe() {
			events -= event
		}
	}
	
	private val events = mutableMapOf<KClass<out BotEvent>, MutableList<SubscribeHandleImpl<out BotEvent>>>()
	
	
	@Suppress("EXPERIMENTAL_API_USAGE")
	override fun <T : BotEvent> on(type: KClass<T>): Flow<T> = callbackFlow {
		val handle = on(type) { send(it) }
		invokeOnClose { handle.unsubscribe() }
	}
	
	override fun <T : BotEvent> on(type: KClass<T>, listener: suspend (T) -> Unit): SubscribeHandle {
		val handle = SubscribeHandleImpl(type, listener)
		
		val list = events.getOrPut(type) { mutableListOf() }
		list += handle
		
		return handle
	}
}
