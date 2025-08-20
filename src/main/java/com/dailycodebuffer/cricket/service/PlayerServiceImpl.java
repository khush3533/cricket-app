package com.dailycodebuffer.cricket.service;

import com.dailycodebuffer.cricket.dto.PlayerDTO;
import com.dailycodebuffer.cricket.entity.PlayerEntity;
import com.dailycodebuffer.cricket.exception.CricketAppServiceRuntimeException;
import com.dailycodebuffer.cricket.model.PlayerRequest;
import com.dailycodebuffer.cricket.model.PlayerResponse;
import com.dailycodebuffer.cricket.repository.PlayerRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import lombok.extern.log4j.Log4j2;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Log4j2
public class PlayerServiceImpl implements PlayerService{

    private static final Logger log = LoggerFactory.getLogger(PlayerServiceImpl.class);
    @Autowired
    private PlayerRepository playerRepository;

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public PlayerResponse addPlayer(PlayerRequest playerRequest) {
        log.info("Started adding Player...");
        log.debug("Debug Entering addPlayer() with request: {}", playerRequest);
        PlayerEntity playerEntity = new PlayerEntity();
        BeanUtils.copyProperties(playerRequest, playerEntity);
        PlayerEntity savedEntity = playerRepository.save(playerEntity);
        PlayerResponse response = new PlayerResponse();
        BeanUtils.copyProperties(savedEntity, response);
        log.info("Finished adding Player...");
        log.debug("Exiting addPlayer() with response: {}", response);
        return response;

    }

    @Override
    public PlayerResponse getPlayerById(Long playerId) {
        log.info("Get the Player for playerId: {}", playerId);
        log.debug("Debug Get the Player for playerId: {}", playerId);

        PlayerEntity player
                = playerRepository.findById(playerId)
                .orElseThrow(
                        () -> new CricketAppServiceRuntimeException("Player with given id not found","NO_PLAYER_FOUND"));

        PlayerResponse playerResponse
                = new PlayerResponse();

        BeanUtils.copyProperties(player, playerResponse);
         log.debug("Debug Done with player By id "+playerResponse);
        return playerResponse;
    }


        @Override
        public Page<PlayerResponse> getPlayers(Pageable pageable) {
            log.info("Info Get all Players");
           
            Page<PlayerEntity> playerEntities = playerRepository.findAll(pageable);

            return playerEntities.map(entity -> {
                PlayerResponse response = new PlayerResponse();
                response.setPlayerId(entity.getPlayerId());
                response.setName(entity.getName());
                response.setRole(entity.getRole());
                return response;
            });

        }


    @Override
    public ResponseEntity<String> deletePlayerById(Long playerId) {
        log.info("Delete player "+playerId);
        boolean exists = playerRepository.existsById(playerId);
        System.out.println("Is there or not "+exists);
        if (!exists) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("Player not found with id: " + playerId);
        }
       return ResponseEntity.ok("Player deleted successfully!");
    }



    @Override
    public PlayerResponse updatePlayer(Long playerId, PlayerRequest playerRequest) {
        log.debug("Entering updatePlayer() with id: {} and request: {}", playerId, playerRequest);
        PlayerEntity existingPlayer = playerRepository.findById(playerId)
                .orElseThrow(() -> new RuntimeException("Player not found with id: " + playerId));
        log.debug(" Debug Existing player fetched: {}", existingPlayer);
        BeanUtils.copyProperties(playerRequest, existingPlayer);
        log.debug("Debug After copying properties: {}", existingPlayer);


        PlayerEntity updatedEntity = playerRepository.save(existingPlayer);
        log.info("Player updated successfully with id: {}", updatedEntity.getPlayerId());


        PlayerResponse response = new PlayerResponse();
        BeanUtils.copyProperties(updatedEntity, response);
        log.debug("Returning response: {}", response);

        return response;
    }


    @Override
    public PlayerResponse partialUpdatePlayer(Long playerId, PlayerRequest playerRequest) {
        log.debug("Entering partialUpdatePlayer() with id: {} and request: {}", playerId, playerRequest);


        PlayerEntity existingPlayer = playerRepository.findById(playerId)
                .orElseThrow(() -> new RuntimeException("Player not found with id: " + playerId));
        log.debug("Existing player fetched: {}", existingPlayer);


        if (playerRequest.getName() != null) {
            existingPlayer.setName(playerRequest.getName());
            log.debug("Updated name to: {}", playerRequest.getName());
        }
        if (playerRequest.getRole() != null) {
            existingPlayer.setRole(playerRequest.getRole());
            log.debug("Updated role to: {}", playerRequest.getRole());
        }


        PlayerEntity updatedEntity = playerRepository.save(existingPlayer);
        log.info("Player partially updated successfully with id: {}", updatedEntity.getPlayerId());


        PlayerResponse response = new PlayerResponse();
        BeanUtils.copyProperties(updatedEntity, response);
        log.debug("Returning response: {}", response);

        return response;
    }

    @Override
    public List<PlayerDTO> searchPlayersByKeywords(List<String> keywordList) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<PlayerEntity> cq = cb.createQuery(PlayerEntity.class);
        Root<PlayerEntity> player = cq.from(PlayerEntity.class);

        List<Predicate> predicates = new ArrayList<>();
        for (String keyword : keywordList) {
            Predicate nameLike = cb.like(player.get("name"), "%" + keyword + "%");
            Predicate roleLike = cb.like(player.get("role"), "%" + keyword + "%");
            predicates.add(cb.or(nameLike, roleLike));
        }

        cq.where(cb.or(predicates.toArray(new Predicate[0])));
        List<PlayerEntity> result = entityManager.createQuery(cq).getResultList();

        // Map to projection manually
        List<PlayerDTO> projectedResult = new ArrayList<>();
        for (PlayerEntity p : result) {
            projectedResult.add(new PlayerDTO() {
                @Override
                public Long getPlayerId() { return p.getPlayerId(); }
                @Override
                public String getName() { return p.getName(); }
                @Override
                public String getRole() { return p.getRole(); }
            });
        }

        return projectedResult;
    }

    @Override
    public List<PlayerDTO> searchPlayerByRole(String role) {
        return playerRepository.findAllByRole(role);
    }


}
