package com.url.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import com.url.controller.UrlMappingController;
import com.url.dtos.ClickEventDTO;
import com.url.dtos.UrlMappingDTO;
import com.url.models.UrlMapping;
import com.url.models.User;
import com.url.repository.ClickEventRepository;
import com.url.repository.UrlMappingRepository;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class UrlMappingService {

	private UrlMappingRepository urlMappingRepository;
	private ClickEventRepository clickEventRepository;

	public UrlMappingDTO createShortUrl(String originalUrl, User user) {
		String shortUrl=generateShortUrl();
		UrlMapping urlMapping=new UrlMapping();
		urlMapping.setOriginalUrl(originalUrl);
		urlMapping.setShortUrl(shortUrl);
		urlMapping.setUser(user);
		urlMapping.setCreatedDate(LocalDateTime.now());
		
		UrlMapping savedUrlMapping=urlMappingRepository.save(urlMapping);
		return convertToDto(urlMapping);
	}
	
//
	private UrlMappingDTO convertToDto(UrlMapping urlMapping)
	{
		UrlMappingDTO urlMappingDTO=new UrlMappingDTO();
		urlMappingDTO.setId(urlMapping.getId());
		urlMappingDTO.setOriginalUrl(urlMapping.getOriginalUrl());
		urlMappingDTO.setShortUrl(urlMapping.getShortUrl());
		urlMappingDTO.setClickCount(urlMapping.getClickCount());
		urlMappingDTO.setCreatedDate(urlMapping.getCreatedDate());
		urlMappingDTO.setUsername(urlMapping.getUser().getUsername());
		
		return urlMappingDTO;
	}
	
//	
	private static String generateShortUrl() {
		String characters="ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
		Random random=new Random();
		StringBuilder shortUrl=new StringBuilder(8);
		
		for(int i=0; i<8;i++)
		{
			shortUrl.append(characters.charAt(random.nextInt(characters.length())));
		}
		return shortUrl.toString();
	}
	
////
	public List<UrlMappingDTO> getUrlsByUser(User user) {
		
		return urlMappingRepository.findByUser(user).stream()
				.map(this::convertToDto)
				.toList(); //==>//collect(Collectors.toUnmodifiableList());	
										
	}
	
////////
	public List<ClickEventDTO> getClickEventsByDate(String shortUrl, LocalDateTime start, LocalDateTime end) {
		UrlMapping urlMapping=urlMappingRepository.findByShortUrl(shortUrl);
		if(urlMapping != null)
		{
			return clickEventRepository.findByUrlMappingAndClickDateBetween(urlMapping, start, end)
					.stream()
					.collect(Collectors.groupingBy(click -> click.getClickDate().toLocalDate(),
					Collectors.counting()))
					.entrySet().stream()
					.map(entry -> {
						ClickEventDTO clickEventDTO=new ClickEventDTO();
						clickEventDTO.setClickDate(entry.getKey());
						clickEventDTO.setCount(entry.getValue());
						return clickEventDTO;
					})
					.collect(Collectors.toList());
		}
		return null;
	}
	
}
