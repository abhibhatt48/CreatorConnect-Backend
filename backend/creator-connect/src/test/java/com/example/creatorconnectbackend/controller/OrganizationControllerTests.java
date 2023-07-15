package com.example.creatorconnectbackend.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.example.creatorconnectbackend.model.Organization;
import com.example.creatorconnectbackend.service.OrganizationService;

public class OrganizationControllerTests {
    private OrganizationController organizationController;

    @Mock
    private OrganizationService organizationService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        organizationController = new OrganizationController(organizationService);
    }

    @Test
    public void testRegisterOrganization() {
        Organization organization = new Organization();
        Long userId = 12345L;

        when(organizationService.register(organization, userId)).thenReturn(organization);

        ResponseEntity<Organization> response = organizationController.registerOrganization(userId, organization);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(organization, response.getBody());
    }

    @Test
    public void testGetOrganizationById() {
        Organization organization = new Organization();
        Long id = 1L;
        when(organizationService.getById(id)).thenReturn(organization);

        ResponseEntity<Organization> response = organizationController.getOrganizationById(id);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(organization, response.getBody());
    }

    @Test
    public void testUpdateOrganization() {
        Organization organization = new Organization();
        Long id = 1L;
        when(organizationService.update(id, organization)).thenReturn(organization);

        ResponseEntity<Organization> response = organizationController.updateOrganization(id, organization);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(organization, response.getBody());
    }

    @Test
    public void testGetAllOrganizations() {
        Organization organization1 = new Organization();
        Organization organization2 = new Organization();
        List<Organization> organizations = Arrays.asList(organization1, organization2);
        when(organizationService.getAll()).thenReturn(organizations);

        ResponseEntity<List<Organization>> response = organizationController.getAllOrganizations();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(organizations, response.getBody());
    }

    @Test
    public void testDeleteOrganizationById() {
        Long id = 1L;

        ResponseEntity<?> response = organizationController.deleteOrganizationById(id);

        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
        verify(organizationService).deleteById(id);
    }
}
