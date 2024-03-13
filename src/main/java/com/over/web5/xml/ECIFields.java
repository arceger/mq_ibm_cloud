package com.over.web5.xml;

import java.text.ParseException;
import java.text.RuleBasedCollator;
import java.util.HashMap;
import java.util.TreeSet;


public class ECIFields {
	
	
	private RuleBasedCollator collator;
	private HashMap map;
	private TreeSet set;
	private ECIHeader eciHeader;
	

	/*
	 * 
	 * carrega as classes para ordenacao e maps para armazenamento dos fields
	 * a ordenacao � realizada no TreeSet.
	 * 
	 */
	public ECIFields(){
		
			try {
				// classe para ordenacao conforme parametro passado.
				collator = new RuleBasedCollator("<0001<0002<0003<0004<0005<0006<0007<0008<0009<" +
					"0012<0013<0014<0023<0024<0028<0030<0035<0036<0040<0076<0102<0137<0138<0139<0140<0141<0142<0143<" +
					"0144<0146<0147<0151<0152");
				set = new TreeSet(collator);
				map = new HashMap();
				eciHeader= new ECIHeader();
			
			} catch (ParseException e) {
				// TODO Bloco de captura gerado automaticamente
				e.printStackTrace();
			}
			
		}
		
		/**
		 * adiciona um fileld para armazenamento e ordenacao
		 * @param key: chave do field � o ID do field no ECI ex 0002
		 * @param data: informacao a ser armazenada para o field
		 */
	public void addField(String key, String data){
			set.add(key);
			map.put(key,data);
		
	}
	/**
	 * pega a quantidade de field que foi adicionada
	 * @return quantidade de fiels
	 */
	public int getSize(){
		
		return map.size();
	}
	
	/**
	 *  pega o field de acordo com o parametro
	 * @param key: ID do field do ECI 
	 * @return valor do field.
	 */
	
	public String getField(String key){
		
		String retorno = (String)map.get(key);
		return retorno;
		
	}


	/**
	 * classe que pega a string do field ordenada com o nome do campo e tamanho
	 * ex: 00020004abcd
	 * @return string field do ECI
	 */

	public String getFields(){
		String returns="";
		
		int size = set.size();
		// pega o primeiro elemento do treeset e concatena com a chave e o tamanho.
		for(int i=0;i<size;i++){
			int dataSize = map.get(set.first()).toString().length();
			
			if(dataSize<=9)
				returns+=set.first().toString()+"000"+dataSize+map.get(set.first()).toString();
			else
				if(dataSize<=99)
					returns+=set.first().toString()+"00"+dataSize+map.get(set.first()).toString();
				else
					if(dataSize<=999)
						returns+=set.first().toString()+"0"+dataSize+map.get(set.first()).toString();
					else
						if(dataSize<=9999)
							returns+=set.first().toString()+dataSize+map.get(set.first()).toString();
			set.remove(set.first());
		}
			
		
		return returns;
	}
	


	/**
	 * @return eciHeader
	 */
	public ECIHeader getEciHeader() {
		return eciHeader;
	}

	/**
	 * @param header eciHeader
	 */
	public void setEciHeader(ECIHeader header) {
		eciHeader = header;
	}

	public static void setDataObject(String string, ObjHeader objHeader) {
		// TODO Auto-generated method stub
		
	}

}
