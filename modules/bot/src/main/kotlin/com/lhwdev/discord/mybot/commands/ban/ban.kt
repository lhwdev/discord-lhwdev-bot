package com.lhwdev.discord.mybot.commands.ban

import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import com.lhwdev.bot.command.builder.botCommand
import com.lhwdev.bot.command.interceptor.DefaultInterceptor
import com.lhwdev.bot.command.parameter.ParameterTypes
import com.lhwdev.bot.command.scope.InvokeScope
import com.lhwdev.bot.command.scope.replyEmbed
import com.lhwdev.bot.localization.localeText
import com.lhwdev.bot.structure.user.User
import kotlinx.serialization.Serializable
import kotlinx.serialization.builtins.ListSerializer
import java.time.Instant
import kotlin.time.Duration


private enum class CommandType(override val enumName: CharSequence) : ParameterTypes.EnumModel {
	ban("ban"),
	unban("unban")
}

@Serializable
private class BanInfo(
	val user: User,
	var start: Long,
	var duration: Long,
	var reason: String
) {
	override fun toString(): String = localeText {
		"${mention(user.uniqueDisplayName)}, ${time(start)}~${duration} " +
			"(${localeText(korean = "이유", english = "reason")}: ${code(reason)})"
	}
}


val banCommand = botCommand(id = "com.lhwdev.bot.commands.basic:ban") {
	title = localeText {
		korean = "벤"
		english = "ban"
	}
	
	interceptor(DefaultInterceptor)
	
	val type by parameter(
		type = ParameterTypes.Enum<CommandType>()
	)
	
	val user by parameter(
		type = ParameterTypes.Mention.User,
		description = localeText {
			korean = "벤때릴 사용자"
			english = "The user which you will ban"
		}
	)
	
	
	var banHistory by config(ListSerializer(BanInfo.serializer()))
	
	
	fun InvokeScope.replace(info: BanInfo?): BanInfo? {
		val last = banHistory.indexOfFirst { it.user == user }
		
		return if(last == -1) {
			if(info != null) banHistory = banHistory + info
			null
		} else {
			if(info == null) {
				banHistory.removeAt(last)
			} else {
				banHistory.set(last, info)
			}
		}
	}
	
	val ban = subcommand("ban") {
		val duration by parameter(
			type = ParameterTypes.Time.Duration(),
			default = Long.MAX_VALUE,
			description = localeText {
				korean = "벤때릴 기간"
				english = "The duration for which you will ban"
			}
		)
		
		val reason by parameter(
			type = ParameterTypes.String,
			description = localeText {
				korean = "벤 이유"
				english = "The reason for ban"
			}
		)
		
		onInvoke(updatable = true) {
			val info = BanInfo(user, start = Instant.now().toEpochMilli(), duration = duration, reason = reason)
			val last = replace(info)
			if(last == null) replyEmbed {
				title = localeText(mention(user, notification = true)) {
					korean = "${it}을 벤했습니다."
					english = "Banned $it."
					
				}
				content {
					+localeText(duration) {
						korean = "기간: $it"
						english = "Duration: $it"
					}
					+localeText(duration) {
						korean = "이유: $it"
						english = "Reason: $it"
					}
				}
				
				allowMention()
			} else replyEmbed {
				title = localeText {
					korean { "${mention(user, notification = true)}의 벤 정보를 업데이트했습니다." }
					english { "Updated ban information of ${mention(user, notification = true)}." }
				}
				
				content {
					+localeText {
						korean { "기존: $last" }
						english { "Before: $last" }
					}
					+localeText {
						korean { "신규: $info" }
						english { "Updated: $info" }
					}
				}
				
				allowMention()
			}
		}
	}
	
	val unban = subcommand("unban") {
		onInvoke {
			val last = replace(null)
			replyEmbed {
				title = localeText(mention(user, notification = true)) {
					korean = "${it}의 벤을 해제했습니다."
					english = "Unbanned $it."
				}
				
				allowMention()
			}
		}
	}
	
	
	onInvoke {
		updatable {
			when(type) {
				CommandType.ban -> ban()
				CommandType.unban -> unban()
			}
		}
	}
}
