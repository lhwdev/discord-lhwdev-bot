package com.lhwdev.vfs


public enum class WatchEventKind { create, modify, delete }


public class WatchEvent(public val event: WatchEventKind, public val path: UnknownPath)
