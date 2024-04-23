package com.android.calendarapp.feature.user.domain.convert

import com.android.calendarapp.feature.user.data.entity.UserEntity
import com.android.calendarapp.feature.user.domain.model.UserModel

fun UserModel.toEntity(): UserEntity = UserEntity (
    userId = this.userId,
    userName = this.userName,
    userBirth = this.userBirth,
    userType = this.userType
)

fun UserEntity.toModel(): UserModel = UserModel(
    userId = this.userId,
    userName = this.userName,
    userBirth = this.userBirth,
    userType = this.userType
)