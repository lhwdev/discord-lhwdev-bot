package com.lhwdev.bot.backend

import com.lhwdev.bot.structure.chat.Component
import com.lhwdev.bot.structure.chat.Embed
import com.lhwdev.bot.structure.chat.Message


interface Unsafe {
	fun createMessage(
		content: String = "",
		embeds: List<Embed> = emptyList(),
		components: List<Component> = emptyList()
	): Message
}
