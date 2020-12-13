package com.smtm.application.security.users.infrastructure;

import org.springframework.data.repository.CrudRepository;

public interface DbUsersRepository extends CrudRepository<User, Long> {

    User findByEmail(String email);
}
