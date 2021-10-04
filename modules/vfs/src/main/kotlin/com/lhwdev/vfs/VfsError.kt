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
	init {
		TODO("not complete")
	}
	
	public sealed class WithPath(public val path: Path) : VfsError()
	
	public abstract val errorCode: String
	
	// ENOENT
	public class NoEntry(path: Path) : WithPath(path) {
		override val errorCode: String get() = "ENOENT"
		override val message: String get() = "$path does not exist"
	}
	
	// EACCES
	public class NoAccess(path: Path) : WithPath(path) {
		override val errorCode: String get() = "EACCES"
	}
	
	// EEXIST
	public class FileExist(path: Path) : WithPath(path) {
		override val errorCode: String get() = "EEXIST"
	}
	
	// ENOTDIR, EISDIR
	public class WrongType(path: Path, public val actual: PathType) : WithPath(path) {
		override val errorCode: String
			get() = when(actual) {
				PathType.file -> "ENOTDIR"
				PathType.directory -> "EISDIR"
			}
	}
	
	// EIO, etc.
	public open class Other(
		public override val errorCode: String,
		override val message: String?,
		override val cause: Throwable?
	) : VfsError()
}
