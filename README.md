# To-Do List Application

## Descrizione

Una semplice applicazione web **To-Do List** sviluppata con **Java/Spring Boot** per il backend, **MySQL** come database, e un frontend React (integrato nel progetto) compilato separatamente. Permette agli utenti di gestire le proprie attività con operazioni CRUD e autenticazione di base tramite JWT.

## Screenshot

![Screenshot Home](link_screenshot_home)

## Caratteristiche

- Gestione attività (Create, Read, Update, Delete)
- Autenticazione e autorizzazione con Spring Security + JWT
- Interfaccia React responsive eseguita su `localhost:3000`
- Persistenza dati con MySQL

## Prerequisiti

- Java 21+
- Maven
- MySQL

## Setup e avvio

1. Clona il repository:

   ```bash
   git clone https://github.com/Manu3l02/to-do-list.git
   cd to-do-list
   ```

2. Avvio del backend Spring Boot:

   ```bash
   mvn clean install
   java -jar target/to-do-list-0.0.1-SNAPSHOT.jar
   ```

3. Avvio del frontend React:
   Apri una seconda finestra del terminale, posizionati nella cartella /src/main/resources/static/frontend e lancia:

   ```bash
   npm start
   ```

4. Accesso all’applicazione
   - Frontend: [http://localhost:3000](http://localhost:3000)
   - Backend API: [http://localhost:8443](http://localhost:8443)

> **Nota**: React comunica con il backend attraverso chiamate HTTP REST su porta 8443. Assicurati che il proxy nel `package.json` punti a `http://localhost:8443`.

## Struttura delle cartelle

```text
to-do-list/
├── .gitignore
├── mvnw, mvnw.cmd
├── pom.xml                     # Configurazione Maven principale
├── HELP.md                     # Documentazione integrativa
├── src/
│   ├── main/
│   │   ├── java/               # Codice sorgente Java
│   │   └── resources/
│   │       ├── application.properties
│   └── test/                   # Test JUnit
├── target/                     # Output build Maven
```

## API Endpoints

| Metodo | Endpoint           | Descrizione                |
| ------ | ------------------ | -------------------------- |
| POST   | `/api/auth/signup` | Registrazione utente       |
| POST   | `/api/auth/login`  | Login e ottenimento JWT    |
| GET    | `/api/tasks`       | Recupera tutte le attività |
| POST   | `/api/tasks`       | Crea nuova attività        |
| PUT    | `/api/tasks/{id}`  | Aggiorna un’attività       |
| DELETE | `/api/tasks/{id}`  | Elimina un’attività        |

## Roadmap

- Filtri per data e priorità
- Ordinamento drag-and-drop delle attività
- Deploy su piattaforma cloud (Heroku, Railway, ecc.)

## CI/CD (opzionale)

Esempio di **GitHub Actions** per build Maven e test:

```yaml
name: CI
on: [push]
jobs:
  build:
    runs-on: ubuntu-latest
    steps:
      - uses: actions/checkout@v2
      - name: Set up JDK
        uses: actions/setup-java@v2
        with:
          java-version: "17"
      - name: Build with Maven
        run: mvn clean install
```

## Contatti

- **Email**: manuel.altavela@gmail.com
- **LinkedIn**: https://www.linkedin.com/in/manuel-altavela-358243303/
