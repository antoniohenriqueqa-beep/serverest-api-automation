# Como contribuir

## Padrão de commits

Seguimos o [Conventional Commits](https://www.conventionalcommits.org/pt-br/):

```
feat:  novo cenário, client ou recurso
fix:   correção em teste ou infraestrutura
chore: build, dependências, configuração
ci:    pipeline
docs:  documentação
```

## Antes de abrir um PR

- [ ] `mvn test` passa localmente
- [ ] Cenários novos descrevem regra de negócio, não implementação
- [ ] Nenhum dado sensível ou credencial no código
- [ ] Massa de teste gerada dinamicamente, nunca fixa

## Convenções de código

- Steps não montam requisição — quem faz isso é o client do recurso
- Asserção fica no step, nunca no client
- Pré-condição que falha lança exceção com o corpo da resposta
- Tags: `@smoke`, `@negativo`, `@contrato`, `@seguranca`, `@regra-de-negocio`
