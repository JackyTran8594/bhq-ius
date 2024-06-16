package com.thanhbinh.dms.domain.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "device")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Device extends Auditable<String> implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "imei_number")
    private Long imeiNumber;

    @Column(name = "serial_number")
    private String serialNumber;

    @Column(name = "display_name", length = 50)
    private String displayName;

    @ManyToOne(cascade = CascadeType.ALL,  fetch = FetchType.LAZY)
    @JoinColumn(name = "device_type_id", referencedColumnName = "id")
    private DeviceType deviceType;

    @ManyToOne(cascade = CascadeType.ALL,  fetch = FetchType.LAZY)
    @JoinColumn(name = "device_group_id", referencedColumnName = "id")
    private DeviceGroup deviceGroup;

    @Column(name = "description", columnDefinition = "NVARCHAR(500)")
    private String description;

    @Column(name = "registration_time")
    private LocalDateTime registrationTime;

    @Column(name = "made_in", columnDefinition = "NVARCHAR(200)")
    private String madeIn;

    @Column(name = "mac_address")
    private String macAddress;

    @Column(name = "firmware_version")
    private String firmwareVersion;

    @Column(name = "firmware_update_time")
    private LocalDateTime firmwareUpdateTime;

    @Column(name = "ip_address_current")
    private String ipAddressCurrent;

    @Column(name = "status_code_state")
    private String statusCodeState;

    @Column(name = "last_battery_level", precision = 11, scale = 5)
    private BigDecimal lastBatteryLevel;

    @Column(name = "last_battery_vols", precision = 11, scale = 5)
    private BigDecimal lastBatteryVols;

    @Column(name = "last_valid_latitude", length = 50)
    private String lastValidLatitude;

    @Column(name = "last_valid_longitude", length = 50)
    private String lastValidLongitude;

    @Column(name = "last_gps_timestamp")
    private Long lastGpsTimestamp;

    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "sim_id", referencedColumnName = "id")
    private Sim sim;

    @ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "org_id", referencedColumnName = "id")
    private Organization organization;

    @ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "batch_number", referencedColumnName = "batch_number")
    private ProductBatch productBatch;

    @Column(name = "note", columnDefinition = "VARCHAR(1000)")
    private String note;

}
