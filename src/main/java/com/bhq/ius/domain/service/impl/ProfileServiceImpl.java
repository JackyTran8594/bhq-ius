package com.bhq.ius.domain.service.impl;

import com.bhq.ius.constant.RecordState;
import com.bhq.ius.constant.XmlElement;
import com.bhq.ius.domain.dto.*;
import com.bhq.ius.domain.entity.Profile;
import com.bhq.ius.domain.repository.ProfileRepository;
import com.bhq.ius.domain.service.ProfileService;
import com.bhq.ius.domain.specification.GenericSpecificationBuilder;
import com.bhq.ius.domain.specification.criteria.SearchCriteria;
import com.bhq.ius.domain.specification.criteria.SearchOperation;
import com.bhq.ius.utils.DataUtil;
import com.bhq.ius.utils.XmlUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.List;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
@Slf4j
public class ProfileServiceImpl implements ProfileService {

    @Autowired
    private ProfileRepository repository;

    @Override
    public Page<ProfileDto> findBySearchParam(Optional<String> search, Pageable page) {
        GenericSpecificationBuilder<Profile> builder = new GenericSpecificationBuilder<>();
        // check chuỗi để tách các param search
        if (search.isPresent()) {
            Pattern pattern = Pattern.compile("(\\w+?)(\\.)(:|<|>|(\\w+?))(\\.)(\\w+?),", Pattern.UNICODE_CHARACTER_CLASS);
            Matcher matcher = pattern.matcher(search.get() + ",");
            while (matcher.find()) {
                builder.with(new SearchCriteria(matcher.group(1), matcher.group(3), matcher.group(6)));
            }
        }
        /* default search with status not submitted*/
        builder.with(new SearchCriteria("state", SearchOperation.NOT_EQUAL.getName(), RecordState.SUBMITTED.name()));
        builder.with(new SearchCriteria("state", SearchOperation.NUL.getName(),""));
        // specification
        builder.setClazz(Profile.class);
        Specification<Profile> spec = builder.build();
        Page<ProfileDto> listDTO = repository.findAll(spec, page).map(entity -> {
            ProfileDto dto = new ProfileDto();
            BeanUtils.copyProperties(entity, dto);
            return dto;
        });
        return listDTO;
    }

    @Override
    public ProfileDto create(ProfileDto dto) {
        Profile entity = new Profile();
        BeanUtils.copyProperties(dto, entity);
        repository.save(entity);
        return dto;
    }

    @Override
    public ProfileDto update(ProfileDto dto) {
        Optional<Profile> entity = repository.findById(dto.getId());
        if (entity.isPresent()) {
            BeanUtils.copyProperties(dto, entity);
            repository.save(entity.get());
        }
        return dto;
    }

    @Override
    public void deleteById(Long id) {
        repository.deleteById(id);
    }

    @Override
    public void deleteByListId(List<Long> listId) {
        repository.deleteAllById(listId);
    }

    @Override
    public ProfileDto findById(Long id) {
        ProfileDto dto = new ProfileDto();
        Optional<Profile> entity = repository.findById(id);
        entity.ifPresent(value -> BeanUtils.copyProperties(value, dto));
        return dto;
    }
}
