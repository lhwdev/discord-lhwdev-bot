package com.lhwdev.vfs

import java.time.Instant


public sealed interface PathMetadata : PathPermissionObject {
	public val path: Path
	
	
	// Properties
	public suspend fun <T : Any> get(key: MetadataKey<T>): T?
	public suspend fun <T : Any> set(key: MutableMetadataKey<T>, value: T): Boolean
	
	public suspend fun sizeBytes(): Long
}


public open class MetadataKey<T : Any>(public val name: String)
public open class MutableMetadataKey<T : Any>(name: String) : MetadataKey<T>(name)

public object DefaultMetadataKeys {
	public val createdTime: MutableMetadataKey<Instant> = MutableMetadataKey("default:created")
	public val lastModifiedTime: MutableMetadataKey<Instant> = MutableMetadataKey("default:lastModified")
	public val lastAccessedTime: MutableMetadataKey<Instant> = MutableMetadataKey("default:lastAccessed")
	
	public val isHidden: MutableMetadataKey<Boolean> = MutableMetadataKey("default:isHidden")
	public val isSymbolicLink: MetadataKey<Boolean> = MetadataKey("default:isSymlink")
}


public suspend fun PathMetadata.createdTime(): Instant? = get(DefaultMetadataKeys.createdTime)
public suspend fun PathMetadata.lastModifiedTime(): Instant? = get(DefaultMetadataKeys.lastModifiedTime)
public suspend fun PathMetadata.lastAccessedTime(): Instant? = get(DefaultMetadataKeys.lastAccessedTime)

public suspend fun PathMetadata.isHidden(): Boolean? = get(DefaultMetadataKeys.isHidden)
public suspend fun PathMetadata.isSymbolicLink(): Boolean? = get(DefaultMetadataKeys.isSymbolicLink)


public interface FileMetadata : PathMetadata {
	override val path: FilePath
}

public interface DirectoryMetadata : PathMetadata {
	override val path: DirectoryPath
}
