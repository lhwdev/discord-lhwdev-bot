package com.lhwdev.bot.structure.user

import com.lhwdev.bot.structure.BotEntity
import com.lhwdev.vfs.Path


interface UniqueUserName


interface User : BotEntity {
	/// Information
	
	val id: String
	
	val uniqueNameInfo: UniqueUserName
	
	val uniqueName: String
		get() = uniqueNameInfo.toString()
	
	val displayName: String
	
	val profile: Path
	
	
	suspend fun dmChannel(): DmChannel
}
