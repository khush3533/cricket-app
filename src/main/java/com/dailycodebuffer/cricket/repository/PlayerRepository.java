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

    List<PlayerDTO> findAllByRole(String role);




}
