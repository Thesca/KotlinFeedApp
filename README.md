# **PROJETO FINAL DA DISCIPLINA DE APLICATIVOS MOBILE**

## **Pré-requisitos**
1. **Instalar o Android Studio**:
   - Baixe e instale a última versão do [Android Studio](https://developer.android.com/studio).
2. **Configurar o Ambiente**:
   - Certifique-se de que o Android SDK está instalado e atualizado no Android Studio.

## **Executando o Aplicativo**
1. Conecte um dispositivo Android via USB ou inicie um Emulador Android:
   - Vá em **"Tools > Device Manager"** e configure um emulador, se necessário.
2. Clique no botão **"Run"** (ícone de play) na barra de ferramentas do Android Studio.
3. Aguarde a compilação e execução no dispositivo/emulador.

---


## **Explicação do Projeto: Aplicativo de Feed de Posts**
O projeto tem como objetivo criar um aplicativo de feed de posts onde todos os usuários possam criar e visualizar publicações. Ele envolve uma estrutura básica de navegação e funcionalidades essenciais para gerenciar a experiência do usuário de forma eficiente.

---

## **Objetivo Geral**
Criar um aplicativo funcional e intuitivo com quatro telas principais: **Login**, **Cadastro**, **Criação de Post** e **Feed**. O propósito principal é permitir que usuários façam o login, publiquem posts e visualizem um feed compartilhado.

---

## **Funcionalidades Principais**

### 1. **Tela de Login (Main Screen)**
- **Objetivo:** Permitir que o usuário acesse sua conta no aplicativo/feed.
- **Funcionalidade:**
  - Campos para inserir e-mail e senha.
  - Botão de "Login".
  - Link ou botão para redirecionar à Tela de Cadastro, caso o usuário não possua conta.
- **Navegação:**
  - Após um login bem-sucedido, o usuário é direcionado ao **Feed Principal**.
  - Caso contrário, é exibida uma mensagem de erro (ex.: "Usuário ou senha inválidos").
- **RESPONSÁVEL:** Gustavo Liberado

---

### 2. **Tela de Cadastro**
- **Objetivo:** Registrar novos usuários no aplicativo.
- **Funcionalidade:**
  - Campos para inserir informações do usuário (ex.: nome, e-mail, senha).
  - Validações para evitar dados incorretos ou incompletos.
  - Botão "Cadastrar".
  - Botão “Voltar para o Login”.
- **Navegação:**
  - Após o cadastro bem-sucedido, o usuário é redirecionado à **Tela de Login**.
- **RESPONSÁVEL:** Thiago Escaliente

---

### 3. **Tela de Feed Principal**
- **Objetivo:** Exibir todos os posts criados pelos usuários em ordem cronológica.
- **Funcionalidade:**
  - Lista de posts com o Botão "Criar Post" para acessar a tela de criação.
  - Botão "Logout" para encerrar a sessão do usuário.
- **Navegação:**
  - O feed é acessado após o login.
  - Botões permitem alternar entre o **Feed**, **Criação de Post** ou encerrar a sessão.
- **RESPONSÁVEL:** Vinicius Santana

---

### 4. **Tela de Criação de Post**
- **Objetivo:** Permitir que o usuário crie e publique novos posts.
- **Funcionalidade:**
  - Campo para inserir o conteúdo do post.
  - Botão "Postar" para enviar o post ao feed.
  - Botão "Voltar".
  - Botão "Ver o feed".
- **Navegação:**
  - Após criar o post, o usuário é redirecionado de volta ao **Feed**, onde o novo post aparece imediatamente.
- **RESPONSÁVEL:** Alberth Ronaldy

---

# **Classes do Projeto**

## **Classe: Usuario**
- **Atributos:**
  - `id` (Int): Identificador único do usuário.
  - `nome` (String): Nome do usuário.
  - `email` (String): Endereço de e-mail do usuário.
  - `senha` (String): Senha para autenticação.

---

## **Classe: Post**
- **Atributos:**
  - `id` (Int): Identificador único do post.
  - `idUsuario` (String): Referência ao identificador do usuário que criou o post.
  - `conteudo` (String): Texto ou conteúdo do post.


![alt text](<DiagramaClasses.png>)
