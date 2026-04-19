package com.qt.MedTracker.User;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class UserRepositoryDataJpaTest {

    @Autowired
    private UserRepository userRepository;

    @Test
    void findsUsersByEmailAndSlackMemberId() {
        User user = new User("Repository User", LocalDate.of(1990, 1, 10), "repo@example.com", "encoded", "PATIENT");
        user.setSlackMemberId("UREPO123");
        userRepository.save(user);

        assertThat(userRepository.findUserByEmail("repo@example.com")).isPresent();
        assertThat(userRepository.findBySlackMemberId("UREPO123")).isPresent();
    }
}
