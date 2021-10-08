package com.lhwdev.discord.mybot.config


object Config {
	object Secret {
		@Serializable
		class Discord(val token: String)
	}
}
