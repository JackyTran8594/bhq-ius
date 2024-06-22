package com.bhq.ius.domain.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@Table(name = "course")
@AllArgsConstructor
@NoArgsConstructor
public class Course extends Auditable<String> {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "SO_CMT", length = 50)
    private String soCMT;

    @Column(name = "SO_CMND_CU", length = 50)
    private String soCMTCu;

    @Column(name = "MA_BCI")
    private String maBCI;

    @Column(name = "MA_SO_GTVT")
    private String maSoGTVT;

    @Column(name = "TEN_SO_GTVT")
    private String tenSoGTVT;

    @Column(name = "MA_CSDT")
    private String maCSDT;

    @Column(name = "TEN_CSDT")
    private String tenCSDT;

    @Column(name = "MA_KHOA_HOC")
    private String maKhoaHoc;

    @Column(name = "TEN_KHOA_HOC")
    private String tenKhoaHoc;

    @Column(name = "MA_HANG_DAO_TAO")
    private String maHangDaoTao;

    @Column(name = "HANG_GPLX")
    private String hangGPLX;

    @Column(name = "SO_BCI")
    private String soBCI;

    @Column(name = "NGAY_BCI")
    private LocalDateTime ngayBCI;

    @Column(name = "LUU_LUONG")
    private String luuLuong;

    @Column(name = "SO_HOC_SINH")
    private String soHocSinh;

    @Column(name = "NGAY_KHAI_GIANG")
    private LocalDateTime ngayKhaiGiang;

    @Column(name = "NGAY_BE_GIANG")
    private LocalDateTime ngayBeGiang;

    @Column(name = "SO_QD_KG")
    private String soQDKG;

    @Column(name = "NGAY_QD_KG")
    private LocalDateTime ngayQDKG;

    @Column(name = "NGAY_SAT_HACH")
    private LocalDateTime ngaySatHach;

    @Column(name = "THOI_GIAN_DT")
    private LocalDateTime thoiGianDT;

}