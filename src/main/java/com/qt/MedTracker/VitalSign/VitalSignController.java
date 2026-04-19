package com.qt.MedTracker.VitalSign;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/vitalsigns")
public class VitalSignController {

    @Autowired
    private VitalSignService vitalSignService;

    @GetMapping
    public List<VitalSign> getAllVitalSigns() {
        return vitalSignService.getAllVitalSigns();
    }

    @GetMapping("/user/{userId}")
    public List<VitalSign> getVitalSignsByUserId(@PathVariable Long userId) {
        return vitalSignService.getVitalSignsByUserId(userId);
    }

    @GetMapping("/{id}")
    public ResponseEntity<VitalSign> getVitalSignById(@PathVariable Integer id) {
        Optional<VitalSign> vitalSign = vitalSignService.getVitalSignById(id);
        return vitalSign.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    public VitalSign createVitalSign(@RequestBody VitalSign vitalSign) {
        return vitalSignService.saveVitalSign(vitalSign);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteVitalSign(@PathVariable Integer id) {
        vitalSignService.deleteVitalSign(id);
        return ResponseEntity.noContent().build();
    }
}
