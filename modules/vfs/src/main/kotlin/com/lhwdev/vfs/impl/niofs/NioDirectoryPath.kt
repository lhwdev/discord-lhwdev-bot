@file:Suppress("BlockingMethodInNonBlockingContext")

package com.lhwdev.vfs.impl.niofs

import com.lhwdev.vfs.*
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import java.nio.file.Files
import kotlin.io.path.deleteExisting
import kotlin.io.path.name


public class NioDirectoryPath(
	fileSystem: NioFileSystem, path: java.nio.file.Path
) : NioPath(fileSystem, path), DirectoryPath {
	override fun asUnknown(): UnknownPath = NioUnknownPath(fileSystem, path)
	
	override suspend fun metadata(): DirectoryMetadata = nioPathMetadata(fileSystem, this) as DirectoryMetadata
	
	override suspend fun list(glob: String): Flow<UnknownPath> = flow {
		Files.newDirectoryStream(path, glob).use { stream ->
			stream.forEach { emit(get(it.name)) }
		}
	}
	
	override suspend fun deleteSingle() {
		io { path.deleteExisting() }
	}
	
	override suspend fun deleteRecursive(): Unit = io {
		Files.walk(path).use { stream ->
			stream.iterator().forEach {
				it.deleteExisting()
			}
		}
	}
	
	override suspend fun watch(recursive: Boolean, vararg kinds: WatchEventKind): Flow<WatchEvent> =
		watchPath(fileSystem, path, kinds, recursive)
}
