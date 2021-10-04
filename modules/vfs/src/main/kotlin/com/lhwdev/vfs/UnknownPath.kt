package com.lhwdev.vfs


public interface UnknownPath : Path {
	// File IO
	public suspend fun asFile(): FilePath
	
	public suspend fun createFile(): FilePath
	public suspend fun createFileWithDir(recursive: Boolean = true): FilePath
	
	
	// Directory IO
	public suspend fun asDirectory(): DirectoryPath
	
	public suspend fun createDirectory(recursive: Boolean = true): DirectoryPath
}
