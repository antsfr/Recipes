package recipes.presentation;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import recipes.business.user.User;
import recipes.business.user.UserService;

import javax.validation.Valid;
import java.util.Collections;
import java.util.Map;

@RestController
@RequestMapping("/api")
@Validated
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) { this.userService = userService; }

    @PostMapping("/register")
    @ResponseStatus(code = HttpStatus.OK)
    public ResponseEntity<Map<String, Long>> registerUser(@Valid @RequestBody User user) {
        return ResponseEntity.ok(Collections.singletonMap("id", userService.addUser(user)));
    }
}
