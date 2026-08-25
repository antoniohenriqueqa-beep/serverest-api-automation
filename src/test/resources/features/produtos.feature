# language: pt
@produtos
Funcionalidade: Cadastro de produtos

  Como consumidor da API ServeRest
  Quero cadastrar produtos na loja
  Para garantir que as regras de autorização e unicidade são respeitadas

  @smoke
  Cenário: Administrador autenticado deve cadastrar produto
    Dado que estou autenticado como administrador
    E que possuo os dados de um novo produto
    Quando submeto o cadastro do produto
    Então o status code deve ser 201
    E a mensagem retornada deve ser "Cadastro realizado com sucesso"
    E o identificador do recurso criado deve ser retornado

  @negativo @seguranca
  Cenário: Cadastro de produto sem token deve ser bloqueado
    Dado que possuo os dados de um novo produto
    Quando submeto o cadastro do produto sem token de autenticação
    Então o status code deve ser 401
    E a mensagem retornada deve ser "Token de acesso ausente, inválido, expirado ou usuário do token não existe mais"

  @negativo @seguranca
  Cenário: Cadastro de produto por usuário não administrador deve ser bloqueado
    Dado que estou autenticado como usuário comum
    E que possuo os dados de um novo produto
    Quando submeto o cadastro do produto
    Então o status code deve ser 403
    E a mensagem retornada deve ser "Rota exclusiva para administradores"

  @negativo
  Cenário: Cadastro de produto com nome já existente deve ser bloqueado
    Dado que estou autenticado como administrador
    E que existe um produto cadastrado
    Quando submeto o cadastro de outro produto com o mesmo nome
    Então o status code deve ser 400
    E a mensagem retornada deve ser "Já existe produto com esse nome"
