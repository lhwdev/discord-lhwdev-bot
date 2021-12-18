package com.lhwdev.bot.command.parameter

import com.lhwdev.bot.structure.channel.ChannelId
import com.lhwdev.bot.structure.user.UserId


public object ParameterTypes {
	public object String : ParameterType<kotlin.String>
	
	
	public object Int : ParameterType<kotlin.Int>
	
	public object Number : ParameterType<Double>
	
	
	public class Enum<T : kotlin.Enum<*>>(public val values: Array<T>) : ParameterType<T>
	
	public inline fun <reified T : kotlin.Enum<T>> Enum(): Enum<T> =
		Enum(enumValues())
	
	
	public object Message : ParameterType<com.lhwdev.bot.structure.chat.Message>
	
	
	public object Mention {
		public object Channel : ParameterType<ChannelId>
		
		public object User : ParameterType<UserId>
	}
}
