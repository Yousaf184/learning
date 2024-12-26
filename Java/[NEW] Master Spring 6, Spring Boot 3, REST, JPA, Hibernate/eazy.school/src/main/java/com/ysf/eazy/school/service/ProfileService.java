package com.ysf.eazy.school.service;

import com.ysf.eazy.school.dao.jpa.PersonRepository;
import com.ysf.eazy.school.dto.Profile;
import com.ysf.eazy.school.model.jpa.Address;
import com.ysf.eazy.school.model.jpa.Person;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ProfileService {

    private final PersonRepository personRepository;

    @Autowired
    public ProfileService(PersonRepository personRepository) {
        this.personRepository = personRepository;
    }

    public Profile getProfileOfLoggedInUser(Person user) {
        Profile profile = new Profile();

        profile.setName(user.getName());
        profile.setMobileNumber(user.getMobileNumber());
        profile.setEmail(user.getEmail());

        if (user.getAddress() != null) {
            Address address = user.getAddress();

            profile.setAddress1(address.getAddress1());
            profile.setAddress2(address.getAddress2());
            profile.setCity(address.getCity());
            profile.setState(address.getState());
            profile.setZipCode(address.getZipCode());
        }

        return profile;
    }

    public Person updateUserProfile(Profile updatedProfile, Person userToUpdate) {
        userToUpdate.setName(updatedProfile.getName());
        userToUpdate.setMobileNumber(updatedProfile.getMobileNumber());
        userToUpdate.setEmail(updatedProfile.getEmail());

        if (userToUpdate.getAddress() == null) {
            userToUpdate.setAddress(new Address());
        }

        Address address = userToUpdate.getAddress();
        address.setAddress1(updatedProfile.getAddress1());
        address.setAddress2(updatedProfile.getAddress2());
        address.setCity(updatedProfile.getCity());
        address.setState(updatedProfile.getState());
        address.setZipCode(updatedProfile.getZipCode());
        userToUpdate.setAddress(address);

        return this.personRepository.save(userToUpdate);
    }
}
