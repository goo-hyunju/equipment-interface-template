package com.eqca.controller.upper;

import com.eqca.common.utils.LogUtils;
import com.eqca.controller.base.event.EqInjectionEventPublish;
import com.eqca.controller.lower.event.EqControlEventPublish;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.eqca.repository.dto.domain.api.APIResponse;
import com.eqca.repository.dto.domain.eqca.eqcaCommunicationRecord;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * Orca Interface API Server
 * @since 1.0
 * @author  kth
 * @version 1.0
 */
@Slf4j
@Tag(name = "wcs - Orca", description = "Orca Adapter 표준 인터페이스")
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/adapter-inf")
public class OrcaApiServerController {

	private final EqControlEventPublish eqControlEventPublish;

	/**
	 * HeartBeat
     */
	@PostMapping("/heartbeat")
    public APIResponse<String> receiveHeartbeat(@RequestBody OrcaCommunicationRecord.HeartBeat bodyData){
		try {
			
			String eqCode = bodyData.eqCode();
			
			log.info(LogUtils.basic("", "HeartBeat 수신"));
			
			return APIResponse.success();
		}catch(Exception e) {
			return APIResponse.of(HttpStatus.INTERNAL_SERVER_ERROR.value(), e.getMessage());
		}
	}

	@PostMapping("/test")
	public APIResponse<String> plcdataSet(){
		try {

			eqControlEventPublish.publish("TEST", "TEST");
			return APIResponse.success();

		}catch(Exception e) {
			return APIResponse.of(HttpStatus.INTERNAL_SERVER_ERROR.value(), e.getMessage());
		}
	}

}
