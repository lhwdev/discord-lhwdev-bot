package com.lhwdev.bot.structure.chat

import com.lhwdev.bot.structure.user.UserId
import kotlinx.coroutines.flow.Flow


public interface UserMentions {
	public val users: Flow<UserId>
	
	public suspend fun includes(user: UserId): Boolean
	
	public suspend fun usersEqual(users: List<UserId>): Boolean
	
	public suspend fun toList(): List<UserId>
}
