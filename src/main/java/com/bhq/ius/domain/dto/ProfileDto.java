package com.bhq.ius.domain.dto;

import com.bhq.ius.domain.dto.common.BaseDto;
import lombok.*;
import org.springframework.cglib.core.Local;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Set;

@Data
public class ProfileDto extends BaseDto<String> {
    private Long id;

    private String soCMT;

    private String soCMTCu;

    private String soHoSo;

    private String maDVNhanHoSo;

    private String tenDVNhanHoSo;

    private LocalDate ngayNhanHoSo;

    private String nguoiNhanHoSo;

    private String maLoaiHoSo;

    private String tenLoaiHoSo;

    private String anhChanDung;

    private String chatLuongAnh;

    private LocalDate ngayThuNhanAnh;

    private String nguoiThuNhanAnh;

    private String soGPLXDaCo;

    private String hangGPLXDaCo;

    private String dvCapGPLXDaCo;

    private String tenDVCapGPLXDaCo;

    private String noiCapGPLXDaCo;

    private LocalDate ngayCapGPLXDaCo;

    private LocalDate ngayHHGPLXDaCo;

    private LocalDate ngayTTGPLXDaCo;

    private String maNoiHocLaiXe;

    private String tenNoiHocLaiXe;

    private String namHocLaiXe;

    private String soNamLaiXe;

    private String soKMAnToan;

    private String giayCNSK;

    private String hinhThucCap;

    private String hangGPLX;

    private String hangDaoTao;

    private String chonInGPLX;

    private String driver_uuid;

}