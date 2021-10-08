package com.lhwdev.vfs.impl.niofs

import com.lhwdev.vfs.FileSystem
import com.lhwdev.vfs.UnknownPath


public class NioFileSystem(private val fs: java.nio.file.FileSystem, public val protocol: String) : FileSystem {
	override val separator: String
		get() = fs.separator
	
	override fun resolve(path: String): UnknownPath {
		return NioUnknownPath(this, fs.getPath(path))
	}
	
	override fun resolve(vararg path: String): UnknownPath {
		return NioUnknownPath(this, fs.getPath(path[0], *path.copyOfRange(1, path.size)))
	}
}
