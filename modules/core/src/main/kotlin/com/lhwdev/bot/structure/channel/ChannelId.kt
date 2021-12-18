package com.lhwdev.bot.structure.channel

import com.lhwdev.bot.structure.EntityId


public interface ChannelId : EntityId<Channel> {
	public val mention: String
}
