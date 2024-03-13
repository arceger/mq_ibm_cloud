/*
 * Criado em 27/03/2009
 *
 * Para alterar o gabarito para este arquivo gerado v� para
 * Janela&gt;Prefer�ncias&gt;Java&gt;Gera��o de C�digos&gt;C�digo e Coment�rios
 */
package com.over.web5.xml;

import java.util.Calendar;


public class ECIHeader {

	private String recordIdentifier = new String();
	private String recordType = new String();
	private String recordLength = new String();
	private String numberFields = new String();
	private String generalError = new String();
	private String ECI_ID = new String();
	private String ibmCountryCode = new String();
	private String creationDate = new String();
	private String creationTime = new String();
	private String vendorID = new String();
	private String brokerID = new String();
	private String callManagerID = new String();
	private String recordVersion = new String();
	private String GMToffset = new String();
	private String languageCode = new String();
	private String reserved = new String("                              " +
		"                                       ");
	
public ECIHeader(){
	
	//o formato do ECI � YYYYMMDD E HHMMSS
	
	

		
	Calendar calendar = Calendar.getInstance();
	
	//seta o date
	int ano,dia,mes;
	String date="";
	ano=calendar.get(Calendar.YEAR);
	dia=(calendar.get(Calendar.MONTH)+1);
	mes=calendar.get(Calendar.DAY_OF_MONTH);
	
	date+=ano;
	
	if(dia<10)
		date+="0"+dia;
		else
			date+=dia;
			
	if(mes<10)
		date+="0"+mes;
		else
			date+=mes;
	
	this.setCreationDate(date);
	
	
	//seta o time
	String time="";
	int hora,minuto,segundo;
	hora=calendar.get(Calendar.HOUR_OF_DAY);
	minuto=calendar.get(Calendar.MINUTE);
	segundo=calendar.get(Calendar.SECOND);
	
	
	if(hora<10)
		time+="0"+hora;
		else
			time+=hora;
		
	if(minuto<10)
		time+="0"+minuto;
		else
			time+=minuto;
	
	if(segundo<10)
		time+="0"+segundo;
		else
			time+=segundo;
		
	this.setCreationTime(time);
	this.setGeneralError("0000");
}


	/**
	 * retorna a string header do ECI
	 * @return string header do ECI
	 */

	public String getECIHeaderString() throws IllegalArgumentException{
		
		String returns = new String();
		
		//verifica se todos os campo abedecem o tamanho	
		if(brokerID.length()!=10){
			throw new IllegalArgumentException("Campo BrokerID com tamanho incorreto = "+brokerID.length()+", seu tamanho deve ser igual a 10");
		}		
				
		if(callManagerID.length()!=10){
			throw new IllegalArgumentException("Campo CallManagerID com tamanho incorreto = "+callManagerID.length()+", seu tamanho deve ser igual a 10");
		}	
				
		if(ECI_ID.length()!=10){
			throw new IllegalArgumentException("Campo ECI_ID com tamanho incorreto = "+ECI_ID.length()+", seu tamanho deve ser igual a 10");
		}		
				
		if(generalError.length()!=4){
			throw new IllegalArgumentException("Campo GeneralError com tamanho incorreto = "+generalError.length()+", seu tamanho deve ser igual a 4");
		}		
				
		if(GMToffset.length()!=5){
			throw new IllegalArgumentException("Campo GMToffset com tamanho incorreto = "+GMToffset.length()+", seu tamanho deve ser igual a 5");
		}		
		
		if(ibmCountryCode.length()!=3){
			throw new IllegalArgumentException("Campo IBMCountryCode com tamanho incorreto = "+ibmCountryCode.length()+", seu tamanho deve ser igual a 3");
		}		
		
		if(languageCode.length()!=2){
			throw new IllegalArgumentException("Campo LanguageCode com tamanho incorreto = "+languageCode.length()+", seu tamanho deve ser igual a 2");
		}		
				
		if(recordIdentifier.length()!=8){
			throw new IllegalArgumentException("Campo RecordIdentifier com tamanho incorreto = "+(recordIdentifier.length()-2)+", seu tamanho deve ser igual a 6");
		}		
				
		if(recordType.length()!=4){
			throw new IllegalArgumentException("Campo RecordType com tamanho incorreto = "+recordType.length()+" , tamanho deve ser igual a 4");
		}		
				
		if(recordVersion.length()!=4){
			throw new IllegalArgumentException("Campo RecordVersion com tamanho incorreto = "+recordVersion.length()+", seu tamanho deve ser igual a 4");
		}
				
		if(reserved.length()!=69){
			throw new IllegalArgumentException("Campo Reserved com tamanho incorreto = "+reserved.length()+", seu tamanho deve ser igual a 69");
		}		
				
		if(vendorID.length()!=10){
			throw new IllegalArgumentException("Campo VendorID com tamanho incorreto = "+vendorID.length()+", seu tamanho deve ser igual a 10");
		}		
							
		returns = recordIdentifier+recordType+recordLength+numberFields+
		generalError+ECI_ID+ibmCountryCode+creationDate+creationTime+
		vendorID+brokerID+callManagerID+recordVersion+GMToffset+
		languageCode+reserved;
		
		return returns; 
	}


	/**
	 * @return brokerID
	 */
	public String getBrokerID() {
		return brokerID;
	}

	/**
	 * @return callManagerID
	 */
	public String getCallManagerID() {
		return callManagerID;
	}

	/**
	 * @return creationDate
	 */
	public String getCreationDate() {
		return creationDate;
	}

	/**
	 * @return creationTime
	 */
	public String getCreationTime() {
		return creationTime;
	}

	/**
	 * @return ECI_ID
	 */
	public String getECI_ID() {
		return ECI_ID;
	}

	/**
	 * @return generalError
	 */
	public String getGeneralError() {
		return generalError;
	}

	/**
	 * @return GMToffset
	 */
	public String getGMToffset() {
		return GMToffset;
	}

	/**
	 * @return ibmCountryCode
	 */
	public String getIBMCountryCode() {
		return ibmCountryCode;
	}

	/**
	 * @return languageCode
	 */
	public String getLanguageCode() {
		return languageCode;
	}

	/**
	 * @return numberFields
	 */
	public String getNumberFields() {
		return numberFields;
	}

	/** 
	 * @return recordIdentifier
	 */
	public String getRecordIdentifier() {
		return recordIdentifier;
	}

	/**
	 * @return recordLength
	 */
	public String getRecordLength() {
		return recordLength;
	}

	/**
	 * @return recordType
	 */
	public String getRecordType() {
		return recordType;
	}

	/**
	 * @return recordVersion
	 */
	public String getRecordVersion() {
		return recordVersion;
	}

	/**
	 * @return reserved
	 */
	public String getReserved() {
		return reserved;
	}

	/**
	 * @return vendorID
	 */
	public String getVendorID() {
		return vendorID;
	}

	/**
	 * @param string brokerID
	 */
	public void setBrokerID(String string){
									
		brokerID = string;
	}

	/**
	 * @param string callManagerID
	 */
	public void setCallManagerID(String string){
		
		callManagerID = string;
	}

	/**
	 * @param string creationDate
	 */
	public void setCreationDate(String string) {
		creationDate = string;
	}

	/**
	 * @param string creationTime
	 */
	public void setCreationTime(String string) {
		creationTime = string;
	}

	/**
	 * @param string ECI_ID
	 */
	public void setECI_ID(String string){
		
		ECI_ID = string;
	}

	/**
	 * @param string generalError
	 */
	public void setGeneralError(String string){
		
		generalError = string;
	}

	/**
	 * @param string GMToffset
	 */
	public void setGMToffset(String string){
		
		GMToffset = string;
	}

	/**
	 * @param string ibmCountryCode
	 */
	public void setIBMCountryCode(String string){
		
		ibmCountryCode = string;
	}

	/**
	 * @param string languageCode
	 */
	public void setLanguageCode(String string){
		
		languageCode = string;
	}

	/**
	 * @param string numberFields
	 */
	public void setNumberFields(String string) {
		
		int size = 8 - string.length();
		
		for( int i=0;i<size;i++)
			string="0"+string;
				
		numberFields = string;
	}

	/**
	 * @param string recordIdentifier
	 */
	public void setRecordIdentifier(String string){
		
		recordIdentifier = string+"  ";
	}

	/**
	 * @param string recordLength
	 */
	public void setRecordLength(String string) {
		
		int size = 8 - string.length();
		
			for( int i=0;i<size;i++)
				string="0"+string;
				
				
		recordLength = string;
	}

	/**
	 * @param string recordType
	 */
	public void setRecordType(String string){
		
		recordType = string;
	}

	/**
	 * @param string recordVersion
	 */
	public void setRecordVersion(String string){
		
		
		recordVersion = string;
	}

	/**
	 * @param string reserved
	 */
	public void setReserved(String string){
		
		reserved = string;
	}

	/**
	 * @param string vendorID
	 */
	public void setVendorID(String string){
		
		vendorID = string;
	}

}
