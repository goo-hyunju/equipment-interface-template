package com.eqca.common.utils;

import java.math.BigInteger;

import com.altiall.convert.Converter;

public class PlcDataConvert {
	/**
     * 16진수 문자열의 바이트 순서를 바꾸고(Swap) ASCII로 변환합니다.
     * (예: "4241" -> "4142" -> "AB")
     *
     * @param littleEndianHex 리틀 엔디안 방식의 16진수 데이터 (예: "4241")
     * @return 바이트 순서가 변환된 최종 ASCII 문자열 (예: "AB")
     */
    public static String swapBytesAndConvertToAscii(String littleEndianHex) {
        if (littleEndianHex == null || littleEndianHex.length() % 4 != 0) {
            System.err.println("❌ 에러: 입력된 16진수 문자열의 길이는 4의 배수여야 합니다.");
            return null;
        }

        // 바이트 순서를 뒤집어 저장할 StringBuilder
        StringBuilder bigEndianHex = new StringBuilder();

        // 4글자(2바이트) 단위로 순회
        for (int i = 0; i < littleEndianHex.length(); i += 4) {
            // 2바이트 워드 (예: "4241")
            String word = littleEndianHex.substring(i, i + 4);
            // 상위 바이트와 하위 바이트를 분리
            String lsb = word.substring(0, 2); // 하위 바이트 (예: "42")
            String msb = word.substring(2, 4); // 상위 바이트 (예: "41")

            // 순서를 뒤집어서(MSB + LSB) 추가
            bigEndianHex.append(msb).append(lsb); // "41" + "42"
        }

        // 순서가 맞춰진 16진수 문자열("4142")을 최종 ASCII("AB")로 변환
        return Converter.hexString2AscString(bigEndianHex.toString());
    }
    
    /**
     * 16진수 문자열의 바이트 순서를 바꾸고(Swap) DECIMAL 변환합니다.
     * (예: "4241" -> "4142" -> 171)
     *
     * @param littleEndianHex 리틀 엔디안 방식의 16진수 데이터 (예: "4241")
     * @return 바이트 순서가 변환된 최종 ASCII 문자열 (예: 171)
     */
    public static int swapBytesAndConvertToDecimal(String littleEndianHex) {
        if (littleEndianHex == null || littleEndianHex.length() % 4 != 0) {
            System.err.println("❌ 에러: 입력된 16진수 문자열의 길이는 4의 배수여야 합니다.");
            return 0;
        }

        // 바이트 순서를 뒤집어 저장할 StringBuilder
        StringBuilder bigEndianHex = new StringBuilder();

        // 4글자(2바이트) 단위로 순회
        for (int i = 0; i < littleEndianHex.length(); i += 4) {
            // 2바이트 워드 (예: "4241")
            String word = littleEndianHex.substring(i, i + 4);
            // 상위 바이트와 하위 바이트를 분리
            String lsb = word.substring(0, 2); // 하위 바이트 (예: "42")
            String msb = word.substring(2, 4); // 상위 바이트 (예: "41")

            // 순서를 뒤집어서(MSB + LSB) 추가
            bigEndianHex.append(msb).append(lsb); // "41" + "42"
        }

        // 순서가 맞춰진 16진수 문자열("4142")을 최종 ASCII("AB")로 변환
        return Converter.hexString2Decimal(bigEndianHex.toString());
    }
    
    /**
     * 16진수 문자열의 바이트 순서를 바꾸고(Swap) 2진수로 변환하고 2진수를 다시 뒤집는다.
     * (예: "4241" -> "4142" -> "0100001010000010")
     *
     * @param littleEndianHex 리틀 엔디안 방식의 16진수 데이터 (예: "4241")
     * @return 바이트 순서가 변환된 최종 ASCII 문자열 (예: "0100001010000010")
     */
    public static String swapByteAndConvertToBitData(String littleEndianHex) {
    	if (littleEndianHex == null || littleEndianHex.length() % 4 != 0) {
            System.err.println("❌ 에러: 입력된 16진수 문자열의 길이는 4의 배수여야 합니다.");
            return null;
        }
    	
    	// 2. 바이트 순서 변경 ("4241" -> "4142")
        StringBuilder byteSwappedHex = new StringBuilder();
        // 문자열 끝에서부터 2글자씩 잘라서 앞에 추가합니다.
        for (int i = littleEndianHex.length(); i > 0; i -= 2) {
            byteSwappedHex.append(littleEndianHex.substring(i - 2, i));
        }        

        // 3. 2진수로 변환
        // BigInteger를 사용해 16진수 문자열을 숫자로 변환 후 2진수 문자열로 만듭니다.
        String binaryString = new BigInteger(byteSwappedHex.toString(), 16).toString(2);
        
        // 16진수 한 자리는 2진수 네 자리이므로 길이를 맞춰주기 위해 앞에 0을 추가합니다.
        int expectedBitLength = byteSwappedHex.length() * 4;
        while (binaryString.length() < expectedBitLength) {
            binaryString = "0" + binaryString;
        }        

        // 4. 2진수 값 뒤집기
        String reversedBinary = new StringBuilder(binaryString).reverse().toString();
        
        return reversedBinary;
    }
}
