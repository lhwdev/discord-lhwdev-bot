package com.lhwdev.vfs


public val Path.nameWithoutExtension: String get() = name.substringBeforeLast('.')
public val Path.extension: String get() = name.substringAfterLast('.')


public suspend fun Path.notExists(): Boolean = !exists()

public suspend fun Path.checkExist() {
	if(notExists()) {
		throw VfsError.NoEntry(this)
	}
}

public suspend fun Path.checkFile() {
	if(!isFile()) {
		if(notExists()) throw VfsError.NoEntry(this)
		else throw VfsError.WrongType(this, expected = PathType.file, actual = PathType.directory)
	}
}

public suspend fun Path.checkDirectory() {
	if(!isDirectory()) {
		if(notExists()) throw VfsError.NoEntry(this)
		else throw VfsError.WrongType(this, expected = PathType.file, actual = PathType.file)
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


public suspend fun Path.asAnyPath(): AnyPath = when(this) {
	is UnknownPath -> asAnyPath()
	is AnyPath -> this
}

public suspend fun Path.deleteRecursive() {
	asAnyPath().deleteRecursive()
}

public suspend fun AnyPath.deleteRecursive() {
	when(this) {
		is FilePath -> delete()
		is DirectoryPath -> deleteRecursive()
	}
}

public suspend fun Path.deleteSingle() {
	asAnyPath().deleteSingle()
}

public suspend fun AnyPath.deleteSingle() {
	when(this) {
		is FilePath -> delete()
		is DirectoryPath -> deleteSingle()
	}
}

public suspend inline fun <R> FilePath.read(block: (FileRead) -> R): R {
	val input = openRead()
	return input.use(block)
}

public suspend inline fun <R> FilePath.append(block: (FileWrite) -> R): R {
	val input = openAppend()
	return input.use(block)
}

public suspend inline fun <R> FilePath.write(block: (FileWrite) -> R): R {
	val input = openWrite()
	return input.use(block)
}
