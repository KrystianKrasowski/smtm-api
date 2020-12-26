package com.smtm.infrastructure.persistence.users;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

public interface DbUsersRepository extends CrudRepository<User, Long> {

    User findByEmail(String email);
}
