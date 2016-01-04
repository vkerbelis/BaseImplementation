package com.telesoftas.retrofitimpl.data.entities;

import com.google.gson.annotations.SerializedName;

/**
 * @author Vidmantas Kerbelis (vkerbelis@yahoo.com) on 2015-11-12.
 */
public class UserResponse {

    @SerializedName("created_at")
    private String createdAt;

    @SerializedName("updated_at")
    private String updatedAt;

    private String email;

    @SerializedName("account_type")
    private String accountType;

    private String id;

    public String getId() {
        return id;
    }
}
