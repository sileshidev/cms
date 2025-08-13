package com.cms.contraband.service.controller;

import com.cms.contraband.service.model.Contraband;
import com.cms.contraband.service.model.ContrabandRepository;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/api/contraband")
@RequiredArgsConstructor
public class ContrabandController {

	private final ContrabandRepository repository;

	@PostMapping
	public Contraband create(@RequestBody @Valid Contraband c) {
		c.setContrabandCode("CB-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase());
		if (c.getSeizureTime() == null) {
			c.setSeizureTime(Instant.now());
		}
		if (c.getStatus() == null) {
			c.setStatus("REGISTERED");
		}
		Contraband saved = repository.save(c);
		try {
			String json = "{" +
				"\"timestamp\":\"" + Instant.now().toString() + "\"," +
				"\"actor\":\"system\"," +
				"\"action\":\"CONTRABAND_CREATED\"," +
				"\"entityType\":\"Contraband\"," +
				"\"entityId\":\"" + saved.getContrabandCode() + "\"," +
				"\"details\":\"Created contraband record\"}";
			HttpClient.newHttpClient().send(
				HttpRequest.newBuilder(URI.create("http://localhost:8083/api/audit"))
					.header("Content-Type", "application/json")
					.POST(HttpRequest.BodyPublishers.ofString(json, StandardCharsets.UTF_8))
					.build(),
				HttpResponse.BodyHandlers.ofString());
		} catch (Exception ignored) {}
		return saved;
	}

	@GetMapping
	public List<Contraband> list() { return repository.findAll(); }

	@GetMapping("/{code}")
	public ResponseEntity<Contraband> getByCode(@PathVariable("code") @NotBlank String code) {
		return repository.findByContrabandCode(code)
				.map(ResponseEntity::ok)
				.orElse(ResponseEntity.notFound().build());
	}

	@PatchMapping("/{id}")
	public ResponseEntity<Contraband> update(@PathVariable Long id, @RequestBody Map<String, Object> fields) {
		return repository.findById(id).map(c -> {
			if (fields.containsKey("status")) c.setStatus((String) fields.get("status"));
			if (fields.containsKey("assignedStorage")) c.setNotes((c.getNotes()==null?"":""+c.getNotes()+"\n") + "Storage: " + fields.get("assignedStorage"));
			return ResponseEntity.ok(repository.save(c));
		}).orElse(ResponseEntity.notFound().build());
	}
}
