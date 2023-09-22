package ru.practicum.ewmservice.compilation.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.ewmservice.compilation.model.Compilation;

public interface CompilationRepository extends JpaRepository<Compilation, Integer> {

    Page<Compilation> findByPinned(Boolean pinned, PageRequest page);

}
