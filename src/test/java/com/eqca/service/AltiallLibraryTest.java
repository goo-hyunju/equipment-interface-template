package com.eqca.service;

import com.altiall.common.code.OrcaCommonCode;
import com.altiall.convert.Converter;
import com.altiall.plc.mitsubishi.format.MelsecQFormat;
import com.altiall.plc.mitsubishi.format.MelsecSeries;
import com.altiall.plc.mitsubishi.protocol.qmelsec.q3e.MelsecQ3E;
import com.altiall.socket.java.JavaSocketClient;
import com.altiall.socket.java.JavaSocketServer;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;

import java.util.Collections;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Slf4j
public class AltiallLibraryTest {

    @Test
    void 공통코드(){

        // EquipmentProperties
        Map<String, OrcaCommonCode.EquipmentProperties> eqPropertieValues =
                Collections.unmodifiableMap(Stream.of(OrcaCommonCode.EquipmentProperties.values())
                        .collect(Collectors.toMap(OrcaCommonCode.EquipmentProperties::getCode, Function.identity())));

        log.info("EquipmentProperties code=value: {}", eqPropertieValues);

        // DeviceType
        Map<String, OrcaCommonCode.DeviceType> deviceTypeValues =
                Collections.unmodifiableMap(Stream.of(OrcaCommonCode.DeviceType.values())
                        .collect(Collectors.toMap(OrcaCommonCode.DeviceType::getCode, Function.identity())));

        log.info("DeviceType code=value : {}", deviceTypeValues);
    }

    /**
     * TODO : 한글 깨짐 체크
     * @throws Exception
     */
    @Test
    void 컨버터() throws Exception {
        log.info("LeftPad : {}", Converter.leftPad("Hello", 10, '0'));

        log.info("Reverse : {}", Converter.reverse("Hello", 0, 5));

        try{
            log.info("Reverse Error : {}", Converter.reverse("Hello", 0, 6));
        }catch (Exception e){
            log.error("Reverse Error : {}", e.getMessage());
        }
    }

    @Test
    void 멜섹Q3E포맷(){

        try {
            MelsecQ3E melsecQ3EAscii = new MelsecQ3E(MelsecSeries.CommunicateFormat.ASCII);
            log.info("readBit ASCII : {}", melsecQ3EAscii.readBit(MelsecQFormat.DeviceCode.M, 1000, 10));
            log.info("writeBit ASCII : {}", melsecQ3EAscii.writeBit(MelsecQFormat.DeviceCode.M, 1000, new int[]{1, 1, 0}));
            log.info("readWord ASCII : {}", melsecQ3EAscii.readWord(MelsecQFormat.DeviceCode.W, 1000, 10));
            log.info("writeWord ASCII : {}", melsecQ3EAscii.writeWord(MelsecQFormat.DeviceCode.W, 1000, new int[]{1, 2, 3, 4, 5, 6, 7, 8, 9, 10}));

            MelsecQ3E melsecQ3EBinary = new MelsecQ3E(MelsecSeries.CommunicateFormat.BINARY);
            log.info("readBit BINARY : {}", melsecQ3EBinary.readBit(MelsecQFormat.DeviceCode.M, 1000, 10));
            log.info("writeBit BINARY : {}", melsecQ3EBinary.writeBit(MelsecQFormat.DeviceCode.M, 1000, new int[]{1, 1, 0}));
            log.info("readWord BINARY : {}", melsecQ3EBinary.readWord(MelsecQFormat.DeviceCode.W, 1000, 10));
            log.info("writeWord BINARY : {}", melsecQ3EBinary.writeWord(MelsecQFormat.DeviceCode.W, 1000, new int[]{1, 2, 3, 4, 5, 6, 7, 8, 9, 10}));
        }catch (Exception ignore){}
    }


    @Test
    void Java소켓(){
        // 서버
        JavaSocketServer serverSocket = new JavaSocketServer(9000);


        // 클라이언트
        JavaSocketClient socketClient = new JavaSocketClient("localhost", 9000);
        try {
            log.info("3");
//            serverSocket.start(); // TODO : finally에서 stop()호출 하는 듯 함
//            socketClient.connect();
            log.info("ServerSocket :{}", "Start");

        }catch (Exception e){
            log.error(e.getMessage());
        }finally {
            socketClient.disconnect();

            serverSocket.stop();
        }
    }
}
