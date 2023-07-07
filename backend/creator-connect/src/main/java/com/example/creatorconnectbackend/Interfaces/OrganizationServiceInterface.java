package com.example.creatorconnectbackend.Interfaces;

import java.util.List;

import com.example.creatorconnectbackend.model.Organization;

public interface OrganizationServiceInterface {
	Organization register(Organization organization);
    Organization getById(Long id);
    Organization update(Long id, Organization updatedOrganization);
    List<Organization> getAll();
    void deleteById(Long id);
}
