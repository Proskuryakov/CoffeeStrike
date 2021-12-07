package ru.vsu.cs.proskuryakov.coffeestrike.app.config;

import ma.glasnost.orika.MapperFacade;
import ma.glasnost.orika.MapperFactory;
import ma.glasnost.orika.converter.ConverterFactory;
import ma.glasnost.orika.converter.DefaultConverterFactory;
import ma.glasnost.orika.impl.DefaultMapperFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import ru.vsu.cs.proskuryakov.coffeestrike.api.models.Category;
import ru.vsu.cs.proskuryakov.coffeestrike.api.models.Drink;
import ru.vsu.cs.proskuryakov.coffeestrike.api.models.Product;
import ru.vsu.cs.proskuryakov.coffeestrike.api.models.Unit;
import ru.vsu.cs.proskuryakov.coffeestrike.db.domains.CategoryItem;
import ru.vsu.cs.proskuryakov.coffeestrike.db.domains.DrinkItem;
import ru.vsu.cs.proskuryakov.coffeestrike.db.domains.ProductItem;
import ru.vsu.cs.proskuryakov.coffeestrike.db.domains.UnitItem;

import javax.annotation.PostConstruct;

@Configuration
public class MapperFactoryConfig {

	private ConverterFactory converterFactory;
	private MapperFactory mapperFactory;
	private MapperFacade mapperFacade;

	@PostConstruct
	public void configurer() {
		converterFactory = new DefaultConverterFactory();
		mapperFactory = new DefaultMapperFactory.Builder().converterFactory(converterFactory).build();

		mapperFactory.classMap(Category.class, CategoryItem.class)
				.field("id", "categoryid").byDefault().register();
		mapperFactory.classMap(Drink.class, DrinkItem.class)
				.field("id", "drinkid").byDefault().register();
		mapperFactory.classMap(Product.class, ProductItem.class)
				.field("id", "productid").byDefault().register();
		mapperFactory.classMap(Unit.class, UnitItem.class)
				.field("id", "unitid").byDefault().register();

		mapperFacade = mapperFactory.getMapperFacade();
	}

	@Bean
	public ConverterFactory converterFactory() {
		return converterFactory;
	}

	@Bean
	public MapperFactory mapperFactory(ConverterFactory converterFactory) {
		return mapperFactory;
	}

	@Bean
	public MapperFacade mapperFacade(MapperFactory mapperFactory) {
		return mapperFacade;
	}

}
