package com.telesoftas.retrofitimpl.data.entities;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

import java.lang.reflect.Type;

/**
 * @author Vidmantas Kerbelis (vkerbelis@yahoo.com) on 2015-11-12.
 */
public class User {

    private String email;
    private String password;
    private String name;
    private String address;
    private String phone;
    private long dateOfBirth;
    private int gender;
    private boolean subscribed;
    private String serverId;

    public User() {
    }

    public User(String email, String password) {
        this.email = email;
        this.password = password;
    }

    public static boolean equals(User first, User second) {
        if (first == null && second == null) {
            return true;
        } else if (first == null || second == null) {
            return false;
        }
        return !(first.name != null && !first.name.equals(second.name)) &&
                !(first.address != null && !first.address.equals(second.address)) &&
                !(first.email != null && !first.email.equals(second.email)) &&
                !(first.phone != null && !first.phone.equals(second.phone)) &&
                first.dateOfBirth == second.dateOfBirth &&
                first.gender == second.gender &&
                first.subscribed == second.subscribed;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public long getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(long dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public int getGender() {
        return gender;
    }

    public void setGender(int gender) {
        this.gender = gender;
    }

    public boolean isSubscribed() {
        return subscribed;
    }

    public void setSubscribed(boolean subscribed) {
        this.subscribed = subscribed;
    }

    public String getServerId() {
        return serverId;
    }

    public void setServerId(String id) {
        this.serverId = id;
    }

    public static class UserSerializer implements JsonSerializer<User> {

        @Override
        public JsonElement serialize(User src, Type typeOfSrc, JsonSerializationContext context) {
            JsonObject result = new JsonObject();
            result.add("email", new JsonPrimitive(src.getEmail()));
            if (src.getPassword() != null) {
                result.add("password", new JsonPrimitive(src.getPassword()));
            }
            if (src.getName() != null) {
                result.add("name", new JsonPrimitive(src.getName()));
            }
            if (src.getAddress() != null) {
                result.add("street_address", new JsonPrimitive(src.getAddress()));
            }
            return result;
        }
    }

    public static class UserDeserializer implements JsonDeserializer<User> {

        @Override
        public User deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
            JsonObject jsonObject = (JsonObject) json;
            User user = new User();
            JsonElement email = jsonObject.get("email");
            if (email != null) {
                user.setEmail(email.getAsString());
            }
            JsonElement name = jsonObject.get("name");
            if (name != null) {
                user.setName(name.getAsString());
            }
            JsonElement address = jsonObject.get("street_address");
            if (address != null) {
                user.setAddress(address.getAsString());
            }
            user.setServerId(jsonObject.get("id").getAsString());
            return user;
        }
    }
}
