package com.eqca.repository.dto.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class MdDeviceCtrlMelsec {
	String cfgCode;
	String memorytype;
	int readAddr;
	int readCount;
	String dataType;
}
