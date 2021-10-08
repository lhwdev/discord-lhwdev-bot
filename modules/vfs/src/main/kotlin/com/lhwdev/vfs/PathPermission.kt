package com.lhwdev.vfs


public interface PathPermissionObject {
	// Permissions
	public val permission: PathPermission
	public val owner: PermissionSubject
	
	public fun testPermission(from: PermissionSubject): PathPermission
}


public interface PermissionSubject {
	public val name: String
}


public interface PathPermission {
	public class Key(public val name: String) {
		public companion object Default {
			public val readable: Key = Key("default:readable")
			public val writable: Key = Key("default:writable")
			public val executable: Key = Key("default:executable")
			public val modifyAttributes: Key = Key("default:modifyAttributes")
		}
		
		override fun toString(): String = "permission $name"
	}
	
	public suspend fun get(key: Key): Boolean
	public suspend fun set(key: Key, permission: Boolean): Boolean
}


public suspend fun PathPermission.isReadable(): Boolean = get(PathPermission.Key.readable)
public suspend fun PathPermission.isWritable(): Boolean = get(PathPermission.Key.writable)
public suspend fun PathPermission.isExecutable(): Boolean = get(PathPermission.Key.executable)
