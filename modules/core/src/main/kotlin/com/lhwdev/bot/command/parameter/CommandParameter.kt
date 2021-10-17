package com.lhwdev.bot.command.parameter

import androidx.compose.runtime.State


interface CommandParameter<out T> : State<T>
