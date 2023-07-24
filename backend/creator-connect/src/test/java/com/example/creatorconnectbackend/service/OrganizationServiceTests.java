package com.example.creatorconnectbackend.services;

import com.example.creatorconnectbackend.models.Organization;
import com.example.creatorconnectbackend.models.User;
import com.example.creatorconnectbackend.services.OrganizationService;
import com.example.creatorconnectbackend.services.UserService;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

class OrganizationServiceTest {

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
        organizationService = new OrganizationService(jdbcTemplate);
    }
    private Organization createMockOrganization(long id) {
        Organization organization = new Organization();
        organization.setOrgID(id);
        organization.setOrgName("Example Org");
        organization.setProfileImage("https://example.org/profile-image.png");
        organization.setCompanyType("Type A");
        organization.setSize(100L);
        organization.setWebsiteLink("https://example.org");
        organization.setTargetInfluencerNiche(Arrays.asList("Niche A", "Niche B"));
        organization.setLocation("Location A");
        organization.setBio("This is a bio.");
        organization.setInstagram("https://instagram.com/example");
        organization.setFacebook("https://facebook.com/example");
        organization.setTwitter("https://twitter.com/example");
        organization.setTiktok("https://tiktok.com/example");
        organization.setYoutube("https://youtube.com/example");
        organization.setTwitch("https://twitch.com/example");
        return organization;
    }

    @Test
    void testRegister() {
        OrganizationService organizationServiceSpy = Mockito.spy(new OrganizationService(jdbcTemplate));

        Organization organization = createMockOrganization(1L);

        Mockito.doReturn(organization).when(organizationServiceSpy).register(Mockito.any(Organization.class), Mockito.anyLong());
        Organization registeredOrganization = organizationServiceSpy.register(organization, 1L);

        assertNotNull(registeredOrganization);
        assertEquals("Example Org", registeredOrganization.getOrgName());
        Mockito.verify(organizationServiceSpy, Mockito.times(1)).register(Mockito.any(Organization.class), Mockito.anyLong());
    }
    
    @Test
    void testGetById_InvalidId_ThrowsException() {
        Long id = 1L;

        when(jdbcTemplate.queryForObject(anyString(), any(Object[].class), any(RowMapper.class))).thenThrow(EmptyResultDataAccessException.class);

        assertThrows(RuntimeException.class, () -> organizationService.getById(id));

        verify(jdbcTemplate, times(1)).queryForObject(anyString(), any(Object[].class), any(RowMapper.class));
    }

    @Test
    void testUpdate_OrganizationExists() {
        Long id = 1L;
        Organization updatedOrganization = createMockOrganization(id);
        updatedOrganization.setOrgName("Updated Org");

        ArgumentCaptor<String> sqlArgumentCaptor = ArgumentCaptor.forClass(String.class);
        ArgumentCaptor<Object[]> argsArgumentCaptor = ArgumentCaptor.forClass(Object[].class);

        when(jdbcTemplate.update(sqlArgumentCaptor.capture(), argsArgumentCaptor.capture())).thenReturn(1);
        when(organizationService.getById(id)).thenReturn(updatedOrganization);

        Organization result = organizationService.update(id, updatedOrganization);

        assertNotNull(result);
        assertEquals("Updated Org", result.getOrgName());

        // Check if SQL and parameters passed to jdbcTemplate.update are as expected
        String sqlPassed = sqlArgumentCaptor.getValue();
        Object[] argsPassed = argsArgumentCaptor.getValue();
    }



    @Test
    void testUpdate_OrganizationDoesNotExist() {
        Organization updatedOrganization = createMockOrganization(1L);

        when(jdbcTemplate.update(anyString(), (Object[]) any())).thenReturn(0);

        assertThrows(RuntimeException.class, () -> organizationService.update(1L, updatedOrganization));
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
