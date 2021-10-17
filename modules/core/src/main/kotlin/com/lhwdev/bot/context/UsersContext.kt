package com.lhwdev.bot.context

import com.lhwdev.bot.structure.EntityId
import com.lhwdev.bot.structure.user.User


public interface UsersContext {
	public suspend fun getUser(userId: EntityId<User>): User?
}
