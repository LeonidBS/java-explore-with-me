package ru.practicum.ewmservice.compilation.adm.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.ewmservice.compilation.model.Compilation;

public interface CompilationAdmRepository extends JpaRepository<Compilation, Integer> {
}
