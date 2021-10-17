package com.lhwdev.bot.backend.discord

import com.lhwdev.bot.Client
import com.lhwdev.bot.event.BotEvent
import com.lhwdev.bot.structure.user.User
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.SharedFlow


class DiscordClient : Client {
	override val scope: CoroutineScope
		get() = TODO()
	
	override val selfUser: User
		get() = TODO()
	
	override val eventFlow: SharedFlow<BotEvent>
		get() = TODO()
}
