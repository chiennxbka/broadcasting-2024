package com.mintpot.broadcasting.rest;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.mintpot.broadcasting.common.entities.User;
import com.mintpot.broadcasting.common.request.StatusRequest;
import com.mintpot.broadcasting.common.request.UserReq;
import com.mintpot.broadcasting.common.response.UserResponse;
import com.mintpot.broadcasting.service.user.UserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequiredArgsConstructor
@Api(tags = {"[Admin APIs] User management"})
public class UserController extends AbstractController {

    private final UserService service;

    @GetMapping("/user")
    @ApiOperation(value = "Get all user")
    public ResponseEntity<List<UserResponse>> getCollections(
        @RequestParam(value = "query", required = false) String query) {
        List<UserResponse> users = service.getCollections(query);
        return ResponseEntity.ok(users);
    }

    @GetMapping("/user/me")
    @ApiOperation(value = "Get current user")
    public ResponseEntity<UserResponse> getUserInformation() throws JsonProcessingException {
        return ResponseEntity.ok(service.getMe());
    }

    @PatchMapping("/user/me")
    @ApiOperation(value = "Update status")
    public ResponseEntity<UserResponse> updateStatus(@RequestBody StatusRequest request) {
        return ResponseEntity.ok(service.updateStatus(request));
    }

    @PostMapping("/user/signup")
    @ApiOperation(value = "Register an user")
    public ResponseEntity<Void> register(UserReq form) {
        User userExists = service.findByUsername(form.getUsername());
        if (userExists != null) {
            throw new DataIntegrityViolationException("User with username " + form.getUsername() + " already exists");
        }
        Long id = service.insert(form);
        URI uri = URI.create(String.format("/api/v2/user/%d", id));
        return ResponseEntity.created(uri).build();
    }

    @PutMapping("/user/{id}")
    @ApiOperation(value = "Update user by id")
    public ResponseEntity<Void> update(@PathVariable(name = "id") Long id, UserReq form) {
        form.setId(id);
        service.update(form);
        return ResponseEntity.noContent().build();
    }

    @GetMapping(value = "/user/{id}")
    @ApiOperation(value = "Get user by id")
    public ResponseEntity<User> findById(@PathVariable(name = "id") Long id) {
        return ResponseEntity.ok(service.findById(id));
    }

    @GetMapping(value = "/user/summaries")
    @ApiOperation(value = "Get summaries")
    public ResponseEntity<List<User>> getSummaries() {
        return ResponseEntity.ok(service.getSummaries());
    }

    @DeleteMapping(value = "/user/{id}")
    @ApiOperation(value = "Delete user by id")
    public ResponseEntity<Void> remove(@PathVariable(name = "id") Long id) {
        service.remove(id);
        return ResponseEntity.ok().build();
    }
}
