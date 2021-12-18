package com.lhwdev.bot.structure.chat


public interface MessageBase {
	public val content: String
	
	public val embeds: List<Embed>
	
	public val components: List<Component>
	
	public val attachments: List<Attachment>
	
	public val userMentions: UserMentions
	
	public val
}
