@file:OptIn(ExperimentalContracts::class)

package com.lhwdev.bot.command.builder

import androidx.compose.runtime.MutableState
import com.lhwdev.bot.command.Command
import com.lhwdev.bot.command.Subcommand
import com.lhwdev.bot.command.interceptor.CommandInterceptor
import com.lhwdev.bot.command.parameter.CommandParameter
import com.lhwdev.bot.command.parameter.CommandParameterImpl
import com.lhwdev.bot.command.parameter.ParameterType
import com.lhwdev.bot.command.scope.InvokeScope
import com.lhwdev.bot.command.scope.UpdatableScope
import com.lhwdev.bot.localization.LocaleText
import kotlinx.serialization.KSerializer
import kotlin.contracts.ExperimentalContracts
import kotlin.contracts.InvocationKind
import kotlin.contracts.contract


public inline fun <T> botCommand(
	id: String,
	block: CommandBuilder<T>.() -> Unit
): Command<T> {
	contract { callsInPlace(block, InvocationKind.EXACTLY_ONCE) }
	return CommandBuilder<T>(id).apply(block).build()
}

@JvmName("botCommandUnit")
public inline fun botCommand(
	id: String,
	block: CommandBuilder<Unit>.() -> Unit
): Command<Unit> {
	contract { callsInPlace(block, InvocationKind.EXACTLY_ONCE) }
	return botCommand<Unit>(id, block)
}


public inline fun <T> botSubcommand(
	id: String,
	block: CommandBuilder<T>.() -> Unit
): Subcommand<T> {
	contract { callsInPlace(block, InvocationKind.EXACTLY_ONCE) }
	return SubcommandBuilder<T>(id).apply(block).build()
}

@JvmName("botSubCommandUnit")
public inline fun botSubcommand(
	id: String,
	block: CommandBuilder<Unit>.() -> Unit
): Subcommand<Unit> {
	contract { callsInPlace(block, InvocationKind.EXACTLY_ONCE) }
	return botSubcommand<Unit>(id, block)
}


public open class CommandBuilder<T>(
	@PublishedApi
	internal val id: String
) {
	private val interceptors = mutableListOf<CommandInterceptor>()
	private val parameters = mutableListOf<CommandParameter<*>>()
	private val subcommands = mutableMapOf<String, Command<*>>()
	private var onInvoke: (suspend InvokeScope.() -> T)? = null
	
	
	public var title: LocaleText? = null
	
	public var description: LocaleText? = null
	
	
	public fun interceptor(interceptorToAdd: CommandInterceptor) {
		interceptors += interceptorToAdd
	}
	
	
	public fun <T> mainParameter(
		name: String,
		type: ParameterType<T>,
		description: LocaleText? = null
	): CommandParameter<T> {
		val parameter = CommandParameterImpl(name, type, description, isMainParameter = true)
		parameters += parameter
		return parameter
	}
	
	public fun <T> parameter(
		name: String,
		type: ParameterType<T>,
		description: LocaleText? = null
	): CommandParameter<T> {
		val parameter = CommandParameterImpl(name, type, description)
		parameters += parameter
		return parameter
	}
	
	public fun <T> parameter(
		name: String,
		type: ParameterType<T>,
		default: T,
		description: LocaleText
	): CommandParameter<T> = parameter(name, type, description)
	
	
	public fun <T> config(serializer: KSerializer<T>): MutableState<T> = TODO()
	
	
	public inline fun <S> subcommand(id: String, block: CommandBuilder<S>.() -> Unit): Subcommand<S> {
		contract { callsInPlace(block, InvocationKind.EXACTLY_ONCE) }
		val command =
			botSubcommand("${this.id}#$id", block) // actually this id does not matter a lot; just not to conflict
		subcommand(id, command)
		return command
	}
	
	@JvmName("subcommandUnit")
	public inline fun subcommand(id: String, block: CommandBuilder<Unit>.() -> Unit): Subcommand<Unit> {
		contract { callsInPlace(block, InvocationKind.EXACTLY_ONCE) }
		return subcommand<Unit>(id, block)
	}
	
	public fun subcommand(id: String, command: Command<*>) {
		subcommands[id] = command
	}
	
	
	public fun onInvoke(block: suspend InvokeScope.() -> T) {
		onInvoke = block
	}
	
	
	@PublishedApi
	internal open fun build(): Command<T> = object : Command<T> {
		
	}
}

public inline fun CommandBuilder<Unit>.onUpdatableInvoke(noinline block: suspend UpdatableScope.() -> Unit) {
	onInvoke {
		updatable(block)
	}
}


public open class SubcommandBuilder<T>(id: String) : CommandBuilder<T>(id) {
	@PublishedApi
	override fun build(): Subcommand<T> = object : Subcommand<T> {
		
	}
}
