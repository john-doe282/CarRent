package com.andrew.rental.service;

import com.andrew.rental.domain.User;
import com.andrew.rental.service.dto.UserDTO;
import org.springframework.stereotype.Service;

@Service
public class PoliceService {
    public boolean eligible(UserDTO credentials) {
        return true;
    }
}
