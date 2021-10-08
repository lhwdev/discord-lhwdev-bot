package com.lhwdev.bot.event

import com.lhwdev.bot.Client


sealed interface BotEvent {
	val client: Client
}
