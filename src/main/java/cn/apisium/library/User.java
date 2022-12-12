package cn.apisium.library;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import java.util.Objects;
import java.util.Scanner;

/**
 * User
 */
@JsonPropertyOrder({ "userId", "firstName", "lastName", "email" })
public final class User {
    @JsonProperty("User_id")
    private String userId;
    @JsonProperty("first_name")
    private String firstName;
    @JsonProperty("last_name")
    private String lastName;
    @JsonProperty("email")
    private String email;

    public User() { }
    public User(Scanner in) {
        System.out.print("Creating User:");
        System.out.print("User ID: ");
        setUserId(in.nextLine());
        System.out.print("First Name: ");
        setFirstName(in.nextLine());
        System.out.print("Last Name: ");
        setLastName(in.nextLine());
        System.out.print("Email: ");
        setEmail(in.nextLine());
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    @SuppressWarnings("unused")
    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    @SuppressWarnings("unused")
    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @Override
    public String toString() {
        return "User{" +
                "userId='" + userId + '\'' +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", email='" + email + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        User user = (User) o;

        if (!Objects.equals(userId, user.userId)) return false;
        if (!Objects.equals(firstName, user.firstName)) return false;
        if (!Objects.equals(lastName, user.lastName)) return false;
        return Objects.equals(email, user.email);
    }

    @Override
    public int hashCode() {
        int result = userId != null ? userId.hashCode() : 0;
        result = 31 * result + (firstName != null ? firstName.hashCode() : 0);
        result = 31 * result + (lastName != null ? lastName.hashCode() : 0);
        result = 31 * result + (email != null ? email.hashCode() : 0);
        return result;
    }
}
