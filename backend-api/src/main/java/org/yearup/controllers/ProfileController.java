package org.yearup.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import org.yearup.data.ProductDao;
import org.yearup.data.ProfileDao;
import org.yearup.data.UserDao;
import org.yearup.models.Profile;
import org.yearup.models.User;

import java.security.Principal;

@RestController
@RequestMapping("profile")
@CrossOrigin
public class ProfileController {
    private ProfileDao profileDao;
    private UserDao userDao;

    @Autowired
    public ProfileController(ProfileDao profileDao, UserDao userDao){
        this.profileDao = profileDao;
        this.userDao = userDao;
    }

    @GetMapping("")
    @PreAuthorize("hasRole('ROLE_USER')")
    public ResponseEntity<Profile> getUserProfile(Principal principal){
        String userName = principal.getName();
        User user = userDao.getByUserName(userName);
        int userId = user.getId();

        Profile userProfile = profileDao.getProfileById(userId);

        if(userProfile == null){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User profile not found.");
        }

        return ResponseEntity.ok(userProfile);
    }

    @PostMapping("")
    @PreAuthorize("hasRole('ROLE_USER')")
    public ResponseEntity<Profile> createUserProfile(@RequestBody Profile profile,
                                                     Principal principal){
        String userName = principal.getName();
        User user = userDao.getByUserName(userName);
        int userId = user.getId();

        profile.setUserId(userId);
        Profile userProfile = profileDao.create(profile);

        return ResponseEntity.status(HttpStatus.CREATED).body(userProfile);
    }

    @PutMapping("")
    @PreAuthorize("hasRole('ROLE_USER')")
    public ResponseEntity<Profile> updateUserProfile(@RequestBody Profile profile, Principal principal){
        String userName = principal.getName();
        User user = userDao.getByUserName(userName);
        int userId = user.getId();

        Profile userProfile = profileDao.updateProfile(userId, profile);

        if(userProfile == null){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User profile not found.");
        }

        return ResponseEntity.ok(userProfile);
    }

}
