package com.bhq.ius.domain.dto;

import com.bhq.ius.domain.dto.common.BaseDto;
import lombok.*;

@Data
public class DriverDto extends BaseDto<String> {
    private Long id;

    private String soTT;

    private String maDK;

    private String hoTenDem;

    private String ten;

    private String hoVaTen;

    private String ngaySinh;

    private String maQuocTich;

    private String tenQuocTich;

    private String noiTT;

    private String noiTTMaDvhc;

    private String noiTTMaDvql;

    private String noiCT;

    private String noiCTMaDvhc;

    private String noiCTMaDvql;

    private String soCMT;

    private String ngayCapCMT;

    private String noiCapCMT;

    private String gioiTinh;

    private String hoVaTenIn;

    private String soCMTCu;

}