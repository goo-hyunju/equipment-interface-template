package com.eqca.repository.dto.domain.eqca;


import lombok.Builder;

/**
 * Adapter 표준 프로토콜에 대한 Data Record
 * @since 1.0
 * @version 2.0
 * @author kth
 */
public class OrcaCommunicationRecord {

	/**
     * HeartBeat
     * @param eqCode 설비코드
     */
    @Builder
    public record HeartBeat(
            String eqCode
    ){}
        
    /**
     * 설비상태코드
     * @param eqCode 설비코드
     * @param deviceCode 장비코드
     * @param ctrlStatus 제어모드(0:자동, 1:수동, 2:알람)
     * @param workExist 화물유무
     * @param statusCode 상태코드
     * @param statusDesc 상태설명
     */
    @Builder
    public record EquipmentStatusReport(
            String eqCode,
            String deviceCode,
            int ctrlStatus,
            int workExist,
            String statusCode,
            String statusDesc            
    ){}
    
    /**
     * 로딩보고
     * @param eqCode 설비코드
     * @param deviceCode 장비코드
     * @param barcode 화물 바코드
     * @param stockSize 화물사이즈
     * @param infPid 인터락번호
     */
    @Builder
    public record LoadingReport(
            String eqCode,
            String deviceCode,
            String barcode,
            int stockSize,
            int infPid                       
    ){}
    
    /**
     * 언로딩보고
     * @param eqCode 설비코드
     * @param deviceCode 장비코드
     * @param barcode 화물 바코드
     * @param infPid 인터락번호
     */
    @Builder
    public record UnLoadingReport(
            String eqCode,
            String deviceCode,
            String barcode,
            int infPid                       
    ){}
    
    /**
     * 보관설비 작업지시
     * 
     */
    @Builder
    public record StorageOrder(
            String eqCode,
            String deviceCode,
            String cmdId,
            String cmdType,
            String barcode,
            String sourceCode,
            String sourceBay,
            String sourceRow,
            String sourceLevel,
            String sourceDeep,
            String destCode,
            String destBay,
            String destRow,
            String destLevel,
            String destDeep,
            String desc1,
            String desc2,
            String desc3
    ){}
    
    /**
     * 이동설비 작업지시
     * 
     */
    @Builder
    public record RouteOrder(
            String eqCode,
            String deviceCode,
            String cmdId,
            String cmdType,
            String barcode,
            String sourceCode,            
            String destCode,            
            String desc1,
            String desc2,
            String desc3
    ){}
    
    /**
     * Place설비 작업지시
     * 
     */
    @Builder
    public record PlaceOrder(
            String eqCode,
            String deviceCode,
            String cmdId,
            String cmdType,
            String barcode,
            String barcodeInfo,
            String virtualBarcode,
            String targetBarcode,
            String orderCount,
            String assignDeviceCode,
            String orderPriority,            
            String sourceCode,            
            String destCode,     
            String orderTotalCount,
            String desc1,
            String desc2,
            String desc3
    ){}
    
    /**
     * 피킹설비 작업지시
     * 
     */
    @Builder
    public record PickOrder(
            String eqCode,
            String deviceCode,
            String cmdId,
            String cmdType,
            String barcode,
            String barcodeInfo,
            String orderCount,
            String assighDeviceCode,
            String sourceCode,            
            String destCode,            
            String desc1,
            String desc2,
            String desc3
    ){}
    
    /**
     * 분류설비 작업지시
     * 
     */
    @Builder
    public record SortOrder(
            String eqCode,            
            String cmdId,
            String cmdType,
            String barcode,                                      
            String destChuteCode,            
            String desc1,
            String desc2,
            String desc3
    ){}
    
    /**
     * 보관설비 작업 결과
     * 
     */
    @Builder
    public record StorageOrderResult(
            String eqCode,            
            String cmdId,
            String cmdType,
            String sourceCode,                                      
            String destCode,
            String resultCode,
            String errorDesc,
            String desc1,
            String desc2,
            String desc3
    ){}
    
    /**
     * 이동설비 작업 결과
     * 
     */
    @Builder
    public record RouteOrderResult(
            String eqCode,            
            String cmdId,
            String cmdType,
            String sourceCode,                                      
            String destCode,
            String resultCode,
            String errorDesc,
            String desc1,
            String desc2,
            String desc3
    ){}
    
    /**
     * Place설비 작업 결과
     * 
     */
    @Builder
    public record PlaceOrderResult(
            String eqCode,            
            String cmdId,
            String cmdType,
            String barcode,
            String virtualBarcode,
            String targetBarocde,
            String orderCount,
            String resultCount,
            String sourceCode,                                      
            String destCode,
            String resultCode,
            String errorDesc,
            String desc1,
            String desc2,
            String desc3
    ){}
    
    /**
     * 피킹설비 작업 결과
     * 
     */
    @Builder
    public record PickOrderResult(
            String eqCode,            
            String cmdId,
            String cmdType,
            String barcode,            
            String orderCount,
            String resultCount,
            String sourceCode,                                      
            String destCode,
            String resultCode,
            String errorDesc,
            String desc1,
            String desc2,
            String desc3
    ){}
    
    /**
     * 분류설비 작업 결과
     * 
     */
    @Builder
    public record SortOrderResult(
            String eqCode,            
            String cmdId,
            String cmdType,
            String barcode,            
            String orderCount,
            String resultCount,
            String sourceCode,                                      
            String destCode,
            String resultCode,
            String errorDesc,
            String desc1,
            String desc2,
            String desc3
    ){}
    
    /**
     * 적재 Cell 요청
     * 
     */
    @Builder
    public record RequestStockCell(
            String eqCode,            
            String barcode,
            String itemCode,
            String itemClass            
    ){}
    
    /**
     * 적재 Cell 요청
     * 
     */
    @Builder
    public record ResponseStockCell(
            String eqCode,            
            String barcode,
            String cellCode          
    ){}
}
