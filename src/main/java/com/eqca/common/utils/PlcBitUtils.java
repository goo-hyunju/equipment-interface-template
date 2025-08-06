package com.eqca.common.utils;

/**
 * PLC 데이터 비트 분석을 위한 유틸리티 클래스
 */
public class PlcBitUtils {
    
    /**
     * 16비트 데이터를 상세한 비트 문자열로 변환
     * @param value 10진수 값
     * @return 비트 분석 문자열
     */
    public static String toBitAnalysisString(int value) {
        String bit16 = String.format("%16s", Integer.toBinaryString(value)).replace(' ', '0');
        String highByte = bit16.substring(0, 8);
        String lowByte = bit16.substring(8, 16);
        
        return String.format("16bit:%s High:%s Low:%s Dec:%d Hex:0x%04X", 
            bit16, highByte, lowByte, value, value & 0xFFFF);
    }
    
    /**
     * 비트별 상세 분석 (설정된 비트만 표시)
     * @param value 10진수 값
     * @return 설정된 비트 정보 문자열
     */
    public static String getSetBitsInfo(int value) {
        if (value == 0) return "모든 비트 0";
        
        StringBuilder sb = new StringBuilder();
        for (int i = 15; i >= 0; i--) {
            if ((value & (1 << i)) != 0) {
                sb.append(String.format("Bit%d ", i));
            }
        }
        return sb.toString().trim();
    }
    
    /**
     * 니블(4비트) 단위로 분리
     * @param value 10진수 값
     * @return 니블별 분리 문자열
     */
    public static String toNibbleString(int value) {
        String bit16 = String.format("%16s", Integer.toBinaryString(value)).replace(' ', '0');
        return String.format("%s_%s_%s_%s", 
            bit16.substring(0, 4),   // Bit 15-12
            bit16.substring(4, 8),   // Bit 11-8
            bit16.substring(8, 12),  // Bit 7-4
            bit16.substring(12, 16)  // Bit 3-0
        );
    }
    
    /**
     * 동작상태 비트 분석 (설비 사양서 기반)
     * @param motionStatus 동작상태 값
     * @return 에러 비트 분석 결과
     */
    public static String analyzeMotionStatusBits(int motionStatus) {
        boolean sensorError = (motionStatus & 0x0001) != 0;  // Bit 0
        boolean motorError = (motionStatus & 0x0002) != 0;   // Bit 1
        boolean cableError = (motionStatus & 0x0004) != 0;   // Bit 2
        
        String baseStatus = switch (motionStatus & 0xFFF8) {  // 상위 비트로 기본 상태 판단
            case 0 -> "대기";
            case 8 -> "동작";  // Bit 3 set
            case 16 -> "에러"; // Bit 4 set
            default -> "기타";
        };
        
        StringBuilder errors = new StringBuilder();
        if (sensorError) errors.append("센서에러 ");
        if (motorError) errors.append("모터에러 ");
        if (cableError) errors.append("케이블에러 ");
        
        return String.format("기본상태:%s 에러:%s", baseStatus, 
            errors.length() > 0 ? errors.toString().trim() : "없음");
    }
    
    /**
     * 전체 맵 데이터의 비트 패턴 요약
     * @param readData PLC 읽기 데이터 맵
     * @return 비트 패턴 요약 문자열
     */
    public static String summarizeBitPatterns(java.util.Map<Integer, com.altiall.plc.mitsubishi.MelsecData> readData) {
        if (readData.isEmpty()) return "데이터 없음";
        
        long totalBits = readData.size() * 16L;
        long setBits = readData.values().stream()
            .mapToLong(data -> Integer.bitCount(data.getDecimal()))
            .sum();
        
        int minAddr = readData.keySet().stream().mapToInt(Integer::intValue).min().orElse(0);
        int maxAddr = readData.keySet().stream().mapToInt(Integer::intValue).max().orElse(0);
        
        int nonZeroCount = (int) readData.values().stream()
            .mapToInt(data -> data.getDecimal())
            .filter(value -> value != 0)
            .count();
        
        return String.format("주소:%d~%d 총:%d개 비트:%d/%d(%.1f%%) 논제로:%d개", 
            minAddr, maxAddr, readData.size(), setBits, totalBits, 
            (setBits * 100.0 / totalBits), nonZeroCount);
    }
    
    /**
     * 트랙 데이터 비트 패턴 분석
     * @param trackData 5개 워드 트랙 데이터 배열
     * @param trackNo 트랙 번호
     * @return 트랙 비트 패턴 분석 결과
     */
    public static String analyzeTrackBitPattern(int[] trackData, int trackNo) {
        if (trackData.length != 5) return "잘못된 트랙 데이터 크기";
        
        String[] fieldNames = {"제어모드", "동작상태", "상태코드", "화물작번", "목적지"};
        StringBuilder sb = new StringBuilder();
        sb.append(String.format("Track%d: ", trackNo));
        
        for (int i = 0; i < 5; i++) {
            String bitStr = String.format("%16s", Integer.toBinaryString(trackData[i])).replace(' ', '0');
            sb.append(String.format("%s(%d):%s ", fieldNames[i], trackData[i], 
                bitStr.substring(12))); // 하위 4비트만 표시
        }
        
        return sb.toString();
    }
}
