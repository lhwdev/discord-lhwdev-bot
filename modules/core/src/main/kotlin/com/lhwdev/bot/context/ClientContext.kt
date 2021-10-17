package com.lhwdev.bot.context

import com.lhwdev.bot.Client
import com.lhwdev.bot.backend.BotBackend
import com.lhwdev.bot.structure.EntityId
import com.lhwdev.bot.structure.guild.Guild
import com.lhwdev.bot.structure.user.User


public interface ClientContext {
	public val backend: BotBackend
	
	public val client: Client
	
	
	public val selfUserId: EntityId<User>
	
	public suspend fun getSelfUser(): User
	
	
	public suspend fun getGuild(guildId: EntityId<Guild>): Guild?
}
