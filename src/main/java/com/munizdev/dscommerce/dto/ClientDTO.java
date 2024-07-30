package com.munizdev.dscommerce.dto;

import com.munizdev.dscommerce.entities.User;

public class ClientDTO {

    private Long id;
    private String name;

    public ClientDTO() {
    }

    public ClientDTO(User entity) {
        id = entity.getId();
        name = entity.getName();
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }
}
