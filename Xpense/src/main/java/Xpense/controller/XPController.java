package Xpense.controller;

import Xpense.models.XPEvent;
import Xpense.service.XPService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/xp")
@CrossOrigin(origins = "*")
public class XPController {

    @Autowired
    private XPService xpService;

    @GetMapping
    public ResponseEntity<List<XPEvent>> getXPHistory(@RequestParam String userId) {
        return ResponseEntity.ok(xpService.getUserXPHistory(userId));
    }
}