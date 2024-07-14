package com.bhq.ius.domain.entity;

import com.bhq.ius.constant.RecordState;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Set;

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

    @Column(name = "uuid")
    private String uuid;

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
    private LocalDate ngayBCI;

    @Column(name = "LUU_LUONG")
    private String luuLuong;

    @Column(name = "SO_HOC_SINH")
    private String soHocSinh;

    @Column(name = "NGAY_KHAI_GIANG")
    private LocalDate ngayKhaiGiang;

    @Column(name = "NGAY_BE_GIANG")
    private LocalDate ngayBeGiang;

    @Column(name = "SO_QD_KG")
    private String soQDKG;

    @Column(name = "NGAY_QD_KG")
    private LocalDate ngayQDKG;

    @Column(name = "NGAY_SAT_HACH")
    private LocalDate ngaySatHach;

    @Column(name = "THOI_GIAN_DT")
    private String thoiGianDT;

    @Column(name = "STATE")
    @Enumerated(EnumType.STRING)
    private RecordState state;

    @Column(name = "ERROR", length = 1000)
    private String error;

    @Column(name = "ID_COURSE_MOODLE")
    private String idCourseMoodle;

    @Column(name = "SHORTNAME_COURSE_MOODLE")
    private String shortNameCourseMoodle;

    @OneToMany(mappedBy = "course", cascade = CascadeType.ALL)
    private Set<Driver> drivers;

}