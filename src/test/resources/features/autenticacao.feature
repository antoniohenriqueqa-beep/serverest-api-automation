# language: pt
@autenticacao
Funcionalidade: Autenticação de usuários

  Como consumidor da API ServeRest
  Quero autenticar usuários cadastrados
  Para obter o token necessário às operações protegidas

  @smoke
  Cenário: Login com credenciais válidas deve retornar token
    Dado que existe um usuário administrador cadastrado
    Quando realizo o login com as credenciais do usuário
    Então o status code deve ser 200
    E a mensagem retornada deve ser "Login realizado com sucesso"
    E um token de autorização deve ser retornado

  @negativo
  Cenário: Login com senha incorreta deve ser rejeitado
    Dado que existe um usuário administrador cadastrado
    Quando realizo o login com a senha "senha-invalida"
    Então o status code deve ser 401
    E a mensagem retornada deve ser "Email e/ou senha inválidos"

  @negativo
  Cenário: Login com e-mail não cadastrado deve ser rejeitado
    Quando realizo o login com o e-mail "nao.cadastrado@teste.com" e a senha "qualquer123"
    Então o status code deve ser 401
    E a mensagem retornada deve ser "Email e/ou senha inválidos"
