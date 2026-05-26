package com.cafebuddy.service;

import com.cafebuddy.dto.CafeDto;
import com.cafebuddy.dto.UploadResultDto;
import com.cafebuddy.exception.ExcelParseException;
import com.cafebuddy.model.Cafe;
import com.cafebuddy.repository.BookingRepository;
import com.cafebuddy.repository.CafeRepository;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.Instant;
import java.util.*;

@Service
public class CafeService {

    private static final Logger logger = LoggerFactory.getLogger(CafeService.class);

    @Autowired
    private CafeRepository cafeRepository;
    
    @Autowired
    private BookingRepository bookingRepository;

    private static final Map<String, String> ALIASES = new HashMap<>();
    static {
        ALIASES.put("cafe name", "name"); ALIASES.put("cafe", "name");
        ALIASES.put("neighbourhood", "area"); ALIASES.put("neighborhood", "area");
        ALIASES.put("location", "area"); ALIASES.put("latitude", "lat");
        ALIASES.put("longitude", "lng"); ALIASES.put("long", "lng");
    }

    public List<CafeDto> getAllCafes() {

        List<CafeDto> cafes = cafeRepository.findAll().stream().map(this::toDto).toList();

        Instant now = Instant.now();

        cafes.forEach(cafe -> {
            Integer active =
                    bookingRepository.countActivePeople(cafe.getId(), now);

            cafe.setHere(active);
        });

        return cafes;
    }

    public Optional<CafeDto> getCafeById(Long id) {
        return cafeRepository.findById(id).map(this::toDto);
    }

    public List<CafeDto> searchCafes(String query) {
        return cafeRepository
                .findByNameContainingIgnoreCaseOrAreaContainingIgnoreCase(query, query)
                .stream().map(this::toDto).toList();
    }

    public CafeDto addCafe(CafeDto dto) {
        Cafe cafe = Cafe.builder()
                .name(dto.getName()).area(dto.getArea()).address(dto.getAddress())
                .lat(dto.getLat()).lng(dto.getLng()).mood(dto.getMood())
                .wifi(dto.getWifi()).outlets(dto.getOutlets()).here(0).build();
        return toDto(cafeRepository.save(cafe));
    }

    public CafeDto updateCafe(Long id, CafeDto dto) {
        Cafe cafe = cafeRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Cafe not found: " + id));
        cafe.setName(dto.getName()); cafe.setArea(dto.getArea());
        cafe.setAddress(dto.getAddress()); cafe.setLat(dto.getLat());
        cafe.setLng(dto.getLng()); cafe.setMood(dto.getMood());
        cafe.setWifi(dto.getWifi()); cafe.setOutlets(dto.getOutlets());
        return toDto(cafeRepository.save(cafe));
    }

    public void deleteCafe(Long id) {
        if (!cafeRepository.existsById(id)) throw new RuntimeException("Cafe not found: " + id);
        cafeRepository.deleteById(id);
    }

    public UploadResultDto importFromExcel(MultipartFile file) {
        validateFile(file);
        List<String> errors = new ArrayList<>();
        List<Cafe> toSave = new ArrayList<>();
        int totalRows = 0;
        try (Workbook workbook = openWorkbook(file)) {
            Sheet sheet = workbook.getSheetAt(0);
            Map<String, Integer> headerIndex = parseHeader(sheet.getRow(0));
            for (int rowNum = 1; rowNum <= sheet.getLastRowNum(); rowNum++) {
                Row row = sheet.getRow(rowNum);
                if (row == null || isBlankRow(row)) continue;
                totalRows++;
                try {
                    toSave.add(rowToCafe(row, headerIndex));
                } catch (Exception e) {
                    errors.add("Row " + (rowNum + 1) + ": " + e.getMessage());
                    logger.warn("Skipping row {}: {}", rowNum + 1, e.getMessage());
                }
            }
            List<Cafe> saved = cafeRepository.saveAll(toSave);
            return UploadResultDto.builder().totalRows(totalRows).imported(saved.size())
                    .skipped(errors.size()).errors(errors)
                    .cafes(saved.stream().map(this::toDto).toList()).build();
        } catch (IOException e) {
            throw new ExcelParseException("Could not read the Excel file: " + e.getMessage());
        }
    }

    private void validateFile(MultipartFile file) {
        if (file == null || file.isEmpty()) throw new ExcelParseException("Please upload a non-empty Excel file.");
        String name = Objects.requireNonNullElse(file.getOriginalFilename(), "").toLowerCase();
        if (!name.endsWith(".xlsx") && !name.endsWith(".xls")) throw new ExcelParseException("Only .xlsx and .xls files are supported.");
    }

    private Workbook openWorkbook(MultipartFile file) throws IOException {
        String name = Objects.requireNonNullElse(file.getOriginalFilename(), "").toLowerCase();
        return name.endsWith(".xls") ? new HSSFWorkbook(file.getInputStream()) : new XSSFWorkbook(file.getInputStream());
    }

    private Map<String, Integer> parseHeader(Row header) {
        if (header == null) throw new ExcelParseException("The Excel file has no header row.");
        Map<String, Integer> index = new HashMap<>();
        for (Cell cell : header) {
            String raw = cellString(cell).trim().toLowerCase();
            if (raw.isBlank()) continue;
            index.put(ALIASES.getOrDefault(raw, raw), cell.getColumnIndex());
        }
        if (!index.containsKey("name")) throw new ExcelParseException("Header must contain a 'name' column.");
        if (!index.containsKey("area")) throw new ExcelParseException("Header must contain an 'area' column.");
        return index;
    }

    private Cafe rowToCafe(Row row, Map<String, Integer> idx) {
        return Cafe.builder()
                .name(required(row, idx, "name", "Cafe name"))
                .area(required(row, idx, "area", "Area"))
                .address(optional(row, idx, "address"))
                .lat(optionalDouble(row, idx, "lat")).lng(optionalDouble(row, idx, "lng"))
                .mood(normalise(optional(row, idx, "mood"), List.of("Quiet","Heads-down","Mixed","Chatty"), "Mixed"))
                .wifi(normalise(optional(row, idx, "wifi"), List.of("Fast","Decent","Spotty"), "Decent"))
                .outlets(normalise(optional(row, idx, "outlets"), List.of("Plenty","Some","Few"), "Some"))
                .here(0).build();
    }

    private String required(Row row, Map<String, Integer> idx, String col, String label) {
        String val = optional(row, idx, col);
        if (val == null || val.isBlank()) throw new IllegalArgumentException(label + " is required.");
        return val.trim();
    }

    private String optional(Row row, Map<String, Integer> idx, String col) {
        if (!idx.containsKey(col)) return null;
        Cell cell = row.getCell(idx.get(col));
        return cell == null ? null : cellString(cell).trim();
    }

    private Double optionalDouble(Row row, Map<String, Integer> idx, String col) {
        if (!idx.containsKey(col)) return null;
        Cell cell = row.getCell(idx.get(col));
        if (cell == null) return null;
        try {
            if (cell.getCellType() == CellType.NUMERIC) return cell.getNumericCellValue();
            String s = cell.toString().trim();
            return s.isBlank() ? null : Double.parseDouble(s);
        } catch (NumberFormatException e) { return null; }
    }

    private String cellString(Cell cell) {
        if (cell == null) return "";
        return switch (cell.getCellType()) {
            case STRING -> cell.getStringCellValue();
            case NUMERIC -> { double d = cell.getNumericCellValue(); yield (d == Math.floor(d)) ? String.valueOf((long) d) : String.valueOf(d); }
            case BOOLEAN -> String.valueOf(cell.getBooleanCellValue());
            case FORMULA -> cell.toString();
            default -> "";
        };
    }

    private String normalise(String raw, List<String> allowed, String fallback) {
        if (raw == null || raw.isBlank()) return fallback;
        return allowed.stream().filter(a -> a.equalsIgnoreCase(raw)).findFirst().orElse(fallback);
    }

    private boolean isBlankRow(Row row) {
        for (Cell cell : row) { if (cell.getCellType() != CellType.BLANK && !cellString(cell).isBlank()) return false; }
        return true;
    }

    private CafeDto toDto(Cafe c) {
        return CafeDto.builder().id(c.getId()).name(c.getName()).area(c.getArea())
                .address(c.getAddress()).lat(c.getLat()).lng(c.getLng())
                .mood(c.getMood()).wifi(c.getWifi()).outlets(c.getOutlets()).here(c.getHere()).build();
    }
}
