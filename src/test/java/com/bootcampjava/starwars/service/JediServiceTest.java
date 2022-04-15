package com.bootcampjava.starwars.service;

import com.bootcampjava.starwars.model.Jedi;
import com.bootcampjava.starwars.repository.JediRepositoryImpl;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.List;
import java.util.Optional;

@ExtendWith(SpringExtension.class)
@SpringBootTest
public class JediServiceTest {

    @Autowired
    private JediService jediService;

    @MockBean
    private JediRepositoryImpl jediRepository;

    @Test
    @DisplayName("Should return Jedi with success")
    public void testFindBySuccess() {

        // cenario
        Jedi mockJedi = new Jedi(1,"Jedi Name",10,1);
        Mockito.doReturn(Optional.of(mockJedi)).when(jediRepository).findById(1);

        // execucao
        Optional<Jedi> returnedJedi = jediService.findById(1);

        // assert
        Assertions.assertTrue(returnedJedi.isPresent(),"Jedi was not found");
        Assertions.assertSame(returnedJedi.get(), mockJedi, "Jedis must be the same");

    }

    //TODO: Criar teste de erro NOT FOUND
    @Test
    @DisplayName("Should return empty when Jedi not found")
    public void testFindByIdNotFound() {

        Integer idJedi = 4;

        // cenario
        Mockito.doReturn(Optional.empty()).when(jediRepository).findById(idJedi);

        // execucao
        Optional<Jedi> returnedJedi = jediService.findById(idJedi);

        // assert
        Assertions.assertFalse(returnedJedi.isPresent(), "Jedi was not found");

    }

    //TODO: Criar um teste pro findAll();
    @Test
    @DisplayName("Should return all Jedi's elements with success")
    public void testFindAllWithSuccess() {

        // cenario
        Jedi mockJedi = new Jedi(1,"Jedi Name",10,1);
        Mockito.doReturn(List.of(mockJedi)).when(jediRepository).findAll();

        // execucao
        List<Jedi> listAllJedi = jediService.findAll();

        // assert
        Assertions.assertTrue(listAllJedi.size() == 1,"Jedi list size equal to one");
        Assertions.assertSame(listAllJedi.get(0), mockJedi, "Jedis must be the same");
    }

    @Test
    @DisplayName("Should return Jedi saved with success")
    public void testSaveJediBySuccess() {

        // cenario
        Jedi mockJedi = new Jedi(1,"Jedi Name",10,1);
        Mockito.doReturn(mockJedi).when(jediRepository).save(Mockito.any());

        // execucao
        Jedi jediSaved = jediService.save(mockJedi);

        // assert
        Assertions.assertSame(jediSaved, mockJedi, "Jedis must be the same");
        Assertions.assertNotNull(jediSaved.getId(), "Successfully recorded jedi");

    }

    @Test
    @DisplayName("Should return error when trying to save the Jedi")
    public void testSaveJediError() {

        // cenario
        Jedi mockJedi = new Jedi(1,"Jedi Name",10,1);
        Mockito.doReturn(null).when(jediRepository).save(mockJedi);

        // execucao
        Jedi jediNull = jediService.save(mockJedi);

        // assert
        Assertions.assertNull(jediNull, "error saving jedi");

    }

}
