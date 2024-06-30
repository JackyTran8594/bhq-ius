package com.bhq.ius.domain.entity;

import com.bhq.ius.constant.RecordState;
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

    @Column(name = "STATE")
    @Enumerated(EnumType.STRING)
    private RecordState state;

    @Column(name = "ERROR", length = 1000)
    private String error;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "driver_uuid", referencedColumnName = "uuid")
    private Driver driver;

}