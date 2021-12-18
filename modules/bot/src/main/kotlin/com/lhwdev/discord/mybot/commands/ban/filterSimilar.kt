package com.lhwdev.discord.mybot.commands.ban

import androidx.compose.runtime.getValue
import com.lhwdev.bot.command.builder.botCommand
import com.lhwdev.bot.command.interceptor.ContextMenuInterceptor
import com.lhwdev.bot.command.parameter.ParameterTypes


private fun fuzzySearch(content: String): (String) -> Float = TODO()


val filterSimilarCommand = botCommand(id = "filterSimilar") {
	interceptor(ContextMenuInterceptor)
	
	val message by mainParameter("message", ParameterTypes.Message)
	
	
	onInvoke {
		val content = message.content
		
		message.author
		
		val search = fuzzySearch(content)
		
	}
}
