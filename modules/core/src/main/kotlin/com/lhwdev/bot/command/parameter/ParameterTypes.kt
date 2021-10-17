package com.lhwdev.bot.command.parameter


object ParameterTypes {
	object String : ParameterType<kotlin.String>
	
	
	object Int : ParameterType<kotlin.Int>
	
	object Number : ParameterType<Double>
	
	
	class Enum<T : kotlin.Enum<*>>(val values: Array<T>) : ParameterType<T>
	
	inline fun <reified T : kotlin.Enum<T>> Enum(): Enum<T> =
		Enum(enumValues())
	
	
}
