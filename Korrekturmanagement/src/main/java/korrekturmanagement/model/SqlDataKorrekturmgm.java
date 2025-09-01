package korrekturmanagement.model;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import jakarta.persistence.*;

@Entity
@Table(name = "korrekturen")
public class SqlDataKorrekturmgm {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(name = "erstellt_am", nullable = false, updatable = false)
	private LocalDateTime erstelltAm;

	@Column(name= "bearbeitet_am", nullable = false)
	private LocalDateTime bearbeitetAm;

	@Column(name= "error_text",nullable = false)
	private String errorText;

	@Column(nullable = false)
	private String person;

	@Lob
	@Column(name = "dokument", columnDefinition = "LONGBLOB")
	private byte[] uploadDokument;

	@Column(name = "datei_name")
	private String dateiName;

	@Column(name = "kategorie")
	private String kategorie;

	@Column(name = "priorisierung")
	private String priorisierung;

	@Column(name = "medienbezug")
	private String medienbezug;

	@Column(name = "bearbeitungs_status")
	private String bearbeitungsStatus;

	// --- Lifecycle Callbacks ---

	@PrePersist
	protected void onCreate() {
		erstelltAm = LocalDateTime.now();
		bearbeitetAm = LocalDateTime.now();
		setBearbeitungsStatus("Neu");
	}
	

	@PreUpdate
	protected void onUpdate() {
		bearbeitetAm = LocalDateTime.now();
	}

	// --- Konstruktoren ---
	public SqlDataKorrekturmgm() {
	}

	public SqlDataKorrekturmgm(String errorText, String person) {
		this.errorText = errorText;
		this.person = person;
	}

	// --- Getter / Setter ---
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getErrorText() {
		return errorText;
	}

	public void setErrorText(String text) {
		this.errorText = text;
	}

	public String getPerson() {
		return person;
	}

	public void setPerson(String person) {
		this.person = person;
	}

	public String getDateiName() {
		return dateiName;
	}

	public void setDateiName(String dateiName) {
		this.dateiName = dateiName;
	}

	public byte[] getPdfDokument() {
		return uploadDokument;
	}

	public void setPdfDokument(byte[] uploadDokument) {
		this.uploadDokument = uploadDokument;
	}

	public LocalDateTime getErstelltAm() {
		return erstelltAm;
	}

	public LocalDateTime getBearbeitetAm() {
		return bearbeitetAm;
	}

	public String getKategorie() {
		return kategorie;
	}

	public void setKategorie(String kategorie) {
		this.kategorie = kategorie;
	}

	public String getPriorisierung() {
		return priorisierung;
	}

	public void setPriorisierung(String priorisierung) {
		this.priorisierung = priorisierung;
	}

	public String getMedienbezug() {
		return medienbezug;
	}

	public void setMedienbezug(String medienbezug) {
		this.medienbezug = medienbezug;
	}

	public String getBearbeitungsStatus() {
		return bearbeitungsStatus;
	}

	public void setBearbeitungsStatus(String bearbeitungsStatus) {
		this.bearbeitungsStatus = bearbeitungsStatus;
	}
	
	public String getErstelltAmFormatted() {
	    return erstelltAm != null 
	        ? erstelltAm.format(DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm")) 
	        : "";
	}

	public String getBearbeitetAmFormatted() {
	    return bearbeitetAm != null 
	        ? bearbeitetAm.format(DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm")) 
	        : "";
	}

}
