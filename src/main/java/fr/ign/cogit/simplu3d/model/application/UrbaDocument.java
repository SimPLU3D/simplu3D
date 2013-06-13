package fr.ign.cogit.simplu3d.model.application;

import java.util.Date;

public class UrbaDocument {
  
  
  private Date approvalDate;
  private Date endDate;
  private String documentType;
  
  
  
  
  
  public UrbaDocument() {
    super();
  }



  public UrbaDocument(Date approvalDate, Date endDate, String documentType) {
    super();
    this.approvalDate = approvalDate;
    this.endDate = endDate;
    this.documentType = documentType;
  }
  
  
  
  public Date getApprovalDate() {
    return approvalDate;
  }
  public void setApprovalDate(Date approvalDate) {
    this.approvalDate = approvalDate;
  }
  public Date getEndDate() {
    return endDate;
  }
  public void setEndDate(Date endDate) {
    this.endDate = endDate;
  }
  public String getDocumentType() {
    return documentType;
  }
  public void setDocumentType(String documentType) {
    this.documentType = documentType;
  }
  
  
  
  
  
  
  
  
  
  

}
