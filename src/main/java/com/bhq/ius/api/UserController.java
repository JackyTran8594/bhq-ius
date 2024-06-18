package com.bhq.ius.api;

import com.bhq.ius.domain.dto.UserDto;
import com.bhq.ius.domain.dto.common.BaseResponseData;
import com.bhq.ius.domain.service.UserService;
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
@RequestMapping("/private/api/v1/user")
public class UserController {

    @Autowired
    private UserService service;

    @GetMapping()
    public ResponseEntity<BaseResponseData<UserDto>> search(@RequestParam(name = "pageNumber") int pageNumber,
                                                            @RequestParam(name = "pageSize") int pageSize,
                                                            @RequestParam(name = "search") Optional<String> search) {
        BaseResponseData<UserDto> response = new BaseResponseData<>();
        Pageable page = CommonUtil.pageRequest(new ArrayList<>(), pageNumber - 1, pageSize);
        Page<UserDto> listDTO = service.findBySearchParam(search, page);
        // response
        response.pagingData = listDTO;
        response.success();
        return new ResponseEntity<>(response, HttpStatus.OK);
    }


    @PostMapping("")
    public ResponseEntity<BaseResponseData<UserDto>> create(@RequestBody @Valid UserDto item) {
        BaseResponseData<UserDto> response = new BaseResponseData<>();
        UserDto dto = service.create(item);
        response.initData(dto);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PutMapping("/{id}")
    public ResponseEntity<BaseResponseData<UserDto>> update(@RequestBody @Valid UserDto item) {
        BaseResponseData<UserDto> response = new BaseResponseData<>();
        UserDto dto = service.update(item);
        response.initData(dto);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<BaseResponseData<UserDto>> getById(@PathVariable(value = "id") Long id) {
        BaseResponseData<UserDto> response = new BaseResponseData<>();
        UserDto dto = service.findById(id);
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
