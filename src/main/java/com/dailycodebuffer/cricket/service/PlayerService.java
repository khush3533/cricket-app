package com.dailycodebuffer.cricket.service;

import com.dailycodebuffer.cricket.dto.PlayerDTO;
import com.dailycodebuffer.cricket.model.PlayerRequest;
import com.dailycodebuffer.cricket.model.PlayerResponse;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface PlayerService {
    PlayerResponse addPlayer(PlayerRequest playerRequest);
    PlayerResponse getPlayerById(Long playerId);
    Page<PlayerResponse> getPlayers(Pageable pageable);
    ResponseEntity<String> deletePlayerById(Long playerId);
    List<PlayerDTO> searchPlayerByRole(String role);
    PlayerResponse updatePlayer(Long playerId, @Valid PlayerRequest playerRequest);
    PlayerResponse partialUpdatePlayer(Long playerId, PlayerRequest playerRequest);
    List<PlayerDTO> searchPlayers(String keywords);
}
