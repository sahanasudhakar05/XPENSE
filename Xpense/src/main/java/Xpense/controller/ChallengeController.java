package Xpense.controller;

import Xpense.models.Challenge;
import Xpense.models.UserChallenge;
import Xpense.repository.ChallengeRepository;
import Xpense.repository.UserChallengeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/api/challenges")
@CrossOrigin(origins = "*")
public class ChallengeController {

    @Autowired
    private ChallengeRepository challengeRepository;

    @Autowired
    private UserChallengeRepository userChallengeRepository;

    @GetMapping
    public ResponseEntity<List<Challenge>> getAllChallenges() {
        return ResponseEntity.ok(challengeRepository.findAll());
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<UserChallenge>> getUserChallenges(@PathVariable String userId) {
        return ResponseEntity.ok(userChallengeRepository.findByUserId(UUID.fromString(userId)));
    }

    @PostMapping("/join")
    public ResponseEntity<?> joinChallenge(@RequestBody Map<String, String> body) {
        UUID userId = UUID.fromString(body.get("userId"));
        UUID challengeId = UUID.fromString(body.get("challengeId"));

        if (userChallengeRepository.findByUserIdAndChallengeId(userId, challengeId).isPresent()) {
            return ResponseEntity.badRequest().body("Already joined");
        }

        UserChallenge uc = new UserChallenge();
        uc.setUserId(userId);
        uc.setChallengeId(challengeId);
        uc.setProgress(0);
        uc.setCompleted(false);
        uc.setJoinedAt(LocalDateTime.now());
        return ResponseEntity.ok(userChallengeRepository.save(uc));
    }
}