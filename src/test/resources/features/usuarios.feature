# language: pt
@usuarios
Funcionalidade: Cadastro e consulta de usuários

  Como consumidor da API ServeRest
  Quero cadastrar e consultar usuários
  Para garantir que o contrato e as regras de negócio do recurso estão íntegros

  @smoke @contrato
  Cenário: Listagem de usuários deve respeitar o contrato publicado
    Quando consulto a lista de usuários
    Então o status code deve ser 200
    E o corpo da resposta deve seguir o schema "usuarios-lista"

  @smoke
  Cenário: Cadastro de usuário com dados válidos
    Dado que possuo os dados de um novo usuário administrador
    Quando submeto o cadastro do usuário
    Então o status code deve ser 201
    E a mensagem retornada deve ser "Cadastro realizado com sucesso"
    E o corpo da resposta deve seguir o schema "cadastro-sucesso"
    E o identificador do recurso criado deve ser retornado

  @negativo
  Cenário: Cadastro deve ser bloqueado quando o e-mail já está em uso
    Dado que existe um usuário administrador cadastrado
    Quando submeto o cadastro de outro usuário com o mesmo e-mail
    Então o status code deve ser 400
    E a mensagem retornada deve ser "Este email já está sendo usado"

  @negativo @contrato
  Esquema do Cenário: Cadastro deve ser bloqueado sem o campo obrigatório <campo>
    Dado que possuo os dados de um novo usuário administrador
    Quando submeto o cadastro do usuário sem o campo "<campo>"
    Então o status code deve ser 400
    E o corpo deve conter o erro "<mensagem>" para o campo "<campo>"

    Exemplos:
      | campo    | mensagem                    |
      | nome     | nome é obrigatório          |
      | email    | email é obrigatório         |
      | password | password é obrigatório      |

  @negativo
  Cenário: Consulta de usuário inexistente deve retornar mensagem de negócio
    Quando consulto o usuário de id "0000000000000000"
    Então o status code deve ser 400
    E a mensagem retornada deve ser "Usuário não encontrado"
