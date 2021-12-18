package com.lhwdev.discord.mybot.commands.notice

import androidx.compose.runtime.getValue
import com.lhwdev.bot.command.builder.botCommand
import com.lhwdev.bot.command.builder.onUpdatableInvoke
import com.lhwdev.bot.command.parameter.ParameterTypes
import com.lhwdev.bot.localization.localeText
import com.lhwdev.bot.structure.channel.MessageChannel
import com.lhwdev.utils.botError


val noticeCommand = botCommand(id = "com.lhwdev.bot.commands.basic:notice") {
	val targetChannelId by parameter(
		name = "targetChannel",
		type = ParameterTypes.Mention.Channel,
		description = localeText {
			korean = "공지를 전송할 채널"
			english = "The channel to which the notification is sent"
		}
	)
	
	onUpdatableInvoke {
		val targetChannel = getChannel(targetChannelId) as? MessageChannel
			?: botError("채널 ${targetChannelId.mention}을 찾을 수 없습니다.")
		
		targetChannel
	}
}
