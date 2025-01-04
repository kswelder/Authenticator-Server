package com.welderhayne.Oauth.Repositories;

import com.welderhayne.Oauth.Enums.Permitions;
import com.welderhayne.Oauth.Models.Register;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface RegisterRepository extends JpaRepository<Register, Integer> {
    Optional<Register> findRegisterByIdKey(String idKey);
    Optional<Register> findRegisterByUsername(String username);
    Optional<Register> findRegisterByEmail(String email);
    Optional<List<Register>> findAllRegisterByPermition(Permitions perm);
    boolean existsByUsername(String username);
    boolean existsByEmail(String email);
}
