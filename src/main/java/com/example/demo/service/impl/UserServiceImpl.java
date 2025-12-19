package com.example.demo.service.impl;

import com.example.demo.dto.UserDto;
import com.example.demo.entity.Role;
import com.example.demo.entity.User;
import com.example.demo.exception.BadRequestException;
import com.example.demo.exception.NotFoundException;
import com.example.demo.mapper.UserMapper;
import com.example.demo.repository.RoleRepository;
import com.example.demo.repository.UserRepository;
import com.example.demo.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final UserMapper userMapper;

    @Override
    @Transactional(readOnly = true)
    public Page<UserDto> getAll(Pageable pageable) {
        return userRepository.findAll(pageable).map(userMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public UserDto getById(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("User not found: " + id));
        return userMapper.toDto(user);
    }

    @Override
    public UserDto create(UserDto dto) {
        if (userRepository.existsByUsername(dto.getUsername())) {
            throw new BadRequestException("Username already exists");
        }
        if (userRepository.existsByEmail(dto.getEmail())) {
            throw new BadRequestException("Email already exists");
        }
        Role role = roleRepository.findByName(dto.getRoleName())
                .orElseThrow(() -> new BadRequestException("Role not found: " + dto.getRoleName()));
        if (dto.getPassword() == null || dto.getPassword().isBlank()) {
            throw new BadRequestException("Password is required");
        }
        User user = userMapper.fromDto(dto, role);
        user.setPassword(passwordEncoder.encode(dto.getPassword()));
        return userMapper.toDto(userRepository.save(user));
    }

    @Override
    public UserDto update(Long id, UserDto dto) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("User not found: " + id));

        if (!user.getUsername().equals(dto.getUsername())
                && userRepository.existsByUsername(dto.getUsername())) {
            throw new BadRequestException("Username already exists");
        }
        if (!user.getEmail().equals(dto.getEmail())
                && userRepository.existsByEmail(dto.getEmail())) {
            throw new BadRequestException("Email already exists");
        }

        Role role = roleRepository.findByName(dto.getRoleName())
                .orElseThrow(() -> new BadRequestException("Role not found: " + dto.getRoleName()));

        user.setUsername(dto.getUsername());
        user.setEmail(dto.getEmail());
        user.setEnabled(dto.isEnabled());
        user.setRole(role);

        if (dto.getPassword() != null && !dto.getPassword().isBlank()) {
            user.setPassword(passwordEncoder.encode(dto.getPassword()));
        }

        return userMapper.toDto(userRepository.save(user));
    }

    @Override
    public void delete(Long id) {
        if (!userRepository.existsById(id)) {
            throw new NotFoundException("User not found: " + id);
        }
        userRepository.deleteById(id);
    }
}


