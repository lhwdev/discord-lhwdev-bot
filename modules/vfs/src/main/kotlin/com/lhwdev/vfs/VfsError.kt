package com.lhwdev.vfs

import java.io.IOException


/*
 * ENOTDIR	/* Not a directory */
 * EISDIR	/* Is a directory */
 * ENOSPC	/* No space left on device */
 * EROFS	/* Read only file system */
 * ENONET	/* Machine is not on the network */
 * EFTYPE	/* Inappropriate file type or format */
 * EBADFD	/* f.d. invalid for this operation */
 * EREMCHG	/* Remote address changed */
 * ENOTEMPTY	/* Directory not empty */
 * ENAMETOOLONG	/* File or path name too long */
 * ECONNRESET  /* Connection reset by peer */
 * ENOPROTOOPT	/* Protocol not available */
 * ECONNREFUSED	/* Connection refused */
 * EADDRINUSE		/* Address already in use */
 * ECONNABORTED	/* Connection aborted */
 * ENETUNREACH		/* Network is unreachable */
 * ENETDOWN		/* Network interface is not configured */
 * ETIMEDOUT		/* Connection timed out */
 * EHOSTDOWN		/* Host is down */
 * EHOSTUNREACH	/* Host is unreachable */
 * EPROTONOSUPPORT	/* Unknown protocol */
 * EADDRNOTAVAIL	/* Address not available */
 * ECASECLASH  /* Filename exists with different case */
 */



@Suppress("SpellCheckingInspection")
public sealed class VfsError : IOException() {
	public abstract override val message: String
	
	
	public sealed class WithPath(public val path: Path) : VfsError()
	
	public abstract val errorCode: String?
	
	// ENOENT
	public class NoEntry(path: Path) : WithPath(path) {
		override val errorCode: String get() = "ENOENT"
		override val message: String get() = "'$path' does not exist"
	}
	
	// EACCES
	public class AccessDenied(path: Path, public val reason: String?) : WithPath(path) {
		override val errorCode: String get() = "EACCES"
		override val message: String get() = "Cannot access '$path' (${reason ?: "..."})"
	}
	
	// EEXIST
	public class FileAlreadyExists(path: Path) : WithPath(path) {
		override val errorCode: String get() = "EEXIST"
		override val message: String get() = "'$path' already exists"
	}
	
	// ENOTEMPTY
	public class DirectoryNotEmpty(path: Path, public val content: String? = null) : WithPath(path) {
		override val errorCode: String get() = "ENOTEMPTY"
		override val message: String
			get() = buildString {
				append("Directory '")
				append(path)
				append("' is not empty")
				if(content != null) {
					append("( ")
					append(content)
					append(")")
				}
			}
	}
	
	
	// ENOTDIR, EISDIR
	public class WrongType(path: Path, public val expected: PathType?, public val actual: PathType?) : WithPath(path) {
		public companion object {
			public suspend fun create(path: Path, expected: PathType?): VfsError {
				val type = path.type()
				return if(type == null) {
					NoEntry(path)
				} else {
					WrongType(path, expected = expected, actual = type)
				}
			}
		}
		
		override val errorCode: String
			get() = when(expected) {
				PathType.file -> when(actual) {
					PathType.directory, null -> "EISDIR"
					PathType.file -> "?"
				}
				PathType.directory -> when(actual) {
					PathType.file, null -> "ENOTDIR"
					PathType.directory -> "?"
				}
				null -> when(actual) { // guess
					PathType.file -> "ENOTDIR"
					PathType.directory -> "EISDIR"
					null -> "?"
				}
			}
		override val message: String
			get() = "'$path' is expected to be ${expected ?: "(unknown)"} but it is ${actual ?: "(IDK)"}"
	}
	
	public class NotSupported(public val operation: String, public val by: String) : VfsError() {
		override val errorCode: String? get() = null
		override val message: String get() = "$operation is not supported by $by"
	}
	
	// EIO, etc.
	public open class Other(
		public override val errorCode: String? = null,
		override val message: String,
		override val cause: Throwable?
	) : VfsError()
	
	// EIO, etc.
	public open class OtherWithPath(
		path: Path,
		public override val errorCode: String? = null,
		override val message: String,
		override val cause: Throwable?
	) : WithPath(path)
}
