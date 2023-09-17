package ru.practicum.ewmservice.category.adm.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.practicum.ewmservice.category.model.Category;

@Repository
public interface CategoryAdmRepository extends JpaRepository<Category, Integer> {

}
