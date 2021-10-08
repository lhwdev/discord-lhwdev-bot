package com.lhwdev.vfs.impl.niofs

import com.lhwdev.vfs.Path
import com.lhwdev.vfs.PathType
import com.lhwdev.vfs.VfsError
import kotlinx.coroutines.flow.collect
import java.io.IOException
import java.nio.file.*
import kotlin.io.path.Path


internal suspend inline fun <R> wrapErrors(usedPath: Path? = null, block: () -> R): R = try {
	block()
} catch(e: IOException) {
	throw wrapError(usedPath, e)
}

private suspend fun wrapError(usedPath: Path? = null, e: IOException): Exception {
	if(e !is FileSystemException) {
		return VfsError.Other(message = "nio fs error", cause = e)
	}
	val fs = usedPath?.fileSystem ?: FileSystems.getDefault()
	val path = NioUnknownPath(Path(e.file))
	
	return when(e) {
		is NoSuchFileException ->
			VfsError.NoEntry(path)
		is NotDirectoryException ->
			VfsError.WrongType(path, expected = PathType.directory, actual = path.type())
		is AccessDeniedException ->
			VfsError.AccessDenied(path, reason = e.reason)
		is FileAlreadyExistsException ->
			VfsError.FileAlreadyExists(path)
		is DirectoryNotEmptyException ->
			VfsError.DirectoryNotEmpty(
				path,
				content = buildString {
					var first = true
					path.asDirectory().list().collect {
						if(!first) append(", ")
						append(it)
						first = true
					}
				}
			)
		else ->
			VfsError.OtherWithPath(path = path, message = e.reason ?: "(?)", cause = e)
	}
}
