package com.test.featurestest.domain.repository

import com.test.featurestest.domain.model.Office

interface OfficeRepository {
    fun getOffices(): List<Office>

}