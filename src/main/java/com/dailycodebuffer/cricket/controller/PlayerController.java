package com.dailycodebuffer.cricket.controller;

import com.dailycodebuffer.cricket.dto.PlayerDTO;
import com.dailycodebuffer.cricket.model.PlayerRequest;
import com.dailycodebuffer.cricket.model.PlayerResponse;
import com.dailycodebuffer.cricket.service.PlayerService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/player")
public class PlayerController {

    @Autowired
    private PlayerService playerService;
    @PostMapping
    public ResponseEntity<PlayerResponse> addPlayer(@Valid @RequestBody PlayerRequest playerRequest) {
        PlayerResponse playerResponse = playerService.addPlayer(playerRequest);
        return new ResponseEntity<>(playerResponse, HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<PlayerResponse> getPlayerById(@PathVariable("id") Long playerId) {
        PlayerResponse playerResponse
                = playerService.getPlayerById(playerId);
        return new ResponseEntity<>(playerResponse, HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<Page<PlayerResponse>> getPlayers(@RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "10") int size, @RequestParam(defaultValue = "playerId") String sortBy, @RequestParam(defaultValue = "asc") String sortDir) {

        Sort sort = sortDir.equalsIgnoreCase("asc")
                ? Sort.by(sortBy).ascending()
                : Sort.by(sortBy).descending();
        Pageable pageable = PageRequest.of(page, size, sort);

        Page<PlayerResponse> players = playerService.getPlayers(pageable);
        return new ResponseEntity<>(players, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deletePlayerById(@PathVariable("id") Long playerId) {
        return playerService.deletePlayerById(playerId); // Already returns ResponseEntity
    }

    @GetMapping("/search")
    public ResponseEntity<List<PlayerResponse>> searchPlayer(@RequestParam("role") String role) {
        List<PlayerDTO> projections = playerService.searchPlayerByRole(role);
        List<PlayerResponse> playerResponses = projections.stream()
                .map(p -> {
                    PlayerResponse response = new PlayerResponse();
                    response.setName(p.getName());

                    return response;
                })
                .toList();

        return new ResponseEntity<>(playerResponses, HttpStatus.OK);
    }

    @PutMapping("/{id}")
    public ResponseEntity<PlayerResponse> updatePlayer(
            @PathVariable("id") Long playerId,
            @Valid @RequestBody PlayerRequest playerRequest) {
        PlayerResponse updatedPlayer = playerService.updatePlayer(playerId, playerRequest);
        return new ResponseEntity<>(updatedPlayer, HttpStatus.OK);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<PlayerResponse> partiallyUpdatePlayer(@PathVariable("id") Long playerId, @RequestBody PlayerRequest playerRequest) { // fields can be optional
        PlayerResponse updatedPlayer = playerService.partialUpdatePlayer(playerId, playerRequest);
        return new ResponseEntity<>(updatedPlayer, HttpStatus.OK);
    }

    @GetMapping("/player/search")
    public List<PlayerDTO> searchPlayer(@RequestParam String keywords) {
        return playerService.searchPlayers(keywords);
    }
}
