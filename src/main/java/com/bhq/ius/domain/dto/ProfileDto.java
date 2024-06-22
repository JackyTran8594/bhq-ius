package com.bhq.ius.domain.dto;

import com.bhq.ius.domain.dto.common.BaseDto;
import lombok.*;

import java.util.Set;

@Data
public class ProfileDto extends BaseDto<String> {
    private Long id;

    private String soCMT;

    private String soCMTCu;

    private String soHoSo;

    private String maDVNhanHoSo;

    private String tenDVNhanHoSo;

    private String ngayNhanHoSo;

    private String nguoiNhanHoSo;

    private String maLoaiHoSo;

    private String tenLoaiHoSo;

    private String anhChanDung;

    private String chatLuongAnh;

    private String ngayThuNhanAnh;

    private String nguoiThuNhanAnh;

    private String soGPLXDaCo;

    private String hangGPLXDaCo;

    private String dvCapGPLXDaCo;

    private String tenDVCapGPLXDaCo;

    private String noiCapGPLXDaCo;

    private String ngayCapGPLXDaCo;

    private String ngayHHGPLXDaCo;

    private String ngayTTGPLXDaCo;

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

}