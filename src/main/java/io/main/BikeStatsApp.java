package io.main;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import io.data.DataSet;

@SpringBootApplication
@ComponentScan({ "io.controllers" })
public class BikeStatsApp {

	public static DataSet data;
	
	public static void main(String[] args) {
		data = new DataSet();
		SpringApplication.run(BikeStatsApp.class, args);
	}
}
