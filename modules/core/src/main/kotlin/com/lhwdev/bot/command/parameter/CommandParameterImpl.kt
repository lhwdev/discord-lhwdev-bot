package com.lhwdev.bot.command.parameter

import com.lhwdev.bot.localization.LocaleText


public class CommandParameterImpl<T>(
	name: String,
	type: ParameterType<T>,
	description: LocaleText? = null,
	isMainParameter: Boolean = false
) : CommandParameter<T> {
	override val value: T
		get() = TODO()
}
