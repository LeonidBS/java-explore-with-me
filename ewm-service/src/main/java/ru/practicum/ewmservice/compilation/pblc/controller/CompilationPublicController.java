package ru.practicum.ewmservice.compilation.pblc.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.ewmservice.compilation.dto.CompilationDto;
import ru.practicum.ewmservice.compilation.pblc.service.CompilationPublicService;

import javax.validation.constraints.PositiveOrZero;
import java.util.List;

@Slf4j
@RestController
@RequestMapping(path = "/compilations")
@RequiredArgsConstructor
@Validated
public class CompilationPublicController {
    private final CompilationPublicService compilationPublicService;

    @GetMapping
    public List<CompilationDto> getCompilation(@RequestParam(required = false) Boolean pinned,
                                       @RequestParam(defaultValue = "0") @PositiveOrZero Integer from,
                                       @RequestParam(defaultValue = "10") @PositiveOrZero Integer size) {

        return compilationPublicService.findCompilation(pinned, from, size);
    }

    @GetMapping("/{compId}")
    public CompilationDto getById(@PositiveOrZero @PathVariable Integer compId) {

        return compilationPublicService.findById(compId);
    }
}
