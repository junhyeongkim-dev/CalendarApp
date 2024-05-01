package com.android.calendarapp.feature.user.domain.usecase

import com.android.calendarapp.feature.user.data.repository.UserRepository
import com.android.calendarapp.feature.user.domain.convert.toModel
import com.android.calendarapp.feature.user.domain.model.UserModel
import com.android.calendarapp.library.security.preperence.helper.ISharedPreferencesHelper
import com.android.calendarapp.library.security.tink.helper.TinkHelper
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import javax.inject.Inject

class GetUserUseCaseImpl @Inject constructor(
    private val preferencesHelper: ISharedPreferencesHelper,
    private val userRepository: UserRepository,
    private val tinkHelper: TinkHelper,
) : GetUserUseCase {
    override suspend fun invoke() : Flow<UserModel> {
        return withContext(Dispatchers.IO) {
            val userId = preferencesHelper.getUserId()
            userRepository.selectUser(userId).map { userEntity ->
                userEntity.toModel(tinkHelper)
            }
        }
    }
}