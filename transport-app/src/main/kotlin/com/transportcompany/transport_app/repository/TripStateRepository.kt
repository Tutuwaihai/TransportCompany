package com.transportcompany.transport_app.repository

import com.transportcompany.transport_app.model.TripState
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface TripStateRepository : JpaRepository<TripState, Long> 