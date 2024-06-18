package com.bhq.ius.domain.service;

import com.bhq.ius.domain.dto.UserDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

public interface UserService {
    Page<UserDto> findBySearchParam(Optional<String> search, Pageable page);
    UserDto create(UserDto dto);
    UserDto update(UserDto dto);
    void deleteById(Long id);
    void deleteByListId(List<Long> listId);
    UserDto findById(Long id);


}
