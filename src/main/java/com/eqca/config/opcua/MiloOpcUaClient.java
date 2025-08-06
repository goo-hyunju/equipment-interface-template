package com.eqca.config.opcua;

import java.util.concurrent.ExecutionException;

import org.eclipse.milo.opcua.sdk.client.OpcUaClient;
import org.eclipse.milo.opcua.sdk.client.nodes.UaVariableNode;
import org.eclipse.milo.opcua.stack.core.UaException;
import org.eclipse.milo.opcua.stack.core.types.builtin.DataValue;
import org.eclipse.milo.opcua.stack.core.types.builtin.NodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.StatusCode;
import org.eclipse.milo.opcua.stack.core.types.builtin.Variant;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UByte;
import org.eclipse.milo.opcua.stack.core.types.enumerated.TimestampsToReturn;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class MiloOpcUaClient {
	private final String endPointUrl ;
	private static final int CURRENT_WRITE = 0x02;
	private OpcUaClient client = null;
	
	public MiloOpcUaClient(String address, int port) {
		
		this.endPointUrl = "opc.tcp://" + address + ":" + port + "/G01";
	}
	
	
	public void connect() throws InterruptedException, ExecutionException {
		try {			
			client = OpcUaClient.create(endPointUrl);
            
			client.connect().get();				
			
			
			
		} catch (UaException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void disConnect() throws InterruptedException, ExecutionException{
		try {
			if(client != null) {
				client.disconnect().get();
				client = null;
			}
			
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ExecutionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	/**
     * OPC UA 서버에서 특정 노드의 값을 읽기
     */
    public Object readNodeValue(int namespaceIndex, String identifier) throws ExecutionException, InterruptedException {
        if (client == null) {
            throw new IllegalStateException("클라이언트가 연결되지 않았습니다.");
        }
        NodeId nodeId = new NodeId(namespaceIndex, identifier);
        TimestampsToReturn timestamp = TimestampsToReturn.Both;        
                
        DataValue dataValue = client.readValue(0, timestamp, nodeId).get();
        
        
        if (dataValue == null || dataValue.getValue() == null) {
            log.info("Read null!!");
            return null;
        }               
        Variant variant = dataValue.getValue();
                        
        log.info("현재 노드 값의 데이터 타입: " + variant.getValue().getClass().getSimpleName());
                
        // 노드 값 출력
        Object value = dataValue.getValue().getValue();
        
        return value;
    }
    
    /**
     * OPC UA 서버에서 특정 노드의 값을 설정
     * @throws UaException 
     */
    public void writeNodeValue(int namespaceIndex, String identifier, Object value) throws ExecutionException, InterruptedException, UaException {
        if (client == null) {
            throw new IllegalStateException("클라이언트가 연결되지 않았습니다.");
        }
                
        
        NodeId nodeId = new NodeId(namespaceIndex, identifier);
        UaVariableNode node = (UaVariableNode) client.getAddressSpace().getNode(nodeId);
        
        NodeId dataTypeId = node.getDataType();
                        
        log.info("해당 노드의 데이터 타입: " + dataTypeId);
             
        
     // 데이터 타입의 실제 이름 가져오기
        String dataTypeName = client.getAddressSpace().getNode(dataTypeId).getBrowseName().getName();
        log.info("해당 노드의 데이터 타입: " + dataTypeName);
        
        boolean isHistorizing = node.getHistorizing();

        if (isHistorizing) {
        	log.info("해당 노드는 Historizing 모드가 활성화되어 값 변경이 불가능합니다.");
        } else {
        	log.info("해당 노드는 값을 변경할 수 있습니다.");
        }

        UByte accessLevel = node.getAccessLevel(); // UByte 반환
        UByte userAccessLevel = node.getUserAccessLevel(); // UByte 반환
        
        
        int accessLevelInt = accessLevel.intValue(); // int 변환
        int userAccessLevelInt = userAccessLevel.intValue(); // int 변환
        log.info("Access Level (int): " + accessLevelInt + ", User Access Level (int): " + userAccessLevelInt);

        // 쓰기 권한 확인 (비트 마스킹)
        boolean canWrite = (accessLevelInt & CURRENT_WRITE) != 0;

        if (canWrite) {
        	log.info("해당 노드는 쓰기 가능합니다.");
        } else {
        	log.info("해당 노드는 읽기 전용입니다.");
        }
                
        
        Variant variantValue = new Variant(value); // Variant로 감싸기
        
        DataValue dataValue = new DataValue(variantValue); // DataValue 생성
                
        StatusCode status = client.writeValue(nodeId, dataValue).get();
        
        if (status.isGood()) {
        	log.info("노드 값 쓰기 성공!");
        } else {
        	log.info("노드 값 쓰기 실패: " + status);
        }
        
    }
}
