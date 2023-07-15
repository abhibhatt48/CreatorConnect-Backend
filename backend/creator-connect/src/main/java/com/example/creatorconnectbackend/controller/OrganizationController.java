package com.example.creatorconnectbackend.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.creatorconnectbackend.model.Organization;
import com.example.creatorconnectbackend.service.OrganizationService;

@RestController
@RequestMapping("/api/organizations")
public class OrganizationController {
    private final OrganizationService organizationService;

    @Autowired
    public OrganizationController(OrganizationService organizationService) {
        this.organizationService = organizationService;
    }

    @PostMapping("/register/{userId}")
    public ResponseEntity<Organization> registerOrganization(@PathVariable Long userId,@RequestBody Organization organization) {
        Organization registeredOrganization = organizationService.register(organization, userId);
        return new ResponseEntity<>(registeredOrganization, HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Organization> getOrganizationById(@PathVariable("id") Long id) {
        Organization organization = organizationService.getById(id);
        return new ResponseEntity<>(organization, HttpStatus.OK);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Organization> updateOrganization(@PathVariable("id") Long id, @RequestBody Organization updatedOrganization) {
        Organization organization = organizationService.update(id, updatedOrganization);
        return new ResponseEntity<>(organization, HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<List<Organization>> getAllOrganizations() {
        List<Organization> organizations = organizationService.getAll();
        return new ResponseEntity<>(organizations, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteOrganizationById(@PathVariable("id") Long id) {
        organizationService.deleteById(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}