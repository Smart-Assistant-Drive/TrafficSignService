package com.example.rest.interfaceAdaptersLayer.controllers.dto.createSign

import com.fasterxml.jackson.annotation.JsonCreator
import com.fasterxml.jackson.annotation.JsonProperty
import org.springframework.hateoas.Link
import org.springframework.hateoas.RepresentationModel

data class SignsResponseDto
    @JsonCreator
    constructor(
        @param:JsonProperty("signs")
        val signs: List<SignResponseDto>,
    ) : RepresentationModel<SignsResponseDto>()

fun List<SignResponseDto>.toDto(links: List<Link>): SignsResponseDto =
    SignsResponseDto(
        signs = this,
    ).add(links)
