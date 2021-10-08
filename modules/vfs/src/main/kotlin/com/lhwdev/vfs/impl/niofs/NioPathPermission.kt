package com.lhwdev.vfs.impl.niofs

import com.lhwdev.vfs.PathPermission
import com.lhwdev.vfs.VfsError
import java.nio.file.Files


// // There is no good setting permission thing for java nio; only in platform specific.
// // So we will:
// // - 1. rollback to good old File.setXXX
// // - 2. try POSIX + Acl(Windows) thing both
//
// private fun java.nio.file.Path.toFileOrNull() = try { toFile() } catch(th: UnsupportedOperationException) { null }


public class NioPathPermission(private val path: java.nio.file.Path) : PathPermission {
	override suspend fun get(key: PathPermission.Key): Boolean = when(key) {
		PathPermission.Key.readable -> Files.isReadable(path)
		PathPermission.Key.writable -> Files.isWritable(path)
		PathPermission.Key.executable -> Files.isExecutable(path)
		PathPermission.Key.modifyAttributes -> TODO()
		else -> throw VfsError.NotSupported(operation = "Get $key", by = "NioPathPermission")
	}
	
	override suspend fun set(key: PathPermission.Key, permission: Boolean): Boolean {
		TODO()
		// val file = path.toFileOrNull()
		// if(file != null) {
		// 	file.setReadable(true)
		// } else {
		// 	val posix = path.fileAttributesViewOrNull<PosixFileAttributeView>()
		// 	if(posix != null) {
		// 		posix.setPermissions(
		// 			posix.readAttributes().permissions() + PosixFilePermission.GROUP_READ
		// 		)
		// 	} else {
		// 		val acl = path.fileAttributesViewOrNull<AclFileAttributeView>()
		// 		if(acl != null) {
		// 			acl.acl = acl.acl +
		// 		} else {
		// 			// disgusting indents...
		// 			throw VfsError.NotSupported(operation = "Set isReadable", by = "fs/os")
		// 		}
		// 	}
		// }
	}
}
