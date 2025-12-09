import com.TaskManager.TaskManager.entities.User;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/test")
public class Test1Controller {

    @GetMapping("/check-auth")
    public ResponseEntity<?> checkAuth() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        if (auth == null) {
            return ResponseEntity.status(401).body("Not authenticated");
        }

        Map<String, Object> response = new HashMap<>();
        response.put("authenticated", true);
        response.put("username", auth.getName());
        response.put("authorities", auth.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.toList()));
        response.put("principalClass", auth.getPrincipal().getClass().getName());

        if (auth.getPrincipal() instanceof User) {
            User user = (User) auth.getPrincipal();
            response.put("userRole", user.getUserRole());
            response.put("userRoleName", user.getUserRole().name());
        }

        return ResponseEntity.ok(response);
    }
}