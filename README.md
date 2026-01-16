# Desafio FullStack

Este documento descreve a arquitetura da solucão proposta para o desafio FullStack proposto.

## Arquitetura

A arquitetura proposta e a seguinte:

- Backend
  - Ecossistema Spring Boot
  - Arquitetura Em Camadas
  - - Service Layer
  - - Controller Layer
  - - Repository Layer
  - Banco de Dados H2

- EJB
  - Implementado como uma lib (supondo que o mesmo fosse disponibilizado por uma aplicacao com WildFly ou outro servico) externa
  - Arquitetura Em Camadas
  - - Service Layer
  - - Model/Domain Layer

- Frontend
  - A documentacao do frontend esta no arquivo README.md do frontend-module.

- No backend foi implementado um endpoint para cada operacao proposta no desafio.
  CRUD de Beneficios
  Transferencia de saldo

- A Transferencia de saldo foi implementada como um endpoint que utiliza o EJB para realizar a transferencia de saldo.

- Foram implementados testes unitários e integrados.

- Foi criado um pom.xml no root-project para facilitar a gestao de dependencias.

- Para acessar o swagger do backend, basta executar o backend-module e acessar http://localhost:8080/swagger-ui/index.html

- Foi implementado um handler para capturar erros e retornar uma resposta padronizada.

