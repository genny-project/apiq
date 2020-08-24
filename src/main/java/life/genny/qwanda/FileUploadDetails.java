package life.genny.qwanda;

import java.io.Serializable;


public class FileUploadDetails implements Serializable {

	/**
	 * 
	 */
	 static final long serialVersionUID = 1L;
	
	 String fileName;
	
	 String uploadedFilePath;
	
	 String uploadObjKey;
	
	 String uploadObjValue;
	
	 String userMetaDataObjKey;
	
	 String userMetaDataObjValue;

	/**
	 * @return the fileName
	 */
	public String getFileName() {
		return fileName;
	}

	/**
	 * @param fileName the fileName to set
	 */
	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	/**
	 * @return the uploadedFilePath
	 */
	public String getUploadedFilePath() {
		return uploadedFilePath;
	}

	/**
	 * @param uploadedFilePath the uploadedFilePath to set
	 */
	public void setUploadedFilePath(String uploadedFilePath) {
		this.uploadedFilePath = uploadedFilePath;
	}

	/**
	 * @return the uploadObjKey
	 */
	public String getUploadObjKey() {
		return uploadObjKey;
	}

	/**
	 * @param uploadObjKey the uploadObjKey to set
	 */
	public void setUploadObjKey(String uploadObjKey) {
		this.uploadObjKey = uploadObjKey;
	}

	/**
	 * @return the uploadObjValue
	 */
	public String getUploadObjValue() {
		return uploadObjValue;
	}

	/**
	 * @param uploadObjValue the uploadObjValue to set
	 */
	public void setUploadObjValue(String uploadObjValue) {
		this.uploadObjValue = uploadObjValue;
	}

	/**
	 * @return the userMetaDataObjKey
	 */
	public String getUserMetaDataObjKey() {
		return userMetaDataObjKey;
	}

	/**
	 * @param userMetaDataObjKey the userMetaDataObjKey to set
	 */
	public void setUserMetaDataObjKey(String userMetaDataObjKey) {
		this.userMetaDataObjKey = userMetaDataObjKey;
	}

	/**
	 * @return the userMetaDataObjValue
	 */
	public String getUserMetaDataObjValue() {
		return userMetaDataObjValue;
	}

	/**
	 * @param userMetaDataObjValue the userMetaDataObjValue to set
	 */
	public void setUserMetaDataObjValue(String userMetaDataObjValue) {
		this.userMetaDataObjValue = userMetaDataObjValue;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "FileUploadDetails [fileName=" + fileName + ", uploadedFilePath=" + uploadedFilePath + ", uploadObjKey="
				+ uploadObjKey + ", uploadObjValue=" + uploadObjValue + ", userMetaDataObjKey=" + userMetaDataObjKey
				+ ", userMetaDataObjValue=" + userMetaDataObjValue + "]";
	}

	public FileUploadDetails(String uploadObjKey, String uploadObjValue, String userMetaDataObjKey,
			String userMetaDataObjValue) {
		super();
		this.uploadObjKey = uploadObjKey;
		this.uploadObjValue = uploadObjValue;
		this.userMetaDataObjKey = userMetaDataObjKey;
		this.userMetaDataObjValue = userMetaDataObjValue;
	}

	public FileUploadDetails(String fileName, String uploadObjKey, String uploadObjValue, String userMetaDataObjKey,
			String userMetaDataObjValue) {
		super();
		this.fileName = fileName;
		this.uploadObjKey = uploadObjKey;
		this.uploadObjValue = uploadObjValue;
		this.userMetaDataObjKey = userMetaDataObjKey;
		this.userMetaDataObjValue = userMetaDataObjValue;
	}

	public FileUploadDetails() {
		super();
	}

	public FileUploadDetails(String uploadObjKey, String uploadObjValue) {
		super();
		this.uploadObjKey = uploadObjKey;
		this.uploadObjValue = uploadObjValue;
	}
	
	
	

}
