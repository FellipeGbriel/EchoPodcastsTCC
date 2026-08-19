# Echo: Podcasts Educacionais

Aplicativo Android que agrega podcasts de cunho educacional como material de apoio a programas de monitoria escolar. O projeto foi desenvolvido como Trabalho de Conclusão de Curso do Técnico em **Desenvolvimento de Sistemas** da **ETEC de Taboão da Serra (Centro Paula Souza)**, em 2021.

O nome vem de *echo* ("eco", em inglês): a ideia é ecoar conhecimento de estudantes para outros estudantes.

## Contexto

O trabalho parte da queda no rendimento escolar registrada durante o ensino remoto na pandemia de COVID-19 e do crescimento do consumo de podcasts no Brasil. A proposta é oferecer conteúdo didático em formato de áudio — sob demanda, gratuito e portátil — produzido por monitores e consumido por estudantes a qualquer hora e lugar, inclusive offline após o download.

**Objetivo geral:** desenvolver uma plataforma de apoio a estudantes e monitores escolares, com didática mais atrativa em formato podcast, de fácil acesso e inteiramente gratuita.

## Funcionalidades

- **Cadastro e login** de usuários, com persistência de sessão (`SharedPreferences`) — o app reabre direto na home do perfil correspondente.
- **Três perfis de usuário**, cada um com sua própria tela inicial:
  - **Estudante** (`tipoUser_id = 1`) — ouvir e baixar podcasts;
  - **Administrador** (`tipoUser_id = 2`) — o mesmo, mais acesso à tela de gerenciamento;
  - **Monitor** (`tipoUser_id = 3`) — o mesmo do estudante, mais envio de podcasts.
- **Catálogo por categoria**, com 10 disciplinas: Português, Matemática, História, Geografia, Biologia, Física, Química, Filosofia, Inglês e Espanhol.
- **Reprodução de áudio** com player em notificação (JcPlayer), listando os áudios filtrados pela categoria escolhida.
- **Download dos episódios** via `DownloadManager`, permitindo consumo offline.
- **Envio de áudios** pelo monitor: seleção do arquivo do dispositivo, escolha de categoria e título, com upload para o Firebase Storage e registro dos metadados no Realtime Database.

## Arquitetura

O app usa duas fontes de dados distintas, cada uma com um papel:

```
┌──────────────────────────┐
│  App Android (Java)      │
└───────┬──────────────┬───┘
        │              │
   Volley (HTTP)   Firebase SDK
        │              │
┌───────▼──────┐  ┌────▼────────────────────────┐
│ Scripts PHP  │  │ Firebase                    │
│  + MySQL     │  │  • Storage → arquivos .mp3  │
│ (usuários)   │  │  • Realtime DB → metadados  │
└──────────────┘  └─────────────────────────────┘
```

- **MySQL via PHP** — contas de usuário. O app faz `POST` com Volley para scripts PHP hospedados, que consultam e gravam na tabela `usuarios` (`nome`, `email`, `senha`, `apelido`, `tipoUser_id`, `escolaridade_id`). A senha é gravada com `AES_ENCRYPT` do MySQL.
- **Firebase** — mídia. Os arquivos de áudio vão para o Storage (nó `audios`) e seus metadados para o Realtime Database (nó `audios`), consultados com `orderByChild("audiosCategory").equalTo(categoria)`.

## Estrutura do repositório

```
app/src/main/java/com/example/echopodcasts/
├── splash.java               # Tela de abertura; redireciona conforme sessão salva
├── tela_inicial.java         # Escolha entre entrar e cadastrar
├── tela_login.java           # Autenticação via getUsuarios.php
├── cadastro_comum.java       # Cadastro parte 1 (nome, e-mail, senha, apelido)
├── cadastro_comum2.java      # Cadastro parte 2 (escolaridade) + putUsuarios.php
├── tela_home_comum.java      # Home do estudante
├── tela_home_monitor.java    # Home do monitor
├── tela_homeAdmin.java       # Home do administrador
├── ouvir_categorias.java     # Grade das 10 disciplinas
├── tela_player.java          # Lista + player + download dos podcasts
├── tela_playerGerenciar.java # Player da visão de gerenciamento
├── tela_gerenciar.java       # Tela de gerenciamento (administrador)
├── upload_arquivo.java       # Envio de áudio para o Firebase
└── Model/
    ├── EnviarArquivo.java    # Modelo de escrita no Realtime Database
    └── ReceberArquivo.java   # Modelo de leitura do Realtime Database

PHP/
├── dbConnection.php          # Credenciais do MySQL (dev/produção)
├── getUsuarios.php           # Login: aceita e-mail ou apelido
├── putUsuarios.php           # Cadastro de novo usuário
└── teste.php                 # Script de testes
```

## Tecnologias

| Camada | Ferramenta |
|---|---|
| Aplicativo | Java, Android Studio, `minSdk 21` / `targetSdk 31` |
| Rede | Volley 1.2.1 |
| Mídia e metadados | Firebase Storage, Firebase Realtime Database, Firebase Auth |
| Player | JcPlayer 2.7.0 |
| Imagens | Picasso, Glide, CircleImageView |
| Backend de contas | PHP + MySQL |
| Build | Gradle 7.0.2 |
| Protótipos e diagramas | Adobe XD, Photoshop, After Effects, CorelDRAW, Lucidchart |

## Como executar

**Pré-requisitos:** Android Studio (Arctic Fox ou superior), JDK 8+, um dispositivo ou emulador com Android 5.0 (API 21) ou superior.

1. Clone o repositório e abra a pasta raiz no Android Studio.
2. Aguarde a sincronização do Gradle — as dependências vêm do Google, Maven Central e JitPack (já configurados em `settings.gradle`).
3. Rode o app:

```bash
./gradlew installDebug
```

Para a parte de contas funcionar, é preciso um servidor com PHP e MySQL:

1. Crie o banco `echopod_bd` com as tabelas `usuarios`, `tipoUser` e `escolaridade`.
2. Ajuste as credenciais em `PHP/dbConnection.php`.
3. Publique os scripts de `PHP/` no servidor.
4. Atualize as URLs do web service em `tela_login.java` e `cadastro_comum2.java` para apontar para o seu host.

O `app/google-services.json` incluído aponta para o projeto Firebase original (`echo-podcasts`). Para rodar com um backend próprio, substitua o arquivo pelo do seu projeto Firebase.

## Pontos para melhoria

Este repositório traz o código como foi entregue no TCC em 2021. A partir dele, os próximos passos naturais de evolução são:

- **Concluir o fluxo de aprovação de áudios.** `tela_gerenciar.java` hoje apenas carrega o layout. O campo `visibilidade` em `EnviarArquivo` (`"I"`) já foi previsto para esse controle e é o gancho para o administrador liberar ou recusar os envios dos monitores.
- **Reescrever as consultas dos scripts PHP com *prepared statements*.** Hoje o SQL é montado por concatenação de strings, e `getUsuarios.php` compara a senha em texto plano — este é o ajuste mais importante antes de expor o backend fora do ambiente de estudo.
- **Definir `$tipoUser_id` a partir do `$_POST` em `putUsuarios.php`.** A variável é usada no `INSERT` sem ter sido atribuída.
- **Mover as URLs do web service para um arquivo de configuração**, em vez de mantê-las fixas nas activities, e **enxugar as permissões do `AndroidManifest.xml`** para apenas as que o app usa de fato.

Melhorias de produto já previstas na conclusão do trabalho: gravação de áudio no próprio dispositivo, edição de áudio no aplicativo e criação de listas de reprodução.

## Autores

- Fellipe Gabriel Alves da Silva
- Gabriel Bezerra Teixeira
- João Vitor Rocha Sousa
- Matheus Braz Soares
- Vinicius dos Santos Bezerra

**Orientadores:** Prof. Fabiano Jorge de Oliveira Lopes e Prof. Rodolfo Votto Filho
**Instituição:** ETEC de Taboão da Serra — Centro Paula Souza, 2021
