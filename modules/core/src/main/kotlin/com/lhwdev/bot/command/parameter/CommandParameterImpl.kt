package com.lhwdev.bot.command.parameter

import com.lhwdev.bot.localization.LocaleText


class CommandParameterImpl<T>(
	type: ParameterType<T>,
	description: LocaleText? = null
) : CommandParameter<T> {
	override val value: T
		get() = TODO()
}
