package com.donetop.common.config;

import com.donetop.domain.entity.category.Category;
import com.donetop.repository.category.CategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

import static com.donetop.common.Profile.*;

@Profile(value = {TEST})
@Configuration
@RequiredArgsConstructor
public class CategoryConfig {

	private final CategoryRepository categoryRepository;

	@Bean
	@Transactional
	public CommandLineRunner init() {
		return args -> {

			init(
				"현수막",
				1,
				List.of(
					"선거전문현수막", "게릴라현수막", "법정게시대",
					"족자현수막", "차량용현수막", "유포지(썬팅용)",
					"소형현수막", "깃발/어깨띠"
				)
			);

			init(
				"배너",
				2,
				List.of(
					"현수막배너", "펫트배너",
					"미니배너", "대형배너"
				)
			);

			init(
				"유포지실사",
				3,
				List.of()
			);

			init(
				"전단지",
				4,
				List.of(
					"단면/양면전단", "소량전단",
					"문어발전단", "문고리전단"
				)
			);

			init(
				"명함",
				5,
				List.of(
					"일반지명함", "수입지명함",
					"카드명함", "쿠폰명함"
				)
			);

			init(
				"스티커/자석",
				6,
				List.of(
					"비규격스티커", "규격스티커", "모양스티커(규격)",
					"모양스티커(비규격)", "종이자석", "차량자석"
				)
			);

			init(
				"카달로그",
				7,
				List.of(
					"음식점", "교회", "은행",
					"유치원", "스포츠", "학원",
					"판매", "학교", "회사",
					"선거홍보물"
				)
			);

			init(
				"봉투",
				8,
				List.of(
					"컬러(대/중/소)", "흑백(대/중/소)"
				)
			);

			init(
				"양식ㆍ서식지/영수증",
				9,
				List.of(
					"양식/서식(낱장)", "양식/서식(제본)", "NCR지(2조/3조)",
					"영수증"
				)
			);

			init(
				"아크릴/포멕스 간판",
				10,
				List.of(
					"아크릴", "포멕스", "표찰/명찰"
				)
			);

		};
	}

	private void init(final String parentName, final int sequence, final List<String> childNames) {
		final Category parent = getOrSave(parentName, sequence);
		for (int i = 0; i < childNames.size(); i++) {
			final Category category = getOrSave(childNames.get(i), i + 1);
			if (category.getParent() == null) {
				category.setParent(parent);
				categoryRepository.save(category);
			}
		}
	}

	private Category getOrSave(final String name, final int sequence) {
		final Optional<Category> categoryOptional = categoryRepository.findByName(name);
		return categoryOptional.orElseGet(() -> categoryRepository.save(Category.of(name, sequence)));
	}

}