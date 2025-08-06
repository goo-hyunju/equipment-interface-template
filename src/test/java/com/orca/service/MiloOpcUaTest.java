package com.eqca.service;

import java.util.Arrays;
import java.util.concurrent.ExecutionException;

import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UShort;
import org.junit.jupiter.api.Test;

import com.eqca.config.opcua.MiloOpcUaClient;

public class MiloOpcUaTest {
	
	public class MiloOpcUaClientTest {

		@Test
		void OpcUaClisentTest() throws InterruptedException, ExecutionException {
			
			MiloOpcUaClient client = new MiloOpcUaClient("192.168.3.211", 4840);
			try {
							
				client.connect();
				
				//Word 읽기
				Object value = client.readNodeValue(2, "LS XBM/XBC FEnet (Ethernet).Tags.D400");
				System.out.println("object : " + value);
				
				
				int test = 3;
				UShort writeUshort = UShort.valueOf(3);
				client.writeNodeValue(2, "LS XBM/XBC FEnet (Ethernet).Tags.D400", "21");
				
				UShort[] ushortArray = (UShort[]) value;
				int[] intArray = Arrays.stream(ushortArray).mapToInt(UShort::intValue).toArray();
				int i = 0;
				for(int a : intArray){				
					System.out.println("value[" + i + "] : " + a);
					i++;
				}
				
				intArray[0] = 1;
							
		        UShort[] ushortArrayWrite = new UShort[intArray.length];

		        for (int b = 0; b < intArray.length; b++) {
		        	ushortArrayWrite[b] = UShort.valueOf(intArray[b]);
		        }
		        			
				client.writeNodeValue(2, "Mitsubishi MELSEC-Q/L - Binary Mode.Tags.D200", ushortArrayWrite);
				
				System.out.println("write가 되나?" + intArray);
							
				
				// Bit 읽기
				Object value2 = client.readNodeValue(2, "Mitsubishi MELSEC-Q/L - Binary Mode.Tags.M200");						
				Boolean[] booleanArray = (Boolean[]) value2;		
							
				// Boolean[] → boolean[] 변환
			    boolean[] primitiveArray = new boolean[booleanArray.length];
			    for (int j = 0; j < booleanArray.length; j++) {
			        primitiveArray[j] = booleanArray[j]; // Unboxing
			    }		    
				int c = 0;
				for(boolean a : primitiveArray){				
					System.out.println("value[" + c + "] : " + a);
					c++;
				}
				
				
				
				
				client.disConnect();
			}catch(Exception e) {
				System.out.println("OpcUaClientTest error : " + e);
				client.disConnect();
			}
			
		}
	}

}
