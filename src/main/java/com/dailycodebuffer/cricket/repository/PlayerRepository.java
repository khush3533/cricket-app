package com.dailycodebuffer.cricket.repository;

import com.dailycodebuffer.cricket.dto.PlayerDTO;
import com.dailycodebuffer.cricket.entity.PlayerEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PlayerRepository extends JpaRepository<PlayerEntity, Long> {

    @Query("SELECT new com.dailycodebuffer.cricket.dto.PlayerDTO(p.name) FROM PlayerEntity p WHERE p.role = :role")
    List<PlayerDTO> findAllByRole(@Param("role") String role);




}
