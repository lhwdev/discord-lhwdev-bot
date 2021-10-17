package com.lhwdev.bot.command

import com.lhwdev.bot.command.interceptor.CommandInterceptor
import com.lhwdev.bot.command.parameter.CommandParameter
import com.lhwdev.bot.command.scope.InvokeScope
import com.lhwdev.bot.localization.LocaleText


interface Command<T> {
	val id: String
	
	val title: LocaleText
	
	val description: LocaleText
	
	val interceptors: List<CommandInterceptor>
	
	val parameters: List<CommandParameter<*>>
	
	val subcommands: Map<String, Command<*>>
	
	suspend fun InvokeScope.onInvoke(): T
	
	suspend fun InvokeScope.onUpdate(): T
}
