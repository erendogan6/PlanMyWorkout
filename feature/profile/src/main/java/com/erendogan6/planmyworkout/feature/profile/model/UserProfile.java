package com.erendogan6.planmyworkout.feature.profile.model;

public class UserProfile {
    private String fullName;
    private String email;
    private String profilePictureUrl;

    public UserProfile() {}

    public UserProfile(String fullName, String email, String profilePictureUrl) {
        this.fullName = fullName;
        this.email = email;
        this.profilePictureUrl = profilePictureUrl;
    }

    // Getters and setters
    public String getFullName() { return fullName; }
    public void setFullName(String fullName) { this.fullName = fullName; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getProfilePictureUrl() { return profilePictureUrl; }
    public void setProfilePictureUrl(String profilePictureUrl) { this.profilePictureUrl = profilePictureUrl; }
}