package recipes.presentation;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.CacheControl;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import recipes.business.recipe.Recipe;
import recipes.business.recipe.RecipeService;
import recipes.business.user.UserService;

import javax.validation.Valid;
import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.TimeUnit;

@RestController
@RequestMapping("/api/recipe")
public class RecipeController {
    private final RecipeService recipeService;
    private final UserService userService;

    @Autowired
    public RecipeController(RecipeService recipeService, UserService userService) {
        this.recipeService = recipeService;
        this.userService = userService;
    }

    @GetMapping("/{id}")
    public ResponseEntity<Recipe> getRecipe(@PathVariable("id") Long id) {
        Recipe recipe = recipeService.getRecipeById(id).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "No such recipe"));
        return ResponseEntity.ok().
                cacheControl(CacheControl.maxAge(5, TimeUnit.MINUTES)).
                body(recipe);
//        return recipeService.getRecipeById(id).orElseThrow(
//                () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "No such recipe"));
    }

    @PostMapping("/new")
    public ResponseEntity<Map<String, Long>> postRecipe(@Valid @RequestBody Recipe recipe,
                                                        @AuthenticationPrincipal UserDetails userDetails) {
        recipe.setDate(LocalDateTime.now());
        recipe.setAuthor(userService.getUserByEmail(userDetails.getUsername()).get());
        return ResponseEntity.ok(Collections.singletonMap("id", recipeService.addRecipe(recipe)));
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteRecipe(@PathVariable("id") Long id, @AuthenticationPrincipal UserDetails userDetails) {
        Optional<Recipe> recipe = recipeService.getRecipeById(id);
        if (recipe.isEmpty())
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "No such recipe");
        if (recipeHasAuthor(id))
            if (userIsNotAuthor(userDetails, id))
                throw new ResponseStatusException(HttpStatus.FORBIDDEN, "You cannot delete other authors recipes");
        recipeService.deleteRecipeById(id);
    }

    @PutMapping("/{id}")
    @ResponseStatus(code = HttpStatus.NO_CONTENT)
    public void updateRecipe(@Valid @RequestBody Recipe recipe, @PathVariable("id") Long id,
                             @AuthenticationPrincipal UserDetails userDetails) {
        Optional<Recipe> recipeOptional = recipeService.getRecipeById(id);
        if (recipeOptional.isEmpty())
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "No such recipe");
        if (userIsNotAuthor(userDetails, id))
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "You cannot modify other authors recipes");
        recipe.setDate(LocalDateTime.now());
        recipe.setId(id);
        recipeService.addRecipe(recipe);
    }

    @GetMapping(path = "/search", params = {"!name"})
    public ResponseEntity<List<Recipe>> findRecipeByCategory(@RequestParam String category) {
        //return new ArrayList<>(recipeService.getRecipeByCategory(category));
        List<Recipe> recipes = new ArrayList<>(recipeService.getRecipeByCategory(category));
        return ResponseEntity.ok().
                cacheControl(CacheControl.maxAge(5, TimeUnit.MINUTES)).
                body(recipes);

    }

    @GetMapping(path = "/search", params = {"!category"})
    public ResponseEntity<List<Recipe>> findRecipeByName(@RequestParam String name) {
        //return new ArrayList<>(recipeService.getRecipeByName(name));
        List<Recipe> recipes = new ArrayList<>(recipeService.getRecipeByName(name));
        return ResponseEntity.ok().
                cacheControl(CacheControl.maxAge(5, TimeUnit.MINUTES)).
                body(recipes);
    }


    private boolean userIsNotAuthor(UserDetails userDetails, Long recipeId) {
        return !recipeService.getRecipeById(recipeId).get().getAuthor().getId()
                .equals(userService.getUserByEmail(userDetails.getUsername()).get().getId());
    }

    private boolean recipeHasAuthor(Long recipeId) {
        return recipeService.getRecipeById(recipeId).get().getAuthor() != null;
    }

}
