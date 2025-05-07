package com.example.rest.businessLayer

import com.example.rest.businessLayer.boundaries.SignsDataSourceGateway
import com.example.rest.businessLayer.boundaries.SignsInputBoundary

class SignsUseCase(
    private val signsDataSourceGateway: SignsDataSourceGateway,
) : SignsInputBoundary {
    override fun createSign(requestModel: Any): Result<Any> {
        TODO("Not yet implemented")
    }

    override fun getSigns(
        idRoad: Int,
        direction: Int,
        latitude: Double,
        longitude: Double,
    ): Result<Any> {
        TODO("Not yet implemented")
    }
    /*
    override fun createSign(requestModel: UserRequestModel): Result<UserResponseModel> {
        if (signsDataSourceGateway.existsByName(requestModel.name)) {
            return Result.failure(UserAlreadyPresentException())
        }
        if (!PasswordPolicy.isValid(requestModel.password)) {
            return Result.failure(PasswordToShortException())
        }
        val hashedPassword = userSecurity.getHash(requestModel.password)
        val user: User = User.create(requestModel.name, hashedPassword, requestModel.role)
        val now = LocalDateTime.now()
        val userDataSourceModel =
            UserDataSourceRequestModel(
                user.name,
                hashedPassword,
                user.role,
                now,
                listOf(hashedPassword),
            )
        signsDataSourceGateway.save(userDataSourceModel)
        val token = userSecurity.generateToken(user.name)

        val accountResponseModel = UserResponseModel(user.name, token, now.toString())
        return Result.success(accountResponseModel)
    }

    override fun login(requestModel: LoginRequestModel): Result<LoginResponseModel> {
        if (!signsDataSourceGateway.existsByName(requestModel.username)) {
            return Result.failure(UserNotFound())
        }
        val userDataSourceModel =
            signsDataSourceGateway.findUser(requestModel.username) ?: return Result.failure(UserNotFound())
        if (!userSecurity.checkPassword(requestModel.password, userDataSourceModel.password)) {
            return Result.failure(WrongPasswordException())
        }
        val token = userSecurity.generateToken(requestModel.username)
        val loginResponse = LoginResponseModel(requestModel.username, token)
        return Result.success(loginResponse)
    }

    override fun checkUserToken(token: String): Result<TokenResponseModel> {
        val tokenValidation = userSecurity.validateToken(token)
        if (tokenValidation.isFailure) {
            return Result.failure(InvalidTokenException())
        }
        if (!tokenValidation.getOrNull()!!) {
            return Result.failure(TokenExpiredException())
        }
        val name = userSecurity.tokenUser(token)
        val user = signsDataSourceGateway.findUser(name) ?: return Result.failure(UserNotFound())
        return Result.success(TokenResponseModel(user.name, user.role))
    }
     */
}
