package com.smu.odowan.repository;

import java.util.Optional;

public interface UserRepository {
    Optional<Object> findByEmail(String email);
}
