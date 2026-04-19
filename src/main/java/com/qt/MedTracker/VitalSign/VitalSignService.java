package com.qt.MedTracker.VitalSign;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class VitalSignService {
    private final VitalSignRepository vitalsignRepository;

    @Autowired
    public VitalSignService(VitalSignRepository vitalsignrepository) {
        this.vitalsignRepository = vitalsignrepository;
    }

    public List<VitalSign> getAllVitalSigns() {
        return vitalsignRepository.findAll();
    }

    public List<VitalSign> getVitalSignsByUserId(Long userId) {
        return vitalsignRepository.findByUserId(userId);
    }

    public Optional<VitalSign> getVitalSignById(Integer id) {
        return vitalsignRepository.findById(id);
    }

    public VitalSign getVitalSignForUserOrThrow(Integer id, Long userId) {
        return vitalsignRepository.findByIdAndUserId(id, userId)
                .orElseThrow(() -> new IllegalStateException("vital sign with id " + id + " does not exist for this user"));
    }

    public VitalSign getAccessibleVitalSign(Integer id, Long currentUserId, boolean isAdmin) {
        return isAdmin
                ? vitalsignRepository.findById(id).orElseThrow(() -> new IllegalStateException("vital sign with id " + id + " does not exist"))
                : getVitalSignForUserOrThrow(id, currentUserId);
    }

    public VitalSign saveVitalSign(VitalSign vitalsign) {
        return vitalsignRepository.save(vitalsign);
    }

    public VitalSign createVitalSign(Long userId, VitalSignRequest request) {
        VitalSign vitalSign = new VitalSign(
                userId,
                request.date(),
                request.bodyTemperature(),
                request.heartRate(),
                request.bloodPressureSystolic(),
                request.bloodPressureDiastolic(),
                request.bloodSugar()
        );
        return vitalsignRepository.save(vitalSign);
    }

    public void deleteVitalSign(Integer id) {
        vitalsignRepository.deleteById(id);
    }

    public void deleteVitalSignForUser(Integer id, Long currentUserId, boolean isAdmin) {
        VitalSign vitalSign = getAccessibleVitalSign(id, currentUserId, isAdmin);
        vitalsignRepository.deleteById(vitalSign.getId());
    }
}
