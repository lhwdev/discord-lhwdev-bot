@file:Suppress("BlockingMethodInNonBlockingContext")

package com.lhwdev.vfs.impl.niofs

import com.lhwdev.vfs.FilePath
import com.lhwdev.vfs.PathMetadata
import com.lhwdev.vfs.UnknownPath
import com.lhwdev.vfs.checkFile


public fun NioUnknownPath(path: java.nio.file.Path): NioUnknownPath =
	NioUnknownPath(fileSystem = NioFileSystem(fs = path.fileSystem, protocol = "file"), path = path)


public class NioUnknownPath(
	fileSystem: NioFileSystem,
	path: java.nio.file.Path
) : NioPath(fileSystem, path), UnknownPath {
	override suspend fun metadata(): PathMetadata = io { nioPathMetadata(fileSystem, this) }
	
	override suspend fun asFile(): FilePath {
		checkFile()
		return NioFilePath(fileSystem, path)
	}
	
	override fun toString(): String = absolutePath
}
