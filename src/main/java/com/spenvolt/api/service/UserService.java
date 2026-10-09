package com.spenvolt.api.service;



import com.spenvolt.api.dto.UserRequestDTO;
import com.spenvolt.api.dto.UserResponseDTO;
import com.spenvolt.api.model.User;
import com.spenvolt.api.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

    @Transactional
    public UserResponseDTO create(UserRequestDTO req) {
        User user = new User();
        user.setName(req.getName());
        user.setEmail(req.getEmail());
        user.setPhotoUrl(req.getPhotoUrl());
        user.setPasswordHash(encoder.encode(req.getPassword()));
        return toResponse(userRepository.save(user));
    }

    private UserResponseDTO toResponse(User u) {
        UserResponseDTO dto = new UserResponseDTO();
        dto.setId(u.getId());
        dto.setName(u.getName());
        dto.setEmail(u.getEmail());
        dto.setPhotoUrl(u.getPhotoUrl());
        dto.setActive(u.isActive());
        dto.setCreatedAt(u.getCreatedAt());
        return dto;
    }

    @Transactional(readOnly = true)
    public UserResponseDTO findById(int id) {
        return toResponse(userRepository.findById(id).orElseThrow());
    }
}
