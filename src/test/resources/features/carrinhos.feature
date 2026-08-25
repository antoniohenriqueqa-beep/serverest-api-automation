# language: pt
@carrinhos
Funcionalidade: Carrinho de compras

  Como consumidor da API ServeRest
  Quero registrar, concluir e cancelar carrinhos
  Para garantir que as regras de unicidade e de baixa de estoque são respeitadas

  @smoke @contrato
  Cenário: Listagem de carrinhos deve respeitar o contrato publicado
    Quando consulto a lista de carrinhos
    Então o status code deve ser 200
    E o corpo da resposta deve seguir o schema "carrinhos-lista"

  @smoke
  Cenário: Usuário autenticado deve registrar carrinho com produto disponível
    Dado que estou autenticado como administrador
    E que existe um produto cadastrado
    Quando registro um carrinho com 2 unidades do produto
    Então o status code deve ser 201
    E a mensagem retornada deve ser "Cadastro realizado com sucesso"
    E o identificador do recurso criado deve ser retornado

  @negativo @regra-de-negocio
  Cenário: Não deve ser permitido mais de um carrinho por usuário
    Dado que estou autenticado como administrador
    E que existe um produto cadastrado
    E que registrei um carrinho com 1 unidade do produto
    Quando registro um carrinho com 1 unidade do produto
    Então o status code deve ser 400
    E a mensagem retornada deve ser "Não é permitido ter mais de 1 carrinho"

  @negativo @seguranca
  Cenário: Registro de carrinho sem token deve ser bloqueado
    Dado que estou autenticado como administrador
    E que existe um produto cadastrado
    Quando registro um carrinho sem token de autenticação
    Então o status code deve ser 401
    E a mensagem retornada deve ser "Token de acesso ausente, inválido, expirado ou usuário do token não existe mais"

  @negativo
  Cenário: Registro de carrinho com produto inexistente deve ser bloqueado
    Dado que estou autenticado como administrador
    Quando registro um carrinho com o produto de id "0000000000000000"
    Então o status code deve ser 400
    E a mensagem retornada deve ser "Produto não encontrado"

  @regra-de-negocio
  Cenário: Conclusão da compra deve dar baixa no estoque do produto
    Dado que estou autenticado como administrador
    E que existe um produto cadastrado com 10 unidades em estoque
    E que registrei um carrinho com 3 unidades do produto
    Quando concluo a compra do carrinho
    Então o status code deve ser 200
    E a mensagem retornada deve ser "Registro excluído com sucesso"
    E a quantidade do produto em estoque deve ser 7

  @regra-de-negocio
  Cenário: Cancelamento da compra deve devolver a quantidade ao estoque
    Dado que estou autenticado como administrador
    E que existe um produto cadastrado com 10 unidades em estoque
    E que registrei um carrinho com 4 unidades do produto
    Quando cancelo a compra do carrinho
    Então o status code deve ser 200
    E a mensagem retornada deve ser "Registro excluído com sucesso. Estoque dos produtos reabastecido"
    E a quantidade do produto em estoque deve ser 10

  @negativo
  Cenário: Conclusão sem carrinho registrado deve retornar mensagem de negócio
    Dado que estou autenticado como administrador
    Quando concluo a compra do carrinho
    Então o status code deve ser 200
    E a mensagem retornada deve ser "Não foi encontrado carrinho para esse usuário"
