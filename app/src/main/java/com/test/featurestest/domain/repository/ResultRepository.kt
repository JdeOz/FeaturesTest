package com.test.featurestest.domain.repository

import com.test.featurestest.domain.model.Result

interface ResultRepository {
    fun getResults(): List<Result>

}