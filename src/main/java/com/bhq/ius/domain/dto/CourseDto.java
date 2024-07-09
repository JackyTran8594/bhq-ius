package com.bhq.ius.domain.dto;

import com.bhq.ius.constant.RecordState;
import com.bhq.ius.domain.dto.common.BaseDto;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
public class CourseDto extends BaseDto<String> {
    private Long id;

    private String soCMT;

    private String soCMTCu;

    private String maBCI;

    private String maSoGTVT;

    private String tenSoGTVT;

    private String maCSDT;

    private String tenCSDT;

    private String maKhoaHoc;

    private String tenKhoaHoc;

    private String maHangDaoTao;

    private String hangGPLX;

    private String soBCI;

    private LocalDate ngayBCI;

    private String luuLuong;

    private String soHocSinh;

    private LocalDate ngayKhaiGiang;

    private LocalDate ngayBeGiang;

    private String maGiayTo;

    private String soQDKG;

    private LocalDate ngayQDKG;

    private LocalDate ngaySatHach;

    private String thoiGianDT;
    private String uuid;
    private String error;

    private RecordState state;

}