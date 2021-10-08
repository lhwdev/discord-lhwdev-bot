package com.lhwdev.vfs


public enum class PathType { file, directory }



public sealed interface Path : (@Suppress("DEPRECATION") PathBase)

public sealed interface AnyPath : Path


@Deprecated("Do not use PathBase directly, as it is intended to make implementing easy")
public interface PathBase {
	// Basic information
	public val fileSystem: FileSystem
	
	public val name: String
	
	public val canonicalPath: String
	public val absolutePath: String
	
	public val protocol: String
	
	
	// Navigation
	public operator fun get(child: String): UnknownPath
	public operator fun get(vararg children: String): UnknownPath
	
	public val parent: DirectoryPath
	
	public fun resolve(segment: String): UnknownPath
	public fun resolve(vararg segments: String): UnknownPath
	
	
	// Metadata
	public suspend fun type(): PathType?
	
	public suspend fun isFile(): Boolean = type() == PathType.file
	public suspend fun isDirectory(): Boolean = type() == PathType.directory
	public suspend fun exists(): Boolean = type() != null
	
	public suspend fun metadata(): PathMetadata
	
	
	// IO
	public suspend fun moveTo(path: Path, overwrite: Boolean)
	
	
	// Other
	public override fun toString(): String
}
