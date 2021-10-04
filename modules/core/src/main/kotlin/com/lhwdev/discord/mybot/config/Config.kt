package com.lhwdev.discord.mybot.config

import kotlinx.serialization.Serializable


object Config {
	object Secret {
		@Serializable
		class Discord(val token: String)
	}
}
