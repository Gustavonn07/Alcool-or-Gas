# Álcool ou Gasolina?

Aplicativo Android desenvolvido em **Jetpack Compose** para ajudar o usuário a decidir qual combustível vale mais a pena abastecer com base no preço do álcool e da gasolina. O app também permite salvar postos, editar, excluir, limitar a quantidade salva e alternar entre tema claro/escuro.

---

## 🚀 Funcionalidades

### 🔢 Cálculo automático

O app calcula automaticamente se vale mais a pena abastecer com **álcool** ou **gasolina**, usando a regra dos **70% ou 75%**, definida pelo usuário.

### 💾 Cadastro de Postos

* Salvar postos com nome, preço do álcool, preço da gasolina e percentual usado.
* Editar postos existentes.
* Excluir postos individualmente.
* Limpar todos os postos.
* Limitar quantidade máxima (5, 10, 20 ou ilimitado).

### 🎨 Tema claro/escuro

O usuário pode alternar entre modo claro e modo escuro, utilizando **State + SharedPreferences** para persistência.

### 📋 Listagem de Postos

* Lista completa dos postos cadastrados.
* Exibição dos valores cadastrados.
* Popup para editar/excluir.
* Botões de configuração do limite.

---

## 🛠️ Tecnologias Utilizadas

* **Kotlin**
* **Jetpack Compose**
* **Material 3**
* **SharedPreferences** para persistência de tema e switch de percentual
* **Armazenamento local** (listagem via Helpers customizados)

---

## 📂 Estrutura do Projeto

```
com.example.alcoolorgas
│
├── MainActivity.kt
│   - Tela principal
│   - Controle de tema
│   - Controle do switch 70% / 75%
│   - Fluxo de criação e edição de postos
│
├── components/
│   ├── MoneyField.kt
│   └── StationListPage.kt
│       - Lista de postos
│       - Limite de registros
│       - Limpar tudo
│
├── helpers/
│   └── FuelHelpers.kt
│       - Manipulação de salvar/editar/excluir postos
│
├── models/
│   └── FuelStation.kt
│       - Modelo de dados
│
└── ui/theme/
    - Configurações de tema (Material3)
```

---

## 📱 Fluxo do Usuário

1. Usuário informa valor do álcool, gasolina e nome do posto.
2. Escolhe entre 70% ou 75%.
3. Clica em **Calcular** para ver o resultado.
4. Pode salvar o posto.
5. Na lista:

    * Pode editar um posto.
    * Pode excluir.
    * Pode ajustar limite de armazenamento.
    * Pode limpar tudo.

---

## ⚙️ Lógica do Cálculo

```kotlin
if (alcool / gas <= percentValue / 100.0) {
    "Abasteça com Álcool"
} else {
    "Abasteça com Gasolina"
}
```

---

## 🧪 Tratamento de Edição

Ao selecionar um posto na lista, o app retorna para a tela principal com os campos preenchidos automaticamente.

---

## 📌 Persistência

O app salva automaticamente:

* Tema claro/escuro
* Valor do switch 70/75

Postos são salvos via Storage interno do app.

---

## 📦 Como Rodar

1. Abra o projeto no Android Studio.
2. Sincronize as dependências.
3. Rode em um dispositivo físico ou emulador.

---

## 📝 Licença

Projeto feito para fins educacionais.
