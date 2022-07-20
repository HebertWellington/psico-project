# Psicoproject
SPRING-RESTfull-Api


Projeto de gerenciamento e automação administrativa de uma clínica de psicologia.

:construction: Projeto em construção :construction:

# :hammer: Funcionalidades do projeto atual

- `Funcionalidade 1`: Login de Usuário retornando um token para autorizações futuras.
- `Funcionalidade 2`: Registro de Usuários com autorização de Role @Secured.
- `Funcionalidade 3`: Atualização de usuário reservado somente ao próprio via token, restrito ao usuário que contém o mesmo id @PreAuthorize.
- `Funcionalidade 4`: Busca de Usuários por id, restrito ao Administrador do sistema.
- `Funcionalidade 5`: Busca uma lista de Usuários personalizada por quantidade de registros por página.
- `Funcionalidade 6`: Busca uma lista de Clientes de um Usuário, restrito ao Usuário. Personalizada por quantidade de registros por página.
- `Funcionalidade 7`: Criação e atualização de registro de Clientes, restrito ao Usuário através do token.
- `Funcionalidade 8`: Busca de registro de Clientes por id, restrito ao Usuário através do token.
- `Funcionalidade 9`: Atualização de Roles, restrito ao Administrador.
- `Funcionalidade 10`: Lista todos os registros dos Clientes com seus respectivos Usuários, restrito ao Administrador. Personalizada por quantidade de registros por página.
- `Funcionalidade 11`: Testes de integração com o banco de dados e unitários nas camadas service e resource com Mockito.


## ✔️ Técnicas e tecnologias utilizadas

- ``Java 11``
- ``Spring Boot``
- ``API REST``
- ``Maven-4.0.0``
- ``Mysql``
- ``Lombok``
- ``JUnit 5``
- ``Spring-Security``
- ``JWT-Json Web Token``


## Autores

| [<img src="https://avatars.githubusercontent.com/u/72111388?v=4" width=115><br><sub>Hebert Wellington</sub>](https://github.com/hebertwellington) |
| :---: |
