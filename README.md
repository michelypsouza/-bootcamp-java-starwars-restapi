# bootcamp-java-starwars-restapi

  API Rest de cadastro de JEDIs construída durante as aulas do bootcamp Java da <b>womakerscode</b>. 
  Recursos e dependências utilizadas neste projeto:
  - Java 11
  - Maven
  - Spring Boot
  - Spring Web
  - Spring JDBC
  - Database H2
  - Junit 5 
  - DBunit

## Checklist da lição de casa - testes

### JediServiceTest
- [X] Criar teste de erro NOT FOUND
- [X] Criar um teste pro findAll();

### JediControllerTest
- [X] Teste do POST com sucesso
- [X] Teste do PUT com sucesso
- [X] Teste do PUT com uma versao igual da ja existente - deve retornar um conflito
- [X] Teste do PUT com erro - not found
- [ ] Teste do delete com sucesso
- [ ] Teste do delete com erro - deletar um id ja deletado
- [ ] Teste do delete com erro  - internal server error
