package com.lhwdev.vfs

import java.io.InputStream
import java.io.OutputStream


public interface FilePath : Path {
	// Common
	public fun asUnknown(): UnknownPath
	
	override suspend fun metadata(): FileMetadata
	
	
	// File IO
	public suspend fun createFile(): FilePath
	public suspend fun createFileWithDir(recursive: Boolean = true): FilePath
	
	public suspend fun openRead(): InputStream
	public suspend fun openWrite(): OutputStream
	public suspend fun openAppend(): OutputStream
	
	public suspend fun delete()
}
