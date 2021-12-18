package com.lhwdev.bot.localization


public interface LocaleText {
	public val locales: Set<Locale>
	public fun getText(locale: Locale): String
}

public class SimpleLocaleText(private val texts: Map<Locale, String>) : LocaleText {
	override val locales: Set<Locale>
		get() = texts.keys
	
	override fun getText(locale: Locale): String = texts.getValue(locale)
}


private val sLocalesEnKo = setOf(Locales.English, Locales.Korean)

private class CommonLocaleText(private val english: String, private val korean: String) : LocaleText {
	override val locales: Set<Locale>
		get() = sLocalesEnKo
	
	override fun getText(locale: Locale): String = when(locale) {
		Locales.English -> english
		Locales.Korean -> korean
		else -> error("could not find text for locale $locale")
	}
}


private fun <K, V : Any> MutableMap<K, V>.putOrRemove(key: K, value: V?) {
	if(value == null) {
		remove(key)
	} else {
		put(key, value)
	}
}


public inline fun localeText(block: LocaleTextBuilder.() -> Unit): LocaleText =
	LocaleTextBuilder().apply(block).build()


public class LocaleTextBuilder {
	private val map = mutableMapOf<Locale, String>()
	
	public operator fun set(locale: Locale, value: String) {
		map[locale] = value
	}
	
	
	public var korean: String?
		get() = map[Locales.Korean]
		set(value) {
			map.putOrRemove(Locales.Korean, value)
		}
	
	public var english: String?
		get() = map[Locales.English]
		set(value) {
			map.putOrRemove(Locales.English, value)
		}
	
	
	@Suppress("IntroduceWhenSubject")
	public fun build(): LocaleText = when {
		map.keys == sLocalesEnKo -> CommonLocaleText(
			english = map.getValue(Locales.English),
			korean = map.getValue(Locales.Korean)
		)
		else -> SimpleLocaleText(map)
	}
}
