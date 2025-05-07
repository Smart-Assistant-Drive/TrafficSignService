package com.example.rest.interfaceAdaptersLayer.controllers

import com.example.rest.businessLayer.boundaries.SignsInputBoundary
import com.example.rest.businessLayer.exception.InvalidTokenException
import com.example.rest.businessLayer.exception.PasswordToShortException
import com.example.rest.businessLayer.exception.TokenExpiredException
import com.example.rest.businessLayer.exception.UserAlreadyPresentException
import com.example.rest.businessLayer.exception.UserNotFound
import com.example.rest.interfaceAdaptersLayer.controllers.dto.createSign.SignDto
import com.example.rest.interfaceAdaptersLayer.controllers.dto.createSign.toDto
import com.example.rest.interfaceAdaptersLayer.controllers.dto.createSign.toModel
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.Parameter
import io.swagger.v3.oas.annotations.media.ArraySchema
import io.swagger.v3.oas.annotations.media.Content
import io.swagger.v3.oas.annotations.media.Schema
import io.swagger.v3.oas.annotations.responses.ApiResponse
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo
import org.springframework.http.HttpEntity
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController
import io.swagger.v3.oas.annotations.parameters.RequestBody as SwaggerRequestBody

@RestController
class SignsController(
    val userInput: SignsInputBoundary,
) {
    @Operation(
        summary = "Create a new sign",
        description = "Create a new sign with the given parameters. The sign will be created in the database.",
        requestBody =
            SwaggerRequestBody(
                description = "Sign creation request",
                required = true,
                content =
                    arrayOf(
                        Content(
                            mediaType = "application/json",
                            schema =
                                Schema(
                                    implementation = SignDto::class,
                                ),
                        ),
                    ),
            ),
        responses = [
            ApiResponse(
                responseCode = "201",
                description = "Sign created successfully",
                content = [Content(mediaType = "application/json", schema = Schema(implementation = SignDto::class))],
            ),
            ApiResponse(
                responseCode = "400",
                description = "Bad request",
                content = [Content(mediaType = "application/json")],
            ),
            ApiResponse(
                responseCode = "409",
                description = "Conflict",
                content = [Content(mediaType = "application/json")],
            ),
        ],
    )
    @PostMapping("/create_sign")
    fun create(
        @RequestBody requestModel: SignDto,
    ): HttpEntity<Any> {
        val result = userInput.createSign(requestModel.toModel())
        return if (result.isSuccess) {
            val links =
                listOf(
                    linkTo(WebMvcLinkBuilder.methodOn(SignsController::class.java).create(requestModel))
                        .withSelfRel(),
                )
            ResponseEntity(result.getOrNull()!!.toDto(links), HttpStatus.CREATED)
        } else {
            when (val exception = result.exceptionOrNull()) {
                is UserAlreadyPresentException ->
                    ResponseEntity
                        .status(HttpStatus.CONFLICT)
                        .body(exception.message)

                is PasswordToShortException -> ResponseEntity.badRequest().body(exception.message)
                else -> ResponseEntity.internalServerError().build()
            }
        }
    }

    @Operation(
        summary = "Get signs",
        description = "Get signs from the database with the given parameters. The signs will be returned in a list.",
        parameters = [
            Parameter(
                name = "idRoad",
                description = "The id of the road",
                required = true,
            ),
            Parameter(
                name = "direction",
                description = "The direction of the road",
                required = true,
            ),
            Parameter(
                name = "latitude",
                description = "The latitude of the position",
                required = true,
            ),
            Parameter(
                name = "longitude",
                description = "The longitude of the position",
                required = true,
            ),
        ],
        responses = [
            ApiResponse(
                responseCode = "200",
                description = "Login successful",
                content =
                    [
                        Content(
                            mediaType = "application/json",
                            array =
                                ArraySchema(
                                    schema = Schema(implementation = SignDto::class),
                                ),
                        ),
                    ],
            ),
            ApiResponse(
                responseCode = "400",
                description = "Bad request",
                content = [Content(mediaType = "application/json")],
            ),
            ApiResponse(
                responseCode = "401",
                description = "Unauthorized",
                content = [Content(mediaType = "application/json")],
            ),
        ],
    )
    @GetMapping("/signs/{idRoad}/{direction}/{latitude}/{longitude}")
    fun getSigns(
        @PathVariable idRoad: Int,
        @PathVariable direction: Int,
        @PathVariable latitude: Double,
        @PathVariable longitude: Double,
    ): HttpEntity<Any> {
        val result = userInput.getSigns(idRoad, direction, latitude, longitude)
        return if (result.isSuccess) {
            val links =
                listOf(
                    linkTo(WebMvcLinkBuilder.methodOn(SignsController::class.java).getSigns(idRoad, direction, latitude, longitude))
                        .withSelfRel(),
                )
            ResponseEntity(result.getOrNull()!!.toDto(links), HttpStatus.OK)
        } else {
            when (val exception = result.exceptionOrNull()) {
                is UserNotFound -> ResponseEntity.status(HttpStatus.NOT_FOUND).body(exception.message)
                is TokenExpiredException -> ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(exception.message)
                is InvalidTokenException -> ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(exception.message)
                else -> ResponseEntity.internalServerError().build()
            }
        }
    }
}
