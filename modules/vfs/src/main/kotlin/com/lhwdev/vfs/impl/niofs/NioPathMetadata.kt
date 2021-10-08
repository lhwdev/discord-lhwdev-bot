@file:Suppress("BlockingMethodInNonBlockingContext")

package com.lhwdev.vfs.impl.niofs

import com.lhwdev.vfs.*
import java.nio.file.Files
import java.nio.file.attribute.BasicFileAttributeView
import java.nio.file.attribute.BasicFileAttributes
import java.nio.file.attribute.DosFileAttributeView
import java.nio.file.attribute.FileTime
import java.time.Instant
import kotlin.io.path.fileAttributesView
import kotlin.io.path.fileAttributesViewOrNull


public suspend fun nioPathMetadata(fileSystem: NioFileSystem, path: NioPath): PathMetadata = io {
	val view = path.path.fileAttributesView<BasicFileAttributeView>()
	
	@Suppress("BlockingMethodInNonBlockingContext")
	val attributes = view.readAttributes()
	
	if(attributes.isRegularFile) {
		NioFileMetadata(NioFilePath(fileSystem, path.path), view, attributes)
	} else {
		NioDirectoryMetadata(NioDirectoryPath(fileSystem, path.path), view, attributes)
	}
}


public sealed class NioPathMetadata(
	protected val nioPath: java.nio.file.Path,
	private val view: BasicFileAttributeView,
	private var attributes: BasicFileAttributes?
) {
	// Metadata
	private fun attributes() = attributes ?: run {
		val attr = view.readAttributes()
		attributes = attr
		attr
	}
	
	@Suppress("UNCHECKED_CAST")
	public suspend fun <T : Any> get(key: MetadataKey<T>): T? = when(key) {
		DefaultMetadataKeys.isHidden -> io { Files.isHidden(nioPath) }
		DefaultMetadataKeys.isSymbolicLink -> io { Files.isSymbolicLink(nioPath) }
		
		DefaultMetadataKeys.createdTime -> attributes().creationTime()
		DefaultMetadataKeys.lastModifiedTime -> attributes().lastModifiedTime()
		DefaultMetadataKeys.lastAccessedTime -> attributes().lastAccessTime()
		
		else -> null
	} as T?
	
	public suspend fun <T : Any> set(key: MutableMetadataKey<T>, value: T): Boolean {
		when(key) {
			DefaultMetadataKeys.isHidden -> io {
				val view = nioPath.fileAttributesViewOrNull<DosFileAttributeView>()
					?: throw VfsError.NotSupported(
						operation = "Set isHidden (DefaultMetadataKeys.isHidden)",
						by = "file system"
					)
				
				view.setHidden(value as Boolean)
			}
			
			DefaultMetadataKeys.createdTime ->
				view.setTimes(null, null, FileTime.from(value as Instant))
			DefaultMetadataKeys.lastModifiedTime ->
				view.setTimes(FileTime.from(value as Instant), null, null)
			DefaultMetadataKeys.lastAccessedTime ->
				view.setTimes(null, FileTime.from(value as Instant), null)
			
			else -> return false
		}
		
		return true
	}
	
	
	@Suppress("RedundantSuspendModifier")
	public suspend fun sizeBytes(): Long = attributes().size()
	
	
	// Permission
	
	public val permission: PathPermission get() = NioPathPermission(nioPath)
	public val owner: PermissionSubject get() = TODO()
	
	public fun testPermission(from: PermissionSubject): PathPermission {
		TODO()
	}
}

public class NioFileMetadata(
	override val path: NioFilePath, view: BasicFileAttributeView, attributes: BasicFileAttributes?
) : NioPathMetadata(path.path, view, attributes), FileMetadata

public class NioDirectoryMetadata(
	override val path: NioDirectoryPath, view: BasicFileAttributeView, attributes: BasicFileAttributes?
) : NioPathMetadata(path.path, view, attributes), DirectoryMetadata
