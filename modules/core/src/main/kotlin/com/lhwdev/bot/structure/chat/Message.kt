package com.lhwdev.bot.structure.chat

import com.lhwdev.bot.structure.BotEntity
import com.lhwdev.bot.structure.user.User


public sealed interface Message : BotEntity {
	override val id: MessageId
	
	public val content: String
	
	public val embeds: List<Embed>
	
	public val components: List<Component>
	
	public val author: BelongUser
	
	public fun getAuthorUser(): User
}
