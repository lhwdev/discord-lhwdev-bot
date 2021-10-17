package com.lhwdev.bot.structure.user

import com.lhwdev.bot.structure.BotEntity
import com.lhwdev.bot.structure.EntityId
import com.lhwdev.bot.structure.channel.DmChannel
import com.lhwdev.bot.structure.user.UserContext.Guild
import com.lhwdev.vfs.Path


public interface UniqueUserName


/**
 * The context for [User].
 * One user can set different names, profile image, etc.
 *
 * The permissions are also different; Let there is A, who is on the guild which forbids DM to its member.
 * If you try to call [User.dmChannel] with [User] whose [UserContext] set to that [Guild], it will fail.
 *
 * If you have an enough permission, you can try [User.withUserContext].
 */
public interface UserContext {
	public class Guild(public val guildId: EntityId<Guild>) : UserContext
	
	public class Channel(public val channelId: EntityId<Guild>) : UserContext
	
	public object Global : UserContext
}


public interface User : BotEntity {
	/// Information
	
	override val id: EntityId<User>
	
	public val context: UserContext
	
	public suspend fun withUserContext(context: UserContext): User?
	
	
	public val uniqueName: UniqueUserName
	
	public val uniqueDisplayName: String
		get() = uniqueName.toString()
	
	public val displayName: String
	
	public val profileImage: Path
	
	
	public suspend fun dmChannel(): DmChannel?
}
