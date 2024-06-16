package com.thanhbinh.dms.domain.service.impl;

import com.thanhbinh.dms.domain.dto.DeviceDto;
import com.thanhbinh.dms.domain.entity.Device;
import com.thanhbinh.dms.domain.repository.DeviceRepository;
import com.thanhbinh.dms.domain.service.DeviceService;
import com.thanhbinh.dms.domain.specification.GenericSpecificationBuilder;
import com.thanhbinh.dms.domain.specification.criteria.SearchCriteria;
import com.thanhbinh.dms.utils.DataUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
@Slf4j
public class DeviceServiceImpl implements DeviceService {

    @Autowired
    private DeviceRepository deviceRepository;

    @Override
    public Page<DeviceDto> findBySearchParam(Optional<String> search, Pageable page) {
        GenericSpecificationBuilder<Device> builder = new GenericSpecificationBuilder<>();
        // check chuỗi để tách các param search
        if (DataUtil.notNull(search)) {
            Pattern pattern = Pattern.compile("(\\w+?)(\\.)(:|<|>|(\\w+?))(\\.)(\\w+?),", Pattern.UNICODE_CHARACTER_CLASS);
            Matcher matcher = pattern.matcher(search + ",");
            while (matcher.find()) {
                builder.with(new SearchCriteria(matcher.group(1), matcher.group(3), matcher.group(6)));
            }
        }
        // specification
        builder.setClazz(Device.class);
        Specification<Device> spec = builder.build();
        Page<DeviceDto> listDTO = deviceRepository.findAll(spec, page).map(entity -> {
            DeviceDto dto = new DeviceDto();
            BeanUtils.copyProperties(entity, dto);
            return dto;
        });
        return listDTO;
    }

    @Override
    public DeviceDto createDevice(DeviceDto deviceDTO) {
        Device device = new Device();
        BeanUtils.copyProperties(deviceDTO, device);
        deviceRepository.save(device);
        return deviceDTO;
    }

    @Override
    public DeviceDto updateDevice(DeviceDto deviceDTO) {
        Optional<Device> device = deviceRepository.findById(deviceDTO.getId());
        if(device.isPresent()) {
            BeanUtils.copyProperties(deviceDTO, device);
            deviceRepository.save(device.get());
        }
        return deviceDTO;
    }

    @Override
    public void deleteById(Long deviceId) {
        deviceRepository.deleteById(deviceId);
    }

    @Override
    public void deleteByListId(List<Long> listDeviceId) {
        deviceRepository.deleteAllById(listDeviceId);
    }

    @Override
    public void ImportBatchDevice(List<DeviceDto> listDeviceDto) {
        for (DeviceDto dto: listDeviceDto) {
            createDevice(dto);
        }
    }

    @Override
    public DeviceDto findById(Long id) {
        DeviceDto dto = new DeviceDto();
        Optional<Device> device = deviceRepository.findById(id);
        device.ifPresent(value -> BeanUtils.copyProperties(value, dto));
        return dto;
    }
}
