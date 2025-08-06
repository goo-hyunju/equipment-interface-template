package com.eqca.repository.dto.entity;

import java.time.OffsetDateTime;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class MdDeviceCtrl {
	String cfgCode;
	String cfgName;
	String cfgModel;
	String ctrlIp;
	int ctrlPort;
	String ctrlRscom;
	String infType;
	String infModel;
	String commCode;
	String commDesc;
	String useYn;
	OffsetDateTime createDate;
	OffsetDateTime updateDate;

	String dataType;
}
