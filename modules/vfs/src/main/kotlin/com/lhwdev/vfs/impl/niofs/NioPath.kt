@file:Suppress("BlockingMethodInNonBlockingContext")

package com.lhwdev.vfs.impl.niofs

import com.lhwdev.vfs.*
import com.lhwdev.vfs.impl.common.VfsCommon
import com.lhwdev.vfs.impl.common.moveTo
import kotlin.io.path.*


public sealed class NioPath(
	public override val fileSystem: NioFileSystem,
	public val path: java.nio.file.Path
) : (@Suppress("DEPRECATION") PathBase) {
	public override val name: String
		get() = path.name
	public override val canonicalPath: String
		get() = path.absolutePathString()
	public override val absolutePath: String
		get() = path.absolutePathString()
	public override val protocol: String
		get() = fileSystem.protocol
	
	@Suppress("NOTHING_TO_INLINE")
	internal inline fun withPath(path: java.nio.file.Path) = NioUnknownPath(fileSystem, path)
	
	
	// resolve not intended here, so prohibit such things
	public override fun get(child: String): UnknownPath =
		withPath(path.resolve(child))
	
	public override fun get(vararg children: String): UnknownPath =
		withPath(children.fold(path) { acc, child -> acc.resolve(child) })
	
	public override val parent: DirectoryPath
		get() = NioDirectoryPath(fileSystem, path.parent)
	
	public override fun resolve(segment: String): UnknownPath =
		withPath(path.resolve(segment))
	
	public override fun resolve(vararg segments: String): UnknownPath =
		withPath(segments.fold(path) { acc, segment -> acc.resolve(segment) })
	
	public override suspend fun type(): PathType? = io {
		when {
			path.isRegularFile() -> PathType.file
			path.isDirectory() -> PathType.directory
			else -> null
		}
	}
	
	public suspend fun createFile(): FilePath {
		io { path.createFile() }
		return NioFilePath(fileSystem, path)
	}
	
	public suspend fun createFileWithDir(recursive: Boolean): FilePath {
		io {
			if(recursive) {
				path.parent.createDirectories()
			} else {
				path.parent.createDirectory()
			}
			
			path.createFile()
		}
		
		return NioFilePath(fileSystem, path)
	}
	
	public suspend fun asDirectory(): DirectoryPath {
		if(!isDirectory()) throw VfsError.WrongType.create(this as Path, expected = PathType.directory)
		return NioDirectoryPath(fileSystem, path)
	}
	
	public suspend fun createDirectory(recursive: Boolean): DirectoryPath {
		io {
			if(recursive) {
				path.createDirectories()
			} else {
				path.createDirectory()
			}
		}
		
		return NioDirectoryPath(fileSystem, path)
	}
	
	public override suspend fun isFile(): Boolean = io { path.isRegularFile() }
	
	public override suspend fun isDirectory(): Boolean = io { path.isDirectory() }
	
	public override suspend fun exists(): Boolean = io { path.exists() }
	
	public override suspend fun moveTo(path: Path, overwrite: Boolean): Unit = io {
		if(path is NioPath) {
			@Suppress("BlockingMethodInNonBlockingContext")
			this.path.moveTo(path.path, overwrite = overwrite)
		} else {
			VfsCommon.moveTo(from = this as Path, to = path, overwrite = overwrite)
		}
	}
	
	
	public override fun toString(): String = absolutePath
}
