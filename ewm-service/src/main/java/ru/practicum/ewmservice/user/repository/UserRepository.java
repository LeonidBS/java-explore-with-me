package ru.practicum.ewmservice.user.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.practicum.ewmservice.user.model.User;

import java.util.List;


@Repository
public interface UserRepository extends JpaRepository<User, Integer> {

    Page<User> findByIdIn(List<Integer> ids, Pageable page);

}

