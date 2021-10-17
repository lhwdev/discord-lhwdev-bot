package com.lhwdev.bot.context

import com.lhwdev.bot.structure.EntityId
import com.lhwdev.bot.structure.channel.Channel


public interface GuildContext : ClientContext, UsersContext {
	public suspend fun getChannel(channelId: EntityId<Channel>): Channel?
}
