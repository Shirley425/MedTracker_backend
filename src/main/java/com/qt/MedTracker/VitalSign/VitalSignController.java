package com.qt.MedTracker.VitalSign;

import com.qt.MedTracker.security.SecurityUtils;
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
    public List<VitalSignResponse> getAllVitalSigns() {
        SecurityUtils.requireAdmin();
        return vitalSignService.getAllVitalSigns().stream().map(VitalSignResponse::from).toList();
    }

    @GetMapping("/user/{userId}")
    public List<VitalSignResponse> getVitalSignsByUserId(@PathVariable Long userId) {
        SecurityUtils.requireCurrentUserOrAdmin(userId);
        return vitalSignService.getVitalSignsByUserId(userId).stream().map(VitalSignResponse::from).toList();
    }

    @GetMapping("/me")
    public List<VitalSignResponse> getMyVitalSigns() {
        return vitalSignService.getVitalSignsByUserId(SecurityUtils.getCurrentUser().getUserId())
                .stream()
                .map(VitalSignResponse::from)
                .toList();
    }

    @GetMapping("/{id}")
    public ResponseEntity<VitalSignResponse> getVitalSignById(@PathVariable Integer id) {
        VitalSign vitalSign = vitalSignService.getAccessibleVitalSign(
                id,
                SecurityUtils.getCurrentUser().getUserId(),
                SecurityUtils.isAdmin()
        );
        return ResponseEntity.ok(VitalSignResponse.from(vitalSign));
    }

    @PostMapping
    public VitalSignResponse createVitalSign(@RequestBody VitalSignRequest request) {
        return VitalSignResponse.from(
                vitalSignService.createVitalSign(SecurityUtils.getCurrentUser().getUserId(), request)
        );
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteVitalSign(@PathVariable Integer id) {
        vitalSignService.deleteVitalSignForUser(
                id,
                SecurityUtils.getCurrentUser().getUserId(),
                SecurityUtils.isAdmin()
        );
        return ResponseEntity.noContent().build();
    }
}
