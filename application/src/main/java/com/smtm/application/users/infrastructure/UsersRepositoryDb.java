package com.smtm.application.users.infrastructure;

import org.springframework.data.repository.CrudRepository;

public interface UsersRepositoryDb extends CrudRepository<User, Long> {

    User findByEmail(String email);
}
