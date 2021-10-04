package com.lhwdev.vfs


public enum class PathType { file, directory }


public sealed interface Path {
	// Basic information
	public val fileSystem: FileSystem
	
	public val name: String
	
	public val canonicalPath: String
	public val absolutePath: String
	
	public val protocol: String
	
	
	// Navigation
	public operator fun get(child: String): UnknownPath
	public operator fun get(vararg child: String): UnknownPath
	
	public val parent: DirectoryPath
	
	public fun resolve(segment: String): UnknownPath
	public fun resolve(vararg segment: String): UnknownPath
	
	
	// Metadata
	public suspend fun type(): PathType?
	public suspend fun metadata(): PathMetadata
	
	
	// IO
	public suspend fun renameTo(path: Path)
	
	
	// Other
	public override fun toString(): String
}
