@file:Suppress("BlockingMethodInNonBlockingContext")

package com.lhwdev.vfs.impl.niofs

import com.lhwdev.vfs.*
import kotlinx.coroutines.flow.Flow
import java.io.InputStream
import java.io.OutputStream
import java.nio.file.StandardOpenOption
import kotlin.io.path.deleteExisting
import kotlin.io.path.fileSize
import kotlin.io.path.inputStream
import kotlin.io.path.outputStream


public class NioFilePath(fileSystem: NioFileSystem, path: java.nio.file.Path) : NioPath(fileSystem, path), FilePath {
	override fun asUnknown(): UnknownPath = withPath(path)
	
	override suspend fun metadata(): FileMetadata =
		nioPathMetadata(fileSystem, this) as FileMetadata
	
	
	override suspend fun openRead(): FileRead = wrapErrors {
		NioFileRead(path)
	}
	
	override suspend fun openWrite(): FileWrite = wrapErrors {
		NioFileWrite(path, append = false)
	}
	
	override suspend fun openAppend(): FileWrite = wrapErrors {
		NioFileWrite(path, append = true)
	}
	
	override suspend fun delete() {
		io { path.deleteExisting() }
	}
	
	override suspend fun watch(vararg kinds: WatchEventKind): Flow<WatchEvent> =
		watchPath(fileSystem, path, kinds, recursive = false)
}


public class NioFileRead(private val path: java.nio.file.Path) : FileRead {
	override val sizeBytes: Long
		get() = path.fileSize()
	override val sizeBytesType: SizeBytesType
		get() = SizeBytesType.exact
	
	override val stream: InputStream = path.inputStream()
	
	override suspend fun close() {
		io { stream.close() }
	}
}

public class NioFileWrite(path: java.nio.file.Path, override val append: Boolean) : FileWrite {
	override val maxSizeBytes: Long
		get() = -1
	override val maxSizeBytesType: SizeBytesType
		get() = SizeBytesType.infinite // to do
	
	override val stream: OutputStream = if(append) {
		path.outputStream(StandardOpenOption.CREATE, StandardOpenOption.APPEND, StandardOpenOption.WRITE)
	} else {
		path.outputStream()
	}
	
	override suspend fun close() {
		io { stream.close() }
	}
}
