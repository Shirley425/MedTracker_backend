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

    public VitalSign saveVitalSign(VitalSign vitalsign) {
        return vitalsignRepository.save(vitalsign);
    }

    public void deleteVitalSign(Integer id) {
        vitalsignRepository.deleteById(id);
    }
}
