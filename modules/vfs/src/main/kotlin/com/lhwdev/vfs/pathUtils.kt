package com.lhwdev.vfs

import java.io.InputStream
import java.io.OutputStream


public val Path.nameWithoutExtension: String get() = name.substringBeforeLast('.')
public val Path.extension: String get() = name.substringAfterLast('.')


public suspend fun Path.exists(): Boolean = type() != null
public suspend fun Path.notExists(): Boolean = type() == null

public suspend fun Path.isFile(): Boolean = type() == PathType.file

public suspend fun Path.isDirectory(): Boolean = type() == PathType.directory

public suspend fun Path.checkExist() {
	if(notExists()) {
		throw VfsError.NoEntry(this)
	}
}

public suspend fun Path.checkFile() {
	if(!isFile()) {
		if(notExists()) throw VfsError.NoEntry(this)
		else throw VfsError.WrongType(this, actual = PathType.directory)
	}
}

public suspend fun Path.checkDirectory() {
	if(!isDirectory()) {
		if(notExists()) throw VfsError.NoEntry(this)
		else throw VfsError.WrongType(this, actual = PathType.file)
	}
}


public fun Path.asUnknown(): UnknownPath = when(this) {
	is DirectoryPath -> asUnknown()
	is FilePath -> asUnknown()
	is UnknownPath -> this
}

public suspend fun UnknownPath.ensureFile(): FilePath = if(notExists()) {
	createFile()
} else {
	asFile()
}

public suspend fun UnknownPath.ensureFileWithDir(recursive: Boolean = true): FilePath = if(exists()) {
	asFile()
} else {
	createFileWithDir(recursive = recursive)
}

public suspend fun UnknownPath.ensureDirectory(recursive: Boolean = true): DirectoryPath = if(exists()) {
	asDirectory()
} else {
	createDirectory(recursive = recursive)
}


public suspend inline fun <R> FilePath.read(block: (InputStream) -> R): R {
	val input = openRead()
	return input.use(block)
}

public suspend inline fun <R> FilePath.append(block: (OutputStream) -> R): R {
	val input = openAppend()
	return input.use(block)
}

public suspend inline fun <R> FilePath.write(block: (OutputStream) -> R): R {
	val input = openWrite()
	return input.use(block)
}
