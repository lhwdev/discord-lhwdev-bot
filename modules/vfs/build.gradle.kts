import com.lhwdev.build.*


plugins {
	kotlin("jvm")
	id("org.jetbrains.compose")
	
	id("common-plugin") // must be applied after Kotlin/Android plugins
}

kotlin {
	setup()
	
	explicitApi()
}

dependencies {
	implementation(compose.runtime)
	
	implementation(libs.coroutines)
}
