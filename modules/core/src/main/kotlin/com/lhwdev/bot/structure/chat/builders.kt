package com.lhwdev.bot.structure.chat


public class MessageBuilder {
	public fun embed(embed: Embed) {
		TODO()
	}
	
	public fun build(): Message = TODO()
}

public inline fun MessageBuilder.embed(block: EmbedBuilder.() -> Unit): Embed =
	EmbedBuilder().apply(block).build().also { embed(it) }


public class EmbedBuilder {
	public fun build(): Embed = TODO()
}
