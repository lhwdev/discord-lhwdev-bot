package com.lhwdev.bot.structure.channel

import com.lhwdev.bot.structure.chat.Message
import com.lhwdev.bot.structure.chat.MessageId


public interface MessageChannel : Channel {
	public suspend fun getMessage(messageId: MessageId): Message?
}
