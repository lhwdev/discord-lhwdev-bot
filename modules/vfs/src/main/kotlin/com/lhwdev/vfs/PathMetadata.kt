package com.lhwdev.vfs

import java.time.Instant


public sealed interface PathMetadata : PathPermissionObject {
	public val path: Path
	
	
	// Properties
	public var lastModified: Instant
	
	public var isHidden: Boolean
	public val isSymlink: Boolean
	
	public suspend fun sizeBytes(): Long
}


public interface FileMetadata : PathMetadata {
	override val path: FilePath
}

public interface DirectoryMetadata : PathMetadata {
	override val path: DirectoryPath
}
