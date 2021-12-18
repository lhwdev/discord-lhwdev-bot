package com.lhwdev.bot.localization


public abstract class Locale(public val name: String, public val localizedName: String, public val code: String) {
	override fun toString(): String = "Locale $name ($code)"
}

public object Locales {
	public object English : Locale(name = "English", localizedName = "English", code = "en-us")
	
	public object Korean : Locale(name = "Korean", localizedName = "한국어", code = "ko-kr")
}
