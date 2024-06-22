package com.bhq.ius.domain.dto;

import com.bhq.ius.domain.dto.common.BaseDto;
import lombok.*;

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

    private String ngayBCI;

    private String luuLuong;

    private String soHocSinh;

    private String ngayKhaiGiang;

    private String ngayBeGiang;

    private String maGiayTo;

    private String soQDKG;

    private String ngayQDKG;

    private String ngaySatHach;

    private String thoiGianDT;

}