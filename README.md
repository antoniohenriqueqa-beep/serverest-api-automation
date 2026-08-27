# Automação de Testes de API — ServeRest

![CI](https://github.com/antoniohenriqueqa-beep/serverest-api-automation/actions/workflows/ci.yml/badge.svg)

Framework de testes automatizados para a API REST [ServeRest](https://serverest.dev), construído com Java, RestAssured e Cucumber, com execução contínua no GitHub Actions e relatório Allure publicado no GitHub Pages.

O objetivo do projeto é demonstrar a estruturação de uma suíte de testes de API mantível: separação de responsabilidades, massa de dados dinâmica, validação de contrato por JSON Schema e integração ao pipeline.

**Relatório da última execução:** https://antoniohenriqueqa-beep.github.io/serverest-api-automation

---

## Stack

| Camada | Tecnologia |
|---|---|
| Linguagem | Java 17 |
| Cliente HTTP / asserções | RestAssured 5.4 |
| Especificação de cenários | Cucumber 7 (BDD, Gherkin em português) |
| Executor | JUnit 5 Platform Suite |
| Contrato | JSON Schema (draft-07) |
| Massa de dados | Datafaker |
| Relatório | Allure |
| CI/CD | GitHub Actions |
| Build | Maven |

---

## Como executar

Pré-requisitos: JDK 17 e Maven 3.8+.

```bash
git clone https://github.com/antoniohenriqueqa-beep/serverest-api-automation.git
cd serverest-api-automation
docker compose up -d
mvn test
```

O alvo padrão é a instância local. O ambiente público exige escolha explícita:

```bash
mvn test -Denv=hml
```

A razão é que a `serverest.dev` é mantida gratuitamente pela comunidade, tem dados de terceiros e indisponibilidade eventual — rodar contra ela por padrão tornaria a execução não reprodutível e dispararia tráfego que ninguém pediu.

**O pipeline também não usa o ambiente público.** Sobe a própria instância em container a cada execução. Além da questão de etiqueta, o motivo que mais pesa é de confiabilidade: um build vermelho por dado de terceiro ou indisponibilidade não indica regressão nenhuma, e suíte que falha sem motivo real deixa de ser levada a sério — na prática, desliga a verificação.

Execução por tag:

```bash
mvn test -Dtags="@smoke"
mvn test -Dtags="@negativo and @seguranca"
```

Relatório local:

```bash
mvn allure:serve
```

---

## Estrutura do projeto

```
src/test/
├── java/br/com/qa/serverest/
│   ├── client/      Camada de acesso HTTP — uma classe por recurso da API
│   ├── config/      Leitura da configuração por ambiente
│   ├── factory/     Geração de massa de teste dinâmica
│   ├── model/       POJOs de request e response
│   ├── runner/      Suite JUnit que dispara o Cucumber
│   ├── steps/       Tradução do Gherkin em chamadas aos clients
│   └── support/     Request spec, contexto de cenário e hooks
└── resources/
    ├── features/    Cenários em Gherkin (pt-BR)
    ├── schemas/     Contratos JSON Schema
    └── config/      Propriedades por ambiente (hml, local)
```

---

## Decisões de arquitetura

**Camada de client separada dos steps.** Nenhum step monta requisição diretamente. Se uma rota, um header ou o formato do payload mudar, o ajuste acontece em uma única classe e não se espalha por dezenas de arquivos. É o equivalente do Page Object Model aplicado a API.

**Request specification centralizada.** `SpecFactory` concentra base URI, content type e o filtro do Allure. Isso garante que toda requisição da suíte seja anexada ao relatório automaticamente, sem que cada teste precise se preocupar com isso.

**Contexto de cenário em `ThreadLocal`.** O estado compartilhado entre steps (token, id criado, última resposta) fica isolado por thread, o que permite habilitar execução paralela sem que um cenário enxergue o dado de outro. Os hooks limpam o contexto ao fim de cada cenário.

**Massa de dados sempre dinâmica.** A ServeRest é um ambiente público e compartilhado. E-mail e nome de produto fixos causariam falha por colisão com dados de terceiros — um falso negativo que destrói a confiança na suíte. Por isso todo cadastro usa dados gerados em tempo de execução.

**Pré-condição falha rápido e explícito.** Steps de setup (`Dado que existe um usuário cadastrado`) validam o status code e lançam exceção com o corpo da resposta se a preparação não funcionar. Isso separa "o teste encontrou um defeito" de "o ambiente não estava pronto" — distinção que evita horas de investigação em cima do bug errado.

**Validação de contrato separada da validação de negócio.** Cenários marcados com `@contrato` verificam a estrutura da resposta contra JSON Schema; os demais verificam regra de negócio. Uma quebra de contrato e uma regra incorreta são defeitos de natureza diferente e devem falhar em cenários diferentes.

**Gherkin sem detalhe de implementação.** Os cenários descrevem comportamento de negócio, não endpoint, payload ou ambiente. Isso mantém a feature legível para quem não é técnico e evita reescrever a especificação quando a implementação muda.

**Cada cenário de carrinho cria o próprio usuário.** A API permite apenas um carrinho por usuário, e um carrinho já concluído não pode ser recriado. Reaproveitar usuário entre cenários faria a suíte passar na primeira execução e falhar na segunda — o tipo de acoplamento a estado que torna uma suíte não confiável.

**Verificação de efeito colateral, não só de resposta.** Os cenários de conclusão e cancelamento de compra consultam o produto depois da operação para conferir a quantidade em estoque. Validar apenas o status code e a mensagem deixaria passar um defeito em que a API responde com sucesso mas não dá baixa no estoque.

---

## Cobertura atual

| Recurso | Cenários | Foco |
|---|---|---|
| `/usuarios` | 7 | Contrato da listagem, cadastro válido, e-mail duplicado, campos obrigatórios, consulta inexistente |
| `/login` | 3 | Token válido, senha incorreta, e-mail não cadastrado |
| `/produtos` | 7 | Contrato da listagem e do detalhe, recuperação por id, consulta inexistente, cadastro autorizado, bloqueio sem token, bloqueio para não administrador, nome duplicado |
| `/carrinhos` | 8 | Contrato da listagem, registro válido, limite de um carrinho por usuário, bloqueio sem token, produto inexistente, baixa e reposição de estoque |

Total: **25 cenários**, 114 steps.

---

## Próximos passos

- [ ] Testes de performance com k6 sobre os mesmos endpoints
- [ ] Execução paralela por recurso
- [ ] Camada de testes de UI sobre o front público do ServeRest
- [ ] Relatório de cobertura por endpoint
