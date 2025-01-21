# Telegram-Currency-bot Application
The **Telegram-Currency-Bot** is a Spring Boot application that provides users with live exchange rate information and currency conversion features. This bot allows users to check the exchange 
rate of the Ukrainian Hryvnia (UAH) against several foreign currencies, such as the US Dollar (USD), Euro (EUR), Turkish Lira (TRY), and Romanian Leu (RON), based on the data of the country's major banks. Users can convert amounts between UAH and these currencies effortlessly through interactive bot commands.
## Features
- View live exchange rates of UAH against USD, EUR, TRY, and RON.
- Convert UAH to one of the supported foreign currencies based on the latest exchange rate.
- Convert foreign currencies (USD, EUR, TRY, or RON) into UAH.
- Interactive and user-friendly interface for selecting conversion options and providing required details.
- Fetch and process live exchange rate data from official central bank websites.
- Log user interactions to improve functionality and user experience.
- Ensure smooth application setup and operation with Spring Boot and Gradle.

## Requirements
The application is built using the following technologies:
- **Spring Boot**: 3.3.5
- **Java Platform (JDK)**: 21
- **Telegram Bots**: 6.9.7.0
- **MySQL**: 8.0.40
- **Flyway**: 11.0.1
- **Lombok**: 1.18.36
- **JUnit**: 5
- **Gradle**: 8.8
  
## Database Setup
Before running the application, follow these steps to set up the database:

1. **Create a MySQL 8.0.40 Database**  
   Set up a MySQL database to store the application’s data.

2. **Configure Database and User**  
   Perform the following steps in your MySQL instance to create a user and database for the application:

    1. Create a new user with a password:
       ```sql
       CREATE USER IF NOT EXISTS 'curAdmin'@'%' IDENTIFIED BY 'secret1234';
       ```

    2. Create a new database:
       ```sql
       CREATE DATABASE IF NOT EXISTS currency;
       ```

    3. Assign ownership of the database to the new user:
       ```sql
       GRANT ALL PRIVILEGES ON currency.* TO 'curAdmin'@'%';
       GRANT SUPER ON *.* TO 'curAdmin'@'%';
       ```

3. **Connect to the Database**  
   To connect to the `currency` database as the `curAdmin` user, use the following command in the terminal:
   ```bash
   mysql -u curAdmin -p -D currency

## Getting Started

1. Clone the repository:
   ```bash
   git clone https://github.com/Bohdan100/telegram-currency-bot
   cd telegram-currency-bot

2. Build and Run the Application Using Gradle in Terminal:
   ```bash
   .\gradlew bootRun     (for Windows)
   ./gradlew bootRun     (for Linux)
    ```
3. Build and Run the Application Using a JAR File:
   ```bash
   .\gradlew bootJar     (for Windows)
   ./gradlew bootJar     (for Linux)
   
   java -jar telegram-currency-bot.jar
    ```
4. Access the Application in your mobile phone.

5. Testing the application:
   ```bash
   .\gradlew test            (for Windows)
   ./gradlew test            (for Linux)
   ```
