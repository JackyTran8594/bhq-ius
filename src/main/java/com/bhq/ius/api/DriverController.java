package com.bhq.ius.api;

import com.bhq.ius.domain.dto.DriverDto;
import com.bhq.ius.domain.dto.common.BaseResponseData;
import com.bhq.ius.domain.service.DriverService;
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
@RequestMapping("/private/api/v1/driver")
public class DriverController {

    @Autowired
    private DriverService service;

    @GetMapping()
    public ResponseEntity<BaseResponseData<DriverDto>> search(@RequestParam(name = "pageNumber") int pageNumber,
                                                            @RequestParam(name = "pageSize") int pageSize,
                                                            @RequestParam(name = "search") Optional<String> search) {
        BaseResponseData<DriverDto> response = new BaseResponseData<>();
        Pageable page = CommonUtil.pageRequest(new ArrayList<>(), pageNumber - 1, pageSize);
        Page<DriverDto> listDTO = service.findBySearchParam(search, page);
        // response
        response.pagingData = listDTO;
        response.success();
        return new ResponseEntity<>(response, HttpStatus.OK);
    }


    @PostMapping("")
    public ResponseEntity<BaseResponseData<DriverDto>> create(@RequestBody @Valid DriverDto item) {
        BaseResponseData<DriverDto> response = new BaseResponseData<>();
        DriverDto dto = service.create(item);
        response.initData(dto);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PutMapping("/{id}")
    public ResponseEntity<BaseResponseData<DriverDto>> update(@RequestBody @Valid DriverDto item) {
        BaseResponseData<DriverDto> response = new BaseResponseData<>();
        DriverDto dto = service.update(item);
        response.initData(dto);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<BaseResponseData<DriverDto>> getById(@PathVariable(value = "id") Long id) {
        BaseResponseData<DriverDto> response = new BaseResponseData<>();
        DriverDto dto = service.findById(id);
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
