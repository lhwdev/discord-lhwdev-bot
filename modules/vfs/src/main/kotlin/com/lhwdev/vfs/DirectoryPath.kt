package com.lhwdev.vfs

import kotlinx.coroutines.flow.Flow


public interface DirectoryPath : AnyPath {
	// Common
	public fun asUnknown(): UnknownPath
	
	override suspend fun metadata(): DirectoryMetadata
	
	
	// Directory IO
	public suspend fun createDirectory(recursive: Boolean = true): DirectoryPath
	
	public suspend fun list(glob: String = "*"): Flow<UnknownPath>
	
	public suspend fun deleteSingle()
	public suspend fun deleteRecursive()
	
	public suspend fun watch(recursive: Boolean, vararg kinds: WatchEventKind): Flow<WatchEvent>
}
