package com.lhwdev.bot

import com.lhwdev.bot.event.BotEvent
import com.lhwdev.bot.structure.user.User
import com.lhwdev.utils.EventSubscriber


interface Client : EventSubscriber<BotEvent> {
	val user: User
}
