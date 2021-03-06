package com.cimb.tokolapak.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.cimb.tokolapak.dao.UserRepo;
import com.cimb.tokolapak.entity.User;
import com.cimb.tokolapak.util.EmailUtil;

@RestController
@RequestMapping("/users")
@CrossOrigin
public class UserController {
	
	// User1 Password = 123321 -> abc123abc1212
	// User2 Password = 123321 -> cba1212ab123
	
	@Autowired
	private UserRepo userRepo;
	
	private PasswordEncoder pwEncoder = new BCryptPasswordEncoder();
	@Autowired
	private EmailUtil emailUtil;
	
	@PostMapping
	public User registerUser(@RequestBody User user) {
//		Optional<User> findUser = userRepo.findByUsername(user.getUsername());
//		
//		if (findUser.toString() != "Optional.empty") {
//			throw new RuntimeException("Username exists!");
//		}
		String encodedPassword = pwEncoder.encode(user.getPassword());
		user.setPassword(encodedPassword);
		
		User savedUser = userRepo.save(user);
		savedUser.setPassword(null);
		
		String link = "http://localhost:3000/verified?username=" + user.getUsername();
		
		this.emailUtil.sendEmail(user.getEmail(), "User Registration", "Hi " + user.getUsername() + " please kindly paste the link to verified your account " + link );
		
		return savedUser;
	}
	
	// Cara 1 menggunakan POST method
	@PostMapping("/login")
	public User loginUser (@RequestBody User user) {
		User findUser = userRepo.findByUsername(user.getUsername()).get();
								// Password raw       password sudah encode
		if (pwEncoder.matches(user.getPassword(), findUser.getPassword())) {
			findUser.setPassword(null);
			return findUser;
		} 
		
		throw new RuntimeException("Wrong password!");
//		return null;
	}
	// localhost:8080/users/login?username=seto&password=password123
	@GetMapping("/login")
	public User getLoginUser(@RequestParam String username, @RequestParam String password) {
		User findUser = userRepo.findByUsername(username).get();

		if (pwEncoder.matches(password, findUser.getPassword())) {	
			findUser.setPassword(null);
			return findUser;
		} 

		throw new RuntimeException("Wrong password!");
	}
	
	@PostMapping("/sendEmail")
	public String sendEmailTesting() {
		this.emailUtil.sendEmail("andreas.2897@gmail.com", "Testing springboot emial", "Hiay HIya Hiya dapet email");
		return "email sent";
	}
}