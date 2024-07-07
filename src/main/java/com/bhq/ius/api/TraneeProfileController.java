package com.bhq.ius.api;

import com.bhq.ius.domain.dto.ProfileDto;
import com.bhq.ius.domain.dto.common.BaseResponseData;
import com.bhq.ius.domain.service.ProfileService;
import com.bhq.ius.utils.CommonUtil;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/private/api/v1/profile")
public class TraneeProfileController {

    @Autowired
    private ProfileService service;

    @GetMapping()
    public ResponseEntity<BaseResponseData<ProfileDto>> search(@RequestParam(name = "pageNumber") int pageNumber,
                                                            @RequestParam(name = "pageSize") int pageSize,
                                                            @RequestParam(name = "search") Optional<String> search) {
        BaseResponseData<ProfileDto> response = new BaseResponseData<>();
        Pageable page = CommonUtil.pageRequest(new ArrayList<>(), pageNumber - 1, pageSize);
        Page<ProfileDto> listDTO = service.findBySearchParam(search, page);
        // response
        response.pagingData = listDTO;
        response.success();
        return new ResponseEntity<>(response, HttpStatus.OK);
    }


    @PostMapping("")
    public ResponseEntity<BaseResponseData<ProfileDto>> create(@RequestBody @Valid ProfileDto item) {
        BaseResponseData<ProfileDto> response = new BaseResponseData<>();
        ProfileDto dto = service.create(item);
        response.initData(dto);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PutMapping("/{id}")
    public ResponseEntity<BaseResponseData<ProfileDto>> update(@RequestBody @Valid ProfileDto item) {
        BaseResponseData<ProfileDto> response = new BaseResponseData<>();
        ProfileDto dto = service.update(item);
        response.initData(dto);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<BaseResponseData<ProfileDto>> getById(@PathVariable(value = "id") Long id) {
        BaseResponseData<ProfileDto> response = new BaseResponseData<>();
        ProfileDto dto = service.findById(id);
        response.initData(dto);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<BaseResponseData<Integer>> deleteById(@PathVariable(value = "id") Long id) {
        BaseResponseData<Integer> response = new BaseResponseData<>();
        service.deleteById(id);
        response.initData(1);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PostMapping("/deleteByListId")
    public ResponseEntity<BaseResponseData<Integer>> deleteByListId(@RequestBody List<Long> listId) {
        BaseResponseData<Integer> response = new BaseResponseData<>();
        service.deleteByListId(listId);
        response.success();
        return new ResponseEntity<>(response, HttpStatus.OK);

    }


}
