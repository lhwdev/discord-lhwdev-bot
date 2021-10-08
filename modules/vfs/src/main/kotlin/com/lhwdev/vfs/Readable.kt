package com.lhwdev.vfs


public interface Readable {
	public suspend fun openRead(): FileRead
}

public interface Writeable {
	public suspend fun openWrite(): FileWrite
	public suspend fun openAppend(): FileWrite
}
