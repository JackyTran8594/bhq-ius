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
@Table(name = "profile")
@AllArgsConstructor
@NoArgsConstructor
public class Profile extends Auditable<String> {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "uuid")
    private String uuid;

    @Column(name = "SO_CMT", length = 50)
    private String soCMT;

    @Column(name = "SO_CMND_CU", length = 50)
    private String soCMTCu;

    @Column(name = "SO_HO_SO")
    private String soHoSo;

    @Column(name = "MA_DV_NHAN_HOSO")
    private String maDVNhanHoSo;

    @Column(name = "TEN_DV_NHAN_HOSO", columnDefinition = "NVARCHAR(500)")
    private String tenDVNhanHoSo;

    @Column(name = "NGAY_NHAN_HOSO")
    private String ngayNhanHoSo;

    @Column(name = "NGUOI_NHAN_HOSO", columnDefinition = "NVARCHAR(500)")
    private String nguoiNhanHoSo;

    @Column(name = "MA_LOAI_HOSO")
    private String maLoaiHoSo;

    @Column(name = "TEN_LOAI_HOSO", columnDefinition = "NVARCHAR(500)")
    private String tenLoaiHoSo;

    @Column(name = "ANH_CHAN_DUNG", columnDefinition = "TEXT")
    private String anhChanDung;

    @Column(name = "CHAT_LUONG_ANH")
    private String chatLuongAnh;

    @Column(name = "NGAY_THU_NHAN_ANH")
    private LocalDate ngayThuNhanAnh;

    @Column(name = "NGUOI_THU_NHAN_ANH", columnDefinition = "NVARCHAR(200)")
    private String nguoiThuNhanAnh;

    @Column(name = "SO_GPLX_DA_CO")
    private String soGPLXDaCo;

    @Column(name = "HANG_GPLX_DA_CO")
    private String hangGPLXDaCo;

    @Column(name = "DV_CAP_GPLX_DACO")
    private String dvCapGPLXDaCo;

    @Column(name = "TEN_DV_CAP_GPLX_DACO")
    private String tenDVCapGPLXDaCo;

    @Column(name = "NOI_CAP_GPLX_DACO")
    private String noiCapGPLXDaCo;

    @Column(name = "NGAY_CAP_GPLX_DACO")
    private LocalDate ngayCapGPLXDaCo;

    @Column(name = "NGAY_HH_GPLX_DACO")
    private LocalDate ngayHHGPLXDaCo;

    @Column(name = "NGAY_TT_GPLX_DACO")
    private LocalDate ngayTTGPLXDaCo;

    @Column(name = "MA_NOI_HOC_LAIXE")
    private String maNoiHocLaiXe;

    @Column(name = "TEN_NOI_HOC_LAIXE")
    private String tenNoiHocLaiXe;

    @Column(name = "NAM_HOC_LAIXE")
    private String namHocLaiXe;

    @Column(name = "SO_NAM_LAIXE")
    private String soNamLaiXe;

    @Column(name = "SO_KM_ANTOAN")
    private String soKMAnToan;

    @Column(name = "GIAY_CNSK")
    private String giayCNSK;

    @Column(name = "HINH_THUC_CAP")
    private String hinhThucCap;

    @Column(name = "HANG_GPLX")
    private String hangGPLX;

    @Column(name = "HANG_DAOTAO")
    private String hangDaoTao;

    @Column(name = "CHON_IN_GPLX")
    private String chonInGPLX;

    @Column(name = "STATE")
    @Enumerated(EnumType.STRING)
    private RecordState state;

    @Column(name = "ERROR", length = 1000)
    private String error;

    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "driver_uuid", referencedColumnName = "uuid")
    private Driver driver;

}