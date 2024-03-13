package com.over.web5.xml;

public class ECITransformer {

	private String eciRecord="";
	private ECIFields eciFields;
	
	public ECITransformer(){}
	
	
	/**
	 * construtor utilizado para criar um transformador, converto a String do ECI para a classe ECIFields
	 * @param eciRecord 
	 */
	
	public ECITransformer(String eciRecord){
		this.eciRecord=eciRecord;
		
	}
	/**
	 * construtor utilizado para criar um transformador; converte um ECIFields para uma string do ECI
	 * @param eciFields
	 */
	public ECITransformer(ECIFields eciFields){
		
		this.eciFields=eciFields;
		
	}
	/**
	 * realiza a transformacao do ECIFields para a string do ECI
	 * @return string a ser enviada para o ECI
	 * @throws NullPointerException caso tenha algum campo obrigatorio nulo
	 */
	
	public String transformToEci(){
			
		String fields = this.eciFields.getFields();
		eciFields.getEciHeader().setNumberFields(eciFields.getSize()+"");
		eciFields.getEciHeader().setRecordLength((fields.length()+169)+"");
		String header = this.eciFields.getEciHeader().getECIHeaderString();
		String eciRecord = header+fields;
					
		return eciRecord;
		
	}
	
	/**
	 * tranforma a string do ECI para um tipo ECIFields
	 * @return classe ECIFields
	 * @throws NullPointerException caso tenha algum campo abrigatorio nulo
	 */
	public ECIFields transformToField(){
				
		ECIFields eciFields = new ECIFields();
		//pega os valores do Header ele possui uma posicao fixa
		eciFields.getEciHeader().setRecordIdentifier(eciRecord.substring(0,8));
		eciFields.getEciHeader().setRecordType(eciRecord.substring(8,12));
		eciFields.getEciHeader().setRecordLength(eciRecord.substring(12,20));
		eciFields.getEciHeader().setNumberFields(eciRecord.substring(20,28));
		eciFields.getEciHeader().setGeneralError(eciRecord.substring(28,32));
		eciFields.getEciHeader().setECI_ID(eciRecord.substring(32,42));
		eciFields.getEciHeader().setIBMCountryCode(eciRecord.substring(42,45));
		eciFields.getEciHeader().setCreationDate(eciRecord.substring(45,53));
		eciFields.getEciHeader().setCreationTime(eciRecord.substring(53,59));
		eciFields.getEciHeader().setVendorID(eciRecord.substring(59,69));
		eciFields.getEciHeader().setBrokerID(eciRecord.substring(69,79));
		eciFields.getEciHeader().setCallManagerID(eciRecord.substring(79,89));
		eciFields.getEciHeader().setRecordVersion(eciRecord.substring(89,93));
		eciFields.getEciHeader().setGMToffset(eciRecord.substring(93,98));
		eciFields.getEciHeader().setLanguageCode(eciRecord.substring(98,100));
		eciFields.getEciHeader().setReserved(eciRecord.substring(100,169));
		
		/*
		 * a partir deste ponto a logica funciona para pegar os valores e ids do 
		 * ECI e colocalos dentro da classe ECIFields
		 * 
		 */
		
		
		// pega o numero de records
		int numberFields=Integer.parseInt(eciRecord.substring(20,28));
		//pega a string a partir do inicio dos Fields
		String fields = eciRecord.substring(169);
		
		for(int i=0;i<numberFields;i++){
			// pega o id que fica nas primeiras 4 posicoes
			String cod = fields.substring(0,4);
			// pega o tamanho da string que fica 4 posicoes depois do id
			int tam = Integer.parseInt(fields.substring(4,8));
			//pega o valor de acordo com o tamanho o valor fica apos o id e o tamanho na string
			String value = fields.substring(8,tam+8);
			// adiciona o ID no map
			eciFields.addField(cod,value);
			// pega a string a partir do proximo field
			fields = fields.substring(tam+8);
		
		}
		
		return eciFields;
	}
	
	/**
	 * @return ECIFields
	 */
	public ECIFields getEciFields() {
		return eciFields;
	}

	/**
	 * @return String do ECI
	 */
	public String getEciRecord() {
		return eciRecord;
	}

	/**
	 * @param fields eciFields
	 */
	public void setEciFields(ECIFields fields) {
		eciFields = fields;
	}

	/**
	 * @param string eciRecord
	 */
	public void setEciRecord(String string) {
		eciRecord = string;
	}
	
	
	
	

}
