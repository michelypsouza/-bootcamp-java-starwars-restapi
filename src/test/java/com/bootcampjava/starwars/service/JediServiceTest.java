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
        Assertions.assertEquals(listAllJedi.size(), 1,"Jedi list size different from one");
        Assertions.assertSame(listAllJedi.get(0), mockJedi, "Jedis must be the same");
    }

    @Test
    @DisplayName("Should return empty list of result when Jedis are not found")
    public void testFindAllNotFound() {

        // cenario
        Mockito.doReturn(List.of()).when(jediRepository).findAll();

        // execucao
        List<Jedi> listAllJedi = jediService.findAll();

        // assert
        Assertions.assertTrue(listAllJedi.isEmpty(),"list of Jedi is not empty");
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
        Assertions.assertTrue(jediSaved != null && jediSaved.getId() != null, "Jedi was not saved");
        Assertions.assertNotNull(jediSaved, "Jedi should not be null");

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
        Assertions.assertNull(jediNull, "Jedi should be null");
    }

    @Test
    @DisplayName("Should return Jedi updated with success")
    public void testUpdateJediBySuccess() {

        // cenario
        Jedi mockJedi = new Jedi(1,"Jedi Name",10,1);
        Mockito.doReturn(mockJedi).when(jediRepository).save(Mockito.any());
        Mockito.doReturn(true).when(jediRepository).update(mockJedi);

        // execucao
        boolean updated = jediService.update(mockJedi);

        // assert
        Assertions.assertTrue(updated, "Jedi was not updated");
    }

    @Test
    @DisplayName("Should not update a Jedi ")
    public void testUpdateJediNotExecuted() {

        // cenario
        Jedi mockJedi = new Jedi(1,"Jedi Name",10,1);
        Mockito.doReturn(null).when(jediRepository).save(Mockito.any());
        Mockito.doReturn(false).when(jediRepository).update(mockJedi);

        // execucao
        boolean updated = jediService.update(mockJedi);

        // assert
        Assertions.assertFalse(updated, "Updated of jedi executed");
    }

    @Test
    @DisplayName("Should delete a Jedi with success")
    public void testDeleteJediBySuccess() {

        // cenario
        Mockito.doReturn(true).when(jediRepository).delete(1);

        // execucao
        boolean jediDeleted = jediService.delete(1);

        // assert
        Assertions.assertTrue(jediDeleted, "Jedi not deleted");
    }

    @Test
    @DisplayName("Shouldn't delete a Jedi when it has an error")
    public void testDeleteJediError() {

        // cenario
        Mockito.doReturn(false).when(jediRepository).delete(1);

        // execucao
        boolean jediNotDeleted = jediService.delete(1);

        // assert
        Assertions.assertFalse(jediNotDeleted, "Jedi is not deleted");
    }

}
