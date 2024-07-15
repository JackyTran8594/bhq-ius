package com.bhq.ius.domain.dto;

import lombok.Data;

import java.util.List;

@Data
public class ReportOneInfoDto {
    public Long driverSuccess;
    public Long driverFailed;
    public Long driverNotSubmitted;
    public Long profileSuccess;
    public Long profileFailed;
    public Long profileNotSubmitted;
    public Long enrollSuccess;
    public Long enrollFailed;
    public Long enrollNotSubmitted;
}
