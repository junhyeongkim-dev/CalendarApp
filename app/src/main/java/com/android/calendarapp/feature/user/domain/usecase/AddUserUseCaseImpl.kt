package com.android.calendarapp.feature.user.domain.usecase

import com.android.calendarapp.feature.user.data.repository.UserRepository
import com.android.calendarapp.feature.user.domain.convert.toEntity
import com.android.calendarapp.feature.user.domain.model.UserModel
import com.android.calendarapp.library.security.tink.helper.TinkHelper
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject
class AddUserUseCaseImpl @Inject constructor(
    private val userRepository: UserRepository,
    private val tinkHelper: TinkHelper
) : AddUserUseCase {
    override suspend fun invoke(userModel: UserModel) =
        withContext(Dispatchers.IO) {
            userRepository.insertUser(userModel.toEntity(tinkHelper))
        }
}

