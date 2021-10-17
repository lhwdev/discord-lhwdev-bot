package com.lhwdev.bot.command.interceptor

import com.lhwdev.bot.command.scope.InterceptorScope


interface CommandInterceptor {
	fun InterceptorScope.load()
}
