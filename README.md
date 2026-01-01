# Pokedex Application
######################


A Full Stack web application to search and analyze Pokemon data. It's like a dream come true to go on an adventure to learn about every Pokemon.
This project brings that nostalgia to life with modern technology. So here I'm to make that small kid in you happy.

## Architecture

The application is built using a standard layered architecture:
- **Frontend**: HTML5, CSS3, and Vanilla JavaScript (ES6).
- **Backend**: Java 17 with Spring Boot.
- **Data Source**: PokeAPI (v2).

### Key Features
- **Search Functionality**: Retreives detailed Pokemon statistics, official artwork, and physical attributes.
- **Autocomplete**: Provides real-time name suggestions as the user types.
- **Performance Optimization**: Implements a custom Least Recently Used (LRU) cache with a Time-To-Live (TTL) eviction policy.
- **Responsive Design**: Custom CSS dashboard interface.


## API Reference

### 1. Search Pokemon
Fetches details for a specific Pokemon.

- **URL**: `/api/pokemon/{name}`
- **Method**: `GET`
- **Response**: JSON
- **Success Code**: 200 OK
- **Error Code**: 404 Not Found

### 2. Autocomplete Suggestions
Returns a list of matching names based on the input query.

- **URL**: `/api/pokemon/suggestions?query={prefix}`
- **Method**: `GET`
- **Response**: JSON Array of Strings


## Setup and execution
### Prerequisites
- Java JDK 17 or higher
- Maven 3.6+


### Local Installation
1. Clone the repository
2. cd pokedex-app
3. mvn spring-boot:run

## -> Just in case if you were checking the repo and want to run it directly there is a file named "PokedexApplication.java" just run this file. 
