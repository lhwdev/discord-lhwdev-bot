package com.lhwdev.bot.command.scope

import androidx.compose.runtime.State
import com.lhwdev.bot.command.Command
import com.lhwdev.bot.command.Subcommand
import com.lhwdev.bot.command.parameter.CommandParameter
import com.lhwdev.bot.context.ChannelContext
import com.lhwdev.bot.structure.chat.EmbedBuilder
import com.lhwdev.bot.structure.chat.Message
import com.lhwdev.bot.structure.chat.MessageBuilder
import com.lhwdev.bot.structure.chat.embed



public interface InvokeScope : ChannelContext {
	public val selfCommand: Command<*>
	
	public fun <T> parameterFor(parameter: CommandParameter<T>): T
	
	
	public suspend operator fun <C> Subcommand<C>.invoke(): C
	
	
	public suspend fun reply(message: Message)
	
	
	public fun updatable(block: UpdatableScope.() -> Unit)
	
	public fun <R> updatableState(block: UpdatableScope.() -> R): State<R>
}


public suspend inline fun InvokeScope.reply(block: MessageBuilder.() -> Unit): Message {
	val message = MessageBuilder().apply(block).build()
	reply(message)
	return message
}

public suspend inline fun InvokeScope.replyEmbed(block: EmbedBuilder.() -> Unit): Message = reply {
	embed(block)
}



public interface UpdatableScope : InvokeScope {
	public enum class UpdateStatus { creating, updating }
	
	public val updateStatus: UpdateStatus
}


public val UpdatableScope.isCreating: Boolean
	get() = updateStatus == UpdatableScope.UpdateStatus.creating

public val UpdatableScope.isUpdating: Boolean
	get() = updateStatus == UpdatableScope.UpdateStatus.updating

