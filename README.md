# Recipes

## Table of Contents

- [About](#about)
- [Usage](#usage)

## About <a name = "about"></a>

This is a Hyperskill project.
An app for recipes storage. Allows storing, retrieving, updating, and deleting recipes.

## Usage <a name = "usage"></a>

### GET http://localhost:8080/api/recipe/id

<h4>Returns the recipe with specified id if user asking is registered.</h4>

### GET http://localhost:8080/api/recipe/search

<h4>Returns recipes with specified category or name if user asking is registered.</h4>

### POST http://localhost:8080/api/recipe/new

<h4>Saves new recipe if user posting is registered.</h4>

### DELETE http://localhost:8080/api/recipe/id

<h4>Deletes the recipe with specified id if user deleting is owner of this recipe.</h4>

### PUT http://localhost:8080/api/recipe/id

<h4>Updates the recipe with specified id if user updating is owner of this recipe.</h4>

### POST http://localhost:8080/api/register

<h4>Registers new user.</h4>
