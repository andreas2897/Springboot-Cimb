package com.cimb.tokolapak.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.cimb.tokolapak.dao.UserRepo;
import com.cimb.tokolapak.entity.User;

@RestController
public class VerifiedStatusController {
	@Autowired
	private UserRepo userRepo;

	@GetMapping("/verified")
	public User isVerivied (@RequestParam String username) {
		
		User findUser = userRepo.findByUsername(username).get();
		if (findUser != null) {
			findUser.setVerifiedStatus(true);
			userRepo.save(findUser);
			return findUser;
		} else {
			return null;
		}
	}
}
