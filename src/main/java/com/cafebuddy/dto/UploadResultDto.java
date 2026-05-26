package com.cafebuddy.dto;

import java.util.List;

public class UploadResultDto {
    private int totalRows;
    private int imported;
    private int skipped;
    private List<String> errors;
    private List<CafeDto> cafes;

    public UploadResultDto() {}

    public int getTotalRows() { return totalRows; }
    public int getImported() { return imported; }
    public int getSkipped() { return skipped; }
    public List<String> getErrors() { return errors; }
    public List<CafeDto> getCafes() { return cafes; }

    public void setTotalRows(int totalRows) { this.totalRows = totalRows; }
    public void setImported(int imported) { this.imported = imported; }
    public void setSkipped(int skipped) { this.skipped = skipped; }
    public void setErrors(List<String> errors) { this.errors = errors; }
    public void setCafes(List<CafeDto> cafes) { this.cafes = cafes; }

    public static Builder builder() { return new Builder(); }

    public static class Builder {
        private int totalRows, imported, skipped;
        private List<String> errors;
        private List<CafeDto> cafes;

        public Builder totalRows(int totalRows) { this.totalRows = totalRows; return this; }
        public Builder imported(int imported) { this.imported = imported; return this; }
        public Builder skipped(int skipped) { this.skipped = skipped; return this; }
        public Builder errors(List<String> errors) { this.errors = errors; return this; }
        public Builder cafes(List<CafeDto> cafes) { this.cafes = cafes; return this; }

        public UploadResultDto build() {
            UploadResultDto dto = new UploadResultDto();
            dto.totalRows = this.totalRows; dto.imported = this.imported;
            dto.skipped = this.skipped; dto.errors = this.errors; dto.cafes = this.cafes;
            return dto;
        }
    }
}
