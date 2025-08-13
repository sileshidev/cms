package com.cms.contraband.service.model;

import jakarta.persistence.*;

import java.time.Instant;

@Entity
public class Contraband {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true)
    private String contrabandCode;

    private String type;
    private String category;
    private Double quantity;
    private String unit;
    private String serialNumber;

    private Instant seizureTime;
    private Double latitude;
    private Double longitude;
    private String seizedBy;
    private String agency;
    private String notes;

    private String status; // REGISTERED, IN_STORAGE, TRANSFER_PENDING, DESTROY_PENDING

    public Contraband() {}

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getContrabandCode() { return contrabandCode; }
    public void setContrabandCode(String contrabandCode) { this.contrabandCode = contrabandCode; }

    public String getType() { return type; }
    public void setType(String type) { this.type = type; }

    public String getCategory() { return category; }
    public void setCategory(String category) { this.category = category; }

    public Double getQuantity() { return quantity; }
    public void setQuantity(Double quantity) { this.quantity = quantity; }

    public String getUnit() { return unit; }
    public void setUnit(String unit) { this.unit = unit; }

    public String getSerialNumber() { return serialNumber; }
    public void setSerialNumber(String serialNumber) { this.serialNumber = serialNumber; }

    public Instant getSeizureTime() { return seizureTime; }
    public void setSeizureTime(Instant seizureTime) { this.seizureTime = seizureTime; }

    public Double getLatitude() { return latitude; }
    public void setLatitude(Double latitude) { this.latitude = latitude; }

    public Double getLongitude() { return longitude; }
    public void setLongitude(Double longitude) { this.longitude = longitude; }

    public String getSeizedBy() { return seizedBy; }
    public void setSeizedBy(String seizedBy) { this.seizedBy = seizedBy; }

    public String getAgency() { return agency; }
    public void setAgency(String agency) { this.agency = agency; }

    public String getNotes() { return notes; }
    public void setNotes(String notes) { this.notes = notes; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
}
