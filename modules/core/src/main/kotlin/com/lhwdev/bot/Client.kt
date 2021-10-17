package com.lhwdev.bot

import com.lhwdev.bot.context.ClientContext
import com.lhwdev.bot.event.BotEvent
import com.lhwdev.utils.EventSubscriber
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.filterIsInstance
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach


public interface Client : ClientContext, EventSubscriber<BotEvent> {
	public val scope: CoroutineScope
}


public inline fun <reified T : BotEvent> Client.on(
	scope: CoroutineScope = this.scope,
	noinline block: suspend (T) -> Unit
) {
	eventFlow
		.filterIsInstance<T>()
		.onEach(block)
		.launchIn(scope)
}
