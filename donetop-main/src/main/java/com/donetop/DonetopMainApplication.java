package com.donetop;

import com.donetop.domain.entity.draft.Draft;
import com.donetop.repository.draft.DraftRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@SpringBootApplication
public class DonetopMainApplication {

	public static void main( String[] args ) {
		SpringApplication.run( DonetopMainApplication.class, args );
	}

//	@Bean
//	public CommandLineRunner testData(final DraftRepository draftRepository) {
//		return args -> {
//			List<Draft> drafts = new ArrayList<>();
//			LocalDateTime now = LocalDateTime.now();
//			for (int i = 0; i < 323; i++) {
//				Draft draft = new Draft().toBuilder()
//					.customerName("jin" + i)
//					.companyName("jin's company")
//					.email("jin@test.com")
//					.category("category")
//					.phoneNumber("010-0000-0000")
//					.price(1000 + i)
//					.address("address" + i)
//					.memo("memo" + i)
//					.password("password" + i)
//					.createTime(now)
//					.updateTime(now).build();
//				drafts.add(draft);
//				now = now.plusDays(1L);
//			}
//			draftRepository.saveAll(drafts);
//		};
//	}
}
