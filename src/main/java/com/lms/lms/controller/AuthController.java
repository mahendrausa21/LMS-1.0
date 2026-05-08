   package com.lms.lms.controller;

   import com.lms.lms.model.User;  // Ensure this matches your actual User class
   import com.lms.lms.service.UserService;
   import jakarta.validation.Valid;
   import org.springframework.beans.factory.annotation.Autowired;
   import org.springframework.security.authentication.AuthenticationManager;
   import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
   import org.springframework.security.core.Authentication;
   import org.springframework.security.core.context.SecurityContextHolder;
   import org.springframework.stereotype.Controller;
   import org.springframework.ui.Model;
   import org.springframework.validation.BindingResult;
   import org.springframework.web.bind.annotation.GetMapping;
   import org.springframework.web.bind.annotation.PostMapping;
   import org.springframework.web.bind.annotation.ModelAttribute;

   @Controller
   public class AuthController {
       private final UserService userService;
       private final AuthenticationManager authenticationManager;

       @Autowired
       public AuthController(UserService userService, AuthenticationManager authenticationManager) {
           this.userService = userService;
           this.authenticationManager = authenticationManager;
       }

       @GetMapping("/login")
       public String login() {
           return "login";
       }

       @GetMapping("/register")
       public String registerForm(Model model) {
           model.addAttribute("user", new User());
           return "register";
       }

       @PostMapping("/register")
       public String registerUser(@ModelAttribute @Valid User user, BindingResult bindingResult, Model model) {
           if (bindingResult.hasErrors()) {
               return "register";  // Redisplay form with errors
           }

           if (userService.findByEmail(user.getEmail()) != null) {
               model.addAttribute("error", "Email already exists");
               return "register";
           }

           if (user.getName() == null || user.getName().isEmpty()) {
               model.addAttribute("error", "Name is required");
               return "register";
           }

           if (user.getEmail() == null || user.getEmail().isEmpty()) {
               model.addAttribute("error", "Email is required");
               return "register";
           }

           if (user.getPassword() == null || user.getPassword().isEmpty()) {
               model.addAttribute("error", "Password is required");
               return "register";
           }

           if (user.getRole() == null) {
               model.addAttribute("error", "Role is required");
               return "register";
           }

           // Save the user
           userService.saveUser(user);  // Assuming this encodes the password

           try {
               // Authenticate and login the user
               authenticateAndLogin(user);

               // Get the authenticated user's role and redirect
               Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
               
               if (authentication == null || authentication.getAuthorities() == null || authentication.getAuthorities().isEmpty()) {
                   model.addAttribute("error", "Authentication failed. Please try logging in.");
                   return "register";
               }
               
               String role = authentication.getAuthorities().stream().findFirst().get().getAuthority();  // e.g., "ROLE_STUDENT"

               if (role.equals("ROLE_STUDENT")) {
                   return "redirect:/student-dashboard";  // Redirect to student dashboard
               } else if (role.equals("ROLE_TEACHER")) {
                   return "redirect:/teacher-dashboard";  // Redirect to teacher dashboard
               } else {
                   return "redirect:/home";  // Fallback redirect
               }
           } catch (Exception e) {
               // If authentication fails, redirect to login page
               model.addAttribute("error", "Registration successful! Please log in.");
               return "redirect:/login";
           }
       }

       private void authenticateAndLogin(User user) {
           Authentication authentication = authenticationManager.authenticate(
               new UsernamePasswordAuthenticationToken(user.getEmail(), user.getPassword())
           );
           SecurityContextHolder.getContext().setAuthentication(authentication);
       }
   }
   