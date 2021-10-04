package com.lhwdev.vfs


public interface PathPermissionObject {
	// Permissions
	public val permission: PathPermission
	
	public fun testPermission(from: PermissionSubject): PathPermission
}


public interface PermissionSubject {
	public val name: String
}


public interface PathPermission {
	public class Key {
		public companion object Default {
			public val readable: Key = Key()
			public val writable: Key = Key()
			public val executable: Key = Key()
			public val modifyAttributes: Key = Key()
		}
	}
	
	
	public var isReadable: Boolean
	public var isWritable: Boolean
	public var isExecutable: Boolean
	
	public operator fun get(key: Key): Boolean
	public operator fun set(key: Key, permission: Boolean)
}
