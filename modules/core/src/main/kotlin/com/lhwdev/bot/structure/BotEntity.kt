package com.lhwdev.bot.structure

import com.lhwdev.bot.Client


public interface BotEntity {
	public val id: EntityId<*>
	
	public val client: Client
} 
