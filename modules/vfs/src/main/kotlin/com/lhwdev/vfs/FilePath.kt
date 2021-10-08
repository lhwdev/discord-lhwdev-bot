package com.lhwdev.vfs

import kotlinx.coroutines.flow.Flow
import java.io.InputStream
import java.io.OutputStream


public interface FilePath : AnyPath, Readable, Writeable {
	// Common
	public fun asUnknown(): UnknownPath
	
	override suspend fun metadata(): FileMetadata
	
	
	// File IO
	public suspend fun createFile(): FilePath
	public suspend fun createFileWithDir(recursive: Boolean = true): FilePath
	
	// (from Readable, Writeable)
	
	public suspend fun delete()
	
	public suspend fun watch(vararg kinds: WatchEventKind): Flow<WatchEvent>
}


public enum class SizeBytesType { exact, estimate, infinite, unavailable }

public interface FileRead : SuspendCloseable {
	public val sizeBytes: Long
	public val sizeBytesType: SizeBytesType
	
	public val stream: InputStream
}


public interface FileWrite : SuspendCloseable {
	public val append: Boolean
	
	public val maxSizeBytes: Long
	public val maxSizeBytesType: SizeBytesType
	
	public val stream: OutputStream
}
