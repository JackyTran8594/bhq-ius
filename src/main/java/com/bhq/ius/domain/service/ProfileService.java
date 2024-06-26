package com.bhq.ius.domain.service;

import com.bhq.ius.domain.dto.DriverDto;
import com.bhq.ius.domain.dto.ProfileDto;
import com.bhq.ius.domain.dto.UserDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Optional;

public interface ProfileService {
    Page<ProfileDto> findBySearchParam(Optional<String> search, Pageable page);
    ProfileDto create(ProfileDto dto);
    ProfileDto update(ProfileDto dto);
    void deleteById(Long id);
    void deleteByListId(List<Long> listId);
    ProfileDto findById(Long id);







}
