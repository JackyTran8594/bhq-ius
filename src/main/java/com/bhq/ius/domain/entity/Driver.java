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
@Table(name = "driver")
@AllArgsConstructor
@NoArgsConstructor
public class Driver extends Auditable<String> {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "SO_TT", length = 100)
    private String soTT;

    @Column(name = "MA_DK")
    private String maDK;

    @Column(name = "HO_TEN_DEM", columnDefinition = "NVARCHAR(200)")
    private String hoTenDem;

    @Column(name = "TEN", columnDefinition = "NVARCHAR(100)")
    private String ten;

    @Column(name = "HO_VA_TEN", columnDefinition = "NVARCHAR(500)")
    private String hoVaTen;

    @Column(name = "NGAY_SINH")
    private LocalDateTime ngaySinh;

    @Column(name = "MA_QUOC_TICH")
    private String maQuocTich;

    @Column(name = "TEN_QUOC_TICH", columnDefinition = "NVARCHAR(200)")
    private String tenQuocTich;

    @Column(name = "NOI_TT", columnDefinition = "NVARCHAR(500)")
    private String noiTT;

    @Column(name = "NOI_TT_MA_DVHC", columnDefinition = "NVARCHAR(500)")
    private String noiTTMaDvhc;

    @Column(name = "NOI_TT_MA_DVQL", columnDefinition = "NVARCHAR(500)")
    private String noiTTMaDvql;

    @Column(name = "NOI_CT", columnDefinition = "NVARCHAR(500)")
    private String noiCT;

    @Column(name = "NOI_CT_MA_DVHC", columnDefinition = "NVARCHAR(500)")
    private String noiCTMaDvhc;

    @Column(name = "NOI_CT_MA_DVQL", columnDefinition = "NVARCHAR(500)")
    private String noiCTMaDvql;

    @Column(name = "SO_CMT", length = 50)
    private String soCMT;

    @Column(name = "NGAY_CAP_CMT")
    private LocalDateTime ngayCapCMT;

    @Column(name = "NOI_CAP_CMT")
    private String noiCAPCMT;

    @Column(name = "GIOI_TINH")
    private String gioiTinh;

    @Column(name = "HO_VA_TEN_IN", columnDefinition = "NVARCHAR(500)")
    private String hoVaTenIn;

    @Column(name = "SO_CMND_CU")
    private String soCMTCu;

    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinColumn(name = "profile_id", referencedColumnName = "id")
    private Profile profile;

}