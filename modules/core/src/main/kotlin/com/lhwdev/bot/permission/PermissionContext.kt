package com.lhwdev.bot.permission

import kotlinx.coroutines.asContextElement
import kotlinx.coroutines.withContext
import kotlin.contracts.ExperimentalContracts
import kotlin.contracts.InvocationKind
import kotlin.contracts.contract


@PublishedApi
internal val sLocalPermissionContext: ThreadLocal<PermissionContext?> = object : ThreadLocal<PermissionContext?>() {
	override fun initialValue(): PermissionContext = sDefaultPermission
}


private val sDefaultPermission = PermissionContext()

public val permissionContext: PermissionContext? // null = bypass
	get() = sLocalPermissionContext.get()


@OptIn(ExperimentalContracts::class)
public suspend inline fun <R> withPermission(
	context: PermissionContext?,
	crossinline block: suspend () -> R
): R {
	contract {
		callsInPlace(block, InvocationKind.EXACTLY_ONCE)
	}
	
	val newContext = sLocalPermissionContext.get().mergeSafe(context)
	
	return withContext(sLocalPermissionContext.asContextElement(newContext)) {
		block()
	}
}

@OptIn(ExperimentalContracts::class)
public suspend inline fun <R> withForcedPermission(
	context: PermissionContext?,
	crossinline block: suspend () -> R
): R {
	contract {
		callsInPlace(block, InvocationKind.EXACTLY_ONCE)
	}
	
	return withContext(sLocalPermissionContext.asContextElement(context)) {
		block()
	}
}


public class PermissionContext {
	public fun mergeSafe(other: PermissionContext): PermissionContext = this
}


@JvmName("mergeSafeOtherNullable")
public fun PermissionContext.mergeSafe(other: PermissionContext?): PermissionContext =
	if(other == null) this else mergeSafe(other)

@JvmName("mergeSafeThisNullable")
public fun PermissionContext?.mergeSafe(other: PermissionContext): PermissionContext =
	if(this == null) other else mergeSafe(other)

@JvmName("mergeSafeAllNullable")
public fun PermissionContext?.mergeSafe(other: PermissionContext?): PermissionContext? = when {
	this == null -> other
	other == null -> this
	else -> mergeSafe(other)
}
