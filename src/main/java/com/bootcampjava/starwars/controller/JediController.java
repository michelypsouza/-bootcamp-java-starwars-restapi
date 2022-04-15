package com.bootcampjava.starwars.controller;

import com.bootcampjava.starwars.model.Jedi;
import com.bootcampjava.starwars.service.JediService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.Optional;

@RestController
public class JediController {

    private static final Logger logger = LogManager.getLogger(JediController.class);

    private final JediService jediService;

    public JediController(JediService jediService) {
        this.jediService = jediService;
    }

    @GetMapping("/jedi/{id}")
    public ResponseEntity<?> getId(@PathVariable Integer id) {

        return  jediService.findById(id)
                .map(jedi -> {
                    try {
                        return ResponseEntity
                                .ok()
                                .eTag(Integer.toString(jedi.getVersion()))
                                .location(new URI("/jedi/" + jedi.getId()))
                                .body(jedi);
                    } catch (URISyntaxException e) {
                        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
                    }
                })
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping("/jedi")
    public ResponseEntity<Jedi> saveJedi(@RequestBody Jedi jedi) {

        Jedi newJedi = jediService.save(jedi);

        try {
            return ResponseEntity
                    .created(new URI("/jedi/" + newJedi.getId()))
                    .eTag(Integer.toString(newJedi.getVersion()))
                    .body(newJedi);
        } catch (URISyntaxException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }

    }

    @PutMapping("/jedi/{id}")
    public ResponseEntity<?> updateJedi(@RequestBody Jedi jedi,
                                        @PathVariable Integer id,
                                        @RequestHeader("If-Match") Integer ifMatch) {

        Optional<Jedi> existingJedi = jediService.findById(id);

        return existingJedi.map(j -> {
            if (!(j.getVersion().equals(ifMatch))) {
                return ResponseEntity.status(HttpStatus.CONFLICT).build();
            }

            // update jedi
            j.setName(jedi.getName());
            j.setStrength(j.getStrength());
            j.setVersion(j.getVersion() + 1);

            try {
                if (jediService.update(j)) {
                    return ResponseEntity.ok()
                            .location(new URI("/jedi/" + j.getId()))
                            .eTag(Integer.toString(j.getVersion()))
                            .body(j);
                } else {
                    return ResponseEntity.notFound().build();
                }
            } catch (URISyntaxException e) {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
            }
        }).orElse(ResponseEntity.notFound().build());

    }

    @DeleteMapping("/jedi/{id}")
    public ResponseEntity<?> deleteJedi(@PathVariable Integer id) {

        logger.info("Deleting Jedi with ID {}", id);

        Optional<Jedi> existingJedi = jediService.findById(id);

        return existingJedi.map(j -> {
            if (jediService.delete(j.getId())) {
                return ResponseEntity.ok().build();
            } else {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
            }
        }).orElse(ResponseEntity.notFound().build());

    }

}
