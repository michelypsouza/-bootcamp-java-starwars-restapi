package com.bootcampjava.starwars.repository;

import com.bootcampjava.starwars.model.Jedi;
import com.github.database.rider.core.api.connection.ConnectionHolder;
import com.github.database.rider.core.api.dataset.DataSet;
import com.github.database.rider.junit5.DBUnitExtension;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import javax.sql.DataSource;
import java.util.List;
import java.util.Optional;

@ExtendWith({DBUnitExtension.class, SpringExtension.class})
@SpringBootTest
@ActiveProfiles("test")
public class JediRepositoryTest {

    @Autowired
    private DataSource dataSource;

    @Autowired
    private JediRepository jediRepository;

    public ConnectionHolder getConnectionHolder() {
        return () -> dataSource.getConnection();
    }

    @Test
    @DataSet("jedi.yml")
    @DisplayName("Should return All Jedis with success")
    public void testFindAll() {

        // execucao
        List<Jedi> listJedis = jediRepository.findAll();

        // assert
        Assertions.assertFalse(listJedis.isEmpty(), "There are jedis of the list");
        Assertions.assertEquals(4, listJedis.size(), "We should have 4 jedis in our database");

    }

    @Test
    @DataSet("jedi.yml")
    @DisplayName("Should return a Jedi by id with success")
    public void testFindByIdWithSuccess() {

        // cenario
        Integer id  = 1;
        Jedi expectedJedi = new Jedi(id, "HanSolo", 50, 1);

        // execucao
        Optional<Jedi> jediDB = jediRepository.findById(id);

        // assert
        Assertions.assertTrue(jediDB.isPresent(), "Jedi was not found");
        Assertions.assertEquals(jediDB.get().getVersion(), expectedJedi.getVersion(), "Jedis version must be the same");
        Assertions.assertEquals(jediDB.get().getName(), expectedJedi.getName(), "Jedis name must be the same");
        Assertions.assertEquals(jediDB.get().getStrength(), expectedJedi.getStrength(), "Jedis strength must be the same");
        Assertions.assertEquals(jediDB.get().getId(), expectedJedi.getId(), "Jedis id must be the same");

    }

    @Test
    @DataSet("jedi.yml")
    @DisplayName("Should return empty when Jedi not found")
    public void testFindByIdNotFound() {

        // execucao
        Optional<Jedi> jediNotFound = jediRepository.findById(0);

        // assert
        Assertions.assertFalse(jediNotFound.isPresent(), "Jedi not was found");

    }

    @Test
    @DataSet("jedi.yml")
    @DisplayName("Should return a Jedi saved with success")
    public void testSaveWithSuccess() {

        // cenario
        Jedi jedi = new Jedi(50, "Jedi Name", 40, 1);

        // execucao
        Jedi jediSaved = jediRepository.save(jedi);

        // assert
        Assertions.assertNotNull(jediSaved, "Jedis should not be null");
        Assertions.assertEquals(jediSaved.getVersion(), jediSaved.getVersion(), "Jedis version must be the same");
        Assertions.assertEquals(jediSaved.getName(), jediSaved.getName(), "Jedis name must be the same");
        Assertions.assertEquals(jediSaved.getStrength(), jediSaved.getStrength(), "Jedis strength must be the same");
        Assertions.assertEquals(jediSaved.getId(), jediSaved.getId(), "Jedis id must be the same");
    }

    @Test
    @DataSet("jedi.yml")
    @DisplayName("Should return a Jedi updated with success")
    public void testUpdateWithSuccess() {

        // cenario
        Integer id = 2;
        Jedi j = null;
        Optional<Jedi> jedi = jediRepository.findById(2);
        if (jedi.isPresent()) {
            j = new Jedi(jedi.get().getId(),
                         jedi.get().getName(),
                         jedi.get().getStrength(),
                         jedi.get().getVersion());
            j.setName("Jedi Name");
        }

        // execucao
        boolean jediUpdated = jediRepository.update(j);

        // assert
        Assertions.assertTrue(jediUpdated, "Successfully updated jedi");
        Assertions.assertNotEquals(jedi.get().getName(), j.getName(), "Jedis name is different");

    }

    @Test
    @DataSet("jedi.yml")
    @DisplayName("Should not update a Jedi")
    public void testUpdateFail() {

        // cenario
        Jedi jedi = new Jedi(20, "Jedi Name", 231, 1);

        // execucao
        boolean updateFail = jediRepository.update(jedi);

        // assert
        Assertions.assertFalse(updateFail, "Jedi not updated");

    }

    @Test
    @DataSet("jedi.yml")
    @DisplayName("Should delete a Jedi with success into repository")
    public void testDeleteSuccess() {

        // execucao
        Integer id = 1;
        boolean jediDeleted = jediRepository.delete(id);

        // assert
        Assertions.assertTrue(jediDeleted, "Jedi deleted");
        Optional<Jedi> jedi = jediRepository.findById(id);
        Assertions.assertFalse(jedi.isPresent(), "Jedi not exists in the database");
    }

    @Test
    @DataSet("jedi.yml")
    @DisplayName("Shouldn't delete a Jedi when it has an error")
    public void testDeleteFail() {

        // execucao
        Integer id = 99;
        boolean jediNotDeleted = jediRepository.delete(id);

        // assert
        Assertions.assertFalse(jediNotDeleted, "Jedi not deleted");
        Optional<Jedi> jedi = jediRepository.findById(id);
        Assertions.assertFalse(jedi.isPresent(), "Jedi not exists in the database");
    }

}
