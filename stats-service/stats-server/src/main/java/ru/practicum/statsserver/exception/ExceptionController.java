package ru.practicum.statsserver.exception;


import org.hibernate.HibernateException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path = "/exception")
public class ExceptionController {

    @GetMapping("/bad_request")
    Exception getMyValidationException() {
        return new MyValidationException("bad_request");
    }

    @GetMapping("/conflict/{e}")
    Exception getDataIntegrityViolationException(@PathVariable Integer e) {
        if (e == 1) {
            return new DataIntegrityViolationException("conflict");
        } else {
            return new HibernateException("conflict");
        }
    }
}
