package com.eqca.repository.dto.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.OffsetDateTime;

@NoArgsConstructor
@Data
public class MdDevice {
	String eqCode;
	String deviceCode;
	String deviceName;
	String deviceType;
	String funcType;
	String ovEqCode;
	String ovDeviceCode;
	String motionType;
	String deviceAddrCode;
	String statusCode;
	String statusDesc;
	String useYn;
	OffsetDateTime createDate;
	OffsetDateTime updateDate;
	String cfgCode;
	String floorInfo;
	String ctrlStatus;
	String motionStatus;
	String workId;
	Integer destCode;

	@Builder
	public MdDevice( String eqCode, String deviceCode, String deviceName, String deviceType, String funcType,
			String ovEqCode, String ovDeviceCode, String motionType, String deviceAddrCode, String statusCode,
			String statusDesc, String useYn, OffsetDateTime createDate, OffsetDateTime updateDate, String cfgCode,
			String floorInfo, String ctrlStatus, String motionStatus, String workId, Integer destCode) {
		this.eqCode = eqCode;
		this.deviceCode = deviceCode;
		this.deviceName = deviceName;
		this.deviceType = deviceType;
		this.funcType = funcType;
		this.ovEqCode = ovEqCode;
		this.ovDeviceCode = ovDeviceCode;
		this.motionType = motionType;
		this.deviceAddrCode = deviceAddrCode;
		this.statusCode = statusCode;
		this.statusDesc = statusDesc;
		this.useYn = useYn;
		this.createDate = createDate;
		this.updateDate = updateDate;
		this.cfgCode = cfgCode;
		this.floorInfo = floorInfo;
		this.ctrlStatus = ctrlStatus;
		this.motionStatus = motionStatus;
		this.workId = workId;
		this.destCode = destCode;
	}
}
