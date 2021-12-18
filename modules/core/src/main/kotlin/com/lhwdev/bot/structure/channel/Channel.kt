package com.lhwdev.bot.structure.channel

import com.lhwdev.bot.context.ChannelContext
import com.lhwdev.bot.structure.BotEntity


public interface Channel : BotEntity, ChannelContext {
	override val id: ChannelId
	
	public val mention: String
}
