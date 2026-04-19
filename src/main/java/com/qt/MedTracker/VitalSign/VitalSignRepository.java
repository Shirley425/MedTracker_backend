package com.qt.MedTracker.VitalSign;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface VitalSignRepository extends JpaRepository<VitalSign, Integer> {
    List<VitalSign> findByUserId(Long userId);
}
