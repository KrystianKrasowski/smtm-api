package com.smtm.infrastructure.persistence.plans

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import org.springframework.stereotype.Repository
import java.time.LocalDate

@Repository
internal interface PlansJpaRepository : JpaRepository<PlanEntity, String> {

    @Query("SELECT p FROM PlanEntity p WHERE p.start <= :date AND p.end >= :date AND p.ownerId = :ownerId ORDER BY p.end DESC")
    fun findByOwnerIdAndMatchingDate(@Param("date") date: LocalDate, @Param("ownerId") ownerId: String): List<PlanEntity>

    fun findAllByOwnerIdOrderByEndDesc(ownerId: String): List<PlanEntity>
}
