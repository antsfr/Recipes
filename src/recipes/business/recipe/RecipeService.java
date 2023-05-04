package recipes.business.recipe;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import recipes.persistence.RecipeRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class RecipeService {

    private final RecipeRepository recipeRepository;
    private final List<Recipe> recipes = new ArrayList<>();

    @Autowired
    public RecipeService(RecipeRepository repository) {
        this.recipeRepository = repository;
    }

    public List<Recipe> getRecipes() {
        recipes.clear();
        recipeRepository.findAll().forEach(recipes::add);
        return recipes;
    }

    public Optional<Recipe> getRecipeById(Long id) {
        return recipeRepository.findById(id);
    }

    public void deleteRecipeById(Long id) {
        recipeRepository.deleteById(id);
    }

    public Long addRecipe(Recipe recipe) {
        return recipeRepository.save(recipe).getId();
    }

    public List<Recipe> getRecipeByCategory(String category) {
        return recipeRepository.findByCategoryIgnoreCaseOrderByDateDesc(category);
    }

    public List<Recipe> getRecipeByName(String name) {
        return recipeRepository.findByNameContainingIgnoreCaseOrderByDateDesc(name);
    }

}
