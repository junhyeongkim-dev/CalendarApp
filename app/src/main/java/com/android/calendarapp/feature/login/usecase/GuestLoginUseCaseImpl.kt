package com.android.calendarapp.feature.login.usecase

import com.android.calendarapp.feature.user.domain.model.UserModel
import com.android.calendarapp.feature.user.domain.usecase.AddUserUseCase
import com.android.calendarapp.library.login.model.LoginResponseModel
import com.android.calendarapp.library.login.constant.LoginConstant
import com.android.calendarapp.library.login.type.LoginType
import com.android.calendarapp.library.security.preperence.PrefStorageProvider
import com.android.calendarapp.library.security.preperence.constants.PrefStorageConstance
import kotlinx.coroutines.channels.Channel
import javax.inject.Inject

class GuestLoginUseCaseImpl @Inject constructor(
    private val perfStorageProvider: PrefStorageProvider,
    private val addUserUseCase: AddUserUseCase
) : GuestLoginUseCase {
    override suspend fun invoke(result: Channel<LoginResponseModel>) {

        perfStorageProvider.setString(PrefStorageConstance.USER_ID_PREF, LoginType.GUEST.name)

        val userModel = UserModel(
            userId = LoginType.GUEST.name,
            userName = "게스트",
            userType = LoginType.GUEST
        )

        addUserUseCase(userModel).also {
            result.send(
                LoginResponseModel(
                    code = LoginConstant.SUCCESS,
                    description = LoginConstant.SUCCESS_DESC
                )
            )
        }
    }
}