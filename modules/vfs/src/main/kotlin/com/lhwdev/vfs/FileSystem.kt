package com.lhwdev.vfs


public interface FileSystem {
	// Basic information
	public val separator: String
	
	
	// Navigation
	public fun resolve(path: String): UnknownPath
	public fun resolve(vararg path: String): UnknownPath
}
