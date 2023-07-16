package com.example.creatorconnectbackend.service;

import com.example.creatorconnectbackend.model.Organization;
import com.example.creatorconnectbackend.model.User;
import com.example.creatorconnectbackend.service.OrganizationService;
import com.example.creatorconnectbackend.service.UserService;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

class OrganizationServiceTests {

    @Mock
    private UserService userService;

    @Mock
    private JdbcTemplate jdbcTemplate;

    @Mock
    private EmailService emailService;

    @InjectMocks
    private OrganizationService organizationService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        organizationService = new OrganizationService(jdbcTemplate, userService);
    }

    @Test
    void testRegister_ValidInput_ReturnsOrganization() {
        // Mock the user retrieval from the database
        User user = new User();
        user.setUserID(1L);
        user.setUser_type("Organization");
        //System.out.println(user);

        when(jdbcTemplate.queryForObject(anyString(), any(Object[].class), any(RowMapper.class))).thenReturn(user);

        // Create the organization object
        Organization organization = new Organization();
        organization.setUserId(1L);
        organization.setOrgName("Example Org");
        organization.setProfileImage("example.jpg");
        organization.setCompanyType("Type A");
        organization.setSize(100L);
        organization.setWebsiteLink("https://example.org");
        organization.setTargetInfluencerType("Type B");
        organization.setLocation("Location A");

        // Mock the jdbcInsert object
        SimpleJdbcInsert jdbcInsert = mock(SimpleJdbcInsert.class);

        // Mock the behavior of jdbcInsert.withTableName
        when(jdbcInsert.withTableName(anyString())).thenReturn(jdbcInsert);

        // Mock the behavior of jdbcInsert.execute
        when(jdbcInsert.execute(any(Map.class))).thenReturn(1);

        // Mock the behavior of jdbcTemplate.queryForObject
        when(jdbcTemplate.queryForObject(anyString(), any(Object[].class), any(RowMapper.class))).thenReturn(user);

        // Call the method under test
        Organization result = organizationService.register(organization, user.getUserID());

        // Verify the result
        assertNotNull(result);
        assertEquals(organization, result);

        // Verify that the user is fetched from the database
        verify(jdbcTemplate, times(1)).queryForObject(anyString(), any(Object[].class), any(RowMapper.class));

        // Verify that the organization is inserted into the database
        verify(jdbcInsert, times(1)).withTableName(anyString());
        verify(jdbcInsert, times(1)).execute(any(Map.class));
    }



    @Test
    void testRegister_NonOrganizationUser_ReturnsNull() {
        Organization organization = new Organization();
        organization.setOrgName("Example Org");

        Long userId = 1L;

        User user = new User();
        user.setUser_type("Influencer");

        when(jdbcTemplate.queryForObject(anyString(), any(Object[].class), any(RowMapper.class))).thenReturn(user);

        Organization result = organizationService.register(organization, userId);

        assertNull(result);
        verify(jdbcTemplate, never()).update(anyString(), any(Object[].class));
    }


    @Test
    void testGetById_ValidId_ReturnsOrganization() {
        Long id = 1L;

        Organization organization = new Organization();
        organization.setOrgID(id);
        organization.setOrgName("Example Org");

        when(jdbcTemplate.queryForObject(anyString(), any(Object[].class), any(RowMapper.class))).thenReturn(organization);

        Organization result = organizationService.getById(id);

        assertNotNull(result);
        assertEquals(organization, result);
        verify(jdbcTemplate, times(1)).queryForObject(anyString(), any(Object[].class), any(RowMapper.class));
    }

    @Test
    void testGetById_InvalidId_ThrowsException() {
        Long id = 1L;

        when(jdbcTemplate.queryForObject(anyString(), any(Object[].class), any(RowMapper.class))).thenThrow(EmptyResultDataAccessException.class);

        assertThrows(RuntimeException.class, () -> organizationService.getById(id));

        verify(jdbcTemplate, times(1)).queryForObject(anyString(), any(Object[].class), any(RowMapper.class));
    }

    @Test
    void testUpdate_ValidId_ReturnsUpdatedOrganization() {
        Long id = 1L;

        Organization updatedOrganization = new Organization();
        updatedOrganization.setOrgID(id);
        updatedOrganization.setOrgName("Updated Org");

        when(jdbcTemplate.update(anyString(), any(Object[].class))).thenReturn(1);
        when(organizationService.getById(id)).thenReturn(updatedOrganization);

        Organization result = organizationService.update(id, updatedOrganization);

        assertNotNull(result);
        assertEquals(updatedOrganization, result);
        verify(jdbcTemplate, times(1)).update(anyString(), any(Object[].class));

    }

    @Test
    void testUpdate_InvalidId_ThrowsException() {
        Long id = 1L;

        Organization updatedOrganization = new Organization();
        updatedOrganization.setOrgID(id);
        updatedOrganization.setOrgName("Updated Org");

        when(jdbcTemplate.update(anyString(), any(Object[].class))).thenReturn(0);

        assertThrows(RuntimeException.class, () -> organizationService.update(id, updatedOrganization));

        verify(jdbcTemplate, times(1)).update(anyString(), any(Object[].class));
        OrganizationService organizationServiceMock = Mockito.mock(OrganizationService.class);
        verify(organizationServiceMock, never()).getById(id);
    }



    @Test
    void testGetAll_ReturnsListOfOrganizations() {
        List<Organization> organizations = new ArrayList<>();
        organizations.add(new Organization());
        organizations.add(new Organization());

        when(jdbcTemplate.query(anyString(), any(RowMapper.class))).thenReturn(organizations);

        List<Organization> result = organizationService.getAll();

        assertNotNull(result);
        assertEquals(organizations.size(), result.size());
        assertEquals(organizations, result);
        verify(jdbcTemplate, times(1)).query(anyString(), any(RowMapper.class));
    }

    @Test
    void testDeleteById_ValidId_DeletesOrganization() {
        Long id = 1L;

        when(jdbcTemplate.update(anyString(), any(Object[].class))).thenReturn(1);

        assertDoesNotThrow(() -> organizationService.deleteById(id));

        verify(jdbcTemplate, times(1)).update(anyString(), any(Object[].class));
    }

    @Test
    void testDeleteById_InvalidId_ThrowsException() {
        Long id = 1L;

        when(jdbcTemplate.update(anyString(), any(Object[].class))).thenReturn(0);

        assertThrows(RuntimeException.class, () -> organizationService.deleteById(id));

        verify(jdbcTemplate, times(1)).update(anyString(), any(Object[].class));
    }
}




//updatedOrganization.setOrgName("Pepsi");
//        updatedOrganization.setSize(120L);
//        updatedOrganization.setProfileImage("pepsi.jpg");
//        updatedOrganization.setCompanyType("Beverage");
//        updatedOrganization.setWebsiteLink("https://pepsi.com");
//        updatedOrganization.setLocation("USA");
//        updatedOrganization.setTargetInfluencerType("Footballer");
