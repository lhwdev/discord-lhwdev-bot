@file:Suppress("BlockingMethodInNonBlockingContext")

package com.lhwdev.vfs.impl.niofs

import com.lhwdev.vfs.WatchEventKind
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.runInterruptible
import java.nio.file.*
import kotlin.io.path.isDirectory


private fun kindToNio(kind: WatchEventKind) = when(kind) {
	WatchEventKind.create -> StandardWatchEventKinds.ENTRY_CREATE
	WatchEventKind.modify -> StandardWatchEventKinds.ENTRY_MODIFY
	WatchEventKind.delete -> StandardWatchEventKinds.ENTRY_DELETE
}

private fun kindFromNio(kind: WatchEvent.Kind<*>) = when(kind) {
	StandardWatchEventKinds.ENTRY_CREATE -> WatchEventKind.create
	StandardWatchEventKinds.ENTRY_MODIFY -> WatchEventKind.modify
	StandardWatchEventKinds.ENTRY_DELETE -> WatchEventKind.delete
	else -> null
}


internal suspend fun watchPath(
	fileSystem: NioFileSystem,
	path: Path,
	kinds: Array<out WatchEventKind>,
	recursive: Boolean
) = io {
	val watchService = path.fileSystem.newWatchService()
	val keys = HashMap<WatchKey, Path>()
	
	val kindList = ArrayList<WatchEvent.Kind<*>>(kinds.size + 1)
	for(kind in kinds) {
		kindList += kindToNio(kind)
	}
	if(recursive && WatchEventKind.create !in kinds) {
		kindList += StandardWatchEventKinds.ENTRY_CREATE
	}
	
	val nioKinds = kindList.toTypedArray()
	
	fun registerAll(path: Path) = Files.walk(path).use { stream ->
		for(item in stream) {
			if(item.isDirectory(LinkOption.NOFOLLOW_LINKS)) {
				val key = item.register(watchService, *nioKinds)
				keys[key] = item
			}
		}
	}
	
	if(recursive) {
		registerAll(path)
	} else {
		keys[path.register(watchService, *nioKinds)] = path
	}
	
	flow {
		watchService.use {
			while(true) {
				val key = runInterruptible { watchService.take() }
				
				for(event in key.pollEvents()) {
					val kind = event.kind()
					if(kind == StandardWatchEventKinds.OVERFLOW) continue
					
					val eventKind = kindFromNio(kind) ?: continue
					val eventPath = path.resolve(event.context() as Path)
					
					if(recursive && eventPath.isDirectory(LinkOption.NOFOLLOW_LINKS)) {
						registerAll(eventPath)
					}
					
					val valid = key.reset()
					if(!valid) {
						keys -= key
						if(keys.isEmpty()) break
					}
					
					emit(com.lhwdev.vfs.WatchEvent(eventKind, NioUnknownPath(fileSystem, eventPath)))
				}
			}
		}
	}.flowOn(Dispatchers.IO)
}
