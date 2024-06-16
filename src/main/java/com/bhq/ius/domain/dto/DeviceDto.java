package com.thanhbinh.dms.domain.dto;

import com.thanhbinh.dms.domain.dto.common.BaseDto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@EqualsAndHashCode(callSuper = false)
@AllArgsConstructor
@NoArgsConstructor
public class DeviceDto extends BaseDto<String> {
    private Long id;

    private Long imeiNumber;

    private String serialNumber;

    private String displayName;

    private Long deviceTypeId;

    private Long deviceGroupId;

    private String description;

    private LocalDateTime registrationTime;

    private String madeIn;

    private String macAddress;

    private String firmwareVersion;

    private LocalDateTime firmwareUpdateTime;

    private String ipAddressCurrent;

    private String statusCodeState;

    private BigDecimal lastBatteryLevel;

    private BigDecimal lastBatteryVols;

    private String lastValidLatitude;

    private String lastValidLongitude;

    private Long lastGpsTimestamp;

    private Long simId;

    private Long orgId;

    private Long bacthNumber;

    private String note;
}
