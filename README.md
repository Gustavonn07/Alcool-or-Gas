# Alcool ou Gasolina

Este é um aplicativo Android desenvolvido em Kotlin cujo objetivo é
registrar preços de combustível (álcool e gasolina), salvar a
localização do posto e exibir essa localização no mapa. O usuário pode
cadastrar postos, visualizar uma lista dos registros e abrir o mapa
nativo mostrando onde o posto está localizado.

------------------------------------------------------------------------

## Funcionalidades

### Cadastro de Preços

-   O usuário informa:
    -   Nome do posto
    -   Preço do álcool
    -   Preço da gasolina
-   O aplicativo calcula automaticamente a porcentagem (`percent`)
    referente à relação álcool/gasolina.

### Coleta de Localização

-   O aplicativo solicita permissão de acesso à localização
    (`ACCESS_FINE_LOCATION`).
-   Ao registrar um posto, a latitude e longitude atuais são capturadas
    e salvas junto aos dados.
-   Não utiliza *bibliotecas externas* para GPS: usa apenas a API nativa
    do Android (`FusedLocationProviderClient` ou `LocationManager`,
    conforme implementação vista em aula).

### Visualização de Mapa

-   Na lista de postos cadastrados, ao clicar em um item:
    -   É exibida uma opção para visualizar o local no mapa.

    -   A abertura do mapa é feita usando `Intent` com URI no formato:

            geo:LAT,LNG?q=LAT,LNG(Posto)

    -   O Google Maps (ou outro app compatível) abre automaticamente.

### Estrutura de Dados

O modelo principal usado no app:

``` kotlin
data class FuelStation(
    val id: String,
    val name: String,
    val alcool: Double,
    val gasolina: Double,
    val latitude: Double? = null,
    val longitude: Double? = null,
    val date: Long = System.currentTimeMillis(),
    val percent: Int
)
```

### Persistência

-   Os dados podem ser salvos via SharedPreferences ou banco local
    (dependendo da implementação da aula).
-   Cada registro inclui:
    -   Preços
    -   Nome
    -   Coordenadas
    -   Data de criação
    -   Porcentagem calculada do custo-benefício do álcool

------------------------------------------------------------------------

## Tecnologias Utilizadas

-   **Kotlin**
-   **Android SDK**
-   **Intents e Geo URIs**
-   **Permissões com AndroidX**
-   **Localização sem bibliotecas externas**

------------------------------------------------------------------------

## Como Rodar o Projeto

1.  Clone o repositório:

    ``` bash
    git clone https://github.com/SEU-USUARIO/alcool-ou-gasolina.git
    ```

2.  Abra no Android Studio.

3.  Execute no emulador ou dispositivo físico.

4.  Permita a coleta de localização quando solicitado.

------------------------------------------------------------------------

## Permissões Necessárias

``` xml
<uses-permission android:name="android.permission.ACCESS_FINE_LOCATION" />
```

------------------------------------------------------------------------

## Funcionalidade de Mapa com Intent

Exemplo do Intent usado:

``` kotlin
val uri = Uri.parse("geo:${station.latitude},${station.longitude}?q=${station.latitude},${station.longitude}(${station.name})")
val intent = Intent(Intent.ACTION_VIEW, uri)
intent.setPackage("com.google.android.apps.maps")
startActivity(intent)
```

------------------------------------------------------------------------

## Infos a mais
Vídeo do projeto: https://www.youtube.com/watch?v=yf2jUPRt4ZA

<img width="468" height="998" alt="image" src="https://github.com/user-attachments/assets/9b752942-18b8-4580-b53a-9f15a6b8af98" />
<img width="469" height="988" alt="image" src="https://github.com/user-attachments/assets/a93053ae-b6b2-4576-8cf1-78bb2b4011f8" />

### Membros: 
Gustavo Nepomuceno Nogueira - 554728 <br />
Tais Gomes Crisostomo Saboia - 554499

## Licença

Projeto desenvolvido para fins educacionais (Aulas de Desenvolvimento
Android -- 2025).
