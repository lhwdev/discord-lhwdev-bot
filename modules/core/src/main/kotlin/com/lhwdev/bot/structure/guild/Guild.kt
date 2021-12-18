package com.lhwdev.bot.structure.guild

import com.lhwdev.bot.context.GuildContext
import com.lhwdev.bot.structure.BotEntity


public interface Guild : BotEntity, GuildContext {
	override val id: GuildId
}
