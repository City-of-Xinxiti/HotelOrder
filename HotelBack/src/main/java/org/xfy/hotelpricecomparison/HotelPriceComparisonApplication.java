package org.xfy.hotelpricecomparison;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@MapperScan("org.xfy.hotelpricecomparison.mapper")  // 扫描Mapper接口
@EnableScheduling  // 启用定时任务
public class HotelPriceComparisonApplication {

	public static void main(String[] args) {
		SpringApplication.run(HotelPriceComparisonApplication.class, args);
	}

}
