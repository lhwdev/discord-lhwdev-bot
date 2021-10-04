import com.lhwdev.build.*


plugins {
	kotlin("jvm")
	id("org.jetbrains.compose")
	
	id("common-plugin") // must be applied after Kotlin/Android plugins
}

kotlin {
	setup()
}

dependencies {
	implementation(project(":vfs"))
	
	implementation(compose.runtime)
}
