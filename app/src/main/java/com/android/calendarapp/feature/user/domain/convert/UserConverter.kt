package com.android.calendarapp.feature.user.domain.convert

import com.android.calendarapp.feature.user.data.entity.UserEntity
import com.android.calendarapp.feature.user.domain.model.UserModel
import com.android.calendarapp.library.security.tink.helper.TinkHelper

fun UserModel.toEntity(tinkHelper: TinkHelper): UserEntity = UserEntity (
    userId = this.userId,
    userName = tinkHelper.stringEncrypt(this.userName, this.userId),
    userBirth = tinkHelper.stringEncrypt(this.userBirth, this.userId),
    userType = this.userType
)

fun UserEntity.toModel(tinkHelper: TinkHelper): UserModel = UserModel(
    userId = this.userId,
    userName = tinkHelper.stringDecrypt(this.userName, this.userId),
    userBirth = tinkHelper.stringDecrypt(this.userBirth, this.userId),
    userType = this.userType
)