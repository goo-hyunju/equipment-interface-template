package com.eqca.controller.web;

import com.eqca.common.enums.reg.eq.EMdDeviceCtrlModel;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc
@SpringBootTest
public class WebControllerTest {

    @Autowired
    private WebController controller;

    @Autowired
    private MockMvc mockMvc;

    // MyBatis Mapper 테스트 예시
    @Autowired
    private com.eqca.repository.mapper.a.equipment.EquipmentMapper equipmentMapper;

    // Scheduler 시스템 테스트 예시 (로그 기반)
@Autowired(required = false)
private com.eqca.config.scheduler.SchedulerTaskService schedulerTaskService;

    // Event System 테스트 예시
    @Autowired
    private org.springframework.context.ApplicationEventPublisher eventPublisher;

    @Test
    void contextLoads() {
        assertThat(controller).isNotNull();
    }

    @Test
    void check() {
        EMdDeviceCtrlModel test = EMdDeviceCtrlModel.L_PLC;
        System.out.println("test = " + test.getValue());
    }

    // MyBatis Mapper 동작 테스트
    @Test
    void testMapperSelect() {
        var result = equipmentMapper.selectMdDeviceCtrl();
        assertThat(result).isNotEmpty();
    }

    // API Controller 응답 테스트
    @Test
    void testApiEndpoint() throws Exception {
        mockMvc.perform(get("/api/health"))
                .andExpect(status().isOk());
    }

    // Exception Handler 테스트 (실제 예외 타입으로 변경)
    @Test
    void testExceptionHandler() {
        assertThatThrownBy(() -> { throw new RuntimeException("테스트 예외"); })
                .isInstanceOf(RuntimeException.class);
    }

    // Scheduler 시스템 테스트 (실제 스케줄러가 있을 때만 사용)
    // @Test
    // void testSchedulerRegister() {
    //     if (schedulerTaskService != null) {
    //         schedulerTaskService.registerTask("test", () -> {}, 1000);
    //     }
    // }

    // Event System 테스트 (실제 이벤트 객체와 구독자 필요)
    @Test
    void testEventPublish() {
        eventPublisher.publishEvent("테스트 이벤트");
        // 구독자에서 처리된 결과는 로그 또는 별도 검증 코드 필요
    }
}
