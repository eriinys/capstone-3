package org.yearup.data;


import org.yearup.models.Profile;

public interface ProfileDao
{
    Profile getProfileById(int userId);
    Profile create(Profile profile);
    Profile updateProfile(int userId, Profile profile);
}
