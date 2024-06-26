package com.bhq.ius.api;

import com.bhq.ius.domain.dto.CourseDto;
import com.bhq.ius.domain.dto.common.BaseResponseData;
import com.bhq.ius.domain.service.CourseService;
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
@RequestMapping("/private/api/v1/Course")
public class CourseController {

    @Autowired
    private CourseService service;

    @GetMapping()
    public ResponseEntity<BaseResponseData<CourseDto>> search(@RequestParam(name = "pageNumber") int pageNumber,
                                                            @RequestParam(name = "pageSize") int pageSize,
                                                            @RequestParam(name = "search") Optional<String> search) {
        BaseResponseData<CourseDto> response = new BaseResponseData<>();
        Pageable page = CommonUtil.pageRequest(new ArrayList<>(), pageNumber - 1, pageSize);
        Page<CourseDto> listDTO = service.findBySearchParam(search, page);
        // response
        response.pagingData = listDTO;
        response.success();
        return new ResponseEntity<>(response, HttpStatus.OK);
    }


    @PostMapping("")
    public ResponseEntity<BaseResponseData<CourseDto>> create(@RequestBody @Valid CourseDto item) {
        BaseResponseData<CourseDto> response = new BaseResponseData<>();
        CourseDto dto = service.create(item);
        response.initData(dto);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PutMapping("/{id}")
    public ResponseEntity<BaseResponseData<CourseDto>> update(@RequestBody @Valid CourseDto item) {
        BaseResponseData<CourseDto> response = new BaseResponseData<>();
        CourseDto dto = service.update(item);
        response.initData(dto);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<BaseResponseData<CourseDto>> getById(@PathVariable(value = "id") Long id) {
        BaseResponseData<CourseDto> response = new BaseResponseData<>();
        CourseDto dto = service.findById(id);
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
