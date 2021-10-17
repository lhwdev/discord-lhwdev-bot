package com.lhwdev.bot.backend.discord

import com.lhwdev.bot.backend.BotBackend
import dev.kord.core.Kord
import dev.kord.core.behavior.reply
import dev.kord.core.event.interaction.InteractionCreateEvent
import dev.kord.core.event.message.MessageCreateEvent
import dev.kord.core.on


class DiscordBackend : BotBackend {
	suspend fun hi() {
		val kord = Kord("") {
			enableShutdownHook = true
		}
		
		kord.on<MessageCreateEvent> {
			message.reply { }
		}
		
		kord.on<InteractionCreateEvent> {
			
		}
	}
}
