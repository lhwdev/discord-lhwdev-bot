package com.lhwdev.vfs


public interface SuspendCloseable {
	public suspend fun close()
}


public suspend inline fun <T : SuspendCloseable, R> T.use(block: (T) -> R): R = try {
	block(this)
} finally {
	close()
}
