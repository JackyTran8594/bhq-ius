package com.bhq.ius.domain.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "document")
@AllArgsConstructor
@NoArgsConstructor
public class Document extends Auditable<String> {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "uuid")
    private String uuid;

    @Column(name = "SO_CMT", length = 50)
    private String soCMT;

    @Column(name = "SO_CMND_CU", length = 50)
    private String soCMTCu;

    @Column(name = "MA_GIAY_TO")
    private String maGiayTo;

    @Column(name = "TEN_GIAY_TO", columnDefinition = "NVARCHAR(500)")
    private String tenGiayTo;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "driver_uuid", referencedColumnName = "uuid")
    private Driver driver;

}