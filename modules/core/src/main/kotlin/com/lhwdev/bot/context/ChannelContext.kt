package com.lhwdev.bot.context

import com.lhwdev.bot.structure.EntityId
import com.lhwdev.bot.structure.chat.Message


public interface ChannelContext : UsersContext {
	public suspend fun getMessage(messageId: EntityId<Message>): Message?
}
