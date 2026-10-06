package Xpense.controller;

import Xpense.models.Friends;
import Xpense.models.User;
import Xpense.repository.FriendsRepository;
import Xpense.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.*;

@RestController
@RequestMapping("/api/friends")
@CrossOrigin(origins = "*")
public class FriendsController {

    @Autowired
    private FriendsRepository friendsRepository;

    @Autowired
    private UserRepository userRepository;

    @GetMapping("/{userId}")
    public ResponseEntity<List<User>> getFriends(@PathVariable String userId) {
        UUID uid = UUID.fromString(userId);
        List<Friends> friendships = friendsRepository.findByUserAOrUserB(uid, uid)
                .stream().filter(f -> f.getStatus().equals("accepted")).toList();
        List<User> friends = new ArrayList<>();
        for (Friends f : friendships) {
            UUID friendId = f.getUserA().equals(uid) ? f.getUserB() : f.getUserA();
            userRepository.findById(friendId).ifPresent(friends::add);
        }
        return ResponseEntity.ok(friends);
    }

    @GetMapping("/requests/{userId}")
    public ResponseEntity<List<Map<String, Object>>> getPendingRequests(@PathVariable String userId) {
        UUID uid = UUID.fromString(userId);
        List<Friends> pending = friendsRepository.findByUserAOrUserB(uid, uid)
                .stream()
                .filter(f -> f.getStatus().equals("pending") && f.getUserB().equals(uid))
                .toList();

        List<Map<String, Object>> result = new ArrayList<>();
        for (Friends f : pending) {
            userRepository.findById(f.getUserA()).ifPresent(u -> {
                Map<String, Object> map = new HashMap<>();
                map.put("friendshipId", f.getId());
                map.put("userId", u.getId());
                map.put("displayName", u.getDisplayName());
                result.add(map);
            });
        }
        return ResponseEntity.ok(result);
    }

    @PostMapping("/request")
    public ResponseEntity<?> sendRequest(@RequestBody Map<String, String> body) {
        String myId = body.get("userId");
        String friendName = body.get("friendName");

        Optional<User> friendOpt = userRepository.findByDisplayNameExact(friendName);
        if (friendOpt.isEmpty()) return ResponseEntity.badRequest().body("User not found");

        UUID userA = UUID.fromString(myId);
        UUID userB = friendOpt.get().getId();

        if (userA.equals(userB)) return ResponseEntity.badRequest().body("Cannot add yourself");

        if (friendsRepository.findByUserAAndUserB(userA, userB).isPresent() ||
                friendsRepository.findByUserAAndUserB(userB, userA).isPresent()) {
            return ResponseEntity.badRequest().body("Request already sent or already friends");
        }

        Friends f = new Friends();
        f.setUserA(userA);
        f.setUserB(userB);
        f.setStatus("pending");
        return ResponseEntity.ok(friendsRepository.save(f));
    }

    @PostMapping("/accept")
    public ResponseEntity<?> acceptRequest(@RequestBody Map<String, String> body) {
        UUID friendshipId = UUID.fromString(body.get("friendshipId"));
        return friendsRepository.findById(friendshipId)
                .map(f -> {
                    f.setStatus("accepted");
                    return ResponseEntity.ok(friendsRepository.save(f));
                })
                .orElse(ResponseEntity.notFound().build());
    }
}