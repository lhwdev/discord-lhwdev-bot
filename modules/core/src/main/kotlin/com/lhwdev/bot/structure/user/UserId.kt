package com.lhwdev.bot.structure.user

import com.lhwdev.bot.structure.EntityId


public interface UserId : EntityId<User> {
	public val mention: String
}
