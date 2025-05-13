package com.example.rest.businessLayer

import com.example.rest.businessLayer.adapter.sign.SignDataSourceModel
import com.example.rest.businessLayer.adapter.sign.SignModel
import com.example.rest.businessLayer.boundaries.SignsDataSourceGateway
import com.example.rest.businessLayer.boundaries.SignsInputBoundary
import com.example.rest.domainLayer.GeoLocationPosition
import com.example.rest.domainLayer.LaneReference
import com.example.rest.domainLayer.TrafficSign
import com.example.rest.domainLayer.lane.AllLaneReference
import com.example.rest.domainLayer.lane.FromLaneReference
import com.example.rest.domainLayer.lane.RangeLaneReference
import com.example.rest.domainLayer.lane.SingleLaneReference
import com.example.rest.domainLayer.lane.ToLaneReference
import com.example.rest.domainLayer.trafficSign.AllSigns
import com.example.rest.domainLayer.trafficSign.MaximumSpeedLimitSign
import com.example.rest.domainLayer.trafficSign.MinimumSpeedLimitSign
import com.example.rest.domainLayer.trafficSign.NoEntrySign
import com.example.rest.domainLayer.trafficSign.SpeedUnit
import com.example.rest.domainLayer.trafficSign.StopSign
import com.example.rest.domainLayer.trafficSign.YieldSign

class SignsUseCase(
    private val signsDataSourceGateway: SignsDataSourceGateway,
) : SignsInputBoundary {
    override fun allSignsTypes(): Result<List<String>> = Result.success(AllSigns.listOfType)

    override fun createSign(requestModel: SignModel): Result<SignModel> {
        val sign = mapToModel(requestModel)
        if (sign == null) {
            return Result.failure(IllegalArgumentException("Invalid sign type"))
        }
        val signDataSourceModel = modelToDb(sign)
        if (signDataSourceModel == null) {
            return Result.failure(IllegalArgumentException("Invalid sign data source model"))
        }
        signsDataSourceGateway.save(signDataSourceModel)
        return Result.success(mapToResponse(sign))
    }

    override fun getSigns(
        idRoad: Int,
        direction: Int,
        latitude: Double,
        longitude: Double,
    ): Result<List<SignModel>> {
        val signDataSourceModels =
            signsDataSourceGateway.findSigns(idRoad, direction, latitude, longitude)
        val signModels = signDataSourceModels.mapNotNull { mapToModelFromDb(it) }
        return Result.success(signModels.map { mapToResponse(it) })
    }

    private fun modelToDb(sign: TrafficSign): SignDataSourceModel? {
        val signType = sign.trafficSignType
        val lane = convertFromLaneReference(sign.laneReference)
        if (lane == null) {
            return null
        }
        return when (signType) {
            is NoEntrySign ->
                SignDataSourceModel(
                    type = "NoEntrySign",
                    category = signType.category.name,
                    idRoad = sign.roadId,
                    direction = sign.directionId,
                    latitude = sign.geoLocationPosition.latitude,
                    longitude = sign.geoLocationPosition.longitude,
                    lanes = lane,
                    speedLimit = null,
                    unit = null,
                )
            is MaximumSpeedLimitSign ->
                SignDataSourceModel(
                    type = "MaxSpeedLimitSign",
                    category = signType.category.name,
                    idRoad = sign.roadId,
                    direction = sign.directionId,
                    latitude = sign.geoLocationPosition.latitude,
                    longitude = sign.geoLocationPosition.longitude,
                    lanes = lane,
                    speedLimit = signType.maximumSpeedLimit,
                    unit = signType.speedUnit.name,
                )
            is MinimumSpeedLimitSign ->
                SignDataSourceModel(
                    type = "MinSpeedLimitSign",
                    category = signType.category.name,
                    idRoad = sign.roadId,
                    direction = sign.directionId,
                    latitude = sign.geoLocationPosition.latitude,
                    longitude = sign.geoLocationPosition.longitude,
                    lanes = lane,
                    speedLimit = signType.minimumSpeedLimit,
                    unit = signType.speedUnit.name,
                )
            is StopSign ->
                SignDataSourceModel(
                    type = "StopSign",
                    category = signType.category.name,
                    idRoad = sign.roadId,
                    direction = sign.directionId,
                    latitude = sign.geoLocationPosition.latitude,
                    longitude = sign.geoLocationPosition.longitude,
                    lanes = lane,
                    speedLimit = null,
                    unit = null,
                )
            is YieldSign ->
                SignDataSourceModel(
                    type = "YieldSign",
                    category = signType.category.name,
                    idRoad = sign.roadId,
                    direction = sign.directionId,
                    latitude = sign.geoLocationPosition.latitude,
                    longitude = sign.geoLocationPosition.longitude,
                    lanes = lane,
                    speedLimit = null,
                    unit = null,
                )

            else -> null
        }
    }

    private object SignTypes {
        const val NO_ENTRY = "NoEntrySign"
        const val MAX_SPEED = "MaxSpeedLimitSign"
        const val MIN_SPEED = "MinSpeedLimitSign"
        const val STOP = "StopSign"
        const val YIELD = "YieldSign"
    }

    private fun SignModel.toGeoLocation() = GeoLocationPosition(latitude, longitude)

    private fun SignDataSourceModel.toGeoLocation() = GeoLocationPosition(latitude, longitude)

    private fun mapToModel(signModel: SignModel): TrafficSign? =
        when (signModel.type) {
            SignTypes.NO_ENTRY ->
                createBaseSignFromSignModel(signModel) {
                    trafficSignType(NoEntrySign)
                }
            SignTypes.MAX_SPEED ->
                createBaseSignFromSignModel(signModel) {
                    trafficSignType(
                        MaximumSpeedLimitSign(
                            signModel.speedLimit!!,
                            SpeedUnit.valueOf(signModel.unit!!),
                        ),
                    )
                }
            SignTypes.MIN_SPEED ->
                createBaseSignFromSignModel(signModel) {
                    trafficSignType(
                        MinimumSpeedLimitSign(
                            signModel.speedLimit!!,
                            SpeedUnit.valueOf(signModel.unit!!),
                            convertToLaneReference(signModel.lanes),
                        ),
                    )
                }
            SignTypes.STOP ->
                createBaseSignFromSignModel(signModel) {
                    trafficSignType(StopSign(convertToLaneReference(signModel.lanes)))
                }
            SignTypes.YIELD ->
                createBaseSignFromSignModel(signModel) {
                    trafficSignType(YieldSign(convertToLaneReference(signModel.lanes)))
                }
            else -> null
        }

    private fun createBaseSignFromSignModel(
        signModel: SignModel,
        configurator: TrafficSign.Builder.() -> TrafficSign.Builder,
    ): TrafficSign =
        TrafficSign
            .Builder()
            .roadId(signModel.idRoad)
            .directionId(signModel.direction)
            .geoLocationPosition(signModel.toGeoLocation())
            .configurator()
            .build()

    private fun createBaseSignFromDB(
        signDataSourceModel: SignDataSourceModel,
        configurator: TrafficSign.Builder.() -> TrafficSign.Builder,
    ): TrafficSign =
        TrafficSign
            .Builder()
            .roadId(signDataSourceModel.idRoad)
            .directionId(signDataSourceModel.direction)
            .geoLocationPosition(signDataSourceModel.toGeoLocation())
            .configurator()
            .build()

    fun mapToModelFromDb(signDataSourceModel: SignDataSourceModel): TrafficSign? =
        when (signDataSourceModel.type) {
            SignTypes.NO_ENTRY ->
                createBaseSignFromDB(signDataSourceModel) {
                    trafficSignType(NoEntrySign)
                }
            SignTypes.MAX_SPEED ->
                createBaseSignFromDB(signDataSourceModel) {
                    trafficSignType(
                        MaximumSpeedLimitSign(
                            signDataSourceModel.speedLimit!!,
                            SpeedUnit.valueOf(signDataSourceModel.unit!!),
                        ),
                    )
                }
            SignTypes.MIN_SPEED ->
                createBaseSignFromDB(signDataSourceModel) {
                    trafficSignType(
                        MinimumSpeedLimitSign(
                            signDataSourceModel.speedLimit!!,
                            SpeedUnit.valueOf(signDataSourceModel.unit!!),
                            convertToLaneReference(signDataSourceModel.lanes),
                        ),
                    )
                }
            SignTypes.STOP ->
                createBaseSignFromDB(signDataSourceModel) {
                    trafficSignType(StopSign(convertToLaneReference(signDataSourceModel.lanes)))
                }
            SignTypes.YIELD ->
                createBaseSignFromDB(signDataSourceModel) {
                    trafficSignType(YieldSign(convertToLaneReference(signDataSourceModel.lanes)))
                }
            else -> null
        }

    fun mapToResponse(sign: TrafficSign): SignModel =
        SignModel(
            type = sign.trafficSignType::class.simpleName ?: "",
            category = sign.trafficSignType.category.name,
            idRoad = sign.roadId,
            direction = sign.directionId,
            latitude = sign.geoLocationPosition.latitude,
            longitude = sign.geoLocationPosition.longitude,
            lanes = convertFromLaneReference(sign.laneReference)!!,
            speedLimit =
                when (val speedLimit = sign.trafficSignType) {
                    is MaximumSpeedLimitSign -> speedLimit.maximumSpeedLimit
                    is MinimumSpeedLimitSign -> speedLimit.minimumSpeedLimit
                    else -> null
                },
            unit =
                when (val speedUnit = sign.trafficSignType) {
                    is MaximumSpeedLimitSign -> speedUnit.speedUnit.name
                    is MinimumSpeedLimitSign -> speedUnit.speedUnit.name
                    else -> null
                },
        )

    private fun convertToLaneReference(lanes: String): LaneReference =
        when {
            lanes == "all" -> AllLaneReference
            lanes.startsWith("-") -> {
                val end = lanes.substring(1).trim().toInt()
                ToLaneReference(end)
            }
            lanes.endsWith("-") -> {
                val start = lanes.substring(0, lanes.length - 1).trim().toInt()
                FromLaneReference(start)
            }
            lanes.contains("-") -> {
                val (start, end) = lanes.split("-").map { it.trim().toInt() }
                RangeLaneReference(start, end)
            }
            else -> SingleLaneReference(lanes.toInt())
        }

    private fun convertFromLaneReference(lanes: LaneReference): String? =
        when (lanes) {
            is AllLaneReference -> "all"
            is SingleLaneReference -> lanes.laneNumber.toString()
            is FromLaneReference -> "${lanes.fromLaneNumber} -"
            is ToLaneReference -> "- ${lanes.toLaneNumber}"
            is RangeLaneReference -> "${lanes.startLaneNumber} - ${lanes.endLaneNumber}"
            else -> null
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
